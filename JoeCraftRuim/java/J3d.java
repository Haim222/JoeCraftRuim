// J3D: para objetos 3d poligonais.
// dica: 
// 		um tri com '&' em tg0 o transforma em triangulo de caixa de colisao
//		CUIDADO: nem todo modo de impressao j3d tem isso implementado
//		tris de colisao nao sao impressos, mas avaliam o hit do mouse normalmente. Eles nao trabalham com alfa p nao consumir clock
//    basta atribuir tag "&blabla" a ele. No editor, use ALT+ENTER
import java.awt.Graphics;
import java.io.File;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;// p sort
import java.util.Iterator;
import java.awt.Image;
import java.awt.Color;


public class J3d{	
	// a proxima versao deve salvar o tamanho das esferas embutidas na classe de tris
	// tb tris com hide e paletas proprias JPal(ao menos a semente delas)
	int versao = 7; 
	// static Graphics g=null;
	static boolean opEco=false; // escreve o nome do arquivo quando se carrega o mesmo
		ArrayList<Ponto> pnt = new ArrayList<>();
		ArrayList<Triang> tri = new ArrayList<>();
		ArrayList<Desloc> des = new ArrayList<>();
		ArrayList<Girad> gir = new ArrayList<>();
			float cos3=0, sin3=0, ang3=0;// angulo geral, nao tente deslocar diretamente, use "defGiroGeral()"
			float incX=0, incY=0, incZ=0;// deslocamento geral
			final float VOLTA =(float)(-Math.PI*2);
		String camJ3d=null, // bom p mensagem de eixo perdido
			nome=null; // facilitou p um esquema de arr lst e busca por este string
		boolean 
			opContorno=false, opAramado=false,			
			opSemHit = false, // usei na bigorna de JoeCraft, nunca da "true" no hit do mouse.
			opGirarPSel=false, // ignorar tags e girar apenas pontos selecionados
			temRem=false; // p pontos e tris, nao p movimentos		
		Ponto lastPnt = null; // ?tah coerente lah p baixo??? Tomara q sim.
		Triang lastTri = null;
		
		static int crSomb=16;
		static int opSomb=0, opOscFig=0; // p efeitos de aura
		int iPal=0, cCint=0, crCint=12; // efeitos de hurtMob
		// mecanismo p atrasar a operacao de giradores (?jah foi extendido p deslocadores?). Resetado a cada "giro@()" ou "giro@r()" chamado, mas nao a cada criacao de obj giro. Cuidado.
		// o segundo eh p atraso de retorno
		int opAtraso=0, opAtrasoo=0; 
		int crAnt=-1, crNov=-1, crAntt=-1, crNovv=-1;
		Color ccrNov=null, ccrNovv=null; // esse eh p paleta, usa a deteccao por int, mas atribui com color
		int e=0; 
		float 
			zoom=1f, 
			altSomb=0, 
			tamSomb=0.5f; // tamanho da projecao da sombra a oeste. Zero = sol a pino

	public void defIPalts(int i){
		// sombrearParaPaletas()
		// este i eh um incremento geral, p todo tri
		// este metodo define o decremento de paleta JPal de acordo com o sombreamento de J.cor[] usado.
		// ex: 
		//    J.cor[89] define o decremento p zero
		// 		J.cor[88] define o decremento p um
		// veja q eh um preparo p impressao com paletas
		// isso deu um efeito muito bom no item na mao do joe em joeCraft
		// usar isso quando acabar de carregar o arq j3d
		for(Triang t:tri)
			t.iPalt = t.crt % 10 - 9 +i;
	}
	public void copyPals(J3d p){
		int lm = p.tri.size();
		if(lm>tri.size()){
			lm = tri.size();
			J.esc("aviso: numero de triangulos diferente na copia de paletas: |"+p.camJ3d+"| para |"+camJ3d);
		}
		for(int q=0; q<lm; q++){
			tri.get(q).pal = p.tri.get(q).pal;
			tri.get(q).iPalt = p.tri.get(q).iPalt;
		}
	}	
			
	public void zeraTudo(){
		pnt.clear();
		tri.clear();
		des.clear();
		gir.clear();
		temRem = false;
		camJ3d = null;
		zoom = 1;
		crAnt = -1; crNov=-1;
		crAntt = -1; crNovv=-1;
		iPal = 0;
		opContorno = false;
		opAramado = false;
	}		
			
	public void reset(){
		zeraTudo();
		carrJ3d(camJ3d);
	}
	public boolean saveJ3d(String cam){
		// verRems();
		cam = J.corrCam(cam,"j3d");
		RandomAccessFile arq = null;
		try{
			arq = new RandomAccessFile( new File(cam),"rw");
			arq.writeInt(versao);
			arq.writeInt(pnt.size());
			arq.writeInt(tri.size());

			for(Ponto p:pnt){					
				// arq.writeFloat(p.x*200);
				// arq.writeFloat(p.y*200);
				// arq.writeFloat(p.z*200);
				arq.writeFloat(p.x);
				arq.writeFloat(p.y);
				arq.writeFloat(p.z);
				arq.writeChar(p.tg0);
				arq.writeChar(p.tg1);
				arq.writeChar(p.tg2);
				arq.writeChar(p.tg3);
				arq.writeChar(p.tg4);
				arq.writeChar(p.tg5);
				arq.writeChar(p.tg6);
			}
			for(Triang t:tri){
				arq.writeInt(t.id);
				arq.writeInt(t.crt);
				arq.writeInt(t.crc);
				arq.writeInt(t.p1.id);
				arq.writeInt(t.p2.id);
				arq.writeInt(t.p3.id);
				arq.writeChar(t.tg0);
				arq.writeChar(t.tg1);
				arq.writeChar(t.tg2);
				arq.writeChar(t.tg3);
				arq.writeChar(t.tg4);
				arq.writeChar(t.tg5);
				arq.writeChar(t.tg6);
			}				
			cam=cam;// bom p mensagem de eixo perdido			
			return true;
		} catch(IOException e){
			J.impErr("falha salvando arq 3D: "+cam,"saveJ3d()");
			return false;
		}

	}
	public boolean openJ3d(String cam){
		zeraTudo();
		return addArq(cam, null, null);
	}
	public boolean carrJ3d(String cam){
		zeraTudo();
		return addArq(cam, null, null);
	}
	public J3d(){
		zeraTudo();		
		// evitou um bug
		giraTudo(0);// garantia
	}
	public J3d(String cam){
		zeraTudo();
		carrJ3d(cam);
		giraTudo(0);// garantia		
	}
	public J3d(String cam, float r){
		zeraTudo();
		carrJ3d(cam);
		resize(r);
	}
	public J3d(String cam, float r, String op){
		zeraTudo();
		carrJ3d(cam);
		resize(r);
		
		// presume-se nor como padrao

		selAllPnt();

		// "u", "v" e "w" seriam p inversao
		// implementar outros depois, se precisar
		if(J.tem('u',op)){ 
			inverter('X');
		}
		
		if(J.tem('4',op)){
			float ang90 = (float)(Math.PI*0.5f);
			J3d w = null;
			
			w = new J3d(cam,r);
			w.giroY(null,null,ang90,1);
			w.reg3d();			
			addComp(w,0,0,0);
			
			w = new J3d(cam,r);
			w.giroY(null,null,ang90+ang90,1);
			w.reg3d();						
			addComp(w,0,0,0);
			
			w = new J3d(cam,r);
			w.giroY(null,null,ang90+ang90+ang90,1);
			w.reg3d();						
			addComp(w,0,0,0);			
			
			deselAllPnt();
			return;
		}
		
		// sul
		if(J.tem('s',op)){ 
			rodar90('Y');
			rodar90('Y');
		}
		
		// les
		if(J.tem('l',op)) rodar90('Y');
		
		// oes
		if(J.tem('o',op)) rodar90('y');		
		
		// cma
		if(J.tem('c',op)) rodar90('X');				
		
		// bxo
		if(J.tem('b',op)) rodar90('x');						

		deselAllPnt();
	}	
	public boolean addArq(String cam){
		return addArq(cam, null, null);
	}
	public Ponto procEixo(String tag){
		Ponto q = null;
		tag = J.emMaiusc(tag+"       ");
		for(Ponto p:pnt){
			if(q==null){
				if(p.temTag(tag)) 
					q = p;
			}
		}
		//if(q==null) J.esc("AVISO: eixo perdido: "+tag+"   J3d.procEixo()");
		return q;
	}
	public Triang procTri(String tag){
		tag = J.emMaiusc(tag+"       ");
		for(Triang t:tri)
			if(t.temTag(tag)) 
				return t;		
		J.impErr("triangulo perdido: "+tag,"J3d.procTri()");
		return null;
	}	
	
	
	public boolean temPontoSel(){
		for(Ponto p:pnt)
			if(p.pSel) return true;
		return false;
	}
	public void rodar90(char ca){
		// soh roda pontos selecionados
		// se nao tiver nenhum ponto selecionado, roda tudo
		// nao sei se precisarah das outras direcoes
		
		boolean dSelDepois = false;
		if(!temPontoSel()){
			dSelDepois = true;
			selAllPnt();
		}
		
		float aux = 0;
		for(Ponto p:pnt)
		if(p.pSel)
		switch(ca){			
			case 'X':
				aux = p.y;
				p.y = p.z;
				p.z = -aux;
				break;
			case 'x':
				aux = p.y;
				p.y = -p.z;
				p.z = aux;
				break;				
			case 'Y':
				aux = p.x;
				p.x = p.z;
				p.z = -aux;
				break;
			case 'y':
				aux = p.x;
				p.x = -p.z;
				p.z = aux;
				break;
			default: 
				J.impErr("!direcao de rotacao ainda nao implementada: |"+ca+"|", "J3d.rodar90()");
				break;
		}
		if(dSelDepois) deselAllPnt();
	}
	
	public void inverter(char ca){ // espelhar???
		ca = J.emMaiusc(ca);
		if(!J.vou(ca,'X','Y','Z')) {
			J.impErr("parametro de inversao incorreto: '"+ca+"'", "j3d.inverter()");
			J.sai();
		}
		for(Ponto p:pnt){
			if(ca=='X') p.x=-p.x;
			if(ca=='Y') p.y=-p.y;
			if(ca=='Z') p.z=-p.z;
		}	
	}

	public boolean addArq(String cam, String tagAlca, String tagR){
		// verRems();		
		// addComp
		// se pIns==null, entao o arquivo esta sendo simplesmente aberto
		// se pIns!=null, significa q eh uma alca de insercao, logo, o arquivo esta sendo adicionado neste ponto, o arq esta sendo SOMADO ao ja existente
		// se tagR!=null, os pontos serao retagueados assim q abertos
		cam = J.corrCam(cam,"j3d");
		// if(!J.tem('.',cam)) cam+=".j3d";
		// if(!J.tem('\\',cam)) cam = "j3d\\"+cam;
		
		if(!J.arqExist(cam)) {
			J.impErr("arquivo 3D perdido: "+cam,"addArq()");
			J.sai();
			return false;
		}
		float addX=0, addY=0, addZ=0;
		int pBase=0, tBase=0;
		
		// MUITO IMPORTANTE PQ RESET sin3 e cos3
		// jamais faça sin3=0 ou cos3=0 manualmente
  	giraTudo(0); // ao norte, inicial, como no editor		
		if(tagAlca==null){
			pnt.clear();
			tri.clear();
			des.clear();
			gir.clear();					
		}	else {	
			Ponto p = procEixo(tagAlca);
			if(p==null){
				J.impErr("eixo nao encontrado: "+cam+"::"+tagAlca,"J3d.addArq()");
				System.exit(0);
			} else {
				addX=p.x*200;
				addY=p.y*200;
				addZ=p.z*200;
			}
			pBase=pnt.size();
			tBase=tri.size();
		}
		RandomAccessFile arq = null;
		camJ3d=cam;// p mensagem de eixo perdido
		try{
			arq = new RandomAccessFile( new File(cam),"r");
			int versaoLida = (int)arq.readInt();
			if(versaoLida!=versao){
				J.impErr("CUIDADO!!! 'J3d.java:versao' diferente da versao do arquivo q esta sendo aberto!","J3d.add3d()");
				J.impErr("versao do editor3d e J3d.java estao dessincronizados","");
			}
			int np = (int)arq.readInt();
			int nt = (int)arq.readInt();
			
			int i=0, j=0, k=0, d=0;
			Ponto p;
			for(int q=0; q<np; q++){					
				p = new Ponto(0,0,0);
				p.x = arq.readFloat()+addX; 
				p.y = arq.readFloat()+addY; 
				
				p.z = arq.readFloat()+addZ;
				p.id=q+pBase;
				if(false)
				{
					p.x/=200f;
					p.y/=200f;
					p.z/=200f;
				}
				p.tg0 = (char) arq.readChar();
				p.tg1 = (char) arq.readChar();
				p.tg2 = (char) arq.readChar();
				p.tg3 = (char) arq.readChar();
				p.tg4 = (char) arq.readChar();
				p.tg5 = (char) arq.readChar();
				p.tg6 = (char) arq.readChar();
				
				if(tagR!=null) p.setTag(tagR);
				
				pnt.add(p);
			}

			Triang t = null;
			int ct=0, cc=0, c=0;
			
			if(tagAlca!=null) c=tri.size();
			
			for(int q=0; q<nt; q++){
				d = (int)arq.readInt();
				d = c++;
				ct= (int)arq.readInt();// cores
				cc= (int)arq.readInt();
				i = (int)arq.readInt()+pBase;// ind d pontos
				j = (int)arq.readInt()+pBase;
				k = (int)arq.readInt()+pBase;
				
				t = new Triang(ct,cc, pnt.get(i),pnt.get(j),pnt.get(k));

				t.id=d+tBase;				
				t.tg0 = (char) arq.readChar();
				t.tg1 = (char) arq.readChar();
				t.tg2 = (char) arq.readChar();
				t.tg3 = (char) arq.readChar();
				t.tg4 = (char) arq.readChar();
				t.tg5 = (char) arq.readChar();
				t.tg6 = (char) arq.readChar();
				tri.add(t);					
			}				
			// calcZord();
			arq.close();
			if(opEco)	J.esc("ok: "+cam);
			return true;
		} catch(IOException e){
			J.impErr("falha abrindo arq 3D: "+cam,"addArq()");
			try{ arq.close(); } catch(IOException ee) {}
			return false;
		}
	}
	public void remComp(String tag){
		int c=0;
		// parece q tem um bug aqui
		// nao pode deletar pnt de outros tris
		for(Triang t:tri){
			c=0;
			if(t.p1.temTag(tag)) {t.p1.id=-1;c++;}
			if(t.p2.temTag(tag)) {t.p2.id=-1;c++;}
			if(t.p3.temTag(tag)) {t.p3.id=-1;c++;}
			if(c==3) t.id=-1;
		}
		for(Ponto p:pnt){
			if(p.temTag(tag)) p.id=-1;
		}
		temRem=true; // flag para "reg3d()" q avisa p percorrer a lista em busca de remocoes
	}	
	public void reg3d(){
		if(cCint>0) cCint--;
		if(des.size()>0) regDess();
		if(gir.size()>0) regGirs();
		if(temRem) verRems();
	}
	public void linkar(J3d p, String ttag){
		ttag = J.emMaiusc(ttag);
		int c=0;
		for(Triang t:tri)
			if(t.temTag(ttag)){
				t.lnk = p;
				c++;
			}	
		if(c<=0) J.impErr("!Nao foi encontrado o triangulo requerido na linkagem: "+ttag, "J3d.linkar()");
		else J.esc("triangulos linkados em "+camJ3d+": "+c);
	}
	
	
	public void verRems(){ // deleta pontos e tris com ID = -1
		// bug possivel!
		// cuidado! Possibilidade de deletar pontos componentes de tris
		// atribuir "e=-1" com essa precaucao
		// o ponto pode ficar fora da listagem e um vertice do tri pode ficar inacessivel
		// parece ser melhor nao deletar pontos, apenas isolah-los
		temRem=false;
		Iterator<Triang> it = tri.iterator();
		while(it.hasNext()){
			if (it.next().id==-1) it.remove();
		}
		
				
		// vendo pontos duplicados no arrayList
		// int c=0, cc=0;
		// for(Ponto p:pnt){
			// c++;
			// cc=0;
			// for(Ponto pp:pnt){
				// cc++;
				// if(p.x==pp.x)
				// if(p.y==pp.y)
				// if(p.z==pp.z)
				// if(c!=cc)	
					// p.id=-1;
			// }	
		// }	
		
		Iterator<Ponto> ip = pnt.iterator();
		while(ip.hasNext()){
			if (ip.next().id==-1) ip.remove();
		}		
		
	}
	public void resizeEsfs(float q){
		// veja q a esfera eh parte do tri e nao de ponto
		// foi necessario p otimizar a ordenacao
		for(Triang t:tri)
			if(t.tamEsf>0)
				t.tamEsf*=q;
	}
	public void resize(float q){ // um setZoom() ficaria mais intuitivo, mas agora jah tem muita coisa implementada nos outros fontes, mas fica aqui como tag
		resize(q,q,q);
		resizeEsfs(q); // ainda nao dah p fazer elipses
	}
	public int numPntSel(){
		int c=0;
		for(Ponto p:pnt){
			if(p.pSel)c++;
		}
		return c;
	}
		
	public void turnChild(float fc){
		// isso ajudou no joeCraft
		zoom = zoom/fc;
		resize("cab","@cab",fc);
	}
	public void turnAdult(float fc){
		zoom = zoom*fc;
		resize("cab","@cab",(float)(1/fc));
	}		
	public void resize(float qx, float qy, float qz){
		// qx = 1 p manter
		boolean all = numPntSel()==0;
		for(Ponto p:pnt)
		if((all) || (p.pSel)){
			p.x*=qx;
			p.y*=qy;
			p.z*=qz;
		}
	}
	public void resize(String tag,String tagBase, float q){
		resize(tag,tagBase,q,q,q);
	}
	public void resize(String tag,String tagBase, float qx, float qy, float qz){
		Ponto pBase = null;
		
		if(tagBase!=null) pBase = procEixo(tagBase); 
		// retorna null se nao achou o eixo
		// aviso embutido
		
		for(Ponto p:pnt)
		if(p!=pBase)
		if(p.temTag(tag)){
			if(pBase!=null){
				p.x-=pBase.x;
				p.y-=pBase.y;
				p.z-=pBase.z;
			}		
			p.x*=qx;
			p.y*=qy;
			p.z*=qz;			
			if(pBase!=null){
				p.x+=pBase.x;
				p.y+=pBase.y;
				p.z+=pBase.z;
			}			
		}	
	}
	public void defGiroGeral(float q){ // setGiroGeral
		// define o giro
		// gira todo o sistema no eixo Y
		// bom para reorientar personagens
		// nao interfere em desFre e similares
		// se virar p leste, frente passará a ser a leste
		// nao recorre ao arrayList gir
		// nao tente alterar ang3 diretamente
		if(opInvAng) q = -q;		
		ang3=q;
		cos3=(float)Math.cos(ang3);
		sin3=(float)Math.sin(ang3);		
	}	
		boolean opInvAng=false; // usei na impressao de shapeJoe em realidade paralela, em joeCraft
	public void giraTudo(float q){ // incGiroGeral
		// incrementa o giro
		// gira todo o sistema no eixo Y
		// bom para reorientar personagens
		// nao interfere em desFre e similares
		// se virar p leste, frente passará a ser a leste
		// nao recorre ao arrayList gir
		if(opInvAng) q = -q;
		ang3+=q;
		cos3=(float)Math.cos(ang3);
		sin3=(float)Math.sin(ang3);		
	}
	public void deslTudo(float dx, float dy, float dz){
		// nao afeta outros controles
		incX+=dx;
		incY+=dy;
		incZ+=dz;
	}

	public boolean semMovAtivo(){
		return (operacoesDeGiro==0) && (operacoesDeDesl==0);
	}

	// abaixo deve ser centralizado vendo complementos
	public void desFre(String tag, float u, int pas){
		des.add(new Desloc(tag, 0,0,u, pas));	
		// +z
	}
	public void desTra(String tag, float u, int pas){
		desFre(tag,-u,pas);
		// -z
	}	
	public void desCma(String tag, float u, int pas){
		des.add(new Desloc(tag, 0,u,0, pas));	
		// +y
	}		
	public void desBxo(String tag, float u, int pas){
		desCma(tag,-u,pas);		
		// -y
	}			
	public void desDir(String tag, float u, int pas){
		des.add(new Desloc(tag, u,0,0, pas));
		// +x
	}
	public void desEsq(String tag, float u, int pas){
		desDir(tag,-u,pas);
		// -x
	}	

	public void desFreR(String tag, float u, int pas){
		des.add(new Desloc(tag, 0,0,u, pas));			
		des.add(new Desloc(tag, 0,0,-u, pas+pas));			
	}
	public void desTraR(String tag, float u, int pas){
		desFreR(tag,-u,pas);
		// -z
	}	
	public void desCmaR(String tag, float u, int pas){
		des.add(new Desloc(tag, 0,u,0, pas));	
		des.add(new Desloc(tag, 0,-u,0, pas+pas));	
		// +y
	}		
	public void desBxoR(String tag, float u, int pas){
		desCmaR(tag,-u,pas);
		// -y
	}			
	public void desDirR(String tag, float u, int pas){
		des.add(new Desloc(tag, u,0,0, pas));
		des.add(new Desloc(tag, -u,0,0, pas+pas));
		// +x
	}
	
	public boolean existeTagPnt(String tg){
		tg = J.emMaiusc(tg);
		for(Ponto p:pnt)
			if(p.temTag(tg)) return true;
		return false;
	}


	public Girad giroX(String tag, String tagE, float an, int pas){		
		// "tagE" eh do eixo
		if(tag==null || existeTagPnt(tag))
		if(tagE==null || existeTagPnt(tagE)){
			Girad g = new Girad(tag, tagE, 0, an, pas, opAtraso);
			gir.add( g );
			opAtraso=0;			
			return g;
		}
		opAtraso=0;		
		return null;
	}
	public Girad giroY(String tag, String tagE, float an, int pas){		
		if(tag==null || existeTagPnt(tag))
		if(tagE==null || existeTagPnt(tagE)){	
			Girad g = new Girad(tag, tagE, 1, an, pas, opAtraso);
			gir.add( g );
			opAtraso=0;			
			return g;
		}
		opAtraso=0;		
		return null;
	}	
	public Girad giroZ(String tag, String tagE, float an, int pas){		
		if(tag==null || existeTagPnt(tag))
		if(tagE==null || existeTagPnt(tagE)){	
			Girad g = new Girad(tag, tagE, 2, an, pas, opAtraso);
			gir.add( g );
			opAtraso=0;			
			return g;
		}	
		opAtraso=0;		
		return null;
	}		

	// abaixo, com movimentos complentares embutidos
	public void giroXr(String tag, String tagE, float an, int pas){		
		// sempre terah o dobro de tempo do parametro
		// "pas" p ir e "pas" p voltar

			
		if(tag==null || existeTagPnt(tag))
		if(tagE==null || existeTagPnt(tagE)){		
			Girad g = new Girad(tag, tagE, 0, +an, pas, opAtraso);
			gir.add( g );	
			Girad gg = new Girad(tag, tagE, 0, -an, pas+pas, opAtrasoo);
			gir.add( gg );		
		}
		opAtraso=0;
		opAtrasoo=0;
	}
	public void giroYr(String tag, String tagE, float an, int pas){		
		if(tag==null || existeTagPnt(tag))
		if(tagE==null || existeTagPnt(tagE)){		
			Girad g = new Girad(tag, tagE, 1, +an, pas, opAtraso);
			gir.add( g );
			Girad gg = new Girad(tag, tagE, 1, -an, pas+pas, opAtrasoo);
			gir.add( gg );
		}	
		opAtraso=0;		
		opAtrasoo=0;		
	}	
	public void giroZr(String tag, String tagE, float an, int pas){		
		if(tag==null || existeTagPnt(tag))
		if(tagE==null || existeTagPnt(tagE)){		
			Girad g = new Girad(tag, tagE, 2, +an, pas, opAtraso);
			gir.add( g );
			Girad gg = new Girad(tag, tagE, 2, -an, pas+pas, opAtrasoo);
			gir.add( gg );		
		}
		opAtraso=0;		
		opAtrasoo=0;		
	}			
	
	public void calcZord(Float sinAn, Float cosAn){
		int w=1000;
		// float r,rr,rrr;
		
		for(Triang t:tri){
			t.calcPm();
			t.zo = (int)(sinAn*t.pm.x*w+cosAn*t.pm.z*w);
		}
		Collections.sort(tri);
	}	
	
	public boolean setEsf(int cr, float tam, String tag){
		// atribui esferas a triangulos
		// retorna true q conseguiu atribuir ao menos 1 esfera
		boolean foi=false; 
		for(Triang t:tri)
			if(t.temTag(tag) || tag==null){
				if(tam!=-1)t.tamEsf=tam;
				if(cr!=-1)t.crt=cr;				
				foi=true;
			}
		return foi;
	}
	public boolean setJf1(String cam, float tam, String tag){
		Jf1 f = new Jf1(cam);
		if(f!=null) return setJf1(f,tam,tag);
		return false;
	}
	public boolean setJf1(Jf1 f, float tam, String tag){
		// atribui jf1 a triangulos
		// retorna true q conseguiu atribuir ao menos 1 esfera
		boolean foi=false; 
		for(Triang t:tri)
			if(t.temTag(tag) || tag==null){
				if(tam!=-1)t.tamEsf=tam;
				if(f!=null)t.jf1=f;				
				foi=true;
			}
		return foi;
	}	

	public void imp2d(int dx, int dy, int pz){
		// base como o meio da tela
		// zoom ajustavel por "pz"
		int cr=0;
		for(Triang t:tri){
			if(cr==crAnt) cr=crNov;	
			else if(cr==crAntt) cr=crNovv;	
			else cr=t.crt+iPal;
			
			J.impTri(cr,t.crc,
				(int)(t.p1.x*pz+dx)+J.xPonto, (int)(-t.p1.y*pz+dy)+J.yPonto,
				(int)(t.p2.x*pz+dx)+J.xPonto, (int)(-t.p2.y*pz+dy)+J.yPonto,
				(int)(t.p3.x*pz+dx)+J.xPonto, (int)(-t.p3.y*pz+dy)+J.yPonto);
		}		
	}

	public void preCalc2D(float ii, float jj, float kk, float sz, float cz, float zom){
		for(Ponto p:pnt){
			// J.Coord2 c = J.fxy(p.x*zoom+ii, p.y*zoom+jj, p.z*zoom+kk);
			J.Coord2 c = 
				J.fxy(
					cz*p.x-p.z*sz+ii+incX,  // incX eh p "deslocaTudo()"
					zom*p.y+jj+incY, 
					sz*p.x+p.z*cz+kk+incZ); 
			p.X = c.x;
			p.Y = c.y;
/*
						cz*t.p1.x-t.p1.z*sz+ii, zom*t.p1.y+jj, sz*t.p1.x+t.p1.z*cz+kk,
						cz*t.p2.x-t.p2.z*sz+ii, zom*t.p2.y+jj, sz*t.p2.x+t.p2.z*cz+kk,
						cz*t.p3.x-t.p3.z*sz+ii, zom*t.p3.y+jj, sz*t.p3.x+t.p3.z*cz+kk)

*/			
		}
	}
		int crFogo = 39;
		boolean opZoomFogo=false;
		Long tempoDeImpressao = 0l;
	public void achatar0(){ // p criar sombras, tag reduzZero
		for(Ponto p:pnt)
			p.y = altSomb;		
		for(Triang t:tri){
			t.crt = crSomb;
			t.crc=0;
		}
	}	
	public boolean impJ3d(float ii, float jj, float kk){

		// JAH TEM P R E C A L C
		// retorna true se mouseHit
						// impTri impTris
		// Long tmpIni = System.currentTimeMillis();		
		Long tmpIni = J.nanoTime();	

		// isso nao consome muito clock?
		J.Coord2 ao =  J.fxy(ii,jj,kk);
		if(ao.ao) return false; 
		
		crFogo = 39-J.R(6); // caso algum tri tiver cr39, usar-se-ah essa cor. Veja q um j3d de fogo pode ter varios tris com essa cor. O efeito fica melhor se a cor for igual p todos.
		float zom = zoom;		
		if(opZoomFogo) zom*= 0.7f+J.R(4)/10f;
		float sz= zom*sin3;
		float cz= zom*cos3;		
		
		//=======
		preCalc2D(ii,jj,kk, sz, cz, zom);		
		//=======
		
		boolean hit=false;				
		calcZord(J.sinAngY, J.cosAngY);
		ii+=incX;
		jj+=incY;
		kk+=incZ;
		int cr=0,crr=0;
		int[] iii=null, jjj=null;
		int d=J.opIncCr3d, cr_=0;
		// if(iPal!=0) J.opIncCr3d=iPal;
		

		
		float s1=0;
		float s2=0;
		float s3=0;
		
		// if(crSomb!=0){ // depois; Teria q aumentar calculos no precalc
			// int r = J.opIncCr3d;
			// for(Triang t:tri)
			// if(t.id!=-1)
			// if(!t.hid){// SOMBRAS
				// s1=t.p1.y*tamSomb;
				// s2=t.p2.y*tamSomb;
				// s3=t.p3.y*tamSomb;
			// J.impTri(crSomb,0,
				// -s1+cz*t.p1.x-t.p1.z*sz+ii, jj+altSomb, sz*t.p1.x+t.p1.z*cz+kk,
				// -s2+cz*t.p2.x-t.p2.z*sz+ii, jj+altSomb, sz*t.p2.x+t.p2.z*cz+kk,
				// -s3+cz*t.p3.x-t.p3.z*sz+ii, jj+altSomb, sz*t.p3.x+t.p3.z*cz+kk);
			// }	
			// J.opIncCr3d=r;		
		// }		

		int w = J.tmpCrCint;
		Color crB=null, crrB=null; // p buffer, caso for este o destino do tri
		for(Triang t:tri)
		if(t.id!=-1)
		if(!t.hid){ // TRIS				
			cr = t.crt;			
			crr =t.crc;
			
			
			if(cr==crAnt) cr=crNov;
			else if(cr==crAntt) cr=crNovv;
			
			if(opContorno) if(t.crt>17) crr= t.crt-2;
			cr_ = cr;
			if(cr==39) cr = crFogo; 
			if(cr==12) cr = 99-(int)(J.cont/w) % 10;
			if(cr==11) cr =119-(int)(J.cont/w) % 10;
			if(cr== 9) cr = 89-(int)(J.cont/w) % 10;
			if(cr==14) cr = 79-(int)(J.cont/w) % 10;
			if(cr==10) cr = 69-(int)(J.cont/w) % 10;
			
			if(cr_==cr) {
				if(cr!=crAnt)
				if(cr!=crAntt)
					J.opIncCr3d = iPal;
			}	
			else J.opIncCr3d=0;			
			
			if(opAramado) {crr=cr+iPal; cr=0;}
/*
u = (float)(p.x*Math.cos(angG)-p.z*Math.sin(angG));
w = (float)(p.x*Math.sin(angG)+p.z*Math.cos(angG));
*/
			
			if(cCint>0) 
				if(J.cont % 2==0) 
					cr=crCint;				
			
			// cr= cr-((int)(J.angY*2))%5; esse eh o caminho p sombreamento automatico
			
			//wwwwwwwwwwwww
			// 4.5 eh a amplitude
			// dah p usar cos , tgn e acos
			// pode separar em n s l o
			// nao precisa calcular p cada tri, fazer o calculo geral primeiro
			// dah p inserir um multiplicador p alterar a paleta e fazer cores saturadas de branco
			// nao precisa de outro editor de paleta
			// tem q somar var j3d.angY
			// O SEGREDO:
			// if(J.vou(t.id,0,1))
				// cr=cr-(int)((Math.sin(J.angY*3)+1)*4.5f);			
			
			if(opSomb!=0)
			if(cr>16){
				if(opSomb>9)opSomb=9;
				int lm = cr - cr%10;
				cr-=opSomb;
				if(cr<lm) cr=lm;
			}


			if(J.grafBufImp==null){
				if(	J.impTri(cr,crr, 
							t.p1.X, t.p1.Y,
							t.p2.X, t.p2.Y,
							t.p3.X, t.p3.Y)
					)	hit=true;
			} else {
				
				if(cr==0)	crB=null;
				else crB=J.cor[cr];
				
				if(crr==0)	crrB=null;
				else crrB=J.cor[crr];		
				
				if(	J.bufTri(crB,crrB, 
							t.p1.X, t.p1.Y,
							t.p2.X, t.p2.Y,
							t.p3.X, t.p3.Y)
					)	hit=true;				
			}
			

			if(t.id!=-1)
			if((t.tamEsf>0) || (t.jf1!=null) || (t.fig!=null)){
				// precisa disso pq o ang X tava distorcendo o tamanho da esfera
				float cx = J.cosAngX;
				float sx = J.sinAngX;
				J.cosAngX = 1;
				J.sinAngX = 0;
				// t.calcPm(); // esse nao tah bom
				J.Coord2 r =  J.fxy(cz*t.p1.x-t.p1.z*sz+ii, zom*t.p1.y+jj, 			sz*t.p1.x+t.p1.z*cz+kk);
				J.Coord2 rr = J.fxy(cz*t.p1.x-t.p1.z*sz+ii, zom*t.p1.y+jj+100f, 	sz*t.p1.x+t.p1.z*cz+kk);
				// retornando valores antigos
				J.cosAngX = cx;
				J.sinAngX = sx;				
				// recalculando 
				r =  J.fxy(cz*t.p1.x-t.p1.z*sz+ii, zom*t.p1.y+jj, 			sz*t.p1.x+t.p1.z*cz+kk);
				int h=0;				
				if(opOscFig>0) h=J.RS(opOscFig);
				int te = (int)((r.y-rr.y+h)*t.tamEsf*zom*0.01);
		
				if(!r.ao){
				
					if(t.fig!=null){
						J.impFig(t.fig, r.x-te, r.y-te, r.x+te, r.y+te);
					}
				
					if(t.jf1!=null)
						t.jf1.impJf1(r.x-te,r.y-te, r.x+te,r.y+te);
					
					
					
					if(t.jf1==null)
					if(t.fig==null)
					if(t.tamEsf>0)
					if(t.crt>1)
					if(!r.ao){ // ateh da p ajustar a opcao sem sombreamento depois
						J.impCirc(t.crt-1,0, (int)te, r.x, r.y);
						J.impCirc(t.crt,0,   (int)(te*0.9f), r.x, r.y-(int)(te*0.3f));
					}	
				}	

			}	

			if(t.lnk!=null)	//!!!!!!!!!!!
				if(t.p1!=null){
					// o objeto vai seguir a rotacao geral
					// funciona p peitorais e celas(pq eh fixo), mas nao p ferramentas na mao nem chapeus
					t.lnk.ang3 = ang3;
					t.lnk.cos3 = cos3;
					t.lnk.sin3 = sin3;
					t.lnk.impJ3d(cz*t.pm.x-t.pm.z*sz+ii, zom*t.pm.y+jj, sz*t.pm.x+t.pm.z*cz+kk);
				}	
				
		}
		J.opIncCr3d=d;		
		tempoDeImpressao = J.nanoTime() - tmpIni;
		return hit;
	}
	public boolean impJ3d(float ii, float jj, float kk, int luz, JPal pl){
		// FUNCAO: imprime o j3d usando JPal.java, mas tinge todo o j3d com a paleta unica. Nao usa faixas.
		// 	 luz: afeta toda a iluminacao do j3d
		

		// isso nao consome muito clock?
		J.Coord2 ao =  J.fxy(ii,jj,kk);
		if(ao.ao) return false; 
		float zom = zoom;		
		float sz= zom*sin3;
		float cz= zom*cos3;		
		
		//=======
		preCalc2D(ii,jj,kk, sz, cz, zom);		
		//=======
		
		boolean hit=false;				
		calcZord(J.sinAngY, J.cosAngY);
		ii+=incX;
		jj+=incY;
		kk+=incZ;
		int cr=0,crr=0;
		int[] iii=null, jjj=null;
		
		float s1=0;
		float s2=0;
		float s3=0;

		Color crB=null, crrB=null; // p buffer, caso for este o destino do tri
		int vr = 0; //variacao p luz do j3d
		
		//11111111111
		crFogo = 39-J.R(6); // caso algum tri tiver cr39, usar-se-ah essa cor. Veja q um j3d de fogo pode ter varios tris com essa cor. O efeito fica melhor se a cor for igual p todos.						
		
		for(Triang t:tri)
		if(t.id!=-1)
		if(!t.hid){ // TRIS				
			cr = t.crt;			
			crr =t.crc;


			
			if(J.grafBufImp==null){
				if(	J.impTri(cr,crr, 
							t.p1.X, t.p1.Y,
							t.p2.X, t.p2.Y,
							t.p3.X, t.p3.Y)
					)	hit=true;
			} else {
// pppppppppppppp
				
				vr = 99-t.crt; // presume-se o desenho com a cor 99
				if(pl!=null)
					crB = pl.get(luz-vr);
				else crB = J.cor[12];
				crrB = null; // por enquanto
				
				if(t.crt==39) crB = J.cor[crFogo];
				
			if(t.crt==crAnt) crB = ccrNov;
			if(t.crt==crAntt) crB = ccrNovv;							
				
				J.opTriParaHit = (t.tg0=='&');				
				
				if(	J.bufTri(crB,crrB, 
							t.p1.X, t.p1.Y,
							t.p2.X, t.p2.Y,
							t.p3.X, t.p3.Y)
					)	hit=true;				
			}

			if(t.lnk!=null)	
				if(t.p1!=null){
					// o objeto vai seguir a rotacao geral
					// funciona p peitorais e celas(pq eh fixo), mas nao p ferramentas na mao nem chapeus
					t.lnk.ang3 = ang3;
					t.lnk.cos3 = cos3;
					t.lnk.sin3 = sin3;

					if(t.lnk.impJ3d(cz*t.pm.x-t.pm.z*sz+ii, zom*t.pm.y+jj, sz*t.pm.x+t.pm.z*cz+kk, luz, pl))
						hit = true;
				}	
				
		}
		return hit;
	}
	public boolean impJ3dPal(float ii, float jj, float kk, int luz){
		// FUNCAO: imprime o j3d usando cores da paleta associada
		// NAO USA J.COR[]!!!
		// 	 luz: afeta toda a iluminacao do j3d

		// isso nao consome muito clock?

		J.Coord2 ao =  J.fxy(ii,jj,kk);
		if(ao.ao) return false; 
		float zom = zoom;		
		float sz= zom*sin3;
		float cz= zom*cos3;		
		
		//=======
		preCalc2D(ii,jj,kk, sz, cz, zom);		
		//=======
		
		boolean hit=false;				
		calcZord(J.sinAngY, J.cosAngY);
		ii+=incX;
		jj+=incY;
		kk+=incZ;
		// int cr=0,crr=0;
		int[] iii=null, jjj=null;
		
		float s1=0;
		float s2=0;
		float s3=0;

		// if(cCint>0) cCint--;// isso jah tah no "r e g 3 d ()"
		
		// Color crB=null, crrB=null; // p buffer, caso for este o destino do tri
		// int vr = 0; //variacao p luz do j3d
		
		crFogo = 39-J.R(6); // caso algum tri tiver cr39, usar-se-ah essa cor. Veja q um j3d de fogo pode ter varios tris com essa cor. O efeito fica melhor se a cor for igual p todos.		

		Color acor = null;		
		for(Triang t:tri)
		if(t.id!=-1)
		if(!t.hid){ // TRIS				
			// cr = t.crt;			
			// crr =t.crc;
			

			
			
			
			if(t.pal==null)
				acor = J.cor[10]; // p destacar erro
			else 
				acor = t.pal.get(luz+t.iPalt);
			
			if(t.crt==crAnt) acor = ccrNov;
			if(t.crt==crAntt) acor = ccrNovv;

			if(t.crt==39) acor = J.cor[crFogo]; 
/*
			if(cr==12) cr = 99-(int)(J.cont/w) % 10;
			if(cr==11) cr =119-(int)(J.cont/w) % 10;
			if(cr== 9) cr = 89-(int)(J.cont/w) % 10;
			if(cr==14) cr = 79-(int)(J.cont/w) % 10;
			if(cr==10) cr = 69-(int)(J.cont/w) % 10;
*/
			
			if(cCint>0) 
				if(J.cont % 2==0) 
					acor=J.cor[crCint];

			// repare: deteccao por int, atribuicao por color



			if(J.grafBufImp==null){
				if(	J.impTri(acor,null, 
							t.p1.X, t.p1.Y,
							t.p2.X, t.p2.Y,
							t.p3.X, t.p3.Y)
					)	hit=true;
			} else {
// pppppppppppppp
				
				J.opTriParaHit = (t.tg0=='&');
					
				if(	J.bufTri(acor,null, 
							t.p1.X, t.p1.Y,
							t.p2.X, t.p2.Y,
							t.p3.X, t.p3.Y)
					)	hit=true;				
					
			}
				
		}
		if(opSemHit) return false;
		return hit;
	}



	public class Triang implements Comparable<Triang>{
			JPal pal = null; // isso vai ficar legal
			int iPalt = 0;
			J3d lnk=null; // truque. Dah p fazer coisas legais com isso. Mas nao pode ser recursivo!
			Jf1 jf1=null; // dah p fazer animacoes com isso. Impressao junto com esfera
			Image fig = null; // esse eh p png e jpg
			float tamEsf=0; // esfera. Usa a cor do tri. Valendo zero indica ausencia de esfera.
			int crt=1, crc=2, 
				id=32000, zo=-1; // zOrder
			boolean 
				hid=false;     // CUIDADO! Nem toda a impressao de triangulos em J.java tem esse esquema implementado!
			Ponto pm=null,p1=null,p2=null,p3=null;
			char tg0=' ',tg1=' ',tg2=' ',tg3=' ',
				   tg4=' ',tg5=' ',tg6=' ';
		public int compareTo(Triang outro){
			return (int)((outro.zo - zo)*10000);
			// return zo - outro.zo;
		}
		public void calcPm(){
			// veja q em JoeCraft o tri referencia diretamente o pnt, e nao o faz por referencia de indice
			float i=0, j=0, k=0;
			i		= p1.x*cos3-p1.z*sin3
					+ p2.x*cos3-p2.z*sin3
					+ p3.x*cos3-p3.z*sin3;
			j		= p1.y
					+ p2.y
					+ p3.y;					
			k		= p1.x*sin3+p1.z*cos3
					+ p2.x*sin3+p2.z*cos3
					+ p3.x*sin3+p3.z*cos3;
			pm = new Ponto(
								i/3f, 
								j/3f, 
								k/3f);		
			// abaixo evita bug de impressao
			// nao use "-1" q eh flag de exclusao
			pm.id=32000; 
		}
			// !!!!!!!!!!!!!!!!!!!!!
			// depois eu acabo isso
		public Ponto novoPntSeNaoExiste(Ponto p){
			// retorna o ponto do parametro se o mesmo nao existe na lista de pontos jah criada.
			for(Ponto q:pnt)
				if(q.x==p.x)
				if(q.y==p.y)
				if(q.z==p.z)
					return q;
			return p;	
		}
			boolean opCriandoMalha=false; // esse nome pode mudar
			//!!!!!!!!!!!!!!!!!!!!!
		public Triang(int cr, int crr, Ponto p, Ponto pp, Ponto ppp){
			// aqui deveria ser verificado se o ponto jah existe. Deve ajudar na criacao de malhas.
			// veja q pelo editor os pontos sempre pre-existirao
			// o problema eh a criacao de malhas. Um bool de opcao poderia verificar isso.
			
// opCriandoMalha=true;
			if(opCriandoMalha){
				// se o ponto jah existir na coodenada, pegar ele
				p1 = novoPntSeNaoExiste(p);
				p2 = novoPntSeNaoExiste(pp);
				p3 = novoPntSeNaoExiste(ppp);
			} else {
				p1=p;
				p2=pp;
				p3=ppp;
			}
			crt=cr;
			crc=crr;
			id=tri.size();
			zo=-1;
			hid=false;
			calcPm();
			lnk=null;
		}
		public void setTag(String tag){
			tag = J.emMaiusc(tag+"       ");
			tg0=tag.charAt(0);
			tg1=tag.charAt(1);
			tg2=tag.charAt(2);
			tg3=tag.charAt(3);
			tg4=tag.charAt(4);
			tg5=tag.charAt(5);
			tg6=tag.charAt(6);
		}
		public String getTag(){
			String st = ""
				+tg0
				+tg1
				+tg2
				+tg3
				+tg4
				+tg5
				+tg6;
			return J.emMaiusc(st);
		}		
		public boolean temTag(String tag){
			if(tag==null) return false;
			tag= J.emMaiusc(tag+"       "); 
			if(tag.charAt(0)==tg0)
			if(tag.charAt(1)==tg1)
			if(tag.charAt(2)==tg2)
			if(tag.charAt(3)==tg3)
			if(tag.charAt(4)==tg4)
			if(tag.charAt(5)==tg5)
			if(tag.charAt(6)==tg6)
				return true;
			return false;	
		}
		
	}

	// necessario p acesso ao editor
	public Ponto insPnt(float i, float j, float k){ // ?o certo nao seria um addPnt???
		return insPnt(i,j,k,null); // retornar p ter controle de pSel e similares
	}
	public Triang insTri(int cr, int crr, Ponto p, Ponto pp, Ponto ppp){	
		return insTri(cr,crr,p,pp,ppp,null);
	}
	public Triang insTri(int cr, int crr, Ponto p, Ponto pp, Ponto ppp, String tag){
		// seria bom evitar a criacao de tris coincidentes (duplicados)
		// veja q a combinacao de pontos pode ser desigual, mas o tri no final acaba ficando igual
		if(p==null) return null;
		if(pp==null) return null;
		if(ppp==null) return null;
		if(p==pp) return null;
		if(p==ppp) return null;
		if(pp==ppp) return null;
		
		if((cr==0)&&(crr==0)) { cr=1; crr=2; }
		
		Triang t = new Triang(cr, crr, p,pp,ppp);
		t.hid=false;
		if(tag!=null) t.setTag(tag);
		tri.add(t);
		lastTri = t;
		return t; 
	}

	
	public Ponto getPntMaisAlto(){
		Ponto ret = new Ponto(0,0,0);
		for(Ponto p:pnt)
			if(p.y>ret.y) ret=p;
		return ret;
	}
		
	public Ponto getPnt(String tag){
		for(Ponto p:pnt)
			if(p.temTag(tag))
				return p;
		return null;	
	}
	public Ponto insPnt(float i, float j, float k, String tag){
		Ponto p = null;		
		p = getPnt("."); // pontos reciclaveis. Tava dando problema na exclusao de pnt.
		if(p==null){
			p = new Ponto(i,j,k);
			p.hid=false;			
			if (tag!=null) p.setTag(tag);						
			pnt.add(p);			
			}
		else{
			p.x=i;
			p.y=j;
			p.z=k;
			p.hid=false;			
			if(tag!=null) p.setTag(tag);			
			else p.setTag("");
		}
		lastPnt = p;
		return p; // retornar p ter controle de pSel e similares
	}

	public void hide(String st){
		st = J.emMaiusc(st);
		for(Triang t:tri)
			if(t.temTag(st))
				t.hid=true;		
	}
	public void unhide(String st){
		for(Triang t:tri)
			if(t.temTag(st) || st==null)
				t.hid=false;		
	}	
	public void hide(int fcr){
		// por faixa de cor
		fcr = fcr - (fcr % 10);
		for(Triang t:tri){
			if(t.crt - (t.crt % 10) == fcr)
				t.hid=true;
		}				
	}
	public void unhideAll(){
		for(Triang t:tri)	t.hid = false;
		for(Ponto p:pnt) p.hid = false;	
	}	
	public J3d copy(){
		J3d w = new J3d();
		w.pnt = pnt;
		w.tri = tri;
		w.des = des;
		w.gir = gir;
		w.cos3 = cos3;
		w.sin3 = sin3;
		w.ang3 = ang3;
		w.incX = incX;
		w.incY = incY;
		w.incZ = incZ;
		w.camJ3d = camJ3d;
		w.opContorno=opContorno;
		w.opAramado=opAramado;		
		w.iPal = iPal;
		w.cCint=cCint;
		w.crCint=crCint;
		w.crAnt=crAnt;
		w.crAntt=crAntt;
		w.crNov=crNov;
		w.crNovv=crNovv;
		w.e = e;
		w.zoom = zoom;
		w.tamSomb = tamSomb;		
		return w;
	}
	public J3d clonar(){ // retorna uma copia de si mesmo
		// Parece funcionar bem, mas deve ser melhor testado.
		// NAO TAH BEM NAO! Pontos clonados estao usando ponteiros da instancia antiga. Devem ser unicos.
		J3d w = new J3d();
		// repare q nao eh atribuicao direta
		for(Ponto p:pnt) w.insPnt(p.x, p.y, p.z, p.getTag());
		for(Triang t:tri)	w.insTri(t.crt, t.crc, t.p1, t.p2, t.p3, t.getTag());
		w.cos3 = cos3;
		w.sin3 = sin3;
		w.ang3 = ang3;
		w.incX = incX;
		w.incY = incY;
		w.incZ = incZ;
		w.camJ3d = camJ3d;
		w.opContorno=opContorno;
		w.opAramado=opAramado;		
		w.iPal = iPal;
		w.cCint=cCint;
		w.crCint=crCint;
		w.crAnt=crAnt;
		w.crAntt=crAntt;
		w.crNov=crNov;
		w.crNovv=crNovv;
		w.e = e;
		w.zoom = zoom;
		w.tamSomb = tamSomb;			
		return w;
	}	
	public void clonar(J3d w){ // copia o objeto parametro p esta instancia
		// serah q funciona mesmo? Deve ser testado.
		giraTudo(0);		
		tri.clear();
		pnt.clear();
		gir.clear();
		des.clear();
		for(Ponto p:w.pnt) insPnt(p.x, p.y, p.z, p.getTag());
		
		for(Triang t:w.tri)	insTri(t.crt, t.crc, t.p1, t.p2, t.p3, t.getTag());
	}
	
	
	public class Ponto{
			float x=0, y=0, z=0;
			int X=0, Y=0; // esses sao "p p r e C a l c 2 D()"
			int id=0;
			boolean pSel=false, hid=false;// hid p editor
			char tg0=' ',tg1=' ',tg2=' ',tg3=' ',
				   tg4=' ',tg5=' ',tg6=' ';
		public Ponto(Ponto p){
			x=p.x;
			y=p.y;
			z=p.z;
			id=pnt.size();
			pSel=false;
			zTags();
		}
		public Ponto(float i, float j, float k){
			x=i; y=j; z=k;
			id=pnt.size();
			pSel=false;
		}
		public void zTags(){
			setTag("          ");
		}
		public void setTag(String tag){ // precisava ser "setName()"... precisa escrever uma nova versao, e precisa ser compativel com formatos de arq antigos e fontes antigos tb.
			tag = J.emMaiusc(tag+"       ");
			tg0=tag.charAt(0);
			tg1=tag.charAt(1);
			tg2=tag.charAt(2);
			tg3=tag.charAt(3);
			tg4=tag.charAt(4);
			tg5=tag.charAt(5);
			tg6=tag.charAt(6);
		}
		public String getTag(){
			String st = ""
				+tg0
				+tg1
				+tg2
				+tg3
				+tg4
				+tg5
				+tg6;
			return J.emMaiusc(st);
		}		
		public boolean temTag(String tag){
			if(tag==null) return false;
			tag= J.emMaiusc(tag+"       "); 
			if(tag.charAt(0)==tg0)
			if(tag.charAt(1)==tg1)
			if(tag.charAt(2)==tg2)
			if(tag.charAt(3)==tg3)
			if(tag.charAt(4)==tg4)
			if(tag.charAt(5)==tg5)
			if(tag.charAt(6)==tg6)
				return true;
			return false;	
		}
		public Ponto extY(float p){
			Ponto q = new Ponto(x,y+p,z);
			return q;		
		}	
	}
	public class Desloc{
			int np=-1, cp=-1;
			float vx=0, vy=0, vz=0;
			char tg0=' ',tg1=' ',tg2=' ',tg3=' ',
				   tg4=' ',tg5=' ',tg6=' ';
		public Desloc(String tag, float x, float y, float z, int pas){
			
			z = -z; // corrigindo bug
			y = -y; // q estranho, mas assim funciona
			if(tag==null) tag="*";
			setTag(tag);
			vx = (float)(x/pas);
			vy = (float)(y/pas);
			vz = (float)(z/pas);
			np=pas;
			cp=0;
		}
		public void setTag(String tag){
			if(tag==null) { tg0='*'; return; }
			tag = J.emMaiusc(tag+"              ");
			tg0=tag.charAt(0);
			tg1=tag.charAt(1);
			tg2=tag.charAt(2);
			tg3=tag.charAt(3);
			tg4=tag.charAt(4);
			tg5=tag.charAt(5);
			tg6=tag.charAt(6);
		}
		public String getTag(){
			String st = ""
				+tg0
				+tg1
				+tg2
				+tg3
				+tg4
				+tg5
				+tg6;
			return J.emMaiusc(st);
		}		
		public boolean regDes(){
			cp++;
			int movido=0;
			String tag = getTag();
			if(cp<=np){
				for(Ponto p:pnt){
					if( (tg0=='*') || p.temTag(tag) ){
						p.x+=vx; 
						p.y+=vy; 
						p.z+=vz;
						movido++;
					}
				}
				if(movido==0){
					J.impErr("tag 3d nao encontrada: "+camJ3d+":'"+tag+"'","Desloc.regDes()");
					// System.exit(0);
					}
				return true;
			} else {
				np=-1;
				return false;
			}	
		}
	}
	public void regDess(){
	
		boolean rem=false;
		operacoesDeDesl=0;
		for(Desloc d:des){
			if(!d.regDes()) rem=true;
			else operacoesDeDesl++;
		}
		
		if(rem){
			Iterator<Desloc> d = des.iterator();
			while(d.hasNext()){
				if (d.next().np==-1) d.remove();
			}
		}	
		
	}
	public class Girad{
			Ponto pEx=null;
			char tg0=' ',tg1=' ',tg2=' ',tg3=' ',
				   tg4=' ',tg5=' ',tg6=' ';
			// np = numero de passos (chegar ateh onde)
			// cp = contador de passos (em q peh estah)
			int np=0, cp=0, nEx=1, opAtraso =0; 
			float angG=0;			
		public Girad(){
			pEx=null;
			 zTags();
			 tg0='*';			
			np = 20;
			cp = 0;
			float an = 3.1415f; // 3.1415=PI = 180graus
			angG= an/np;
		}
		public void zTags(){
			setTag("          ");
		}
		public Girad(String tag, String tagE, int ide, float an, int pas, int atrs){
			
			// se nao achou o eixo informado, entao adote 0,0,0
			if(tagE!=null) if(!existeTagPnt(tagE)) {
				tagE=null;
				// melhor avisar isso
				J.esc("AVISO: eixo nao encontrado: "+tagE+". Foi adotado 0:0:0. Shape |"+camJ3d+"|");
			}
		
			// ex: Girad("maos","@ombro",1, -VOLTA/2f, 10)
			this.opAtraso = atrs; // se maior q 0, atrasa o movimento do girador
// pontos-alvo
			if(tag==null) tg0='*';
			else setTag(tag);
// ponto-eixo
			pEx=null;
			if(tagE!=null){
				for(Ponto p:pnt)
					if(pEx==null)
						if(p.temTag(tagE)) pEx= new Ponto(p);					
			}	else pEx= new Ponto(0,0,0);
			if(pEx==null){ // se ainda assim nao achou referencia a tagE:
				J.impErr("eixo nao encontrado: "+camJ3d+":"+tagE,"construtor Girad()");
				// System.exit(0);
			}	
// direcao no eixo: x=0, y=1, z=2;
			if(J.noInt(ide,0,2))nEx=ide; else nEx=1;
// angulo e passos			
			np = pas;
			angG = an/np; // o mecanismo dispensa pasG
			cp = 0;			
		}
		public void setTag(String tag){
			if(tag==null) return;
			tag =J.emMaiusc(tag+"      ");			
			tg0=tag.charAt(0);
			tg1=tag.charAt(1);
			tg2=tag.charAt(2);
			tg3=tag.charAt(3);
			tg4=tag.charAt(4);
			tg5=tag.charAt(5);
			tg6=tag.charAt(6);
		}
		public String getTag(){
			String st = ""
				+tg0
				+tg1
				+tg2
				+tg3
				+tg4
				+tg5
				+tg6;
			return st;	
		}		
		public boolean regGir(){
			if(opAtraso>0){
				opAtraso--;
				return true;
			}
			cp++;
			int movido=0;
			String tag = getTag();
			
			float u=0, w=0;
			float sg = (float)Math.sin(angG);
			float cg = (float)Math.cos(angG);
			boolean vai = false;
			
			if(cp<=np){
				for(Ponto p:pnt)
					{
					// eh inocuo evitar q o eixo gire
					vai = false;
					if(opGirarPSel){
						if(p.pSel) vai = true;
					} else {
						if(tg0=='*') vai = true;
						if(p.temTag(tag)) vai=true;
					}	
					
					if( vai )	{
							p.x-=pEx.x;
							p.y-=pEx.y;
							p.z-=pEx.z;
							switch(nEx){
								case 0: //x
									u = (float)(p.y*cg-p.z*sg);
									w = (float)(p.y*sg+p.z*cg);								
									p.y= u;
									p.z= w;
									break;
								case 1: //y
									u = (float)(p.x*cg-p.z*sg);
									w = (float)(p.x*sg+p.z*cg);
									p.x= u; 
									p.z= w;
									break;									
								case 2: //z
									u = (float)(p.x*cg-p.y*sg);
								  w = (float)(p.x*sg+p.y*cg);
									p.x= u;
									p.y= w;
									break;
							}
							p.x+=pEx.x;
							p.y+=pEx.y;
							p.z+=pEx.z;							
						movido++;
					}
				}
				if(movido==0) {					
					J.impErr("tag 3d nao encontrada: "+camJ3d+":"+tag,"Girad.regGir()");
					// System.exit(0);
				}	
				return true;
			} else {
				np=-1;
				return false;
			}	
			
		}
	}
		public int 
			operacoesDeGiro=0,
			operacoesDeDesl=0; // mudancas feitas no loop atual
		
	public void regGirs(){		
		boolean rem=false;	
		operacoesDeGiro=0;
		for(Girad d:gir){
			if(!d.regGir()) rem=true;
			else operacoesDeGiro++;
		}
		
		if(rem){
			Iterator<Girad> d = gir.iterator();
			while(d.hasNext()){
				if (d.next().np==-1) d.remove();
			}
		}	
	}

	public void addCub(int cr, float ix, float iy, float iz, float res){
		// veja "public J3d carrNSLO(String cam, int d)" de joeCraft
		// melhor seria omitir a face segundo a cor dela
		// na gambiarra, daria p omitir faces apontando p arquivos diferentes
		// poderia ser inserida face por face segundo o arq respectivo
		// poderia ser criada a face sem arq, apenas rotacionando um j3d modelo
		J3d q = new J3d("addCub.j3d",res/10f);
		q.tingir(99,cr);
	
		int pBase=pnt.size();
		int tBase=tri.size();
		
		for(Ponto p:q.pnt)
			insPnt(p.x+ix, p.y+iy, p.z+iz);
			
		Ponto p1=null,p2=null,p3=null;	
		for(Triang t:q.tri){
			p1 = pnt.get(pBase+t.p1.id);
			p2 = pnt.get(pBase+t.p2.id);
			p3 = pnt.get(pBase+t.p3.id);
			if(1==1){
				p1.setTag("$");
				p2.setTag("$");
				p3.setTag("$");
			}
			insTri(t.crt, t.crc, p1,p2,p3);
		}
	}	
	public void addComp(String cam, float ix, float iy, float iz){
		// acho q corrigiu
		J3d q = new J3d(cam);
		addComp(q, ix,iy,iz);
	}
	public void addComp(J3d q){
		addComp(q,0,0,0);
	}	
	public void addComp(J3d q, float ix, float iy, float iz){
		if(q==null) return;
		int pBase=pnt.size();
		int tBase=tri.size();
		
		for(Ponto p:q.pnt)
			insPnt(p.x+ix, p.y+iy, p.z+iz, p.getTag());		
		
			
		Ponto p1=null,p2=null,p3=null;	
		for(Triang t:q.tri){
			p1 = pnt.get(pBase+t.p1.id);
			p2 = pnt.get(pBase+t.p2.id);
			p3 = pnt.get(pBase+t.p3.id);
			// if(1==1){
				// p1.setTag("$");
				// p2.setTag("$");
				// p3.setTag("$");
			// }
			insTri(t.crt, t.crc, p1,p2,p3, t.getTag());
		}
	}	
	public void addComp(J3d q, String tagI, String tagR){
		float 
			ix=0, iy=0, iz=0;
			
		if(tagI!=null) tagI = J.emMaiusc(tagI+"       ");	
		if(tagR!=null) tagR = J.emMaiusc(tagR+"       ");	
		
		if(tagI!=null){
			Ponto p = procEixo(tagI);
			if(p==null){
				J.impErr("tag nao encontrada: '"+tagI+"'","J3d.addComp()");
				System.exit(0);
			}
			if(p!=null){
				ix = p.x;
				iy = p.y;
				iz = p.z;
			}
		}
		
		int pBase=pnt.size();
		int tBase=tri.size();
		
		for(Ponto p:q.pnt)
			insPnt(
				p.x+ix+q.incX, // esse incX eh bom. Dah p usar "desloca tudo" antes de anexar sem usar deslocadores.
				p.y+iy+q.incY, 
				p.z+iz+q.incZ);
			
		Ponto p1=null,p2=null,p3=null;	
		for(Triang t:q.tri){
			p1 = pnt.get(pBase+t.p1.id);
			p2 = pnt.get(pBase+t.p2.id);
			p3 = pnt.get(pBase+t.p3.id);
			if(tagR!=null){
				p1.setTag(tagR);
				p2.setTag(tagR);
				p3.setTag(tagR);
			}
			insTri(t.crt, t.crc, p1,p2,p3);
		}

	}

	public void lnkTriComp(String tag, J3d p){	
		tag = J.emMaiusc(tag+"         ");
		for(Triang t:tri)
			if(t.temTag(tag))
				t.lnk=p;
	}	
	
	public Triang getTri(String tag){
		tag = J.emMaiusc(tag);
		for(Triang t: tri)
			if(t.temTag(tag)) return t;
		return null;
	}
	public int getCor(String tag){
		tag = J.emMaiusc(tag);
		for(Triang t:tri)
			if(t.temTag(tag))
				return t.crt;
		J.impErr("!tag nao encontrada: '"+tag+"'","J3d.getCor()");
		// J.sai();
		return -1;	
	}
	public void ajustaCores(int p){
		// se precisar de tag, fazer depois
		// veja q nao tem controle de limites
		for(Triang t:tri)
			t.crt+=p;
	}

		// repare: "tingir()" usa faixa de cores, 
		//    jah "trocaCor()" usa cores especificas
	public void trocaCor(int cra, int crn){
		for(Triang t:tri)
			if(t.crt==cra)
			if(!t.hid)
				t.crt=crn;
	}
	public void tingir(int fa, JPal p){
		fa = fa - (fa % 10);
		for(Triang t:tri){
			if(!t.hid)
				if(t.crt - (t.crt % 10) == fa)
					t.pal = p;
		}		
	}

	public void tingir(int fa, int fn){
		fa = fa - (fa % 10);
		fn = fn - (fn % 10);
		for(Triang t:tri){
			if(!t.hid)
				if(t.crt - (t.crt % 10) == fa)
					t.crt = fn + t.crt % 10;
		}		
	}
	public void tingir(String tagTri, String tagCop){
		int cr = getCor(tagCop);
		tingir(tagTri,cr);
	}
	public void tingir(String tagTri, int fn){
		fn = fn - (fn % 10);
		for(Triang t:tri)
			if(tagTri==null || t.temTag(tagTri))
				if(!t.hid)
					t.crt = fn + t.crt % 10;
	}
	public void tingir(String tg){
		// tingir todos q estiverem exibidos
		tingir(getCor(tg));
	}	
	public void tingir(int fn){
		// tingir todos q estiverem exibidos
		fn = fn - (fn % 10);
		for(Triang t:tri)
			if(!t.hid)
				t.crt = fn + t.crt % 10;
	}
	public void tingirTudo(JPal p){
		for(Triang t:tri)
			t.pal = p;
	}

	public void hideTriCr(int fa){
		if(fa>=40) // jah precisei trocar a cor 15 de um j3d de balde e me enrolei
			fa = fa - fa%10;
		for(Triang t:tri)
			if(t.crt - t.crt%10 == fa)
				t.hid = true;
	}
	public void hideTriCr(String tag){
		hideTriCr(getCor(tag));
	}
	public void hideTriPP(String tag){
		// opera sobre tag de pontos e nao de tris
		tag = J.emMaiusc(tag+"       ");
		for(Triang t:tri)
			if(t.p1.temTag(tag))
			if(t.p2.temTag(tag))
			if(t.p3.temTag(tag))
				t.hid=true;
	}
	public void unhideTri(String tag){
		// veja: esse opera sobre tag de tris, nao de pontos
		tag = J.emMaiusc(tag);
		for(Triang t:tri)
			if(t.temTag(tag))
				t.hid=false;		
	}

	public void selAllPnt(){
		for(Ponto p:pnt)
			p.pSel=true;
	}
	public void deselAllPnt(){
		for(Ponto p:pnt)
			p.pSel=false;
	}	
}
