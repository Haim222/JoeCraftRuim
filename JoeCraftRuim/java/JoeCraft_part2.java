// JoeCraftRuim
/* ATENÇÃO:

		COMANDOS:
				use as teclas WASD e mouse.
				ENTER: escolher blocos para o inventário. 
				shift+DEL: descartar o item do inventário. 
				shift+ESC: Sair.
				
		AVISO:		
		Este programa é obsoleto e altamente instável. Irá crashar a qualquer hora. Não existe garantia de que vá funcionar. Não espere muito deste projeto (que foi descontinuado). Este programa precisa ser totalmente reescrito.
		Na migração do windows10 p LinuxLite o código precisou ser adaptado e muita coisa ficou descalibrada depois disto. O código já não rodava. Precisei adaptar alguma coisa com alguma dificuldade, e sei q não dá p ajustar todo o cod de novo.
		Os arquivos de sons não estão presentes. Veja "JWav.opMudo=true".

*/
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import java.util.Iterator;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.io.File;
 

public class JoeCraft_part2 extends JoeCraft_part1 implements KeyListener{

  Painel painel;


		Long 
			tmpGuar=J.nanoTime(),
			tmpCont=100000000L;
		
	public class Painel extends JPanel{
	  public void paint(Graphics gg){
			J.impRetRel(10,0, 0,0,200,200);	//???		
			//super.paint(gg); // NAO PRECISA DISSO!
		  g=gg;
			J.g=gg;
			repaint();
			regAll();
			J.regNanoTime();
			
			// J.cont++; // este eh soh po notebook, p pc Dell nao fica bom 
			{ // incremento melhorado p clock
			
				//se a diferenca do tempo guardado com o tempo atual for MAIOR q "tmpCont", entao:
				//  - incrementa cont
				//  - guarde o novo tempo				
				
				// if(J.nanoTime()-tmpGuar>tmpCont){
					// J.cont++;
					// J.contt=0;
					// tmpGuar = J.nanoTime();
				// }
				
			}
		}
	}			
	

// === APOIO ================================

		boolean op1ElBxo=false; // flag q indica p ver primeiro se tem el embaixo do cubo em questao. Bom p adiantar clocks p esteiras.
	public void setPlatRai(int v, int vi, int r, int xa, int ya, int za, int xt, int yt, int zt){
		// tag setCirc
		r = J.corrInt(r, 1, 40); // deve ajudar
		for(int m=-r; m<=+r; m++)
		for(int o=-r; o<=+r; o++)
			setCub(v,vi, xa+m, ya, za+o, xt,yt,zt);
	}	
	public void setLuzEsf(int r, int xa, int ya, int za, int xt, int yt, int zt){
		// nv = nivel da luz ou intensidade, r = raio
		xa-=10; ya-=10; za-=10;
		int v=0, rr=r;
		if(r==APAGAR) rr=100;
		for(int m=1; m<=20; m++)
		for(int n=1; n<=20; n++)
		for(int o=1; o<=20; o++)
			if(fCirc.cel[m][n]<rr)
			if(fCirc.cel[n][o]<rr){
				//v = fCircLuz.cel[m][o]<<1;
				v = fCircLuz.cel[m][o];
				if(v>4)v=8; // evitando luz saturada, fica mais bonito aqui
				if(r==APAGAR) v=APAGAR;
				setLuzTocha(v, xa+m, ya+n, za+o, xt,yt,zt);
			}
	}		
	public boolean temElViz(int q, int xa, int ya, int za, int xt, int yt, int zt){
		// veja q ganha-se algum clock com el colocada ao norte da maquina, mas soh eh perceptivel quando se tem muitas maquinas ao mesmo tempo.
	
		// mais redes depois
		// isso abaixo ajudou
		if(tCub.get(tRede1).life<q) return false;		
		
		// priorizar essa abaixo pq esteiras sao muito frequentes.
		if(op1ElBxo)
		if(getCub(xa,ya-1,za,xt,yt,zt)==tRede1){
			op1ElBxo=false; // retorno automatico
			return true;			
		}	
		
		
		if(getCub(xa,ya,za+1,xt,yt,zt)==tRede1) return true;
		if(getCub(xa,ya,za-1,xt,yt,zt)==tRede1) return true;
		if(getCub(xa+1,ya,za,xt,yt,zt)==tRede1) return true;
		if(getCub(xa-1,ya,za,xt,yt,zt)==tRede1) return true;

		if(getCub(xa,ya+1,za,xt,yt,zt)==tRede1) return true;
		if(getCub(xa,ya-1,za,xt,yt,zt)==tRede1) return true;		
		return false;
	}
	public Cub getCond1Viz(int xa,int ya,int za,int xt,int yt,int zt){
		Cub c=null;
		c = getCubMod(xa,ya,za+1,xt,yt,zt); if(c!=null) if(c.ehCond) if(c.tip!=tCond0) return c;
		c = getCubMod(xa,ya,za-1,xt,yt,zt); if(c!=null) if(c.ehCond) if(c.tip!=tCond0) return c;
		c = getCubMod(xa+1,ya,za,xt,yt,zt); if(c!=null) if(c.ehCond) if(c.tip!=tCond0) return c;
		c = getCubMod(xa-1,ya,za,xt,yt,zt); if(c!=null) if(c.ehCond) if(c.tip!=tCond0) return c;
		c = getCubMod(xa,ya+1,za,xt,yt,zt); if(c!=null) if(c.ehCond) if(c.tip!=tCond0) return c;
		c = getCubMod(xa,ya-1,za,xt,yt,zt); if(c!=null) if(c.ehCond) if(c.tip!=tCond0) return c;
		return null;
	}
	public boolean incCond1Viz(int q, int lm, int xa,int ya,int za,int xt,int yt,int zt){ // tag incCodViz decCondViz
		// incrementa a quantidade "q" da el (=life) do cubo modelo cond# (exceto cond0)
		// retorna true se conseguiu incrementar tudo
		// NAO incrementa e retorna false se ficar acima do limite
		if(q<0) J.impErr("!parametro inválido: q="+q, "incCond1Viz()");
		Cub c = null;
		boolean foi = false;
		
		if(!foi) { c = getCubMod(xa,ya,za+1,xt,yt,zt); if(c!=null) if(c.ehCond) if(c.tip!=tCond0) if(c.life-q>=lm) { c.life-=q; foi=true; }}
		if(!foi) { c = getCubMod(xa,ya,za-1,xt,yt,zt); if(c!=null) if(c.ehCond) if(c.tip!=tCond0) if(c.life-q>=lm) { c.life-=q; foi=true; }}
		if(!foi) { c = getCubMod(xa+1,ya,za,xt,yt,zt); if(c!=null) if(c.ehCond) if(c.tip!=tCond0) if(c.life-q>=lm) { c.life-=q; foi=true; }}
		if(!foi) { c = getCubMod(xa-1,ya,za,xt,yt,zt); if(c!=null) if(c.ehCond) if(c.tip!=tCond0) if(c.life-q>=lm) { c.life-=q; foi=true; }}
		if(!foi) { c = getCubMod(xa,ya-1,za,xt,yt,zt); if(c!=null) if(c.ehCond) if(c.tip!=tCond0) if(c.life-q>=lm) { c.life-=q; foi=true; }}
		if(!foi) { c = getCubMod(xa,ya+1,za,xt,yt,zt); if(c!=null) if(c.ehCond) if(c.tip!=tCond0) if(c.life-q>=lm) { c.life-=q; foi=true; }}
		
		return foi;
	}
	public boolean decCond1Viz(int q, int lm, int xa,int ya,int za,int xt,int yt,int zt){
		// decrementa a quantidade "q" da el (=life) do cubo modelo cond# (exceto cond0)
		// retorna true se conseguiu decrementar tudo
		// NAO decrementa e retorna false se ficar abaixo do limite
		if(q<0) J.impErr("!parametro inválido: q="+q, "decCond1Viz()");
		Cub c = null;
		boolean foi = false;
		
		if(!foi) { c = getCubMod(xa,ya,za+1,xt,yt,zt); if(c!=null) if(c.ehCond) if(c.tip!=tCond0) if(c.life-q>=lm) { c.life-=q; foi=true; }}
		if(!foi) { c = getCubMod(xa,ya,za-1,xt,yt,zt); if(c!=null) if(c.ehCond) if(c.tip!=tCond0) if(c.life-q>=lm) { c.life-=q; foi=true; }}
		if(!foi) { c = getCubMod(xa+1,ya,za,xt,yt,zt); if(c!=null) if(c.ehCond) if(c.tip!=tCond0) if(c.life-q>=lm) { c.life-=q; foi=true; }}
		if(!foi) { c = getCubMod(xa-1,ya,za,xt,yt,zt); if(c!=null) if(c.ehCond) if(c.tip!=tCond0) if(c.life-q>=lm) { c.life-=q; foi=true; }}
		if(!foi) { c = getCubMod(xa,ya-1,za,xt,yt,zt); if(c!=null) if(c.ehCond) if(c.tip!=tCond0) if(c.life-q>=lm) { c.life-=q; foi=true; }}
		if(!foi) { c = getCubMod(xa,ya+1,za,xt,yt,zt); if(c!=null) if(c.ehCond) if(c.tip!=tCond0) if(c.life-q>=lm) { c.life-=q; foi=true; }}
		
		return foi;
	}
	public int getCondDisp(){
		for(int q=tCond0+1; q<=tCond9; q++)
			if(tCub.get(q).life==-1)
				return q;
		J.impErr("!Faixa de cond esgotada", "getCondDisp()");	
		return -1;	
	}

	public boolean inflamavel(int p){
		if(p!=comFogo(p)) return true;
		return false; // melhor q ficar colocando flags
	}
	public int comFogo(int p){
		// ?como fica "cozido" aqui??? Nao seria melhor aproveitar isso???
		if(p==0) return 0; 
		if(p==tMato) return tFogo; 
		if(p==tMato-1) return tFogo; // mato crescendo
		if(p==tTrigo) return tFogo; 
		if(p==tTrigo-1) return tFogo; // trigo crescendo
		
		if(p==getInd("folha_coq")) return tFogo; 
		if(p==getInd("coco")) return 0;  // melhor assim, daih nao fica coco voando
		
		if(p==getInd("grama")) return getInd("chamuscado");
		if(p==getInd("degrau_grama")) return getInd("degrau_chamuscado");
		if(p==getInd("caule_sequoia")) return getInd("caule_sequoia_fogo");
		if(p==getInd("caule_coq")) return getInd("caule_coq_fogo");
		if(p==getInd("caule_coq_fogo")) return getInd("chamuscado_fino_fogo");
		if(p==getInd("madeira")) return getInd("madeira_fogo");
		if(p==getInd("cacto_topo")) return getInd("cacto_fogo");
		if(p==getInd("cacto_base")) return getInd("cacto_fogo");
		if(p==getInd("cacto_fogo")) return getInd("chamuscado_fogo");
		if(p==getInd("degrau_madeira")) return getInd("degrau_madeira_fogo");
		if(p==getInd("degrau_caule")) return getInd("degrau_caule_fogo");
		if(p==getInd("degrau_caule_fogo")) return getInd("degrau_chamuscado_fogo");
		//if(p==getInd("chamuscado")) return getInd("chamuscado_fogo"); // ?isso eh bom???
		if(getCubMod(p).ehFolha) return getInd("folha_fogo");
		if(getCubMod(p).ehCaule) return getInd("caule_fogo"); // pegou seringueira com ranhura???
		if(p==getInd("caule_fogo")) return getInd("chamuscado_fogo");		
		return p;
	}
	public void resetJoeInv(){ // resetInv resetInvJoe limpaInv
			// seria melhor se desse bless: andar no mapa e, somente na carencia destes itens, eles fossem encontrados.
			joeInv.zInv();
			joeInv.addItm(60,"pah"); 
			joeInv.addItm(20,"machado"); 
			joeInv.addItm(60,"picareta");		
			joeInv.addItm(20,"pao");
			cCarrMao=20;
	}
	public void resetJoeBars(){
		joeLife = 90;
		joeAgua = 90;
		joeProt = 90;
		joeCarb = 90;
		joeVit = 90;
		joeAr = 90;
	}
	public int numMobsAtivos(){
		int c=0;
		for(int q=mob0; q<mob9; q++)
			if(tCub.get(q).ativo)
				c++;
		return c;	
	}
	public boolean temCubCol(String t, int alt, int xa, int ya, int za, int xt, int yt, int zt){
		// retorna true se existe algum cubo t na coluna de altura alt informada
		int tp = getInd(t);
		for(int q=0; q<=alt; q++)
			if(getCub(xa,ya+q,za,xt,yt,zt)==tp)
				return true;
		return false;	
	}
	public boolean temCubViz(String st, int xa, int ya, int za, int xt, int yt, int zt){
		return temCubViz(getInd(st),xa,ya,za,xt,yt,zt);
	}
	public boolean temCubViz(int v, int vv, int vvv, int xa, int ya, int za, int xt, int yt, int zt){
		// este eh triplo. (nao deixei 2 p nao confundir com inf)
		if(J.vou(getCub(xa,ya,za+1,xt,yt,zt),v,vv,vvv)) return true;
		if(J.vou(getCub(xa,ya,za-1,xt,yt,zt),v,vv,vvv)) return true;
		if(J.vou(getCub(xa+1,ya,za,xt,yt,zt),v,vv,vvv)) return true;
		if(J.vou(getCub(xa-1,ya,za,xt,yt,zt),v,vv,vvv)) return true;
		return false;		
	}
	public boolean temCubViz(int v, int xa, int ya, int za, int xt, int yt, int zt){
		if(getCub(xa,ya,za+1,xt,yt,zt)==v) return true;
		if(getCub(xa,ya,za-1,xt,yt,zt)==v) return true;
		if(getCub(xa+1,ya,za,xt,yt,zt)==v) return true;
		if(getCub(xa-1,ya,za,xt,yt,zt)==v) return true;
		return false;
	}
	public boolean sohTemCubViz(int v, int xa, int ya, int za, int xt, int yt, int zt){
		// todos ao lado tem q ser == v
		// nao conta acima e abaixo
		if(getCub(xa,ya,za+1,xt,yt,zt)==v) 
		if(getCub(xa,ya,za-1,xt,yt,zt)==v) 
		if(getCub(xa+1,ya,za,xt,yt,zt)==v) 
		if(getCub(xa-1,ya,za,xt,yt,zt)==v) 
			return true;
		return false;
	}	
	public int getRioViz(int xa, int ya, int za, int xt, int yt, int zt){
		// nao examina abaixo e acima (nao mecha nisso, ok? Deixa assim mesmo.)
		int v = 0;
		
		v = getCub(xa,ya,za+1,xt,yt,zt);
		if(tCub.get(v).opFazRio) return v;

		v = getCub(xa,ya,za-1,xt,yt,zt);
		if(tCub.get(v).opFazRio) return v;

		v = getCub(xa+1,ya,za,xt,yt,zt);
		if(tCub.get(v).opFazRio) return v;		
		
		v = getCub(xa-1,ya,za,xt,yt,zt);
		if(tCub.get(v).opFazRio) return v;				
		
		return 0;
	}	
		boolean 
			opSemAreiaCai=false, // se true, areias nao cairao ao ser plotadas. CUIDADO!!! Nao tem auto retorno, tem q ajustar manual.
			opPlotSobreSolo=false; // usei p s e e d m a t o		
	public void setCubArea(String nm, int vi, int xa, int ya, int za, int xxa, int yya, int zza, int xt, int yt, int zt){
		/*	PENSAR EM MUDAR O NOME:
					- setRet()
					- setMonolito()
		*/
		int v = getInd(nm);
		int w = 0;
		if(xa>xxa) {w=xa; xa=xxa; xxa=w; }
		if(ya>yya) {w=ya; ya=yya; yya=w; }
		if(za>zza) {w=za; za=zza; zza=w; }
		for(int m=xa; m<=xxa; m++)
		for(int n=ya; n<=yya; n++)
		for(int o=za; o<=zza; o++){
			setCub(v, vi, m,n,o,xt,yt,zt); // lembrando q pode extrapolar sim a coordenada q o sistema intera.
		}
	}
	public void setCubAreaSV(String nm, int vi, int xa, int ya, int za, int xxa, int yya, int zza, int xt, int yt, int zt){
		int v = getInd(nm);
		int w = 0;
		if(xa>xxa) {w=xa; xa=xxa; xxa=w; }
		if(ya>yya) {w=ya; ya=yya; yya=w; }
		if(za>zza) {w=za; za=zza; zza=w; }
		for(int m=xa; m<=xxa; m++)
		for(int n=ya; n<=yya; n++)
		for(int o=za; o<=zza; o++){
			setCubSV(v, vi, m,n,o,xt,yt,zt); // lembrando q pode extrapolar sim a coordenada q o sistema intera.
		}
	}
	public void trocaCubArea(String nma, String nmn, int xa, int ya, int za, int xxa, int yya, int zza, int xt, int yt, int zt){
		// este é um "setCubAreaSV()" mais incrementado
		if(nma==null) nma="nulo";
		if(nmn==null) nmn="nulo";
		int w = 0, v=0;
		int va = getInd(nma);
		int vn = getInd(nmn);
		if(xa>xxa) {w=xa; xa=xxa; xxa=w; }
		if(ya>yya) {w=ya; ya=yya; yya=w; }
		if(za>zza) {w=za; za=zza; zza=w; }			
		for(int m=xa; m<=xxa; m++)
		for(int n=ya; n<=yya; n++)
		for(int o=za; o<=zza; o++){
			v = getCub(m,n,o,xt,yt,zt); // lembrando q pode extrapolar sim a coordenada q o sistema intera.
			if(v==va) setCub(vn, m,n,o, xt,yt,zt);
		}		
	}
	public void trocaCubRai(String nma, String nmn, int r, int xa, int ya, int za, int xt, int yt, int zt){	
		trocaCubArea(nma, nmn, xa-r, ya-r, za-r, xa+r,ya+r,za+r, xt,yt,zt);
	}
	public boolean setCub(String st,int xa, int ya, int za, int xt, int yt, int zt){
		int v = getInd(st);
		if(v!=-1) return setCub(v,0, xa,ya,za,xt,yt,zt);
		return false;
	}
	public boolean setCub(int v, int xa, int ya, int za, int xt, int yt, int zt){
		return setCub(v,0,xa,ya,za,xt,yt,zt);
	}
	public boolean setCubSV(String st,int inf, int xa, int ya, int za, int xt, int yt, int zt){
		return setCubSV(getInd(st),inf,xa,ya,za,xt,yt,zt);
	}
	public boolean setCubSV(int v,int inf, int xa, int ya, int za, int xt, int yt, int zt){
		if(J.vou(getCub(xa,ya,za,xt,yt,zt),0,tAgua)){
			setCub(v,inf,xa,ya,za,xt,yt,zt);
			return true;
		}
		return false;
	}
	public boolean setCub(int v,int vi, int xa, int ya, int za, int xt, int yt, int zt){
		//v = comCai(v); // isso eh bom??? R: nem tanto. Bugou a areia.
		
		int q=tamTab;
		if(v!=tSeedPreencher)
		if(v!=tPreenchido)
		if(v!=tPlotar)
		if(v!=0){
			int vb = getCub(xa,ya-1,za,xt,yt,zt);
			if(vb==tPreenchido){
				trocaTudo(tPreenchido,v);
				trocaTudo(tSeedPreencher,v); // p acabar o processo, lembre q ctrl precisa ficar pressionado p alastrar
				return true;
			}
		}

		// apagando quando deletar lampada (tocha, fogueira e similares a caminho)
		if(v==0 || v==tLampadaOff) // mais tipos p acender depois llllllllllllll
		if(getCub(xa,ya,za, xt,yt,zt)==tLampadaOn){  // mais tipos p apagar de pois
			setLuzEsf(APAGAR, xa,ya,za, xt,yt,zt);
		}
		if(v==tLampadaOn){  // mais tipos p apagar de pois
			setLuzEsf(20, xa,ya,za, xt,yt,zt); // esse primeiro par tinha q estar numa constante
		}		
			
		// inundando quando remover blocos
		if(v==0){
			int vv = getRioViz(xa,ya,za,xt,yt,zt); // esta linha nao examina cma e bxo
			//if(vv!=0) v = emRioCorr(vv); // removi um bug p inserir um pior. Deu problema com o cubo " p l o t a r "
			if(vv!=0) 
				v = vv; // deixa assim mesmo
			else {
				int vc = getCub(xa,ya+1,za, xt,yt,zt);
				if(tCub.get(vc).opFazRio)
					v=vc;
			}		
		}

		// tratando cond#
		if(v==0) if(getCubMod(xa,ya,za,xt,yt,zt).ehCond){ 
			if(getCub(xa,ya,za,xt,yt,zt)!=tCond0){ // ?serah q vai pesar no clock???
				getCubMod(xa,ya,za,xt,yt,zt).life=-1; // p ficar disponivel
				int tp = getCub(xa,ya,za,xt,yt,zt);
				trocaTudo(tp,tCond0);
				for(Maq m: maq)	if(m.condCon.tip==tp) m.condCon=null;
			}
		}			
		
		int vb = getCub(xa,ya-1,za,xt,yt,zt);
		if(v==vb)
		if(getCubMod(v).ehDegrau){
			setCub(semDegrau(v),xa,ya-1,za,xt,yt,zt);
			return true; // serah q dá overflow???
		}

		if(opPlotSobreSolo){
			for(int r=0; r<altTab*tamArea; r++)
				if(getCub(xa,r,za,xt,yt,zt)==0){
					ya=r;
					break;
				}					
		}
			
		{ // correcoes de limites 
			if(xa>=tamArea) while(xa>=tamArea) {xa-=tamArea; xt++; };
			if(ya>=tamArea) while(ya>=tamArea) {ya-=tamArea; yt++; };
			if(za>=tamArea) while(za>=tamArea) {za-=tamArea; zt++; };
				
			if(xa<0) while(xa<0) {xa+=tamArea; xt--; };
			if(ya<0) while(ya<0) {ya+=tamArea; yt--; };
			if(za<0) while(za<0) {za+=tamArea; zt--; };
		}
		
		if(J.noInt(xt,0,tamTab-1))
		if(J.noInt(yt,0,altTab-1))
		if(J.noInt(zt,0,tamTab-1))
		if(J.noInt(xa,0,tamArea-1))
		if(J.noInt(ya,0,tamArea-1))
		if(J.noInt(za,0,tamArea-1))
		if(tab!=null)
		if(tab.are!=null){
			Area ar = tab.are[xt][yt][zt];
			if(ar!=null)
			if(ar.cel!=null)
			{				
				if(v==0){ // fazendo um buraco abaixo de areias
					int vc = getCub(xa,ya+1,za,xt,yt,zt);
					if(tCub.get(vc).ehAreia)
						setCub(comCai(vc),xa,ya+1,za,xt,yt,zt);
				}

				if(v==0){
					//int ca = ar.cel[xa][ya][za];
					ar.inf[xa][ya][za].dis = 0; // esse "dis" deve sumir. É sobre as conexoes eletricas. Já foram substituidas. Remover depois.
					
					// substituindo "dis"
					ar.inf[xa][ya][za].in=0; // abaixo eh importante. 
					ar.inf[xa][ya][za].is=0;
					ar.inf[xa][ya][za].il=0;
					ar.inf[xa][ya][za].io=0;
					ar.inf[xa][ya][za].ic=0;
					ar.inf[xa][ya][za].ib=0;
					//tCub.get(ca).ic=ib=in=is=il=io;					
				}
				
				if(v!=-1) ar.cel[xa][ya][za] = v;
				if(vi!=-1) ar.inf[xa][ya][za].inf = vi;
				ar.inf[xa][ya][za].opSupAgua=false; // deve ajudar 
				//ar.inf[xa][ya][za].dis = 0; // tah dando problema
				ar.jahSombreado=false; // desencadearah "c S o m b r e a r" automaticamente
				if(v!=tPlotar){
					ar.cCalcOc = tmpCalcOc; // jah desencadeara a verificacao de bordas automaticamente
					ar.e = 1;
					ar.cSave = 3;
				}

/*
				if(tCub.get(vs).ehMob){
					tCub.get(v).ativo = false;
					setCub(0,xa,ya,za,xt,yt,zt);
					// sumir com o mob, jah q vai ser sobreposto
				}
*/				
				if(v!=0) ar.ehAreaVazia=false;
				
				else { // precisa verificar bordas das outras areas vizinhas
					Area a = null;//                            precisa ser "2" aqui!
					a = getTarea(xt,yt+1,zt);	if(a!=null) {a.e=5; a.ajTmpBor(); }
					a = getTarea(xt,yt-1,zt);	if(a!=null) {a.e=5; a.ajTmpBor(); }
					a = getTarea(xt,yt,zt+1);	if(a!=null) {a.e=5; a.ajTmpBor(); }
					a = getTarea(xt,yt,zt-1);	if(a!=null) {a.e=5; a.ajTmpBor(); }
					a = getTarea(xt+1,yt,zt);	if(a!=null) {a.e=5; a.ajTmpBor(); }
					a = getTarea(xt-1,yt,zt);	if(a!=null) {a.e=5; a.ajTmpBor(); } 
				}
				return true;
			}
		}
		return false;		
	}
	public int getCubW(int xa, int ya, int za, int xw, int yw, int zw){
		// ?serah q isso funciona???
		for(int m=0; m<tamTab; m++)
		for(int n=0; n<altTab; n++)
		for(int o=0; o<tamTab; o++)
			if(tab.are[m][n][o].xw==xw)
			if(tab.are[m][n][o].yw==yw)
			if(tab.are[m][n][o].zw==zw)
				if(tab.are[m][n][o].cel!=null)
					return tab.are[m][n][o].cel[xa][ya][za];
		// ?seria legal abrir o arquivo??? Depois, talvez.
		return -1;
	}
	public int getCubDir(int d, int xa, int ya, int za, int xt, int yt, int zt){
		// retorna tip do cubo da direcao "d" a partir da coordenada dada
		// usei p valvulas, mas deve dar p adaptar p outros tb
		switch(d){
			case J.BXO: ya--; break;
			case J.CMA: ya++; break;
			case J.NOR: za++; break;
			case J.SUL: za--; break;
			case J.OES: xa--; break;
			case J.LES: xa++; break;
		}
		return getCub(xa,ya,za, xt,yt,zt);
	}
	public int getCub(int xa, int ya, int za, int xt, int yt, int zt){
		
		{ // correcoes de limites
			if(xa>=tamArea) while(xa>=tamArea) {xa-=tamArea; xt++; };
			if(ya>=tamArea) while(ya>=tamArea) {ya-=tamArea; yt++; };
			if(za>=tamArea) while(za>=tamArea) {za-=tamArea; zt++; };
				
			if(xa<0) while(xa<0) {xa+=tamArea; xt--; };
			if(ya<0) while(ya<0) {ya+=tamArea; yt--; };
			if(za<0) while(za<0) {za+=tamArea; zt--; };
		}

		
		if(J.noInt(xt,0,tamTab-1))
		if(J.noInt(yt,0,altTab-1))
		if(J.noInt(zt,0,tamTab-1))
		if(J.noInt(xa,0,tamArea-1))
		if(J.noInt(ya,0,tamArea-1))
		if(J.noInt(za,0,tamArea-1))
		if(tab!=null)
		if(tab.are!=null)	
		if(tab.are[xt][yt][zt]!=null)
		if(tab.are[xt][yt][zt].cel!=null)
		{
			return tab.are[xt][yt][zt].cel[xa][ya][za];
		}
		return 0;
	}
	public Cub getMob(int xa, int ya, int za, int xt, int yt, int zt){
		int v = getCub(xa,ya,za,xt,yt,zt);
		Cub c = tCub.get(v);
		if(c.ehMob) return c;
		return null;
	}
	public Cub getCubModDir(int d, int xa, int ya, int za, int xt, int yt, int zt){
		switch(d){
			case J.BXO: ya--; break;
			case J.CMA: ya++; break;
			case J.NOR: za++; break;
			case J.SUL: za--; break;
			case J.OES: xa--; break;
			case J.LES: xa++; break;
		}		
		return getCubMod(xa,ya,za,xt,yt,zt);
	}
	
	public void rodaInf(char ca, int min,int max, int xa,int ya,int za,int xt,int yt,int zt){
		if(ca=='+'){			
			incInf(xa,ya,za,xt,yt,zt);
			if(getInf(xa,ya,za,xt,yt,zt)>max)
				setInf(min,xa,ya,za,xt,yt,zt);
			return;
		}
		if(ca=='-'){			
			decInf(xa,ya,za,xt,yt,zt);
			if(getInf(xa,ya,za,xt,yt,zt)<min)
				setInf(max,xa,ya,za,xt,yt,zt);
			return;
		}		
		J.impErr("!parametro incorreto: |"+ca+"|", "rodaInf()");
	}
	public void incInfDir(int d, int xa, int ya, int za, int xt, int yt, int zt){
		switch(d){
			case J.CMA: ya++; break;
			case J.BXO: ya--; break;
			case J.NOR: za++; break;
			case J.SUL: za--; break;
			case J.LES: xa++; break;
			case J.OES: xa--; break;
		}
		incInf(xa,ya,za,xt,yt,zt);
	}
	public void decInfDir(int d, int xa, int ya, int za, int xt, int yt, int zt){
		switch(d){
			case J.CMA: ya++; break;
			case J.BXO: ya--; break;
			case J.NOR: za++; break;
			case J.SUL: za--; break;
			case J.LES: xa++; break;
			case J.OES: xa--; break;
		}
		decInf(xa,ya,za,xt,yt,zt);
	}
	public void setCubDir(int d, int tp, int xa, int ya, int za, int xt, int yt, int zt){
		switch(d){
			case J.CMA: ya++; break;
			case J.BXO: ya--; break;
			case J.NOR: za++; break;
			case J.SUL: za--; break;
			case J.LES: xa++; break;
			case J.OES: xa--; break;
		}
		setCub(tp, xa,ya,za,xt,yt,zt);
	}
	public boolean incInfLm(int u, int lm, int xa, int ya, int za, int xt, int yt, int zt){
		if(u==0) return true;
		int v = getInf(xa,ya,za,xt,yt,zt);
		if(v+u<=lm) {
			setInf(v+u, xa,ya,za,xt,yt,zt);
			return true;
		}
		return false;
	}	
	public boolean incInfLm(int lm, int xa, int ya, int za, int xt, int yt, int zt){
		int v = getInf(xa,ya,za,xt,yt,zt);
		if(v+1<=lm) {
			setInf(v+1, xa,ya,za,xt,yt,zt);
			return true;
		}
		return false;
	}
	public boolean incInf(int xa, int ya, int za, int xt, int yt, int zt){
		int v = getInf(xa,ya,za,xt,yt,zt);
		v+=1;
		return setInf(v,xa,ya,za,xt,yt,zt);
	}
	public boolean decInfLm(int lm, int xa, int ya, int za, int xt, int yt, int zt){
		int v = getInf(xa,ya,za,xt,yt,zt);
		if(v>lm){
			v--;
			setInf(v,xa,ya,za,xt,yt,zt);
			return true;
		}
		return false;
	}	
	public boolean decInfLm(int q, int lm, int xa, int ya, int za, int xt, int yt, int zt){
		int v = getInf(xa,ya,za,xt,yt,zt);
		v-=q;
		if(v>=lm){
			setInf(v,xa,ya,za,xt,yt,zt);
			return true;
		}
		return false;
	}	
	public boolean decInf(int q, int xa, int ya, int za, int xt, int yt, int zt){
		int v = getInf(xa,ya,za,xt,yt,zt);
		v-=q;
		return setInf(v,xa,ya,za,xt,yt,zt);
	}	
	public boolean decInf(int xa, int ya, int za, int xt, int yt, int zt){
		int v = getInf(xa,ya,za,xt,yt,zt);
		v-=1;
		return setInf(v,xa,ya,za,xt,yt,zt);
	}	
	
	public boolean setInf(int v, int xa, int ya, int za, int xt, int yt, int zt){

		int q=tamTab;
		
		for(int w=0; w<q; w++){
			if(ya>=tamArea){ya-=tamArea; yt++; }
			if(ya<0){ya+=tamArea; yt--; }
		}
		for(int w=0; w<q; w++){		
			if(xa>=tamArea){xa-=tamArea; xt++; }
			if(xa<0){xa+=tamArea; xt--; }		
		}
		for(int w=0; w<q; w++){		
			if(za>=tamArea){za-=tamArea; zt++; }
			if(za<0){za+=tamArea; zt--; }				
		}
		
		if(J.noInt(xt,0,tamTab-1))
		if(J.noInt(yt,0,altTab-1))
		if(J.noInt(zt,0,tamTab-1))
		if(J.noInt(xa,0,tamArea-1))
		if(J.noInt(ya,0,tamArea-1))
		if(J.noInt(za,0,tamArea-1))
		if(tab!=null)
		if(tab.are!=null)	
		if(tab.are[xt][yt][zt]!=null)
		if(tab.are[xt][yt][zt].inf!=null)
		{			
			tab.are[xt][yt][zt].inf[xa][ya][za].inf = v;
			return true;
		}
		return false;		
	}
	public boolean setDis(char d, int xa, int ya, int za, int xt, int yt, int zt){
		// deve ser removido futuramente.
		// obsoleto. Tava usando p mostrar a direcao de distorcao de canos e condutores eletricos. Só permitia uma "distorcao", eu precisava de 6, simultaneas ou nao. Já implementei.
		{ // correcoes de limites
			if(xa>=tamArea) while(xa>=tamArea) {xa-=tamArea; xt++; };
			if(ya>=tamArea) while(ya>=tamArea) {ya-=tamArea; yt++; };
			if(za>=tamArea) while(za>=tamArea) {za-=tamArea; zt++; };
				
			if(xa<0) while(xa<0) {xa+=tamArea; xt--; };
			if(ya<0) while(ya<0) {ya+=tamArea; yt--; };
			if(za<0) while(za<0) {za+=tamArea; zt--; };
		}

		if(J.noInt(xt,0,tamTab-1))
		if(J.noInt(yt,0,altTab-1))
		if(J.noInt(zt,0,tamTab-1))
		if(J.noInt(xa,0,tamArea-1))
		if(J.noInt(ya,0,tamArea-1))
		if(J.noInt(za,0,tamArea-1))
		if(tab!=null)
		if(tab.are!=null)	
		if(tab.are[xt][yt][zt]!=null)
		if(tab.are[xt][yt][zt].inf!=null)
		{			
			tab.are[xt][yt][zt].inf[xa][ya][za].dis = d;
			return true;
		}
		return false;		
	}
	public int getDis(int xa, int ya, int za, int xt, int yt, int zt){
		
		if(ya>=tamArea){ya-=tamArea; yt++; }
		if(ya<0){ya+=tamArea; yt--; }
		
		if(xa>=tamArea){xa-=tamArea; xt++; }
		if(xa<0){xa+=tamArea; xt--; }		
		
		if(za>=tamArea){za-=tamArea; zt++; }
		if(za<0){za+=tamArea; zt--; }				
		
		if(J.noInt(xt,0,tamTab-1))
		if(J.noInt(yt,0,altTab-1))
		if(J.noInt(zt,0,tamTab-1))
		if(J.noInt(xa,0,tamArea-1))
		if(J.noInt(ya,0,tamArea-1))
		if(J.noInt(za,0,tamArea-1))
		if(tab!=null)
		if(tab.are!=null)	
		if(tab.are[xt][yt][zt]!=null)
		if(tab.are[xt][yt][zt].inf!=null)
		{
			return tab.are[xt][yt][zt].inf[xa][ya][za].dis;
		}
		return 0;
	}
	public int getInf(int xa, int ya, int za, int xt, int yt, int zt){
		
		if(ya>=tamArea){ya-=tamArea; yt++; }
		if(ya<0){ya+=tamArea; yt--; }
		
		if(xa>=tamArea){xa-=tamArea; xt++; }
		if(xa<0){xa+=tamArea; xt--; }		
		
		if(za>=tamArea){za-=tamArea; zt++; }
		if(za<0){za+=tamArea; zt--; }				
		
		if(J.noInt(xt,0,tamTab-1))
		if(J.noInt(yt,0,altTab-1))
		if(J.noInt(zt,0,tamTab-1))
		if(J.noInt(xa,0,tamArea-1))
		if(J.noInt(ya,0,tamArea-1))
		if(J.noInt(za,0,tamArea-1))
		if(tab!=null)
		if(tab.are!=null)	
		if(tab.are[xt][yt][zt]!=null)
		if(tab.are[xt][yt][zt].inf!=null)
		{
			return tab.are[xt][yt][zt].inf[xa][ya][za].inf;
		}
		return 0;
	}
	public int getInfDir(int d, int xa, int ya, int za, int xt, int yt, int zt){
		switch(d){
			case J.CMA: ya++; break;
			case J.BXO: ya--; break;
			case J.NOR: za++; break;
			case J.SUL: za--; break;
			case J.LES: xa++; break;
			case J.OES: xa--; break;
		}
		return getInf(xa,ya,za,xt,yt,zt);
	}

	public int getLife(int xa, int ya, int za, int xt, int yt, int zt){
		int t = getCub(xa,ya,za,xt,yt,zt);
		return tCub.get(t).life;
	}
	public void setLife(int q, int xa, int ya, int za, int xt, int yt, int zt){
		int t = getCub(xa,ya,za,xt,yt,zt);
		tCub.get(t).life = q;
	}
	public void incLife(int xa, int ya, int za, int xt, int yt, int zt){
		int v = getLife(xa,ya,za,xt,yt,zt);
		setLife(v+1,xa,ya,za,xt,yt,zt);
	}
	public void decLife(int xa, int ya, int za, int xt, int yt, int zt){
		int v = getLife(xa,ya,za,xt,yt,zt);
		setLife(v-1,xa,ya,za,xt,yt,zt);
	}	
	public boolean setLuzTocha(int v, int xa, int ya, int za, int xt, int yt, int zt){
		// nivel de luz de um cubo especifico
		int q=tamTab;

		for(int w=0; w<q; w++){
			if(ya>=tamArea){ya-=tamArea; yt++; }
			if(ya<0){ya+=tamArea; yt--; }
		}
		for(int w=0; w<q; w++){		
			if(xa>=tamArea){xa-=tamArea; xt++; }
			if(xa<0){xa+=tamArea; xt--; }		
		}
		for(int w=0; w<q; w++){		
			if(za>=tamArea){za-=tamArea; zt++; }
			if(za<0){za+=tamArea; zt--; }				
		}
		
		if(J.noInt(xt,0,tamTab-1))
		if(J.noInt(yt,0,altTab-1))
		if(J.noInt(zt,0,tamTab-1))
		if(J.noInt(xa,0,tamArea-1))
		if(J.noInt(ya,0,tamArea-1))
		if(J.noInt(za,0,tamArea-1))
		if(tab!=null)
		if(tab.are!=null)	
		if(tab.are[xt][yt][zt]!=null)
		if(tab.are[xt][yt][zt].inf!=null)
		{			
			tab.are[xt][yt][zt].inf[xa][ya][za].setLuz2(v); // luz de tocha
			tab.are[xt][yt][zt].cSombrear = tmpSombrear;
			return true;
		}
		return false;		
	}

	public InfImp getInfImp(int xa, int ya, int za, int xt, int yt, int zt){
		
		if(ya>=tamArea){ya-=tamArea; yt++; }
		if(ya<0){ya+=tamArea; yt--; }
		
		if(xa>=tamArea){xa-=tamArea; xt++; }
		if(xa<0){xa+=tamArea; xt--; }		
		
		if(za>=tamArea){za-=tamArea; zt++; }
		if(za<0){za+=tamArea; zt--; }				
		
		if(J.noInt(xt,0,tamTab-1))
		if(J.noInt(yt,0,altTab-1))
		if(J.noInt(zt,0,tamTab-1))
		if(J.noInt(xa,0,tamArea-1))
		if(J.noInt(ya,0,tamArea-1))
		if(J.noInt(za,0,tamArea-1))
		if(tab!=null)
		if(tab.are!=null)	
		if(tab.are[xt][yt][zt]!=null)
		if(tab.are[xt][yt][zt].inf!=null)
		{
			return tab.are[xt][yt][zt].inf[xa][ya][za];
		}
		return null;
	}
	public int getLc(int xa, int ya, int za, int xt, int yt, int zt){
		// retorna a iluminacao da face superior do cubo em questao
		// usei p sombreamento da mao do joe, q precisava reagir a iluminacao local
		// (mas acho q a iluminacao da mao precisa ser revista, nao sei se jah o fiz)
		
		if(ya>=tamArea){ya-=tamArea; yt++; }
		if(ya<0){ya+=tamArea; yt--; }
		
		if(xa>=tamArea){xa-=tamArea; xt++; }
		if(xa<0){xa+=tamArea; xt--; }		
		
		if(za>=tamArea){za-=tamArea; zt++; }
		if(za<0){za+=tamArea; zt--; }				
		
		if(J.noInt(xt,0,tamTab-1))
		if(J.noInt(yt,0,altTab-1))
		if(J.noInt(zt,0,tamTab-1))
		if(J.noInt(xa,0,tamArea-1))
		if(J.noInt(ya,0,tamArea-1))
		if(J.noInt(za,0,tamArea-1))
		if(tab!=null)
		if(tab.are!=null)	
		if(tab.are[xt][yt][zt]!=null)
		if(tab.are[xt][yt][zt].inf!=null)
		{
			return tab.are[xt][yt][zt].inf[xa][ya][za].lc;
		}
		return 0;
	}

	public boolean temSwitchOffViz(int xa, int ya, int za, int xt, int yt, int zt){
		if(tCub.get(getCub(xa,ya,za+1,xt,yt,zt)).ehSwitchOff) return true;
		if(tCub.get(getCub(xa,ya,za-1,xt,yt,zt)).ehSwitchOff) return true;
		if(tCub.get(getCub(xa+1,ya,za,xt,yt,zt)).ehSwitchOff) return true;
		if(tCub.get(getCub(xa-1,ya,za,xt,yt,zt)).ehSwitchOff) return true;
		if(tCub.get(getCub(xa,ya+1,za,xt,yt,zt)).ehSwitchOff) return true;
		if(tCub.get(getCub(xa,ya-1,za,xt,yt,zt)).ehSwitchOff) return true;
		return false;
	}
	public boolean temSwitchOnViz(int xa, int ya, int za, int xt, int yt, int zt){
		if(tCub.get(getCub(xa,ya,za+1,xt,yt,zt)).ehSwitchOn) return true;
		if(tCub.get(getCub(xa,ya,za-1,xt,yt,zt)).ehSwitchOn) return true;
		if(tCub.get(getCub(xa+1,ya,za,xt,yt,zt)).ehSwitchOn) return true;
		if(tCub.get(getCub(xa-1,ya,za,xt,yt,zt)).ehSwitchOn) return true;
		if(tCub.get(getCub(xa,ya+1,za,xt,yt,zt)).ehSwitchOn) return true;
		if(tCub.get(getCub(xa,ya-1,za,xt,yt,zt)).ehSwitchOn) return true;
		return false;
	}	
	public boolean ehConEl(int xa, int ya, int za, int xt, int yt, int zt){
		int v = getCub(xa,ya,za,xt,yt,zt);
		if(tCub.get(v).ehConEl) return true;
		// limites???
		return false;
	}		

	public boolean setOc(char op, int xa, int ya, int za, int xt, int yt, int zt){
		// op: "N": oculta norte; "n" exibe norte.
		// "T" oculta tudo; "t" exibe tudo
		// maiusculas ocultam
		// use n s l o c b t
		
		// ateh daria p adaptar op p String, mas acho q consumiria mais clock		

		{ // correcoes de limites 
			if(xa>=tamArea) while(xa>=tamArea) {xa-=tamArea; xt++; };
			if(ya>=tamArea) while(ya>=tamArea) {ya-=tamArea; yt++; };
			if(za>=tamArea) while(za>=tamArea) {za-=tamArea; zt++; };
				
			if(xa<0) while(xa<0) {xa+=tamArea; xt--; };
			if(ya<0) while(ya<0) {ya+=tamArea; yt--; };
			if(za<0) while(za<0) {za+=tamArea; zt--; };
		}
		

		if(op=='T') J.impErr("!melhor rever este parametro: ["+op+"]", "setOc()");
		boolean ot = op=='O'; // oculta tudo e exibe tudo
		boolean et = op=='o';
		
		if(J.noInt(xt,0,tamTab-1))
		if(J.noInt(yt,0,altTab-1))
		if(J.noInt(zt,0,tamTab-1))
		if(J.noInt(xa,0,tamArea-1))
		if(J.noInt(ya,0,tamArea-1))
		if(J.noInt(za,0,tamArea-1))
		if(tab!=null)
		if(tab.are!=null)	
		if(tab.are[xt][yt][zt]!=null)
		if(tab.are[xt][yt][zt].inf!=null)
		{
			if(ot || op=='C') tab.are[xt][yt][zt].inf[xa][ya][za].ocCma = true;
			if(et || op=='c') tab.are[xt][yt][zt].inf[xa][ya][za].ocCma = false;

			if(ot || op=='B') tab.are[xt][yt][zt].inf[xa][ya][za].ocBxo = true;
			if(et || op=='b') tab.are[xt][yt][zt].inf[xa][ya][za].ocBxo = false;

			if(ot || op=='N') tab.are[xt][yt][zt].inf[xa][ya][za].ocNor = true;
			if(et || op=='n') tab.are[xt][yt][zt].inf[xa][ya][za].ocNor = false;

			if(ot || op=='S') tab.are[xt][yt][zt].inf[xa][ya][za].ocSul = true;
			if(et || op=='s') tab.are[xt][yt][zt].inf[xa][ya][za].ocSul = false;
			
			if(ot || op=='L') tab.are[xt][yt][zt].inf[xa][ya][za].ocLes = true;
			if(et || op=='l') tab.are[xt][yt][zt].inf[xa][ya][za].ocLes = false;

			if(ot || op=='O') tab.are[xt][yt][zt].inf[xa][ya][za].ocOes = true;
			if(et || op=='o') tab.are[xt][yt][zt].inf[xa][ya][za].ocOes = false;
			
			return true;
		}
		return false;		
	}
	
		

	
// ===================================================


	public static void main(String aaa[]){
	  new JoeCraft_part2();
	}
  public JoeCraft_part2(){
	  painel = new Painel();
		painel.setBackground(J.cor[1]);
		add(painel);
		addKeyListener(this);
	  setSize(J.maxXf, J.maxYf);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setExtendedState(MAXIMIZED_BOTH);
		setVisible(true);
		J.iniPlt("jf1/joe3d_09.plt"); // melhor assim
		iniAll();
	}

// === CALIBRAGEM DO FRAME-RATE =============
	int 
		opDistImpDt=2; // imprimir detalhes apenas nas areas vizinhas ao joe. Quanto maior este numero, mais lento fica o frame-rate. Em "2", apenas as duas areas contiguas vizinhas a area do joe terao j3ds impressos.


// === PLOTAGENS ================================

			Jf1 fCirc = new Jf1("circ03.jf1"); // p esferas, com bordas de valor maior
			Jf1 fCircLuz = new Jf1("circ26.jf1"); // p luzes, com interior de valor maior
	public void plotPir(int t, int alt, int xa, int ya, int za, int xt, int yt, int zt){
		ya--;
		for(int n=0; n<alt; n++)
		for(int m=-n; m<+n; m++)
		for(int o=-n; o<+n; o++)
			setCub(t, m+xa,alt-n+ya,o+za, xt,yt,zt);
	}
	public void plotCol(String st, int alt, int xa, int ya, int za, int xt, int yt, int zt){
		// comeca exatamente em ya, cuidado p nao sobrescrever algum bloco
		int v = getInd(st);
		for(int q=0; q<alt; q++)
			setCub(v,xa,ya+q,za, xt,yt,zt);
	}
	public void plotTora(String st, int d,int tam, int xa, int ya, int za,  int xt, int yt, int zt){
		// coluna na horizontal
		int v = getInd(st);
		for(int q=0; q<tam; q++){
			switch(d){
				case NOR: setCub(v,xa,ya,za+q, xt,yt,zt); break;
				case SUL: setCub(v,xa,ya,za-q, xt,yt,zt); break;
				case LES: setCub(v,xa+q,ya,za, xt,yt,zt); break;
				case OES: setCub(v,xa-q,ya,za, xt,yt,zt); break;
			}
		}
	}	
	public void trocaEsf(String st, int r, int xa, int ya, int za, int xt, int yt, int zt){
		int v = getInd(st);
		for(int m=1; m<=20; m++)
		for(int n=1; n<=20; n++)
		for(int o=1; o<=20; o++){
			if(getCub(xa+m-10,ya+n-10,za+o-10, xt,yt,zt)!=0)
			if(fCirc.cel[m][n]<r)
			if(fCirc.cel[n][o]<r)
				setCub(v,xa+m-10,ya+n-10,za+o-10, xt,yt,zt);
		}
	}
	public void plotEsf(String st, int r, int xa, int ya, int za, int xt, int yt, int zt){
		int v = getInd(st);
		for(int m=1; m<=20; m++)
		for(int n=1; n<=20; n++)
		for(int o=1; o<=20; o++){
			if(fCirc.cel[m][n]<r)
			if(fCirc.cel[n][o]<r)
				setCub(v,xa+m-10,ya+n-10,za+o-10, xt,yt,zt);
		}
	}
	public void plotCacto(String st, int xa, int ya, int za, int xt,int yt, int zt){
		// seria legal o cacto meio ou cacto topo depois
		int t=getInd(st);
		int tt=t;
		if(t==tCactoTopo) {t=tCactoBase; tt=tCactoTopo; } // idem p cac zetta depois
		setCub(t,xa,ya,za,xt,yt,zt);
		setCub(t,xa,ya+1,za,xt,yt,zt);
		setCub(t,xa,ya+2,za,xt,yt,zt);
		setCub(tt,xa,ya+3,za,xt,yt,zt);
	}

	
	public void plotArv(String ref, int xa, int ya, int za, int xt,int yt, int zt){
		// se nao encontrar, getInd() dah erro, lista os cubos e sai do programa
		plotArv(getInd(ref),xa,ya,za,xt,yt,zt);
	}
	public void plotArv(int ref, int xa, int ya, int za, int xt,int yt, int zt){
		String cau="caule";
		String fol="folha";
		int alt = 6+J.R(3);		
		
		if(ref==getInd("folha_cel")) { 
			fol="folha_cel";
			cau="caule_cel";
		}
		if(ref==getInd("folha_seringueira")){ 
			fol="folha_seringueira"; 
			cau="caule_seringueira"; 
		}	
		if(ref==getInd("folha_sequoia")){			
			int tc = getInd("caule_sequoia"), tf = getInd("folha_sequoia");
			alt = 18+J.R(9);
			if(alt>altTab*tamArea) alt = altTab*tamArea;
		
			for(int q=0; q<alt; q++){
				setCub(tc, xa,ya+q,za,xt,yt,zt);
				if(q>(alt>>2))
					if(q%2==0)
							plotCircSV(tf,2+J.R(2),xa+J.RS(2),ya+q,za+J.RS(2),xt,yt,zt);
			}
			
			int l = 4+J.R(3);
			plotCirc(fol,l-1,xa,ya+alt,za,xt,yt,zt);
			plotCirc(fol,l-0,xa,ya+alt-1,za,xt,yt,zt);
			plotCirc(fol,l-2,xa,ya+alt-2,za,xt,yt,zt);
			plotCirc(fol,l-3,xa,ya+alt-3,za,xt,yt,zt);			
			return;			
		}
		if(ref==getInd("folha_macieira")){
			fol = "folha_macieira";
			cau = "caule_macieira";
		}
		if(ref==getInd("folha_coq")){
			int tc = getInd("caule_coq");
			alt = 5+J.R(3);
			for(int q=0; q<alt; q++)
				setCub(tc,xa,ya+q,za,xt,yt,zt);
			setCub("folha_coq",xa,ya+alt,za,xt,yt,zt);			
			return;
		}
		if(ref==getInd("folha_bananeira")){
			int tc = getInd("caule_bananeira");
			alt = 3+J.R(2);
			for(int q=0; q<alt; q++)
				setCub(tc,xa,ya+q,za,xt,yt,zt);
			setCub("folha_bananeira",xa,ya+alt,za,xt,yt,zt);			
			return;
		}

		if(ref==-1) { // randomizar silvestre
			fol = J.rndSt(
				"folha_amarela",
				"folha_rosa",
				"folha_branca",
				"folha_laranja",
				"folha_clara");
		}
		plotEsf(fol,4+J.R(2),xa,ya+alt,za,xt,yt,zt); // tah certo isso???
		plotCol(cau,alt-1,xa,ya,za,xt,yt,zt);		
	}
	public void plotPortal(int t, int xa, int ya, int za, int xt,int yt, int zt){
		// poderia variar o sentido aqui
	
// DEPOIS:	
//		if(t==getInd("areia")) bor = "rocha_deserto";
//		if(t==getInd("areia_zetta")) bor = "rocha_zetta";
		
/*
			if(sobPeh==getInd("rocha")) st = "vanilla";
			if(sobPeh==getInd("nuvem")) st = "aether";
			if(sobPeh==getInd("rocha_zetta")) st = "zetta";
			if(sobPeh==getInd("rocha_magma")) st = "nether";
			if(sobPeh==getInd("rocha_deserto")) st = "desert";
			if(sobPeh==getInd("rocha_marciana")) st = "marte";
			if(sobPeh==getInd("rocha_lunar")) st = "lua";

*/		

		plotCol("portal_NS",3,xa,ya+1,za,xt,yt,zt);
		plotCol("portal_NS",3,xa+1,ya+1,za,xt,yt,zt);
		plotCol("obsidian",4,xa-1,ya,za,xt,yt,zt);
		plotCol("obsidian",4,xa+2,ya,za,xt,yt,zt);
		plotTora("obsidian",LES,4,xa-1,ya+4,za,xt,yt,zt);
		plotTora("obsidian",LES,4,xa-1,ya,za,xt,yt,zt);
	}
	public void plotCirc(String st, int r, int xa, int ya, int za, int xt, int yt, int zt){
		plotCirc(getInd(st),r,xa,ya,za,xt,yt,zt);
	}
	public void plotCirc(int v, int r, int xa, int ya, int za, int xt, int yt, int zt){
		if(r<=0) return;
		for(int m=1; m<=20; m++)
		for(int o=1; o<=20; o++){
			if(fCirc.cel[m][o]<=r)
				setCub(v,xa+m-10,ya,za+o-10, xt,yt,zt);
		}
	}
	public void plotCircSV(int v, int r, int xa, int ya, int za, int xt, int yt, int zt){
		if(r<=0) return;
		for(int m=1; m<=20; m++)
		for(int o=1; o<=20; o++){
			if(fCirc.cel[m][o]<=r)
			if(0==getCub(xa+m-10,ya,za+o-10, xt,yt,zt))
				setCub(v,xa+m-10,ya,za+o-10, xt,yt,zt);
		}
	}
	
	public boolean temAreaVizNula(int xt, int yt, int zt){
		if(tab!=null){
			
			if(yt>=altTab) return false; // ??? p retirar bug. Deu certo??? Parece q resolveu.
				
			if(xt>0) 
				if(tab.are[xt-1]
				[yt]
				[zt].cel==null) return true;
			if(yt>0) if(tab.are[xt][yt-1][zt].cel==null) return true;
			if(zt>0) if(tab.are[xt][yt][zt-1].cel==null) return true;
			
			if(xt<tamTab-1) if(tab.are[xt+1][yt][zt].cel==null) return true;
			if(yt<altTab-1) if(tab.are[xt][yt+1][zt].cel==null) return true;
			if(zt<tamTab-1) if(tab.are[xt][yt][zt+1].cel==null) return true;
			
			// diagonais
			if(xt>0) if(zt>0)	
				if(tab.are[xt-1][yt][zt-1].cel==null) return true;
			if(xt<tamTab-1) if(zt>0)	
				if(tab.are[xt+1][yt][zt-1].cel==null) return true;
			if(xt>0) if(zt<tamTab-1)	
				if(tab.are[xt-1][yt][zt+1].cel==null) return true;
			if(xt<tamTab-1) if(zt<tamTab-1)	
				if(tab.are[xt+1][yt][zt+1].cel==null) return true;			
			
			
		}
		return false;		
	}
	public void plotPlatAether(int xa, int ya, int za, int xt,int yt, int zt){		
		int 
			r = 4+J.R(10), 
			q = 4,
			nuv = getInd("nuvem"),
			sol = getInd("grama_cel");
		if(tab!=null)
		if(J.noInt(xt,1,tamTab-2))
		if(J.noInt(zt,1,tamTab-2))
		//if(J.noInt(yt,1,altTab-2))
		if(!temAreaVizNula(xt,yt,zt))
		{
			setCub(0, xa,ya,za,xt,yt,zt);
			for(int w=0; w<3; w++){
				plotCirc(nuv, r+1-w, xa,ya-w,za,xt,yt,zt);
				plotCirc(nuv, r+1-w-1, xa,ya-w-1,za,xt,yt,zt);
				plotCirc(sol, r-w, xa,ya-w,za,xt,yt,zt);
			}
			if(J.B(20))
				setCub(tSeedPorSolo, xa+J.RS(q),ya+1,za+J.RS(q),xt,yt,zt);
			if(J.B(30))
				setCub(tSeedPlatAether, xa+J.RS(q),ya+J.RS(q),za+J.RS(q),xt,yt,zt);
		}
	}
		
// === AREAS ================================		
		final boolean
			SAVE=true, OPEN=false;
		final int 
			tamCel2d = 3, 
			tamArea=10, // 0..9, essa eh a largura da area 3d, o numero de celulas q ela contem
			tamAre2d = tamCel2d*tamArea,
			tmpCalcOc=1,
			tmpMapArqs=3, tmpBor=3,
			tmpSombrear=6,
			tmpSmoothArea=-1;
		int 
			opDensArv=1000, // entre 30 e 1000; 30 eh bem denso: muitas arvores por area; Menor q isso fica ruim.
			numAreaImp=0, 
			numAreaVazia=0,
			numAreaOculta=0,
			opLuzAltura=0;	// variar a luz em funcao da altura do cubo
		boolean 
			opSaveArea=false,
			opRealParal=false;
	public Area getTareaNN(int i, int j, int k){
		// facilita em alguns ifs lah p baixo
		Area a = getTarea(i,j,k);
		if(a!=null) return a;
		return new Area(intNull,intNull,intNull);		
	}
	public Area getTarea(int i, int j, int k){
		if(tab==null) return null;
		if(J.noInt(i,0,tamTab-1))
		if(J.noInt(j,0,altTab-1))
		if(J.noInt(k,0,tamTab-1)){
			if(tab.are[i][j][k].cel==null) return null; // ainda nao carregada
			return tab.are[i][j][k];
		}
		return null;// nao achou
	}
	public Area getWarea(int i, int j, int k){
		// no futuro poderah retornar o arquivo carregado/criado, mas beeeem depois.
		
		// deve ter um loop de coordenada de mundo aqui!
		if(i>=maxXmap) i-=maxXmap;
		if(k>=maxZmap) k-=maxZmap;
		if(i<0) i+=maxXmap;
		if(k<0) k+=maxZmap;
		
		if(tab==null) return null;
		Area a = null;
		for(int m=0; m<tamTab; m++)
		for(int n=0; n<altTab; n++)
		for(int o=0; o<tamTab; o++){
			a = tab.are[m][n][o];
			if(a!=null)	
			if(a.xw==i)
			if(a.yw==j)
			if(a.zw==k)
				if(a.cel!=null)// isso eh bom???
					return a;
		}			
		return null;// nao achou
	}
	public void iniExp(int xa,int ya,int za,int xt,int yt,int zt){
		int r=5+J.RS(2), v=0;
		for(int m=1; m<=20; m++)
		for(int n=1; n<=20; n++)
		for(int o=1; o<=20; o++){
			if(fCirc.cel[m][n]<r)
			if(fCirc.cel[n][o]<r){
				v = getCub(xa+m-10,ya+n-10,za+o-10, xt,yt,zt);
				if(J.vou(v,tTnt,tTntOn)) v=tSeedExp;
				else v = tExp;
				setCub(v,xa+m-10,ya+n-10,za+o-10, xt,yt,zt);
			}
		}		
	}
	
	public class Area{ 

			int[][][] cel = null;		
			InfImp[][][] inf = null;
			boolean 
				jahSombreado = false,
				ehAreaVazia=false, // interno, nao mecha com isso.
				ehAreaOculta=false; // interno, nao mecha com isso.

			int xw=intNull, yw=intNull, zw=intNull,
				cVobNor=tmpBor, cVobSul=tmpBor+1, 
				cVobLes=tmpBor+2, cVobOes=tmpBor+3,
				cVobCma=tmpBor+4, cVobBxo=tmpBor+5,
				cSombrear=tmpSombrear,
				cSmoothArea=0, // somente areas criadas precisam disso, areas consultadas nao
				cCalcOc=0, cSave=0, 
				xImp=0, yImp=0, zImp=0,
				opBioma=0, // 0=vanilla, outros depois
				e=0,ce=0;// p maquina de estados
					
					

// aaaaaaaaaaa
		public Area(int i, int j, int k){
			
			// corrigindo coords negativas
			if(i<0)
			for(int q=0; q<1000; q++){
				if(i<0) i+=maxXmap;
				if(i>0) break;
			}
			if(k<0)
			for(int q=0; q<1000; q++){
				if(k<0) k+=maxZmap;
				if(k>0) break;
			}			
			
			i = i % maxXmap;
			k = k % maxZmap;
			xw=i; yw=j; zw=k;
		}	
		public void ajTmpBor(){
			cVobNor=tmpBor+J.R(tmpBor);
			cVobSul=tmpBor+J.R(tmpBor);
			cVobLes=tmpBor+J.R(tmpBor);
			cVobOes=tmpBor+J.R(tmpBor);
			cVobCma=tmpBor+J.R(tmpBor);
			cVobBxo=tmpBor+J.R(tmpBor);
		}
		public void smoothArea(){
if(1==1) return;
			if(cel==null) {
				cSmoothArea = tmpSmoothArea;
				return;
			}
				
			boolean vai=false;
			int v=0, vd=0;
				
			for(int m=1; m<tamArea-1; m++)
			for(int n=1; n<tamArea-1; n++)
			for(int o=1; o<tamArea-1; o++){
				v = cel[m][n][o];
				if(v==0) continue;
				
				vai=false;


				     if(cel[m][n][o+1]==0) vai=true;
				else if(cel[m][n][o-1]==0) vai=true;
				else if(cel[m+1][n][o]==0) vai=true;
				else if(cel[m-1][n][o]==0) vai=true;
			
				if(cel[m][n+1][o]!=0) vai=false;
			
				if(vai) {
					vd = comDegrau(v);
					// ABAIXO BUGA O MAPA!
					// if(cel[m][n-1][o]==vd) vd=0;
					cel[m][n][o] = vd;
				}
			}
		}
		public boolean temCub(String p){
			for(Cub q:tCub){
				if(J.iguais(q.name,p))
					return true;
			}
			J.esc("aviso: cubo nao encontrado: |"+p+"|");
			return false;
		}
		
		

		public void reg(){
			if(cSave>0){
				cSave--; // save area tem prioridade
				if(cSave==0) { // melhor nao avaliar frame Rate aqui
					saveOpenArea(SAVE, stCamAres(xw,yw,zw)+".are");
				}
			}
			if(cCalcOc>0){
				cCalcOc--;
				if(cCalcOc==0) 
				if(frameRate<lmFrameRate){
					calcOc();
					ajTmpBor();
					// cVobNor=2; cVobSul=3;
					// cVobLes=4; cVobOes=5;
					// cVobCma=6; cVobBxo=7;
				} else cCalcOc = tmpCalcOc;
			}
			if(cSmoothArea>0){
				cSmoothArea--;
				if(cSmoothArea<=0){
					if(frameRate>lmFrameRate)
						cSmoothArea=tmpSmoothArea;
					else smoothArea();
				}
			}
			if(!jahSombreado)	if(cSombrear<=0)	cSombrear = tmpSombrear;
			if(cSombrear>0){
				cSombrear--;				
				if(cSombrear<=0)
				if(frameRate<lmFrameRate){
					sombrear();		
					jahSombreado=true;
				} else { 
					cSombrear = tmpSombrear;
					jahSombreado=false;
				}
			}
			int tmp=6;
			if(ce>0) ce--;
			switch(e){
				case 0: 
					if(inf==null) zInf();				
					if(frameRate<lmFrameRate)
						verArq();
					if(cel!=null) { e=1; ce=tmp; }
					break;
				case 1: // esperar
					if(ce==0) { e=2; ce=tmp; }
					break;
				case 2:
					//smoothArea(); // serah q ajudou aqui mesmo? Sem contador???
					// nao ajudou. Tah bugando degraus.
					e=3;
					break;
				case 3:
					calcOc();
					e=4;
					break;
				case 4:// esperar
					if(ce==0) { e=5; ce=tmp; }				
					break;
				case 5:
					ajTmpBor();
					// cVobNor=tmpBor;
					// cVobSul=tmpBor+1;
					// cVobLes=tmpBor+2;
					// cVobOes=tmpBor+3;
					// cVobCma=tmpBor+4;
					// cVobBxo=tmpBor+5;	
					e=6;
					break;
				case 6:
					if(cVobNor>0){ cVobNor--;	if(cVobNor==1) vobNor(); }
					if(cVobSul>0){ cVobSul--;	if(cVobSul==1) vobSul(); }
					if(cVobLes>0){ cVobLes--;	if(cVobLes==1) vobLes(); }
					if(cVobOes>0){ cVobOes--;	if(cVobOes==1) vobOes(); }
					if(cVobCma>0){ cVobCma--;	if(cVobCma==1) vobCma(); }
					if(cVobBxo>0){ cVobBxo--;	if(cVobBxo==1) vobBxo(); }
					
					if(cVobNor==-2)
					if(cVobSul==-2)
					if(cVobLes==-2)
					if(cVobOes==-2)
					if(cVobCma==-2)
					if(cVobBxo==-2)
						e = 7;

				
					break;
				case 7:// esperar
					if(ce==0) { e=8; ce=tmp; }
					break;
				case 8: // bordas
					e=9; 				
					break;
			}
		}
		public void vobCma(){
			if(yw==altTab-1) {
				cVobCma=-2; // esse eh diferente
				return;
			}			
			Area a = getWarea(xw,yw+1,zw); 
			//Area a = getTarea(xt,yt+1,zt); 
			if(a==null) {
				cVobCma = tmpBor+J.R(tmpBor); // reagendando
				return;
			}	
			
			
			int v = 0;
			for(int m=0; m<tamArea; m++)
			for(int o=0; o<tamArea; o++){
				v = a.cel[m][0][o];
				if(v<0)continue;
				if(v>tCub.size()-1) continue;
				
				inf[m][tamArea-1][o].ocCma = tCub.get(v).ocultador;
				
			}				
			cVobCma=-2;
		}		
		public void vobBxo(){
			if(yw==0) {
				cVobBxo=-2; // esse eh diferente
				return;
			}
			Area a = getWarea(xw,yw-1,zw); 
			//Area a = getTarea(xt,yt-1,zt); 
			if(a==null) {
				cVobBxo = tmpBor+J.R(tmpBor); // reagendando
				return;
			}	
			
			
			int v = 0;
			for(int m=0; m<tamArea; m++)
			for(int o=0; o<tamArea; o++){
				v = a.cel[m][tamArea-1][o];
				if(v<0)continue;
				if(v>tCub.size()-1) continue;
				
				inf[m][0][o].ocBxo = tCub.get(v).ocultador;
			}				
			cVobBxo=-2;
		}				
		public void vobNor(){
			Area a = getWarea(xw,yw,zw+1); 
			//Area a = getTarea(xt,yt,zt+1); 
			if(a==null) {
				cVobNor = tmpBor+J.R(tmpBor); // reagendando
				return;
			}

						
			
			
			int v = 0;
			for(int m=0; m<tamArea; m++)
			for(int n=0; n<tamArea; n++){
				v = a.cel[m][n][0];
				if(v<0)continue;
				if(v>tCub.size()-1) continue;
				inf[m][n][tamArea-1].ocNor = tCub.get(v).ocultador;
			}				
			cVobNor=-2; // valendo 2, equivale a "jah verificado"
		}
		public void vobSul(){
			Area a = getWarea(xw,yw,zw-1); 
			//Area a = getTarea(xt,yt,zt-1); 
			if(a==null) {
				cVobSul = tmpBor+J.R(tmpBor); // reagendando
				return;
			}	
			
			int v = 0;
			for(int m=0; m<tamArea; m++)
			for(int n=0; n<tamArea; n++){
				v = a.cel[m][n][tamArea-1];
				if(v<0)continue;
				if(v>tCub.size()-1) continue;
				inf[m][n][0].ocSul = tCub.get(v).ocultador;
			}				
			cVobSul=-2;			
		}		
		public void vobLes(){
			Area a = getWarea(xw+1,yw,zw);
			//Area a = getTarea(xt+1,yt,zt);
			if(a==null) {
				cVobLes = tmpBor+J.R(tmpBor); // reagendando
				return;
			}	
			
			
			int v = 0;
			for(int o=0; o<tamArea; o++)
			for(int n=0; n<tamArea; n++){
				v = a.cel[0][n][o];
				if(v<0)continue;
				if(v>tCub.size()-1) continue;

				inf[tamArea-1][n][o].ocLes = tCub.get(v).ocultador;
			}				
			cVobLes=-2;
		}
		public void vobOes(){
			Area a = getWarea(xw-1,yw,zw);
			// Area a = getTarea(xt-1,yt,zt);
			if(a==null) {
				cVobOes = tmpBor+J.R(tmpBor); // reagendando
				return;
			}	
			
			
			int v = 0;
			for(int o=0; o<tamArea; o++)
			for(int n=0; n<tamArea; n++){
				v = a.cel[tamArea-1][n][o];
				if(v<0)continue;
				if(v>tCub.size()-1) continue;
				inf[0][n][o].ocOes = tCub.get(v).ocultador;
			}				
			cVobOes=-2;
		}

	public void sombrear(){
		if(inf==null || cel==null){
			cSombrear = tmpSombrear;
			return;
		}
		Area 
			ac = getWarea(xw,yw+1,zw),
			al = getWarea(xw+1,yw,zw),
			ao = getWarea(xw-1,yw,zw),
			an = getWarea(xw,yw,zw+1),
			as = getWarea(xw,yw,zw-1);
		
		if(ac==null)
		if(yw<altTab-1){
			cSombrear=tmpSombrear;
			return;
		}
		
		boolean sss = false;
// lc=0, lb=-4, ln=-2, ls=-2, ll=-1, lo=-3;
		int qc=-5, qs=qc-2, qn=qc-2, ql=qc-1, qo=qc-3, qb=qc-4;		
		int vvv = 0;
		
		for(int m=0; m<tamArea; m++)
		for(int o=0; o<tamArea; o++)
		{
			sss = false; // deve ser ajustado pela area de cima aqui
			if(ac!=null)
				if(ac.inf!=null) 
				if(ac.inf[m][0][o].lc!=0) 
					sss=true;
			for(int n=tamArea-1; n>=0; n--){
				inf[m][n][o].reset();
				if(sss){
					// cma
					inf[m][n][o].lc = qc;
					
					// bxo
					inf[m][n][o].lb = qb;

					// cubo sul, face nor
					if(o>0)	
						inf[m][n][o-1].ln = qn;
					else if(as!=null) if(as.inf!=null)
						as.inf[m][n][tamArea-1].ln = qn;
				
					// cubo nor, face sul
					if(o<tamArea-1)	
						inf[m][n][o+1].ls = qs;
					else if(an!=null) if(an.inf!=null)
						an.inf[m][n][0].ls = qs;
				
					// cubo les, face oes
					if(m<tamArea-1)	
						inf[m+1][n][o].lo = qo;
					else if(al!=null) if(al.inf!=null)
						al.inf[0][n][o].lo = qo;
				
					// cubo oes, face les
					if(m>0) 
						inf[m-1][n][o].ll = ql;				
					else if(ao!=null) if(ao.inf!=null)
						ao.inf[tamArea-1][n][o].ll = ql;				
				}
				
				vvv = cel[m][n][o];
				if(vvv>tCub.size()) vvv=0;// ?sem bug???
				if(tCub.get(vvv).opFazSombra) sss = true;
			}			
		}
		
	}	
		
		public void verArq(){
			// consulta/cria o arquivo na hora devida
			if(xw==intNull || zw==intNull) 
				J.impErr("!xw e zw nao deveriam ser nulos aqui!","Area.reg()");			
			
			if(cel==null) 
				if(cArq<=0)
				if(frameRate<lmFrameRate)
				if(J.B(3)){
					String cam = stCamAres(xw,yw,zw)+".are";
					cArq=tmpMapArqs;

					if(J.arqExist(cam)){
						zCels();
						saveOpenArea(OPEN, cam);
					}	else {
						rnd();
						saveOpenArea(SAVE, cam);
					}
					
					
				}
		}
		public void zCels(){
			// todas as celulas preenchidas com zeros. Precisou p abrir areas. O preparo estah otimizado por este metodo.
			int[][][] w = new int[tamArea][tamArea][tamArea];
			for(int m=0; m<tamArea; m++)
			for(int n=0; n<tamArea; n++)
			for(int o=0; o<tamArea; o++)
				w[m][n][o] = 0;
			cel = w;			
		}
		public void zInf(){
			// todas as celulas preenchidas com zeros. Precisou p abrir areas. O preparo estah otimizado por este metodo.
			InfImp[][][] w = new InfImp[tamArea][tamArea][tamArea];
			for(int m=0; m<tamArea; m++)
			for(int n=0; n<tamArea; n++)
			for(int o=0; o<tamArea; o++)
				w[m][n][o] = new InfImp();
			inf = w;
		}
		public Jf1 openFrag(){			

			
			RandomAccessFile arq = null;
			String cam = stCamAres(xw,0,zw)+".frg";

			if(!J.arqExist(cam)) return new Jf1(0);
			
			Jf1 f = new Jf1();
			int v = 0;
			try{
				arq = new RandomAccessFile(cam, "rw");
				opBioma = arq.readInt();				
				//float txMap=100/255f;
				float txMap=(altTab*tamArea)/255f;
				for(int m=0; m<10; m++)
				for(int n=0; n<10; n++){
					v = arq.readInt();
					// a variacao precisa ser entre 0..100, 
					// mas o arq frg tem 0..255
					//f.cel[m+1][n+1] = (int)(v*0.7f);
					
					// ALTURA MAXIMA DA TABELA+AREA: 100
					// q * 255 = 100?
					// q = 100/255
					f.cel[m+1][n+1] = (int)(v*txMap);
					// J.esc("v="+v);
				}
				

				
				if(J.opDebug) 
					J.esc("consultado: |"+cam+"|"); // me parece q tava comendo clock!!!
				
				arq.close();
			} catch(IOException e){				
				try{ arq.close(); }catch(IOException ee){}
				e.printStackTrace();
				J.sai("!falha consultando fragmento: "+xw+":"+zw+"  "+cam);
			}
			return f;
		}
		public void rndAether(){
			int v = 0;
			int w[][][] = new int[tamArea][tamArea][tamArea];
			for(int m=0; m<tamArea; m++)
			for(int n=0; n<tamArea; n++)
			for(int o=0; o<tamArea; o++){
				v=0;
				if(J.B(10000)) v = tSeedPlatAether;
				w[m][n][o] = v;
			}
			cel = w;

		}
		
		public void rnd(){ // rndArea
			
			if(J.iguais(stAmb,"aether")){
				rndAether();
				smoothArea(); 
				return;
			}

			int 
				ts = tSolo,
				tss = tSSolo,
				tsss = tSSSolo,
				tssss = tSSSSolo,
				v=0, nn=0,
				tRocha = getInd("rocha"),
				as=0;				
			
			Jf1 rel = openFrag();
			int q = (int)(yw*tamArea);
			int w[][][] = new int[tamArea][tamArea][tamArea];
			int niv=opAltAgua-yt*tamArea;
			
			ehAreaOculta=true;
			for(int m=0; m<tamArea; m++)
			for(int n=0; n<tamArea; n++)
			for(int o=0; o<tamArea; o++){
				v = 0;
				as = rel.cel[m+1][o+1];
				
				if(n+q==as+1)					
					if(J.B(opDensArv)){ // 30 aqui eh bem denso, melhor ser um valor >= a isso
						v=tSeedPorSolo;
						
						if(J.iguais(stAmb,"vanilla"))
						switch(J.R(10)){ // SOH P VANILLA
							case 1: v=tSeedMacieira; break;
							case 2: v=tSeedCoq; break;
							case 3: v=tSeedCtt; break; 
						}
					}
				
				if(n+q==as)v=ts;
				if(n+q<as)v=tss;
				if(n+q<as-3)v=tsss;
				if(n+q<=1)v=tssss;

				if(v==tRocha)
					if(J.B(40)) 
						v = getInd(J.rndSt("min_carvao","min_ferro","min_ouro","min_cobre"));
				
				if(v==0)
					if(n+q<=opAltAgua)
						v=aguaDoAmb;
				
				if(v==tGrama)
				if(q+n<=opAltAgua+2)
					v=tAreia;
				
				nn = n;

				// if(yw>0) nn = tamArea-nn-1;
				if(v==0 || tCub.get(v).opFazRio)
				//if(v==0)
					ehAreaOculta=false;
				w[m][nn][o] = v;
			}	
			cel = w;
			
			smoothArea(); // tomara q nao dane o clock. Nao tah funcionando do outro jeito.
		}
		public boolean ehOcultador(int tp, int i, int j, int k){
			// este "tp" é o cubo q perguntou, e a coordenada pode ser diferente dele
			// necessario p "ocultaOmesmo"
			// padrao da borda
			boolean b = false;
			//boolean b = true;
			
			if(i<0) return b;
			if(j<0) return b;
			if(k<0) return b;

			if(i>=tamArea) return b;
			if(j>=tamArea) return b;
			if(k>=tamArea) return b;
			
			//mas nao serah bem assim			
			// if(cel[i][j][k]!=0) return true; 
			int v = cel[i][j][k];			
			if(v==-1) return false;
			if(v==tp) if(getCubMod(tp).ocultaOmesmo) return true; // landMark oculta landMark; vidros ocultando vidros(?já fiz este???)
			if(v>tCub.size()-1) return false;
			if(tCub.get(v).ocultador) return true;
			return false;
		}
		public void calcOc(){ // verOc
			//InfImp w[][][] = new InfImp[tamArea][tamArea][tamArea];
			//if(inf==null) zInf();
			InfImp q = null;			
			int v=0;
			for(int m=0; m<tamArea; m++)
			for(int n=0; n<tamArea; n++)
			for(int o=0; o<tamArea; o++){
				q = inf[m][n][o];
				v = cel[m][n][o];
				if(getCubMod(v).ocultavel==false){
					q.ocTudo=q.ocNor=q.ocSul=q.ocLes=q.ocOes=q.ocCma=q.ocBxo=false;
					continue;
				}
		
				if(v>tCub.size()) continue;
				
				if(ehOcultador(v, m,n,o-1)) q.ocSul=true; else q.ocSul=false;
				if(ehOcultador(v, m,n,o+1)) q.ocNor=true; else q.ocNor=false;
				if(ehOcultador(v, m+1,n,o)) q.ocLes=true; else q.ocLes=false;
				if(ehOcultador(v, m-1,n,o)) q.ocOes=true; else q.ocOes=false;
				if(ehOcultador(v, m,n+1,o)) q.ocCma=true; else q.ocCma=false;
				if(ehOcultador(v, m,n-1,o)) q.ocBxo=true; else q.ocBxo=false;
			
				if(n<tamArea-1){
					
					if(cel[m][n][o]==tAgua){
						//if(yt*tamArea+n<opAltAgua) q.ocCma=true;  						// ruim p rios subterraneos, mas corrigido abaixo
						// 	q.opSupAgua=true; // forca a imp de cima
						
						
						if(cel[m][n+1][o]==0) 	{q.ocCma=false; q.opSupAgua=true;}
						else	q.ocCma=true;
						
						
					}					
				}					

/*
				if(n<tamArea-1) // vista grossa p inter Areas, acho q a incidencia eh muito pequena
				if(tCub.get(v).opFazRio)
				if(getCub(m,n+1,o, xt,yt,zt)!=0) // interAreas embutido
					q.ocCma=true;					
*/
				

				
				q.ocTudo=false;
				if(q.ocNor)				if(q.ocSul)
				if(q.ocLes)				if(q.ocOes)
				if(q.ocCma)				if(q.ocBxo)
					q.ocTudo=true;
				
				//inf[m][n][o] = q;
			//	w[m][n][o] = q;
			}
			//inf = w;
		}

		public void imp2d(int i, int j){
			
			Color cr = null;
			int v =0;

			if(cel!=null)
			for(int m=0; m<tamArea; m++)
			for(int n=0; n<tamArea; n++) // arrumar depois
			for(int o=0; o<tamArea; o++){
				v = cel[m][n][9-o];
				if(v==0) continue;
				cr = tCub.get(v).crMap;
				cr = J.altColor(cr,n*10-60);
				J.impRetRel(cr,null, // por enquanto
					i+m*tamCel2d,j+o*tamCel2d,
					tamCel2d,tamCel2d);
			}
		}
		public void imp3d(int i, int j, int k){
			xt=i; yt=j; zt=k;
			i-=meioTab;
			k-=meioTab;
			
			xImp = i*tamArea;
			yImp = j*tamArea;
			zImp = k*tamArea;
			if(ehAreaVazia) {
				numAreaVazia++;
				return;
			}
			if(ehAreaOculta) {
				numAreaOculta++;
				return;
			}			
			if(cel==null) return;
			
			numAreaImp++;
			
			

			int b = buss; // ajuda p debug
			
			if(b==NOR){
				if(J.angY<1) b=NL;
				if(J.angY>2) b=NO;
			}
			if(b==LES){
				if(J.angY>1.56f) b=SL;
				else b=NL;
			}
			if(b==SUL){
				if(J.angY>3.13f) b=SO;
				else b=SL;
			}
			if(b==OES){
				if(J.angY>4.72f) b=NO;
				else b=SO;
			}			
			
			if(buss==NOR){
				if(b==NL){
					for(int o=tamArea-1; o>=0; o--)
					for(int m=tamArea-1; m>=0; m--)
						impCol(m,0,o);
					return;				
				}
				if(b==NO){
					for(int o=tamArea-1; o>=0; o--)
					for(int m=0; m<tamArea; m++)
						impCol(m,0,o);
					return;				
				}				
			}
			if(buss==SUL){
				if(b==SO){
					for(int o=0; o<tamArea; o++)
					for(int m=0; m<tamArea; m++)
						impCol(m,0,o);
					return;
				}				
				if(b==SL){
					for(int o=0; o<tamArea; o++)
					for(int m=tamArea-1; m>=0; m--)
						impCol(m,0,o);
					return;
				}				
			}
			if(b==SO){
				for(int m=0; m<tamArea; m++)
				for(int o=0; o<tamArea; o++)
					impCol(m,0,o);
				return;
			}
			if(b==SL){
				for(int m=tamArea-1; m>=0; m--)
				for(int o=0; o<tamArea; o++)
					impCol(m,0,o);
				return;
			}
			if(b==NL){
				for(int m=tamArea-1; m>=0; m--)
				for(int o=tamArea-1; o>=0; o--)
					impCol(m,0,o);
				return;				
			}
			if(b==NO){
				for(int m=0; m<tamArea; m++)
				for(int o=tamArea-1; o>=0; o--)
					impCol(m,0,o);
				return;				
			}

		}
			int v_=0; // p reter a ultima pesquisa de cubo.
			Cub ocub=null;
		public void impCel(int i, int j, int k){
			int 
				q = tamArea>>1,
				lmc = tCub.size()-1,
				v=0;
			v = cel[i][j][k];
			
			//33333333333333
			if(opRealParal) // isso pode evoluir bastante
				if(v==tGrama)
					v = tMyceliun;
			
			xa = i; ya = j; za = k;
			
			if(v>lmc) v = J.R(lmc)+1;
			if(v==0) return;
			if(v<0) J.impErr("!valor estranho da area.cel[][][]: "+v,"Area.impCel()");
			
			opOmiteFaceBxo = (ya==0 && yt==0); // a face de baixo de y0 nao deve ser imprimida, assim ganha clock
			opOmiteFaceCma = (ya==9 && yt==altTab-1); // isso ajuda em cavernas
			
			
			// CUIDADO, A LINHA ABAIXO BUGA!!!
			// if(v<tCub.size()-1) return; 
			
			opLuzAltura=j%2;

			// melhor q isso seria a matriz cel de Area ser feita de  s s   C u b   (como ponteiros) e nao de int, q demanda um get() mais demorado.
			if(v!=v_) {
				ocub = tCub.get(v);
				v_ = v;
			}

			if(ocub==null) ocub = tCub.get(v);

			ocub.imp(
				i-q+xImp, 
				j-q+yImp, 
				k-q+zImp, inf[i][j][k]);
			/*
			tCub.get(v).imp(
				i-q+xImp, 
				j-q+yImp, 
				k-q+zImp, inf[i][j][k]);
			*/	
		}
		public void impCol(int i, int j, int k){ // de area
			// isso nao tah perfeito
			// deveria imprimir as altas+baixas e ir fechando ateh a altura dos olhos do joe.
			int min = 0, max=9;
			
			if(J.angX>0f){
				for(j=min; j<=max; j++){
					if(cel[i][j][k]==0) continue;
					impCel(i,j,k);
				}	
			} else {
				for(j=max; j>=min; j--){
					if(cel[i][j][k]==0) continue;					
					impCel(i,j,k);				
				}
			}	
		}
		public boolean saveOpenArea(boolean s, String cam){
			
			if(s)
				if(!opSaveArea)
					return false;
				
			if(cel==null)
				J.impErr("!Area.cel[][] nao deveria ser nula aqui! cam: |"+cam+"|","Area.saveOpenArea(), "+(s?"SAVE":"OPEN"));
			int v = 0;
			RandomAccessFile arq = null;
			try{
				arq = new RandomAccessFile(cam,"rw");
				if(s){
					
					for(int m=0; m<tamArea; m++)
					for(int n=0; n<tamArea; n++)
					for(int o=0; o<tamArea; o++){
						v = cel[m][n][o];
						if(tCub.get(v).ehCond) v = tCond0;
						// if(v==tAgua)v=0;
						// if(v==tAguaCorr)v=0;
						
						// p nao ter q ficar vendo direcao e tudo. O ruim eh q perde o item.
						if(J.noInt(v,tra0,tra9)) v=tTranspVazio; 
						if(J.noInt(v,mob0,mob9)) v=0; // o certo seria gravar o seed do mob, mas isso deve ganhar clock
						
						arq.writeInt(v);
						
						if(tCub.get(v).temExtraByte)
							arq.writeInt(inf[m][n][o].inf);
					}
					
				} else {
					ehAreaVazia = true;
					int i = 0;
					for(int m=0; m<tamArea; m++)
					for(int n=0; n<tamArea; n++)
					for(int o=0; o<tamArea; o++){
						v = (int)arq.readInt();
						if(J.noInt(v,tCond0+1,tCond0+maxCond)) v=tCond0; // garantia
						if(v!=0) ehAreaVazia = false;
						if(J.noInt(v,mob0,mob9)) v = tExp;
						if(v>=30000) v = tExp;  // o de cima nao foi suficiente p tirar o bug.
						
						if(v>tCub.size())v=tExp;
						
						cel[m][n][o] = v;
						
						if(tCub.get(v).temExtraByte){
							i = (int)arq.readInt();							
							inf[m][n][o].inf = i;
						}
						
						
					}
					// contadores devem ser inicializados aqui @@@@@@@
					
				
				
					cCalcOc = tmpCalcOc;
					cVobNor = tmpBor;
					cVobSul = tmpBor+1;
					cVobLes = tmpBor+2;
					cVobOes = tmpBor+3;
					cVobCma = tmpBor+4;
					cVobBxo = tmpBor+5;
					cSombrear = tmpSombrear;
					cSmoothArea=tmpSmoothArea;
				}
				arq.close();				
				if(opImpCamMaps)
					J.esc((s?"save":"open")+" ok: "+cam);
				
				return true;
			} catch(IOException e){
				e.printStackTrace();
				if(arq!=null) try{arq.close();}catch(IOException ee){}
				J.delArq(cam);
				J.esc("AVISO: deletando arquivo de area defeituoso: |"+cam+"|");
				J.playArqWav("ruido32.wav");
				impSts("AVISO: deletando arquivo de area defeituoso: |"+cam+"|");
				cel=null;
				//J.impErr("!falha "+(s?"salvando":"abrindo")+" |"+cam+"|");				
			}
			return false;
		}
	}
				
// === TABELA ===============================
		final int  /////////////////
			//altTab = 10, // era soh 3 antes. Fica legal tb, mas demora p carregar todas as areas.
			altTab = 5, 
			//tamTab = 6, // CUIDADO! USE SOMENTE NUMEROS PARES AQUI!!!
			tamTab = 16, // CUIDADO! USE SOMENTE NUMEROS PARES AQUI!!!
			//tamTab = 26, 
			meioTab = tamTab>>1;
		Tab tab = null;


	public class Tab{
			Area are[][][] = null;
		public Tab(){
			if(maxXmap==intNull) 
				verTamMap(camMap);				
			ini();
		}		
		public void verTamMap(String cam){
			if(!J.arqExist(cam)) {
				J.impErr("!Arquivo perdido: "+cam,"verTamMap()");
				return;
			}	
			RandomAccessFile arq=null;
			try{
				arq = new RandomAccessFile(cam,"rw");
				maxXmap = (int)arq.readInt(); 
				maxZmap = (int)arq.readInt(); 
				arq.close();
				J.esc("ok: maxXmap e maxZmap consultados: "+maxXmap+" : "+maxZmap);
				return;
			}catch(IOException e){
				try { arq.close(); } catch(IOException ee) {};
				J.impErr("falha consultando arquivo: "+cam,"verTamMap()");
				return;
			}				
		}
		public void ini(){
			
			Area w[][][] = new Area[tamTab][altTab][tamTab];
			for(int m=0; m<tamTab; m++)
			for(int n=0; n<altTab; n++)
			for(int o=0; o<tamTab; o++)
				w[m][n][o] = new Area(m+xJoeIni,n+yJoeIni,o+zJoeIni);
			are = w;
		}
		public boolean delAreHit(int xtt, int ytt, int ztt){
			if(J.noInt(xtt,0,tamTab-1))
			if(J.noInt(ytt,0,tamTab-1))
			if(J.noInt(ztt,0,tamTab-1)){
				int xww = are[xtt][ytt][ztt].xw;
				int yww = are[xtt][ytt][ztt].yw;
				int zww = are[xtt][ytt][ztt].zw;
				String cm = stCamAres(xww,yww,zww)+".are";
				J.delArq(cm);
				are[xtt][ytt][ztt] = new Area(xww,yww,zww);
				return true;
			}
			return false;				
		}
							private void regReg(int m, int n, int o){
								
								int q = meioTab; 
								if(are[m+q][n][o+q]!=null)	are[m+q][n][o+q].reg();
								if(are[q-m-1][n][q-o-1]!=null)	are[q-m-1][n][q-o-1].reg();
								if(are[m+q][n][q-o-1]!=null)	are[m+q][n][q-o-1].reg();
								if(are[q-m-1][n][o+q]!=null)	are[q-m-1][n][o+q].reg();
							}
		
		public void reg(){
			// ajusta areas com cel[][] nula
			int q = meioTab; // numeros impares p tamTab nao sao bem vindos!
			if(are!=null){
			
					if(J.B(2))
						for(int m=0; m<q; m++)
						for(int o=0; o<q; o++) 
						for(int n=0; n<altTab; n++)							
							regReg(m,n,o);
					else	
						for(int o=0; o<q; o++)
						for(int m=0; m<q; m++)
						for(int n=0; n<altTab; n++)														
							regReg(m,n,o);						
				}
			
		}
		public void imp2d(int i, int j){
			if(opImpTab2d<=0) return;
			if(opImpTab2d>2) opImpTab2d=0;

			J.impRetRel(
				16,15, 
				i-1, j-1,
				tamAre2d*tamTab+2,
				tamAre2d*tamTab+2);
			
			int q=tamAre2d,mm=0,nn=0;
			for(int m=0; m<tamTab; m++)
			for(int o=0; o<tamTab; o++)			
			if(are[m][0][o]!=null){ // @@@depois
				mm = i+m*q;
				nn = j+(tamTab-o-1)*q; // consegui!
				are[m][0][o].imp2d(mm,nn);
				if(opImpTab2d==2){
					esc(mm,nn,"");	
					esc(""+are[m][0][o].xw+":"+are[m][0][o].zw);
				}
			}
		}
		public void impCol(int i, int j0, int k){ // de tab
			opImpDt = false;
			int q=opDistImpDt;
			if(J.noInt(i, meioTab-q, meioTab+q))
			if(J.noInt(k, meioTab-q, meioTab+q))
				opImpDt = true;
				
			if(J.angX>0f){ // lembre q existem 2 deste metodo, um p tab e outro p area
				for(int j=0; j<altTab; j++){
					are[i][j][k].reg();
					are[i][j][k].imp3d(i,j,k);
				}
			} else {
				for(int j=altTab-1; j>=0; j--){
					are[i][j][k].reg();					
					are[i][j][k].imp3d(i,j,k);				
				}
			}
		}
		public void imp3d(){
			if(are==null) return;
			
			int 
				b = buss, // ajuda p debug
				lm = tamTab; // area vizivel da tabela
			
			
			if(lm>tamTab)	lm = tamTab;
			
			
			if(b==NOR){
				if(J.angY<1) b=NL;
				if(J.angY>2) b=NO;
			}
			if(b==LES){
				if(J.angY>1.56f) b=SL;
				else b=NL;
			}
			if(b==SUL){
				if(J.angY>3.13f) b=SO;
				else b=SL;
			}
			if(b==OES){
				if(J.angY>4.72f) b=NO;
				else b=SO;
			}			
			
			if(buss==NOR){
				if(b==NL){
					for(int o=lm-1; o>=0; o--)
					for(int m=lm-1; m>=0; m--)
						impCol(m,0,o);
					return;				
				}
				if(b==NO){
					for(int o=lm-1; o>=0; o--)
					for(int m=0; m<lm; m++)
						impCol(m,0,o);
					return;				
				}				
			}
			
			if(buss==SUL){
				if(b==SO){
					for(int o=0; o<lm; o++)
					for(int m=0; m<lm; m++)
						impCol(m,0,o);
					return;
				}				
				if(b==SL){
					for(int o=0; o<lm; o++)
					for(int m=lm-1; m>=0; m--)
						impCol(m,0,o);
					return;
				}				
			}
			
			if(b==SO){
				for(int m=0; m<lm; m++)
				for(int o=0; o<lm; o++)
					impCol(m,0,o);
				return;
			}
			
			if(b==SL){
				for(int m=lm-1; m>=0; m--)
				for(int o=0; o<lm; o++)
					impCol(m,0,o);
				return;
			}
			
			if(b==NL){
				for(int m=lm-1; m>=0; m--)
				for(int o=lm-1; o>=0; o--)
					impCol(m,0,o);
				return;				
			}
			
			if(b==NO){
				for(int m=0; m<lm; m++)
				for(int o=lm-1; o>=0; o--)
					impCol(m,0,o);
				return;				
			}

			
		}
		
		public void desNor(){			
			for(int m=0; m<tamTab; m++)
			for(int n=0; n<altTab; n++)
			for(int o=0; o<tamTab-1; o++){
				are[m][n][o] = are[m][n][o+1];			
				// are[m][n][o].zInf();
			}
			
			int mm=0, oo=0;
			Area aux = null;
			for(int n=0; n<altTab; n++)							
			for(int m=0; m<tamTab; m++){
				aux = are[m][n][tamTab-2];
				if(aux==null) continue;
				mm = aux.xw;
				oo = aux.zw+1; // z incrementa indo p sul
				are[m][n][tamTab-1] = new Area(mm,n,oo);
			}	
			
		}
		public void desSul(){
			for(int m=0; m<tamTab; m++)
			for(int n=0; n<altTab; n++)				
			for(int o=tamTab-1; o>0; o--){
				are[m][n][o] = are[m][n][o-1];			
				// are[m][n][o].zInf();				
			}
			
			int mm=0, oo=0;
			Area aux = null;
			for(int n=0; n<altTab; n++)			
			for(int m=0; m<tamTab; m++){
				aux = are[m][n][1];
				if(aux==null) continue;
				mm = aux.xw;
				oo = aux.zw-1; // z incrementa indo p sul
				are[m][n][0] = new Area(mm,n,oo);
			}	
		}
		public void desLes(){
			for(int m=tamTab-1; m>0; m--)
			for(int n=0; n<altTab; n++)				
			for(int o=0; o<tamTab; o++){
				are[m][n][o] = are[m-1][n][o];			
				// are[m][n][o].zInf();				
			}
			
			int mm=0, oo=0;
			Area aux = null;
			for(int n=0; n<altTab; n++)			
			for(int o=0; o<tamTab; o++){
				aux = are[1][n][o];
				if(aux==null) continue;
				mm = aux.xw-1;
				oo = aux.zw; // z incrementa indo p sul
				are[0][n][o] = new Area(mm,n,oo);
			}	

		}
		public void desOes(){
			for(int m=0; m<tamTab-1; m++)
			for(int n=0; n<altTab; n++)				
			for(int o=0; o<tamTab; o++){
				are[m][n][o] = are[m+1][n][o];
				// are[m][n][o].zInf();				
			}
			
			int mm=0, oo=0;
			Area aux = null;
			for(int n=0; n<altTab; n++)			
			for(int o=0; o<tamTab; o++){
				aux = are[tamTab-2][n][o];
				if(aux==null) continue;
				mm = aux.xw+1;
				oo = aux.zw; // z incrementa indo p sul
				are[tamTab-1][n][o] = new Area(mm,n,oo);
			}				
		}
	}

// === CUBO =================================
		ArrayList<Cub> tCub = new ArrayList<>();
		ArrayList<Cub> tFer = new ArrayList<>();
		ArrayList<Cub> tPlant = new ArrayList<>();
//		ArrayList<Cub> tPlant = new ArrayList<>(); // p mover com o vento depois
		// este acima eh secundario, p girar ferramentas plotadas. Todos devem estar em tCub
		boolean 
			opOmiteFaceBxo=false,// omitir face bxo de y0
			opOmiteFaceCma=false,
			opImpDt=true; // mecanismo p ganhar clocks, dt de longe nao precisa ser imprimido
		int numCubImp=0, numJ3dImp=0;
		int maxCond=100, // p faixa de conds disponiveis, de  t C o n d 0 a  t C o n d 9
			maxElBat=1000, // p baterias fixas
			maxElCond=100; // eletricidade maxima q  c o n d  consegue acumular
		
	boolean 
		opImpNorAlt=false,
		opSemChuva=false,
		opImpCmaAlt=false; // p debug, omite a face superior em intervalos de tempo
		
	public void regAnis(){
		for(Cub c:tFer)
			c.dtCen.giraTudo(0.1f);

		if(opVarinhaOn) // isso parece nao funcionar
			tCub.get(tIsca).dtCen.reg3d();

		{ // g e r   e o l i c o
			if(J.cont%10==0)
				tCub.get(tGerEolico).dtCen.giroZ("pas","@eixo",-VOLTA/2f,10);
			tCub.get(tGerEolico).dtCen.reg3d();			
		}
		
			
		float q = 0;
		for(Cub c:tPlant){
			c.dtCen.reg3d();
			if(c.dtCen.semMovAtivo()){
				if(c.opAniAlga==false) // animar como trigo
				if(J.B(6)){
					if(q==0) q=0.02f+J.R(10)/100f;
					c.dtCen.desFreR("nd1",q,2);
					c.dtCen.desFreR("nd2",q+q+q,2);
				}
				if(c.opAniAlga){
					q = J.RS(10)/60f;
					c.dtCen.desFreR("nd1",q*2,60);
					c.dtCen.desFreR("nd2",q*4,60);
					c.dtCen.desFreR("nd3",q*8,60);
					c.dtCen.desFreR("nd4",q*16,60);
				}				
			}
		}
		

			
		
	}
	public Cub addFerr(String nm, String cm){
		return addFerr(nm,cm,null);
	}
	public Cub addFerr(String nm, String cm, int cr){		
		return addFerr(nm,cm,J.cor[cr]);
	}
	public Cub addFerr(String nm, String cm, Color cr){
		Cub c = new Cub();
		c.ehFerr=true;
		c.ehGhostBlock=true;
		c.opFazSombra=false;
//		c.opGirDtCen=true; // NAO AQUI,  r e g A n i s () jah faz o servico
		c.opQtdEhSaude = true;
		c.dtCen = new J3d(cm, 0.01f);
		c.dtCen.giroX(null, null, 0.3f, 1);
		c.dtCen.reg3d();
		c.opGetComClick=true;			
		c.temExtraByte=true;

		c.crDtCen = addPal(49); // precisa disso senao buga
		if(cr!=null)
			c.crDtCen = addPal(cr);

		c.setName(nm);
		c.ocultador = false;
		//c.opAutoGet = true; // pegar quando passar por cima
		// achei melhor tirar p nao atrapalhar a envasadeira
		c.opQtdEhSaude = true;
		c.ehGhostBlock = true;
		c.tip = tCub.size();
		c.ajPals();	// autoAjuste de paletas
		
		// autoAjuste de paletas
		int crj = 0, vr=0;
		for(J3d.Triang t: c.dtCen.tri){
			vr = t.crt%10;// 6
			crj = t.crt;  // 76
			crj-= crj%10; // 70
			crj+=9;       // 79
			if(cr!=null)	t.pal = addPal(J.altColor(cr,(int)(-vr*10)));
			else	t.pal = addPal(crj);
		}		
		
		tCub.add(c);		
		tFer.add(c);// p girah-la		
		return c;
		
	}
	
	public Cub addPao(String nm, String cm, Color cr, String op){
		Cub c = new Cub();
		c.ehGhostBlock=true;
		c.opFazSombra=false;
		c.dtCen = new J3d(cm, 0.01f,op);
		c.dtCen.reg3d();
		
		c.crDtCen = addPal(49); // precisa disso senao buga
		if(cr!=null)
			c.crDtCen = addPal(cr);

		c.setName(nm);
		c.ocultador = false;
		//c.opAutoGet = true; // pegar quando passar por cima
		c.ehGhostBlock = true;
		c.tip = tCub.size();
		tCub.add(c);		
		//tFer.add(c);// p girah-la... nao mais
		
		// autoAjuste de paletas

		int crj = 0, vr=0;
		for(J3d.Triang t: c.dtCen.tri){
			vr = t.crt%10;// 6
			crj = t.crt;  // 76
			crj-= crj%10; // 70
			crj+=9;       // 79
			if(cr!=null)	t.pal = addPal(J.altColor(cr,(int)(-vr*10)));
			else	t.pal = addPal(crj);
		}
		return c;
		
	}

	public Cub addTrigo(String nm, String cm, int cr, String op){
		return addTrigo(nm,cm,J.cor[cr],op);
	}
	public Cub addTrigo(String nm, String cm, Color cr, String op){
		Cub c = new Cub();
		String opp="";
		if(J.tem("4",op)) opp+="4"; // o "c" em op tah dando problema, por isso esse filtro aqui.
		c.dtCen = new J3d(cm, 0.01f,opp);	
		c.ehGhostBlock=true;
		c.operavel=true;		
		c.opVatorPodeMover=false;		
		c.opJoePodeMover=false;
		c.crDtCen = addPal(49); // precisa disso senao bugar
		if(cr!=null)
			c.crDtCen = addPal(cr);
		if(J.tem('c',op))
			c.opCresce100 = true;
		if(c.opCresce100)
			c.temExtraByte=true;
		c.setName(nm);
		c.opFazSombra=false;
		c.ocultador = false;
		c.tip = tCub.size();
		tCub.add(c);		
		
		// autoAjuste de paletas
		int crj = 0, vr=0;
		for(J3d.Triang t: c.dtCen.tri){
			vr = t.crt%10;// 6
			crj = t.crt;  // 76
			crj-= crj%10; // 70
			crj+=9;       // 79
			if(cr!=null)	t.pal = addPal(J.altColor(cr,(int)(-vr*10)));
			else	t.pal = addPal(crj);
		}

		
		return c;
		
	}

	public Cub addBigorna(String nm, String cm, int cr, String op){ 
		return addBigorna(nm,cm,J.cor[cr],op);
	}
	public Cub addBigorna(String nm, String cm, Color cr, String op){ // copia adaptada de addTrigo q ta funcionando bem.
		Cub c = new Cub(J.altAlfa(cr,0f));// (quase) totalmente transparente, apenas p hit. Este "quase" eh um bug.
		String opp="";
		if(J.tem("4",op)) opp+="4"; // o "c" em op tah dando problema, por isso esse filtro aqui.
		c.dtCen = new J3d(cm, 0.01f,opp);	
		c.dtCen.opSemHit=true; // flag p nao atrapalhar a direcao da face.
		//c.ehGhostBlock=true;
		//c.operavel=true;		
		//c.opVatorPodeMover=false;		
		//c.opJoePodeMover=false;								
		c.crDtCen = addPal(49); // precisa disso senao buga
		if(cr!=null) c.crDtCen = addPal(cr);
		//if(J.tem('c',op))	c.opCresce100 = true;
		c.temExtraByte=true; // indice do equipamento
		c.setName(nm);
		//c.opFazSombra=false;
		c.ocultador = false;
		c.tip = tCub.size();
		tCub.add(c);		
		
		// autoAjuste de paletas
		int crj = 0, vr=0;
		for(J3d.Triang t: c.dtCen.tri){
			vr = t.crt%10;// 6
			crj = t.crt;  // 76
			if(cr!=null){
				crj-= crj%10; // 70
				crj+=9;       // 79
			}
			if(cr!=null)	t.pal = addPal(J.altColor(cr,(int)(-vr*10)));
			else	t.pal = addPal(crj);
		}

		
		return c;
		
	}	

	public Cub addSuit(String nm, String cm, Color cr, String op){ // copia adaptada de addTrigo
		Cub c = new Cub(J.altAlfa(cr,0f));// (quase) totalmente transparente, apenas p hit. Este "quase" eh um bug.
		String opp="";
		c.dtCen = new J3d(cm, 0.01f,opp);	
		c.dtCen.opSemHit=true; // flag p nao atrapalhar a direcao da face.
		c.ehGhostBlock=true;
		c.opAutoGet=false;
		c.opGetComClick=true;
		//c.operavel=true;		
		//c.opVatorPodeMover=false;		
		//c.opJoePodeMover=false;								
		c.crDtCen = addPal(49); // precisa disso senao buga
		if(cr!=null) c.crDtCen = addPal(cr);		
		c.temExtraByte=true; // indice do equipamento
		c.setName(nm);
		c.ehFerr=true;
		//c.opFazSombra=false;
		//c.ocultador = false;
		c.tip = tCub.size();
		
		
		tCub.add(c);		
		
		// autoAjuste de paletas
		int crj = 0, vr=0, crf=0;
		for(J3d.Triang t: c.dtCen.tri){
			vr = t.crt%10;// 6
			crj = t.crt;  // 76
			crf = crj-vr+9;
			if(cr!=null){
				crj-= crj%10; // 70
				crj+=vr;       // 76
			}							
			if(cr!=null && crf!=179) t.pal = addPal(J.altColor(cr,-100+(int)(+vr*10)));
			else	t.pal = addPal(crj);
		}
		return c;		
	}
	
	public Cub addAbobora(String nm, String cm, Color cr, String op){
		Cub c = new Cub(cr);		
		c.setDtLat(cm,J.altColor(cr,-10));
		c.ehGhostBlock=false;
		c.operavel=true;		
		c.opVatorPodeMover=true;		
		c.opJoePodeMover=true;								
		if(J.tem('c',op))
			c.opCresce100 = true;
		if(c.opCresce100){
			c.temExtraByte=true;			
		}
		c.setName(nm);
		c.opFazSombra=false;
		c.ocultador = false;
		c.tip = tCub.size();
		tCub.add(c);				
		return c;
	}
	public Cub addSwitch(String nm, String cm, String op){
		Cub c = new Cub();
		c.dtCen = new J3d(cm, 0.01f,op);	
		c.crDtCen = addPal(49); // precisa disso senao buga
		c.ehGhostBlock=true;
		c.setName(nm);
		c.opFazSombra=false;
		c.ocultador = false;
		c.operavel = true; // soh p garantir
		c.tip = tCub.size();
		tCub.add(c);		

		// autoAjuste de paletas
		// mas isso nao tah perfeito ainda. O certo seria um ajuste fino de sombra no tri, mas usando a mesma paleta. Ex: 4 tris, sendo q 3 deles sao o mesmo azul, porem com sombreamento. O certo eh usar a mesma paleta e sombrear. (ex relaxado, mas dah p entender)
		int crj = 0, vr=0;
		for(J3d.Triang t: c.dtCen.tri){
			vr = t.crt%10;// 6
			crj = t.crt;  // 76
			//if(cr!=null)
			{
				crj-= crj%10; // 70
				crj+=9;       // 79
			}
			t.pal = addPal(crj);
		}
		return c;
		
	}

	public Cub addSprite(String nm, String cm, Color cr){
		return addSprite(nm,cm,cr,"");
	}
	public Cub addSprite(String nm, String cm, Color cr, String op){
		Cub c = new Cub();
		c.dtCen = new J3d(cm, 0.01f,op);
		c.ehGhostBlock=true;
		c.opVatorPodeMover=true;		
		c.opJoePodeMover=false;								
		c.crDtCen = addPal(49); // precisa disso senao buga
		if(cr!=null)
			c.crDtCen = addPal(cr);		
		c.setName(nm);
		c.opFazSombra=false;
		c.ocultador = false;
		c.tip = tCub.size();
		tCub.add(c);
		// autoAjuste de paletas

		int crj = 0, vr=0;
		for(J3d.Triang t: c.dtCen.tri){
			vr = t.crt%10;// 6
			crj = t.crt;  // 76			
			if(cr!=null){
				crj-= crj%10; // 70
				crj+=9;       // 79
			}						
			if(cr!=null)	t.pal = addPal(J.altColor(cr,(int)(-vr*10)));
			else	t.pal = addPal(crj);
		}
		return c;
		
	}

	public Cub addAgua(String nm, int cr){
		return addAgua(nm,J.cor[cr]);
	}
	public Cub addAgua(String nm, Color cr){
		int w = JPal.opContraste;
		//cr = J.altAlfa(cr,0.5f); // veja q isso afeta lava tb
		JPal.opContraste = 8; // ficou razoavel
		Cub c = new Cub(cr);
		JPal.opContraste = w;
		c.setName(nm);
		c.opBounceCma=true;
		c.opLuzAgua=true;
		c.opFazRio=true;
		c.ehGhostBlock=true; 
		c.ocultador = false; // mas isso consumirah clock
		c.iCal = 0.05f;
		c.tc=0f; // meio-bloco, como degrau
		c.crMap = cr;
		tCub.add(c);
		return c;		
	}
	
	public Cub addCub(String nm, int cr){
		return addCub(nm,J.cor[cr]);
	}
	public Cub addGrama(String nm, int crc, int crl){
		return addGrama(nm,J.cor[crc],J.cor[crl]);
	}
	public Cub addGrama(String nm, Color crc, Color crl){
		Cub c = addCub(nm,crl);
		c.crCma = addPal(crc);
		c.crMap = crc;
		c.ehGrama = true;
		c.ehGrama = true; // usei como flag p mao
		return c;
	}		
	public Cub addCub(String nm, Color cr){
		Cub c = new Cub(cr);
		c.setName(nm);
		c.crMap = cr;
		tCub.add(c);
		return c;
	}	

		int maxMob = 32, mob0=-1, mob9=-1;
	public int iniCubs(){		
		if(tCub.size()>0) return 0;
		{ // vanilla
			addCub("nulo",16).setIcon(new Jf1(0)).ocultador=false;
			lastCub.opVatorPodeMover=false;		
			lastCub.opFazSombra=false;	
			lastCub.ehGhostBlock=true;	 // precisa disso senao buga o  s m o o t h   u n d e r w a t e r 
		
			addGrama("grama",69,129).setDtLat("barba05c.j3d",69).setDtCma("obsidian01cma.j3d",69-2).setIcon("bloco03.jf1",69,129,129-2,69-4,69-2).setExtCom("0a").setWav("tf").setCozido("areia");
			lastCub.opMenosDtCma=true;
			addGrama("degrau_grama",69,129).setDtLat("barba05d.j3d",69).setDtCma("obsidian01cmab.j3d",69-2).setIcon("bloco17.jf1",69,129,129-2,69-4,69-2).setExtCom("0a").setWav("tf").setCozido("degrau_areia");
			lastCub.ehDegrau=true;						
			lastCub.tc=0;
			lastCub.ocultador=false;					
			
			addCub("terra",129).setDtLat("obsidian01.j3d",129-2).setDtCma("obsidian01cma.j3d",129-2).setDtBxo("obsidian01bxo.j3d",129-2).setIcon("bloco09.jf1",129).setExtCom("0a").setWav("tt").setCozido("areia");
			addCub("rocha",49).setDtLat("obsidian01.j3d",49-2).setDtCma("obsidian01cma.j3d",49-2).setDtBxo("obsidian01bxo.j3d",49-2).setIcon("bloco36.jf1",49).setExtCom("p").setWav("rr");
			lastCub.opMenosDtCma=true;			
			lastCub.opAcidoCorroi=false;						

			addCub("terra_arada",129-3).setDtLat("obsidian01.j3d",129-3-2).setDtCma("monte02.j3d",129-3-2).setDtBxo("obsidian01bxo.j3d",129-3-2).setIcon("bloco09.jf1",129-3-2).setExtCom("a").setWav("tt").setCozido("areia");
			lastCub.operavel=true;		
			addCub("terra_arada_regada",129-6).setDtLat("obsidian01.j3d",129-6-1).setDtCma("monte02.j3d",129-6).setDtBxo("obsidian01bxo.j3d",129-6-2).setIcon("bloco09.jf1",129-6-2).setExtCom("a").setWav("tt").setCozido("areia");
			lastCub.operavel=true;


		  addAgua("agua",J.altColor(J.cor[9], 20)).setIcon("bloco09.jf1",89).setWav("gg").setOption("L").setCozido("nuvem");
			lastCub.ehBebivel=true;
			lastCub.ehConCano=true; // deve ajudar
			addAgua("agua_corr",119).setIcon("bloco09.jf1",89).setWav("gg").setCozido("nuvem");
			lastCub.ehConCano=true; // deve ajudar
			lastCub.ehBebivel=true;			
			lastCub.operavel=true;					

			// CUIDADO! abaixo precisa ser nesta ordem quando existe crescimento 1000
			addTrigo("trigo_crescendo","trigo07.j3d",J.altColor(J.cor[69],150),"c4").setIcon("trigo05.jf1",69).setWav("ff").setPotCalor(1);
			addTrigo("trigo","trigo07.j3d",J.altColor(J.cor[139],40),"4").setIcon("trigo05.jf1",139).setExtCom("0f").setWav("ff").setPotCalor(1);
			
				addSprite("muda_seca","mudaSeca01.j3d",J.cor[139]);
				lastCub.setIcon("muda02",139,138,137,136,135);
				lastCub.setPotCalor(1).setExtCom("0f").setWav("ff");
				lastCub.setExtCom("0");
				tPlant.add(lastCub); // p animacao da muda


			// CUIDADO! abaixo precisa ser nesta ordem quando existe crescimento 1000
			addTrigo("mato_crescendo","capim02.j3d",J.mixColor(69,15),"c4").setIcon("trigo05.jf1",149).setExtCom("0f").setWav("ff").setPotCalor(1);
			addTrigo("mato","capim02.j3d",J.mixColor(69,15),"4").setIcon("trigo05.jf1",149).setExtCom("0f").setWav("ff").setPotCalor(1);
			//addTrigo("mato","trigo07.j3d",J.mixColor(69,15),"4").setIcon("trigo05.jf1",149).setExtCom("0f").setWav("ff").setPotCalor(1);

			addCub("vago101",99);
			addCub("vago102",99);

			//Color cr = J.mixColor(49,);
			Color cr = J.cor[49];
			addSprite("pedra1","pedra01.j3d",cr); 
			addSprite("pedra2","pedra02.j3d",cr); 
			addSprite("pedra3","pedra03.j3d",cr);
						
			addCub("lama",129-4).setDtLat("caule04.j3d",129-3).setDtCma("obsidian01cma.j3d",129-3).setDtBxo("obsidian01bxo.j3d",129-4).setIcon("bloco09.jf1",129-3).setExtCom("ac").setWav("ll").setCozido("areia");			// "c" eh colher de pedreiro
			addCub("degrau_lama",129-4).setDtCma("obsidian01cma.j3d",129-3).setDtBxo("obsidian01bxo.j3d",129-3).setIcon("bloco15.jf1",129-4).setExtCom("a").setWav("tt").setCozido("degrau_areia").tc=0;
			lastCub.ehDegrau=true;						
			lastCub.ocultador=false;						
			}			
		{ // arvores
			{ // comum		
				addCub("caule",59-8).setCrCmaBxo(139).setDtCma("caule02.j3d",139-2).setDtBxo("caule02bxo.j3d",139-2).setDtLat("caule04.j3d",59-7).setIcon("bloco06.jf1",139,59,59-2,139-4,12).setExtCom("m").setWav("mm").setPotCalor(4);
				lastCub.ehCaule = true;					
				addCub("folha",69).setDtLat("obsidian01.j3d",69-2).setDtCma("obsidian01cma.j3d",69-2).setDtBxo("obsidian01bxo.j3d",69-2).setIcon("bloco09.jf1",69).setExtCom("f").setWav("ff").setPotCalor(1);
				lastCub.operavel=true; // gerarah frutos depois (serah???)... 
				lastCub.ehFolha=true;		
				lastCub.opBounceFaces = true;					
				addCub("madeira",139).setDtLat("madeira02.j3d",139-1).setDtBxo("madeira01bxo.j3d",139-1).setDtCma("madeira01cma.j3d",139-1).setIcon("bloco11.jf1",139).setExtCom("m").setWav("mm").setPotCalor(3);
				lastCub.ehMadeira=true;
				addCub("degrau_madeira",139).setDtLat("madeira02.j3d",139-1).setDtBxo("madeira01bxo.j3d",139-1).setDtCma("madeira01cmab.j3d",139-1).setIcon("bloco12b.jf1",139).setExtCom("m").setWav("mm").setPotCalor(3).tc=0f;
				lastCub.ehDegrau=true;
				lastCub.ehMadeira=true;
				lastCub.ocultador=false;
			}
			{ // macieira
				addSprite("muda_macieira","muda02.j3d",J.cor[69-4]);
				lastCub.operavel=true;
				lastCub.setIcon("muda02",149,148,147,146,145);
				lastCub.setExtCom("0");
				tPlant.add(lastCub); // p animacao da muda
				addCub("caule_macieira",59-9).setCrCmaBxo(139-1).setDtCma("caule02.j3d",139-2).setDtBxo("caule02bxo.j3d",139-2).setDtLat("caule04.j3d",59-8).setIcon("bloco06.jf1",139-2,59-2,59-4,139-6,12).setExtCom("m").setWav("mm").setPotCalor(4);
				lastCub.ehCaule = true;					
				addCub("folha_macieira",69-4).setDtLat("obsidian01.j3d",69-6).setDtCma("obsidian01cma.j3d",69-6).setDtBxo("obsidian01bxo.j3d",69-6).setIcon("bloco09.jf1",69-4).setExtCom("f").setWav("ff").setPotCalor(1);
				lastCub.operavel=true;
				lastCub.ehFolha=true;		
				lastCub.opBounceFaces = true;
				// maca04.j3d x 4 laterais p fazer a apanhada
				addCub("folha_maca_verde",69-4).setDtLat("maca03.j3d",10).setDtCma("obsidian01cma.j3d",69-6).setDtBxo("maca05.j3d",10).setIcon("bloco09.jf1",69-4).setExtCom("f").setWav("ff").setPotCalor(1);
				lastCub.operavel=true;
				lastCub.ehFolha=true;		
				lastCub.opBounceFaces = true;

				addCub("folha_maca",69-4).setDtLat("maca03.j3d",99).setDtCma("obsidian01cma.j3d",69-6).setDtBxo("maca05.j3d",99).setIcon("bloco09.jf1",69-4).setExtCom("f").setWav("ff").setPotCalor(1);
				lastCub.operavel=true;
				lastCub.ehFolha=true;		
				lastCub.opBounceFaces = true;
			}			
			{	// coloridas
				// amarelo
				// rosa
				// branco
				// laranja
				// verde claro
				int cr = 69;
				cr= 79; addCub("folha_amarela",cr).setDtLat("obsidian01.j3d",cr-2).setDtCma("obsidian01cma.j3d",cr-2).setDtBxo("obsidian01bxo.j3d",cr-2).setIcon("bloco09.jf1",cr).setExtCom("f").setWav("ff").setPotCalor(1).ehFolha=true; lastCub.opBounceFaces = true;
				cr=209; addCub("folha_rosa",cr).setDtLat("obsidian01.j3d",cr-2).setDtCma("obsidian01cma.j3d",cr-2).setDtBxo("obsidian01bxo.j3d",cr-2).setIcon("bloco09.jf1",cr).setExtCom("f").setWav("ff").setPotCalor(1).ehFolha=true; lastCub.opBounceFaces = true;
				cr=119; addCub("folha_branca",cr).setDtLat("obsidian01.j3d",cr-2).setDtCma("obsidian01cma.j3d",cr-2).setDtBxo("obsidian01bxo.j3d",cr-2).setIcon("bloco09.jf1",cr).setExtCom("f").setWav("ff").setPotCalor(1).ehFolha=true; lastCub.opBounceFaces = true;
				cr=159; addCub("folha_laranja",cr).setDtLat("obsidian01.j3d",cr-2).setDtCma("obsidian01cma.j3d",cr-2).setDtBxo("obsidian01bxo.j3d",cr-2).setIcon("bloco09.jf1",cr).setExtCom("f").setWav("ff").setPotCalor(1).ehFolha=true; lastCub.opBounceFaces = true;
				cr=219; addCub("folha_clara",cr).setDtLat("obsidian01.j3d",cr-2).setDtCma("obsidian01cma.j3d",cr-2).setDtBxo("obsidian01bxo.j3d",cr-2).setIcon("bloco09.jf1",cr).setExtCom("f").setWav("ff").setPotCalor(1).ehFolha=true; lastCub.opBounceFaces = true;
			}
			{ // coqueiro
				addCub("caule_coq",149-8).setCrCmaBxo(139).setIcon("bloco06.jf1",139,179,179-2,139-4,12).setExtCom("m").setWav("mm").setPotCalor(3);
				lastCub.setIncCal(-0.36f);
				lastCub.ocultador=false;
				lastCub.ipLO = lastCub.ipNS = 0.1f;
				lastCub.ehCaule = true;
				addSprite("folha_coq","coqueiro03.j3d",J.cor[69],"4").setIcon("folha02.jf1",149);
				lastCub.operavel=true;
				lastCub.dtCen.resize(6f);
				addCub("coco",J.cor[149-5]).setIcon("bloco32.jf1",149,147,145,143,12).setExtCom("0mf").setWav("mm");
				lastCub.dy=-0.3f; // altura do solo
				lastCub.ocultador=false;
				lastCub.opFazSombra=false;
				lastCub.inflar(-0.3f);
				lastCub.opGetComClick=true;	// nao aqui pq tem q gastar estamina
			}
		}
		{ // cogumelos, solo floresta e micelium (escevi errado de proposito)
				addGrama("myceliun",49,129).setDtLat("barba05c.j3d",49).setDtCma("obsidian01cma.j3d",49-2).setIcon("bloco03.jf1",49,129,129-2,49-4,49-2).setExtCom("a").setWav("tf").setCozido("areia");
				addGrama("degrau_myceliun",49,129).setDtLat("barba05d.j3d",49).setDtCma("obsidian01cmab.j3d",49-2).setIcon("bloco17.jf1",49,129,129-2,49-4,49-2).setExtCom("a").setWav("tf").setCozido("areia");
				lastCub.ehDegrau=true;							
				lastCub.tc=0;
				lastCub.ocultador=false;

				{
					Color crTer = J.cor[109-5]; // calibrar melhor isso depois
					Color crGra = J.cor[179-5]; // ?jah pensou definir a cor em tempo de execucao??? Com JWin e tudo?
					addGrama("solo_floresta",crGra,crTer).setDtLat("barba05c.j3d",crGra).setDtCma("obsidian01cma.j3d",crGra.darker()).setIcon("bloco03.jf1",135,127,127-2,135-4,135-2).setExtCom("a").setWav("tf").setCozido("areia");
					addGrama("degrau_solo_floresta",crGra,crTer).setDtLat("barba05d.j3d",crGra).setDtCma("obsidian01cmab.j3d",crGra.darker()).setIcon("bloco17.jf1",135,127,127-2,135-4,135-2).setExtCom("a").setWav("tf").setCozido("areia");
					lastCub.ehDegrau=true;								
					lastCub.tc=0;
					lastCub.ocultador=false;
					lastCub.operavel=true;
				}

				//lastCub.opGetComClick=true;			
				addCub("vago40",0);// p cogumelos vermelho, beje e preto,,,
				addCub("vago41",0);
				addCub("vago42",0);
				addCub("vago43",0);
				addCub("vago44",0);
				addCub("vago45",0);
				addCub("vago46",0); // cogumelo cel ia ser legal
				addCub("vago47",0);			
		}
		{ // deserto
			addCub("areia",J.altColor(J.cor[139],20)).setDtLat("obsidian01.j3d",J.altColor(J.cor[139],10)).setDtCma("areia01cma.j3d",J.altColor(J.cor[139],1)).setIcon("bloco09.jf1",139-1).setExtCom("0ac").setWav("aa").setCozido("vidraca"); // depois, fazer isto soh no alto-forno (temperatura mais elevada)
			lastCub.ehAreia=true;
			lastCub.opAcidoCorroi=false;						
			
			addCub("areia_cai",J.altColor(J.cor[139],20)).setDtLat("obsidian01.j3d",J.altColor(J.cor[139],30)).setDtCma("areia01cma.j3d",J.altColor(J.cor[139],10))			.setIcon("bloco09.jf1",139-1).setExtCom("0ac").setWav("aa");
			lastCub.ehAreia=true;			
			lastCub.operavel=true;
			lastCub.opAreiaCai=true;
			
			addCub("degrau_areia",J.altColor(J.cor[139],20)).setDtLat("obsidian01b.j3d",J.altColor(J.cor[139],30)).setDtCma("areia01cmab.j3d",J.altColor(J.cor[139],10))			.setIcon("bloco15.jf1",139-1).setExtCom("0ac").setWav("aa");
			lastCub.ehDegrau=true;						
			lastCub.tc=0;
			lastCub.ehAreia=true;			
			lastCub.ocultador=false;			
			
				
			addCub("cacto_base",69).setDtLat("cacto01.j3d",179).setCrCmaBxo(139).setDtCma("caule02.j3d",139-2).setCrBxo(139).setDtBxo("caule02bxo.j3d",139-2).setIcon("cacto04.jf1").setExtCom("m").setWav("mm").setPotCalor(2).ehCaule=true;
			lastCub.operavel=true;
			addCub("cacto_topo",69).setDtLat("cacto01.j3d",179).setDtCma("cacto01cma.j3d",179).setCrBxo(139).setDtBxo("caule02bxo.j3d",139-2).setIcon("cacto04.jf1").setExtCom("m").setWav("mm").setPotCalor(2).ehCaule=true;
			lastCub.operavel=true;
			
			addCub("rocha_deserto",J.altColor(J.cor[139],40)).setDtLat("obsidian01.j3d",J.altColor(J.cor[139],20)).setDtCma("obsidian01cma.j3d",J.altColor(J.cor[139],20)).setDtBxo("obsidian01bxo.j3d",J.altColor(J.cor[139],20)).setIcon("bloco36.jf1",139).setExtCom("p").setWav("rr");			
			lastCub.opMenosDtCma=true;			
			addCub("vago02",0); // p templos com desenhos nas pedras
		}
		{	// polar
			addCub("neve",119).setDtLat("obsidian01.j3d",119-1).setDtCma("areia01cma.j3d",118).setIcon("bloco09.jf1",119).setExtCom("a").setWav("aa").setCozido("agua");
			lastCub.operavel=true;		
			addCub("gelo",J.mixColor(89,119)).setDtLat("vidro03.j3d",119).setDtCma("vidro03cma.j3d",119).setDtBxo("vidro03bxo.j3d",119).setIcon("bloco10.jf1",119).setExtCom("p").setWav("rr").setCozido("agua").cct=-3;
			lastCub.ehGelo=true;
			lastCub.operavel=true;		
			addCub("vago05",0); // gmite, ctite
			addCub("vago06",0);
		}
		{ // minerios. Estes cubos precisam ser mutantes de acordo com o ambiente
			addCub("min_carvao",49).setDtLat("veia02.j3d",179-4).setDtCma("obsidian01cma.j3d",179-4).setDtBxo("obsidian01bxo.j3d",179-4).setIcon("bloco05.jf1",49,49-2,49-4,179-4,179-8).setExtCom("p").setWav("rr");
			lastCub.ehMinerio=true;
			lastCub.opAcidoCorroi=false;						
			addCub("min_ferro",49).setDtLat("veia02.j3d",139).setDtCma("obsidian01cma.j3d",139).setDtBxo("obsidian01bxo.j3d",139).setIcon("bloco05.jf1",49,49-2,49-4,139,139-2).setExtCom("p").setWav("rr").setCozido("barra_ferro");
			lastCub.ehMinerio=true;
			lastCub.opAcidoCorroi=false;						
			addCub("min_cobre",49).setDtLat("veia02.j3d",109).setDtCma("obsidian01cma.j3d",99).setDtBxo("obsidian01bxo.j3d",99).setIcon("bloco05.jf1",49,49-2,49-4,59,59-2).setExtCom("p").setWav("rr").setCozido("barra_cobre");
			lastCub.opAcidoCorroi=false;						
			lastCub.ehMinerio=true;
			addCub("min_ouro",49).setDtLat("veia02.j3d",159).setDtCma("obsidian01cma.j3d",159).setDtBxo("obsidian01bxo.j3d",159).setIcon("bloco05.jf1",49,49-2,49-4,159,159-2).setExtCom("p").setWav("rr").setCozido("barra_ouro");
			lastCub.opAcidoCorroi=false;						
			lastCub.ehMinerio=true;			
			
			
			addFerr("barra_ouro", "barra01.j3d",159).setIcon("barra02.jf1",159).setWav("rr");
			addFerr("barra_cobre","barra01.j3d", 59).setIcon("barra02.jf1", 59).setWav("rr");
			addFerr("barra_ferro","barra01.j3d", 49).setIcon("barra02.jf1", 49).setWav("rr");			
		}
		{	// seeds
			addCub("seed_por_solo",12);
			lastCub.operavel=true;		
			lastCub.crMap = J.cor[12];
			
			addCub("seed_plat_aether",12).operavel=true;				
			
			addCub("seed_sequoia",12).operavel=true;		
			
			addCub("seed_mato",12).operavel=true;		
			addCub("seed_pedra",12).operavel=true;
			lastCub.opFazSombra=false;
			addCub("seed_macieira",12).operavel=true;
			addCub("seed_coq",12).operavel=true;
			addCub("seed_ctt",12).operavel=true;
			
		}
		{ // ferramentas
			addCub("bancada",139).setCrCmaBxo(109).setDtLat("bancada05.j3d",49).setIcon("bancad02.jf1").setExtCom("m").setWav("mm").setPotCalor(4);
			addFerr("machado","machado02.j3d").setIcon("machad05.jf1").setPotCalor(2);
			addFerr("pah","pah01.j3d").setIcon("pah01.jf1").setPotCalor(2);
			addFerr("picareta","picareta02.j3d").setIcon("picareta.jf1").setPotCalor(2);
			addFerr("enxada","enxada01.j3d").setIcon("enxada02.jf1").setPotCalor(2);
			addFerr("foice","foice01.j3d").setIcon("foice02.jf1").setPotCalor(2);
			addFerr("varinha","varinha01.j3d").setIcon("varinha1.jf1").setPotCalor(2);
			addFerr("espada","espada01.j3d").setIcon("espada01.jf1");
			addFerr("arco","arco02.j3d").setIcon("arco05.jf1").setPotCalor(2);
			addFerr("rod","cajado02.j3d").setIcon("cajado01.jf1").setPotCalor(2);
			addFerr("chave_fenda","chavefenda01.j3d").setIcon("chfenda2.jf1");
			addFerr("serrote","serrote03.j3d").setIcon("serrot02.jf1").setPotCalor(1);
			addFerr("jetPack","jetpack01.j3d",119).setIcon("jetpack1.jf1",49,47,45,43,41);
			addFerr("bolsa","bolsa01.j3d",119).setIcon("bolsa01.jf1",49,47,45,43,41);
		}		
		{ // sementes
			// tem mais um monte lah p baixo, mas eh melhor deixar junto com as repectivas plantas
			addFerr("sem_trigo","semente02.j3d",139).setIcon("saco05.jf1",139).setWav("f ").setPotCalor(1).ehEmbrulho=true; // ajudou no shape da mao
			lastCub.ehSemente=true;								
			lastCub.opGetComClick=true;						
			lastCub.temExtraByte=true;					
		}
		{ // fornalha e bau
			addCub("fornalha",179).setDtLat("fornalha03.j3d",16).setDtCma("obsidian01cma.j3d",179-2).setDtBxo("obsidian01bxo.j3d",179-3).setIcon("forn04.jf1").setExtCom("p").setWav("rr").setIncCal(-0.01f);
			lastCub.temExtraByte=true;		
			lastCub.operavel=true; // soh p garantir		
			lastCub.ocultador = false;		
			lastCub.opVatorPodeMover=false;		
			lastCub.opJoePodeMover=false;				
			
			// depois: bolar as direcoes abaixo
			addCub("bau",59).setDtLat("bau04b.j3d",179).setDtNor("bau04",179).setIcon("bau09.jf1").setDtCma("madeira01cma.j3d",109).setExtCom("m").setWav("mm").opVatorPodeMover=false;
			lastCub.operavel = true;
			lastCub.temExtraByte=true;		
			lastCub.opVatorPodeMover=false;		
			lastCub.opJoePodeMover=false;		
			
			
		}
		{	// aether
			addCub("nuvem",119).setDtLat("obsidian01.j3d",119-1).setDtCma("obsidian01cma.j3d",119-1).setDtBxo("obsidian01bxo.j3d",119-1).setIcon("bloco09.jf1",119).setWav("aa");
			lastCub.opBounceFaces = true;					
			
			addGrama("grama_cel",J.mixColor(69,119),J.mixColor(129,119)).setDtLat("barba05c.j3d",J.mixColor(69,119)).setDtCma("obsidian01cma.j3d",J.mixColor(69,117)).setIcon("bloco03.jf1",        279,269,268,278,277).setExtCom("a").setWav("tf").setCozido("areia_cel");
			lastCub.opMenosDtCma=true;			
			lastCub.opAcidoCorroi=false;						
			addGrama("degrau_grama_cel",J.mixColor(69,119),J.mixColor(129,119)).setDtLat("barba05d.j3d",J.mixColor(69,119)).setDtCma("obsidian01cmab.j3d",J.mixColor(69,117)).setIcon("bloco17.jf1",279,269,268,278,277).setExtCom("a").setWav("tf").setCozido("areia_cel");
			lastCub.ehDegrau=true;						
			lastCub.opAcidoCorroi=false;						
			lastCub.tc=0;
			lastCub.ocultador=false;				
			lastCub.operavel=true;				

			addCub("terra_cel",J.mixColor(129,119)).setDtLat("obsidian01.j3d",J.mixColor(129,117)).setDtCma("obsidian01cma.j3d",J.mixColor(129,117)).setDtBxo("obsidian01bxo.j3d",J.mixColor(129,117)).setIcon("bloco09.jf1",269).setExtCom("a").setWav("tt").setCozido("areia_cel");
			lastCub.opAcidoCorroi=false;						
			addCub("degrau_terra_cel",J.mixColor(129,119)).setDtLat("obsidian01b.j3d",J.mixColor(129,117)).setDtCma("obsidian01cmab.j3d",J.mixColor(129,117)).setDtBxo("obsidian01bxo.j3d",J.mixColor(129,117)).setIcon("bloco15.jf1",269).setExtCom("a").setWav("tt").setCozido("areia_cel");
			lastCub.ehDegrau=true;						
			lastCub.opAcidoCorroi=false;						
			lastCub.tc=0;
			lastCub.ocultador=false;				
			lastCub.operavel=true;				
			
			addCub("caule_cel",J.mixColor(59,119)).setCrCmaBxo(J.mixColor(139,119)).setDtCma("caule02.j3d",139).setDtBxo("caule02bxo.j3d",139).setDtLat("caule04.j3d",J.mixColor(59,117)).setIcon("bloco06.jf1",289, 129,129-4,289-4,12).setExtCom("m").setWav("mm").setPotCalor(4);
			lastCub.ehCaule = true;
			lastCub.opAcidoCorroi=false;						
			addCub("madeira_cel",J.mixColor(139,119)).setDtLat("madeira02.j3d",J.mixColor(139,118)).setDtBxo("madeira01bxo.j3d",J.mixColor(139,118)).setDtCma("madeira01cma.j3d",J.mixColor(139,118)).setIcon("bloco11.jf1",289).setExtCom("m").setWav("mm").setPotCalor(3);
			lastCub.ehMadeira=true;			
			lastCub.opAcidoCorroi=false;						
		
			addCub("folha_cel",J.mixColor(69,119)).setDtLat("obsidian01.j3d",116).setDtCma("obsidian01cma.j3d",116).setDtBxo("obsidian01bxo.j3d",116).setIcon("bloco09.jf1",279).setExtCom("f").setWav("ff").setPotCalor(1);
			lastCub.opAcidoCorroi=false;						
			lastCub.operavel=true; // gerarah frutos depois		
			lastCub.ehFolha=true;		
			lastCub.opBounceFaces = true;		
			
			addCub("areia_cel",    J.mixColor(139,119)).setDtLat("obsidian01.j3d",J.mixColor(139,118)).setDtCma("areia01cma.j3d",J.mixColor(139,118)).setIcon("bloco09.jf1",209).setExtCom("a").setWav("aa");	
			lastCub.opAcidoCorroi=false;						
			lastCub.ehAreia=true;			
			addCub("areia_cel_cai",J.mixColor(139,119)).setDtLat("obsidian01.j3d",J.mixColor(139,118)).setDtCma("areia01cma.j3d",J.mixColor(139,118)).setIcon("bloco09.jf1",209).setExtCom("a").setWav("aa");
			lastCub.ehAreia=true;			
			lastCub.opAcidoCorroi=false;						
			lastCub.operavel=true;
			lastCub.opAreiaCai=true;
			addCub("degrau_areia_cel",J.mixColor(139,119)).setDtLat("obsidian01b.j3d",J.mixColor(139,118)).setDtCma("areia01cmab.j3d",J.mixColor(139,118)).setIcon("bloco15.jf1",209).setExtCom("a").setWav("aa");	
			lastCub.ehDegrau=true;						
			lastCub.ehAreia=true;			
			lastCub.opAcidoCorroi=false;						
			lastCub.tc=0;
			lastCub.ocultador=false;
			
			
			addCub("marmore",119).setDtLat("marmore01.j3d",119-1).setDtCma("marmore01cma.j3d",119-1).setDtBxo("marmore01bxo.j3d",119-1).setIcon("bloco18.jf1",119).setExtCom("p").setWav("rr");
			lastCub.opAcidoCorroi=false;						
			addCub("degrau_marmore",119).setDtLat("marmore01b.j3d",119-1).setDtCma("marmore01cmab.j3d",119-1).setDtBxo("marmore01bxo.j3d",119-1).setIcon("bloco15.jf1",119).setExtCom("p").setWav("rr");		
			lastCub.ehDegrau=true;						
			lastCub.opAcidoCorroi=false;						
			lastCub.tc=0;
			lastCub.ocultador=false;	

			addCub("coluna_marmore",119).setDtLat("coluna01.j3d",119-1).setIcon("bloco19.jf1",119).setExtCom("p").setWav("rr").ocultador=false;
			lastCub.ocultador=true;
			lastCub.opAcidoCorroi=false;
		
			addAgua("agua_cel",11).setIcon("bloco09.jf1",118).setWav("gg").setOption("L").setCozido("nuvem");
			lastCub.opAcidoCorroi=false;						
			//lastCub.ehConCan=true; // soh depois q for bombeavel
			lastCub.operavel=true;		
			addAgua("agua_cel_corr",119).setIcon("bloco09.jf1",119).setWav("gg").setCozido("nuvem");
			lastCub.opAcidoCorroi=false;						
			lastCub.operavel=true;		
			//lastCub.ehConCan=true; 			
			lastCub.ehBebivel=true;
			
		}
		{	// nether
			addCub("cinzas",179).setDtLat("obsidian01.j3d",178).setDtCma("areia01cma.j3d",178).setIcon("bloco09.jf1",178).setExtCom("a").setWav("aa");
			lastCub.ehAreia=true;			
			addCub("cinzas_cai",179).setDtLat("obsidian01.j3d",178).setDtCma("areia01cma.j3d",178).setIcon("bloco09.jf1",178).setExtCom("a").setWav("aa");
			lastCub.operavel=true;
			lastCub.ehAreia=true;			
			lastCub.opAreiaCai=true;
			
			addCub("degrau_cinzas",179).setDtLat("obsidian01b.j3d",178).setDtCma("areia01cmab.j3d",178).setIcon("bloco15.jf1",178).setExtCom("a").setWav("aa");
			lastCub.ehDegrau=true;						
			lastCub.ehAreia=true;			
			lastCub.tc=0;
			lastCub.ocultador=false;					

			addCub("rocha_magma",16).setDtLat("magma01.j3d",12).setDtCma("magma01cma.j3d",12).setDtBxo("magma01bxo.j3d",12).setIcon("bloco16.jf1",99).setExtCom("p").setWav("rr");						
			lastCub.opAcidoCorroi=false;						
			
		
			addAgua("lava",32).setIcon("bloco09.jf1",99).setWav("ll").setOption("L");
			//lastCub.ehConCan=true; // soh depois q for bombeavel
			addAgua("lava_corr",35).setIcon("bloco09.jf1",99).setWav("ll");
			//lastCub.ehConCan=true; 
			lastCub.operavel=true;	// esse eh como rio
			
			addAgua("magma",12).setIcon("bloco15.jf1",99).setWav("ll");
			lastCub.operavel=true; // esse forma rocha, ne?
			lastCub.tc=0.26f;			
			
			addAbobora("fungo_nether_crescendo","caule04.j3d",J.cor[179],"c").setIcon("abob03.jf1",159).setExtCom("0m").setWav("mm");		
			addAbobora("fungo_nether","caule04.j3d",J.cor[179],"").setIcon("abob03.jf1",159).setExtCom("0m").setWav("mm");
			
			addCub("a",16).setDtLat("marmore01.j3d",179).setDtCma("marmore01cma.j3d",179).setDtBxo("marmore01bxo.j3d",179).setIcon("bloco18.jf1",179).setExtCom("p").setWav("rr");
			lastCub.opAcidoCorroi=false;						
			addCub("degrau_granito",16).setDtLat("marmore01b.j3d",179).setDtCma("marmore01cmab.j3d",179).setDtBxo("marmore01bxo.j3d",179).setIcon("bloco15.jf1",179).setExtCom("p").setWav("rr");
			lastCub.ehDegrau=true;						
			lastCub.opAcidoCorroi=false;						
			lastCub.tc=0;
			lastCub.ocultador=false;	

			addCub("vagoG",99);

			addCub("areia_nether",129-6).setDtLat("obsidian01.j3d",129-6-1).setDtCma("areia01cma.j3d",129-7).setDtBxo("obsidian01bxo.j3d",129-6-2).setIcon("bloco09.jf1",129-6-2).setExtCom("a").setWav("aa").setCozido("rocha_nether");		
			lastCub.opAcidoCorroi=false;						
			lastCub.ehAreia=true;			
			addCub("areia_nether_cai",129-6).setDtLat("obsidian01.j3d",129-6-1).setDtCma("areia01cma.j3d",129-7).setDtBxo("obsidian01bxo.j3d",129-6-2).setIcon("bloco09.jf1",129-6-2).setExtCom("a").setWav("aa").setCozido("rocha_nether");				
			lastCub.ehAreia=true;			
			lastCub.opAcidoCorroi=false;						
			lastCub.opAreiaCai=true;		
			lastCub.operavel=true;	
			addCub("degrau_areia_nether",129-6).setDtLat("obsidian01b.j3d",129-6-1).setDtCma("areia01cmab.j3d",129-7).setDtBxo("obsidian01bxo.j3d",129-6-2).setIcon("bloco15.jf1",129-6-2).setExtCom("a").setWav("aa").setCozido("rocha_nether");		
			lastCub.ehDegrau=true;			
			lastCub.ehAreia=true;
			lastCub.opAcidoCorroi=false;						
			lastCub.tc=0;
			lastCub.ocultador=false;		

			addCub("rocha_nether",129-8).setDtLat("obsidian01.j3d", 129-9).setDtCma("obsidian01cma.j3d",129-7).setDtBxo("obsidian01bxo.j3d",129-7).setIcon("bloco36.jf1",129-8).setExtCom("p").setWav("rr");
			lastCub.opMenosDtCma=true;			
			lastCub.opAcidoCorroi=false;						
			
		}
		{ // vators
			addCub("vator_nor",119).setDtLat("vator03.j3d",119-3).setDtNor("vator01.j3d",99).setDtCma("vator03cma.j3d",119-3).setDtBxo("vator03bxo.j3d",119-3).setIcon("vator01.jf1").setExtCom("e").setWav("rr");
			lastCub.setDtOes("vator04esq.j3d",119-3);
			lastCub.setDtLes("vator04dir.j3d",119-3);
			lastCub.operavel=true;// soh p garantir
			lastCub.ehVator=true;					
			
			addCub("vator_sul",119).setDtLat("vator03.j3d",119-3).setDtSul("vator01.j3d",99).setDtCma("vator03cma.j3d",119-3).setDtBxo("vator03bxo.j3d",119-3).setIcon("vator01.jf1").setExtCom("e").setWav("rr");
			lastCub.setDtOes("vator04dir.j3d",119-3);
			lastCub.setDtLes("vator04esq.j3d",119-3);			
			lastCub.operavel=true;
			lastCub.ehVator=true;
			
			addCub("vator_les",119).setDtLat("vator03.j3d",119-3).setDtLes("vator01.j3d",99).setDtCma("vator03cma.j3d",119-3).setDtBxo("vator03bxo.j3d",119-3).setIcon("vator01.jf1").setExtCom("e").setWav("rr");		
			lastCub.setDtNor("vator04esq.j3d",119-3);
			lastCub.setDtSul("vator04dir.j3d",119-3);						
			lastCub.operavel=true;
			lastCub.ehVator=true;					
			
			addCub("vator_oes",119).setDtLat("vator03.j3d",119-3).setDtOes("vator01.j3d",99).setDtCma("vator03cma.j3d",119-3).setDtBxo("vator03bxo.j3d",119-3).setIcon("vator01.jf1").setExtCom("e").setWav("rr");			
			lastCub.setDtNor("vator04dir.j3d",119-3);
			lastCub.setDtSul("vator04esq.j3d",119-3);									
			lastCub.operavel=true;			
			lastCub.ehVator=true;
			
			addCub("vator_cma",119).setDtLat("vator03.j3d",119-3).setDtCma("vator01cma.j3d",99).setDtBxo("vator03bxo.j3d",119-3).setIcon("vator01.jf1").setExtCom("e").setWav("rr");			
			lastCub.operavel=true;			
			lastCub.ehVator=true;					
			
			addCub("vator_bxo",119).setDtLat("vator03.j3d",119-3).setDtBxo("vator01bxo.j3d",99).setDtCma("vator03cma.j3d",119-3).setIcon("vator01.jf1").setExtCom("e").setWav("rr");				
			lastCub.operavel=true;		
			lastCub.ehVator=true;			
				
			addCub("haste_ns",99).setIcon("bloco09.jf1").setExtCom("e").setWav("rr");
			lastCub.ocultador = false;		
			lastCub.opFazSombra = false;		
			lastCub.opJoePodeMover=false;		
			lastCub.operavel=true;
			lastCub.opVatorPodeMover=false;
			lastCub.tl = 0.2f;
			lastCub.to = 0.2f;
			lastCub.tc = 0.2f;
			lastCub.tb = 0.2f;
			addCub("haste_lo",99).setIcon("bloco09.jf1").setExtCom("e").setWav("rr");
			lastCub.ocultador = false;		
			lastCub.operavel=true;			
			lastCub.opFazSombra = false;		
			lastCub.opJoePodeMover=false;
			lastCub.opVatorPodeMover=false;	
			lastCub.tn = 0.2f;
			lastCub.ts = 0.2f;
			lastCub.tc = 0.2f;
			lastCub.tb = 0.2f;		
			addCub("haste_cb",99).setIcon("bloco09.jf1").setExtCom("e").setWav("rr").setIncCal(-0.3f);
			lastCub.opJoePodeMover=false;
			lastCub.opVatorPodeMover=false;
			lastCub.operavel=true;			
			lastCub.ocultador = false;		
			lastCub.opFazSombra = false;
		}
		{ // tijolos
			addCub("tijolo_rocha",49).setDtLat("tijolo01.j3d",49-2).setDtCma("madeira01cma.j3d",49-2).setDtBxo("madeira01bxo.j3d",49-2).setIcon("bloco12.jf1",49).setExtCom("p").setWav("rr");
			lastCub.ehTijolo = true;
			addCub("degrau_tijolo_rocha",49).setDtLat("tijolo01b.j3d",49-2).setDtCma("madeira01cma.j3d",49-2).setDtBxo("madeira01bxo.j3d",49-2).setIcon("bloco12b.jf1",49).setExtCom("p").setWav("rr").ic=-0.5f; // incremento de cima
			lastCub.ehDegrau=true;						
			lastCub.ocultador = false;
			lastCub.ehTijolo = true;

			addCub("tijolo_vermelho",59).setDtLat("tijolo01.j3d",109).setDtCma("madeira01cma.j3d",109).setDtBxo("madeira01bxo.j3d",109).setIcon("bloco12.jf1",59).setExtCom("p").setWav("rr");
			lastCub.ehTijolo = true;
			addCub("degrau_tijolo_vermelho",59).setDtLat("tijolo01b.j3d",109).setDtCma("madeira01cma.j3d",109).setDtBxo("madeira01bxo.j3d",109).setIcon("bloco12b.jf1",59).setExtCom("p").setWav("rr").ic=-0.5f; // incremento de cima
			lastCub.ehDegrau=true;						
			lastCub.ocultador = false;
			lastCub.ehTijolo = true;
			
			addCub("tijolo_bege",J.altColor(J.cor[139],20))
				.setDtLat("tijolo01.j3d",J.altColor(J.cor[139],1))
				.setDtCma("madeira01cma.j3d",J.altColor(J.cor[139],1))
				.setDtBxo("madeira01bxo.j3d",J.altColor(J.cor[139],1))
				.setIcon("bloco12.jf1",139)
				.setExtCom("p").setWav("rr");
			lastCub.ehTijolo = true;
			addCub("degrau_tijolo_bege",J.altColor(J.cor[139],20))
				.setDtLat("tijolo01b.j3d",J.altColor(J.cor[139],1))
				.setDtCma("madeira01cma.j3d",J.altColor(J.cor[139],1))
				.setDtBxo("madeira01bxo.j3d",J.altColor(J.cor[139],1))
				.setIcon("bloco12b.jf1",139)
				.setExtCom("p").setWav("rr").ic=-0.5f; // incremento de cima
			lastCub.ocultador = false;
			lastCub.ehDegrau=true;						
			lastCub.ehTijolo = true;

			addCub("tijolo_marmore",119).setDtLat("tijolo01.j3d",118).setDtCma("madeira01cma.j3d",118).setDtBxo("madeira01bxo.j3d",118).setIcon("bloco12.jf1",118).setExtCom("p").setWav("rr");
			lastCub.opAcidoCorroi=false;						
			lastCub.ehTijolo = true;
			addCub("degrau_tijolo_marmore",119).setDtLat("tijolo01b.j3d",118).setDtCma("madeira01cma.j3d",118).setDtBxo("madeira01bxo.j3d",118).setIcon("bloco12b.jf1",118).setExtCom("p").setWav("rr").ic=-0.5f; // incremento de cima
			lastCub.ehDegrau=true;						
			lastCub.opAcidoCorroi=false;						
			lastCub.ehTijolo = true;
			lastCub.ocultador = false;

			
		}
		addSprite("muda_coq","muda03.j3d",J.cor[149]);
		lastCub.setExtCom("0");
		lastCub.operavel=true;
		lastCub.setIcon("muda02",149,148,147,146,145);
		tPlant.add(lastCub); // p animacao da muda		
		
		addCub("seed_del_viz",12);
		lastCub.operavel=true;		
		lastCub.crMap = J.cor[12];				
		
		{ // portais
			addCub("obsidian",169-7).setDtLat("obsidian01.j3d",163).setDtCma("obsidian01cma.j3d",163).setDtBxo("obsidian01bxo.j3d",163).setIcon("bloco09.jf1",169).setExtCom("p").setWav("rr");		
			lastCub.opAcidoCorroi=false;						
			addCub("PORTAL_NS",169).setIcon("bloco14.jf1",169);		
			lastCub.opLuzPortal = true;		
			lastCub.operavel=true;		
			lastCub.opEhPortal=true;		
			lastCub.tn = 0.1f;
			lastCub.ts = 0.1f;
			lastCub.crLes = null;
			lastCub.crOes = null;
			lastCub.crCma = null;
			lastCub.crBxo = null;	
			lastCub.opBounceFaces = true;
			lastCub.ehGhostBlock = true;
			lastCub.ocultador = false;
			
			addCub("PORTAL_LO",169).setIcon("bloco14.jf1",169);		
			lastCub.opLuzPortal = true;
			lastCub.operavel=true;		
			lastCub.opEhPortal=true;
			lastCub.crNor = null;
			lastCub.crSul = null;
			lastCub.crCma = null;
			lastCub.crBxo = null;
			lastCub.opBounceFaces = true;
			lastCub.tl = 0.1f;
			lastCub.to = 0.1f;
			lastCub.ehGhostBlock = true;
			lastCub.ocultador = false;				
		}
		{ // zetta
			addCub("areia_zetta",J.mixColor(42,162)).setDtLat("obsidian01.j3d",J.mixColor(41,161)).setDtCma("areia01cma.j3d",J.mixColor(41,161)).setIcon("bloco09.jf1",169).setExtCom("a").setWav("aa");
			lastCub.ehAreia=true;			
			lastCub.opAcidoCorroi=false;						
			lastCub.operavel=true;	
			
			addCub("areia_zetta_cai",J.mixColor(42,162)).setDtLat("obsidian01.j3d",J.mixColor(41,161)).setDtCma("areia01cma.j3d",J.mixColor(41,161)).setIcon("bloco09.jf1",169).setExtCom("a").setWav("aa");
			lastCub.ehAreia=true;			
			lastCub.operavel=true;
			lastCub.opAreiaCai=true;			
			
			addCub("degrau_areia_zetta",J.mixColor(42,162)).setDtLat("obsidian01b.j3d",J.mixColor(41,161)).setDtCma("areia01cmab.j3d",J.mixColor(41,161)).setIcon("bloco15.jf1",169).setExtCom("a").setWav("aa");
			lastCub.ehDegrau=true;						
			lastCub.ehAreia=true;			
			lastCub.opAcidoCorroi=false;						
			lastCub.tc=0;
			lastCub.ocultador=false;			
			

			addCub("rocha_zetta",J.mixColor(41,161)).setDtLat("obsidian01.j3d",J.mixColor(40,160)).setDtCma("obsidian01cma.j3d",J.mixColor(40,160)).setDtBxo("obsidian01bxo.j3d",J.mixColor(40,160)).setIcon("bloco36.jf1",169).setExtCom("p").setWav("rr");
			lastCub.opMenosDtCma=true;			
			lastCub.opAcidoCorroi=false;						

			// ia ser legal um cacto cortado ficar bege acima
			addCub("cacto_zetta",J.mixColor(169-7,109-7)).setDtLat("cacto01.j3d",179).setDtCma("cacto01cma.j3d",179).setCrBxo(139).setDtBxo("caule02bxo.j3d",139-2).setIcon("cacto05.jf1").setExtCom("m").setWav("mm").ehCaule=true;
			
			addAgua("fluido_zetta",119).setIcon("bloco09.jf1",119).setWav("gg").setOption("L").setCozido("nuvem");
			addAgua("fluido_zetta_corr",89).setIcon("bloco09.jf1",119).setWav("gg").setCozido("nuvem");
			lastCub.operavel=true;								
		}
		{ // lua e marte
		
			Color cr = J.altColor(J.cor[49],20);
			
			addCub("areia_lunar",cr).setDtLat("obsidian01.j3d",J.altColor(J.cor[49],30)).setDtCma("areia01cma.j3d",J.altColor(J.cor[49],10)).setIcon("bloco09.jf1",49-1).setExtCom("a").setWav("aa");
			lastCub.opAcidoCorroi=false;						
			lastCub.ehAreia=true;			
			lastCub.operavel=true;			
			addCub("areia_lunar_cai",cr).setDtLat("obsidian01.j3d",J.altColor(J.cor[49],30)).setDtCma("areia01cma.j3d",J.altColor(J.cor[49],10)).setIcon("bloco09.jf1",49-1).setExtCom("a").setWav("aa");
			lastCub.opAreiaCai=true;		
			lastCub.ehAreia=true;			
			lastCub.operavel=true;							
			addCub("degrau_areia_lunar",cr).setDtLat("obsidian01b.j3d",J.altColor(J.cor[49],30)).setDtCma("areia01cmab.j3d",J.altColor(J.cor[49],10)).setIcon("bloco15.jf1",49-1).setExtCom("a").setWav("aa");					
			lastCub.ehDegrau=true;						
			lastCub.tc=0;
			lastCub.ehAreia=true;			
			lastCub.ocultador=false;						
			
			cr = J.altColor(J.cor[49],20);
			
			addCub("rocha_lunar",cr).setDtLat("obsidian01.j3d", J.altColor(J.cor[49],20)).setDtCma("obsidian01cma.j3d",J.altColor(J.cor[49],1)).setDtBxo("obsidian01bxo.j3d",J.altColor(J.cor[49],1)).setIcon("bloco36.jf1",49-3).setExtCom("p").setWav("rr");
			lastCub.opMenosDtCma=true;			
			lastCub.opAcidoCorroi=false;						
			
			addSprite("pedra_lunar1","pedra01.j3d",cr); 
			addSprite("pedra_lunar2","pedra02.j3d",cr); 
			addSprite("pedra_lunar3","pedra03.j3d",cr);
			
			cr = J.mixColor(59,129);
			
			addCub("areia_marciana",cr)
				.setDtLat("obsidian01.j3d",J.altColor(cr,10))
				.setDtCma("areia01cma.j3d",J.altColor(cr,-2))
				.setIcon("bloco09.jf1",59-1)
				.setExtCom("a")
				.setWav("aa");
			lastCub.opAcidoCorroi=false;							
			lastCub.ehAreia=true;			
			lastCub.operavel=true;		
			addCub("areia_marciana_cai",cr)
				.setDtLat("obsidian01.j3d",J.altColor(cr,10))
				.setDtCma("areia01cma.j3d",J.altColor(cr,10))
				.setIcon("bloco09.jf1",59-1)
				.setExtCom("a")
				.setWav("aa");
			lastCub.ehAreia=true;				
			lastCub.opAreiaCai=true;		
			lastCub.operavel=true;						
			addCub("degrau_areia_marciana",cr)
				.setDtLat("obsidian01b.j3d",J.altColor(cr,10))
				.setDtCma("areia01cmab.j3d",J.altColor(cr,10))
				.setIcon("bloco15.jf1",59-1)
				.setExtCom("a")
				.setWav("aa");
			lastCub.ehAreia=true;				
			lastCub.ehDegrau=true;						
			lastCub.opAcidoCorroi=false;							
			lastCub.tc=0;
			lastCub.ocultador=false;			

			cr = J.altColor(cr,-20);
			
			addCub("rocha_marciana",cr)
				.setDtLat("obsidian01.j3d",J.altColor(cr,10))
				.setDtCma("obsidian01cma.j3d",J.altColor(cr,10))
				.setDtBxo("obsidian01bxo.j3d",J.altColor(cr,10))
				.setIcon("bloco36.jf1",59-3)
				.setExtCom("p")
				.setWav("rr");
			lastCub.opAcidoCorroi=false;							
			lastCub.opMenosDtCma=true;			
			
			cr = J.altColor(cr,+30);			
			addSprite("pedra_marciana1","pedra01.j3d",cr); 
			addSprite("pedra_marciana2","pedra02.j3d",cr); 
			addSprite("pedra_marciana3","pedra03.j3d",cr);
		}
		{ // frascos e bebiveis
			addFerr("frasco_vazio","frasco04.j3d").setIcon("frasco02.jf1",119);
			lastCub.dtCen.tingir(89,addPal(119));
			lastCub.dtCen.tingir(99,addPal(119));
			lastCub.opQtdEhSaude = false;
			lastCub.opGetComClick=true;						
			addFerr("frasco_agua","frasco04.j3d").setIcon("frasco03.jf1",119,118,89,87,85);
			lastCub.dtCen.tingir(89,addPal(119));		
			lastCub.dtCen.tingir(99,addPal(9));
			lastCub.ehBebivel=true;
			lastCub.opQtdEhSaude = false;		
			lastCub.opGetComClick=true;						
			addFerr("pocao_life","frasco04.j3d").setIcon("frasco03.jf1",119,118,99,97,95);
			lastCub.dtCen.tingir(89,addPal(119));
			//lastCub.opAutoGet=true; // ???
			lastCub.opGetComClick=true;						
			//lastCub.dtCen.tingir(89,addPal(99));
			lastCub.ehBebivel=true;		
			lastCub.opQtdEhSaude = false;				
			
			addFerr("frasco_suco_maca","frasco04.j3d").setIcon("frasco03.jf1",119,118,139,137,135);
			lastCub.dtCen.tingir(89,addPal(119));		
			lastCub.dtCen.tingir(99,addPal(109));
			lastCub.ehBebivel=true;
			lastCub.opGetComClick=true;						
			lastCub.opQtdEhSaude = false;			
		}		
		{ // comiveis
			addPao("pao_cru","pao02.j3d",J.mixColor(139,119),"").setIcon("pao03.jf1",119).setCozido("pao");
			lastCub.opGetComClick=true;						
			lastCub.ehComivel=true;
			addPao("pao","pao02.j3d",J.cor[139],"").setIcon("pao03.jf1",139).setPotCalor(1);
			lastCub.opGetComClick=true;						
			lastCub.ehComivel=true;
			addPao("ovo","ovo01.j3d",J.cor[139],"").setIcon("ovo03.jf1",139);
			lastCub.ehComivel=true;
			lastCub.opGetComClick=true;						
			lastCub.setCozido("ovo_cozido");
			addPao("ovo_cozido","ovo01.j3d",J.cor[119],"").setIcon("ovo03.jf1",119);
			lastCub.ehComivel=true;
			lastCub.opGetComClick=true;						
			addPao("maca","maca04.j3d",J.cor[99],"4").setIcon("maca07.jf1",99);
			lastCub.ehComivel=true;			
			lastCub.opGetComClick=true;						
			addPao("carne","carne01.j3d",J.cor[99],"").setIcon("carne02.jf1",99);
			lastCub.setCozido("bife");
			lastCub.opGetComClick=true;						
			lastCub.ehComivel=true;
			addPao("bife","carne02.j3d",J.cor[109],"").setIcon("bife01.jf1",99);
			lastCub.ehComivel=true;						
			lastCub.opGetComClick=true;			
		}
		{ // explosivos
			addCub("tnt",99).setDtLat("tnt02.j3d",79).setDtCma("tnt02cma.j3d",179).setIcon("tnt01.jf1").setWav("mm").setExtCom("0");
			lastCub.operavel=true;		
			addCub("tnt_On",99).setDtLat("tnt02.j3d",79).setDtCma("tnt02cma.j3d",179).setIcon("tnt01.jf1").setWav("mm");
			lastCub.operavel=true;		
			lastCub.ocultador=false;
			lastCub.opTreme = true;			
			
			addCub("exp",12).operavel=true;
			lastCub.ehGhostBlock=true;
			addCub("exp2",14).operavel=true;
			lastCub.ehGhostBlock=true;			
			
		}
		{ // maquinarios
			{ // esteiras
					addCub("esteira_nor",119).setDtLat("vator03.j3d",119-3).setDtCma("esteiraCma02.j3d",99).setDtBxo("vator03bxo.j3d",119-3).setIcon("estei01.jf1").setExtCom("e").setWav("rr");
					lastCub.ehConEl = true;						
					lastCub.ehEsteira=true;		
					lastCub.operavel=true;		
					
					addCub("esteira_sul",119).setDtLat("vator03.j3d",119-3).setDtCma("esteiraCma02.j3d",99).setDtBxo("vator03bxo.j3d",119-3).setIcon("estei01.jf1").setExtCom("e").setWav("rr");
					lastCub.ehConEl = true;						
					lastCub.dtCma.rodar90('Y');
					lastCub.dtCma.rodar90('Y');
					lastCub.ehEsteira=true;		
					lastCub.operavel=true;		
					
					addCub("esteira_les",119).setDtLat("vator03.j3d",119-3).setDtCma("esteiraCma02.j3d",99).setDtBxo("vator03bxo.j3d",119-3).setIcon("estei01.jf1").setExtCom("e").setWav("rr");
					lastCub.dtCma.rodar90('Y');
					lastCub.ehConEl = true;						
					lastCub.ehEsteira=true;		
					lastCub.operavel=true;			
					
					addCub("esteira_oes",119).setDtLat("vator03.j3d",119-3).setDtCma("esteiraCma02.j3d",99).setDtBxo("vator03bxo.j3d",119-3).setIcon("estei01.jf1").setExtCom("e").setWav("rr");
					lastCub.dtCma.rodar90('y');
					lastCub.ehEsteira=true;
					lastCub.ehConEl = true;				
					lastCub.operavel=true;					
			}

			addCub("identificador",119)  
				.setDtCma("ident02cma.j3d",159)
				.setDtLat("ident02.j3d",119-3)				
				.setDtBxo("ident02bxo.j3d",159-3)
				.setIcon("ident01.jf1")
				.setExtCom("e").setWav("rr");
			lastCub.ocultador=false; // p nao esconder a face da h a s t e  do v a t o r  a b a i  x o
			lastCub.ehConEl = true;						
			lastCub.operavel=true;						
			
			addCub("ger_solar",119)  //"painel_solar" "gerador_solar"
				.setDtCma("geradorSolar01.j3d",189)
				.setIcon("gerSol02.jf1")
				.setExtCom("e").setWav("rr");
			lastCub.tc = -0.4f;
			lastCub.ehGhostBlock=true;
			lastCub.opForceImpCma=true; // este force nao tah bom
			lastCub.ocultador = false;
			lastCub.ehConEl = true;						
			lastCub.operavel=true;						
			

			addCub("bateria_fixa",119)
				.setDtLat("bateria02.j3d",119-3)
				.setDtCma("vator03cma.j3d",119-3)
				.setIcon("bateria01.jf1")
				.setExtCom("e").setWav("rr");				
			lastCub.temExtraByte=true;
			lastCub.dtNor.crAnt=4;
			lastCub.dtSul.crAnt=4;
			lastCub.dtLes.crAnt=4;
			lastCub.dtOes.crAnt=4;
			lastCub.ehConEl = true;						
			//lastCub.opLuzInf20 = true;
			lastCub.operavel=true;						
			
			addCub("extractor",119).setDtLat("maquina06.j3d",69).setDtCma("vator03cma.j3d",119-3).setDtBxo("vator03bxo.j3d",119-3).setIcon("maq03.jf1",119,119-2,119-4,69,69-2).setExtCom("e").setWav("rr");
			lastCub.ehConEl = true;		
			lastCub.operavel=true;	

			addCub("pump",119).setDtLat("vator03.j3d",119-3).setDtCma("elev01Cma.j3d",89).setDtBxo("vator03bxo.j3d",119-3).setIcon("elev01.jf1").setExtCom("e").setWav("rr").setIncCal(-0.01f);
			lastCub.ocultador=false;
			lastCub.ehConEl = true;						
			lastCub.ehConCano=true;			
			lastCub.operavel=true;



			
		}
		{ // eletricos
			{ // switches
				// cma fica sem o nome, eh default
				// ON deve ser sempre o tip maior
				
				addSwitch("switch_off","switch02off.j3d","").setIcon("switch01.jf1").setExtCom("e").ehSwitchOff=true;
				addSwitch("switch_on" ,"switch02on.j3d","").setIcon("switch01.jf1").setExtCom("e").ehSwitchOn=true;

				addSwitch("switch_nor_off","switchnor01off.j3d","").setIcon("switch01.jf1").setExtCom("e").ehSwitchOff=true;
				addSwitch("switch_nor_on" ,"switchnor01on.j3d","").setIcon("switch01.jf1").setExtCom("e").ehSwitchOn=true;

				addSwitch("switch_sul_off","switchnor01off.j3d","s").setIcon("switch01.jf1").setExtCom("e").ehSwitchOff=true;
				addSwitch("switch_sul_on" ,"switchnor01on.j3d","s").setIcon("switch01.jf1").setExtCom("e").ehSwitchOn=true;

				addSwitch("switch_les_off","switchnor01off.j3d","l").setIcon("switch01.jf1").setExtCom("e").ehSwitchOff=true;
				addSwitch("switch_les_on" ,"switchnor01on.j3d","l").setIcon("switch01.jf1").setExtCom("e").ehSwitchOn=true;

				addSwitch("switch_oes_off","switchnor01off.j3d","o").setIcon("switch01.jf1").setExtCom("e").ehSwitchOff=true;
				addSwitch("switch_oes_on" ,"switchnor01on.j3d","o").setIcon("switch01.jf1").setExtCom("e").ehSwitchOn=true;
				
				addSwitch("switch_bxo_off","switchbxo01off.j3d","").setIcon("switch01.jf1").setExtCom("e").ehSwitchOff=true;
				addSwitch("switch_bxo_on" ,"switchbxo01on.j3d","").setIcon("switch01.jf1").setExtCom("e").ehSwitchOn=true;
			}
			{ // botoes
				// cma fica sem o nome, eh default
				// ON deve ser sempre o tip maior
				addSwitch("botao_off","botao01off.j3d","").setIcon("botao01.jf1").setExtCom("e").ehBotaoOff=true;
				addSwitch("botao_on" ,"botao01on.j3d","").setIcon("botao01.jf1").setExtCom("e").ehBotaoOn=true;
				
				addSwitch("botao_nor_off","botaonor01off.j3d","").setIcon("botao01.jf1").setExtCom("e").ehBotaoOff=true;
				addSwitch("botao_nor_on" ,"botaonor01on.j3d","").setIcon("botao01.jf1").setExtCom("e").ehBotaoOn=true;

				addSwitch("botao_sul_off","botaonor01off.j3d","s").setIcon("botao01.jf1").setExtCom("e").ehBotaoOff=true;
				addSwitch("botao_sul_on" ,"botaonor01on.j3d","s").setIcon("botao01.jf1").setExtCom("e").ehBotaoOn=true;

				addSwitch("botao_les_off","botaonor01off.j3d","l").setIcon("botao01.jf1").setExtCom("e").ehBotaoOff=true;
				addSwitch("botao_les_on" ,"botaonor01on.j3d","l").setIcon("botao01.jf1").setExtCom("e").ehBotaoOn=true;

				addSwitch("botao_oes_off","botaonor01off.j3d","o").setIcon("botao01.jf1").setExtCom("e").ehBotaoOff=true;
				addSwitch("botao_oes_on" ,"botaonor01on.j3d","o").setIcon("botao01.jf1").setExtCom("e").ehBotaoOn=true;
				
				addSwitch("botao_bxo_off","botaoBxo01off.j3d","").setIcon("botao01.jf1").setExtCom("e").ehBotaoOff=true;
				addSwitch("botao_bxo_on" ,"botaoBxo01on.j3d","").setIcon("botao01.jf1").setExtCom("e").ehBotaoOn=true;

		
			}
			
			
			addCub("vago200",99);
			addCub("vago1",99);
			addCub("vago2",99);
			
			
		}
		{ // tanques
			
			addTanque("vazio",119-1,"");
			addTanque("agua",89,"l");
			addTanque("poh_ferro",49,"");
			addTanque("poh_enxofre",79,"");
			addTanque("latex",119,"l");
			
			addCub("seed_sem_alga",99).operavel=true;
			addCub("vago402",99);
			addCub("vago403",99);
		}		

		{	// mobs (na verdade sao  s e e d s )
			addCub("zumbi",12).setIcon("ovo04",69).ehSeedMob=true; lastCub.operavel=true;
			addCub("aldea",12).setIcon("ovo04",139).ehSeedMob=true; lastCub.operavel=true;
			addCub("aldeao",12).setIcon("ovo04",139).ehSeedMob=true; lastCub.operavel=true;
			addCub("bruxa",12).setIcon("ovo04",69).ehSeedMob=true; lastCub.operavel=true;
			addCub("mago",12).setIcon("ovo04",89).ehSeedMob=true; lastCub.operavel=true;
			addCub("ghost",12).setIcon("ovo04",179).ehSeedMob=true; lastCub.operavel=true;
			addCub("minotauro",12).setIcon("ovo04",59).ehSeedMob=true; lastCub.operavel=true;
			addCub("caveira",12).setIcon("ovo04",119).ehSeedMob=true; lastCub.operavel=true;

			addCub("creeper",12).setIcon("ovo04",149).ehSeedMob=true;		lastCub.operavel=true;

			addCub("galinha",12).setIcon("ovo04",119).ehSeedMob=true;lastCub.operavel=true;
			addCub("avestruz",12).setIcon("ovo04",179).ehSeedMob=true;lastCub.operavel=true;

			addCub("porco",12).setIcon("ovo04",209).ehSeedMob=true;lastCub.operavel=true;
			addCub("ovelha",12).setIcon("ovo04",119).ehSeedMob=true;		lastCub.operavel=true;
			addCub("cavalo",12).setIcon("ovo04",109).ehSeedMob=true;		lastCub.operavel=true;
			addCub("urso",12).setIcon("ovo04",59).ehSeedMob=true;		lastCub.operavel=true;
			addCub("vaca",12).setIcon("ovo04",119).ehSeedMob=true;lastCub.operavel=true;			
			
			addCub("aranha",12).setIcon("ovo04",179).ehSeedMob=true;lastCub.operavel=true;			
			
			addCub("vago101",12).setIcon("ovo04",119).ehSeedMob=true;lastCub.operavel=true;			
			addCub("vago102",12).setIcon("ovo04",119).ehSeedMob=true;lastCub.operavel=true;			
			addCub("vago103",12).setIcon("ovo04",119).ehSeedMob=true;lastCub.operavel=true;			
		}




		addSprite("isca","isca01.j3d",null);
		lastCub.dtCen.desBxo(null,-0.5f,1);
		lastCub.dtCen.reg3d();
		lastCub.opBounceCen=true;
		lastCub.operavel=true;

		addFerr("peixe1","peixe02.j3d",49).setIcon("peixe02.jf1",49).setCozido("peixe_assado").ehComivel=true;
		lastCub.opGetComClick=true;		
		addFerr("peixe2","peixe02.j3d",119).setIcon("peixe02.jf1",119).setCozido("peixe_assado").ehComivel=true;
		lastCub.opGetComClick=true;		
		addFerr("peixe3","peixe02.j3d",89).setIcon("peixe02.jf1",89).setCozido("peixe_assado").ehComivel=true;
		lastCub.opGetComClick=true;		
		
		addFerr("peixe_assado","peixe02.j3d",109).setIcon("peixe02.jf1",109).ehComivel=true;
		lastCub.opGetComClick=true;		

		addFerr("sem_alface","semente02.j3d",69).setIcon("saco05.jf1",69).setWav("f ").setPotCalor(1).ehEmbrulho=true; // ajudou no shape da mao
		lastCub.opGetComClick=true;		
		lastCub.ehSemente=true;							
		lastCub.temExtraByte=true;					
		addTrigo("alface_crescendo", "alface01.j3d", 69, "c").setIcon("alface04.jf1",69);
		addTrigo("alface", "alface01.j3d", 69,"").setIcon("alface04.jf1",69);
		lastCub.ehComivel=true;		
		
		addFerr("sem_repolho","semente02.j3d",219).setIcon("saco05.jf1",69-3).setWav("f ").setPotCalor(1).ehEmbrulho=true; // ajudou no shape da mao
		lastCub.opGetComClick=true;		
		lastCub.ehSemente=true;							
		lastCub.temExtraByte=true;					
		addTrigo("repolho_crescendo", "alface01.j3d", 219, "c").setIcon("alface04.jf1",69);
		addTrigo("repolho", "alface01.j3d", 219,"").setIcon("alface04.jf1",69);
		lastCub.ehComivel=true;	

		addFerr("sem_cenoura","semente02.j3d",159).setIcon("saco05.jf1",159).setWav("f ").setPotCalor(1).ehEmbrulho=true; // ajudou no shape da mao
		lastCub.opGetComClick=true;		
		lastCub.ehSemente=true;							
		lastCub.temExtraByte=true;					
		addTrigo("cenoura_crescendo", "cenoura01.j3d", null, "c").setIcon("cenour01.jf1",159);
		addTrigo("cenoura_madura", "cenoura01.j3d", null,"").setIcon("cenour01.jf1",159);
		addFerr("cenoura","cenoura02.j3d",null).setIcon("cenour02.jf1");
		lastCub.opGetComClick=true;		
		lastCub.ehComivel=true;	
		
		addFerr("sem_beterraba","semente02.j3d",169).setIcon("saco05.jf1",169).setWav("f ").setPotCalor(1).ehEmbrulho=true; // ajudou no shape da mao
		lastCub.ehSemente=true;							
		lastCub.opGetComClick=true;		
		lastCub.temExtraByte=true;					
		addTrigo("beterraba_crescendo", "beterraba01.j3d", null, "c4").setIcon("beterr01.jf1",169);
		addTrigo("beterraba_madura", "beterraba01.j3d", null,"4").setIcon("beterr01.jf1",169);
		addFerr("beterraba","beterraba02.j3d",null).setIcon("beterr02.jf1");
		lastCub.opGetComClick=true;		
		lastCub.ehComivel=true;			

		addFerr("sem_sortida","semente02.j3d",179).setIcon("saco05.jf1",179).setWav("f ").setPotCalor(1).ehEmbrulho=true;
		lastCub.opGetComClick=true;		
		lastCub.ehSemente=true;

		addFerr("sem_abobora","semente02.j3d",159).setIcon("saco05.jf1",159).setWav("f ").setPotCalor(1).ehEmbrulho=true; 
		lastCub.ehSemente=true;							
		lastCub.opGetComClick=true;		
		lastCub.temExtraByte=true;
		addTrigo("abobora_crescendo", "abobora01.j3d", 159, "c4").setIcon("abob04.jf1",159);
		addTrigo("abobora", "abobora01.j3d", 159,"4").setIcon("abob04.jf1",159).setExtCom("0");
		// cozinhar depois no tacho. Torta de abobora tb seria bom.

		addFerr("sem_melancia","semente02.j3d",149).setIcon("saco05.jf1",149).setWav("f ").setPotCalor(1).ehEmbrulho=true; 
		lastCub.ehSemente=true;							
		lastCub.opGetComClick=true;		
		lastCub.temExtraByte=true;
		addTrigo("melancia_crescendo", "abobora01.j3d", 149, "c4").setIcon("abob04.jf1",149);
		addTrigo("melancia", "abobora01.j3d", 149,"4").setIcon("abob04.jf1",149).setExtCom("0");
		// fatias depois

		addFerr("sem_melao","semente02.j3d",79).setIcon("saco05.jf1",79).setWav("f ").setPotCalor(1).ehEmbrulho=true; 
		lastCub.ehSemente=true;							
		lastCub.opGetComClick=true;		
		lastCub.temExtraByte=true;
		addTrigo("melao_crescendo", "abobora01.j3d", 79, "c4").setIcon("abob04.jf1",79);
		addTrigo("melao", "abobora01.j3d", 79,"4").setIcon("abob04.jf1",79).setExtCom("0");
		// fatias depois

		addFerr("presas","presas02.j3d",99).setIcon("presas01.jf1",119);
		lastCub.opAutoGet=true;
		lastCub.opGetComClick=true;		

		addFerr("sem_milho","semente02.j3d",139).setIcon("saco05.jf1",139).setWav("f ").setPotCalor(1).ehEmbrulho=true; 
		lastCub.ehSemente=true;							
		lastCub.opGetComClick=true;		
		lastCub.temExtraByte=true;
		addTrigo("milho_crescendo", "pehmilho03.j3d", 149, "c").setIcon("milho02.jf1",149).setPotCalor(1);
		addTrigo("milho_maduro", "pehmilho03.j3d", J.mixColor(149,69),"").setIcon("milho02.jf1",149).setExtCom("0").setPotCalor(1);
		lastCub.operavel=true;
		addTrigo("milho", "pehmilho03.j3d", J.mixColor(139,79),"").setIcon("milho02.jf1",139).setExtCom("0").setPotCalor(2);
 		addFerr("espiga_verde", "espiga02.j3d").setIcon("espiga04.jf1").setCozido("milho_cozido");
		lastCub.opGetComClick=true;		
 		addFerr("espiga", "espiga01.j3d").setIcon("espiga05.jf1");
		lastCub.opGetComClick=true;		
 		addFerr("milho_cozido", "espiga04.j3d").setIcon("espiga03.jf1");
		lastCub.opGetComClick=true;		
		lastCub.ehComivel=true;

		addPao("fatia_melancia","fatia01.j3d",null,"").setIcon("fatia01.jf1");
		lastCub.opGetComClick=true;		
		lastCub.ehComivel=true;		
		
		addPao("fatia_melao","fatia02.j3d",null,"").setIcon("fatia04.jf1");
		lastCub.opGetComClick=true;		
		lastCub.ehComivel=true;

		addFerr("veneno","frasco04.j3d").setIcon("frasco03.jf1",119,118,179,177,175);
		lastCub.dtCen.tingir(89,addPal(119));
		lastCub.dtCen.tingir(99,addPal(16));
		lastCub.ehBebivel=true;		
		lastCub.opGetComClick=true;		
		lastCub.opQtdEhSaude = false;

		addFerr("pocao_cel","frasco04.j3d").setIcon("frasco03.jf1",119,118,209,207,205);
		lastCub.dtCen.tingir(89,addPal(119));
		lastCub.dtCen.tingir(99,addPal(209));
		lastCub.ehBebivel=true;		
		lastCub.opGetComClick=true;		
		lastCub.opQtdEhSaude = false;						
		
	
		addFerr("sem_batata","semente02.j3d",139).setIcon("saco05.jf1",139).setWav("f ").setPotCalor(1).ehEmbrulho=true; // ajudou no shape da mao
		lastCub.ehSemente=true;							
		lastCub.temExtraByte=true;					
		lastCub.opGetComClick=true;		
		addTrigo("batata_crescendo", "batata01.j3d", null, "c4").setIcon("batata01.jf1",139);
		addTrigo("batata_madura", "batata01.j3d", null,"4").setIcon("batata01.jf1",139);
		//lastCub.opGetComClick=true;		// nao aqui pq precisa gastar estamina
		addFerr("batata","batata02.j3d",null).setIcon("batata02.jf1",139).setCozido("batata_assada");
		lastCub.opGetComClick=true;		
		lastCub.ehComivel=true;			
		addFerr("batata_assada","batata02.j3d",null).setIcon("batata02.jf1",159);
		lastCub.opGetComClick=true;
		lastCub.ehComivel=true;			

		{ // c r i s t a l   z e t t a
			Color cr = J.cor[169];
			addSprite("cristal_zetta1","cristalZetta02.j3d",cr); 
			lastCub.dtCen.resize(2f);
			addSprite("cristal_zetta2","cristalZetta03.j3d",cr); 
			lastCub.dtCen.resize(2f);
			addSprite("cristal_zetta3","cristalZetta04.j3d",cr);
			lastCub.dtCen.resize(2f);
		}
		addCub("rocha_proEl",14).setDtLat("obsidian01.j3d",9).setDtCma("obsidian01cma.j3d",9).setDtBxo("obsidian01bxo.j3d",9).setIcon("bloco09.jf1",79).setExtCom("p").setWav("rr");
		lastCub.ehConEl = true;		
		lastCub.operavel=true;
		addCub("rocha_antiEl",9).setDtLat("obsidian01.j3d",14).setDtCma("obsidian01cma.j3d",9).setDtBxo("obsidian01bxo.j3d",9).setIcon("bloco09.jf1",189).setExtCom("p").setWav("rr");
		lastCub.ehConEl = true;				
		lastCub.operavel=true;	
		
		addCub("rede1",179).setIcon("bloco33.jf1",179,177,174,10,59).setExtCom("e").setWav("rr");
		lastCub.operavel=true;					
		lastCub.opLuzLife1000=true;
		lastCub.ocultador=false;
		lastCub.ehConEl = true;				
		//lastCub.inflar(-0.3f);
		
		addCub("duplicador",13).setDtLat("obsidian01.j3d",16).setDtCma("obsidian01cma.j3d",16).setDtBxo("obsidian01bxo.j3d",16).setIcon("bloco09.jf1",169).setExtCom("p").setWav("rr");
		lastCub.operavel=true;		
		
		addCub("seed_exp",12).operavel=true;				

		// degrau25 e degrau75
		addCub("degrau_alto_caule",59-8).setCrCmaBxo(139).setIcon("bloco06b.jf1",139,59,59-2,139-4,12).setExtCom("m").setWav("mm").setPotCalor(4);
			lastCub.ehDegrau=true;					
		lastCub.ocultador=false;
		lastCub.tc=0.25f;
		lastCub.ehCaule = true;
		
		addCub("degrau_caule",59-8).setCrCmaBxo(139).setIcon("bloco06c.jf1",139,59,59-2,139-4,12).setExtCom("m").setWav("mm").setPotCalor(4);
			lastCub.ehDegrau=true;					
		lastCub.tc=0f;
		lastCub.ocultador=false;
		lastCub.ehCaule = true;
		addCub("degrau_baixo_caule",59-8).setCrCmaBxo(139).setIcon("bloco06d.jf1",139,59,59-2,139-4,12).setExtCom("m").setWav("mm").setPotCalor(4);
		lastCub.ocultador=false;		
		lastCub.ehDegrau=true;					
		lastCub.tc=-0.25f;		
		lastCub.ehCaule = true;		

		addCub("serra",119).setDtLat("maquina06.j3d",59).setDtCma("serra01cma.j3d",16).setDtBxo("serra01bxo.j3d",16).setIcon("maq03.jf1",119,119-2,119-4,59,59-2).setExtCom("e").setWav("rr");
		lastCub.ocultador=false;
		lastCub.ehConEl = true;		
		lastCub.operavel=true;			
		
		addCub("madeira_facil",139).setDtLat("madeira02.j3d",139-1).setDtBxo("madeira01bxo.j3d",139-1).setDtCma("madeira01cma.j3d",139-1).setIcon("bloco11.jf1",139).setExtCom("0").setWav("mm").setPotCalor(3);
		lastCub.ocultador=false;
		lastCub.opGetComClick=true;		
		lastCub.inflar(-0.02f);
		lastCub.ehMadeira=true;		
		
		{ // mais tanques, tem mais lah p baixo

			addTanque("poh_ouro",159,"");
			addTanque("poh_cobre",99,"");
			addTanque("poh_carvao",16,"");
		} 

		addCub("seed_piramide",12).operavel=true;

		{ // ladrilhos. Separar num metodo depois e deixar o caminho configuravel.
			// seria bom separar isso num outro metodo e fazer o caminho configurahvel
			// dah p fazer mais shapes j3d p aplicar aqui
			int qq=0;
			String cm="ladrilho"+J.R00(3);
			for(int q=4; q<=20; q++){
				if(q==9) continue;
				if(q==10) continue;
				qq = q*10+9;				
				addCub("ladrilho"+q,  qq)
					.setDtLat(cm+".j3d", qq-2)
					.setDtCma(cm+"cma.j3d", qq-2)
					.setDtBxo(cm+"bxo.j3d", qq-2)
					.setIcon("bloco23.jf1", qq)
					.setExtCom("p").setWav("rr");		
			}
			
			addCub("ladrilho25",  J.cor[11])
				.setDtLat(cm+".j3d", J.cor[11].darker())
				.setDtCma(cm+"cma.j3d", J.cor[11].darker())
				.setDtBxo(cm+"bxo.j3d", J.cor[11].darker())
				.setIcon("bloco23.jf1", 89)
				.setExtCom("p").setWav("rr");		
			addCub("ladrilho26",  J.cor[16])
				.setDtLat(cm+".j3d", J.cor[17])
				.setDtCma(cm+"cma.j3d", J.cor[17])
				.setDtBxo(cm+"bxo.j3d", J.cor[17])
				.setIcon("bloco23.jf1", 179)
				.setExtCom("p").setWav("rr");				
			addCub("ladrilho27",  J.cor[209].brighter())
				.setDtLat(cm+".j3d", J.cor[207].brighter())
				.setDtCma(cm+"cma.j3d", J.cor[207].brighter())
				.setDtBxo(cm+"bxo.j3d", J.cor[207].brighter())
				.setIcon("bloco23.jf1", 209)
				.setExtCom("p").setWav("rr");				
		}

		addCub("vago100",99);		
		addCub("vago101",99);		
		addCub("vago102",99);		
		addCub("vago103",99);		

		addCub("envasadora",119).setDtLat("vator03.j3d",119-3, "frasco_carimbo01.j3d", 119-3).setDtCma("elev01Cma.j3d",89).setDtBxo("vator03bxo.j3d",119-3).setIcon("elev01.jf1").setExtCom("e").setWav("rr").setIncCal(-0.01f);
		lastCub.setDtBxo("envasadora01bxo.j3d", 119-2);
		lastCub.ocultador=false; // daria ateh p por efeito de sombra no carimbo. Ia ficar meio almofadado.
		lastCub.ehConEl = true;						
		lastCub.operavel=true;

		addTanque("suco_maca",139,"l");

		addCub("ploter",119).setDtLat("vator03.j3d",119-3, "bau_carimbo01.j3d", 119-3).setDtCma("elev01Cma.j3d",89).setDtBxo("vator03bxo.j3d",119-3).setIcon("elev01.jf1").setExtCom("e").setWav("rr");
		lastCub.ehConEl = true;						
		lastCub.operavel=true;
		
		addCub("juicer",119).setDtLat("vator03.j3d",119-3, "maca_carimbo01.j3d", 119-3).setDtCma("elev01Cma.j3d",89).setDtBxo("vator03bxo.j3d",119-3).setIcon("elev01.jf1").setExtCom("e").setWav("rr");
		lastCub.ehConEl = true;						
		lastCub.ehConCano = true;
		lastCub.operavel=true;		

		addCub("transp_vazio",119).setIcon("bloco21.jf1",119).setExtCom("b").setWav("rr");
		lastCub.operavel=true;		
		lastCub.ocultador=false;		
		lastCub.ehConTransp = true;						
		addSlotsTransp(); // eh melhor q isso fique por ultimo. Ajustar depois quando for resetar o mapa

		addCub("retirador",119).setDtLat("vator03.j3d",119-3, "bau_out_carimbo01.j3d", 69).setDtCma("elev01Cma.j3d",89).setDtBxo("vator03bxo.j3d",119-3).setIcon("elev01.jf1").setExtCom("e").setWav("rr");
		lastCub.ehConEl = true;						
		lastCub.ehConTransp = true;								
		lastCub.operavel=true;
		
		addCub("guardador",119).setDtLat("vator03.j3d",119-3, "bau_in_carimbo01.j3d", 59).setDtCma("elev01Cma.j3d",89).setDtBxo("vator03bxo.j3d",119-3).setIcon("elev01.jf1").setExtCom("e").setWav("rr");
		lastCub.ehConEl = true;						
		lastCub.ehConTransp = true;								
		lastCub.operavel=true;		

		addCub("selecionador",119).setDtLat("vator03.j3d",119-3, "olho_carimbo01.j3d", 119-3).setDtCma("elev01Cma.j3d",89).setDtBxo("vator03bxo.j3d",119-3).setIcon("elev01.jf1").setExtCom("e").setWav("rr");
		lastCub.ehConEl = true;						
		lastCub.ehConTransp = true;								
		lastCub.operavel=true;		


		{ // canos
			addCano("cano_vazio",119);
			lastCub.temExtraByte=false;
			lastCub.opAltInfCano = true;

			addCano("cano_agua",89);			

			addCub("cano_suco_maca",139);
			addCub("vago5000",99);
		}

		// PENSAR NISSO MELHOR. Seria p substituir a montagem de adobe umido manualmente: um misturador juntaria terra e agua fazendo lama; Uma outra maquina entao criaria o adobe umido a partir de lama.
		// addCub("misturador",119).setDtLat("vator03.j3d",119-3, "vortice_carimbo01.j3d", 59).setDtCma("elev01Cma.j3d",89).setDtBxo("vator03bxo.j3d",119-3).setIcon("elev01.jf1").setExtCom("p").setWav("rr");
		// lastCub.ehConEl = true;						
		// lastCub.operavel=true;				

		addCub("degrau_adobe_umido",139-4).setDtLat("tijolo01b.j3d",139-5).setDtCma("madeira01cmab.j3d",139-5).setDtBxo("madeira01bxo.j3d",139-5).setIcon("bloco12b.jf1",139-4).setExtCom("0").setWav("rr").setCozido("degrau_tijolo_bege");
		lastCub.ocultador=false;
		lastCub.ehDegrau=true;					
		lastCub.tc=0;
		lastCub.opGetComClick=true;		
		lastCub.operavel = true;
		// este retira com a mao
		addCub("degrau_adobe_seco",139-2).setDtLat("tijolo01b.j3d",139-3).setDtCma("madeira01cmab.j3d",139-3).setDtBxo("madeira01bxo.j3d",139-3).setIcon("bloco12b.jf1",139-2).setExtCom("0").setWav("rr").setCozido("degrau_tijolo_bege");
		lastCub.ehDegrau=true;					
		lastCub.ocultador=false;		
		lastCub.opGetComClick=true;
		lastCub.tc=0;
		// este retira soh com picareta, como se tivesse sido assentado com argamassa
		addCub("degrau_adobe",139-2).setDtLat("tijolo01b.j3d",139-3).setDtCma("madeira01cmab.j3d",139-3).setDtBxo("madeira01bxo.j3d",139-3).setIcon("bloco12b.jf1",139-2).setExtCom("p").setWav("rr").setCozido("degrau_tijolo_bege");
		lastCub.ocultador=false;		
		lastCub.ehDegrau=true;					
		lastCub.tc=0;		
		// este eh como dois degrais empilhados
		addCub("adobe",139-2).setDtLat("tijolo01.j3d",139-3).setDtCma("madeira01cma.j3d",139-3).setDtBxo("madeira01bxo.j3d",139-3).setIcon("bloco09.jf1",139-2).setExtCom("p").setWav("rr").setCozido("tijolo_bege");
		
//-----------------------------------		
		addCub("adobe_seco",139-2).setDtLat("tijolo01.j3d",139-3).setDtCma("madeira01cma.j3d",139-3).setDtBxo("madeira01bxo.j3d",139-3).setIcon("bloco12.jf1",139-2).setExtCom("p").setWav("rr").setCozido("tijolo_bege");
		

		addCub("adobe_umido",139-4).setDtLat("tijolo01.j3d",139-5).setDtCma("madeira01cma.j3d",139-5).setDtBxo("madeira01bxo.j3d",139-5).setIcon("bloco12.jf1",139-4).setExtCom("0").setWav("rr").setCozido("tijolo_bege");
		lastCub.opGetComClick=true;		
		lastCub.operavel = true;


		addCub("vagooooooo",99);


		addFerr("chave_boca","chaveboca02.j3d").setIcon("chBoca01.jf1");	
		addFerr("chave_fenda","chavefenda01.j3d").setIcon("chFenda2.jf1");	
		
		{// producao de frascos: prensas de vidro, derretedor, goma, etc
			addCub("prensa_nor",49)
			.setDtLat("vator03.j3d",119-3)
			.setDtNor("vator05.j3d",J.cor[119],"frasco_carimbo01.j3d",J.cor[179])
			.setDtCma("vator03cma.j3d",119-3)
			.setDtBxo("vator03bxo.j3d",119-3)
			.setIcon("vator04.jf1",49)
			.setExtCom("e").setWav("rr");
			lastCub.setDtOes("vator04esq.j3d",119-3);
			lastCub.setDtLes("vator04dir.j3d",119-3);
			lastCub.operavel=true;// soh p garantir
			//lastCub.ehVator=true;					

			addCub("prensa_sul",49)
			.setDtLat("vator03.j3d",119-3)
			.setDtSul("vator05.j3d",J.cor[119],"frasco_carimbo01.j3d",J.cor[179])
			.setDtCma("vator03cma.j3d",119-3)
			.setDtBxo("vator03bxo.j3d",119-3)
			.setIcon("vator04.jf1",49)
			.setExtCom("e").setWav("rr");
			lastCub.setDtOes("vator04dir.j3d",119-3);
			lastCub.setDtLes("vator04esq.j3d",119-3);
			lastCub.operavel=true;// soh p garantir
			//lastCub.ehVator=true;								

			addCub("prensa_les",49)
			.setDtLat("vator03.j3d",119-3)
			.setDtLes("vator05.j3d",J.cor[119],"frasco_carimbo01.j3d",J.cor[179])
			.setDtCma("vator03cma.j3d",119-3)
			.setDtBxo("vator03bxo.j3d",119-3)
			.setIcon("vator04.jf1",49)
			.setExtCom("e").setWav("rr");
			lastCub.setDtNor("vator04esq.j3d",119-3);
			lastCub.setDtSul("vator04dir.j3d",119-3);
			lastCub.operavel=true;// soh p garantir
			//lastCub.ehVator=true;		

			addCub("prensa_oes",49)
			.setDtLat("vator03.j3d",119-3)
			.setDtOes("vator05.j3d",J.cor[119],"frasco_carimbo01.j3d",J.cor[179])
			.setDtCma("vator03cma.j3d",119-3)
			.setDtBxo("vator03bxo.j3d",119-3)
			.setIcon("vator04.jf1",49)
			.setExtCom("e").setWav("rr");
			lastCub.setDtNor("vator04dir.j3d",119-3);
			lastCub.setDtSul("vator04esq.j3d",119-3);
			lastCub.operavel=true;// soh p garantir
			//lastCub.ehVator=true;		
			
			addSprite("prensando_NS","prensa02.j3d",J.cor[119]).setIcon("cataven1.jf1",119).setWav("rr");
			lastCub.operavel = true;
			
			addSprite("prensando_LO","prensa02.j3d",J.cor[119]).setIcon("cataven1.jf1",119).setWav("rr");
			lastCub.dtCen.rodar90('y');
			lastCub.operavel = true;						

			addSprite("vidro_goma","vidroGoma01.j3d",J.cor[99],"4").setIcon("cataven1.jf1",119).setWav("rr");
			lastCub.operavel = true;						

			addCub("derretedor",119)  
				.setDtCma("ident02cma.j3d",159)
				.setDtLat("vator03.j3d",119-3,"chama_carimbo01.j3d",119-3)
				.setDtBxo("ident02bxo.j3d",159-3)
				.setIcon("ident01.jf1")
				.setExtCom("e").setWav("rr");
			lastCub.ocultador=false;
			lastCub.ehConEl = true;						
			lastCub.operavel=true;						

			addCub("ger_combustao",119)  
				.setDtCma("ident02cma.j3d",159)
				.setDtLat("vator03.j3d",119-3,"chama_carimbo01.j3d",119-3)
				.setDtBxo("ident02bxo.j3d",159-3)
				.setIcon("ident01.jf1")
				.setExtCom("e").setWav("rr");
			lastCub.ehConCano=true;
			lastCub.ocultador=false;
			lastCub.ehConEl = true;						
			lastCub.operavel=true;						

			
		}
		
		addCub("sonda_petro",179-6).setIcon("bloco25.jf1",179-4).setExtCom("e").setWav("rr");
		lastCub.setIncCal(-0.36f);
		lastCub.tb=1f;		
		lastCub.ocultador=false;		
		lastCub.operavel=true;		

		addTanque("petro",179,"l");
		
		
			addCano("cano_petro",179);
			
			addPao("maca_verde","maca04.j3d",J.cor[69],"4").setIcon("maca07.jf1",69);
			lastCub.dtCen.deslTudo(0,+0.25f,0);
			lastCub.operavel=true;			
			lastCub.ehComivel=true;			
			lastCub.opGetComClick=true;								
			
			addCub("moldadora",119).setDtLat("vator03.j3d",119-3, "brick_carimbo01.j3d", 119-3).setDtCma("elev01Cma.j3d",89).setDtBxo("vator03bxo.j3d",119-3).setIcon("elev01.jf1").setExtCom("e").setWav("rr");
			lastCub.ehConEl = true;						
			lastCub.operavel=true;		
			
			
		addCub("seed_nivelar",12).operavel=true;			
		
		addCub("seed_chuva",12).operavel=true; // inicia/para chuva

			addTrigo("cana_crescendo", "cana03.j3d", J.mixColor(149,69), "c4").setIcon("cana04.jf1",149);
			lastCub.dtCen.resize(1.6f);
			addTrigo("cana_madura", "cana03.j3d", 149,"4").setIcon("cana04.jf1",149).setExtCom("f");
			lastCub.dtCen.resize(1.6f);
			addFerr("talo_cana", "cana04.j3d", 149).setIcon("cana04.jf1",149);
			lastCub.ehComivel=true;							
			lastCub.ehSemente=true;							
			lastCub.opGetComClick=true;			

		{ 
			addTanque("garapa",149,"l"); //trocar este nome depois
		}
			addFerr("frasco_garapa","frasco04.j3d").setIcon("frasco03.jf1",119,118,149,147,145);
			lastCub.dtCen.tingir(89,addPal(119));		
			lastCub.dtCen.tingir(99,addPal(149));
			lastCub.ehBebivel=true;
			lastCub.opGetComClick=true;						
			lastCub.opQtdEhSaude = false;					
			
			addTanque("acucar",119,"");		

		addCub("maq_acucar",119).setDtLat("maquina06.j3d",49).setDtCma("vator03cma.j3d",119-3).setDtBxo("vator03bxo.j3d",119-3).setIcon("maq03.jf1",119,119-2,119-4,49,49-2).setExtCom("e").setWav("rr");
		lastCub.ehConEl = true;		
		lastCub.operavel=true;	

			addCano("cano_garapa",149);			
			
			addCano("cano_comb",169);
			addCano("cano_resina",49);
			addCano("cano_gas",J.mixColor(69,119),69);

			addCub("combustor",119) // nao seria melhor trocar esse nome, jah q esse vai funcionar apenas na torre de refinacao de petro
				.setDtCma("ident02cma.j3d",159)
				.setDtLat("vator03.j3d",119-3,"chama_carimbo01.j3d",179-3)
				.setDtBxo("ident02bxo.j3d",159-3)
				.setIcon("ident01.jf1")
				.setExtCom("e").setWav("rr");
			lastCub.ehConCano = true;							
			lastCub.temExtraByte=true;
			lastCub.ocultador=false;
			lastCub.operavel=true;						

			// componente da torre de refinaria de petroleo
			addCub("torre_ref",119).setDtLat("vator03.j3d",119-3).setDtCma("vator03cma.j3d",119-3).setDtBxo("vator03bxo.j3d",119-3).setIcon("maq05.jf1").setExtCom("e").setWav("rr");
			lastCub.operavel=true;
			
			//torre da refinaria de petroleo, entrada de petroleo
			addCub("torre_ref_p",119).setDtLat("vator03.j3d",119-3).setDtCma("vator03cma.j3d",119-3).setDtBxo("vator03bxo.j3d",119-3).setIcon("maq05.jf1").setExtCom("e").setWav("rr");
			lastCub.setDtNor("vator01.j3d",179);
			//lastCub.ehConCano = true;						
			lastCub.operavel=true;			

			//torre da refinaria de petroleo, saida de  c o m b u s t i v e l			
			addCub("torre_ref_c",119).setDtLat("vator03.j3d",119-3).setDtCma("vator03cma.j3d",119-3).setDtBxo("vator03bxo.j3d",119-3).setIcon("maq05.jf1").setExtCom("e").setWav("rr");
			lastCub.setDtNor("vator01.j3d",169);
			//lastCub.ehConCano = true;						
			lastCub.operavel=true;						

			
			//torre da refinaria de petroleo, saida de resina			
			addCub("torre_ref_r",119).setDtLat("vator03.j3d",119-3).setDtCma("vator03cma.j3d",119-3).setDtBxo("vator03bxo.j3d",119-3).setIcon("maq05.jf1").setExtCom("e").setWav("rr");
			lastCub.setDtNor("vator01.j3d",49);	
			//lastCub.ehConCano = true;						
			lastCub.operavel=true;						
			
			//torre da refinaria de petroleo, saida de gas
			addCub("torre_ref_g",119).setDtLat("vator03.j3d",119-3).setDtCma("vator03cma.j3d",119-3).setDtBxo("vator03bxo.j3d",119-3).setIcon("maq05.jf1").setExtCom("e").setWav("rr");
			lastCub.setDtNor("vator01.j3d",69);
			//lastCub.ehConCano = true;						
			lastCub.operavel=true;						
			
		addCub("seed_refinaria",12).operavel=true;

		addTanque("resina",49,"");
		addTanque("comb",169,"l");
		addTanque("gas",219,"g");

		addCub("transp_in",119).setDtLat("vator03.j3d",119-3, "seta_cma_carimbo01.j3d", 119-3).setDtCma("elev01Cma.j3d",89).setDtBxo("vator03bxo.j3d",119-3).setIcon("elev01.jf1").setExtCom("e").setWav("rr");
		lastCub.ehConEl = true; // puxa o cubo plotado p dentro do transp vazio acima				
		lastCub.operavel=true;

		addCub("transp_out",119).setDtLat("vator03.j3d",119-3, "seta_bxo_carimbo01.j3d", 119-3).setDtCma("elev01Cma.j3d",89).setDtBxo("vator03bxo.j3d",119-3).setIcon("elev01.jf1").setExtCom("e").setWav("rr");
		lastCub.ehConEl = true; // plota o cubo do transp p mapa
		lastCub.operavel=true;

		addSprite("osso","osso03.j3d",J.cor[119]).setIcon("osso01.jf1",119);
		lastCub.opGetComClick=true;								
		
		addCub("vago6000",99);

		addCub("caixote_frasco",139).setDtLat("caixote01.j3d",139-1, "frasco_carimbo01.j3d", 139-1).setDtCma("vator03cma.j3d",139-1).setDtBxo("vator03bxo.j3d",139-1).setIcon("bloco27.jf1").setExtCom("0").setWav("mm");
		lastCub.dtNorr.resize(0.5f, 0.5f, 1f);
		lastCub.dtSull.resize(0.5f, 0.5f, 1f);
		lastCub.dtLess.resize(1f, 0.5f, 0.5f);
		lastCub.dtOess.resize(1f, 0.5f, 0.5f);
		lastCub.dtNorr.deslTudo(0,0.25f,0);
		lastCub.dtSull.deslTudo(0,0.25f,0);
		lastCub.dtLess.deslTudo(0,0.25f,0);
		lastCub.dtOess.deslTudo(0,0.25f,0);
		
		addCub("caixote_suco_maca",139).setDtLat("caixote01.j3d",139-1, "frasco_carimbo01.j3d", 59).setDtCma("vator03cma.j3d",139-1).setDtBxo("vator03bxo.j3d",139-1).setIcon("bloco27.jf1").setExtCom("0").setWav("mm");
		lastCub.dtNorr.resize(0.5f, 0.5f, 1f);
		lastCub.dtSull.resize(0.5f, 0.5f, 1f);
		lastCub.dtLess.resize(1f, 0.5f, 0.5f);
		lastCub.dtOess.resize(1f, 0.5f, 0.5f);
		lastCub.dtNorr.deslTudo(0,0.25f,0);
		lastCub.dtSull.deslTudo(0,0.25f,0);
		lastCub.dtLess.deslTudo(0,0.25f,0);
		lastCub.dtOess.deslTudo(0,0.25f,0);		

		addCub("encaixotadora",119)  // soh copiei e mudei o nome do indentificador, depois eu arrumo
			.setDtCma("ident02cma.j3d",159)
			.setDtLat("latEncaix01.j3d",119-3)
			.setDtBxo("ident02bxo.j3d",159-3)
			.setIcon("ident01.jf1")
			.setExtCom("e").setWav("rr");
		lastCub.tb=1.5f;
		lastCub.ocultador=false; 
		lastCub.ehConEl = true;						
		lastCub.operavel=true;						

		addCub("seed_portal",12).operavel=true;		
		
		addTanque("farinha",119,"");		
		addTanque("leite",119,"l");		

		addFerr("sem_morango","semente02.j3d",99).setIcon("saco05.jf1",99).setWav("f ").setPotCalor(1).ehEmbrulho=true; // ajudou no shape da mao
		lastCub.ehSemente=true;							
		lastCub.opGetComClick=true;		
		lastCub.temExtraByte=true;					
		addTrigo("morangueiro_crescendo", "morangueiro01.j3d", null, "c4").setIcon("muda01.jf1");
		lastCub.dtCen.resize(2f);		
		addTrigo("morangueiro_maduro", "morangueiro01b.j3d", null,"4").setIcon("muda01.jf1");
		lastCub.dtCen.resize(2f);		
		addFerr("morango","morango01.j3d",null).setIcon("morang01.jf1");
		lastCub.opGetComClick=true;		
		lastCub.ehComivel=true;			


		addCub("forno_industrial",119)  // soh copiei e mudei o nome do indentificador, depois eu arrumo
			.setDtCma("ident02cma.j3d",159)
			//.setDtLat("latEncaix01.j3d",119-3)
			.setDtLat("latEncaix01.j3d",J.mixColor(119,99))
			.setDtBxo("ident02bxo.j3d",159-3)
			.setIcon("ident01.jf1")
			.setExtCom("e").setWav("rr");
		lastCub.tb=1.5f;
		lastCub.ocultador=false; 
		lastCub.ehConEl = true;						
		lastCub.operavel=true;						

		addPao("fatia_coco","fatia04.j3d",null,"").setIcon("fatia06.jf1"); // dah p melhorar este icone.
		lastCub.opGetComClick=true;		
		lastCub.ehComivel=true;

		addFerr("flecha","flecha01.j3d").setIcon("flecha02.jf1").setPotCalor(1).opAutoGet=true;
		// abaixo sao as fincadas, jah atiradas
		addSprite("flecha_nor","flecha01_nor.j3d",J.cor[139]).setIcon("flecha02.jf1").setPotCalor(1).opAutoGet=true;
		lastCub.opGetComClick=true;				
		addSprite("flecha_sul","flecha01_sul.j3d",J.cor[139]).setIcon("flecha02.jf1").setPotCalor(1).opAutoGet=true;
		lastCub.opGetComClick=true;				
		addSprite("flecha_les","flecha01_les.j3d",J.cor[139]).setIcon("flecha02.jf1").setPotCalor(1).opAutoGet=true;
		lastCub.opGetComClick=true;				
		addSprite("flecha_oes","flecha01_oes.j3d",J.cor[139]).setIcon("flecha02.jf1").setPotCalor(1).opAutoGet=true;
		lastCub.opGetComClick=true;				
		addSprite("flecha_cma","flecha01_cma.j3d",J.cor[139]).setIcon("flecha02.jf1").setPotCalor(1).opAutoGet=true;
		lastCub.opGetComClick=true;				
		addSprite("flecha_bxo","flecha01_bxo.j3d",J.cor[139]).setIcon("flecha02.jf1").setPotCalor(1).opAutoGet=true;
		lastCub.opGetComClick=true;				
		
				addSprite("splash","splash01.j3d",J.cor[119],"4");
				lastCub.operavel=true;
				lastCub.dyy = -lastCub.tp*3f;			
				lastCub.dtCen.resize(6f);				
				
				addSprite("splash2","splash01.j3d",J.cor[119],"4");
				lastCub.operavel=true;
				lastCub.dyy = -lastCub.tp*3f;			
				lastCub.dtCen.resize(3.5f);				

				addSprite("splash3","splash01.j3d",J.cor[119],"4");
				lastCub.operavel=true;
				lastCub.dyy = -lastCub.tp*3f;			
				lastCub.dtCen.resize(1f);								
				
		tPlotar = tCub.size();
		addCub("plotar",new Color(255,255,255,64));
		lastCub.setName("plotar");
		lastCub.selecionavel=false; 
		lastCub.ocultador=false; 
		//lastCub.ehConEl = true; // acho q eh bom deixar isso... serah???
		//lastCub.ehConCano = true;
		lastCub.ehGhostBlock = true;
		lastCub.opFazSombra = false;
		lastCub.operavel=true;					

			addCub("degrau_rocha",49)
				.setDtLat("obsidian01b.j3d",49-2)
				.setDtCma("areia01cmab.j3d",49-2)
				.setIcon("bloco15.jf1",49)
				.setExtCom("p")
				.setWav("rr");
			lastCub.opAcidoCorroi=false;							
			lastCub.tc=0;
			lastCub.ehDegrau=true;						
			lastCub.ocultador=false;			
			
			addFerr("colher_pedreiro","colher_pedreiro01.j3d").setIcon("colher01.jf1").setPotCalor(1);
			addFerr("cinzel","cinzel01.j3d").setIcon("cinzel01.jf1").setPotCalor(1);
		
			addSprite("pilastra_rocha","pilastra01.j3d",J.cor[119],"4").setIcon("bloco31.jf1",49).setExtCom("p");
			addSprite("pilastra_madeira","pilastra01.j3d",J.cor[139],"4").setIcon("bloco31.jf1",139).setPotCalor(3).setExtCom("m"); // como madeira
			addSprite("pilastra_marmore","pilastra01.j3d",J.cor[119],"4").setIcon("bloco31.jf1",119).setExtCom("p");
			addSprite("pilastra_granito","pilastra01.j3d",J.cor[179],"4").setIcon("bloco31.jf1",179).setExtCom("p");
			addSprite("pilastra_obsidian","pilastra01.j3d",J.cor[169],"4").setIcon("bloco31.jf1",169).setExtCom("p");
			
			
			addCub("granito",179).setDtLat("marmore01.j3d",179-1).setDtCma("marmore01cma.j3d",179-1).setDtBxo("marmore01bxo.j3d",179-1).setIcon("bloco18.jf1",179).setExtCom("p").setWav("rr");
			lastCub.opAcidoCorroi=false;						
			
			addCub("degrau_granito",179).setDtLat("marmore01b.j3d",179-1).setDtCma("marmore01cmab.j3d",179-1).setDtBxo("marmore01bxo.j3d",179-1).setIcon("bloco15.jf1",179).setExtCom("p").setWav("rr");		
			lastCub.opAcidoCorroi=false;						
			lastCub.ehDegrau=true;						
			lastCub.tc=0;
			lastCub.ocultador=false;	

			addCub("coluna_granito",16).setDtLat("coluna01.j3d",179-1).setIcon("bloco19.jf1",179).setExtCom("p").setWav("rr");
			lastCub.opAcidoCorroi=false;						
			lastCub.operavel=true;					

			addCub("tijolo_granito",16).setDtLat("tijolo01.j3d",178).setDtCma("madeira01cma.j3d",178).setDtBxo("madeira01bxo.j3d",178).setIcon("bloco12.jf1",178).setExtCom("p").setWav("rr");
			lastCub.opAcidoCorroi=false;						
			lastCub.ehTijolo = true;
			addCub("degrau_tijolo_granito",16).setDtLat("tijolo01b.j3d",178).setDtCma("madeira01cma.j3d",178).setDtBxo("madeira01bxo.j3d",178).setIcon("bloco12b.jf1",178).setExtCom("p").setWav("rr").ic=-0.5f; // incremento de cima
			lastCub.ehDegrau=true;						
			lastCub.opAcidoCorroi=false;						
			lastCub.ehTijolo = true;
			lastCub.ocultador = false;

			addFerr("balde_vazio","balde02v.j3d").setIcon("balde04.jf1",116,118,117,116,115);
			//lastCub.dtCen.tingir(89,addPal(119));
			//lastCub.dtCen.tingir(99,addPal(119));
			//lastCub.dtCen.crAnt=15;
			//lastCub.dtCen.crNov=0;
			//lastCub.ajPals();	// autoAjuste de paletas						
			//lastCub.dtCen.trocaCor(15,0);			
			//lastCub.dtCen.hideTriCr(15);

			lastCub.dtCen.giroX(null, null, -0.3f, 1); // anulando o tombo
			lastCub.dtCen.reg3d();						
			lastCub.opQtdEhSaude = false;
			lastCub.opGetComClick=true;						
			
			addFerr("balde_agua","balde02.j3d").setIcon("balde04.jf1",189,118,117,116,115);
			//lastCub.dtCen.tingir(89,addPal(119));		
			//lastCub.dtCen.tingir(99,addPal(9));

			lastCub.dtCen.tingir(15,addPal(9));
			lastCub.dtCen.giroX(null, null, -0.3f, 1); // anulando o tombo			
			lastCub.dtCen.reg3d();			
			//lastCub.dtCen.trocaCor(15,189);
			//lastCub.dtCen.crAnt=15;
			//lastCub.dtCen.crNov=189;			
			//lastCub.ajPals();	// autoAjuste de paletas			
			lastCub.ehBebivel=true;
			lastCub.opQtdEhSaude = false;		
			lastCub.opGetComClick=true;


			addCub("ferro_velho",179).setDtLat("obsidian01.j3d",109-2).setDtCma("obsidian01cma.j3d",109-2).setDtBxo("obsidian01bxo.j3d",109-2).setIcon("bloco09.jf1",179).setExtCom("p").setWav("rr").setCozido("barra_ferro");
			lastCub.operavel=true; // p crescer alga sobre, p degrau eu faco uma gambiarra			
			lastCub.opMultBife=4; // um degrau faz 4 barras			

			addCub("degrau_ferro_velho",179)
				.setDtLat("obsidian01b.j3d",109-2)
				.setDtCma("areia01cmab.j3d",109-2)
				.setIcon("bloco15.jf1",179)
				.setCozido("barra_ferro")
				.setExtCom("p")
				.setWav("rr");
			lastCub.opMultBife=2; // um degrau faz 2 barras
			lastCub.ehDegrau=true;						
			lastCub.tc=0;
			lastCub.ocultador=false;			

			addSprite("cranio","cranio02.j3d",null,"").setIcon("cranio04.jf1");
			lastCub.opGetComClick=true;									

			addSprite("cabeca_zumbi","cabecazumbi01.j3d",null,"").setIcon("cabecz01.jf1");
			lastCub.operavel=true;										
			lastCub.opGetComClick=true;										

			addSprite("braco_zumbi","bracozumbi01.j3d",null,"").setIcon("bloco33.jf1",149,147,144,9,169);
			lastCub.operavel=true;			
			lastCub.opGetComClick=true;										
			
			addSprite("muda_silvestre","muda02.j3d",J.cor[69]);
			lastCub.operavel=true;
			lastCub.setIcon("muda02",69,68,67,66,65).setExtCom("0");
			tPlant.add(lastCub); // p animacao da muda

			addTrigo("alga", "alga02.j3d", J.cor[69],"4").setIcon("alga01.jf1",69);
			lastCub.setExtCom("f0").ehComivel=true;
			lastCub.dtCen.resize(2.33f);		
			lastCub.ehGhostBlock=true;
			lastCub.opCresce100=true;// saiu o bug???
			lastCub.temExtraByte=true; // saiu o bug???
			lastCub.opAniAlga=true; // depois eu arrumo isso.
			tPlant.add(lastCub); // p animacao
			
			addCub("degrau_terra",129).setDtLat("obsidian01b.j3d",129-2).setIcon("bloco15.jf1",129).setExtCom("a").setWav("aa").tc=0f;
			lastCub.ehDegrau=true;						
			lastCub.ocultador=false;				

			{ // vidracas
				// este cubo abaixo se auto-corrige p os cubos seguintes
				// repare o "e" no final do nome, refere-se a "estensao": o vidro se alonga abaixo p fechar sobre qq degrau abaixo.
				// veja q soh este cubo tem a tag "o p e r a v e l"
				addCub("VIDRACA",J.altAlfa(49,0.5f)).setIcon("bloco14b.jf1",119).cct=-3;			
				lastCub.operavel = true;
				lastCub.ocultador = false;			
				
				addCub("VIDRACA_NS",J.altAlfa(49,0.5f)).setIcon("bloco14b.jf1",119).cct=-3;
				lastCub.tn = 0.1f;
				lastCub.ts = 0.1f;
				lastCub.crLes = null;
				lastCub.crOes = null;
				lastCub.crCma = null;
				lastCub.crBxo = null;	
				lastCub.ocultador = false;
				
				addCub("VIDRACA_LO",J.altAlfa(49,0.5f)).setIcon("bloco14b.jf1",119).cct=-3;
				lastCub.tl = 0.1f;
				lastCub.to = 0.1f;			
				lastCub.crSul = null;
				lastCub.crNor = null;
				lastCub.crCma = null;
				lastCub.crBxo = null;	
				lastCub.ocultador = false;			

				addCub("VIDRACA_NSe",J.altAlfa(49,0.5f)).setIcon("bloco14b.jf1",119).cct=-3;
				lastCub.tb = 1f;			
				lastCub.tn = 0.1f;
				lastCub.ts = 0.1f;
				lastCub.crLes = null;
				lastCub.crOes = null;
				lastCub.crCma = null;
				lastCub.crBxo = null;	
				lastCub.ocultador = false;
				
				addCub("VIDRACA_LOe",J.altAlfa(49,0.5f)).setIcon("bloco14b.jf1",119).cct=-3;
				lastCub.tl = 0.1f;
				lastCub.to = 0.1f;			
				lastCub.tb = 1f;
				lastCub.crSul = null;
				lastCub.crNor = null;
				lastCub.crCma = null;
				lastCub.crBxo = null;	
				lastCub.ocultador = false;			
			}


				addCub("folha_sequoia",66).setDtLat("obsidian01.j3d",69-2).setDtCma("obsidian01cma.j3d",69-2).setDtBxo("obsidian01bxo.j3d",69-2).setIcon("bloco09.jf1",69).setExtCom("f").setWav("ff").setPotCalor(1);
				lastCub.operavel=true; // tem q sumir se nao tiver caule por perto. Fazer depois
				lastCub.ehFolha=true;		
				lastCub.opBounceFaces = true;					

				addCub("caule_sequoia",59-8).setCrCmaBxo(139).setDtCma("caule02.j3d",139-2).setDtBxo("caule02bxo.j3d",139-2).setDtLat("caule04.j3d",59-7).setIcon("bloco06.jf1",139,59,59-2,139-4,12).setExtCom("m").setWav("mm").setPotCalor(4);
				lastCub.setIncCal(0.2f); // inflar() pega acima e abaixo tb. Nao aqui.
				lastCub.ehCaule = true;								

			{ // seringueira, borracha e relacionados
				addCub("caule_seringueira",J.mixColor(59,69)).setCrCmaBxo(J.mixColor(139,59)).setDtCma("caule02.j3d",139).setDtBxo("caule02bxo.j3d",139).setDtLat("caule04.j3d",J.mixColor(59,67)).setIcon("bloco06.jf1",289, 129,129-4,289-4,12).setExtCom("m").setWav("mm").setPotCalor(4);
				lastCub.ehCaule = true;			
				addCub("caule_seringueira_cheia",J.mixColor(59,69)).setCrCmaBxo(J.mixColor(139,59)).setDtCma("caule02.j3d",139).setDtBxo("caule02bxo.j3d",139).setDtLat("caule04.j3d",J.mixColor(59,67)).setIcon("bloco06.jf1",289, 129,129-4,289-4,12).setExtCom("m").setWav("mm").setPotCalor(4).setDtNor("seringueira01.j3d",119);
				lastCub.operavel=true;				
				lastCub.ehCaule = true;				
				addCub("caule_seringueira_vazia",J.mixColor(59,69)).setCrCmaBxo(J.mixColor(139,59)).setDtCma("caule02.j3d",139).setDtBxo("caule02bxo.j3d",139).setDtLat("caule04.j3d",J.mixColor(59,67)).setIcon("bloco06.jf1",289, 129,129-4,289-4,12).setExtCom("m").setWav("mm").setPotCalor(4).setDtNor("seringueira01.j3d",109);
				lastCub.operavel=true;				
				lastCub.operavel = true;
				lastCub.ehCaule = true;							
				

				addCub("folha_seringueira",J.mixColor(49,69)).setDtLat("obsidian01.j3d",69-2).setDtCma("obsidian01cma.j3d",69-2).setDtBxo("obsidian01bxo.j3d",69-2).setIcon("bloco09.jf1",69).setExtCom("f").setWav("ff").setPotCalor(1);
				lastCub.operavel=true; // deveria sumir se nao tivesse caule por perto
				lastCub.ehFolha=true;
				lastCub.opBounceFaces = true;					
				
				addSprite("muda_seringueira","muda02.j3d",J.mixColor(49,69));
				lastCub.operavel=true;
				lastCub.setIcon("muda02",49).setExtCom("0");
				lastCub.opGetComClick=true;													
				//lastCub.setIcon("muda02",69,68,67,66,65).setExtCom("0");
				tPlant.add(lastCub); // p animacao da muda
				
					addCub("latex",119).setIcon("bloco32.jf1",119,117,115,113,12).setExtCom("0").setWav("ll");
					lastCub.dy=-0.3f; // sobre o solo
					lastCub.setCozido("borracha");
					lastCub.opGetComClick=true;														
					lastCub.ocultador=false;
					lastCub.opFazSombra=false;
					lastCub.inflar(-0.3f);	

					addCub("borracha",179).setIcon("bloco32.jf1",179,177,175,173,12).setExtCom("0").setWav("ff");
					lastCub.dy=-0.3f; // sobre o solo
					lastCub.ocultador=false;
					lastCub.opGetComClick=true;														
					lastCub.opFazSombra=false;
					lastCub.inflar(-0.3f);					
					
					addCub("caneca_latex_vazia",49).setExtCom("0").setWav("mm");
					lastCub.setIcon("bloco32.jf1",179,47,45,44,12);					
					lastCub.dy=-0.3f; // sobre o solo
					lastCub.dz=-0.3f; // ao sul					
					lastCub.opGetComClick=true;														
					lastCub.setCrCma(179); // depois eu arrumo essa cor (serah?)
					lastCub.ocultador=false;
					lastCub.opFazSombra=false;
					lastCub.inflar(-0.3f);										
					
					addCub("caneca_latex_cheia",49).setExtCom("0").setWav("mm");
					lastCub.setIcon("bloco32.jf1",119,47,45,44,12);
					lastCub.dy=-0.3f; // sobre o solo
					lastCub.dz=-0.3f; // ao sul
					lastCub.setCozido("caneca_e_borracha");
					lastCub.setCrCma(119);					
					lastCub.setDtLat("canecaLatex01.j3d",15);
					lastCub.opGetComClick=true;														
					lastCub.ocultador=false;
					lastCub.opFazSombra=false;
					lastCub.inflar(-0.3f);															
					
					addCub("caneca_e_borracha",49).setExtCom("0").setWav("mm");
					lastCub.setIcon("bloco32.jf1",16,47,45,44,12);
					lastCub.dy=-0.3f; // sobre o solo
					//lastCub.dz=-0.3f; // ao sul
					lastCub.setCrCma(16);
					lastCub.setDtLat("canecaLatex01.j3d",16);
					lastCub.opGetComClick=true;														
					lastCub.ocultador=false;
					lastCub.opFazSombra=false;
					lastCub.inflar(-0.3f);															
			}



			

			
			addCub("seed_circ",12).operavel=true;					
			addCub("seed_smooth",12).operavel=true;					
			addCub("seed_preencher",12).operavel=true;					
			addCub("preenchido",4).operavel=true;					

			addCub("lampada_on",15).operavel=true;
			lastCub.ehConEl = true;									
			lastCub.setIcon("bloco23.jf1", 119);
			lastCub.ocultador=false;
			lastCub.inflar(-0.1f);				
			
			addCub("lampada_off",8);
			lastCub.ehConEl = true;									
			lastCub.setIcon("bloco23.jf1", 49);
			lastCub.ocultador=false;
			lastCub.inflar(-0.1f);				
			

		{ // flamejantes

			addSprite("fogo","fogo05.j3d",null,"4").setIcon("fogo06.jf1").setPotCalor(1000);		
			lastCub.opForceImpDt=true; // imprime o dt mesmo q de longe			
			lastCub.opTemFogoQueAlastra=true;			
			lastCub.operavel = true; // ?iluminacao???
			lastCub.tc=-0.5f; // eh assim q se imprime dtNor sem as faces chapadas laterais, mas o certo eh ver o esquema da  b i g o r n a aqui
			lastCub.ocultador = false;
			lastCub.crCma=null; // isso eh bom
			lastCub.crBxo=null;
			lastCub.sombreavel = false; // qual eh qual agora???
			lastCub.opFazSombra = false; // ???

			// este p eh posImp
			Cub cb = addCub("chama",159).setDtLat("chama02.j3d",null).setIcon("fogo06.jf1").setWav("ff").setPotCalor(1000);		
			lastCub.opForceImpDt=true; // imprime o dt mesmo q de longe			
			lastCub.opTemFogoQueAlastra=true;						
			lastCub.selecionavel = false; 
			lastCub.operavel = true; // ?iluminacao???
			lastCub.tc=-0.5f; // eh assim q se imprime dtNor sem as faces chapadas laterais:
			lastCub.ocultador = false;
			lastCub.crCma=null; // isso eh bom
			lastCub.crBxo=null;
			lastCub.sombreavel = false; // qual eh qual agora??? 
			lastCub.opFazSombra = false; //R: sombreavel afeta o shape apenas, opFazSombra "desce" a sombra p blocos abaixo.

			
			addCub("caule_fogo",59-8).setCrCmaBxo(139).setDtCma("caule02.j3d",139-2).setDtBxo("caule02bxo.j3d",139-2).setDtLat("caule04.j3d",59-7).setIcon("bloco06.jf1",139,59,59-2,139-4,12).setExtCom("m").setWav("mm").setPotCalor(4);
			lastCub.opForceImpDt=true; // imprime o dt mesmo q de longe			
			lastCub.opTemFogoQueAlastra=true;
			lastCub.operavel = true; // ?iluminacao???			
			lastCub.ehCaule = true;
			lastCub.cubCen = cb;
			// madeira, folhas + etc aqui depois (cacto tb???)
			// chamuscado p grama, carvao, cinzas (q jah tem)

			addCub("degrau_caule_fogo",59-8).setCrCmaBxo(139).setIcon("bloco06c.jf1",139,59,59-2,139-4,12).setExtCom("m").setWav("mm").setPotCalor(4);
			lastCub.opForceImpDt=true; // imprime o dt mesmo q de longe			
			lastCub.ehDegrau=true;						
			lastCub.opTemFogoQueAlastra=true;			
			lastCub.operavel = true; // ?iluminacao???
			lastCub.tc=0f;
			lastCub.ocultador=false;
			lastCub.ehCaule = true;
			lastCub.cubCen = cb;			
			
			addCub("madeira_fogo",139).setDtLat("madeira02.j3d",139-1).setDtBxo("madeira01bxo.j3d",139-1).setDtCma("madeira01cma.j3d",139-1).setIcon("bloco11.jf1",139).setExtCom("m").setWav("mm").setPotCalor(3);
			lastCub.opForceImpDt=true; // imprime o dt mesmo q de longe			
			lastCub.opTemFogoQueAlastra=true;			
			lastCub.operavel = true; // ?iluminacao???			
			lastCub.ehMadeira=true;
			lastCub.cubCen = cb;			
			
			addCub("degrau_madeira_fogo",139).setDtLat("madeira02.j3d",139-1).setDtBxo("madeira01bxo.j3d",139-1).setDtCma("madeira01cmab.j3d",139-1).setIcon("bloco12b.jf1",139).setExtCom("m").setWav("mm").setPotCalor(3).tc=0f;
			lastCub.opForceImpDt=true; // imprime o dt mesmo q de longe			
			lastCub.ehDegrau=true;						
			lastCub.opTemFogoQueAlastra=true;			
			lastCub.ehMadeira=true;
			lastCub.operavel = true; // ?iluminacao???			
			lastCub.tc=0f;
			lastCub.ocultador=false;
			lastCub.ehCaule = true;
			lastCub.cubCen = cb;			


			addCub("folha_fogo",179).setDtLat("obsidian01.j3d",179-2).setDtCma("obsidian01cma.j3d",179-2).setDtBxo("obsidian01bxo.j3d",179-2).setIcon("bloco09.jf1",179).setExtCom("f").setWav("ff").setPotCalor(1).ehFolha=true; 
			lastCub.opForceImpDt=true; // imprime o dt mesmo q de longe			
			lastCub.opBounceFaces = true;
			lastCub.opTemFogoQueAlastra=true;			
			lastCub.operavel = true; // ?iluminacao???			
			lastCub.cubCen = cb;			
			
			addCub("chamuscado_fogo",179).setDtLat("obsidian01.j3d",179-2).setDtCma("obsidian01cma.j3d",179-2).setDtBxo("obsidian01bxo.j3d",179-2).setIcon("bloco09.jf1",179).setExtCom("pm").setWav("rr"); // p caules e madeira. Cacto tb.
			lastCub.opForceImpDt=true; // imprime o dt mesmo q de longe
			lastCub.opTemFogoQueAlastra=true;
			lastCub.operavel = true; // ?iluminacao???			
			lastCub.cubCen = cb;			

		addCub("degrau_chamuscado",179).setDtLat("obsidian01b.j3d",179-2).setDtCma("areia01cmab.j3d",179-2).setIcon("bloco15.jf1",179).setExtCom("pm").setWav("rr");
		lastCub.ocultador=false;
		lastCub.ehDegrau=true;					
		lastCub.tc=0f;

			addCub("degrau_chamuscado_fogo",179).setDtLat("obsidian01b.j3d",179-2).setDtCma("areia01cmab.j3d",179-2).setIcon("bloco15.jf1",179).setExtCom("pm").setWav("rr");
			lastCub.ocultador=false;
			lastCub.ehDegrau=true;			
			lastCub.tc=0f;
			lastCub.opForceImpDt=true; // imprime o dt mesmo q de longe
			lastCub.opTemFogoQueAlastra=true;
			lastCub.operavel = true; // ?iluminacao???			
			lastCub.cubCen = cb;						
			
			addCub("chamuscado",179).setDtLat("obsidian01.j3d",179-2).setDtCma("obsidian01cma.j3d",179-2).setDtBxo("obsidian01bxo.j3d",179-2).setIcon("bloco09.jf1",179).setExtCom("pm").setWav("rr"); // este sem fogo

			addCub("cacto_fogo",69).setDtLat("cacto01.j3d",179).setDtCma("cacto01cma.j3d",179).setCrBxo(139).setDtBxo("caule02bxo.j3d",139-2).setIcon("cacto04.jf1").setExtCom("m").setWav("mm").setPotCalor(2).ehCaule=true;
			lastCub.cubCen = cb;						
			lastCub.opForceImpDt=true; // imprime o dt mesmo q de longe
			lastCub.opTemFogoQueAlastra=true;
			lastCub.operavel = true; // ?iluminacao???			

			addCub("caule_coq_fogo",149-8).setCrCmaBxo(139).setIcon("bloco06.jf1",139,179,179-2,139-4,12).setExtCom("m").setWav("mm").setPotCalor(3);
			lastCub.ocultador=false;
			lastCub.setIncCal(-0.36f);
			lastCub.ipLO = lastCub.ipNS = 0.1f;
			lastCub.ehCaule = true;
			lastCub.opForceImpDt=true; // imprime o dt mesmo q de longe			
			lastCub.cubCen = cb;									
			lastCub.opTemFogoQueAlastra=true;			
			lastCub.operavel = true; // ?iluminacao???						
			
			addCub("caule_sequoia_fogo",59-8).setCrCmaBxo(139).setDtCma("caule02.j3d",139-2).setDtBxo("caule02bxo.j3d",139-2).setDtLat("caule04.j3d",59-7).setIcon("bloco06.jf1",139,59,59-2,139-4,12).setExtCom("m").setWav("mm").setPotCalor(4);
			lastCub.setIncCal(0.2f); // inflar() pega acima e abaixo tb. Nao aqui.
			lastCub.ehCaule = true;								
			lastCub.opForceImpDt=true; // imprime o dt mesmo q de longe			
			lastCub.cubCen = cb;									
			lastCub.opTemFogoQueAlastra=true;			
			lastCub.operavel = true; // ?iluminacao???						
			
			addCub("chamuscado_fino_fogo",179).setDtLat("obsidian01.j3d",179-2).setDtCma("obsidian01cma.j3d",179-2).setDtBxo("obsidian01bxo.j3d",179-2).setIcon("bloco09.jf1",179).setExtCom("pm").setWav("rr"); // p caules e madeira. Cacto tb.
			lastCub.setIncCal(-0.36f);
			lastCub.ipLO = lastCub.ipNS = 0.1f;
			lastCub.opForceImpDt=true; // imprime o dt mesmo q de longe
			lastCub.opTemFogoQueAlastra=true;
			lastCub.operavel = true; // ?iluminacao???			
			lastCub.cubCen = cb;			

			addCub("chamuscado_fino",179).setDtLat("obsidian01.j3d",179-2).setDtCma("obsidian01cma.j3d",179-2).setDtBxo("obsidian01bxo.j3d",179-2).setIcon("bloco09.jf1",179).setExtCom("pm").setWav("rr"); // p caules e madeira. Cacto tb.
			lastCub.setIncCal(-0.36f);
			lastCub.ipLO = lastCub.ipNS = 0.1f;
			lastCub.opForceImpDt=true; // imprime o dt mesmo q de longe
			//lastCub.opTemFogoQueAlastra=true;
			//lastCub.operavel = true; // ?iluminacao???			
			//lastCub.cubCen = cb;			


			// cinzas jah tem, e degrau dela tb
			// fumaca???

			
		}	
		

			

		{ // mais como fornalhas
			addCub("defumador",109).setDtLat("fornalha03.j3d",16).setDtCma("obsidian01cma.j3d",109-2).setDtBxo("obsidian01bxo.j3d",109-3).setIcon("forn04.jf1").setExtCom("p").setWav("rr").setIncCal(-0.01f);
			lastCub.temExtraByte=true;		
			lastCub.operavel=true; // soh p garantir		
			lastCub.ocultador = false;		
			lastCub.opVatorPodeMover=false;		
			lastCub.opJoePodeMover=false;				
			
			addCub("alto_forno",49).setDtLat("fornalha03.j3d",16).setDtCma("obsidian01cma.j3d",49-2).setDtBxo("obsidian01bxo.j3d",49-3).setIcon("forn04.jf1").setExtCom("p").setWav("rr").setIncCal(-0.01f);
			lastCub.temExtraByte=true;		
			lastCub.operavel=true; // soh p garantir		
			lastCub.ocultador = false;		
			lastCub.opVatorPodeMover=false;		
			lastCub.opJoePodeMover=false;							
		}

			//addSprite("fumaca_branca","triangulo01.j3d",J.cor[10]);
			//lastCub.dtCen.tri.get(0).fig = J.carrFig("boltball01.png");
			//lastCub.dtCen.tri.get(0).tamEsf=10f;
			
		addFerr("farinha","semente02.j3d",119).setIcon("saco05.jf1",119).setWav("f ").setPotCalor(1).ehEmbrulho=true;
		lastCub.temExtraByte=true;					
		lastCub.opGetComClick=true;
		
			addCub("vago199",99);
			addCub("vago200",99);
			addCub("vago201",99);
			
			addBigorna("bigorna","bigorna01.j3d",179,"").setIcon("bigorn03.jf1").setExtCom("p").setWav("rr");
			
			addCub("seed_seringueira",99).operavel=true;
		
		{	// mais liquidos
			addAgua("acido",219).setIcon("bloco09.jf1",219).setWav("ll").setOption("L"); //.setCozido("nuvem");
			lastCub.opAcidoCorroi=false;									
			lastCub.operavel=true;
			addAgua("acido_corr",219).setIcon("bloco09.jf1",219).setWav("ll"); //.setCozido("nuvem");
			lastCub.opAcidoCorroi=false;									
			lastCub.operavel=true;

			addAgua("petroleo",16).setIcon("bloco09.jf1",179).setWav("ll").setOption("L"); //.setCozido("nuvem");
			addAgua("petroleo_corr",8).setIcon("bloco09.jf1",179).setWav("ll"); //.setCozido("nuvem");
			lastCub.operavel=true;			
		}

		//Mais cores depois. Azul seria legal p fazer vilas. Cinza poderia ser ladeira de pedra. Ia ficar bom. Branco e preto tb???
		addCub("TELHADO",109-1).setIcon("bloco09.jf1",109).setExtCom("m").setWav("mm");		
		

		
		addCub("maq_borracha",119).setDtLat("maquina06.j3d",179).setDtCma("vator03cma.j3d",119-3).setDtBxo("vator03bxo.j3d",119-3).setIcon("maq03.jf1",119,119-2,119-4,179,179-2).setExtCom("e").setWav("rr");
		lastCub.opEhMaqInt=lastCub.temExtraByte=true; 
		lastCub.ehConEl = true;		
		lastCub.operavel=true;	
		
		addCub("maq_fio",119).setDtLat("maquina06.j3d",59).setDtCma("vator03cma.j3d",119-3).setIcon("maq03.jf1",119,119-2,119-4,59,57).setExtCom("e").setWav("rr");
		lastCub.opEhMaqInt=lastCub.temExtraByte=true; 
		lastCub.ehConEl = true;		
		lastCub.operavel=true;			

		// maq ductisadora, transforma poh de minerio em fio desencapado (cobre por enquanto)
		addCub("maq_duct",119).setDtLat("maquina06.j3d",99).setDtCma("vator03cma.j3d",119-3).setIcon("maq03.jf1",119,119-2,119-4,99,97).setExtCom("e").setWav("rr");
		lastCub.opEhMaqInt=lastCub.temExtraByte=true; 
		lastCub.ehConEl = true;
		lastCub.operavel=true;

		addCub("rede0",179).setIcon("bloco33.jf1",179,177,174,10,59).setExtCom("e").setWav("rr");
		lastCub.operavel=true;					
		lastCub.dy=-0.3f; // sobre o solo		
		//lastCub.opLuzLife1000=true;
		lastCub.ocultador=false;
		lastCub.ehConEl = true;				
		lastCub.inflar(-0.3f);





		addCub("bateria",119).setIcon("blocoC30.jf1",119,117,115,10,12).setExtCom("0").setWav("rr");
		lastCub.dy=-0.3f; // sobre o solo
		lastCub.temExtraByte=true; // 0..100, q eh qtd disponivel
		lastCub.ehFerr=true;
		lastCub.setDtCma("dtCilindro01.j3d",99);
		lastCub.ehGhostBlock=true;
		lastCub.opGetComClick=true;														
		lastCub.ocultador=false;
		lastCub.opFazSombra=false;
		lastCub.setIncCal(-0.3f);
		
		addSprite("fio_cobre","cordao02.j3d",J.cor[99]); 
		lastCub.setIcon("cordao02.jf1",109);

		addCub("fogao_eletrico",119).setDtLat("maquina06.j3d",99).setDtCma("vator03cma.j3d",119-3).setIcon("maq03.jf1",119,119-2,119-4,99,97).setExtCom("e").setWav("rr");
		lastCub.opEhMaqInt=lastCub.temExtraByte=true; 
		lastCub.ehConEl = true;		
		lastCub.operavel=true;			

		addPao("fatia_abobora","fatia05.j3d",null,"").setIcon("fatia07.jf1");
		lastCub.setCozido("abobora_cozida");		
		lastCub.opGetComClick=true;		
		
		addPao("abobora_cozida","fatia05.j3d",null,"").setIcon("fatia08.jf1");
		lastCub.opGetComClick=true;
		lastCub.ehComivel=true;				
		
		addCub("seed_coluna",99).operavel=true;
			
		addFerr("polvora","semente02.j3d",49).setIcon("saco05.jf1",49).setWav("f ").ehEmbrulho=true; // ajudou no shape da mao
		lastCub.opGetComClick=true;						
		lastCub.temExtraByte=true; // ???			
			
		addSprite("cabeca_creeper","creeper03.j3d",J.cor[69],"").setIcon("creeper02.jf1",69);
		lastCub.opAutoGet=true; // p pegar quando passar por cima

		addFerr("poh_ferro","semente02.j3d",49).setIcon("saco05.jf1",49).setWav("f ").setPotCalor(1).ehEmbrulho=true; // ajudou no shape da mao
		lastCub.ehSemente=true;								
		lastCub.opGetComClick=true;						
		lastCub.temExtraByte=true;
		
		addFerr("poh_cobre","semente02.j3d",99).setIcon("saco05.jf1",99).setWav("f ").setPotCalor(1).ehEmbrulho=true; // ajudou no shape da mao
		lastCub.ehSemente=true;								
		lastCub.opGetComClick=true;						
		lastCub.temExtraByte=true;							
		
		addFerr("poh_ouro","semente02.j3d",79).setIcon("saco05.jf1",79).setWav("f ").setPotCalor(1).ehEmbrulho=true; // ajudou no shape da mao
		lastCub.ehSemente=true;								
		lastCub.opGetComClick=true;						
		lastCub.temExtraByte=true;							
		
		addFerr("poh_carvao","semente02.j3d",179).setIcon("saco05.jf1",179).setWav("f ").setPotCalor(1).ehEmbrulho=true; // ajudou no shape da mao
		lastCub.ehSemente=true;								
		lastCub.opGetComClick=true;						
		lastCub.temExtraByte=true;							
		
		addFerr("poh_enxofre","semente02.j3d",6).setIcon("saco05.jf1",6).setWav("f ").setPotCalor(1).ehEmbrulho=true; // ajudou no shape da mao
		lastCub.ehSemente=true;								
		lastCub.opGetComClick=true;						
		lastCub.temExtraByte=true;									

		addCub("maq_moedor",119).setDtLat("maquina06.j3d",49).setDtCma("vator03cma.j3d",119-3).setIcon("maq03.jf1",119,119-2,119-4,49,47).setExtCom("e").setWav("rr");
		lastCub.opEhMaqInt=lastCub.temExtraByte=true; 
		lastCub.ehConEl = true;		
		lastCub.operavel=true;
		
		addCub("BLOCO_VIDRO",J.altAlfa(49,0.5f)).setIcon("bloco10.jf1",119).cct=-3;
		lastCub.ocultador = true;

		{ // suits
			addSuit("astroSuit","astrosuit02.j3d",J.cor[119],"").setIcon("capac01.jf1",119,117,115,170,172).setExtCom("0").setWav("tt");
			addSuit("aquaSuit","astrosuit02.j3d",J.cor[299],"").setIcon("capac01.jf1",11,3,3,170,172).setExtCom("0").setWav("tt");
			addSuit("coldSuit","astrosuit02.j3d",J.cor[89],"").setIcon("capac01.jf1",89,87,85,170,172).setExtCom("0").setWav("tt");
			addSuit("radioSuit","astrosuit02.j3d",J.cor[159],"").setIcon("capac01.jf1",159,157,155,170,172).setExtCom("0").setWav("tt");
			addSuit("heatSuit","astrosuit02.j3d",J.cor[49],"").setIcon("capac01.jf1",49,47,45,117,119).setExtCom("0").setWav("tt");
			addSuit("marsSuit","astrosuit02.j3d",J.cor[109],"").setIcon("capac01.jf1",109,107,105,111,113).setExtCom("0").setWav("tt");
			addSuit("zettaSuit","astrosuit02.j3d",J.cor[169],"").setIcon("capac01.jf1",169,167,165,170,172).setExtCom("0").setWav("tt");
		}

		addTanque("oxigenio",119,"g");
		addCano("cano_oxigenio",J.cor[119],119);

		addCub("cil_oxigenio",49).setIcon("blocoC30.jf1",49,47,45,10,179).setExtCom("0").setWav("rr");
		lastCub.dy=-0.3f; // sobre o solo
		lastCub.temExtraByte=true; // 0..100, q eh qtd disponivel
		lastCub.ehFerr=true;
		lastCub.setDtCma("dtCilindro01.j3d",179);
		lastCub.ehGhostBlock=true;
		lastCub.opGetComClick=true;														
		lastCub.ocultador=false;
		lastCub.opFazSombra=false;
		lastCub.setIncCal(-0.3f);

		addCub("maq_compressor",119).setDtLat("maquina06.j3d",89).setDtCma("vator03cma.j3d",119-3).setDtBxo("vator03bxo.j3d",119-3).setIcon("maq03.jf1",119,119-2,119-4,179,179-2).setExtCom("e").setWav("rr");
		lastCub.opEhMaqInt=lastCub.temExtraByte=true; 
		lastCub.ehConCano=true; // veja q nem toda maq tera' conexão com cano
		lastCub.ehConEl = true;		
		lastCub.operavel=true;	

		{ // BANANEIRA E RELACIONADOS
			addCub("caule_bananeira",J.mixColor(129,149)).setCrCmaBxo(139).setIcon("bloco06.jf1",139,129,129-2,129-4,12).setExtCom("f").setWav("ff");
			lastCub.setIncCal(-0.32f); 
			lastCub.ocultador=false;
			lastCub.ipLO = lastCub.ipNS = 0.05f;
			lastCub.operavel = true;
			lastCub.ehCaule = true;
			lastCub.opForceImpDt=true; // imprime o dt mesmo q de longe			
			//lastCub.operavel = true; // p cortado depois
			
			addSprite("muda_bananeira","muda03.j3d",J.cor[149]);
			lastCub.setExtCom("0");
			lastCub.operavel=true;
			lastCub.setIcon("muda02",149,148,147,146,145);
			tPlant.add(lastCub); // p animacao da muda		
			
			addSprite("folha_bananeira","folhaBananeira01.j3d",J.cor[69],"4").setIcon("folha02.jf1",149);
			lastCub.operavel=true;
			lastCub.dtCen.resize(6f);
			tPlant.add(lastCub); // p animacao da muda			

			// CUIDADO! tem q ser nesta ordem p crescer!
			addTrigo("penca_banana_crescendo","penca03.j3d",146,"4c").setIcon("apagar01.jf1",149).setExtCom("fm").setWav("ff"); // perder fruto, caule e tudo se extrair antes do tempo			
			lastCub.dzz=-0.5f;
			lastCub.ehCaule=true; // gambiarra			
			lastCub.dtCen.resize(1.8f);
			addTrigo("penca_banana_madura","penca03.j3d",149,"4").setIcon("apagar01.jf1",69).setExtCom("f").setWav("ff");
			lastCub.ehCaule=true; // gambiarra
			lastCub.dzz=-0.5f;
			lastCub.dtCen.resize(1.8f);
			
			addPao("cacho_banana_verde","cacho01.j3d",J.cor[149],"").setIcon("cachoban01.jf1",149);
			lastCub.operavel=true;
			lastCub.dtCen.resize(1.6f);			
			lastCub.opGetComClick=true;		
			addPao("cacho_banana_madura","cacho01.j3d",J.cor[79],"").setIcon("cachoban01.jf1",79);
			lastCub.dtCen.resize(1.6f); // p banana podre eh soh trocar 79 por 179... mas isso implicaria em fazer todas as outras comidas "apodreciveis" tb. Talvez no futuro.
			lastCub.opGetComClick=true;
			
			addPao("banana_verde","banana01.j3d",J.cor[149],"").setIcon("banana01.jf1",149);
			lastCub.dtCen.resize(1.6f); 
			lastCub.operavel=true;
			lastCub.opGetComClick=true;						
			lastCub.ehComivel=true;			

			addPao("banana_madura","banana01.j3d",J.cor[79],"").setIcon("banana01.jf1",79);
			lastCub.dtCen.resize(1.6f); 
			lastCub.opGetComClick=true;						
			lastCub.ehComivel=true;						
			
			addPao("torta_banana_crua","torta02.j3d",J.cor[139],"4").setIcon("torta02.jf1",139);
			lastCub.setCozido("torta_banana_assada");
			lastCub.dtCen.deslTudo(0,+0.25f,0);
			lastCub.ehComivel=true;			
			lastCub.opGetComClick=true;

			addPao("torta_banana_assada","torta02.j3d",J.cor[159],"4").setIcon("torta02.jf1",159);
			lastCub.dtCen.deslTudo(0,+0.25f,0);
			lastCub.ehComivel=true;			
			lastCub.opGetComClick=true;			

	
			
		}
		
		addCub("seed_sem_mato",99).operavel=true;
		
		{
			addCub("valvula_On",49).operavel=true;
			lastCub.setIcon("bloco34.jf1",49);			
			lastCub.ehCano = true;
			//lastCub.inflar(+0.125f); // soh um lembrete
			//lastCub.inflar(-0.25f);
			//lastCub.dz=-0.25f; // ao sul
			lastCub.setDtCen("valvula01b.j3d",J.cor[119],"4");
			lastCub.ocultador = false;
			lastCub.ehConCano = true;
			
			addCub("valvula_Off",49).operavel=true;
			lastCub.setIcon("bloco34.jf1",49);
			lastCub.ehCano = true;
			lastCub.setDtCen("valvula01.j3d",J.cor[119],"4");
			lastCub.ocultador = false;
			lastCub.ehConCano = true;			
		}

		// p fazer varinha de pesca
		addTrigo("bambu_crescendo", "cana03.j3d", J.mixColor(149,10), "c4").setIcon("cana04.jf1",69);
		lastCub.dtCen.resize(2f);
		addTrigo("bambu_maduro", "cana03.j3d", J.mixColor(149,10),"4").setIcon("cana04.jf1",149).setExtCom("fm");
		lastCub.dtCen.resize(2f);
		addFerr("muda_bambu", "cana04.j3d", J.mixColor(149,10)).setIcon("cana04.jf1",149);
		lastCub.ehSemente=true; // estranho, mas deve funcionar
		lastCub.opGetComClick=true;
		addFerr("varinha_bambu", "cana04.j3d", 139-2).setIcon("cana04.jf1",139-2);
		lastCub.opGetComClick=true;			
		addFerr("varinha_bambu_grande", "cana04.j3d", 139).setIcon("cana04.jf1",139);
		lastCub.opGetComClick=true;					
		
		addSprite("fibra_vegetal","cordao02.j3d",J.cor[139]); 
		lastCub.setIcon("cordao02.jf1",139);
		
		addSprite("pedaco_ferro_velho","ferroVelho01.j3d",J.cor[179]); 
		lastCub.setIcon("ferroVelho01.jf1",179);		
		lastCub.stCozido = "barra_ferro";
		lastCub.opMultCarne = 6; // multBife depois
		lastCub.opGetComClick=true;		
		
		// VEJA O GUIA RÁPIDO SOBRE LANDMARK NO BLOCO DEDICADO, em part1
		addCub("land_mark",99).setDtLat("faixa01.j3d",99).operavel=true;
		lastCub.setIcon("bloco35.jf1"); 
		addCub("land_mark_area",J.altAlfa(99,0f)).setDtLat("faixa01.j3d",99);
		lastCub.setIcon("bloco35.jf1");				
		lastCub.opForceImpDt = true;
		lastCub.ocultador = false;
		//lastCub.crCma=null;
		//lastCub.crBxo=null;		
		//lastCub.ocultador=true;
		//lastCub.ocultavel = true;
		lastCub.ocultaOmesmo = true;
		
		addCub("seed_ctt_ult",12).operavel=true;
		
		addCub("seed_del_tip_bxo",12).operavel=true;
		addCub("seed_plat",12).operavel=true;
		addCub("seed_platV",12).operavel=true;
		addCub("seed_sem_agua",12).operavel=true;
		addCub("temp",12).operavel=true;
		addCub("seed_desce_col",12).operavel=true; // plotar no ar, ele vai duplicando abaixo e, quando achar um cubo nao nulo (nem agua), a coluna q cresceu p baixo torna-se deste material. Usei em ctx.
		addCub("rocha_calcaria",119).setDtLat("obsidian01.j3d",119-2).setDtCma("obsidian01cma.j3d",119-2).setDtBxo("obsidian01bxo.j3d",119-2).setIcon("bloco36.jf1",119).setExtCom("p").setWav("rr");		
		
		addSprite("ger_eolico","catavento01.j3d",J.cor[119]).setIcon("cataven1.jf1",119).setExtCom("e").setWav("rr");
		lastCub.ehConEl = true;
		lastCub.operavel = true;

		addCub("sonda",49).setIcon("bloco25.jf1",49).setExtCom("e").setWav("rr");
		lastCub.setIncCal(-0.36f);
		lastCub.tb=1f;
		lastCub.ocultador=false;
		lastCub.operavel=true;
		
		addCub("sonda_agua",89-6).setIcon("bloco25.jf1",89-4).setExtCom("e").setWav("rr");
		lastCub.setIncCal(-0.36f);
		lastCub.tb=1f;		
		lastCub.ocultador=false;		
		lastCub.operavel=true;				
	
		{ // faixa de condutores eletricos
			for(int q=0; q<=maxCond; q++){ // ?tá bom assim??? Isso permite 100 redes diferentes no mesmo mapa.
				//addCub("cond"+q,149);
				//lastCub.opLuzLife1000=true; // habilitar depois
				addCub("cond"+q,J.rndCr9());// p debug
				lastCub.life=-1; // importante. Ao liberar uma rede, deve-se tb zerar o life, senao o cond nao fica disponivel e entope os slots disponiveis.
				lastCub.ehCond=true;
				lastCub.setIcon("bloco33.jf1",149,147,144,10,59);
				lastCub.setExtCom("e").setWav("rr");
				lastCub.operavel=true;					
				lastCub.opFazSombra=true;
				lastCub.ocultador=false;
				lastCub.ehConEl = true;								
			}
			
		}
		

		
		// uuuuuuuuuuuuuuuuu
		addSlotsMobs();
		setInds();
		return 0; // pequena gambiarra
	}
		int tPlotar=-1;
		
	public Cub addTanque(String nm, int cr, String op){
		return addTanque(nm,J.cor[cr],op);
	}
	public Cub addTanque(String nm, Color cr, String op){
		/* op: 
			"g" gas, semitransparente de acordo com a qtd 
			"l" liquidos: bounce cma no conteudo
		*/
		/* isso ficou muito bom.
		   p inserir um novo tanque, basta fazer em "uuu": 
					addTanque("gas",219,"g");
		*/
		if(J.tem("tanque",nm)) 
			J.impErr("!nao eh permitido inserir 'TANQUE' na criacao destes. Este trecho serah inserido automaticamente. ","addTanque()");
		Cub cb = addCub(nm+"_tanque",cr).setIncCal(-0.05f);		
		cb.selecionavel=false; 
		cb.ehPlotavel=false;		
		cb.opBounceWithJoe=false; // corrigindo bug do bounce duplo no posImp
		Cub c = addCub("tanque_"+nm,89).setIcon("bloco20.jf1",119).setExtCom("b").setWav("rr");
		if(J.tem('l',op)) c.opOscTanq = true; // estranho, mas a ordem p oscilar no posImp vem do cubo principal...
		c.temExtraByte=true;
		c.ehConCano=true;
		c.ehTanque = true;			
		// gas eh sempre preenchido totalmente
		if(J.tem('g',op)) c.opAlfaInf20 = true;
		else c.opAltInfTanq = true;
		c.cubCen = cb;		
		return c;
	}	
	
	public Cub addCano(String nm, int cr){
		return addCano(nm, J.cor[cr], cr);
	}
	public Cub addCano(String nm, Color crc, int cri){
		if(!J.tem("cano",nm)) nm="cano_"+nm; // auto-correcao
		Cub c = addCub(nm,crc).setIcon("bloco21.jf1",cri).setExtCom("b").setWav("rr");
		c.ocultavel=false;
		c.operavel=true;
		c.temExtraByte=true;
		c.ocultador=false;
		c.ehCano = true;
		c.ehConCano = true;			
		c.opAltInfCano = true;		
		return c;
	}
	
	public void addSlotsTransp(){
		// acho q eh melhor remover/reformar os transportadores
		tra0 = tCub.size()+1; 
		tra9 = tra0+maxTransp;
		Cub c = null;
		for(int q=0; q<=maxTransp; q++){
			c = addCub("transp"+tCub.size(),118).setExtCom("b").setWav("rr");
			c.ativo=false;
			//c.inflar(-0.1f); // inflado nao ficou bom
			c.operavel=true;		
			c.opGirDtCen=true;			
			c.ehTransp = true;
			c.ocultador=false;			
			c.ehConTransp = true;
			c.ocultador = false;
			c.tip = tra0+q; // ?isso eh bom???
			//c.insPntLife();
			//c.insPlts();				
			c.crMap = J.cor[119];
			tCub.add(c);				
		}
	}	
	

	
	
	public int rndSemente(){
		// trabalhoso p pc, mas pega todas as semente. Bem automatico.
		ArrayList<Integer> l = new ArrayList<>();
		for(int q=0; q<tCub.size(); q++)
			if(tCub.get(q).ehSemente)
				l.add(q);
		return l.get(J.R(l.size()));
	}
	public int plantaDaSemente(int s){
		// daria p automatizar isso e fazer o programa deduzir quando eh semente (pelo nome) e gerar a planta correspondente automaticamente.
		if(s==getInd("coco")) return getInd("muda_coq");
		int b=-1;
		if(s==getInd("sem_sortida")) 
			b=J.R(12); // cuidado aqui!
		// ! se for um numero muito baixo p randomizacao, as ultimas sementes nunca aparecerao.

		if(b==0 || s==getInd("sem_trigo")) return getInd("trigo_crescendo");
		if(b==1 || s==getInd("sem_alface")) return getInd("alface_crescendo");
		if(b==2 || s==getInd("sem_repolho")) return getInd("repolho_crescendo");
		if(b==3 || s==getInd("sem_cenoura")) return getInd("cenoura_crescendo");
		if(b==4 || s==getInd("sem_beterraba")) return getInd("beterraba_crescendo");
		if(b==4 || s==getInd("sem_morango")) return getInd("morangueiro_crescendo");
		if(b==5 || s==getInd("sem_batata")) return getInd("batata_crescendo");
		if(b==6 || s==getInd("sem_abobora")) return getInd("abobora_crescendo");
		if(b==7 || s==getInd("sem_melancia")) return getInd("melancia_crescendo");
		if(b==8 || s==getInd("sem_melao")) return getInd("melao_crescendo");
		if(b==9 || s==getInd("sem_milho")) return getInd("milho_crescendo");
		if(b==10|| s==getInd("talo_cana")) return getInd("cana_crescendo");
		if(b==11|| s==getInd("muda_bambu")) return getInd("bambu_crescendo");


		if(b!=-1) J.impErr("!randomizacao falha para a semente sortida","plantaDaSemente()");
		J.impErr("!semente sem a planta correspondente:"+s+" |"+tCub.get(s).name+"|", "plantaDaSemente()");
		return -1;
	}
	
	
	public void addSlotsMobs(){
		mob0 = tCub.size()+1; 
		mob9 = mob0+maxMob;
		Cub c = null;
		for(int q=0; q<=maxMob; q++){
			c = new Cub();
			c.ativo = false;
			c.ehMob = true;
			c.opImpLife = true;
			c.opFazSombra = false;
			c.ehGhostBlock=false;
			c.dtCen = new J3d();
			c.crDtCen = addPal(49); // precisa disso senao buga
			c.ocultador = false;
			c.tip = mob0+q; // este indica o indice em  t C u b . Nao mecha em "tip" de mobs!!! Use "tipp" em vez deste.
			//c.insPntLife();
			c.insPlts();				
			c.setName("???");
			c.crMap = J.cor[12];
			tCub.add(c);				
		}
	}	
		int tra0=-1, tra9=-1, maxTransp=24;
	public void setInds(){
		// atribuir indices automaticamente
		// tb deduz "ehDegrau" e faz ajustes gerais menores
		int c=0;
		for(Cub cc:tCub){
			cc.tip = c++;
			
			// p q apanhar?
			if(cc.tc==0) if(!cc.opFazRio) cc.ehDegrau=true; 
			if(cc.opTemFogoQueAlastra) cc.operavel=true;
			if(cc.ehFerr || cc.ehSemente) {
				cc.opGetComClick=true; // ?isso eh bom???
				cc.temExtraByte=true; // inf vira qtd q vira inf. Bateria (nao fixa) estah aqui tb.
			}
		}			
		
	}

	public boolean temCub(String p){
		for(Cub c:tCub)
			if(J.iguais(p,c.name))
				return true;
		return false;	
	}
	
	// somente blocos "vivos" precisam do indexamento abaixo, somente os operahveis
	final int 
	// gargalo de inicio de execucao aqui! Muito indexamento de cubo... mas nao parece afetar tanto assim...
			maxVolCano=6, // volume maximo dos canos (ateh pode extrapolar); Nao eh soh mudar este valor, tem q ajustar a parte de impressao do cubo central tb. Tah um pouco inconsistente essa var, mas achei melhor deixar.
			maxVolTanque=20, // volume maximo dos tanques. Tem uma outra var q fala do volume interno das maquinas. Vide ss Maq (a classe primitiva).
			tmpRefinaria = 22,
			tmpRede = 12, // esse pode mecher. Quanto menor, mais rapido as maquinas funcionarao.
			tmpIncRede= tmpRede>>1, // nao mecha nisso
			tmpDecRede= 0, // nao mecha nisso
		tRochaProEl = iniCubs()+J.esc("indexando cubos...")+getInd("rocha_proEl"), // gera el na rede 
		tCauleSeringueiraVazia=getInd("caule_seringueira_vazia"), // este nome nao tah bom
		tCauleSeringueiraCheia=getInd("caule_seringueira_cheia"), // encherah a caneca
		tSplash=getInd("splash"), //fffffffffffffffffffffffff
		tSplash2=getInd("splash2"),
		tSplash3=getInd("splash3"),
		tFerroVelho=getInd("ferro_velho"), // nao confunda com "p e d a c o _ f e r r o _ v e l h o" q são lascas
		tDuplicador=getInd("duplicador"),
		tTorreRef=getInd("torre_ref"),
		tTorreRefR=getInd("torre_ref_r"),
		tTorreRefC=getInd("torre_ref_c"),
		tTorreRefG=getInd("torre_ref_g"),
		tTorreRefP=getInd("torre_ref_p"),
		tCombustor=getInd("combustor"),// p refinaria de petro		
		tEncaix = getInd("encaixotadora"),
		tMinFerro = getInd("min_ferro"),
		tMinOuro = getInd("min_ouro"),
		tMinCobre = getInd("min_cobre"),
		tFornoInd = getInd("forno_industrial"),
		tRochaAntiEl = getInd("rocha_antiEl"), // consome 
		tMoldadora= getInd("moldadora"), 
		tMadeira= getInd("madeira"), 
		tAdobeUmido = getInd("adobe_umido"), // expandi degrais de adobe seco e umido p blocos inteiros
		tDegrauAdobeUmido = getInd("degrau_adobe_umido"),
		tDegrauAdobeSeco = getInd("degrau_adobe_seco"), // antes de retirar. Depois q o player removeu, fica sem o "seco" do final.
		tDegrauAdobe = getInd("degrau_adobe"),
		tVidroGoma = getInd("vidro_goma"), 
		tPrensandoNS = getInd("prensando_NS"), 
		tPrensandoLO = getInd("prensando_LO"), 
		tDerretedor = getInd("derretedor"), 
		tGerCombustao = getInd("ger_combustao"), 
		tLatex = getInd("latex"), 
		tFibraVeg = getInd("fibra_vegetal"), 
		tFioCobre = getInd("fio_cobre"), 
		tPohCobre = getInd("poh_cobre_tanque"),
		tBorracha = getInd("borracha"), 
		tCond0 = getInd("cond0"), 
		tCond9 = getInd("cond"+maxCond), 		
		tRede0 = getInd("rede0"), 
		tRede1 = getInd("rede1"), 
		tPortalNS = getInd("portal_ns"),
		tPortalLO = getInd("portal_lo"),
		tEnvasadora = getInd("envasadora"),
		tPloter = getInd("ploter"),
		tCilOxigenio = getInd("cil_oxigenio"), // outros cilindros depois
		tBateria = getInd("bateria"), // esta eh mais p transportar a el e p suits
		tBateriaFixa = getInd("bateria_fixa"), // normalmente plotada e recarregando por geradores
		tGerSolar = getInd("ger_solar"), // "painel_solar" "gerador_solar"
		tGerEolico = getInd("ger_eolico"),// catavento
		tSonda = getInd("sonda"),
		tSondaAgua = getInd("sonda_agua"),
		tSondaPetro = getInd("sonda_petro"),
		tChFenda = getInd("chave_fenda"),
			tVidraca = getInd("vidraca"),
			tFrascoVazio = getInd("frasco_vazio"),
			tFrascoSucoMaca = getInd("frasco_suco_maca"),
			tFrascoGarapa = getInd("frasco_garapa"),
			tValvOn = getInd("valvula_on"),
			tValvOff = getInd("valvula_off"),
			tTanqueVazio = getInd("tanque_vazio"),
			tTanqueOxigenio = getInd("tanque_oxigenio"),
			tTanqueLatex = getInd("tanque_latex"),			
			tTanqueAgua = getInd("tanque_agua"),			
			tTanqueGarapa = getInd("tanque_garapa"),			
			tTanqueAcucar = getInd("tanque_acucar"),			
			tTanquePetro = getInd("tanque_petro"),			
			tTanqueResina = getInd("tanque_resina"),			
			tTanqueComb = getInd("tanque_comb"),			
			tTanqueGas = getInd("tanque_gas"),			
			tTanqueSucoMaca = getInd("tanque_suco_maca"),			
			tCanoVazio = getInd("cano_vazio"),
			tCanoOxigenio = getInd("cano_oxigenio"),
			tCanoAgua = getInd("cano_agua"),
			tCanoPetro = getInd("cano_petro"),
			tCanoResina = getInd("cano_resina"),
			tCanoComb = getInd("cano_comb"),
			tCanoGas = getInd("cano_gas"),
			tCanoSucoMaca = getInd("cano_suco_maca"),
			tCanoGarapa = getInd("cano_garapa"),
		tSelecionador = getInd("selecionador"),
		tTranspIn = getInd("transp_in"),
		tTranspOut = getInd("transp_out"),
		tTranspVazio = getInd("transp_vazio"),
		//tTransp = getInd("transp"), NAO AQUI
		tChama = getInd("chama"),// este eh posImp
		tFogo = getInd("fogo"),
		tVarinha = getInd("varinha"),
		tIsca = getInd("isca"),
		tRetirador = getInd("retirador"),
		tGuardador = getInd("guardador"),
		//tMisturador = getInd("misturador"),
		tPump = getInd("pump"),
		tMaqAcucar = getInd("maq_acucar"),
		tJuicer = getInd("juicer"),
		tIdent = getInd("identificador"),
		tMaqFogaoEl = getInd("fogao_eletrico"),
		tMaqDuct = getInd("maq_duct"), // maq ductisadora, transforma poh de minerio em fio desencapado (cobre por enquanto)
		tMaqFio = getInd("maq_fio"), // transforma fioDeCobre+borracha em tRede0/tCond0 (tRede0 é temporário)
		tMaqCompressor = getInd("maq_compressor"),
		tMaqBorracha = getInd("maq_borracha"),
		tMaqMoedor = getInd("maq_moedor"),
		tCabecaZumbi = getInd("cabeca_zumbi"),
		tBracoZumbi = getInd("braco_zumbi"),
		tExtractor = getInd("extractor"),
		tSerra = getInd("serra"),
		tMadeiraFacil = getInd("madeira_facil"), // essa eh removivel com a mao, quando sai da serra
		tEstNor = getInd("esteira_nor"),
		tEstSul = getInd("esteira_sul"),
		tEstLes = getInd("esteira_les"),
		tEstOes = getInd("esteira_oes"),
		tHasteCB = getInd("haste_CB"),
		tHasteNS = getInd("haste_NS"),
		tHasteLO = getInd("haste_LO"),
			tAreiaMarc = getInd("areia_marciana"),
			tDegrauAreiaMarc = getInd("degrau_areia_marciana"),
			tTanqLatex = getInd("tanque_latex"),
			tPoFerro = getInd("poh_ferro"), // este eh embrulho
			tTanqPohFerro = getInd("tanque_poh_ferro"),
			tTanqPohEnxofre = getInd("tanque_poh_enxofre"),
			tTanqAcucar = getInd("tanque_acucar"),						
			tAreiaNether = getInd("areia_nether"),
			tDegrauAreiaNether = getInd("degrau_areia_nether"),
		tFolhaBan = getInd("folha_bananeira"),
		tCauleBan = getInd("caule_bananeira"),
		tCachoBanVerde = getInd("cacho_banana_verde"),
		tBanVerde = getInd("banana_verde"),
		tJetPack = getInd("jetPack"),
		tChamuscado = getInd("chamuscado"),
		tAreia = getInd("areia"),
		tLandMark =  getInd("land_mark"),
		tLandMarkArea =  getInd("land_mark_area"),
		tLampadaOn = getInd("lampada_on"),
		tLampadaOff = getInd("lampada_off"),
		tTemp = getInd("temp"), // bloco temporario p se trabalhar com seeds
		tSeedColuna = getInd("seed_coluna"),
		tSeedSeringueira = getInd("seed_seringueira"),
		tPreenchido = getInd("preenchido"),
		tSeedPreencher = getInd("seed_preencher"),
		tSeedSmooth = getInd("seed_smooth"),
		tSeedCirc = getInd("seed_circ"),
		tSeedPortal = getInd("seed_portal"),
		tSeedChuva = getInd("seed_chuva"),
		tSeedNivelar = getInd("seed_nivelar"),
		tSeedPlat = getInd("seed_plat"),
		tSeedPlatV = getInd("seed_platV"),
		tSeedDelViz = getInd("seed_del_viz"),		
		tSeedDesceCol = getInd("seed_desce_col"),
		tSeedRefinaria = getInd("seed_refinaria"),
		tSeedPir = getInd("seed_piramide"),
		tSeedExp = getInd("seed_exp"),
		tSeedMato = getInd("seed_mato"),
		tSeedCtt = getInd("seed_ctt"),
		tSeedCttUlt = getInd("seed_ctt_ult"),
		tSeedCoq = getInd("seed_coq"),
		tSeedSeq = getInd("seed_sequoia"),
		tSeedPedra = getInd("seed_pedra"),
		tSeedPlatAether = getInd("seed_plat_aether"),
		tSeedDelTipAbaixo = getInd("seed_del_tip_bxo"),
		tSeedPorSolo = getInd("seed_por_solo"),
		tSeedSemAgua = getInd("seed_sem_agua"),		
		tSeedSemAlga = getInd("seed_sem_alga"),
		tSeedSemMato = getInd("seed_sem_mato"),
		tSeedMacieira = getInd("seed_macieira"),
		tFolhaCoq = getInd("folha_coq"),
		tFolhaBananeira = getInd("folha_bananeira"),
			tBambuMaduro = getInd("bambu_maduro"),
			tCanaMadura = getInd("cana_madura"),
			tTaloCana = getInd("talo_cana"),
			tMorango = getInd("morango"),
			tMaca = getInd("maca"),
			tMacaVerde = getInd("maca_verde"),
			tFatiaMelancia = getInd("fatia_melancia"),
			tFatiaMelao = getInd("fatia_melao"),
			tFatiaCoco = getInd("fatia_coco"),
		tEspiga = getInd("espiga"),
		tEspigaVerde = getInd("espiga_verde"),
		tMilhoMaduro = getInd("milho_maduro"),
		tMilhoCrescendo = getInd("milho_crescendo"),
		tMilhoSeco = getInd("milho"),
		tAbobora = getInd("abobora"),
		tMelao  = getInd("melao"),
		tMelancia  = getInd("melancia"),
		tRocha = getInd("rocha"),
		tMarmore = getInd("marmore"),
		tAlga = getInd("alga"),
		tTrigo = getInd("trigo"),
		tAlface = getInd("alface"),
		tRepolho = getInd("repolho"),
		tCenoura = getInd("cenoura"),
		tMorangueiro = getInd("morangueiro_maduro"),
		tBeterraba = getInd("beterraba"),
		tBatata = getInd("batata"),
		tMato = getInd("mato"),
		tAstroSuit = getInd("astroSuit"),
		tAquaSuit = getInd("aquaSuit"),
		tColdSuit = getInd("coldSuit"),
		tHeatSuit = getInd("heatSuit"),
		tRadioSuit = getInd("radioSuit"),
		tMarsSuit = getInd("marsSuit"),
		tZettaSuit = getInd("zettaSuit"),
		tBigorna = getInd("bigorna"),
		tBanc = getInd("bancada"),
		tExp = getInd("exp"),
		tExp2 = getInd("exp2"),
		tFolhaMacieira = getInd("folha_macieira"),
		tFolhaMacaVerde	= getInd("folha_maca_verde"),
		tFolhaMaca	= getInd("folha_maca"),
		tTnt = getInd("tnt"), tmpTnt=20, // vai aqui mesmo
		tTntOn = getInd("tnt_on"),
			tLama = getInd("lama"),
			tAgua = getInd("agua"),
			tAguaCorr = getInd("agua_corr"),
			tAguaCel = getInd("agua_cel"),
			tAguaCelCorr = getInd("agua_cel_corr"),		
			tPetroleo = getInd("petroleo"),
			tPetroleoCorr = getInd("petroleo_corr"),
			tAcido = getInd("acido"),
			tAcidoCorr = getInd("acido_corr"),		
		tColunaMarmore = getInd("coluna_Marmore"),		
		tMudaSeringueira = getInd("muda_seringueira"),		
		tMudaCoq = getInd("muda_coq"),		
		tMudaBan = getInd("muda_bananeira"),		
		tMudaSilvestre = getInd("muda_silvestre"),		
		tMudaMacieira = getInd("muda_macieira"),
		tFungoNether = getInd("fungo_nether"),
		tCactoBase = getInd("cacto_base"), 
		tCactoTopo = getInd("cacto_topo"), // ?jah arrumou  c a c t o   z e t t a ???
		tLava = getInd("lava"),
		tLavaCorr = getInd("lava_corr"),		
		tMagma = getInd("magma"),
		tRochaMagma = getInd("rocha_magma"),
		tTerra = getInd("terra"),
		tTerraCel = getInd("terra_cel"),
		tGramaCel = getInd("grama_cel"),
		tGrama = getInd("grama"),
		tMyceliun = getInd("myceliun"),
		tTerraArada = getInd("terra_arada"),
		tTerraAradaRegada = getInd("terra_arada_regada"),
		tVatorNor = getInd("vator_nor"),
		tVatorSul = getInd("vator_sul"),
		tVatorLes = getInd("vator_les"),
		tVatorOes = getInd("vator_oes"),
		tVatorCma = getInd("vator_cma"),
		tVatorBxo = getInd("vator_bxo"),
		tBau = getInd("bau"), // cuidado aqui!		
		tDefumador = getInd("defumador"),
		tAltoForno = getInd("alto_forno"),
		tFornalha = getInd("fornalha")+J.esc("cubos indexados");	
		
		
	public int getCozido(int p){
		for(Cub c:tCub)
			if(c.tip==p)
				return getInd(c.stCozido);
		return 0;
	}



// === ITEM, p inventario, baus e slots =======	
		final int
			larItm=20+3, altItm=20+2+16;

		Jf1 fPadrao = new Jf1("bloco09.jf1");
	public class Itm{
			Jf1 fig = null;
			int tip = J.R(999), qtd = J.R(999);
		public Itm(int q, String st){
			ini(q,getInd(st));
		}
		public Itm(int q, int t){
			ini(q,t);
		}	
						private void ini(int q, int t){
							// controle de limites depois
							if(q==0) t=0;
							if(t==0) q=0;
							qtd = q;
							tip = t;
							fig = null;			
						}
		public void ajustaIcone(){
			fig = tCub.get(tip).getIcon();			
		}
		public boolean imp(int i, int j, boolean sel){
			if(tip==0) {qtd=0; fig=null; }
			if(qtd==0) {tip=0; fig=null; }
			
			int cr = 16;
			if(sel) cr = 12;
			J.impRetRel(cr,0, i,j,larItm,altItm);

			if(qtd>0)
			if(tip!=0)
			if(fig==null){
				fPadrao.impJf1(i+1,j);
				ajustaIcone();
			} else{
				fig.impJf1(i+1,j);
				if(qtd==0) fig=null;
				if(tip==0) fig=null;
			}	
			
			boolean opQtdEhLife=true;
			
			cr = 7;
			if(sel) cr=15;
			if(opQtdEhLife)
			if(J.noInt(qtd,1,100)){
					// 100 -> lar
					// 50	 -> lar/2
					// 25		-> lar/4
					// ?*100 = lar
					// 100*X=lar
				int v = 1+(int)(qtd*(larItm-3)/100f);
				
				J.impRetRel(4,12, i+1, j+24, larItm-2,12);
				J.impRetRel(2,10, i+1, j+24, v,12);
				
				J.impText(i+1,j+34, J.cor[cr], J.intEmSt000(qtd));				
			}
			J.impText(i+2,j+35, J.cor[16], J.intEmSt000(qtd));
			J.impText(i+1,j+34, J.cor[cr], J.intEmSt000(qtd));
			
			if(ms.naAreaRel(i,j,larItm,altItm)) return true; 
			return false;
		}
	}		
	
	public int getInd(String p){
		if(J.iguais(p,"null")) return 0;
		int c=0;
		for(Cub q:tCub){
			if(J.iguais(q.name,p))
				return c;
			c++;
		}
		J.esc("=== LISTA DE CUBOS ===");
		for(Cub cc:tCub)
			J.esc("|"+cc.name+"|");
		J.esc("=== FIM DA LISTA ===");
		J.impErr("!cubo nao encontrado: |"+p+"|","getInd()");
		return -1;
	}	
	
	public int comCai(int p){
		if(p==getInd("areia")) return getInd("areia_cai");
		if(p==getInd("areia_cel")) return getInd("areia_cel_cai");
		if(p==getInd("areia_zetta")) return getInd("areia_zetta_cai");
		if(p==getInd("areia_marciana")) return getInd("areia_marciana_cai");
		if(p==getInd("areia_lunar")) return getInd("areia_lunar_cai");
		if(p==getInd("areia_nether")) return getInd("areia_nether_cai");
		if(p==getInd("cinzas")) return getInd("cinzas_cai");
		// mais tipos depois
		return p;				
	}
	public int semCai(int p){
		if(p==getInd("areia_cai")) return getInd("areia");
		if(p==getInd("areia_cel_cai")) return getInd("areia_cel");
		if(p==getInd("areia_marciana_cai")) return getInd("areia_marciana");
		if(p==getInd("areia_lunar_cai")) return getInd("areia_lunar");
		if(p==getInd("areia_zetta_cai")) return getInd("areia_zetta");
		if(p==getInd("areia_nether_cai")) return getInd("areia_nether");
		if(p==getInd("cinzas_cai")) return getInd("cinzas");
		// mais tipos depois
		return p;				
	}
	public int comCaixote(int p){
		if(p==getInd("frasco_vazio")) return getInd("caixote_frasco");
		if(p==getInd("frasco_suco_maca")) return getInd("caixote_suco_maca");
		//J.impErr("!caixote nao definido: "+p+" "+getName(p),"comCaixote()");
		return -1;
	}
	public int semCaixote(int p){
		if(p==getInd("caixote_frasco")) return getInd("frasco_vazio");
		if(p==getInd("caixote_suco_maca")) return getInd("frasco_suco_maca");
		J.impErr("!caixote nao definido: "+p+" "+getName(p),"comCaixote()");
		return -1;		
	}	
	public int comDegrau(int p){	
		String st = "DEGRAU_"+getCubMod(p).name;
		if(temCub(st)) // mais inteligente assim :)
			return getInd(st);
		return p;
	}
	public int semDegrau(int p){
		String nm = getCubMod(p).name;
		nm = J.trocaTrecho("DEGRAU_","",nm); // se nao achar o trecho p substituir, volta o mesmo.
		//J.sai("=================["+nm+"]");
		return getInd(nm);
	}

	public J3d carrDtPack(Itm it){
		// usei isso p transportadores, adaptei do cod do pack, mas este ultimo tah desatualizado (mas funcionando)
		// melhor juntar isso ao pack depois
		// falta ajustar p ferramentas
		Cub tc = tCub.get(it.tip);
		J3d dt = null;
		String p = (it.qtd>1?"multi":"cubo");
		JPal pl = null;

			if(tc.ehCaule){
				dt = new J3d(p+"Caule01.j3d",0.01f);
				pl = tc.crCma;
				dt.tingir(89,pl);
				pl = tc.crNor;
				dt.tingir(99,pl);
			} else if(tc.ehGrama){
				dt = new J3d(p+"Grama01.j3d",0.01f);
				pl = tc.crCma;
				dt.tingir(89,pl);
				pl = tc.crNor;
				dt.tingir(99,pl);
/*	DEPOIS, se eu tiver paciencia
			} else if(c.ehTijolo){
				c.dtCen = new J3d("multiBrick01.j3d",0.01f);
				pl = tCub.get(t).crCma;
				c.dtCen.tingir(99,pl);
				pl = tCub.get(t).crDtNor;
				c.dtCen.tingir(89,pl);				
*/				
			} else if(tc.ehGelo){
				dt = new J3d(p+"Gelo01.j3d",0.01f);
				pl = tc.crCma;
				dt.tingir(99,pl);
				pl = tc.crDtNor;
				dt.tingir(89,pl);
			} else if(tc.ehMinerio){
			  dt = new J3d(p+"Minerio01.j3d",0.01f);
				pl = tc.crCma;
				dt.tingir(99,pl);
				pl = tc.crDtNor;
				dt.tingir(89,pl);
			} else { // chapado
				dt = new J3d(p+"cubo01.j3d",0.01f);						
				pl = tc.crCma;
				dt.tingir(99,pl);				
			}			
			dt.defIPalts(0);
			return dt;
	
	}	
	
	public int emRioCorr(int p){
		if(p==getInd("agua")) return getInd("agua_corr");
		if(p==getInd("agua_cel")) return getInd("agua_cel_corr");
		if(p==getInd("petroleo")) return getInd("petroleo_corr");
		if(p==getInd("acido")) return getInd("acido_corr");
		if(p==getInd("lava")) return getInd("lava_corr");
		// outros fluidos depois
		
		// nao sei se eh bom isso aqui
		if(p==getInd("rocha_magma")) return getInd("magma");
		
		
		return p;		
	}
	public boolean acidoCorroi(int xa, int ya, int za, int xt, int yt, int zt){
		int t = getCub(xa,ya,za,xt,yt,zt);
		return true;
		//return tCub.get(t).opAcidoCorroi;
	}
	
	public void regFluido(int ag, int agc, int q){
		// veja q usa coordenadas do cubo atual
		// "q" eh a lentidao de alastro, quanto menor, mais rapido alastra
		// agua usa 4
			if(J.C(2)){
				int 
					vb = getCub(xa,ya-1,za, xt,yt,zt),
					vn = getCub(xa,ya,za+1, xt,yt,zt),
					vs = getCub(xa,ya,za-1, xt,yt,zt),
					vl = getCub(xa+1,ya,za, xt,yt,zt),
					vo = getCub(xa-1,ya,za, xt,yt,zt);
					
				if(vb==0) {
					if(J.B(q)) setCub(agc,xa,ya-1,za, xt,yt,zt);
				} else {	
					if(vn==0) if(J.B(q)) setCub(agc,xa,ya,za+1, xt,yt,zt);
					if(vs==0) if(J.B(q)) setCub(agc,xa,ya,za-1, xt,yt,zt);
					if(vl==0) if(J.B(q)) setCub(agc,xa+1,ya,za, xt,yt,zt);
					if(vo==0) if(J.B(q)) setCub(agc,xa-1,ya,za, xt,yt,zt);
				}
				
				if(vb!=0 || (ya==0 && yt==0))
				if(vn!=0)
				if(vs!=0)
				if(vl!=0)
				if(vo!=0) 
					setCub(ag,xa,ya,za, xt,yt,zt);
			}
		
	}
	public int canoDe(int t){

		if(t==getInd("agua")) return getInd("cano_agua");
		if(t==getInd("oxigenio_tanque")) return getInd("cano_oxigenio");
		if(t==getInd("petroleo")) return getInd("cano_petro");
		if(t==getInd("gas_tanque")) return getInd("cano_gas");
		if(t==getInd("resina_tanque")) return getInd("cano_resina");
		if(t==getInd("comb_tanque")) return getInd("cano_comb");
		if(t==getInd("suco_maca_tanque")) return getInd("cano_suco_maca");
		if(t==getInd("garapa")) return getInd("cano_garapa");
		
		J.impErr("!cano nao definido: "+t+" "+getName(t),"canoDe()");		
		return -1;
	}
	public int tanqueDe(int t){
		if(t==0) return getInd("tanque_vazio");
		if(t==getInd("agua")) return getInd("tanque_agua");
		if(t==getInd("latex")) return getInd("tanque_latex");
		if(t==getInd("garapa")) return getInd("tanque_garapa");
		if(t==getInd("petroleo")) return getInd("tanque_petro");
		if(t==getInd("resina_tanque")) return getInd("tanque_resina");
		if(t==getInd("comb_tanque")) return getInd("tanque_comb");
		if(t==getInd("gas_tanque")) return getInd("tanque_gas");
		if(t==getInd("suco_maca_tanque")) return getInd("tanque_suco_maca");
		if(t==getInd("oxigenio_tanque")) return getInd("tanque_oxigenio");
		J.impErr("tanque nao definido: "+t+" "+getName(t)+"      tanqueDe()");		
		return -1;
	}	
	public int conteudo(int xa, int ya, int za, int xt, int yt, int zt){
		return conteudo(getCub(xa,ya,za,xt,yt,zt));
	}
	public int conteudo(int t){
		if(t==0) return 0; // p nao bugar
		if(t==tCanoVazio) return 0;		
		if(t==tJuicer) return 0;// evitando bug q veio sei lah de onde
		
		if(t==tCanoAgua) return getInd("agua");
		if(t==tCanoOxigenio) return getInd("oxigenio_tanque");		
		if(t==tCanoPetro) return getInd("petroleo");
		if(t==tCanoResina) return getInd("resina_tanque");
		if(t==tCanoComb) return getInd("comb_tanque");
		if(t==tCanoGas) return getInd("gas_tanque");
		if(t==tCanoSucoMaca) return getInd("suco_maca_tanque"); // nao eh frasco, esse eh o  c o n t e u d o nao-selecionavel do tanque
		if(t==tCanoGarapa) return getInd("garapa");
		if(t==tTanqueVazio) return 0;
		if(t==tTanqueOxigenio) return getInd("oxigenio_tanque");
		if(t==tTanqueAgua) return getInd("agua");
		if(t==tTanquePetro) return getInd("petroleo");
		if(t==tTanqueResina) return getInd("resina_tanque");
		if(t==tTanqueComb) return getInd("comb_tanque");
		if(t==tTanqueGas) return getInd("gas_tanque");
		if(t==tTanqueSucoMaca) return getInd("suco_maca_tanque");
		if(t==tTanqueGarapa) return getInd("garapa");
		
		if(t==getInd("caixote_frasco")) return getInd("frasco_vazio");
		
		J.impErr("conteudo nao definido: "+t+" "+getName(t),"conteudo()");
		return -1;		
	}
	public boolean regCano(int d, int xa, int ya, int za, int xt, int yt, int zt){
		int 
			tp = getCub(xa,ya,za,xt,yt,zt),
			niv = getInf(xa,ya,za,xt,yt,zt),
			m=0, n=0, o=0;
		switch(d){
			case CMA: n--; break;
			case BXO: n++; break;
			case OES: m--; break;
			case LES: m++; break;
			case NOR: o++; break;
			case SUL: o--; break;
		}
		int c =  getCub(xa+m,ya+n,za+o,xt,yt,zt);
		if(c==tCanoVazio) c = tp;
		
		if(c==tp){					
			int i = getInf(xa+m,ya+n,za+o,xt,yt,zt);
			if(i<niv){
				setCub(c, ++i, xa+m,ya+n,za+o,xt,yt,zt);
				decInf(xa,ya,za,xt,yt,zt);
				return true;
			}					
		}
		return false;
	}
	
		boolean 
			opPisandoNaAgua=false, // p ajuste y
			opCanBounceFaces=false; // controle geral, bom p otimizar o clock
	
		Jf1 icoPadrao = new Jf1("bloco01");
		Cub lastCub = null; // deve facilitar
		int opNivLuzAgua=0;
	
		InfImp 
			infCondLatLO = new InfImp('l'),
			infCondLatNS = new InfImp('n'),
			infCondLatCB = new InfImp('c'),
			infImpTanque = new InfImp('a');
		final int
			PASSEAR = 1,
			FUGIR_JOE=2,
			PERSEGUIR_JOE=3,
			PROCURAR_BLOCO=4,
			ALCANCAR_OBJ=5,
			PACK_PERSEGUE_JOE=6,
			DESCANSANDO=7;
			
			Cub mobScp = null;
			
	J3d 
		dt1Tocha = new J3d("tocha01.j3d",0.01f),
		dt2Tocha = new J3d("tocha02.j3d",0.01f),
		dt3Tocha = new J3d("tocha03.j3d",0.01f);
	

	public class Cub{ // ss cubo ss cel
	// sobre scripts
				ArrayList<String> scp = null;
				int lnScp=-1, 
					xaOb=intNull, yaOb=0, zaOb=0, 
					xtOb=0, ytOb=0, ztOb=0;
				String camScp=null;
			public void ecoarScp(){
				if(camScp==null || scp==null) 
					J.impErr("!o scp nao foi carregado!","ss Cub.ecoarScp()");
				esc(200,12,camScp);
				for(int q=0; q<scp.size(); q++){
					crEsc=7;
					if(q==lnScp) crEsc=15;
					esc(scp.get(q));
				}
			}
			public void carrScp(String cm){
				if(!J.arqExist(cm))
					J.impErr("!scp nao encontrado: |"+cm+"|",cm);
				
				camScp = cm;
				scp = J.openStrings(cm);
				lnScp=0;				
			}
			public void proxLin(){
				// !e se nao tem scp carr
				// !e se ultrapassou a ult linha
				lnScp++;
				if(lnScp>scp.size()) lnScp=0;				
				
				String st = scp.get(lnScp);
				comando(st);				
			}

			public boolean procCub(String st){
				int v = getInd(st);
				xaOb=intNull; // isso eh importante
				int q = 4;
				for(int m=-q; m<=+q; m++)
				for(int n=0; n<=+q; n++)
				for(int o=-q; o<=+q; o++)
					if(v==getCub(xa+m, ya+n, za+o, xt,yt,zt)){
						defOb(xa+m, ya+n, za+o, xt,yt,zt);
						return true;
					}
				impSts("PERDIDO: "+st);
				return false;
			}
			public void defOb(int i, int j, int k, int ii, int jj, int kk){
				xaOb=i;
				yaOb=j;
				zaOb=k;
				xtOb=ii;
				ytOb=jj;
				ztOb=kk;						
			}	
			public void comando(String st){
				if(st.length()<=0) {
					proxLin();
					return;
				}
				
				st = J.emMaiusc(st);
				impSts("scp: ["+st+"]");
				
				// eh soh um comentario				
				if(st.charAt(0)=='/') return; 
				// rotulo
				if(st.charAt(0)==':') return; 
				
				J.extTokens(st);
				
				if(J.iguais(J.tk1,"FUGIR")){
					if(J.iguais(J.tk2,"JOE")){
						intMob=FUGIR_JOE;
						return;					
					}
				}
				if(J.iguais(J.tk1,"INA")){ // if nao achou
					// INA rotulo
					intMob=0;
					if(xaOb==intNull){
						int q = procRotScp(J.tk2);
						if(q!=-1){
							lnScp=q;
							return;
						} else J.impErr("!rotulo de scp nao encontrado: |"+J.tk2+"| "+camScp);
					}
					return;					
				}				
				if(J.iguais(J.tk1,"JUMP")){ // salto incondicional
					// JUMP rotulo
					intMob=0;
					int q = procRotScp(J.tk2);
					if(q!=-1){
						lnScp=q;
						return;
					} else J.impErr("!rotulo de scp nao encontrado: |"+J.tk2+"| "+camScp);					
					return;					
				}									
				if(J.iguais(J.tk1,"MSG")){					
					intMob=0;
					mobMsg=st; // retirar prefixo depois
					return;
				}
				if(J.iguais(J.tk1,"PASS")){
					intMob=PASSEAR;
					return;
				}
				if(J.iguais(J.tk1,"PERS")){
					intMob=0;
					if(J.iguais(J.tk2,"JOE")){
						intMob=PERSEGUIR_JOE;
						return;					
					}				
				}
				if(J.iguais(J.tk1,"PROC")){
					// definir o tipo de bloco depois
					intMob=0;
					xaOb = intNull;
					
					boolean valido = false;
					if(J.iguais(J.tk2,"AREIA")) valido=true;
					if(J.iguais(J.tk2,"CAULE")) valido=true;;
					
					if(valido) procCub(J.tk2);
					if(xaOb!=intNull){
						mobPula();	
						wZumbi.play();
						rumarMobPara(xaOb,yaOb,zaOb,xtOb,ytOb,ztOb);
					}
					
					
					// nao precisa ser todo tipo de bloco, apenas alguns principais
					// ?seria bom referenciar tb por numeros???
					// precisa diferenciar arvore inteira de caule, senao o mob comeca desmontar alguma construcao
					

					return;					
				}								
				if(J.iguais(J.tk1,"SETCUB")){
					for(int w=0; w<10; w++)
						setCub(tAreia, xa,ya+w,za,xt,yt,zt);
					return;
				}
				if(J.iguais(J.tk1,"STOP")){					
					intMob=0;
					return;
				}
				if(J.iguais(J.tk1,"VAI")){
					// !mas tem q ver se tem objetivo
					intMob=ALCANCAR_OBJ;
					return;					
				}

				
				J.impErr("!comando de scp desconhecido: |"+st+"|");
		}
			public int procRotScp(String p){
				// !e se nao tem scp carregado?
				p = J.limpaSt(p);
				if(!J.tem(':',p))
					p=':'+p;
				for(int q=0; q<scp.size(); q++){
					if(J.tem(p,scp.get(q)))
						return q;
				}			
				return -1;
			}
	// fim scripts
			
			Cub // nao pode apontar p si mesmo!!!
				cubCen = null, // p posImp
				cubNor = null, // cuidado!!!
				cubSul = null, 
				cubLes = null, 
				cubOes = null, 
				cubCma = null, 
				cubBxo = null;
			String 
				name = null, 
				stCozido=null; // valendo null, indica q o cubo nao deve entrar na fornalha no slot CAR
			int 
				opMultBife=1,
				opMultCarne=1; // valor padrao. Este é o mecanismo da fornalha p q X carnes gerem 1 bife, sendo q carne aqui poderia ser "ferro velho". Carne aqui seria qq coisa cozivel.
			Color crMap=J.cor[12]; // cor q aparecerah no mapa2d
			JWav 
				wPlot = wPlotPadrao,
				wPass = wPlotPadrao;
			boolean		
				ehCond=false, // condutores eletricos, no novo mecanismo " c o n d # "
				ocultaOmesmo=false, // usei em landMarkArea (?E vidro??? Já fiz???)
				ocultavel=true, // usei p canos. Como sao mais finos, acabavam tendo faces ocultas por grama na lateral dele. Parece q resolveu o bug e abriu uma nova opcao p desenho de cubos.
				opBounceWithJoe=true, // corrigindo bug do bounce duplo p interiores de tanques
				opForceImpDt=false, // imprime o dt mesmo q de longe, usei p fogo e blocos q tem chama como posImp
				opTemFogoQueAlastra=false, // soh p blocos flamejantes, como tem varios, achei melhor "por em evidencia"
				opAniAlga=false, // mudar p tAni depois; no momento, tudo aquilo q nao for animado como trigo, o eh como alga.
				opMenosDtCma=false, // omitindo j3d de cima de vez em quando p economizar clock. Ajustei p gramas, mas dah p expandir p outros.
				ehDegrau=false, // corrigido automaticamente em "s e t I n d ()", mas dah p setar manualmente tb
				ehPlotavel=true, // neguei isto p relogio... mas removi o relogio. Deve usar p outra coisa.
				ehFerr=false,
				ehComivel=false,
				ehBebivel=false,
				ehSemente=false,
				sombreavel=true, // usei no fogo/chama
				selecionavel=true, // precisei isolar alguns cubos da selecao, como laterais pos imp de fios e interiores de tanques
				temPosImp=false,// automatico
				temExtraByte=false, // na verdade eh int, p cubos especiais: fornalhas, trigo crescendo, baus, etc
				opForceImpCma=false, // usei no painel solar e em ladeiras
				opAcidoCorroi=true, // tem q tomar cuidado com isso. No geral, rochas, areias e cubos cel sao imunes a acido. Marmores e tijolos de marmore sao imunes tb.
				opLadNor=false, // ladeiras
				opLadSul=false,
				opLadLes=false,
				opLadOes=false,
				extComMao=false,
				extComPah=false,
				extComColPed=false, // colher de pedreiro
				extComFoice=false,
				extComPic=false,
				extComMach=false,				
				extComChBoca=false,				
				extComChFenda=false,				
				ehSeedMob=false, 
				ehMadeira=false,
				ehGrama=false,// usei como flag p mao
				ehAreia=false,// usei p provocar queda de  a r e i a s
				ehCaule=false,// usei p  p a c k s, os ai em baixo tb
				ehFolha=false,// 
				ehGelo=false,// 
				ehTijolo=false,// 
				ehMinerio=false,// 
				ehEmbrulho=false, // ajudou p shape da mao, eh o saquinho de semente
				ehTanque = false,
				ehCano = false,
				ehConCano = false,
				ehLiqCano = false,
				ehSwitchOn=false,// p mecanismo de acionamento manual, vale p varios switchs
				ehSwitchOff=false,
				ehBotaoOn=false,// p mecanismo de acionamento manual, vale p varios switchs
				ehBotaoOff=false,				
				ehEsteira=false,				
				ehVator=false,
				ativo=false, // p mobs
			ehConEl=false,
				opAlfaInf20=false, // p tanques de gas
				opAltInfTanq=false, // usei no interior de tanques, indica q a altura desse cubo usarah o valor de inf 0..20
				opAltInfCano=false, // usei no interior de canos, indica q a altura desse cubo usarah o valor de inf 0..6
				opOscTanq=false, // p posImp com oscilacao de onda dentro do tanque
				opLuzInf20=false, // usei em c o n d  (q foi removido, mas dah p usar p outra coisa)
				opLuzLife1000=false, // usei em redes eletricas
				opTreme=false, // usei no tnt
				opVatorPodeMover=true,
				opJoePodeMover=true,
				opQtdEhSaude=false,
					operavel=false, // IMPORTANTE, um bloco soh passarah por  r e g C u b () se isso aqui for TRUE, CUIDADO!!!
				opAreiaCai=false, // p areia e similares, aglomera varios tipos de areia
				opFazRio=false, // soh p fluidos
				opFazSombra=true,
				opLuzAgua=false, // sombreamento p agua e similares
				opLuzPortal=false,
				opGirDtCen=false,
				opEhPortal=false, // aciona o som de portal quando proximo
				ehTransp=false,  //  t r a n s p o r t a d o r e s 
				ehConTransp=false, 
				ehMob=false, 
				opImpLife=true, // CUIDADO! p a c k s nao precisam de life impresso
				ehPack=false, // nao misture as coisas
				ehGhostBlock=false, // p trigo e similares
				opBounceCma=false, // efeito de agua na face superior
				opBounceCen=false, // usei na  i s c a 
				opBounceFaces=false, // efeito folhas ao vento
				opEhMaqInt=false, // soh p maquinas com interface, aquelas novas
				opCresce100=false,
				opAutoGet=false,// p pegar quando passar por cima
				opGetComClick=false,
				atrasDoOlho=false,
				foraDaTela=false,
				ocultador=true; // ferramentas e meio-bloco serao false aqui
			
			JPal
				crCma=null, crBxo=null, 
				crNor=null, crSul=null, 
				crLes=null, crOes=null,
				crDtCen=null,
				crDtCma=null, crDtBxo=null, 
				crDtNor=null, crDtSul=null, 
				crDtLes=null, crDtOes=null,
				
				crDtNorr=null, crDtSull=null, 
				crDtLess=null, crDtOess=null;
				
			float 
				iCal=0, // incremento do calibre, todas as faces laterais expandidas, mas dah p murchar tb com valor negativo aqui
				iCalCB=0, // idem, mas apenas p face superior e inferior
				tp = 0.5f, // o cubo terah 1f de altura como padrao, da face abaixo ateh a superior, logo, 0.5f de raio
				tn = tp, ts=tp, tl=tp, to=tp, tc=tp, tb=tp,
				ic=0, ib=0, in=0, is=0, il=0, io=0, // incrementos p cada face, foi melhor separar de t@
				ipLO=0, ipNS=0, // efeito incremento piramide, p efeito abaixo, basta alterar o calibre e aplicar isso.
				dx=0, dy=0, dz=0, // gancho p deslocamento de mobs. Os mobs manipularao essas vars quando andarem.
				dxx=0, dyy=0, dzz=0, // CUIDADO! nao usei isso nos mobs, soh desloca  d t C e n. Usei no  t r a n s p o r t a d o r .
				xImp=0, yImp=0, zImp=0; // esses 3 sao internos, alterados a cada chamada de impressao. Usei no mecanismo p evitar passagem de floats nos parametros. Acho q isso deve economizar clock (serah???)
			Jf1 ico = icoPadrao;
			J3d
				dtCen = null,
				dtCma = null, dtBxo = null,
				dtNor = null, dtSul = null,
				dtLes = null, dtOes = null, // ?serah q precisa de cma e bxo???
				dtNorr = null, dtSull = null,
				dtLess = null, dtOess = null;				
			InfImp infImp = null;	// esse eh interno
			int
				tip=0, tipp=0, tippp=0, // automatico
				hit=0, dir=0, cnt=0, 
				cct=0, // contraste do contorno. Zero economiza clock
				luzPortal=0, //esse eh interno
				potCalor=0, // use setPotCalor()
				cmaNOx=0, cmaNOy=0, 
				cmaNLx=0, cmaNLy=0, 
				cmaSLx=0, cmaSLy=0, 
				cmaSOx=0, cmaSOy=0,
					bxoNOx=0, bxoNOy=0, 
					bxoNLx=0, bxoNLy=0, 
					bxoSLx=0, bxoSLy=0, 
					bxoSOx=0, bxoSOy=0;
					
		public Cub(){ 
			lastCub = this;		
		}								
		public Cub(JPal cr){ 
			crCma = crBxo = crNor = crSul = crLes = crOes = cr;
			lastCub = this;					
		}			
		public Cub(Color cr){
			crCma = crBxo = crNor = crSul = crLes = crOes = addPal(cr);
			lastCub = this;									
		}					
		public Cub(Color crc, Color crl){
			crCma = addPal(crc);
			crBxo = crNor = crSul = crLes = crOes = addPal(crl);
			lastCub = this;					
		}							

		public void zDxx(){
			dxx=0; dyy=0; dzz=0;
		}
		public void ajPals(){
			// otimizar isso depois. Tah  p o r c o;
			int crj = 0;
			if(dtCen!=null)
			for(J3d.Triang t: dtCen.tri){
				crj = t.crt;  // 76
				crj-= crj%10; // 70
				crj+=9;       // 79
				t.pal = addPal(crj);
			}			
			if(dtNor!=null)
			for(J3d.Triang t: dtNor.tri){
				crj = t.crt;  // 76
				crj-= crj%10; // 70
				crj+=9;       // 79
				t.pal = addPal(crj);
			}
			if(dtSul!=null)
			for(J3d.Triang t: dtSul.tri){
				crj = t.crt;  // 76
				crj-= crj%10; // 70
				crj+=9;       // 79
				t.pal = addPal(crj);
			}			
			if(dtLes!=null)
			for(J3d.Triang t: dtLes.tri){
				crj = t.crt;  // 76
				crj-= crj%10; // 70
				crj+=9;       // 79
				t.pal = addPal(crj);
			}			
			if(dtOes!=null)
			for(J3d.Triang t: dtOes.tri){
				crj = t.crt;  // 76
				crj-= crj%10; // 70
				crj+=9;       // 79
				t.pal = addPal(crj);
			}			
			if(dtCma!=null)
			for(J3d.Triang t: dtCma.tri){
				crj = t.crt;  // 76
				crj-= crj%10; // 70
				crj+=9;       // 79
				t.pal = addPal(crj);
			}			
			if(dtBxo!=null)
			for(J3d.Triang t: dtBxo.tri){
				crj = t.crt;  // 76
				crj-= crj%10; // 70
				crj+=9;       // 79
				t.pal = addPal(crj);
			}			

			if(dtNorr!=null)
			for(J3d.Triang t: dtNorr.tri){
				crj = t.crt;  // 76
				crj-= crj%10; // 70
				crj+=9;       // 79
				t.pal = addPal(crj);
			}
			if(dtSull!=null)
			for(J3d.Triang t: dtSull.tri){
				crj = t.crt;  // 76
				crj-= crj%10; // 70
				crj+=9;       // 79
				t.pal = addPal(crj);
			}			
			if(dtLess!=null)
			for(J3d.Triang t: dtLess.tri){
				crj = t.crt;  // 76
				crj-= crj%10; // 70
				crj+=9;       // 79
				t.pal = addPal(crj);
			}			
			if(dtOess!=null)
			for(J3d.Triang t: dtOess.tri){
				crj = t.crt;  // 76
				crj-= crj%10; // 70
				crj+=9;       // 79
				t.pal = addPal(crj);
			}			
			
		}
		

		public Cub setColor(int cr){
			return setColor(J.cor[cr]);
		}
		public Cub setColor(Color cr){
			crCma = crBxo = crNor = crSul = crLes = crOes = addPal(cr);			
			return this;
		}
		public Cub setCrBxo(int cr){
			crBxo = addPal(cr);
			return this;
		}
		public Cub setCrCma(int cr){
			crCma = addPal(cr);
			return this;
		}
		public Cub setCrCma(Color cr){
			crCma = addPal(cr);
			return this;
		}		
		public Cub setCrCmaBxo(int cr){
			return setCrCmaBxo(J.cor[cr]); // melhor centralizar no metodo abaixo
		}
		public Cub setCrCmaBxo(Color cr){
			crCma = addPal(cr);
			crBxo = addPal(cr);
			return this;
		}
		public Cub setExtCom(String st){
			boolean foi=false;
			if(J.tem('0',st))	{ extComMao=true; foi=true;}
			if(J.tem('a',st))	{extComPah=true; foi=true;}
			if(J.tem('c',st))	{extComColPed=true; foi=true;}// colher de pedreiro
			if(J.tem('f',st))	{extComFoice=true; foi=true;}
			if(J.tem('p',st))	{extComPic=true; foi=true;}
			if(J.tem('m',st))	{extComMach=true; foi=true;}
			if(J.tem('b',st))	{extComChBoca=true; foi=true;}
			if(J.tem('e',st))	{extComChFenda=true; foi=true;}
			if(!foi) J.impErr("!ferramenta nao definida: |"+st+"|", "ss Cub.setExtCom()");
			return this;
		}
		public Cub setOption(String st){
			if(J.tem('L',st)){ // usei p agua
				crNor=null;
				crSul=null;
				crLes=null;
				crOes=null;
				crBxo=null;
			}
			return this;
		}
		public Cub setWav(String st){
			// 0 = plot; 1=pass
			if(st.length()!=2) 
				J.impErr("!erro no tamanho ("+st.length()+")do parametro para o cubo |"+name+"|", "Cub.setWav()");
			switch(st.charAt(0)){
				case ' ': break;
				case 'l': wPlot = wPlotLama; break;
				case 'r': wPlot = wPlotRocha; break;
				case 'm': wPlot = wPlotMadeira; break;
				case 'a': wPlot = wPlotAreia; break;
				case 't': wPlot = wPlotTerra; break;
				case 'g': wPlot = wPlotAgua; break;
				case 'f': wPlot = wPlotFolha; break;
				default: J.impErr("!som char0 nao indexado: |"+st.charAt(0)+"|","Cub.setWav()");
			}
			// 1 = pass;
			// melhor se possuisse 3 variacoes
			switch(st.charAt(1)){
				case ' ': break;
				case 'l': wPass = wPlotLama; break;
				case 'r': wPass = wPlotRocha; break;
				case 'm': wPass = wPlotMadeira; break;
				case 'a': wPass = wPlotAreia; break;
				case 't': wPass = wPlotTerra; break;
				case 'g': wPass = wPlotAgua; break;
				case 'f': wPass = wPlotFolha; break;
				default: J.impErr("!som char1 nao indexado: |"+st.charAt(1)+"|","Cub.setWav()");
			}
			return this;
		}		
		public Jf1 getIcon(){
			return ico;
		}
		public Cub setIcon(Jf1 f){
			ico = f;
			return this;
		}
		public Cub setIcon(String cam, int cr15, int cr7, int cr9, int cr1, int cr8){
			ico = new Jf1(cam);
			ico.trocaCores(cr15,cr7,cr9,cr1,cr8);
			return this;			
		}		
		public Cub setIcon(String cam, int tin){
			ico = new Jf1(cam);
			ico.tingir(99,tin); // pressupoe a cor 99
			return this;			
		}
		public Cub setIcon(String cam){
			ico = new Jf1(cam);
			return this;
		}
		public Cub setCozido(String st){
			stCozido = st;
			return this;
		}
		public Cub setPotCalor(int p){
		/*	PARAMETROS:
				0- nenhum
				1- pifio
				2- baixo
				3- medio
				4- alto
				5- gigante
				6- infinito
				sistematizei assim p facilitar ajustes gerais sem precisar reexaminar cubo por cubo
		*/			
			potCalor = p*20;
			if(p==6) potCalor=32000;
			return this;
		}
		public Cub inflar(float p){
			iCal = p;
			iCalCB = p;
			return this;
		}		
		public Cub setIncCal(float p){ // setIcal setCal
			// veja q todo cubo tem extencao 1f de face a face
			// do centro ate a face lateral tem-se 0.5f q eh a distancia padrao (tp, de tamanho padrao) do centro ateh cada face
			iCal = p;
			return this;
		}
		public Cub setDtCen(String cam, int cr){
			setDtCen(cam, J.cor[cr]);
			// puxei isso de  a d d T r i g o ()
			// pena q nao funciona com Color no parametro. Talvez eu arrume depois
			int crj = 0, vr=0;
			for(J3d.Triang t: dtCen.tri){
				vr = t.crt%10;// 6
				crj = t.crt;  // 76
				crj-= crj%10; // 70
				crj+=9;       // 79
				if(cr!=0)	t.pal = addPal(J.altColor(J.cor[cr],(int)(-vr*10)));
				else	t.pal = addPal(crj);
			}						
			return this;
		}
		public Cub setDtCen(String cam, Color cr){
			crDtCen = addPal(cr); // nao adicionara a paleta se a cor jah existir.
			float r = 0.01f; // resize
			dtCen = new J3d(cam, r);			
			return this;
		}
		public Cub setDtCen(String cam, Color cr, String op){
			// serah q tá funcionando bem???
			dtCen = new J3d(cam, 0.01f,op);
			dtCen.reg3d();		
			crDtCen = addPal(49); // precisa disso senao buga
			if(cr!=null) crDtCen = addPal(cr);			
			// autoAjuste de paletas

			int crj = 0, vr=0;
			for(J3d.Triang t: dtCen.tri){
				vr = t.crt%10;// 6
				crj = t.crt;  // 76
				crj-= crj%10; // 70
				crj+=9;       // 79
				if(cr!=null)	t.pal = addPal(J.altColor(cr,(int)(-vr*10)));
				else	t.pal = addPal(crj);
			}
			return this;
		}		
		public Cub setDtCma(String cam, int cr){
			return setDtCma(cam, J.cor[cr]);
		}
		public Cub setDtCma(String cam, Color cr){
			crDtCma = addPal(cr); // nao adicionara a paleta se a cor jah existir.
			float r = 0.01f; // resize
			dtCma = new J3d(cam, r);			
			return this;
		}
		public Cub setDtBxo(String cam, int cr){
			return setDtBxo(cam,J.cor[cr]);
		}
		public Cub setDtBxo(String cam, Color cr){
			crDtBxo = addPal(cr); // nao adicionara a paleta se a cor jah existir.
			float r = 0.01f; // resize
			dtBxo = new J3d(cam, r);			
			return this;			
		}		
		public Cub setDtLat(String cam, int cr){
			return setDtLat(cam,J.cor[cr]);
		}
		public Cub setDtLat(String cam, Color cr){
			return setDtLat(cam,cr,null,null);
		}
		public Cub setDtLat(String cam, int cr, String camm, int crr){
			return setDtLat(cam, J.cor[cr], camm, J.cor[crr]);
		}
		public Cub setDtLat(String cam, Color cr, String camm, Color crr){
			// crDtNor = crDtSul = 
			// crDtLes = crDtOes = addPal(49); 			
			
			// crDtNorr = crDtSull = 
			// crDtLess = crDtOess = addPal(49); 						
			if(cr!=null){
				crDtNor = crDtSul = 
				crDtLes = crDtOes = addPal(cr); 
			} 
			if(crr!=null){
				crDtNorr = crDtSull = 
				crDtLess = crDtOess = addPal(crr); 
			} 			
			
			float r = 0.01f; // resize
			dtNor = new J3d(cam, r);
			dtSul = new J3d(cam, r, "s");
			dtLes = new J3d(cam, r, "l");
			dtOes = new J3d(cam, r, "o");			

			if(camm!=null){
				dtNorr = new J3d(camm, r);
				dtSull = new J3d(camm, r, "s");
				dtLess = new J3d(camm, r, "l");
				dtOess = new J3d(camm, r, "o");			
			}
			
			ajPals();			
			
			
			return this;
		}
		
		public Cub setDtNor(String cam, int cr){
			return setDtNor(cam,J.cor[cr],null,null);
		}
		public Cub setDtNor(String cam, Color cr){
			return setDtNor(cam,cr,null,null);
		}		
		public Cub setDtNor(String cam, Color cr, String camm, Color crr){
			if(cr!=null){
				crDtNor = addPal(cr); 
			} 
			if(crr!=null){
				crDtNorr = addPal(crr); 
			} 			
			
			float r = 0.01f; // resize
			dtNor = new J3d(cam, r);

			if(camm!=null){
				dtNorr = new J3d(camm, r);
			}
			
			ajPals();			
			
			
			return this;
		}

		public Cub setDtSul(String cam, int cr){
			return setDtSul(cam,J.cor[cr],null,null);
		}
		public Cub setDtSul(String cam, Color cr){
			return setDtSul(cam,cr,null,null);			
		}
		public Cub setDtSul(String cam, Color cr, String camm, Color crr){
			if(cr!=null){
				crDtSul =  addPal(cr); 
			} 
			if(crr!=null){
				crDtSull = addPal(crr); 
			} 			
			
			float r = 0.01f; // resize
			dtSul = new J3d(cam, r, "s");

			if(camm!=null){
				dtSull = new J3d(camm, r, "s");
			}
			
			ajPals();			
			
			
			return this;
		}

		
		public Cub setDtLes(String cam, int cr){
			return setDtLes(cam,J.cor[cr],null,null);
		}
		public Cub setDtLes(String cam, Color cr){
			return setDtLes(cam,cr,null,null);
		}
		public Cub setDtLes(String cam, Color cr, String camm, Color crr){
			if(cr!=null)
				crDtLes = addPal(cr); 
			if(crr!=null)
				crDtLess = addPal(crr); 			
			
			float r = 0.01f; // resize
			dtLes = new J3d(cam, r, "l");

			if(camm!=null)
				dtLess = new J3d(camm, r, "l");
			ajPals();						
			return this;
		}

		public Cub setDtOes(String cam, int cr){
			return setDtOes(cam,J.cor[cr],null,null);
		}
		public Cub setDtOes(String cam, Color cr){
			return setDtOes(cam,cr,null,null);
		}
		public Cub setDtOes(String cam, Color cr, String camm, Color crr){
			if(cr!=null)
				crDtOes = addPal(cr); 
			if(crr!=null)
				crDtOess = addPal(crr);
			
			float r = 0.01f; // resize
			dtOes = new J3d(cam, r, "o");

			if(camm!=null)
				dtOess = new J3d(camm, r, "o");
			ajPals();						
			return this;
		}


// APENAS P MOB... (nem sempre, veja abaixo)
				boolean 
					aniVem=false,
					jahFoi=false; // tratando bug
				int 
					cntMob=0, 
					life=100, // sempre de 0..100, mas gambiarrei tb de redes el p mostrar o nivel dela
					tmpDano=6, // intervalo em q o dano eh calculado, tanto pisando quanto em paredes q dao hurt
					cTreme=0; // tem opTreme tb, mas usei soh no tnt
				J3d.Ponto pontoLife=null, pontoLifee=null;
				String mobMsg=null; // mob falando por escrito, eh soh mudar isso p aparecer a mensagem
				float 
					ang = 0, // angulo do mob, 0 = norte
					tamPeh = 0.12f; // 0.06f no peh eh o m i n i m o p q o mob consiga transpor barrancos de 1 de altura com pulo. Quanto maior o peh, mais rapido anda.
			public int rndDrop(String p, String pp){
				if(J.B(2)) return getInd(p); // tah certo isso??? Mente cansada.
				return getInd(pp);
			}
			public int rndDrop(String p, String pp, String ppp){
				switch(J.R(3)){
					case 0: return getInd(p);
					case 1: return getInd(pp);
					case 2: return getInd(ppp);
				}
				J.impErr("!randomizacao errada","rndDrop(p,pp,ppp)");
				return 0;
			}			
			public int rndDrop(String p, String pp, String ppp, String pppp){
				switch(J.R(4)){
					case 0: return getInd(p);
					case 1: return getInd(pp);
					case 2: return getInd(ppp);
					case 3: return getInd(pppp);
				}
				J.impErr("!randomizacao errada","rndDrop(p,pp,ppp,pppp)");
				return 0;
			}			
			public void mobDropaItem(int xa, int ya, int za, int xt, int yt, int zt){ // verDrop regDrop
				// melhor centralizar aqui q definir um por um em  i n i C u b s(). Aqui tah agrupado e dah p comparar melhor.
				// isso eh acionado pelo ataque do joe, beeeem lah p baixo
				// o certo eh q o drop vire um pack, mas terah q ajustar p itens nao-bloco.
				int t = rndSemente();
				if(tipp==tAranha) t=getInd("presas"); // teia depois
				
				if(tipp==tVaca) t=getInd("carne");
				if(tipp==tPorco) t=getInd("carne");
				if(tipp==tCavalo) t=getInd("carne"); // randomizar mais itens depois
				if(tipp==tOvelha) t=getInd("carne");
				if(tipp==tAvestruz) t=getInd("carne");
				
				if(tipp==tAldeao) t=0; 
				if(tipp==tAldea) t=0; 

				if(J.B(3)){ // sobrepoe sementes
					if(tipp == tBruxa) t = rndDrop("veneno", "pocao_life");
					if(tipp == tCaveira) t = rndDrop("osso", "pocao_life", "cranio");
					if(tipp == tZumbi) t = rndDrop("braco_zumbi", "pocao_life", "cabeca_zumbi","veneno");
					if(tipp == tAranha) t = rndDrop("veneno", "pocao_life", "presas");
					if(tipp == tCreeper) t = rndDrop("polvora", "pocao_life", "cabeca_creeper"); // cabeca = explosao instantanea
				}

/*
SEM_TRIGO
SEM_ALFACE
SEM_REPOLHO
SEM_CENOURA
SEM_BETERRABA
SEM_SORTIDA
SEM_ABOBORA
SEM_MELANCIA
SEM_MELAO
SEM_MILHO
SEM_BATATA
TALO_CANA
SEM_MORANGO
*/

				/*
tZumbi=30001, braco de zumbi, cabeca de zumbi, carne podre, larva
tAldea=30002, pao, muita penalidade aqui (melhor nao dropar nada como penalidade. Fazer isto p similares tb)
tAldeao=30003, pao, ferramentas, mas precisa ter penalidade aqui
tBruxa=30004, pocoes; Chapeu e manto magico ia ser legal tb.
			tMago=30005, pocoes. Igual a bruxa. Cajado tb??? Varinha magica? Acho q eh melhor o mago se "do bem" e a bruxa "do mal"
			tGhost=30006, ectoplasma
tMinotauro=30007, presas, armas
			tCreeper=30009, polvora (se comum), alguma pocao ou item magico p craftar se for creeper especial
			tGalinha=30010, carne de galinha, penas (p flechas ou qq)
 tAvestruz=30011, carne, penas?
tPorco=30012, carne
 tVaca= ? , carne, couro
				tJavali(jah tem???): carne de porco, presas
 tCavalo=30013, couro, carne
 tOvelha=30014, la~, carne
			tUrso=30015; couro, presas
				tAranha(jah tem???): presas de aranha(extrair veneno), olho de aranha (p alguma pocao ou veneno)
				tEscorp(jah tem???): presas de escorpiao(extrair veneno), olho de aranha (p alguma pocao ou veneno) (nao eh exatamente "presas". Procurar depois)
				tSnail(jah tem???): concha de, para pocoes 
			*/
				for(int q=0; q<30; q++)
					if(setCubSV(t,0,xaHit+J.RS(2),yaHit,zaHit+J.RS(2),xtHit,ytHit,ztHit))
						return;
			}
					
			public void impLifeMsgMob(){
				if(ehPack) return;
				// tah impreciso, mas funciona
				// imprime a barra de life e a mensagem do mob (juntei life+msg p aproveitar cod)
				int lar = 60, alt = 6;
				if(pontoLife==null){
					pontoLife = dtCen.getPnt("@life");
					pontoLifee = dtCen.getPnt("@@life");
				}
				if(pontoLife==null){
					insPntLife();
					pontoLife = dtCen.getPnt("@life");
					pontoLifee = dtCen.getPnt("@@life");					
				}				

				float fat = 0.03f*(float)(pontoLife.Y-pontoLifee.Y);
				lar = 4+(int)(50*fat);
				alt = 1+(int)(5*fat);
				
				int v = (int)(fat*(life*100)/(50+50-25));
				// lar+lar   100%
				// life				x%
				// x = (life*100)/(60+60)
				
				impBarHbuf(
					v, 
					12, 4, 
					pontoLifee.X-lar,
					pontoLifee.Y-alt,
					pontoLifee.X+lar,
					pontoLifee.Y+alt);
					
				J.bufText(
					pontoLifee.X-lar,
					pontoLifee.Y,
					J.cor[15],
					""+life);		
					
				
				J.bufText(
					pontoLifee.X-lar,
					pontoLifee.Y-16,
					J.cor[15],
					mobMsg);					
					
			}
			public void mobOlhaAng(float an){
				ang=an;
				dtCen.defGiroGeral(-an); 
			}
				int 
					//intMob=PROCURAR_BLOCO; // intencao do mob
					intMob=PASSEAR; // intencao do mob
			public void regIntMob(){
				// jamais passar de um estado para outro por aqui
				// mudancas de estado devem ser feitas unicamente por scp
				switch(intMob){
					case 0: break;
					case PASSEAR: 
						if(J.C(30)) 
							mobOlhaAng(ang+J.RS(100)/100f);
						break;
					case FUGIR_JOE:
						if(J.C(10)) 
							mobOlhaAng(J.angY);
						break;
					case PERSEGUIR_JOE:
						if(J.C(10)) 
							mobOlhaAng(J.angY+dtCen.VOLTA*0.5f);
						break;
					case PACK_PERSEGUE_JOE:{
						ang = J.angY+dtCen.VOLTA*0.5f;
						
					} break;
					case PROCURAR_BLOCO:{
						// depois
						
						
						} break;
					case ALCANCAR_OBJ:{
						if(xaOb==intNull) 
							J.impErr("!objetivo nao definido","regIntMob()");
						if(J.C(10)) 
							rumarMobPara(xaOb,yaOb,zaOb,xtOb,ytOb,ztOb);
						
						if(J.noInt(xa,xaOb-1, xaOb+1))
						if(J.noInt(ya,yaOb-2, yaOb+4))
						if(J.noInt(za,zaOb-1, zaOb+1))
						if(xt==xtOb)
						if(yt==ytOb)
						if(zt==ztOb){
							intMob=0;
							// deve procurar a proxima linha do scp aqui
						}	
						
					} break;

						
				}
				
			}		
			public void rumarMobPara(int xao, int yao, int zao, int xto, int yto, int zto){
				float 
					an = -1f,
					V = -dtCen.VOLTA;				

				// dah p ajustar quando estiver na area vizinha. Eh soh somar ou tirar 9, q eh o tamanho da area
				if(xt>xto)xao-=tamArea;
				if(xt<xto)xao+=tamArea;
				if(zt>zto)zao-=tamArea;
				if(zt<zto)zao+=tamArea;
				
				// CD
				if(an==-1f) if(xao>xa && zao>za) an=V*0.125f;
				// BD
				if(an==-1f) if(xao>xa && zao<za) an=V*0.125f*3f;
				// BE
				if(an==-1f) if(xao<xa && zao<za) an=V*0.125f*5f;
				// CE
				if(an==-1f) if(xao<xa && zao>za) an=V*0.125f*7f;
				// C
				if(an==-1f) if(xao==xa && zao>za) an=0;
				// B
				if(an==-1f) if(xao==xa && zao<za) an=V*0.5f;
				// D
				if(an==-1f) if(xao>xa && zao==za) an=V*0.25f;
				// E
				if(an==-1f) if(xao<xa && zao==za) an=V*0.75f;
				mobOlhaAng(an);				
			}
			public void regPassoGrossoMob(){
			  // deslocar o mob p outra celula da area
				float w = 0.5f; // limites do cubo
				// o problema eh q o mob tah invadindo a impressao do cubo aa sua frente.
				int d = 0;
				if(dz>+w){dz=-w; d=NOR; }
				if(dz<-w){dz=+w; d=SUL; }
				if(dx<-w){dx=+w; d=OES; }
				if(dx>+w){dx=-w; d=LES; }
				
				if(d!=0) 
					if(!moveSV(d, xa,ya,za,xt,yt,zt))
						mobPula();

			}

			public void regPassoFinoMob(){
				if(intMob!=0){ // mas terah outros estados tb, outras intencoes em q o mob permanecerah sem mecher em dx
					// nao daria p otimizar isso abaixo? tag GARGALO.
					float cz = (float)(Math.cos(ang)*tamPeh);
					float cx = (float)(Math.sin(ang)*tamPeh);
					
					dz+=cz;
					dx+=cx;
				}

			}
			public void regAniMob(){
				dtCen.reg3d();
				if(dtCen.semMovAtivo()){
					
					if(tipp==tAranha) {
						float q = dtCen.VOLTA*0.125f;
						aniVem=!aniVem;
						if(aniVem) q = -q;
						dtCen.desFreR("p",q,2);
						dtCen.desFreR("pp",-q,2);
					}

					if(tipp==tZumbi){ // daria p adaptar p aldeao
						float q = dtCen.VOLTA*0.25f;
						int velAni = 6;
						aniVem=!aniVem;
						if(aniVem) q=-q;						
						dtCen.giroXr("legE","@legE",+q,velAni);
						dtCen.giroXr("legD","@legD",-q,velAni);
					}
					if(J.vou(tipp,tAldea,tAldeao,tBruxa,tMago,tGhost,tMinotauro,tCaveira)){
						float q = dtCen.VOLTA*0.25f;
						int velAni = 6;
						aniVem=!aniVem;
						if(aniVem) q=-q;						
						dtCen.giroXr("legE","@legE",+q,velAni);
						dtCen.giroXr("legD","@legD",-q,velAni);
						
						dtCen.giroXr("armE","@armE",-q,velAni);
						dtCen.giroXr("armD","@armD",+q,velAni);						
						
						if(J.B(4)){
						// cabeca, bxo
							dtCen.giroXr("cab","@cab",J.abs(q),6);
						} else if(J.B(2)){
						// cabeca, laterais
							dtCen.opAtrasoo=12; // "oo" eh p retorno
							dtCen.giroYr("cab","@cab",J.rnd(-q*0.5f,+q*0.5f),2);							
						}								
					}
					if(tipp==tCreeper){ // mas serviria p qq quadrupede
						float q = dtCen.VOLTA*0.25f;
						int velAni = 6;
						aniVem=!aniVem;
						if(aniVem) q=-q;						
						dtCen.giroXr("pataTE","@pataT",+q,velAni);
						dtCen.giroXr("pataTD","@pataT",-q,velAni);
						dtCen.giroXr("pataFE","@pataF",-q,velAni);
						dtCen.giroXr("pataFD","@pataF",+q,velAni);						
					}
					if(J.vou(tipp,tPorco,tVaca,tCavalo,tOvelha,tUrso)){
						float q = dtCen.VOLTA*0.25f;
						int velAni = 6;
						aniVem=!aniVem;
						if(aniVem) q=-q;						
						dtCen.giroXr("pataTE","@pataT",+q,velAni);
						dtCen.giroXr("pataTD","@pataT",-q,velAni);
						dtCen.giroXr("pataFE","@pataF",-q,velAni);
						dtCen.giroXr("pataFD","@pataF",+q,velAni);
						if(J.B(3)){
						// cabeca, bxo
							dtCen.opAtrasoo=6; // "oo" eh p retorno
							dtCen.giroXr("cab","@cab",J.abs(q),3);
						} else if(J.B(3)){
						// cabeca, laterais
							dtCen.opAtrasoo=12; // "oo" eh p retorno
							dtCen.giroYr("cab","@cab",J.rnd(-q*0.5f,+q*0.5f),2);
						}								
					}					
					if(J.vou(tipp,tGalinha,tAvestruz)){ 
						float q = dtCen.VOLTA*0.25f;
						int velAni = 3; // esse nome de var nao tah apropriado
						aniVem=!aniVem;
						if(aniVem) q=-q;	
						// patas
						dtCen.giroXr("pataE","@pata",-q,velAni);
						dtCen.giroXr("pataD","@pata",+q,velAni);
						
						// asas
						if(0==getCub(xa,ya-1,za,xt,yt,zt) | J.B(3)){ 
							// nao mover asas junto com a cabeca
							dtCen.giroZr("asaD","@asaD",+J.abs(q+q),2);
							dtCen.giroZr("asaE","@asaE",-J.abs(q+q),2);
						} else if(J.B(3)){
						// cabeca, bxo
							dtCen.giroXr("cab","@cab",J.abs(q+q),2);
						} else if(J.B(3)){
						// cabeca, laterais
							dtCen.opAtrasoo=12; // "oo" eh p retorno
							dtCen.giroYr("cab","@cab",J.rnd(-q*0.5f,+q*0.5f),2);							
						}		
					}
				}
			}
			public void regDanoParede(){
				//VER DANO EM PAREDE E NO CHAO P FUNGO DO NETHER
				//PADRONIZAR NUMA VAR OS CACTOS, P PISAR E ESBARRAR NA PAREDE
				if(J.C(tmpDano))
				if(temCubViz(tCactoBase,tCactoTopo,tCactoTopo, xa,ya,za,xt,yt,zt) // repeti o parametro de proposito
				||temCubViz(tCactoBase,tCactoTopo,tCactoTopo, xa,ya+1,za,xt,yt,zt)){
					// padronizar abaixo, ver dano chao tb, colocar num metodo aa parte depois
					life-=12;
					dtCen.crCint = 15;
					dtCen.cCint = cTreme = 6; // som aqui depois
					if(life<=0) life=100;						
				}				
			}
			public void regDanoChao(){
				if(J.C(tmpDano)) // o certo seria um contador de dano especifico p isso
				if(tCactoTopo==getCub(xa,ya-1,za,xt,yt,zt)){// cactoBase nao espeta
					life-=12;
					dtCen.crCint = 15;
					dtCen.cCint = cTreme = 6; // som aqui depois
					if(life<=0) life=100;
				}
			}
			
			public void setBracoDeZumbi(){
				//if(dtCen==null) return;
				//if(lastCub==null) return;
				//J.sai("333333333333333");
				dtCen.giroX("armE","@armE",lastCub.dtCen.VOLTA*0.25f,1);
				dtCen.giroX("armD","@armD",lastCub.dtCen.VOLTA*0.25f,1);
				dtCen.reg3d();						
			}
			public void insPlts(){
				// ajustando paleta
				int vr=0, crj=0;
				// 77777777777777
				dtCen.defIPalts(0); // sombreamento dor J.cor[], mas aplicado em tri.pal
				int cr = 0;
				for(J3d.Triang t: dtCen.tri){					
					cr = t.crt; // 64
					cr = cr-(cr%10); // 60
					cr+=9; // 69
					t.pal = addPal(cr);
				}
			}
			public void insPntLife(){
				// define o ponto de impressao da barra de life
				// ambos pontos inseridos sao ajustes de posicao e tamanho da barra de acordo com a distancia do player
				float w = dtCen.getPntMaisAlto().y;
				w+=0.25f;
				dtCen.insPnt(0,w,0,"@life");
				w+=0.25f;
				dtCen.insPnt(0,w,0,"@@life");		
			}
				float 
					vyMob=0, 
					opGravMob = 0.0625f,
					lmGravMob = 0.5f;
				int cJumpMob=0;	
			public void mobPula(){ // mobJump jumpMob
				if(getCub(xa,ya-1,za,xt,yt,zt)!=0){ // mas talvez nao seja soh isso
					//wZumbi.play(); // talvez um som de pulo aqui. Deve variar segundo o mob.
					vyMob=+0.25f; // um pulinho baixo q buga menos
					cJumpMob=12;
					move(CMA,xa,ya,za,xt,yt,zt);
				}				
			}	
			public void regJumpMob(){
				if(getCub(xa,ya-1,za,xt,yt,zt)==0){					
					vyMob-=opGravMob;
					if(vyMob>+lmGravMob) vyMob=+lmGravMob;
					if(vyMob<-lmGravMob) vyMob=-lmGravMob;
					
					dy+=vyMob; // melhor q seja "multiplo" de 5
					if(dy<=-0.5f){
						dy=0;
						vyMob=0;
						move(BXO, xa,ya,za,xt,yt,zt);
					} 
				} else if(cJumpMob<=0){
					dy=0;
					vyMob=0;
				}

			}
		public void zMob(){
			// deve liberar o slot com seguranca
			// setCub(0,xa,ya,za,xt,yt,zt);// nao sei se isso eh bom
			ativo=false;
			cntMob=0;
			//life=0;
			//cubCen=null;
			//dtCen=null;			
			//tip=0;
		}
		public void regMob(){
			if(opEliminarMobs){ // esse eh flag de debug
				setCub(0,xa,ya,za,xt,yt,zt);
				zMob();
				return;
			}
			
			
			if(jahFoi) return; // gambiarra contra bug
			

	
			if(cJumpMob>0) cJumpMob--;
			
//			mobMsg = name+" aqui. Blz?";
			
			regIntMob(); // modifica o angulo
			regPassoFinoMob();
			regPassoGrossoMob();
			
			regBotaOvo();
							
			regSomMob();
			regJumpMob();
			regAtaqueCAC();
			regAniMob();
			regDanoChao();
			regDanoParede();
			jahFoi=true;
		}		
		
		public void regAtaqueCAC(){
			float d = distDoJoe();
			if(J.C(6)) if(J.vou(tipp,tVaca,tCavalo,tPorco,tOvelha))	if(distDoJoe()<4){
				mobOlhaAng(J.angY+dtCen.VOLTA*0.5f);					
				if(J.B(3)){
					joeLife-=6;
					iniOculos(J.cor[12], 6);					
					wMorde.play();
				}				
			}
			
			if(J.C(6)) if(J.vou(tipp,tAranha,tAranha))	if(distDoJoe()<4){ // escorpiao depois
				mobOlhaAng(J.angY+dtCen.VOLTA*0.5f);					
				if(J.B(3)){
					joeLife-=12;
					iniOculos(J.cor[12], 6);
					wPeconha.play(); // tava igual da caveira. Jah mudou da caveira???
				}				
			}			
			
			if(J.C(3)) if(tipp==tCreeper) if(d<4){
					switch(tippp){
						case 129: // creeper-peido !!!
							//wExp.play();
							wPeidao.play();
							setCub(0,0, xa,ya,za,xt,yt,zt);
							setCub(0,0, xa,ya+1,za,xt,yt,zt);							
							zMob();
							iniOculos(J.cor[129], 12);
							joeLife-=30;
							trocaEsf("LAMA",4, xa,ya,za, xt,yt,zt);
							break;
						case 79: // creeper-trovao
							wTrovao.play();							
							setCub(0,0, xa,ya,za,xt,yt,zt);
							setCub(0,0, xa,ya+1,za,xt,yt,zt);
							zMob();							
							trocaEsf("chamuscado",3, xa,ya-1,za,xt,yt,zt);
							iniOculos(J.cor[119], 12);
							joeLife-=30;
							break;							
						default: 
							wExp.play();						
							iniExp(xa,ya,za,xt,yt,zt); 
							joeLife-=30;							
							zMob();							
							// nao seria melhor atrasar este processo com um contador???
							break;
					}
			}
		}

		public float distDoJoe(){						
			//return J.dist3d(0,0,0, xImp, yImp,zImp);
			return J.dist3d(
				-J.xCam,J.yCam,-J.zCam, 
				xImp, yImp,zImp);
		}
		public void regSomMob(){
			if(J.C(30)) // mas nao serah tao simples assim, veja q existem eventos q provocam o som.
			if(J.B(10)){
				// creeper nao deve fazer barulho aqui, como no mine oficial... mas teriam os passos
				if(tipp==tUrso) wUrso.play();
				if(tipp==tOvelha) wOvelha.play();
				if(tipp==tCavalo) wCavalo.play();
				if(tipp==tGalinha) wGalinha.play();
				if(tipp==tCaveira) wCaveira.play();
				if(tipp==tAldea) wAldea.play();
				if(tipp==tMago) wMago.play();
				if(tipp==tBruxa) wBruxa.play();
				if(tipp==tGhost) wGhost.play();
				if(tipp==tMinotauro) wMinotauro.play();
				if(tipp==tAranha) wAranha.play(); // escorpiao				
				if(tipp==tPorco) wPorco.play(); // javali???
				if(tipp==tVaca) wVaca.play();
			}
		}
		public void regBotaOvo(){
			if(tipp==tGalinha) // avestruz tb deveria botar. Depois
			if(J.C(100))
			if(J.B(10))
			if(setCubSV("ovo",0,xa+J.RS(2),ya,za+J.RS(2),xt,yt,zt))
				wPop.play();
		}
		// FIM, APENAS P MOB
		
		
		public void aplicarVentoTrigo(float q){
			if(dtCen.semMovAtivo()){
				int p = 4+J.R(3);
				float f = (q+J.R((int)(q+q+q)))/30f;
				dtCen.desFreR("nd1",f,p);
				dtCen.desFreR("nd2",f+f,p);
			} dtCen.reg3d();			
		}

		public int verLuzLife1000(int lif){
			// parametro de 0..1000
			//return (int)((lif%101)/5f)-10;			
			return (int)((lif%1001)/50f)-10;			
		}
		
			int maxRede1=1000;
		public void altRede(int q, int rd, int xa, int ya, int za, int xt, int yt, int zt){
						if(getCub(xa,ya,za-1, xt,yt,zt)==rd) tCub.get(rd).life = J.corrInt(q+tCub.get(rd).life, 0,maxRede1);
			else 	if(getCub(xa,ya,za+1, xt,yt,zt)==rd) tCub.get(rd).life = J.corrInt(q+tCub.get(rd).life, 0,maxRede1);
			else 	if(getCub(xa+1,ya,za, xt,yt,zt)==rd) tCub.get(rd).life = J.corrInt(q+tCub.get(rd).life, 0,maxRede1);
			else 	if(getCub(xa-1,ya,za, xt,yt,zt)==rd) tCub.get(rd).life = J.corrInt(q+tCub.get(rd).life, 0,maxRede1);
			else 	if(getCub(xa,ya+1,za, xt,yt,zt)==rd) tCub.get(rd).life = J.corrInt(q+tCub.get(rd).life, 0,maxRede1);
			else 	if(getCub(xa,ya-1,za, xt,yt,zt)==rd) tCub.get(rd).life = J.corrInt(q+tCub.get(rd).life, 0,maxRede1);			
		}
		public void corrDisEl(){
			
			float q = -0.125f*3;
			float qq=+0.01f;
			infImp.in=q;			infImp.is=q;
			infImp.il=q;			infImp.io=q;
			infImp.ic=q;			infImp.ib=q;
			if(getCubMod(xa,ya,za+1,xt,yt,zt).ehConEl) {infImp.in=qq; infImp.ocNor=true; }
			if(getCubMod(xa,ya,za-1,xt,yt,zt).ehConEl) {infImp.is=qq; infImp.ocSul=true; }
			if(getCubMod(xa+1,ya,za,xt,yt,zt).ehConEl) {infImp.il=qq; infImp.ocLes=true; }
			if(getCubMod(xa-1,ya,za,xt,yt,zt).ehConEl) {infImp.io=qq; infImp.ocOes=true; }
			if(getCubMod(xa,ya+1,za,xt,yt,zt).ehConEl) {infImp.ic=qq; infImp.ocCma=true; }
			if(getCubMod(xa,ya-1,za,xt,yt,zt).ehConEl) {infImp.ib=qq; infImp.ocBxo=true; }
		}
		public void corrDisTransp(){
			float q = -0.125f*3;
			float qq=+0.01f;
			infImp.in=q;			infImp.is=q;
			infImp.il=q;			infImp.io=q;
			infImp.ic=q;			infImp.ib=q;
			if(getCubMod(xa,ya,za+1,xt,yt,zt).ehConCano) {infImp.in=qq; infImp.ocNor=true; }
			if(getCubMod(xa,ya,za-1,xt,yt,zt).ehConCano) {infImp.is=qq; infImp.ocSul=true; }
			if(getCubMod(xa+1,ya,za,xt,yt,zt).ehConCano) {infImp.il=qq; infImp.ocLes=true; }
			if(getCubMod(xa-1,ya,za,xt,yt,zt).ehConCano) {infImp.io=qq; infImp.ocOes=true; }
			if(getCubMod(xa,ya+1,za,xt,yt,zt).ehConCano) {infImp.ic=qq; infImp.ocCma=true; }
			if(getCubMod(xa,ya-1,za,xt,yt,zt).ehConCano) {infImp.ib=qq; infImp.ocBxo=true; }
		}
		public void corrDisCano(){			
			corrDisCano(xa,ya,za,xt,yt,zt);
		}
		public void corrDisCano(int xa, int ya, int za, int xt, int yt, int zt){			
			if(getCubMod(xa,ya,za,xt,yt,zt).ehCano==false) return;		
			float q = -0.125f*3;
			float qq=+0.01f;
			infImp.in=q;			infImp.is=q;
			infImp.il=q;			infImp.io=q;
			infImp.ic=q;			infImp.ib=q;
			if(getCubMod(xa,ya,za+1,xt,yt,zt).ehConCano) {infImp.in=qq; infImp.ocNor=true; }
			if(getCubMod(xa,ya,za-1,xt,yt,zt).ehConCano) {infImp.is=qq; infImp.ocSul=true; }
			if(getCubMod(xa+1,ya,za,xt,yt,zt).ehConCano) {infImp.il=qq; infImp.ocLes=true; }
			if(getCubMod(xa-1,ya,za,xt,yt,zt).ehConCano) {infImp.io=qq; infImp.ocOes=true; }
			if(getCubMod(xa,ya+1,za,xt,yt,zt).ehConCano) {infImp.ic=qq; infImp.ocCma=true; }
			if(getCubMod(xa,ya-1,za,xt,yt,zt).ehConCano) {infImp.ib=qq; infImp.ocBxo=true; }
		}
		public void checkClickReact(){
			//pra cubos q reagem a click
				if(ms.b1)	
				if(cPlot<=0)
				if(xa==xaHit) if(ya==yaHit) if(za==zaHit)
				if(xt==xtHit)	if(yt==ytHit)	if(zt==ztHit)
				if(joeInv.naMao()!=tChFenda){// mas isso eh impreciso...
					corrDisCano();
					opAniMaoGolpe=true;
						
					if(tip==tValvOn){
						setCub(tValvOff,xa,ya,za,xt,yt,zt);
						wSwitchOff.play();						
						cPlot=12;
					} else if(tip==tValvOff){
						setCub(tValvOn,xa,ya,za,xt,yt,zt);
						wSwitchOn.play();						
						cPlot=12;
					} else if(J.vou(tip,tLandMark, tLandMarkArea)){
						joeInv.addItm(2,tLandMark);
						trocaTudo(tLandMark,0); 
						trocaTudo(tLandMarkArea,0);
						xaCtt=INTNULO; // importante
					}
					
					if(ehVator) if(cPlot<=0){
						cPlot=12;
						altVator(xa,ya,za,xt,yt,zt);
					}				
					{ // switchs
						if(ehSwitchOn){
							cPlot=12;
							wSwitchOff.play();
							setCub(tip-1,xa,ya,za,xt,yt,zt);
						}
						if(ehSwitchOff){
							cPlot=12;
							wSwitchOn.play();					
							setCub(tip+1,xa,ya,za,xt,yt,zt);
						}
				}
					{ // botoes
						if(ehBotaoOff){
							cPlot=12;
							wSwitchOn.play();					
							setCub(tip+1,xa,ya,za,xt,yt,zt);
						}
					}				


					if(tip==tTnt)//4444444444444444
					if(joeInv.naMao()!=0)
					if(joeInv.naMao()!=tTnt)
					if(joeInv.naMao()!=tTntOn)
					{
						cPlot=12;
						wFiss.play();
						setCub("tnt_on",xa, ya,za, xt,yt,zt);
					}				
				}			
		}

		public Maq getMaq(int xa, int ya, int za, int xt, int yt, int zt){
			for(Maq mq:maq)
				if(mq.xa==xa)
				if(mq.ya==ya)
				if(mq.za==za)
				if(mq.xt==xt)
				if(mq.yt==yt)
				if(mq.zt==zt)
					return mq;
			return null;	
		}


		public void zCub(){
			setCub(0,0, xa,ya,za,xt,yt,zt);
		}
		public void regCub(){// reg() ccccccccccccccccccccccccc
			temPosImp=false;
			if(cubCen!=null) temPosImp=true; 
			// laterais sao manuais, p nao atrapalhar o clock

  		// GARGALO
			// substituir isso tudo por classes derivadas de ssCub e chamar um "exec()" (melhor se for um "regCub()" da sub classe) sem passar por este monte de ifs
			// fazer isso aos poucos [[?será q eu faço mesmo??? O cod tá GIGANTE (=dificil manutenção). É o maior fonte q eu já programei a minha vida inteira e... acho q nao vou fazer isso nao]]
			// "operavel" deverah entao desaparecer
			
/*
[]
[][]cano out gas
[]
[][]cano out combust
[]
[][]cano out resina
[]
[][]cano in petroleo
[]combustor []canoPetro/comb+switch

*/			
			
			if(opEhMaqInt){
				if((J.cont+xa+ya+za) % tmpRede == tmpDecRede){
					Maq mq = getMaq(xa,ya,za,xt,yt,zt);
					
					if(mq!=null)
					if(mq.ligado)
					if(mq.el>0){						
				
						{ // escoando producao
							if(mq.pd.tip!=0)
							if(mq.pd.qtd!=0)
							if(ehEsse(tTranspVazio,xa,ya-1,za,xt,yt,zt))
							if(setTransp(mq.pd.tip, xa,ya-1,za,xt,yt,zt)){
								mq.el--; // ?soh isso???
								mq.pd.qtd--;
								if(mq.pd.qtd<=0) mq.pd.tip=0;
								// ?som aqui???
							}
						}
						
						{ // realimentando com mp							
							Cub c = getCubMod(xa,ya+1,za,xt,yt,zt);							
							if(c.ehTransp)
							if(c.tipp!=0)
							if(J.vou(mq.mp.tip,0,c.tipp)) // limitacao de qtd no slot??? Nao poderia ser ilimitado. Depois;
							{
								c.ativo=false; // acho q eh bom por isso.
								mq.el--; // ?soh isso???
								mq.mp.tip=c.tipp;
								mq.mp.qtd++;
								setCub(tTranspVazio,xa,ya+1,za,xt,yt,zt);
								// ?som aqui???
							}
						}
						
					}
				}			
			}
			if(tip==tSeedDesceCol){ // usei p "alicerce" de ctx, mas deve servir p outras coisas tb.
			/*
				se for degrau, vira normal
				se for grama vira terra
			*/
				if((J.cont+xa-ya+za)%6==0){
					int vb = getCub(xa,ya-1,za, xt,yt,zt);
					if(J.vou(vb,0,tAgua)) 
						setCub(tip, xa,ya-1,za, xt,yt,zt);
					
					// pequeno filtro. Mais depois, se precisar. // NAO FUNCIONOU
					//if(getCubMod(vb).ehDegrau) vb = semDegrau(vb);
					//if(vb==tGrama) vb = getInd("terra"); 
					
					else if(vb!=tSeedDesceCol)
						trocaCubCol(
							getCubMod(tip).name,
							getCubMod(vb).name, 
								0, 32, // raio e altura
								xa,ya,za, xt,yt,zt); 
				}
				return;
			}
			if(tip==tTemp){
				// bloco temporário p trabalhar com seeds
				if(cPlot<=0) setCub(0,xa,ya,za,xt,yt,zt);
				return;
			}
			if(tip==tSeedPlatV){
				impSts("dica: ajuste o raio com CTRL+WHEEL");				
				setCub(0, xa,ya,za,xt,yt,zt);
				int v = getCub(xa,ya-1,za,xt,yt,zt);
				int vi = getInf(xa,ya-1,za,xt,yt,zt);
				setPlatRai(v,vi, opNumMultPlot, xa,ya-1,za, xt,yt,zt);
				setPlatRai(0,0, opNumMultPlot-1, xa,ya-1,za, xt,yt,zt);
				return;
			}
			if(tip==tSeedSemAlga){
				setCub(0,xa,ya,za,xt,yt,zt);
				trocaTudo("alga","temp");				
				return;
			}
			if(tip==tSeedSemAgua){
				setCub(0,xa,ya,za,xt,yt,zt);
				trocaTudo("agua","temp");
				return;
			}
			if(tip==tSeedPlat){
				impSts("dica: ajuste o raio com CTRL+WHEEL");				
				setCub(0, xa,ya,za,xt,yt,zt);
				int v = getCub(xa,ya-1,za,xt,yt,zt);
				int vi = getInf(xa,ya-1,za,xt,yt,zt);
				setPlatRai(v,vi, opNumMultPlot, xa,ya-1,za, xt,yt,zt);
				return;
			}
			

			
			if(tip==tSeedDelTipAbaixo){		
				impSts("dica: ajuste o raio com CTRL+WHEEL");
				setCub(0, xa,ya,za,xt,yt,zt);
				int r = opNumMultPlot;				
				trocaCubRai(getCubMod(xa,ya-1,za,xt,yt,zt).name,null,r, xa, ya, za, xt,yt,zt);
				return;
			}
			if(tip==tLandMark) if(J.C(100)) {
				int lm = 32;				
				
				if(xaCtt==INTNULO){
					for(int m=-lm; m<=+lm; m++)
					for(int n=-lm; n<=+lm; n++)
					for(int o=-lm; o<=+lm; o++)
						if(!((m==0)&&(n==0)&&(o==0)))
						if(getCub(xa+m,ya+n,za+o, xt,yt,zt)==tLandMark){
							xaCtt=xa; yaCtt=ya; zaCtt=za; 
							xxaCtt=xa+m; yyaCtt=ya+n; zzaCtt=za+o;							
							xtCtt=xt; ytCtt=yt; ztCtt=zt;
							J.esc("................ area encontrada: "+yyaCtt);							
							return;
						}
				}
				if(xaCtt!=INTNULO){
					
					// corrigindo ordem
					if(xaCtt>xxaCtt) { int w=xaCtt; xaCtt=xxaCtt; xxaCtt=w; }
					if(yaCtt>yyaCtt) { int w=yaCtt; yaCtt=yyaCtt; yyaCtt=w; }
					if(zaCtt>zzaCtt) { int w=zaCtt; zaCtt=zzaCtt; zzaCtt=w; }

					int alt=yaCtt; boolean limpa=true;
					for(int n=yaCtt; n<=lm; n++){ // camada a camada; a primeira totalmente nula é o limite
						limpa = true;
						for(int m=xaCtt; m<=xxaCtt; m++)
						for(int o=zaCtt; o<=zzaCtt; o++)
							if(limpa)
							 if(getCub(m,n,o, xtCtt, ytCtt, ztCtt)!=0)
								 limpa=false;
						if(limpa) yyaCtt = n;
						if(limpa) break;
					}
					J.esc("................ altura: "+yyaCtt);
					trocaCubArea("land_mark",null,  xaCtt, yaCtt, zaCtt, xxaCtt, yyaCtt, zzaCtt, xtCtt, ytCtt, ztCtt);
					trocaCubArea(null, "land_mark_area",xaCtt, yaCtt, zaCtt, xxaCtt, yyaCtt, zzaCtt, xtCtt, ytCtt, ztCtt);
					// saveCtxLandMark depois, por teclado
					return;
				}
			}
			if(tip==tLampadaOn) if(J.C(100)){ // soh p quando for carregada de um arq are.
				setLuzEsf(20, xa, ya, za, xt, yt, zt);			
				return;
			}
			if(tip==tSeedSeringueira){ 
				setCub(0,xa,ya,za,xt,yt,zt);
				plotArv("folha_seringueira",xa,ya,za,xt,yt,zt);
				return;
			}
			if(tip==tCauleSeringueiraVazia){ 
				if(J.C(100)) 
				if(opGrowFast || J.B(20))
				if(ehEsse("caule_seringueira",xa,ya-1,za,xt,yt,zt))
				if(ehEsse("caule_seringueira",xa,ya+1,za,xt,yt,zt))
				if(temCubViz("folha_seringueira",xa,ya+2,za,xt,yt,zt) 
					|| temCubViz("folha_seringueira",xa,ya+3,za,xt,yt,zt) 
					|| temCubViz("folha_seringueira",xa,ya+4,za,xt,yt,zt)){
						setCub("caule_seringueira_cheia",xa,ya,za,xt,yt,zt);
						wGrowTrigo.play();
					}
				return;
			}
			if(tip==tCauleSeringueiraCheia){ 
				if(J.C(100)) 
				if(opGrowFast || J.B(20)) // poderia variar segundo as estacoes do ano
				if(ehEsse("caneca_latex_vazia",xa,ya,za+1,xt,yt,zt)){ // norte z +, conforme convencao
						setCub("caule_seringueira_vazia",xa,ya,za,xt,yt,zt);
						setCub("caneca_latex_cheia",xa,ya,za+1,xt,yt,zt);
						wPlotLama.play(); 
					}
				return;
			}
			if(tip==tCachoBanVerde){
				if((J.cont+xa+ya+za)%120==0)
				if(J.B(20) || opGrowFast)									
					setCub("CACHO_BANANA_MADURA", xa,ya,za,xt,yt,zt);
				return;
			}
			if(tip==tValvOn){
				// se no tempo certo... [[PRECISEI FAZER UM ALGORITMO... FUNCIONOU]]
				if((J.cont+xa+ya+za)%21==0) for(int q=0; q<20; q++){ // 20 tentativas. Se conseguir uma já sai.
					//   randomizar fonte, se for cano com conteudo
					//   randomizar dest, se for cano mesmo se vazio
					int df = J.rnd(J.NOR, J.SUL, J.LES, J.OES, J.BXO); // acima nao.
					int dd = J.rnd(J.NOR, J.SUL, J.LES, J.OES, J.BXO);
					if(df==dd) continue;
					int tf = getCubDir(df, xa,ya,za,xt,yt,zt);
					int td = getCubDir(dd, xa,ya,za,xt,yt,zt);
					if(getCubMod(tf).ehCano==false) continue;
					if(getCubMod(td).ehCano==false) continue;
					if(conteudo(tf)==0) continue;
					
					//   se niv da fonte > niv dest
					int nf = getInfDir(df, xa,ya,za,xt,yt,zt);
					int nd = getInfDir(dd, xa,ya,za,xt,yt,zt);
					if(nf>nd){
					//     entao transferir fonte->dest (transformar canos vazios se necessario)
						if(td==tCanoVazio) setCubDir(dd, canoDe(conteudo(tf)),xa,ya,za,xt,yt,zt);
						decInfDir(df, xa,ya,za,xt,yt,zt);
						incInfDir(dd, xa,ya,za,xt,yt,zt);
						//wPop.play();
						return;
					}
					J.esc("-=-=-=-=-=--=- VALVULA nao conseguiu acionar nada no loop de numero "+q);
				}
				/*	O certo seria:
									pegar uma das 4 dir randomicas+abaixo e tirar da maior passando p de menor nivel
									nao pegar a de cima pq o shape fica zuado
									veja q o nivel do cano de baixo pode ser maior q o dos lados, logo, a agua deve subir
									esquecer tanques por enquanto, fazer soh canos agora
				*/
				/*	VALVULAS: Pensei em passar fluidos/gazes p:
							- tanqueLiq|valvula->cano
							- cano<->valvula<->cano
							- tanqueGaz|valvula->cano
							- agua|vidro|valvula->cano
							- agua|valvulaSubmersa->cano
						É interessante q só passe em um sentido... mas nao sei se fica prático programar assim
				*/

			}				
			if(tip==tBanVerde){
				if((J.cont+xa+ya+za)%120==0)
				if(J.B(20) || opGrowFast)					
					setCub("BANANA_MADURA", xa,ya,za,xt,yt,zt);
				return;
			}			
			if(tip==tSeedColuna){ 
				setCub(0, xa,ya,za,xt,yt,zt);
				int vb = getCub(xa,ya-1,za,xt,yt,zt);
				int vi = getInf(xa,ya-1,za,xt,yt,zt);
				for(int nn=0; nn<opNumMultPlot; nn++)
					if(!setCubSV(vb,vi,xa,ya+nn,za, xt,yt,zt))
						return;
				return;
			}
			if(tip==tSeedChuva){ // isto serah magia no futuro
				if(opChuva) fimChuva();
				else iniChuva();
				setCub(0,xa,ya,za,xt,yt,zt); 
				joeInv.decSel(); 
				return;
			}
			if(tip==tDuplicador){ // este eh mais p debug
				if((J.cont+xa+ya+za) % tmpRede == tmpDecRede){
					if(setCubSV(
						getCub(xa,ya+1,za,xt,yt,zt),
						getInf(xa,ya+1,za,xt,yt,zt), 
						xa,ya-1,za,xt,yt,zt))
							wPop.play();
				}
			}
			if(tip==tCabecaZumbi){
				
				if(infImp.idy>0f) infImp.idy-=0.1f;
				if((J.cont+xa+ya+za) % 100 == 0)
					if(J.B(30))
					if(distJoe(xa,ya,za,xt,yt,zt)<10f){
						infImp.idy = 0.25f;
						wZumbi.play();
					}
				return;
			}
			if(tip==tBracoZumbi){
				if((J.cont+xa+ya+za) % 30 == 0){
					if(moveSV(BXO,xa,ya,za,xt,yt,zt)) return;
					if(J.B(10))
					if(moveSV(J.rnd(NOR,SUL,LES,OES), xa,ya,za,xt,yt,zt)) // direcao randomica
						wPlotFolha.play();				
				}
				return;
			}
			if(tip==tVidraca){
				wPop.play();
				String st = "vidraca";
				impSts("vidraca ajustada");
				int cn = getCub(xa,ya,za+1,xt,yt,zt);
				int cs = getCub(xa,ya,za-1,xt,yt,zt);
				int cl = getCub(xa+1,ya,za,xt,yt,zt);
				int co = getCub(xa-1,ya,za,xt,yt,zt);
				int cb = getCub(xa,ya-1,za,xt,yt,zt);
				if(cn==0) if(cs==0) st="vidraca_NS";
				if(cl==0) if(co==0) st="vidraca_LO";
				if(getCubMod(cb).ehDegrau) st+="e";
				if(J.iguais(st,"vidracae")) st = "vidraca";
				setCub(st, xa,ya,za,xt,yt,zt);
				return;
			}
			if(tip==tPlotar){
				setCub(0,0, xa,ya,za,xt,yt,zt);
				return;
			}
			if(tip==tSplash){
				if(J.C(3))
				setCub(tSplash2,xa,ya,za,xt,yt,zt);				
				return;
			}
			if(tip==tSplash2){
				if(J.C(3))				
				setCub(tSplash3,xa,ya,za,xt,yt,zt);				
				return;
			}			
			if(tip==tSplash3){
				if(J.C(3))				
				setCub(0,xa,ya,za,xt,yt,zt);				
				return;
			}						
			if(tip==tFornoInd){
				// poderah passar pela esteira sem q asse, assim, eh melhor uma sequencia de fornos... mas nao queimaria???
				int q = 22;
				if((J.cont+xa+ya+za)%tmpRede==tmpDecRede)
				if(temSwitchOnViz(xa,ya,za,xt,yt,zt)) // avaliar estes dois por ultimo, jah q consome mais clock
				if(temElViz(q, xa,ya,za,xt,yt,zt))// nao precisaria ver abaixo nem acima, GARGALO
				{ 
				
					int v = getCub(xa,ya-1,za,xt,yt,zt);
					String st = tCub.get(v).stCozido;
					if(st!=null)
					if(!J.vou(v,tMinFerro,tMinOuro,tMinCobre)){
						setCub(st,xa,ya-1,za,xt,yt,zt);
						cTreme=tmpRede;						
						wGerCombustao.agPlay(1000); // deve resolver
					}
				
					/*
					eh soh usar " s s   C u b . s t C o z i d o " q jah foi definido em "setCozido()"
					o problema eh q minerios de ouro e similares virarao barras correspondentes
						bolo cru
						pao cru
						broa crua
						peixe cru
						carne->bife
						batata
						ovo
						?milho???
						?animais???
						madeira->carvao?
					*/
				}
				return;
			}
			if(tip==tEncaix){

				if((J.cont+xa+ya+za)%(tmpRede>>1)==tmpDecRede){
					int q=3, cc=0, cb=0;
					cc = getCub(xa,ya+1,za,xt,yt,zt);
					cb = getCub(xa,ya-1,za,xt,yt,zt);
					
					// evitando o burlar qtd
					if(cc==0) setInf(0, xa,ya,za,xt,yt,zt);
					
					if(cb!=0)
					if(cb==cc)
					if(comCaixote(cc)!=-1)	
					if(temSwitchOnViz(xa,ya,za,xt,yt,zt)) // avaliar estes dois por ultimo, jah q consome mais clock
					if(temElViz(q, xa,ya,za,xt,yt,zt)) // nao precisaria ver abaixo nem acima, GARGALO
					{
						cTreme=tmpRede;						
						int qt = getInf(xa,ya,za,xt,yt,zt);
						qt++;
						if(qt>=20){
							setCub(comCaixote(cb), xa,ya-1,za,xt,yt,zt);
							setInf(0, xa,ya,za,xt,yt,zt);
							wMachado.play(); 
						} else {
							setInf(qt, xa,ya,za, xt,yt,zt);
							setCub(0, xa,ya-1,za,xt,yt,zt);
							wGuar.play(); 
						}
					}
				}
				return;
			}
			if(tip==tTorreRefG){ // gas
				if((J.cont+xa+ya+za)%tmpRefinaria==0)
				if(J.B(3))// precisa p distribuir a producao, senao faz muito gas e nenhuma resina
				// se tem cano de saida
				if(ehEsse(tCanoGas,tCanoVazio,xa,ya,za+1,xt,yt,zt))
				if(getInf(xa,ya,za+1,xt,yt,zt)<maxVolCano)
				// se tem calor
				if(ehEsse(tCombustor,xa,ya-7,za,xt,yt,zt))
				if(getInf(xa,ya-7,za,xt,yt,zt)>0)
				// se tem petroleo disponivel
				if(ehEsse(tCanoPetro,xa,ya-6,za+1,xt,yt,zt))
				if(getInf(xa,ya-6,za+1,xt,yt,zt)>0)
				// se a estrutura ta completa (quase)
				if(ehEsse(tTorreRefP,xa,ya-6,za,xt,yt,zt))
				if(ehEsse(tTorreRef,xa,ya+1,za,xt,yt,zt))
				if(ehEsse(tTorreRef,xa,ya-1,za,xt,yt,zt))
				{
					cTreme=tmpRede;					
					int nc=getInf(xa,ya,za+1,xt,yt,zt);
					setCub(tCanoGas,++nc,xa,ya,za+1,xt,yt,zt);
					decInf(xa,ya-6,za+1,xt,yt,zt);
					wRefinaria.agPlay(1000);
				}					
				return;
			}
			if(tip==tTorreRefC){ // combustivel
				if((J.cont+xa+ya+za)%tmpRefinaria==0)
				if(J.B(3))					
				// se tem cano de saida
				if(ehEsse(tCanoComb,tCanoVazio,xa,ya,za+1,xt,yt,zt))
				if(getInf(xa,ya,za+1,xt,yt,zt)<maxVolCano)
				// se tem calor
				if(ehEsse(tCombustor,xa,ya-5,za,xt,yt,zt))
				if(getInf(xa,ya-5,za,xt,yt,zt)>0)
				// se tem petroleo disponivel
				if(ehEsse(tCanoPetro,xa,ya-4,za+1,xt,yt,zt))
				if(getInf(xa,ya-4,za+1,xt,yt,zt)>0)
				// se a estrutura ta completa (quase)
				if(ehEsse(tTorreRefP,xa,ya-4,za,xt,yt,zt))
				if(ehEsse(tTorreRef,xa,ya+1,za,xt,yt,zt))
				if(ehEsse(tTorreRef,xa,ya-1,za,xt,yt,zt))
				{
					cTreme=tmpRede;					
					int nc=getInf(xa,ya,za+1,xt,yt,zt);
					setCub(tCanoComb,++nc,xa,ya,za+1,xt,yt,zt);
					decInf(xa,ya-4,za+1,xt,yt,zt);
					wRefinaria.agPlay(1000);
				}					
				return;
			}
			if(tip==tTorreRefR){ // resina
				if((J.cont+xa+ya+za)%tmpRefinaria==0)
				if(J.B(3))					
				// se tem cano de saida
				if(ehEsse(tCanoResina,tCanoVazio,xa,ya,za+1,xt,yt,zt))
				if(getInf(xa,ya,za+1,xt,yt,zt)<maxVolCano)
				// se tem calor
				if(ehEsse(tCombustor,xa,ya-3,za,xt,yt,zt))
				if(getInf(xa,ya-3,za,xt,yt,zt)>0)
				// se tem petroleo disponivel
				if(ehEsse(tCanoPetro,xa,ya-2,za+1,xt,yt,zt))
				if(getInf(xa,ya-2,za+1,xt,yt,zt)>0)
				// se a estrutura ta completa (quase)
				if(ehEsse(tTorreRefP,xa,ya-2,za,xt,yt,zt))
				if(ehEsse(tTorreRef,xa,ya+1,za,xt,yt,zt))
				if(ehEsse(tTorreRef,xa,ya-1,za,xt,yt,zt))
				{
					cTreme=tmpRede;					
					int nc=getInf(xa,ya,za+1,xt,yt,zt);
					setCub(tCanoResina,++nc,xa,ya,za+1,xt,yt,zt);
					decInf(xa,ya-2,za+1,xt,yt,zt);
					wRefinaria.agPlay(1000);
				}					
				return;
			}
			if(tip==tCombustor){ // p refinaria, mudar nome depois
				if((J.cont+xa+ya+za)%tmpRefinaria==0)
				if(temSwitchOnViz(xa,ya,za,xt,yt,zt)){
					cTreme=tmpRede;					
					decInfLm(1, 0, xa,ya,za,xt,yt,zt); // qtd e limite
					int cn = getCub(xa,ya,za-1,xt,yt,zt);
					int nn = getInf(xa,ya,za-1,xt,yt,zt);
					
					// reacendendo combustor
					if(nn>0)
					if(getInf(xa,ya,za,xt,yt,zt)<=0)	
					if(J.vou(cn, tCanoPetro, tCanoComb, tCanoGas))
					{
						int q=20; // petro padrao
						if(cn==tCanoComb) q=100;
						if(cn==tCanoGas) q=100;
						decInf(xa,ya,za-1,xt,yt,zt);
						setInf(q, xa,ya,za,xt,yt,zt);						
					}
				}
				return;
			}			
			if(tip==tGerCombustao){ // esquema antigo
				/*	INSTRUCOES, p esquema novo
							um cano de combustivel ao norte (petroleo)
				*/
				if((J.cont+xa+ya+za) % tmpRede == tmpIncRede){
					Cub cubNor = getCubMod(xa,ya,za+1, xt,yt,zt);
					Cub cubSul = getCubMod(xa,ya,za-1, xt,yt,zt);
					int nivCan = getInf(xa,ya,za+1, xt,yt,zt);
					int nivCom = getInf(xa,ya,za, xt,yt,zt);
					
					int q=0; // quantificar a el gerada de acordo com o tipo de combustivel
					if(cubNor.tip==tCanoGas) q = 50; 
					if(cubNor.tip==tCanoComb) q = 50;
					if(cubNor.tip==tCanoPetro) q = 20; // basta listar aqui e dozar q funciona
					
					if(q>0) // amarrado a listagem acima
					if(nivCan>0)
					if(cubSul.ehCond)
					if(temSwitchOnViz(xa,ya,za,xt,yt,zt)){						
						if(cubSul.tip==tCond0) {
							int ds = getCondDisp();
							setCub(ds,1, xa,ya,za-1,xt,yt,zt);
							cubSul = getCubMod(ds);
						}
						if(cubSul.life+q<maxElCond){
							cubSul.life+=q;
							decInfLm(1, xa,ya,za+1, xt,yt,zt);
							infImp.cTreme = 100;
							wGerCombustao.agPlay(1000);
						}
					}
					//return; // nao agora
				}
			}
			if(tip==tGerCombustao){ // esquema antigo
				if((J.cont+xa+ya+za) % tmpRede == tmpIncRede)
				if(temElViz(0, xa,ya,za,xt,yt,zt))
				if(temSwitchOnViz(xa,ya,za,xt,yt,zt))
				{ 
					cTreme=tmpRede;			
					int cc = getCub(xa,ya+1,za,xt,yt,zt);
					int nc = getInf(xa,ya+1,za,xt,yt,zt);
					int nr = getCubMod(tRede1).life;
					int q=50; // variar depois segundo o combustivel
					if(q+nr>1000) return; // ?isso eh bom??? Soh ativar se nao sobrecarregar a rede.
					
					// petroleo, gasolina, gaz e fluido zetta depois
					if(nc>0)
					if(J.vou(cc,tTanquePetro,tCanoPetro)){
						altRede(+q,tRede1, xa,ya,za,xt,yt,zt);
						decInf(xa,ya+1,za,xt,yt,zt);
						wGerCombustao.agPlay(1000);
					}
				}
				return;
			}
			if(tip==tDerretedor){
				int q=3;
				//int q=20; // talvez soh funcione numa outra rede mais forte
				if((J.cont+xa+ya+za) % (tmpRede<<1) == tmpDecRede){ 
				if(tAreia==getCub(xa,ya+1,za,xt,yt,zt))
				if(0==getCub(xa,ya-1,za,xt,yt,zt))
				if(temElViz(q, xa,ya,za,xt,yt,zt))
				if(temSwitchOnViz(xa,ya,za,xt,yt,zt)){
					cTreme=tmpRede;					
					altRede(-q,tRede1, xa,ya,za,xt,yt,zt);					
					setCub(0, xa,ya+1,za,xt,yt,zt);
					setCub(tVidroGoma, xa,ya-1,za,xt,yt,zt);
					wDerretedor.play();
					return;
				}
			}
			}
			if(tip==tPrensandoNS || tip==tPrensandoLO){
				if((J.cont+xa+ya+za)%(tmpRede<<3)==0){
					setCub("frasco_vazio",xa,ya,za,xt,yt,zt);
					// frasco quente depois
				}
				return;
			}
			if(tip==tVidroGoma){
				if((J.cont+xa+ya+za)%(tmpRede<<2)==0){
					if(ehEsse("prensa_sul",xa,ya,za+1,xt,yt,zt))
					if(ehEsse("prensa_nor",xa,ya,za-1,xt,yt,zt)){
						setCub("prensando_NS",xa,ya,za,xt,yt,zt);
						// som
						return;
					}
					if(ehEsse("prensa_les",xa-1,ya,za,xt,yt,zt))
					if(ehEsse("prensa_oes",xa+1,ya,za,xt,yt,zt)){
						setCub("prensando_LO",xa,ya,za,xt,yt,zt);
						// som
						return;
					}
					
					// L e O depois
					// gastar el
					// ver switch
				}
				return;
			}
			if(tip==tSonda){ // sai debaixo da pump
				// a sonda desce da pump por um espaco vazio
				// jah a sonda agua sobe por uma sonda comum
				if((J.cont+xa-ya+za) % 48 ==0){ // esse "-" eh importante
					int tb=getCub(xa,ya-1,za,xt,yt,zt);
					if(tb==0){
						wGuar.play(); // trocado mesmo
						setCub(tSonda,xa,ya-1,za,xt,yt,zt);
						return;
					}
					if(tb==tAgua){						
						setCub(tSondaAgua,xa,ya,za,xt,yt,zt);
						return;
					}
					if(tb==tPetroleo){
						setCub(tSondaPetro,xa,ya,za,xt,yt,zt);
						return;
					}					
				}
				return;
			}
			if(tip==tSondaAgua){ // sai debaixo da pump
				if((J.cont+xa+ya+za) % 12 ==0){
					if(getCub(xa,ya+1,za,xt,yt,zt)==tSonda){
						setCub(tSondaAgua,xa,ya+1,za,xt,yt,zt);
					}
				}
				return;
			}			
			if(tip==tSondaPetro){ 
				if((J.cont+xa+ya+za) % 12 ==0){
					if(getCub(xa,ya+1,za,xt,yt,zt)==tSonda){
						setCub(tSondaPetro,xa,ya+1,za,xt,yt,zt);
					}
				}
				return;
			}						
			if(tip==tDegrauAdobeUmido){ 
				if((J.cont+xa+ya+za) % 48 ==0)
				if(J.B(20)) // este abaixo eh facil de remover. Removido e depois plotado vira "d e g r a u   a d o b e   s e c o", q eh removivel com picareta;.
				if(getCub(xa,ya+1,za,xt,yt,zt)==0)
				if(temCubViz(0,xa,ya,za,xt,yt,zt))	// melhor assim
					setCub("degrau_adobe_seco",xa,ya,za,xt,yt,zt);
				return;
			}
			if(tip==tAdobeUmido){ 
				if((J.cont+xa+ya+za) % 48 ==0)
				if(J.B(20)) // este abaixo eh facil de remover. Removido e depois plotado vira "d e g r a u   a d o b e   s e c o", q eh removivel com picareta;.
				if(getCub(xa,ya+1,za,xt,yt,zt)==0)
				if(temCubViz(0,xa,ya,za,xt,yt,zt))	// melhor assim
					setCub("adobe_seco",xa,ya,za,xt,yt,zt);
				return;
			}			
			if(tip==tSelecionador){
				// se o  c o n t e u  d o do transportador N/S ou L/O for igual ao cubo acima do sel, encaminhar ao transportador abaixo
				if((J.cont+xa+ya+za) % (tmpRede>>1) == tmpDecRede){ // este tem q ser mais rapido q transportadores
					int tc = getCub(xa,ya+1,za,xt,yt,zt);
					if(ehDegrau) return; // este nao vale (???)
					int tb = getCub(xa,ya-1,za,xt,yt,zt);
					if(tb==0) return; // disposicao invalida
					int q = 3; // qtd el necessaria
					if(tb==tTranspVazio)
					if(temSwitchOnViz(xa,ya,za,xt,yt,zt)) // avaliar estes dois por ultimo, jah q consome mais clock
					if(temElViz(q, xa,ya,za,xt,yt,zt))
					{
						int tn = getCub(xa,ya,za+1,xt,yt,zt);
						int ts = getCub(xa,ya,za-1,xt,yt,zt);
						int tl = getCub(xa+1,ya,za,xt,yt,zt);
						int to = getCub(xa-1,ya,za,xt,yt,zt);
						
						if(tCub.get(tn).ehTransp)
						if(tCub.get(tn).dir==SUL)
						if(ts==tTranspVazio){
							tCub.get(tn).zDxx();
							if(tCub.get(tn).tipp==tc){ // eh esse
								tCub.get(tn).dir=BXO;
								trocaPos(BS, xa,ya,za+1, xt,yt,zt);
								wSelecionador.play();
								altRede(-q,tRede1, xa,ya,za,xt,yt,zt);
							} else { // segue normal
								trocaPos(SUL2, xa,ya,za+1, xt,yt,zt);
							}
							return;
						}

						if(tCub.get(ts).ehTransp)
						if(tCub.get(ts).dir==NOR)
						if(tn==tTranspVazio){
							tCub.get(ts).zDxx();
							if(tCub.get(ts).tipp==tc){ // eh esse
								wSelecionador.play();							
								altRede(-q,tRede1, xa,ya,za,xt,yt,zt);								
								tCub.get(ts).dir=BXO;
								trocaPos(BN, xa,ya,za-1, xt,yt,zt);
							} else { // segue normal
								trocaPos(NOR2, xa,ya,za-1, xt,yt,zt);
							}
							return;
						}
						
						if(tCub.get(to).ehTransp)
						if(tCub.get(to).dir==LES)
						if(tl==tTranspVazio){
							tCub.get(to).zDxx();
							if(tCub.get(to).tipp==tc){ // eh esse
								tCub.get(to).dir=BXO;
								trocaPos(BL, xa-1,ya,za, xt,yt,zt);
								wSelecionador.play();
								altRede(-q,tRede1, xa,ya,za,xt,yt,zt);
							} else { // segue normal
								trocaPos(LES2, xa-1,ya,za, xt,yt,zt);
							}
							return;
						}
						
						if(tCub.get(tl).ehTransp)
						if(tCub.get(tl).dir==OES)
						if(to==tTranspVazio){
							tCub.get(tl).zDxx();
							if(tCub.get(tl).tipp==tc){ // eh esse
								tCub.get(tl).dir=BXO;
								trocaPos(BO, xa+1,ya,za, xt,yt,zt);
								wSelecionador.play();
								altRede(-q,tRede1, xa,ya,za,xt,yt,zt);
							} else { // segue normal
								trocaPos(OES2, xa+1,ya,za, xt,yt,zt);
							}
							return;
						}						
						
					}
				}
				return;
			}
			if(tip==tTranspIn) if((J.cont+xa+ya+za)%tmpRede==tmpDecRede){
				// plotado p transp
				// puxa o bloco plotado abaixo p transp q estah em cima
				
				int tc = getCub(xa,ya+1,za,xt,yt,zt);
				int tb = getCub(xa,ya-1,za,xt,yt,zt);
				int q = 6; // ?ta bom esse valor???
				
				if(tc==tTranspVazio)
				if(tb!=0)
				if(tCub.get(tb).ehTransp==false)
				if(temSwitchOnViz(xa,ya,za,xt,yt,zt)) // avaliar estes dois por ultimo, jah q consome mais clock
				if(temElViz(q, xa,ya,za,xt,yt,zt))
				{
					if(setTransp(tb,xa,ya+1,za,xt,yt,zt)){
						setCub(0, xa,ya-1,za, xt,yt,zt);
						wGuar.play();
						altRede(-q,tRede1, xa,ya,za,xt,yt,zt);											
						return;
					}
				}
				return;
			}
			if(tip==tTranspOut) if((J.cont+xa+ya+za)%(tmpRede>>1)==tmpDecRede){ // um pouco mais rapido p nao bugar
				// plota o cubo do transp acima no espaco vazio abaixo
				
				int tb = getCub(xa,ya-1,za,xt,yt,zt);
				int tc = getCub(xa,ya+1,za,xt,yt,zt);
				int q = 6; //?tah bom esse valor???
				
				if(tb==0)
				if(tc!=tTranspVazio)	
				if(tCub.get(tc).ehTransp)
				if(temSwitchOnViz(xa,ya,za,xt,yt,zt)) // avaliar estes dois por ultimo, jah q consome mais clock
				if(temElViz(q, xa,ya,za,xt,yt,zt)) // ?Seria bom mesmo deixar isso dependendo de el???
				{	
					setCub(tCub.get(tc).tipp, xa,ya-1,za, xt,yt,zt); 
					tCub.get(tc).ativo = false;
					tCub.get(tc).cubCen = null;
					tCub.get(tc).dtCen = null;
					
					setCub(tTranspVazio,xa,ya+1,za,xt,yt,zt);
					wGuar.play();
					altRede(-q,tRede1, xa,ya,za,xt,yt,zt);											
					return;
				}
				return;
			}
			if(tip==tRetirador){ 
				// do bau p transportador
				// avalia apenas o bau de baixo
				// retira do bau abaixo e coloca no transportador acima
				// este usa el, o  g u a r d a d o r nao
				if((J.cont+xa+ya+za) % tmpRede == tmpDecRede){
					int tc = getCub(xa,ya+1,za,xt,yt,zt);
					int tb = getCub(xa,ya-1,za,xt,yt,zt);
					int q = 3; // qtd el necessaria


					
					// do bau p transp
					if(tc==tTranspVazio) 
					if(tb==tBau)
					if(temSwitchOnViz(xa,ya,za,xt,yt,zt)) // avaliar estes dois por ultimo, jah q consome mais clock
					if(temElViz(q, xa,ya,za,xt,yt,zt))
					{
						cTreme=tmpRede;
						int b = getNumBau(xa,ya-1,za,xt,yt,zt);
						if(b==-1) return;
						//if(b==-1) J.impErr("!slot do bau nao encontrado","regCub(), tRetirador");
						Itm it = bau.get(b).retiraItem();
						if(it!=null)
						if(setTransp(it.tip,xa,ya+1,za,xt,yt,zt)){
							wGuar.play();
							altRede(-q,tRede1, xa,ya,za,xt,yt,zt);											
							return;
						}
					}

					
				}
				return;
			}
			if(tip==tGuardador){ 
				// do transportador p bau
				// avalia apenas o transportador acima 
				// guarda no bau abaixo
				if((J.cont+xa+ya+za) % tmpRede == tmpDecRede){
					int q = 3;
					if(temElViz(q, xa,ya,za,xt,yt,zt))
					if(temSwitchOnViz(xa,ya,za,xt,yt,zt))						
					{
						cTreme=tmpRede;					
						int tc = getCub(xa,ya+1,za,xt,yt,zt);
						int tb = getCub(xa,ya-1,za,xt,yt,zt);


						// do transp p bau
						if(tb==tBau)
						if(tCub.get(tc).ehTransp) // cheio						
						{				
							int t = tCub.get(tc).tipp;
							int b = getNumBau(xa,ya-1,za,xt,yt,zt);
							if(b==-1) J.impErr("!bau nao encontrado","regCub(), tGuardador");
							if(bau.get(b).insBau(1,t)){
								tCub.get(tc).ativo = false;
								tCub.get(tc).cubCen = null;
								tCub.get(tc).dtCen = null;
								setCub(tTranspVazio,xa,ya+1,za,xt,yt,zt);
								altRede(-q,tRede1, xa,ya,za,xt,yt,zt);
								wGuar.play();
								return;
							}
						}						

						// plotado acima p bau
						if(tb==tBau)
						if(!J.vou(tc,0,tTranspVazio)){// ?Outras excessoes???
							
							int b = getNumBau(xa,ya-1,za,xt,yt,zt);
							if(b==-1) J.impErr("!bau nao encontrado","regCub(), tGuardador, NAO sobre transportadores");
							if(bau.get(b).insBau(1,tc)){
								setCub(0, xa,ya+1,za, xt,yt,zt);
								altRede(-q,tRede1, xa,ya,za,xt,yt,zt);
								wGuar.play();
								return;					
							}
							
							
						}
						
					}
				}
				return;
			}
			if(tip==tMoldadora){
				int q = 3;
				if((J.cont+xa+ya+za) % tmpRede == tmpDecRede)
				if(getCub(xa,ya-1,za,xt,yt,zt)==0)
				if(getCub(xa,ya+1,za,xt,yt,zt)==tLama)
				if(temElViz(q, xa,ya,za,xt,yt,zt))
				if(temSwitchOnViz(xa,ya,za,xt,yt,zt))
				{					
					cTreme=tmpRede;			
					setCub(0,xa,ya+1,za,xt,yt,zt);
					setCub(tDegrauAdobeUmido,xa,ya-1,za,xt,yt,zt);
					altRede(-q,tRede1, xa,ya,za,xt,yt,zt);				
					wPlotLama.play(); // ?esse tah bom???
				}
				return;
			}
			if(tip==tEnvasadora){
				int q = 3;
				if((J.cont+xa+ya+za) % (tmpRede>>1) == tmpDecRede)
				if(temElViz(q, xa,ya,za,xt,yt,zt))
				if(temSwitchOnViz(xa,ya,za,xt,yt,zt))
				{
					
					int bc = getCub(xa,ya+1,za,xt,yt,zt);
					int bb = getCub(xa,ya-1,za,xt,yt,zt);
					
					// esse eh o melhor jeito de corrigir tanques. Adotar como modelo. Evitar usar busca por strings q consome clock, ainda mais quando todas as maquinas funcionam no mesmo J.cont
					//if(bc==tTanqueVazio) bc = tTanqueSucoMaca;
					// mas acho q isso eh inocuo aqui
					
					if(bb==tFrascoVazio)
					if(getInf(xa,ya+1,za,xt,yt,zt)>0){
						
						int nv = getInf(xa,ya+1,za,xt,yt,zt);
						boolean foi = false;
						
						if(!foi) if(bc==tTanqueSucoMaca){
							setCub(tFrascoSucoMaca, xa,ya-1,za,xt,yt,zt);
							setCub(tTanqueSucoMaca,nv-1, xa,ya+1,za,xt,yt,zt);
							foi=true;
						}
						if(!foi) if(bc==tCanoSucoMaca){
							setCub(tFrascoSucoMaca, xa,ya-1,za,xt,yt,zt);
							setCub(tCanoSucoMaca,nv-1, xa,ya+1,za,xt,yt,zt);
							foi=true;
						}						
						if(!foi) if(bc==tTanqueGarapa){
							setCub(tFrascoGarapa, xa,ya-1,za,xt,yt,zt);
							setCub(tTanqueGarapa,nv-1, xa,ya+1,za,xt,yt,zt);
							foi=true;
						}
						if(!foi) if(bc==tCanoGarapa){
							setCub(tFrascoGarapa, xa,ya-1,za,xt,yt,zt);
							setCub(tCanoGarapa,nv-1, xa,ya+1,za,xt,yt,zt);
							foi=true;
						}						
						if(foi){
							cTreme=tmpRede;
							altRede(-q,tRede1, xa,ya,za,xt,yt,zt);				
							wEnvas.agPlay(1000);
							return;							
						}
					}
				}
				return;
			}
			if(tip==tPloter){
				// retira um item do bau acima e plota no espaco vazio abaixo
				int q = 3;
				if((J.cont+xa+ya+za) % tmpRede == tmpDecRede)
				if(temElViz(q, xa,ya,za,xt,yt,zt))
				if(temSwitchOnViz(xa,ya,za,xt,yt,zt)){
					int cb = 0;
					Itm it = null;
					
					if(getCub(xa,ya+1,za,xt,yt,zt)==tBau)
					if(getCub(xa,ya-1,za,xt,yt,zt)==0){
						int b = getNumBau(xa,ya+1,za,xt,yt,zt);
						if(b==-1) J.impErr("!bau nao encontrado","regCub(), tPloter");
						it = bau.get(b).retiraItem();
						if(it!=null){
							cTreme=tmpRede;													
							altRede(-q,tRede1, xa,ya,za,xt,yt,zt);							
							setCub(it.tip, 0, xa,ya-1,za,xt,yt,zt);
							wPloter.agPlay(1000);
						}
					}					
				}
				return;
			}
			if(tip==tJuicer){
				int q = 3;

				if((J.cont+xa+ya+za) % tmpRede == tmpDecRede)
				if(temElViz(q, xa,ya,za,xt,yt,zt))
				if(temSwitchOnViz(xa,ya,za,xt,yt,zt))
				{
					
					
					int cb = getCub(xa,ya-1,za, xt,yt,zt);
					int nv = getInf(xa,ya-1,za, xt,yt,zt);
					int cc = getCub(xa,ya+1,za, xt,yt,zt);
					boolean foi = false;
					
					if(nv>=maxVolTanque) if(getCubMod(cb).ehTanque) return;
					if(nv>=maxVolCano) if(getCubMod(cb).ehCano) return;
					
					// ajustando tanques vazios
					// mas isso tinha q tah separado
					if(cb==tTanqueVazio) {
						if(cc==tMaca || cc==tMacaVerde)	cb = tTanqueSucoMaca;
						if(cc==tTaloCana) cb = tTanqueGarapa;
					}
					if(cb==tCanoVazio) {
						if(cc==tMaca || cc==tMacaVerde)	cb = tCanoSucoMaca;
						if(cc==tTaloCana) cb = tCanoGarapa;
					}					
					
					if(!foi) // suco de maca
					if(cb==tTanqueSucoMaca || cb==tCanoSucoMaca)
					if(cc==tMaca || cc==tMacaVerde){
						setCub(cb,nv+1,xa,ya-1,za,xt,yt,zt);
						foi = true;
					}					
					if(!foi) // cana e garapa
					if(cb==tTanqueGarapa || cb==tCanoGarapa)
					if(cc==tTaloCana){
						setCub(cb,nv+1,xa,ya-1,za,xt,yt,zt);
						foi = true;
					}					
					
					if(foi){ // finalizando
							cTreme=tmpRede;																		
						setCub(0,xa,ya+1,za,xt,yt,zt);
						altRede(-q,tRede1, xa,ya,za,xt,yt,zt);							
						wMaqFunc.agPlay(1000);
					}
				}
				return;
			}
			if(tip==tBau){
				// nada aqui
				// mas talvez desse p calcular o indice automaticamente... talvez depois.
			}
			if(tip==tPortalNS || tip==tPortalLO){			
				if(distJoe(xa,ya,za,xt,yt,zt)<1.6f){
					int m = (tamArea>>1) -(int)(J.xCam);
					int o = (tamArea>>1) -(int)(J.zCam);					
					int sobPeh = getCub(m, (int)(yJoe-0.5f), o, meioTab, 0, meioTab);			
					String st = J.rndSt(new String[]{"vanilla","desert","lua","marte","zetta","nether","aether"});
					//if(sobPeh==getInd("obsidian")) st = "vanilla";
					if(sobPeh==getInd("rocha")) st = "vanilla";
					if(sobPeh==getInd("nuvem")) st = "aether";
					if(sobPeh==getInd("rocha_zetta")) st = "zetta";
					if(sobPeh==getInd("rocha_magma")) st = "nether";
					if(sobPeh==getInd("rocha_deserto")) st = "desert";
					if(sobPeh==getInd("rocha_marciana")) st = "marte";
					if(sobPeh==getInd("rocha_lunar")) st = "lua";
					proMundo(st);
					return;					
				}					
			}
			if(tip==tSeedRefinaria){
				if(J.C(30)){
					setCub("combustor", xa,ya,za,xt,yt,zt);
					setCub("switch_nor_off", xa,ya,za+1,xt,yt,zt);
					setCub("cano_petro", xa,ya,za-1,xt,yt,zt);
					
					setCub("torre_ref_p", xa,ya+1,za,xt,yt,zt);
					setCub("cano_petro", xa,ya+1,za+1,xt,yt,zt);
					
					setCub("torre_ref", xa,ya+2,za,xt,yt,zt);
					
					setCub("torre_ref_r", xa,ya+3,za,xt,yt,zt);
					setCub("cano_resina", xa,ya+3,za+1,xt,yt,zt);
					
					setCub("torre_ref", xa,ya+4,za,xt,yt,zt);					
					
					setCub("torre_ref_c", xa,ya+5,za,xt,yt,zt);
					setCub("cano_comb", xa,ya+5,za+1,xt,yt,zt);					
					
					setCub("torre_ref", xa,ya+6,za,xt,yt,zt);					
					
					setCub("torre_ref_g", xa,ya+7,za,xt,yt,zt);
					setCub("cano_gas", xa,ya+7,za+1,xt,yt,zt);					
					
					setCub("torre_ref", xa,ya+8,za,xt,yt,zt);					
				}
				return;
			}
			if(tip==tSeedDelViz){		
				if(cPlot<=0){ // semi-instantaneo				
					int q=1;
					for(int m=-q; m<=+q; m++)
					for(int n=-q; n<=+q; n++)
					for(int o=-q; o<=+q; o++)
						setCub(0, xa+m,ya+n,za+o,xt,yt,zt);
				}
				return;			
			}
			if(tip==tSeedNivelar){
				if(J.C(30)){
					int q=6;
					for(int m=-q; m<=+q; m++)
					for(int n=0; n<=q+q; n++)
					for(int o=-q; o<=+q; o++)
						setCub(0, xa+m,ya+n,za+o,xt,yt,zt);
				}
				return;
			}
			if(tip==tSeedPortal){
				if(J.C(30)){
					setCub(0,xa,ya,za,xt,yt,zt);
					plotPortal(-1,xa,ya,za,xt,yt,zt);
				}
				return;
			}
			if(tip==tSeedCirc){ // circulo no plano horizontal usando o cubo logo abaixo
				if(!temAreaVizNula(xt,yt,zt)){
					impSts("use a roda do mouse + CONTROL p ajustar o tamanho do circulo");
					zCub();
					plotCirc(comCai(getCub(xa,ya-1,za,xt,yt,zt)),opNumMultPlot,xa,ya-1,za,xt,yt,zt);
				}				
				return;
			}
			if(tip==tSeedSmooth){ // suaviza a area em q este cubo eh plotado
				if(!temAreaVizNula(xt,yt,zt)){
					setCub(0, xa,ya,za,xt,yt,zt);					
					getTarea(xt,yt,zt).smoothArea();					
				}				
				return;
			}
			if(tip==tSeedPreencher){ // ao plota-lo, o bloco se espalha como agua, qq bloco plotado acima dele substituirah ele todo no mapa
			
				//setCub("latex",xa,ya,za,xt,yt,zt);
				//if(1==1) return;
				
				if(ctrlOn) // por seguranca
				//if((J.cont+xa+ya+za) % 3==0)
				{
					setCubSV(tSeedPreencher,0, xa+J.RS(2), ya-J.R(2), za+J.RS(2),xt,yt,zt);					
					if(!temCubViz(0,0,tAgua,xa,ya,za,xt,yt,zt))
					if(!J.vou(getCub(xa,ya-1,za,xt,yt,zt),0,tAgua))
						setCub(tPreenchido,xa,ya,za,xt,yt,zt);
				}
				return;
			}
			if(tip==tSeedPir){
				if(J.C(30))
				if(!temAreaVizNula(xt,yt,zt))
				if(!temAreaVizNula(xt,yt+1,zt)){
					setCub(0, xa,ya,za,xt,yt,zt);
					plotPir(getCub(xa,ya-1,za, xt,yt,zt), 6+J.R(8), xa,ya,za,xt,yt,zt);
				}				
				return;
			}
			if(tip==tRede0){ // transforma em rede1 ao contato com ela
				if((J.cont+xa+ya+za)%47==0)
					if(temCubViz("rede1",xa,ya,za,xt,yt,zt) 
						|| ehEsse("rede1",xa,ya+1,za,xt,yt,zt)
						|| ehEsse("rede1",xa,ya-1,za,xt,yt,zt))
							setCub(tRede1,xa,ya,za,xt,yt,zt);
				return;
			}
			if(tip==tRede1){
				//temPosImp=true; // isso eh importante!!! NAO, JAH FOI!!!
				/*
				Cub ccc = tCub.get(tRede1Lat);
				ccc.life = life;
				if(ehConEl(xa,ya,za+1,xt,yt,zt)) cubNor = ccc; else cubNor=null;
				if(ehConEl(xa,ya,za-1,xt,yt,zt)) cubSul = ccc; else cubSul=null;
				if(ehConEl(xa+1,ya,za,xt,yt,zt)) cubLes = ccc; else cubLes=null;
				if(ehConEl(xa-1,ya,za,xt,yt,zt)) cubOes = ccc; else cubOes=null;
				if(ehConEl(xa,ya+1,za,xt,yt,zt)) cubCma = ccc; else cubCma=null;
				if(ehConEl(xa,ya-1,za,xt,yt,zt)) cubBxo = ccc; else cubBxo=null;
				*/
				
				if((J.cont+xa+ya+za)%47==0){
					corrDisEl();
					if(!ehEsse(tRede1, xa,ya,za+1,xt,yt,zt))
					if(!ehEsse(tRede1, xa,ya,za-1,xt,yt,zt))
					if(!ehEsse(tRede1, xa+1,ya,za,xt,yt,zt))
					if(!ehEsse(tRede1, xa-1,ya,za,xt,yt,zt))
					if(!ehEsse(tRede1, xa,ya+1,za,xt,yt,zt))
					if(!ehEsse(tRede1, xa,ya-1,za,xt,yt,zt))
						setCub(0, xa,ya,za,xt,yt,zt);
				}
			}
			if(tip==tIdent){
				// se o bloco de cima for igual ao bloco de baixo, entao aciona o vator na "coroa-cruz" abaixo
				// idem se o bloco de cima for vazio e o de baixo for nao-vazio
				
				int q = 1; // bem baixo p nao falhar
				if((J.cont+xa+ya+za) % 3 ==0)  // acho q esse precisa ser mais rapido
				if(temElViz(q, xa,ya,za,xt,yt,zt))
				if(temSwitchOnViz(xa,ya,za,xt,yt,zt))
				{
					int bc = getCub(xa,ya+1,za,xt,yt,zt);
					int bb = getCub(xa,ya-1,za,xt,yt,zt);

					boolean foi = false;
					if(bc!=0){
						// pequeno filtro gambiarrento
						if(bb==tDegrauAdobeSeco) bb=tDegrauAdobe;
						if(bb==tMadeiraFacil) bb=tMadeira;

						if(bc==bb) foi=true;
					} else {
						// aqui identifica a presenca de qq bloco nao nulo
						if(bb!=0) foi = true;
					}
					if(foi){
							cTreme=tmpRede;																			
						wIdent.agPlay(1000);
						altRede(-q,tRede1, xa,ya,za,xt,yt,zt);
						altVator(xa,ya-1,za+1,xt,yt,zt);
						altVator(xa,ya-1,za-1,xt,yt,zt);
						altVator(xa+1,ya-1,za,xt,yt,zt);
						altVator(xa-1,ya-1,za,xt,yt,zt);
					}						
				}
				return;
			}
			if(tip==tRochaAntiEl){ 
				if((J.cont+xa+ya+za) % tmpRede==tmpDecRede){
					// esquema novo
					decCond1Viz(1, 0, xa,ya,za,xt,yt,zt); // :)
					
					// esquema antigo
					if(temElViz(0, xa,ya,za,xt,yt,zt)) altRede(-1,tRede1, xa,ya,za,xt,yt,zt);				
					
				}
				return;
			}			
			if(tip==tRochaProEl){ 
				if((J.cont+xa+ya+za) %tmpRede==tmpIncRede){
					{// esquema novo
						int d=0;
						boolean foi=false; // só funciona com "2" abaixo, com um buga;
						d=J.BXO; if(!foi) if(getCubDir(d, xa,ya,za,xt,yt,zt)==tCond0) { setCubDir(d, getCondDisp(), xa,ya,za,xt,yt,zt); getCubModDir(d, xa,ya,za,xt,yt,zt).life+=2; foi=true; }
						d=J.CMA; if(!foi) if(getCubDir(d, xa,ya,za,xt,yt,zt)==tCond0) { setCubDir(d, getCondDisp(), xa,ya,za,xt,yt,zt); getCubModDir(d, xa,ya,za,xt,yt,zt).life+=2; foi=true; }
						d=J.NOR; if(!foi) if(getCubDir(d, xa,ya,za,xt,yt,zt)==tCond0) { setCubDir(d, getCondDisp(), xa,ya,za,xt,yt,zt); getCubModDir(d, xa,ya,za,xt,yt,zt).life+=2; foi=true; }
						d=J.SUL; if(!foi) if(getCubDir(d, xa,ya,za,xt,yt,zt)==tCond0) { setCubDir(d, getCondDisp(), xa,ya,za,xt,yt,zt); getCubModDir(d, xa,ya,za,xt,yt,zt).life+=2; foi=true; }
						d=J.LES; if(!foi) if(getCubDir(d, xa,ya,za,xt,yt,zt)==tCond0) { setCubDir(d, getCondDisp(), xa,ya,za,xt,yt,zt); getCubModDir(d, xa,ya,za,xt,yt,zt).life+=2; foi=true; }
						d=J.OES; if(!foi) if(getCubDir(d, xa,ya,za,xt,yt,zt)==tCond0) { setCubDir(d, getCondDisp(), xa,ya,za,xt,yt,zt); getCubModDir(d, xa,ya,za,xt,yt,zt).life+=2; foi=true; }
						//incCond1Viz(1, maxElCond, xa,ya,za,xt,yt,zt);
						
					}
					
					// esquema antigo
					if(temElViz(0, xa,ya,za,xt,yt,zt))					
						altRede(+1,tRede1, xa,ya,za,xt,yt,zt);
						return;
					}					
				  return;
			}
			if(tip==tGerSolar || tip==tGerEolico){
				if((J.cont+xa+ya+za) % tmpRede == tmpIncRede){
					int q = 1;
					if(tip==tGerEolico) q = 3;
					
					// c a r r   cond# abaixo, esquema novo
					if(getCubMod(xa,ya-1,za,xt,yt,zt).ehCond){
						if(getCubMod(xa,ya-1,za,xt,yt,zt).tip==tCond0) 
							setCub(getCondDisp(),xa,ya-1,za,xt,yt,zt);
						else 
							if(getCubMod(xa,ya-1,za,xt,yt,zt).life<maxElCond) 
								getCubMod(xa,ya-1,za,xt,yt,zt).life+=q;						
						return;
					}					
					
					// c a r r   r e d e, esquema antigo
					if(ehEsse(tRede1,xa,ya-1,za,xt,yt,zt)){
						altRede(+q, tRede1,xa,ya-1,za,xt,yt,zt);
						return;
					}
					
					// c a r r   b a t e r i a s
					if(ehEsse(tBateriaFixa,xa,ya-1,za,xt,yt,zt)){
					  int nv = getInf(xa,ya-1,za,xt,yt,zt);
						nv = J.corrInt(q+nv,0,100);
						setInf(nv, xa,ya-1,za,xt,yt,zt);
						return;						
					}
				}
				return;
			}
			if(tip==tIsca){
				// com ou sem varinha na mao os pontos serao definidos
				J3d.Ponto p = dtCen.getPnt("ponta");
				XPesca = p.X;
				YPesca = p.Y;
				if(!opVarinhaOn) setCub(0, xa,ya,za,xt,yt,zt);
			}
			if(tip==tFerroVelho || tip==tMarmore){ // remover isso depois. Deve ser substituido por reg de area
				if(J.C(30))
				if(J.B(3)){	// mudar depois do debug
					int bc = getCub(xa,ya+1,za,xt,yt,zt);
					int bcc = getCub(xa,ya+2,za,xt,yt,zt);
					if(bc==tAgua)
					if(bcc==tAgua)
						setCub(getInd("alga"),12, xa,ya+1,za,xt,yt,zt);
				}
				return;
			}
			if(tip==tSeedPorSolo){
				if(J.C(30))
				if(!temAreaVizNula(xt,yt,zt)){
					//int vd = getCub(xa,ya-1,za,xt,yt,zt);
					//int vb = semDegrau(vd);
					int vb = getCub(xa,ya-1,za,xt,yt,zt);
					int vc = getCub(xa,ya+1,za,xt,yt,zt);
					setCub(0,xa,ya,za,xt,yt,zt);
					opPlotSobreSolo=false;
					
					if(vb==tAreia) if(vc==tAgua){						
						for(int r=0; r<6; r++)
							setCub(J.rndSt("degrau_ferro_velho","ferro_velho"),
								xa+J.RS(2),
								ya+J.RS(2),
								za+J.RS(2),
									xt,yt,zt);
						return;
					}
						
					
					if(vb==getInd("rocha")) {
						setCub("rocha", xa,ya,za,xt,yt,zt);
					}
					if(vb==getInd("areia")) if(!temCubViz(tAgua,xa,ya,za,xt,yt,zt)){
						plotCacto("cacto_base",xa,ya,za,xt,yt,zt);
						
					}
					if(vb==getInd("areia_zetta")) {
						int tt = getInd("cristal_zetta1");
						opPlotSobreSolo=true;
						int mm=xa+J.RS(10);
						int oo=za+J.RS(10);
						for(int q=0; q<10; q++)
							if(getCub(mm,ya-1,oo,xt,yt,zt)==vb)
								setCub(tt+J.R(3),0, mm,ya,oo,xt,yt,zt);
						
						opPlotSobreSolo=false;
					}
					if(vb==getInd("grama")) {
						setCub("seed_mato",xa+J.RS(4),ya,za+J.RS(4),xt,yt,zt);
						plotArv(-1, xa,ya,za,xt,yt,zt);
					}
					if(vb==getInd("grama_cel")){
						plotArv("folha_cel",xa,ya,za,xt,yt,zt);
					}
					if(vb==getInd("solo_floresta")) {
						plotArv("folha_sequoia",xa,ya,za,xt,yt,zt);
					}
					if(J.vou(vb,getInd("areia_marciana"),getInd("rocha_marciana"))) {
						setCub("seed_pedra",xa,ya,za,xt,yt,zt);
					}					

					return;
				}
			} 
			if(tip==tSeedSemMato){
				if(J.C(30)){					
					trocaTudo(tMato,tExp);				
					trocaTudo(tMato-1,tExp); // tomara q isso nao bug. Precisa respeitar a ordem de inserção, senão dá problema: o cubo "mato crescendo" deve ser inserido antes de mato "maduro". Devem ficar um ao lado do outro.
					setCub(0,xa,ya,za,xt,yt,zt);
				}
			}
			if(tip==tExp){
				if(J.B(3))setCub(tExp2,xa,ya,za,xt,yt,zt);
				return;
			} 
			if(tip==tExp2){
				// isso nao tah bom
				int vv = getRioViz(xa,ya,za,xt,yt,zt);
				vv = emRioCorr(vv);
				setCub(vv,xa,ya,za,xt,yt,zt);				
				tab.are[xt][yt][zt].cCalcOc=1;
				tab.are[xt][yt][zt].ehAreaOculta=false;
				return;
			} 
			if(tip==tSeedExp){
				//iniExp(xa,ya,za,xt,yt,zt);
				iniTerr();
				wExp.play();				
				int vv = 0, r=6;
				for(int m=1; m<=20; m++)
				for(int n=1; n<=20; n++)
				for(int o=1; o<=20; o++){
					if(fCirc.cel[m][n]<r)
					if(fCirc.cel[n][o]<r){
						vv = getCub(xa+m-10,ya+n-10,za+o-10, xt,yt,zt);
						if(J.vou(vv,tTnt,tTntOn)) vv = tSeedExp;
						else vv = tExp;
						setCub(vv,xa+m-10,ya+n-10,za+o-10, xt,yt,zt);
					}
				}				
				return;
			}
			if(tip==tTntOn){
				incInf(xa,ya,za,xt,yt,zt);
				if(getInf(xa,ya,za,xt,yt,zt)>tmpTnt)
					setCub(tSeedExp, xa,ya,za, xt,yt,zt);
				return;
			}
			if(tip==tAguaCorr){
				regFluido(tAgua,tAguaCorr, 4);
				return;
			} 
			if(tip==tAguaCelCorr){
				regFluido(tAguaCel,tAguaCelCorr, 4);
				return;
			} 			
			if(tip==tPetroleoCorr){
				regFluido(tPetroleo,tPetroleoCorr, 4);
				return;
			} 						
			if(tip==tAcidoCorr){
				regFluido(tAcido,tAcidoCorr, 4);
				return;
			} 			
			if(tip==tSeedCtt){ // 000000000000000000000								
				if(J.C(32))
				if(!temAreaVizNula(xt,yt,zt)){					
					setCub(0, xa,ya,za,xt,yt,zt);
					String fil = "vanilla"; // filtro por ambiente, depois.
					ArrayList<String> lis = J.listarArqs("c://java//maps//ctx//", fil, "ctx");
					String cm = "padrao.ctx";
					if(lis.size()>0) cm = lis.get(J.R(lis.size()));
					impSts(cm); // isso é bom???					
					openCtxLandMark(cm, xa,ya,za,xt,yt,zt);
				}
				return;				
			}
			if(tip==tSeedCttUlt){ // 000000000000000000000								
				if(J.C(32))
				if(!temAreaVizNula(xt,yt,zt)){					
					setCub(0, xa,ya,za,xt,yt,zt);
					String cm = camUltCtxEsc;
					impSts(cm); // isso é bom???					
					openCtxLandMark(cm, xa,ya,za,xt,yt,zt);
				}
				return;				
			}

			if(tip==tSeedMacieira){
				if(J.C(12))
				if(!temAreaVizNula(xt,yt,zt))
				if(J.vou(getCub(xa,ya-1,za,xt,yt,zt),
						getInd("grama"),
						getInd("grama_cel"),
						getInd("terra"),
						getInd("terra_arada"),
						getInd("terra_arada_regada"))){
					setCub(0,0, xa,ya,za,xt,yt,zt);

					if(!temCubViz(tAgua, xa,ya,za, xt,yt,zt)) // mas nao sao soh estes e pode ter agua acima tb
						plotArv("folha_macieira",xa,ya,za,xt,yt,zt);					
				}
				return;								
			}
			if(tip==tSeedMato){
				if(J.C(12))
				if(!temAreaVizNula(xt,yt,zt)){
					int q = 2;
					opPlotSobreSolo=false;
					setCub(0,0, xa,ya,za,xt,yt,zt);
					opPlotSobreSolo=true;
					for(int m=-q; m<=+q; m++)
					for(int o=-q; o<=+q; o++)
						if(J.B(4))
							setCub(tMato,500+J.RS(200), xa+m,ya,za+o,xt,yt,zt);
					opPlotSobreSolo=false;
				}
				return;				
			}
			if(tip==tSeedCoq){
				if(J.C(12))
				if(getTarea(xt,yt+1,zt)!=null){
					setCub(0,0, xa,ya,za,xt,yt,zt);					
					if(!temCubViz("agua",xa,ya,za,xt,yt,zt)) // impreciso, mas ajuda. Completar depois.
					if(J.vou(getCub(xa,ya-1,za,xt,yt,zt),
							tAreia, // CUIDADO AQUI!
							tGrama,
							tTerra,
							tTerraArada,
							tTerraAradaRegada))	
									plotArv("folha_coq",xa,ya,za,xt,yt,zt);


				}
				return;				
			}
			if(tip==tSeedSeq){
				if(J.C(12))
				if(!temAreaVizNula(xt,yt,zt))
				if(!temAreaVizNula(xt,yt+2,zt)){
					plotArv("folha_sequoia",xa,ya,za,xt,yt,zt);
				}
				return;
			}
			if(tip==tSeedPedra){
				if(J.C(12))
				if(!temAreaVizNula(xt,yt,zt)){
					setCub(0,0, xa,ya,za,xt,yt,zt);
					int vb = getCub(xa,ya-1,za,xt,yt,zt);
					
					int bas = getInd("pedra1");
					// em  m a r t e ?
					if(bas==0)
					if(J.vou(vb,getInd("areia_marciana"),getInd("rocha_marciana"))) 
						bas = getInd("pedra_marciana1"); // 2 e 3 seguidos
					
					if(bas==0) 
					if(J.vou(vb,getInd("areia_lunar"),getInd("rocha_lunar"))) 
						bas = getInd("pedra_lunar1"); // 2 e 3 seguidos
					
					
					opPlotSobreSolo=true;
				
					int q = 2;
					for(int m=-q; m<=+q; m++)
					for(int o=-q; o<=+q; o++)
						if(J.B(4))
							setCub(bas+J.R(3), xa+m,ya,za+o,xt,yt,zt);
					opPlotSobreSolo=false;
				}
				return;				
			}
			if(tip==tSeedPlatAether){
				if(J.C(12))
					plotPlatAether(xa,ya,za,xt,yt,zt);
				return;
			}
			if(tip==tCauleBan){
				{ // broto sobre caule, muda sobre caule no solo
					if((J.cont+xa+ya+za)%500==0)
					if(J.B(30) || opGrowFast)
					if(getCub(xa,ya+1,za, xt,yt,zt)==0)
					if(getCub(xa,ya-1,za, xt,yt,zt)==tTerraAradaRegada)
						setCub("muda_bananeira",xa,ya+1,za, xt,yt,zt);
				}
				{ // mudas aparecem ao lado do caule, este rente ao solo
				
					if((J.cont+xa+ya+za)%500==0 || opGrowFast)
					if(J.B(30) || opGrowFast){
						if(getCub(xa,ya+1,za, xt,yt,zt)==tip)
						if(getCub(xa,ya+2,za, xt,yt,zt)==tip)
						if(temCubCol("folha_bananeira",10, xa,ya,za, xt,yt,zt))
						{
							int q = 10;
							if(getCub(xa+1,ya-1,za, xt,yt,zt)==tTerraAradaRegada) setCubSV("muda_bananeira",q, xa+1,ya,za, xt,yt,zt);
							if(getCub(xa-1,ya-1,za, xt,yt,zt)==tTerraAradaRegada) setCubSV("muda_bananeira",q, xa-1,ya,za, xt,yt,zt);
							if(getCub(xa,ya-1,za+1, xt,yt,zt)==tTerraAradaRegada) setCubSV("muda_bananeira",q, xa,ya,za+1, xt,yt,zt);
							if(getCub(xa,ya-1,za-1, xt,yt,zt)==tTerraAradaRegada) setCubSV("muda_bananeira",q, xa,ya,za-1, xt,yt,zt);
						}
					}

					
				}
				return;
			}
			if(tip==tMudaBan){
				if((J.cont+xa+ya+za)%120==0)
				if(J.B(22) || opGrowFast)
				if(!temCubViz("caule_bananeira",xa,ya,za,xt,yt,zt)){
					int cb = getCub(xa,ya-1,za,xt,yt,zt);
					if(J.vou(cb,
							getInd("terra_arada_regada"),
							getInd("caule_bananeira"))){
								wGrow.play();
								plotArv("folha_bananeira",xa,ya,za,xt,yt,zt);
					} else {
						setCub("muda_seca",xa,ya,za,xt,yt,zt);
					}
				}
				return;				
			}
			if(tip==tFolhaBan){
				// gerar penca... acho q nao precisa crescer pq ban serao baixas
				if((J.cont+xa+ya+za)%120==0)
				if(J.B(20) || opGrowFast)
				if(getCub(xa,ya-1,za, xt,yt,zt)==tCauleBan)
				if(getCub(xa,ya-2,za, xt,yt,zt)==tCauleBan)
				if(getCub(xa,ya-3,za, xt,yt,zt)==tCauleBan)
				if(temCubCol("terra_arada_regada",10, xa,ya-10,za,xt,yt,zt)){
					if(setCubSV("penca_banana_crescendo",6,xa,ya-1,za+1, xt,yt,zt)) // soh cresce p norte
						; //wGrow.play(); // NORTE É Z+1!!!
				}					
				return;
			}
			if(tip==tMudaCoq){
				if((J.cont+xa+ya+za)%120==0)
				if(J.B(20) || opGrowFast)					
				if(ehUmDesses("areia","terra","terra_arada","terra_arada_regada",xa,ya-1,za,xt,yt,zt))
					plotArv("folha_coq",xa,ya,za,xt,yt,zt); // pensei em plotar um seed aqui, mas ia aparecer o cubo vermelho num flick. Acho q nao ia ficar legal.
				return;
			}
			if(tip==tMaca || tip==tMacaVerde){
				
				if(tip==tMacaVerde)
				if((J.cont+xa+ya+za)%100==0)
				if(J.B(22))
					setCub(tMaca,xa,ya,za,xt,yt,zt);
				
				if((J.cont+xa+ya+za)%10==0) // caindo
				if(getCub(xa,ya+1,za,xt,yt,zt)==0)
				if(getCub(xa,ya-1,za,xt,yt,zt)==0)
					moveSV(BXO, xa,ya,za,xt,yt,zt);				
				return;
			}

			if(tip==tFolhaMacieira){
				if((J.cont+xa+ya+za)%100==0)
				if(J.B(12))	
				if((xa+ya+za)%4==0){
					// tem um bug aqui					
					if(getCub(xa,ya-1,za,xt,yt,zt)==0)
						setCub("maca_verde",xa,ya-1,za,xt,yt,zt);
					else{						
						setCub("folha_maca_verde",xa,ya,za,xt,yt,zt);
					}
				}
				return;
			}
			if(tip==tFolhaMacaVerde){
				if((J.cont+xa+ya+za)%100==0)
				if(J.B(12)){
					setCub("folha_maca",xa,ya,za,xt,yt,zt);
				}
				return;
			}			
			if(tip==tMudaSeringueira){
				if(J.C(100))
				if(J.B(20) || opGrowFast){
					int v = getCub(xa,ya-1,za,xt,yt,zt);
					v = semDegrau(v);
					if(!temCubViz(tAgua,xa,ya-1,za,xt,yt,zt)) // na minha pesquisa, seringueira nao gosta de agua e vai bem em solo arenoso (mas nao vou considerara arenoso)
					if(J.vou(v,
							getInd("grama"),
							getInd("terra"), // arada e ragada NAO, seringueira nao gosta de solo enxarcado.
							getInd("terra_arada")))	{ 
								wGrow.play();
								plotArv("folha_seringueira",xa,ya,za,xt,yt,zt);
							}
				}
				return;				
			}
			if(tip==tMudaSilvestre){
				if(J.C(100))
				if(J.B(20) || opGrowFast){
					int v = getCub(xa,ya-1,za,xt,yt,zt);
					v = semDegrau(v);
					if(J.vou(v,
							getInd("grama"),
							getInd("terra"),
							getInd("terra_arada"),
							getInd("terra_arada_regada"))){
								wGrow.play();
								plotArv(-1,xa,ya,za,xt,yt,zt);
					}
				}
				return;				
			}
			if(tip==tMudaMacieira){
				if(J.C(100))
				if(J.B(20) || opGrowFast)
				if(J.vou(
						getCub(xa,ya-1,za,xt,yt,zt),
						getInd("grama"),
						getInd("terra"),
						getInd("terra_arada"),
						getInd("terra_arada_regada"))){
							wGrow.play();
							plotArv("folha_macieira",xa,ya,za,xt,yt,zt);
				}
				return;				
			}
			if(tip==tAguaCel){
				if((J.cont+xa+ya+za) % 60 == 0){
					if(J.B(12)) if(getCub(xa,ya,za+1,xt,yt,zt)==tGrama) setCub("grama_cel",xa,ya,za+1,xt,yt,zt);
					if(J.B(12)) if(getCub(xa,ya,za-1,xt,yt,zt)==tGrama) setCub("grama_cel",xa,ya,za-1,xt,yt,zt);
					if(J.B(12)) if(getCub(xa+1,ya,za,xt,yt,zt)==tGrama) setCub("grama_cel",xa+1,ya,za,xt,yt,zt);
					if(J.B(12)) if(getCub(xa-1,ya,za,xt,yt,zt)==tGrama) setCub("grama_cel",xa-1,ya,za,xt,yt,zt);
				}
				return;
			} 	
			if(tip==tAcido){
				/* isso nao ficou bom
				if(J.C(6)){
					int t = tAreia;
					boolean foi = false;
					if(J.B(6)) if(acidoCorroi(xa,ya,za+1,xt,yt,zt)) { setCub(t,xa,ya,za+1,xt,yt,zt); foi=true; }
					if(J.B(6)) if(acidoCorroi(xa,ya,za-1,xt,yt,zt)) { setCub(t,xa,ya,za-1,xt,yt,zt); foi=true; }
					if(J.B(6)) if(acidoCorroi(xa+1,ya,za,xt,yt,zt)) { setCub(t,xa+1,ya,za,xt,yt,zt); foi=true; }
					if(J.B(6)) if(acidoCorroi(xa-1,ya,za,xt,yt,zt)) { setCub(t,xa-1,ya,za,xt,yt,zt); foi=true; }
					if(foi) setCub(t, xa,ya,za,xt,yt,zt);
				}
				return;
				*/
			} 				
			if(tip==tLavaCorr){
				regFluido(tLava,tLavaCorr, 12);
				return;
			} 			
			if(tip==tMagma){
				regFluido(tRochaMagma,tMagma, 10);
				return;
			} 
			if(tip==tFolhaCoq){
				// crescendo... depois

				// gerando cocos
				if((J.cont+xa+ya+za)%120==0 || opGrowFast)
				if(J.B(22))					
				if(ehEsse("caule_coq",xa,ya-1,za,xt,yt,zt)) // altura 4 p comecar a gerar coco
				if(ehEsse("caule_coq",xa,ya-2,za,xt,yt,zt))
				if(ehEsse("caule_coq",xa,ya-3,za,xt,yt,zt))
				if(ehEsse("caule_coq",xa,ya-4,za,xt,yt,zt)){
					int m = J.RS(2);
					int o = J.RS(2);
					setCubSV("coco",0,xa+m,ya-1,za+o,xt,yt,zt);
				}
				return; 
			}
			if(tip==tCactoBase){
				if((J.cont+xa+ya+za) % 60==0)
					if(J.B(20)) 
						if(getCub(xa,ya+1,za,xt,yt,zt)==0)
							setCub(tCactoTopo, xa,ya+1,za,xt,yt,zt);
				return; 				
			}
			if(tip==tCactoTopo){
				if((J.cont+xa+ya+za) % 60==0)
				if(J.B(20)) 
					if(getCub(xa,ya-1,za,xt,yt,zt)==tCactoBase)
					if(getCub(xa,ya-2,za,xt,yt,zt)==tCactoBase)
					if(getCub(xa,ya-3,za,xt,yt,zt)==tAreia)
					if(getCub(xa,ya+1,za,xt,yt,zt)==0){
						setCub(tCactoBase, xa,ya,za,xt,yt,zt);
						setCub(tCactoTopo, xa,ya+1,za,xt,yt,zt);
					}
				return; 				
			}			
			if(tip==tAlga){
				if((J.cont+xa+ya+za) % 300==0) // nao existirá alga fora dagua
					if(!J.vou(getCub(xa,ya+1,za,xt,yt,zt),tAgua,tAlga)){
						zCub();
						return;
					}	
					
				if((J.cont+xa+ya+za) % 60==0)
				if(getInf(xa,ya,za,xt,yt,zt)>33){  // alastrar mais rapido
					if(opGrowFast || J.B(20)){
						int mm=xa+J.RS(2);
						int nn=ya+J.RS(2);
						int oo=za+J.RS(2);
						int cb = getCub(mm,nn-1,oo,xt,yt,zt);
						int cc = getCub(mm,nn+1,oo,xt,yt,zt);
						int ccc = getCub(mm,nn+2,oo,xt,yt,zt);
		
						Cub cm = getCubMod(cb);
						boolean al = false;
						if(cm.ehTijolo) al=true;
						else if(cm.ehMinerio) al=true;
						else if(cm.ehTanque) al=true;
						else if(cm.ehCano) al=true;
						else if(J.vou(cb,tFerroVelho,tMarmore)) al=true;
						// achei melhor deixar " r o c h  a " de fora p ao baquear o frame-rate
					
						if(al)
						if(cc==tAgua)
						if(ccc==tAgua) // serah q tah bom este critehrio??? 
							setCubSV(tAlga,6, mm,nn,oo,xt,yt,zt);
						
					}
				}
				//return; // aqui nao, tem o   o p C r e s c e 1 0 0  lah p baixo
			}
			if(tip==tMato){
				if((J.cont+xa+ya+za) % 300==0) // nao existirá capim sobre folha (aquele bug)
					if(getCubMod(xa,ya-1,za,xt,yt,zt).ehFolha){
						zCub();
						return;
					}					
				
				if((J.cont+xa+ya+za) % 60==0){
					if(J.B(20)){

						int vb = getCub(xa,ya-1,za,xt,yt,zt);						
						if(getCubMod(vb).ehFolha || vb==0) zCub();
						
						int mm=xa+J.RS(2);
						int oo=za+J.RS(2);
						int cb = getCub(mm,ya-1,oo,xt,yt,zt);
						if(J.vou(cb,tGrama,tTerra,tTerraArada,tTerraAradaRegada,tAreia,tChamuscado)){

							// mato transforma terra nua em grama
							if(J.vou(cb,tAreia,tChamuscado)) // outra areia depois
								setCub(tTerra, mm,ya-1,oo,xt,yt,zt);
							else if(J.vou(cb,tTerra,tTerraArada,tTerraAradaRegada))
								setCub(tGrama, mm,ya-1,oo,xt,yt,zt);
							

							opPlotSobreSolo=true;
							setCubSV(tMato-1,0, mm,ya,oo,xt,yt,zt);
							//wGet.play(); // debug
						}
						opPlotSobreSolo=false;
					}
				}
				//return;  // este return nao eh bom aqui, jah q tem o opCresce100 lah p baixo
			}
			if(ehCond){					
				
				if((J.cont+xa+ya+za)%47==0){
					corrDisEl();					
					if(tip==tCond0) life=0;
				}
					
				if((J.cont-xa-ya-za)%47==0){
					int d = 0; // meio porco, mas acho q funciona					
					d = J.CMA; if(getCubMod(getCubDir(d, xa,ya,za, xt,yt,zt)).ehCond) if(getCubMod(getCubDir(d, xa,ya,za, xt,yt,zt)).life<life) setCubDir(d, tip, xa,ya,za, xt,yt,zt);
					d = J.NOR; if(getCubMod(getCubDir(d, xa,ya,za, xt,yt,zt)).ehCond) if(getCubMod(getCubDir(d, xa,ya,za, xt,yt,zt)).life<life) setCubDir(d, tip, xa,ya,za, xt,yt,zt);
					d = J.LES; if(getCubMod(getCubDir(d, xa,ya,za, xt,yt,zt)).ehCond) if(getCubMod(getCubDir(d, xa,ya,za, xt,yt,zt)).life<life) setCubDir(d, tip, xa,ya,za, xt,yt,zt);					
				}
				if((J.cont+xa+ya+za)%47==0){
					int d = 0; // meio porco, mas acho q funciona										
					d = J.BXO; if(getCubMod(getCubDir(d, xa,ya,za, xt,yt,zt)).ehCond) if(getCubMod(getCubDir(d, xa,ya,za, xt,yt,zt)).life<life) setCubDir(d, tip, xa,ya,za, xt,yt,zt);
					d = J.SUL; if(getCubMod(getCubDir(d, xa,ya,za, xt,yt,zt)).ehCond) if(getCubMod(getCubDir(d, xa,ya,za, xt,yt,zt)).life<life) setCubDir(d, tip, xa,ya,za, xt,yt,zt);
					d = J.OES; if(getCubMod(getCubDir(d, xa,ya,za, xt,yt,zt)).ehCond) if(getCubMod(getCubDir(d, xa,ya,za, xt,yt,zt)).life<life) setCubDir(d, tip, xa,ya,za, xt,yt,zt);				
				}
				return;
			}
			if(tip==tBateriaFixa){ // ?Já tirou o duplicado abaixo???
				if((J.cont+xa+ya+za) % tmpRede == tmpIncRede){					
				
					{ // carregando cond com bat
						if(temSwitchOnViz(xa,ya,za, xt,yt,zt))
						if(getCubMod(xa,ya-1,za, xt,yt,zt).ehCond){
							
							if(getCub(xa,ya-1,za, xt,yt,zt)==tCond0){
								setCub(getCondDisp(),  xa,ya-1,za, xt,yt,zt);
							} else {
								if(decInfLm(1,0,xa,ya,za,xt,yt,zt))
									getCubMod(xa,ya-1,za, xt,yt,zt).life++;
							}
							
						}
					}	
					{ // carregando bat com cond
						// nao precisa de switch p recarregar a bat (?Isso é bom???)
						if(getCubMod(xa,ya+1,za, xt,yt,zt).ehCond)
						if(getCubMod(xa,ya+1,za, xt,yt,zt).life>0) // se tem el disponivel em cond
						if(getCub(xa,ya+1,za, xt,yt,zt)!=tCond0) // soh p garantir
						if(getInf(xa,ya,za, xt,yt,zt)<maxElBat){ // se ainda cabe el na bateria
							incInfLm(1,maxElBat,xa,ya,za, xt,yt,zt);
							getCubMod(xa,ya+1,za, xt,yt,zt).life--;						
						{																									
					}
				}
			}
				}
			}
			if(tip==tBateriaFixa){
				// carrega/descarrega de 1 em 1 
				// nao seria melhor alterar isso???
				// poderia carregar tirando o maximo da rede e descarregando aos poucos, ou ao contrario
				// precisa de switch p descarregar

				// ACHO Q ISSO AQUI TÁ PESADO P CLOCK...
				
				// 0..1000
				int vi = getInf(xa,ya,za,xt,yt,zt);

				// 90 abaixo eh a faixa de cor em J.cor[]
				int cr = 90+(int)(vi*0.009f); 
				// 0..1000 -> 0..9 + 90
				
				dtNor.ccrNov = J.cor[cr];
				dtSul.ccrNov = J.cor[cr];
				dtLes.ccrNov = J.cor[cr];
				dtOes.ccrNov = J.cor[cr];
				
				//int q = 10;
				
				// CARREGAR bateria_fixa com uma rede acima dela
				if((J.cont+xa+ya+za) % tmpRede == tmpDecRede)
				if(getCub(xa,ya+1,za,xt,yt,zt)==tRede1) // outras redes depois
					if(vi<maxElBat) // baterias_fixas ateh o nivel 1000
					if(tCub.get(tRede1).life>0){
						cTreme=tmpRede;																			
						tCub.get(tRede1).life--;
						incInf(xa,ya,za,xt,yt,zt);
					}
					
				// DEScarregar bateria_fixa com uma rede abaixo dela
				if((J.cont+xa+ya+za) % tmpRede == tmpIncRede)
				if(temSwitchOnViz(xa,ya,za,xt,yt,zt))	// soh descarrega com s w i t c h  o n
				if(getCub(xa,ya-1,za,xt,yt,zt)==tRede1) // outras redes depois
					if(getInf(xa,ya,za,xt,yt,zt)>0) // baterias_fixas ateh o nivel 1000
					if(tCub.get(tRede1).life<maxRede1){
						cTreme=tmpRede;																			
						tCub.get(tRede1).life++;
						// mas tah ruim  c a r r e g a r   e   d e s c a r r e g a r   de 1 em 1. Melhorar depois.
						decInf(xa,ya,za,xt,yt,zt);
					}					
				return;
			}
			if(tip==tPump){
				// nao mais enche tanques acima, apenas canos
				int q=6; // quantidade de el p funcionar.
				if((J.cont+xa+ya+za) % tmpRede == tmpDecRede)
				if(getCubMod(xa,ya+1,za,xt,yt,zt).ehCano)
				if(temElViz(q, xa,ya,za,xt,yt,zt))
				if(temSwitchOnViz(xa,ya,za,xt,yt,zt)){
					int nc = getInf(xa,ya+1,za,xt,yt,zt);
					int tc = getCub(xa,ya+1,za,xt,yt,zt);
					int tb = getCub(xa,ya-1,za,xt,yt,zt);
					
					if(tb==0) setCub(tSonda,xa,ya-1,za,xt,yt,zt);
					
					// depois: petroleo, fluidozetta, acido, lava, agua cel, etc
					if(J.vou(tb,tAgua,tSondaAgua))
					if(nc<6)
					if(J.vou(conteudo(tc),tAgua,0)){
						// deveria consumir agua, neh?
						if(tb==tSondaAgua) setCub(tSonda,xa,ya-1,za,xt,yt,zt);
						tc = tCanoAgua;
						setCub(tc,++nc,xa,ya+1,za,xt,yt,zt);
						wPump.agPlay(1000);
							cTreme=tmpRede;																			
						altRede(-q,tRede1, xa,ya,za,xt,yt,zt);
					}
					
					// juntar ao cod acima depois
					if(J.vou(tb,tPetroleo,tSondaPetro))
					if(nc<6)
					if(J.vou(conteudo(tc),tPetroleo,0)){
						if(tb==tSondaPetro) setCub(tSonda,xa,ya-1,za,xt,yt,zt);
						tc = tCanoPetro;
						setCub(tc,++nc,xa,ya+1,za,xt,yt,zt);
						wPump.agPlay(1000);
							cTreme=tmpRede;																			
						altRede(-q,tRede1, xa,ya,za,xt,yt,zt);										
					}					
				}
				return;
			}
			if(tip==tSerra){
				int q = 12; // qtd de el p funcionar
				if((J.cont+xa+ya+za) % tmpRede == tmpDecRede)
				if(getCub(xa,ya-1,za,xt,yt,zt)==0)	
				if(getCubMod(xa,ya+1,za,xt,yt,zt).ehCaule)
				if(temElViz(q, xa,ya,za,xt,yt,zt))
				if(temSwitchOnViz(xa,ya,za,xt,yt,zt))
				{
					String st="";
					if(ehEsse("caule",xa,ya+1,za,xt,yt,zt)) st="degrau_alto_caule";
					if(ehEsse("degrau_alto_caule",xa,ya+1,za,xt,yt,zt)) st="degrau_caule";
					if(ehEsse("degrau_caule",xa,ya+1,za,xt,yt,zt)) st="degrau_baixo_caule";
					if(ehEsse("degrau_baixo_caule",xa,ya+1,za,xt,yt,zt)) st="nulo";
					setCub(st, xa,ya+1,za,xt,yt,zt);
					setCub("madeira_facil", xa,ya-1,za,xt,yt,zt);
					altRede(-q, tRede1, xa,ya,za,xt,yt,zt);
					cTreme=tmpRede;																		
					wSerra.agPlay(1000);
				}
				return;
			}
			if(tip==tMaqAcucar){
				int q = 24; // qtd de el p funcionar
				if((J.cont+xa+ya+za) % tmpRede == tmpDecRede)
				if(getInf(xa,ya+1,za,xt,yt,zt)==maxVolTanque) // soh funciona com tanque cheio acima
				if(getInf(xa,ya-1,za,xt,yt,zt)<maxVolTanque)
				if(ehEsse(tTanqueAcucar,tTanqueVazio, xa,ya-1,za,xt,yt,zt))
				if(temElViz(q, xa,ya,za,xt,yt,zt))
				if(temSwitchOnViz(xa,ya,za,xt,yt,zt)){

					int cc = getCub(xa,ya+1,za,xt,yt,zt);
					int cb = getCub(xa,ya-1,za,xt,yt,zt);
					int nc = getInf(xa,ya+1,za,xt,yt,zt);
					int nb = getInf(xa,ya-1,za,xt,yt,zt);

					if(nb>=maxVolTanque) return; // mas jah foi visto isso
					if(nc!=maxVolTanque) return; // deixei por seguranca
					if(cc==tTanqueVazio) return;

					boolean foi = false;

					if(!foi)
					if(cc==tTanqueSucoMaca){
						setCub(tTanqueVazio, xa,ya+1,za,xt,yt,zt);
						setCub(tTanqueAcucar, nb+1, xa,ya-1,za,xt,yt,zt);
						foi = true;
					}

					if(!foi)
					if(cc==tTanqueGarapa){
						setCub(tTanqueVazio, xa,ya+1,za,xt,yt,zt);
						setCub(tTanqueAcucar, nb+5, xa,ya-1,za,xt,yt,zt);
						foi = true;
					}					

					if(foi){
						cTreme=tmpRede;																			
						wMaqFunc.agPlay(1000);		
						altRede(-q, tRede1, xa,ya,za,xt,yt,zt);						
						return;
					}
				}
				return;				
			}
			if(tip==tExtractor){
				int q = 6; // qtd de el p funcionar
				if((J.cont+xa+ya+za) % tmpRede == tmpDecRede)	
				if(temElViz(q, xa,ya,za,xt,yt,zt))
				if(temSwitchOnViz(xa,ya,za,xt,yt,zt)){
					
					// corrigindo tanques vazios
					if(ehEsse(tTanqueVazio,xa,ya-1,za,xt,yt,zt)){
						// esse trecho de correcao de tanque nao deve ser muito frequente, logo, nao me preocupei tanto com clock
						String st = null;
						
						// nao eh bom deixar esse monte de string abaixo, da lag (?ou "leg"???)
						if(ehUmDesses("areia_marciana","degrau_areia_marciana",xa,ya+1,za,xt,yt,zt)) st = "tanque_poh_ferro";
						if(ehUmDesses("areia_nether","degrau_areia_nether",xa,ya+1,za,xt,yt,zt))   st = "tanque_poh_enxofre";
						if(ehEsse("min_ferro",xa,ya+1,za,xt,yt,zt))   st = "tanque_poh_ferro";
						if(ehEsse("min_ouro",xa,ya+1,za,xt,yt,zt))   st = "tanque_poh_ouro";
						if(ehEsse("min_cobre",xa,ya+1,za,xt,yt,zt))   st = "tanque_poh_cobre";
						if(ehEsse("min_carvao",xa,ya+1,za,xt,yt,zt))   st = "tanque_poh_carvao";
						
						if(st!=null)
							setCub(st,xa,ya-1,za,xt,yt,zt);
					}
						
					// ?serah q isso abaixo tah engolindo clock???
					// R: provavelmente sim
						
					// funcionamento;
					

					
					if(ehUmDesses(tAreiaMarc,tDegrauAreiaMarc,xa,ya+1,za,xt,yt,zt))
					if(ehEsse(tTanqPohFerro,xa,ya-1,za,xt,yt,zt))
					if(incInfLm((J.B(6)?1:0),maxVolTanque, xa,ya-1,za, xt,yt,zt)){
						setCub(0, xa,ya+1,za,xt,yt,zt);
						wExtractor.agPlay(1000);
						altRede(-q, tRede1, xa,ya,za,xt,yt,zt);
							cTreme=tmpRede;																			
						return;
					}

					if(ehUmDesses(tAreiaNether,tDegrauAreiaNether,xa,ya+1,za,xt,yt,zt))
					if(ehEsse(tTanqPohEnxofre,xa,ya-1,za,xt,yt,zt))
					if(incInfLm((J.B(6)?1:0),maxVolTanque, xa,ya-1,za, xt,yt,zt)){
						setCub(0, xa,ya+1,za,xt,yt,zt);						
						wExtractor.agPlay(1000);
						altRede(-q, tRede1, xa,ya,za,xt,yt,zt);						
							cTreme=tmpRede;																			
						return;
					}

					
					// abaixo precisa ser otimizado, mas jah funciona
					if(ehEsse("min_ferro",xa,ya+1,za,xt,yt,zt))
					if(ehEsse("tanque_poh_ferro",xa,ya-1,za,xt,yt,zt))
					if(incInfLm(J.R(4)+1, maxVolTanque, xa,ya-1,za, xt,yt,zt)){
						setCub(0, xa,ya+1,za,xt,yt,zt);						
						wExtractor.agPlay(1000);
						altRede(-q, tRede1, xa,ya,za,xt,yt,zt);						
							cTreme=tmpRede;																			
						return;
					}
					
					if(ehEsse("min_ouro",xa,ya+1,za,xt,yt,zt))
					if(ehEsse("tanque_poh_ouro",xa,ya-1,za,xt,yt,zt))
					if(incInfLm(J.R(4)+1, maxVolTanque, xa,ya-1,za, xt,yt,zt)){
						setCub(0, xa,ya+1,za,xt,yt,zt);						
						wExtractor.agPlay(1000);
						altRede(-q, tRede1, xa,ya,za,xt,yt,zt);						
							cTreme=tmpRede;																			
						return;
					}				
					
					if(ehEsse("min_cobre",xa,ya+1,za,xt,yt,zt))
					if(ehEsse("tanque_poh_cobre",xa,ya-1,za,xt,yt,zt))
					if(incInfLm(J.R(4)+1, maxVolTanque, xa,ya-1,za, xt,yt,zt)){
						setCub(0, xa,ya+1,za,xt,yt,zt);						
						wExtractor.agPlay(1000);
						altRede(-q, tRede1, xa,ya,za,xt,yt,zt);						
							cTreme=tmpRede;																			
						return;
					}					

					if(ehEsse("min_carvao",xa,ya+1,za,xt,yt,zt))
					if(ehEsse("tanque_poh_carvao",xa,ya-1,za,xt,yt,zt))
					if(incInfLm(J.R(4)+1, maxVolTanque, xa,ya-1,za, xt,yt,zt)){
						setCub(0, xa,ya+1,za,xt,yt,zt);						
						wExtractor.agPlay(1000);
						altRede(-q, tRede1, xa,ya,za,xt,yt,zt);						
							cTreme=tmpRede;																			
						return;
					}					
					
				}				
				return;
			}
			if(tip==tMilhoMaduro){
				if(J.C(100))
				if(J.B(24)) // vira milho bege
					setCub(tMilhoSeco, xa,ya,za, xt,yt,zt);
				return;				
			}
			if(tip==tTerraArada){
				if((J.cont+xa+ya+za)%60==0)
				if(J.B(12))	
				if(temCubViz(tAgua, xa,ya,za, xt,yt,zt)
					||temCubViz(tAguaCel, xa,ya,za, xt,yt,zt))
					setCub(tTerraAradaRegada, xa,ya,za, xt,yt,zt);				
				return;
			}	
			if(tip==tTerraAradaRegada){
				if((J.cont+xa+ya+za)%60==0)
				if(J.B(12))	
				if(!temCubViz(tAgua, xa,ya,za, xt,yt,zt)
					&& !temCubViz(tAguaCel, xa,ya,za, xt,yt,zt))
					setCub(tTerraArada, xa,ya,za, xt,yt,zt);
				return;
			}

			if(J.vou(tip,tHasteNS,tHasteLO,tHasteCB)){
				// pequena gambiarra
				setCub(0, xa,ya,za,xt,yt,zt);
				return;
			}			
			if(tip==tTranspVazio){
				if((J.cont+xa+ya+za)%12==0)
				if(infImp.dis==0){
					corrDisTransp();				
				}
				return;
			}				
			if(ehTransp){ // nao vazio
				
				if(infImp.dis==0)
					corrDisTransp();
				
				float ld = 0.5f, ds=0.1f;
				zDxx();
				if(dir==LES) dxx=+ds*cnt;
				if(dir==OES) dxx=-ds*cnt;
				if(dir==NOR) dzz=+ds*cnt;
				if(dir==SUL) dzz=-ds*cnt;
				if(dir==CMA) dyy=+ds*cnt;
				if(dir==BXO) dyy=-ds*cnt;
				
				cnt++;
				
				//if((J.cont+xa*7+ya*5+za*3)%12==0)
				if(cnt>5)
				{
					cnt=-5;
					boolean foi = false;
					
					
					if(!foi)
					if(dir==NOR)
					if(getCub(xa,ya,za+1,xt,yt,zt)==tTranspVazio){
						zDxx();
						trocaPos(dir, xa,ya,za,xt,yt,zt);
						foi=true;
					}

					if(!foi)					
					if(dir==SUL)
					if(getCub(xa,ya,za-1,xt,yt,zt)==tTranspVazio){
						zDxx();
						trocaPos(dir, xa,ya,za,xt,yt,zt);					
						foi=true;					
					}
					if(!foi)					
					if(dir==LES)
					if(getCub(xa+1,ya,za,xt,yt,zt)==tTranspVazio){
						zDxx();
						trocaPos(dir, xa,ya,za,xt,yt,zt);					
						foi=true;						
					}
					if(!foi)					
					if(dir==OES)
					if(getCub(xa-1,ya,za,xt,yt,zt)==tTranspVazio){
						zDxx();
						trocaPos(dir, xa,ya,za,xt,yt,zt);										
						foi=true;
					}
					if(!foi)					
					if(dir==CMA)
					if(getCub(xa,ya+1,za,xt,yt,zt)==tTranspVazio){
						zDxx();
						trocaPos(dir, xa,ya,za,xt,yt,zt);										
						foi=true;						
					}
					if(!foi)					
					if(dir==BXO)
					if(getCub(xa,ya-1,za,xt,yt,zt)==tTranspVazio){
						zDxx();
						trocaPos(dir, xa,ya,za,xt,yt,zt);										
						foi=true;						
					}
					
					
					if(!foi){// reavaliar dir
				
						zDxx();
						int d=0;
						if(d==0) if(dir!=SUL) if(getCub(xa,ya,za+1,xt,yt,zt)==tTranspVazio) d=NOR;
						if(d==0) if(dir!=NOR) if(getCub(xa,ya,za-1,xt,yt,zt)==tTranspVazio) d=SUL;
						if(d==0) if(dir!=LES) if(getCub(xa-1,ya,za,xt,yt,zt)==tTranspVazio) d=OES;
						if(d==0) if(dir!=OES) if(getCub(xa+1,ya,za,xt,yt,zt)==tTranspVazio) d=LES;
						if(d==0) if(dir!=CMA) if(getCub(xa,ya-1,za,xt,yt,zt)==tTranspVazio) d=BXO;
						if(d==0) if(dir!=BXO) if(getCub(xa,ya+1,za,xt,yt,zt)==tTranspVazio) d=CMA;
						
						if(d==0){ // se for terminal
							dir = invDir(dir); 
							// tentar plotar depois

						}	else dir = d;
					}
					
				}
				return;
			}
			if(ehEsteira){
				op1ElBxo=true; // ver primeiro el abaixo				
				if((J.cont+xa+ya+za)%3==0)
				if(getCub(xa,ya+1,za,xt,yt,zt)!=0)
				if(temElViz(1,xa,ya,za,xt,yt,zt))
				{
					InfImp pc = getInfImp(xa,ya+1,za,xt,yt,zt);
					pc.ocNor=pc.ocSul=pc.ocLes=pc.ocOes=pc.ocCma=pc.ocTudo=false;
					float vEst = 0.1f; // velocidade da esteira

					if(tip==tEstNor) 
					if(J.vou(getCub(xa,ya+1,za+1,xt,yt,zt),0,tPlotar)){
						pc.idx=0f; pc.idy=0f; pc.idz+=vEst;
						if(pc.idz>=0.5f){
							pc.idz=0f;
							if(moveSV(NOR, xa,ya+1,za,xt,yt,zt)) getInfImp(xa,ya+1,za+1,xt,yt,zt).idz=-0.5f;						
						}
					}
					if(tip==tEstSul) 
					if(J.vou(getCub(xa,ya+1,za-1,xt,yt,zt),0,tPlotar)){
						pc.idx=0f; pc.idy=0f; pc.idz-=vEst;
						if(pc.idz<=-0.5f){
							pc.idz=0f;
							if(moveSV(SUL, xa,ya+1,za,xt,yt,zt)) getInfImp(xa,ya+1,za-1,xt,yt,zt).idz=+0.5f;
						}
					}					
					if(tip==tEstLes) 
					if(J.vou(getCub(xa+1,ya+1,za,xt,yt,zt),0,tPlotar)){
						pc.idx+=vEst; pc.idy=0f; pc.idz=0f;
						if(pc.idx>=+0.5f){
							pc.idx=0f;
							if(moveSV(LES, xa,ya+1,za,xt,yt,zt)) getInfImp(xa+1,ya+1,za,xt,yt,zt).idx=-0.5f;
						}
					}										
					if(tip==tEstOes) 
					if(J.vou(getCub(xa-1,ya+1,za,xt,yt,zt),0,tPlotar)){
						pc.idx-=vEst; pc.idy=0f; pc.idz=0f;
						if(pc.idx<=-0.5f){
							pc.idx=0f;
							if(moveSV(OES, xa,ya+1,za,xt,yt,zt)) getInfImp(xa-1,ya+1,za,xt,yt,zt).idx=+0.5f;
						}
					}										

					
				}
				return;	
			}
			if(opTemFogoQueAlastra){// opFogoAlastra opChama  ffffffffffffffffffff
				if(tip==tFogo) J.opQtdNoise=10; // usar isso p dar choque em mobs depois			
				if(tip==tChama) {J.opQtdNoise=10; return; } // usar isso p dar choque em mobs depois			
				if((J.cont+xa+ya+za+xt*10+yt*10+zt*10)%12==0){
					if(cSomFogo==0) {cSomFogo=tmpSomFogo; wFogo.loop(); } else cSomFogo=tmpSomFogo;
					int m=xa+J.RS(2);
					int n=ya+J.RS(2);
					int o=za+J.RS(2);
					int v = getCub(m,n,o,xt,yt,zt);
					if(v==tTnt) setCub(tTntOn,m,n,o,xt,yt,zt);
					int vv = comFogo(v);
					if(J.B(6)) if(getCubMod(vv).opTemFogoQueAlastra) vv=0;
					if(v!=vv) setCub(vv, m,n,o,xt,yt,zt);					
				}					
				return;
			}
			if(J.vou(tip,tTerraCel,tGramaCel)){// degraus tb???
				if((J.cont+xa+ya+za+xt*10+yt*10+zt*10)%12==0){
					int q = 6;
					if(J.B(q)) setCubSV("nuvem",0,xa,ya,za+1,xt,yt,zt);
					if(J.B(q)) setCubSV("nuvem",0,xa,ya,za-1,xt,yt,zt);
					if(J.B(q)) setCubSV("nuvem",0,xa+1,ya,za,xt,yt,zt);
					if(J.B(q)) setCubSV("nuvem",0,xa-1,ya,za,xt,yt,zt);
					if(J.B(q)) setCubSV("nuvem",0,xa,ya-1,za,xt,yt,zt);
				}
				return;
			}
			if(opEhPortal){
				if(J.noInt(xt,meioTab-1,meioTab+1))	
				if(J.noInt(zt,meioTab-1,meioTab+1))	
				if(!wPortalProx.isPlaying())	
					wPortalProx.play();			
				return;
			}
			if(ehBotaoOn) if(cPlot<=0){		// botoes devem voltar sozinhos
				wSwitchOff.play();
				setCub(tip-1,xa,ya,za,xt,yt,zt);
				return;
			}
			if(ehSeedMob)	if(J.C(12))	if(getSlotLivreMob()!=-1){
				setCub(0, xa,ya,za, xt,yt,zt);
				if(J.iguais("zumbi",name)) iniMob(tZumbi,xa,ya,za,xt,yt,zt);
				if(J.iguais("aldea",name)) iniMob(tAldea,xa,ya,za,xt,yt,zt);
				if(J.iguais("aldeao",name)) iniMob(tAldeao,xa,ya,za,xt,yt,zt);
				if(J.iguais("bruxa",name)) iniMob(tBruxa,xa,ya,za,xt,yt,zt);
				if(J.iguais("mago",name)) iniMob(tMago,xa,ya,za,xt,yt,zt);
				if(J.iguais("ghost",name)) iniMob(tGhost,xa,ya,za,xt,yt,zt);
				if(J.iguais("minotauro",name)) iniMob(tMinotauro,xa,ya,za,xt,yt,zt);
				if(J.iguais("caveira",name)) iniMob(tCaveira,xa,ya,za,xt,yt,zt);
				
				if(J.iguais("galinha",name)) iniMob(tGalinha,xa,ya,za,xt,yt,zt);
				if(J.iguais("avestruz",name)) iniMob(tAvestruz,xa,ya,za,xt,yt,zt);
				if(J.iguais("creeper",name)) iniMob(tCreeper,xa,ya,za,xt,yt,zt);
				if(J.iguais("porco",name)) iniMob(tPorco,xa,ya,za,xt,yt,zt);
				if(J.iguais("vaca",name)) iniMob(tVaca,xa,ya,za,xt,yt,zt);
				
				if(J.iguais("cavalo",name)) iniMob(tCavalo,xa,ya,za,xt,yt,zt);
				if(J.iguais("ovelha",name)) iniMob(tOvelha,xa,ya,za,xt,yt,zt);
				if(J.iguais("urso",name)) iniMob(tUrso,xa,ya,za,xt,yt,zt);
				if(J.iguais("aranha",name)) iniMob(tAranha,xa,ya,za,xt,yt,zt);
				return;				
				
			}
			if(ehTanque) if((J.cont+xa+ya+za)%16==0){
				int nv = getInf(xa,ya,za,xt,yt,zt);
				int cc = getCub(xa,ya+1,za,xt,yt,zt);
				int cb = getCub(xa,ya-1,za,xt,yt,zt);
				int nb = getInf(xa,ya-1,za,xt,yt,zt);

				// convertendo tanque vazio segundo cano acima
				if(tip==tTanqueVazio)
				if(getCubMod(xa,ya+1,za,xt,yt,zt).ehCano)
				if(getInf(xa,ya+1,za,xt,yt,zt)>0){
					setCub(tanqueDe(conteudo(cc)),0,xa,ya,za,xt,yt,zt);
					return;
				}
				
				// escorrendo p tanque de baixo
				if(nv>0)
				if(J.vou(cb,tip,tTanqueVazio))
				if(nb<maxVolTanque){					
					//if(nv==1) setCub(tTanqueVazio,xa,ya,za,xt,yt,zt);
					decInfLm(1,0,xa,ya,za,xt,yt,zt);
					setCub(tip,nb+1, xa,ya-1,za,xt,yt,zt); // isso jah corrige se for vazio
					return;
				}
				
				// esvaziando por cano baixo
				if(nv>0){
					if(tCub.get(cb).ehCano){
						if(cb==tCanoVazio) cb=canoDe(conteudo(tip));
						if(nb<maxVolCano){
							//if(nv==1) setCub(tTanqueVazio,xa,ya,za,xt,yt,zt);							
							decInfLm(1,0,xa,ya,za,xt,yt,zt);
							setCub(cb,++nb,xa,ya-1,za,xt,yt,zt);
							return;
						}
					}
				}
				
				// enchendo por cano acima
				//int cc = getCub(xa,ya+1,za,xt,yt,zt);
				int nc = getInf(xa,ya+1,za,xt,yt,zt);
				if(tip==tTanqueVazio) tip=tanqueDe(conteudo(cc));
				
				if(nv<maxVolTanque)
				if(tCub.get(cc).ehCano)
				if(conteudo(cc)==conteudo(tip))
				if(nc>0){
					decInfLm(1,0,xa,ya+1,za,xt,yt,zt);
					setCub(tip,++nv,xa,ya,za,xt,yt,zt);
					return;
				}	
				
				return;
			}
			if(ehCano){
				if((J.cont+xa+ya+za)%1000==0){				
					//wPop.play();
					corrDisCano(); // verificar de tempos em tempos
				}
				if((J.cont+xa+ya+za)%12==0){				
					if(infImp.ic==0) corrDisCano(); // pega a primeira vez q o cano nao foi ajustado
					for(int q=0; q<10; q++)
					if(regCano(J.rnd(NOR,SUL,LES,OES,CMA,BXO), xa,ya,za,xt,yt,zt)){
						corrDisCano();
						if(infImp.inf==0)	setCub(tCanoVazio,0,xa,ya,za,xt,yt,zt);					
						return;
					}
					return;
				}
			}
			if(opAreiaCai){
				if((J.cont-xa-ya-za)%5==0){				
					cTreme=tmpRede;// mais p debug
					if(moveSV(BXO,xa,ya,za,xt,yt,zt))	; // nao fazer nada aqui
					else setCub(semCai(tip),xa,ya,za,xt,yt,zt);					
				}
				return;
			}
			if(opCresce100) if((J.cont+xa+ya+za) %10 == 0) {
				int vi = getInf(xa,ya,za, xt,yt,zt);
				
				// corrigindo bug de mapas antigos
				//if(vi>100) setInf(100, xa,ya,za, xt,yt,zt); // jah nao precisa e andou bugando a  a l g a
				
				//infImp.inf = vi;
				tn=ts=tl=to=vi/200f;
				tc=-0.5f+tn+tn;				
				if(opGrowFast || J.B(6)){ // este J.B() poderia variar conforme a estacao do ano, planeta, tipo de solo, etc
					if(vi<100) {
						setInf(++vi, xa,ya,za, xt,yt,zt);												
						//infImp.inf = vi;
						
						if(!opAniAlga) // melhorar isso depois
						if(vi>=100){
							setCub(tip+1,0, xa,ya,za, xt,yt,zt);						
							wGrowTrigo.play();
						}
						
					}
				}
				return;
			}
		}

			float decDegrau=0f; // isso eh mais p mobs em  i m p C e n
		public void imp(float i, float j, float k, InfImp p){ // impCub
			opSupAgua=p.opSupAgua;
			{// autocorrecao
				if(opAreiaCai)operavel=true; 				
				if(ehTanque) operavel=true; 				
				
				if(cubCen!=null)temPosImp=true;
			}
			{// altura geral
				j-=yJoe;
				j+=altJoe;
				if(opBounceWithJoe) j+= yBounceJoe; // corrigindo o interior de tanques
				j+=2.3f; // calibragem fina			
				// isso nao tah exato, mas tah funcionando
				
				if(opPisandoNaAgua)
					j+=0.5f;				
			}
			{// tremor e tremendo
				if(cTreme>0) cTreme--; // usei nos mobs
				if(p.cTreme>0) p.cTreme--;
					
				if(opTreme || cTreme>0 || p.cTreme>0){ 
					// cTreme pega um cubo-modelo. Ex: se um moedor tremer, todos os cubos moedores do mapa tremerao
					// p.cTreme é apenas p cubo na coordenada especifica
					int q = 10;
					i+=J.RS(q)/300f; // veja: opTreme eh p tremer p sempre
					j+=J.RS(q)/300f; // cTreme, claro, eh temporario
					k+=J.RS(q)/300f;
				}
				if(cTerr>0){
					i+=xTerr;
					j+=yTerr;
					k+=zTerr;
				}				
			}
			{// deslocamento fino de idx, (q veio de " I n f I m p ")
				if(p!=null){					
					if(p.idx!=0) i+=p.idx;
					if(p.idy!=0) j+=p.idy;
					if(p.idz!=0) k+=p.idz;
				}
			}
			xImp = i+dx; yImp=j+dy; zImp=k+dz;
			{ // verificando infImp
				if(p==null) return;
				infImp = p;					
			}
			{ // calculando vertices e fora da tela
				atrasDoOlho=false;
				foraDaTela=false;			
				
				calcVerts();
				
				if(foraDaTela) return;
				if(atrasDoOlho) return;				
			}
			hit = 0; // isso precisa ficar antes da impressao de dtCen
			if(opGirDtCen) // p  p a c k s, mas dah p expandir p outros tb
			if(dtCen!=null) dtCen.giraTudo(+0.1f); // CUIDADO! Packs sao unicos no modelo. Nao fica bom adotar isso p ferr plotadas, jah q elas girariam mais de uma vez, conforme o numero de blocos ferr na tabela. Veja q isso eh bom apenas p girar cubos nao-modelo (ferr plotadas e grama sao modelo, packs, mobs e transportadores sao nao-modelo)
			{ // mobs
				if(ehMob){
					if(ativo){
						decDegrau=0;
						if(getCubMod(xa,ya-1,za,xt,yt,zt).ehDegrau)
						//if(tCub.get(getCub(xa,ya-1,za,xt,yt,zt)).ehDegrau)
							decDegrau=0.5f;
						impCen();
						cntMob++;
						regMob();
						if(life<100 || mobMsg!=null)
							impLifeMsgMob();
						if(hit!=0) defHit();
						numCubImp++; // mais exatidao
						decDegrau=0;
					}
					return;
				}
			}
			{ // ajustando luz da agua e portal
				opNivLuzAgua = 0;
				if(opLuzAgua) 
					opNivLuzAgua=(int)( 2f*getSin((J.cont>>3)+xa+za+xt*10+yt*10+zt*10));

				if(opLuzPortal)
					luzPortal=(int)( 2f*getSin((J.cont)+xa+ya+za+xt*10+yt*10+zt*10));				
			}
			if(operavel) regCub(); // precisa ser antes de imprimir, senao buga a  b a t e r i a
			{ // fog segundo distancia
				float dj = distJoe(xa,ya,za,xt,yt,zt);
				JPal.setTing(dj/disFog, crFog);
			}
			{ // imprimindo faces e retornando hit
			
			
				// baixo
				if(!opOmiteFaceBxo)	
					if(!p.ocBxo) 
						if(crBxo!=null) 
							impBxo();
			
				// ?mas como fica a impressao de dt sem a face???
				// cima				
				//if(ehEsteira)p.ocCma=false; // pequena gambiarra
				
				if(ehDegrau) p.ocCma=false;
				
				if(!opOmiteFaceCma || opSupAgua)
						if(!p.ocCma || opSupAgua)
							if(crCma!=null) 
								impCma(); 
							
				// laterais			
				if(!p.ocNor) if(crNor!=null) impNor();
				if(!p.ocSul) if(crSul!=null) impSul();
				if(!p.ocLes) if(crLes!=null) impLes();
				if(!p.ocOes) if(crOes!=null) impOes();
				
				if(dtCen!=null) impCen();							
				
				numCubImp++;				

				if(!selecionavel) 
							hit=0;// p interiores de tanques e laterais de fios el
				
				if(hit!=0) defHit();
				J.opQtdNoise=0;
			}
			if(temPosImp){
				j+=yJoe;
				j-=altJoe;
				j-=2.3f;
				if(opPisandoNaAgua)	j-=0.5f;
				float ilp = 0.5f-0.16f; // incremento lateral p posImp			
				
				{ // posImp central					
					if(!ehMob)
					if(cubCen!=null){
						
						if(opAltInfTanq) // de 0 a 20, maxVolTanque tinha q tah aqui
							cubCen.tc = -0.49f+getInf(xa,ya,za,xt,yt,zt)/22f;
						
						if(opAltInfCano) // de 0 a 6, maxVolCano tinha q tah aqui
							cubCen.tc = -0.49f+getInf(xa,ya,za,xt,yt,zt)/6.5f;							
						
						if(opOscTanq) 
							cubCen.tc-= getSin(J.cont)/50f;
						if(opAlfaInf20) 
							JPal.opAlfa = (float)(0.1f+p.inf/20f);
						//p.inf; 
						// 0..20
						// 0..1f
						cubCen.imp(i,j,k, infImpTanque);
						JPal.opAlfa = 0f;
					}					
				}
				{ // posImp laterais
					if(!temPosImp) return; 

					infCondLatNS.inf=p.inf; // vish... até esqueci o q eu fiz aqui. Faz muito tempo q eu programo isso e o fonte tá GIGANTE!!!
					infCondLatLO.inf=p.inf;
					infCondLatCB.inf=p.inf;
					
					// ajustar pela bussola depois
					if(cubNor!=null) cubNor.imp(i,j,k+ilp, infCondLatNS); 
					if(cubSul!=null) cubSul.imp(i,j,k-ilp, infCondLatNS); 
					if(cubLes!=null) cubLes.imp(i+ilp,j,k, infCondLatLO); 
					if(cubOes!=null) cubOes.imp(i-ilp,j,k, infCondLatLO); 
					if(cubCma!=null) cubCma.imp(i,j+ilp,k, infCondLatCB); 
					if(cubBxo!=null) cubBxo.imp(i,j-ilp,k, infCondLatCB); 
					
				}
			}
			checkClickReact(); // soh pode reagir a clique se a coord de hit jah foi verificada, ou seja, tem q imprimir primeiro.			
		}

		public void defHit(){
				stHit = name;
				
				dHit_ = dHit;
				xaHit_ = xaHit;
				yaHit_ = yaHit;
				zaHit_ = zaHit;
				xtHit_ = xtHit;
				ytHit_ = ytHit;
				ztHit_ = ztHit;
				
				dHit = hit;
				xaHit = xa;
				yaHit = ya;
				zaHit = za;
				xtHit = xt;
				ytHit = yt;
				ztHit = zt;
		}

		public void calcVerts(){
			// ponto central do cubo
			J.Coord3 cen = new J.Coord3(xImp,yImp,zImp);

				
//J.opDebug=true;
//VER J.ajHit !!!
			J.opVerHit=false; 
			if(J.noInt(cen.X, J.xPonto-margemXhit, J.xPonto+margemXhit))
			if(J.noInt(cen.Y, J.yPonto-margemYhit, J.yPonto+margemYhit))
				J.opVerHit=true;

			atrasDoOlho=false;
			if(cen.ao){
				atrasDoOlho = true;
				return;
			}
			// daria p lapidar isso pegando os pontos 2d extremos p x e y, mas achei melhor economizar clock
			// !!!bug da parede proxima raioX aqui! 9999999999999
			if(xt!=meioTab)
			if(zt!=meioTab)
			// if(!J.noInt(xt,meioTab-2,meioTab+2))
			// if(!J.noInt(zt,meioTab-2,meioTab+2))
			{
				if(cen.X<J.xPonto-margemX) {foraDaTela=true; return;}
				if(cen.X>J.xPonto+margemX) {foraDaTela=true; return;}
				if(cen.Y<J.yPonto-margemY) {foraDaTela=true; return;}
				if(cen.Y>J.yPonto+margemY) {foraDaTela=true; return;}
			}
			if(ehMob) return; // acho q nenhum mob usarah faces, mesmo se tiver um badBlock, daria p imprimir como j3d inteiro.
			//if(crCma==null) return; // parece o suficiente p ver se o mob eh um cubo ou nao. Um trigo jah estaria fora.
			
			// !NAO!!! A linha abaixo criou um bug nos portais, na agua e similares
			//if(crNor==null) return; // parece o suficiente p ver se o mob eh um cubo ou nao. Um trigo jah estaria fora.

			if(opCanBounceFaces){
				if(opBounceCma) // p agua
					ic = 0.025f*getSin(xt*10+zt*10+xa+za+J.cont);
				if(opBounceFaces)
					ic = ib = il = io = in = is = 0.05f+0.05f*getSin(xt*10+zt*10+xa+za+J.cont);
			}
			if(iCal!=0){ // calibre
				in=iCal;
				is=iCal;
				il=iCal;
				io=iCal;
			}			
			if(iCalCB!=0){ 
				ic=iCalCB;
				ib=iCalCB;
			}			

			/*
			if(infImp.dis!=0){
				float q = 0.4f;
				if(opAltInfCano) q = (float) (q-infImp.inf/40f);
				float qq = q*0.5f;
				if(infImp.dis=='n') {io=il=ic=ib=-q; in=is=+qq; }
				if(infImp.dis=='l') {in=is=ic=ib=-q; il=io=+qq; }
				if(infImp.dis=='c') {in=is=il=io=-q; ic=ib=+qq; }
				if(infImp.dis=='j') {ic=ib=in=is=il=io=-qq; }
			}
			*/

			// ganhando clocks nos 2 blocos abaixo
			float hc = tc+ic+infImp.ic;
			float hb = -tb-ib-infImp.ib;			
			float ho = -to-io-ipLO-infImp.io;
			float hl = +tl+il+ipLO+infImp.il;
			float hn = +tn+in+ipNS+infImp.in;
			float hs = -ts-is-infImp.is;
			
			{ // cma
				J.Coord3 no = cen.rel(ho, hc, hn); 
				J.Coord3 nl = cen.rel(hl, hc, hn);
				J.Coord3 sl = cen.rel(hl, hc, hs);
				J.Coord3 so = cen.rel(ho, hc, hs);				
				cmaNOx = no.X;	cmaNOy = no.Y;
				cmaNLx = nl.X;	cmaNLy = nl.Y;
				cmaSLx = sl.X;	cmaSLy = sl.Y;
				cmaSOx = so.X;	cmaSOy = so.Y;
			}			
			{ // bxo
				J.Coord3 no = cen.rel(ho, hb, hn);
				J.Coord3 nl = cen.rel(hl, hb, hn);
				J.Coord3 sl = cen.rel(hl, hb, hs);
				J.Coord3 so = cen.rel(ho, hb, hs);
				bxoNOx = no.X;	bxoNOy = no.Y;
				bxoNLx = nl.X;	bxoNLy = nl.Y;
				bxoSLx = sl.X;	bxoSLy = sl.Y;
				bxoSOx = so.X;	bxoSOy = so.Y;		
			}

			if(opLadNor){
				cmaNOx = bxoNOx;
				cmaNOy = bxoNOy;
				cmaNLx = bxoNLx;
				cmaNLy = bxoNLy;
			}
			if(opLadSul){
				cmaSOx = bxoSOx;
				cmaSOy = bxoSOy;
				cmaSLx = bxoSLx;
				cmaSLy = bxoSLy;
			}			
			if(opLadOes){
				cmaSOx = bxoSOx;
				cmaSOy = bxoSOy;
				cmaNOx = bxoNOx;
				cmaNOy = bxoNOy;
			}						
			if(opLadLes){
				cmaSLx = bxoSLx;
				cmaSLy = bxoSLy;
				cmaNLx = bxoNLx;
				cmaNLy = bxoNLy;
			}							
		}
		public Cub setName(String nm){
			// verificar se jah existe com o nome. Reportar erro se jah existe e sair do programa.
			J.opEmMaiusc = true;
			name = J.limpaSt(nm);
			return this;
		}
		
		public void impCen(){			
			// soh p detalhe central. Nao tem face aqui.
			// isso eh tb usado p mobs
				
				
			if(dtCen!=null){
				dtCen.zoom=1f;
				if(opCresce100){
					//int vi = getInf(xa,ya,za,xt,yt,xt);
					int vi = infImp.inf;
					//dtCen.zoom = (float)(0.125f+vi/1000f); // 0..1000
					dtCen.zoom = (float)(vi/100f); // 0..100
				}


				float ic = 0;
				if(opBounceCen) // usei p isca da varinha de pescar
					ic = 0.1f*getSin(xt*10+zt*10+xa+za+J.cont);

				
				int lz = J.maior(infImp.lc,infImp.lt)-opLuzAltura+opLuzGeral;
				if(!sombreavel) lz=0;
				if(
					dtCen.impJ3dPal(
						xImp+dxx,
						yImp-0.5f-decDegrau+dyy+ic,
						zImp+dzz, 
						lz))	
								hit = CEN;
				// usa a luz de cima
				numJ3dImp++;				
			}	
		}
		public void impBxo(){			
			// de costas???
			// if(J.angX>0) return; // mas acho q nao serah tao simples assim

			if(yt*tamArea+ya<yJoe) return;			

			// nivel de cor
			int ncr = J.maior(infImp.lb,infImp.lt)-opLuzAltura+opLuzGeral;
			if(opLuzAgua) ncr = opNivLuzAgua;
			if(opLuzInf20) ncr = infImp.inf %31-15-4;
			if(opLuzLife1000) ncr = verLuzLife1000(life)-4;

			
			Color 
				cr = crBxo.get(ncr),
				crr = null;
			if(cct!=0)	
				crr = crBxo.get(-cct+ncr);
				

				
			if(	
			J.bufPol( cr,crr,
				bxoNOx,bxoNOy,
				bxoNLx,bxoNLy,
				bxoSLx,bxoSLy,
				bxoSOx,bxoSOy))
					hit = J.BXO;
			
			if(opImpDt ||opForceImpDt)
			if(dtBxo!=null){	
				dtBxo.zoom = tb+tb;
				dtBxo.impJ3d(xImp,yImp-0.5f-ib,zImp, ncr,crDtBxo);		
				numJ3dImp++;
			}	
		}
			boolean opSupAgua=false;
		public void impCma(){			
			// de costas???
			//if(J.angX<-0.4f) return; 
			
			/*
			if(!opForceImpCma)
			if(!opSupAgua)
			if(yt*tamArea+ya>yJoe) 
				return;
			*/
			if(tip==tAgua)
			if(!opSupAgua)
			if(opUnderWater)	
				return;
		
			opSupAgua=false; 
			
			if(opImpCmaAlt)// p debug
				if(J.B(3)) return;
				
			// nivel de cor
			int ncr = J.maior(infImp.lc,infImp.lt)-opLuzAltura-opNivLuzAgua+opLuzGeral;
			//if(opLuzAgua) ncr = opNivLuzAgua;
			if(opLuzAgua) ncr+=J.R(2);
			if(opLuzInf20) ncr = infImp.inf %31-15-0;
			if(opLuzLife1000) ncr = verLuzLife1000(life)-0;

			Color 
				cr = crCma.get(ncr),
				crr=null;
			if(cct!=0) crr = crCma.get(-cct+ncr);
				

			
			if(
			J.bufPol(cr,crr,
				cmaNOx,cmaNOy,
				cmaNLx,cmaNLy,
				cmaSLx,cmaSLy,
				cmaSOx,cmaSOy)
				) hit = J.CMA;
				
			if(opMenosDtCma)
				if(((xa%3==2)&&(za%3==2))==false)
					return;
				
			if(opImpDt || opForceImpDt)
			if(dtCma!=null){
				dtCma.zoom = tc+tc;
				dtCma.impJ3d(xImp,yImp-0.5f+ic,zImp, ncr,crDtCma);
				numJ3dImp++;				
			}	
		}
		public void impNor(){			
			// de costas???
			if(cmaNLx>cmaNOx) return; 
			/* mas nem sempre eh assim: 
					- p colunas muito altas, quando se olha p cima, ficam falhadas.
			*/

			if(opImpNorAlt)// p debug
				if(J.cont % 10 <5) return;			

			// nivel de cor
			int ncr = J.maior(infImp.ln,infImp.lt)-opLuzAltura+opLuzGeral+luzPortal;
			if(opLuzAgua) ncr = opNivLuzAgua;
			if(opLuzInf20) ncr = infImp.inf %31-15-2;			
			if(opLuzLife1000) ncr = verLuzLife1000(life)-2;

			Color 
				cr = crNor.get(ncr),
				crr = null;
			if(cct!=0)	
				crr = crNor.get(-cct+ncr);

				
			if(	
			J.bufPol(cr,crr,
				cmaNLx,cmaNLy,
				cmaNOx,cmaNOy,
				bxoNOx,bxoNOy,
				bxoNLx,bxoNLy))
				 hit = NOR;
			if(opImpDt || opForceImpDt)
			if(dtNor!=null){
				dtNor.zoom = tn+tn;
				dtNor.impJ3d(xImp,yImp-0.5f,zImp+in, ncr,crDtNor);
				numJ3dImp++;				
			}
			if(opImpDt || opForceImpDt)
			if(dtNorr!=null){
				dtNorr.zoom = tn+tn;
				dtNorr.impJ3d(xImp,yImp-0.5f,zImp+in, ncr,crDtNorr);
				numJ3dImp++;				
			}			
		}
		public void impSul(){			
			// de costas???
			if(cmaSOx>cmaSLx) return; 

			// nivel de cor
			int ncr = J.maior(infImp.ls,infImp.lt)-opLuzAltura+opLuzGeral+luzPortal;
			if(opLuzAgua) ncr = opNivLuzAgua;
			if(opLuzInf20) ncr = infImp.inf %31-15-2;			
			if(opLuzLife1000) ncr = verLuzLife1000(life)-2;
			

			Color 
				cr = crSul.get(ncr),
				crr = null;
			if(cct!=0) crr = crSul.get(-cct+ncr);
			
			if(
			J.bufPol(cr,crr,
				cmaSOx,cmaSOy,
				cmaSLx,cmaSLy,
				bxoSLx,bxoSLy,
				bxoSOx,bxoSOy))
				hit = SUL;
			if(opImpDt || opForceImpDt)
			if(dtSul!=null){
				dtSul.zoom = ts+ts;
				dtSul.impJ3d(xImp,yImp-0.5f,zImp-is, ncr,crDtSul);
				numJ3dImp++;				
			}	
			if(opImpDt || opForceImpDt)
			if(dtSull!=null){
				dtSull.zoom = ts+ts;
				dtSull.impJ3d(xImp,yImp-0.5f,zImp-is,+ncr,crDtSull);									
				numJ3dImp++;				
			}	
		}
		public void impLes(){			
			// de costas???
			if(cmaSLx>cmaNLx) return; 

			// nivel de cor
			int ncr = J.maior(infImp.ll,infImp.lt)-opLuzAltura+opLuzGeral+luzPortal;
			if(opLuzAgua) ncr = opNivLuzAgua;
			if(opLuzInf20) ncr = infImp.inf %31-15-1;			
			if(opLuzLife1000) ncr = verLuzLife1000(life)-1;
			
			Color 
				cr = crLes.get(ncr),
				crr = null;
			if(cct!=0)	
				crr = crLes.get(-cct+ncr);
			
			
			if(
			J.bufPol(cr,crr,
				cmaSLx,cmaSLy,
				cmaNLx,cmaNLy,
				bxoNLx,bxoNLy,
				bxoSLx,bxoSLy)
				) hit = LES;
				
			if(opImpDt || opForceImpDt)
			if(dtLes!=null){
				dtLes.zoom = tl+tl;
				dtLes.impJ3d(xImp+il,yImp-0.5f,zImp,ncr,crDtLes);
				numJ3dImp++;
			}	
			
			if(opImpDt || opForceImpDt)
			if(dtLess!=null){
				dtLess.zoom = tl+tl;
				dtLess.impJ3d(xImp+il,yImp-0.5f,zImp, ncr,crDtLess);
				numJ3dImp++;
			}	
			
		}
		public void impOes(){			
			// de costas???
			if(cmaNOx>cmaSOx) return; 

			// nivel de cor
			int ncr = J.maior(infImp.lo,infImp.lt)-opLuzAltura+opLuzGeral+luzPortal;
			if(opLuzAgua) ncr = opNivLuzAgua;
			if(opLuzInf20) ncr = infImp.inf %31-15-3;			
			if(opLuzLife1000) ncr = verLuzLife1000(life)-3;
//lc=0, lb=-4, ln=-2, ls=-2, ll=-1, lo=-3;															
			
			Color 
				cr = crOes.get(ncr),
				crr = null;
			if(cct!=0)	
				crr = crOes.get(-cct+ncr);
			
			
			if(
			J.bufPol(cr,crr,
				cmaNOx,cmaNOy,
				cmaSOx,cmaSOy,
				bxoSOx,bxoSOy,
				bxoNOx,bxoNOy)
				) hit = OES;
			if(opImpDt || opForceImpDt)
			if(dtOes!=null){
				dtOes.zoom = to+to;
				dtOes.impJ3d(xImp-io,yImp-0.5f,zImp, ncr,crDtOes);
				numJ3dImp++;					
			}
			if(opImpDt || opForceImpDt)
			if(dtOess!=null){
				dtOess.zoom = to+to;
				dtOess.impJ3d(xImp-io,yImp-0.5f,zImp, ncr,crDtOess);
				numJ3dImp++;					
			}
		}
	}
	public String getName(int p){
		if(J.noInt(p,0,tCub.size()-1))
			return tCub.get(p).name;
		return "???";
	}
	
// === CURSOR ===============================
		String stHit="";
		int 
			tmpPlot=16,
			tmpArar=tmpPlot,
			xt=0, yt=0, zt=0,
			xa=0, ya=0, za=0,
			cPlot=0,
			dHit=0,		
			dHit_=0,		
			xaHit_ = 0, yaHit_ = 0,	zaHit_ = 0,
			xtHit_ = 0, ytHit_ = 0,	ztHit_ = 0,
			xaHit = 0, yaHit = 0,	zaHit = 0,
			xtHit = 0, ytHit = 0,	ztHit = 0;
	public void zHits(){
		stHit = null;
		dHit=0;
		// xtHit=0; ytHit=0; ztHit=0; 
		// xaHit=0; yaHit=0; zaHit=0; 
		xt=0; yt=0; zt=0; xa=0; ya=0; za=0; 
	}

		int opTravaDirPlot=0, opNumMultPlot=3;
	public void plotDir(){
		if(stHit==null) return;
		
		if(opTravaDirPlot!=0)
			dHit=opTravaDirPlot;
		if(shiftOn) dHit = J.CMA; // deve ajudar
		
		int 
			v = joeInv.naMao(), vv=0, 
			m = 0, n=0, o=0;
		
		//if(v==tLampadaOn)  // intensidade depois LLLLLLLLLLLLLLL
		//	setLuzEsf(20,10, xaHit, yaHit, zaHit, xtHit, ytHit, ztHit);
		
		if(v!=comCai(v)) v = comCai(v);
		int bs = getCub(xaHit, yaHit, zaHit, xtHit, ytHit, ztHit);
		
		int lm=1;
		if(ctrlOn) lm=opNumMultPlot;
		for(int q=0; q<lm; q++){
			switch(dHit){				
				case CMA: n++; break;
				case BXO: n--; break;
				case OES: m--; break;
				case LES: m++; break;
				case SUL: o--; break;
				case NOR: o++; break; // conforme convencao
				default: n--; // ??? nao seria melhor evitar a plotagem aqui??? Ou seria apenas substituicao simples de bloco??? Deveria ter um comando p substituicao direta, sem precisar deletar o bloco primeiro.
			}
			
			{ // plotando ferramentas. Nao tem como plotar uma sequencia de ferramentas com ctrl.
				if(getCubMod(v).ehFerr){ // plotar qtd como inf. Bom p baterias tb
					setCub(v,joeInv.qtdNaMao(), xaHit+m, yaHit+n, zaHit+o, xtHit, ytHit,ztHit);
					joeInv.zItm(); // zera soh o item selecionado no inv
					wPlotMadeira.play(); // outros sons??? 
					cCarrMao = tmpCarrMao;
					opAniMaoGolpe=true;
					cPlot = tmpPlot;
					ms.b1=false; // tirando bug no desespero
					return;
				}
			}			
			
			// isso abaixo nao dah certo pq pode ter bau zerado q estah plotado
			// talvez fosse melhor quardar as coordenadas do bau plotado
			//if(v==tBau) vv = getNumBauZerado(); 
			// soh serah possivel remover bau vazio (ou entao, perder todos os itens na remocao, zerar bau)
			
			if(v==tAgua) v=tAguaCorr;
			if(v==tAguaCel) v=tAguaCelCorr;
			if(v==tAcido) v=tAcidoCorr;
			if(v==tPetroleo) v=tPetroleoCorr; // mas acho q isso nao eh muito bom
			if(v==tLava) v=tLavaCorr;
			
			if(tCub.get(v).ehSwitchOn || tCub.get(v).ehSwitchOff){
				switch(dHit){
					case NOR: v = getInd("switch_nor_off"); break;
					case SUL: v = getInd("switch_sul_off"); break;
					case LES: v = getInd("switch_les_off"); break;
					case OES: v = getInd("switch_oes_off"); break;
					case BXO: v = getInd("switch_bxo_off"); break;
					case CMA: v = getInd("switch_off"); break;
				}
			}
			if(tCub.get(v).ehBotaoOn || tCub.get(v).ehBotaoOff){
				switch(dHit){
					case NOR: v = getInd("botao_nor_off"); break;
					case SUL: v = getInd("botao_sul_off"); break;
					case LES: v = getInd("botao_les_off"); break;
					case OES: v = getInd("botao_oes_off"); break;
					case BXO: v = getInd("botao_bxo_off"); break;
					case CMA: v = getInd("botao_off"); break;
				}
			}

			// completando degraus q==0, soh no primeiro loop
			if(q==0 && getCubMod(bs).ehDegrau && dHit==CMA) {
				if(bs==v) {
					m=0; n=0; o=0;
					v = semDegrau(v);
					setCub(v, vv,		 
						xaHit+m, yaHit+n, zaHit+o,
						xtHit, ytHit, ztHit);	
					tCub.get(v).wPlot.play();				
					if(opGastaItem)	joeInv.decSel();
					if(joeInv.qtdNaMao()<=0)	joeInv.selectRel(0);					
					return;	
				}
			}
			
			if( getCubMod(xaHit+m, yaHit+n, zaHit+o, xtHit, ytHit, ztHit).ehGhostBlock){
				setCub(v, vv,		 
					xaHit+m, yaHit+n, zaHit+o,
					xtHit, ytHit, ztHit);
				if(opGastaItem)	joeInv.decSel();
				if(joeInv.qtdNaMao()<=0) q=100000; // fora do loop na marra
			} else q=1000000;
			
		}
		
		if(v!=0) tCub.get(v).wPlot.play();
		
		if(joeInv.qtdNaMao()<=0)
			joeInv.selectRel(0);

	}
	public String stDir(int p){
		switch(p){
			case CMA: return "CMA";
			case BXO: return "BXO";
			case NOR: return "NOR";
			case SUL: return "SUL";
			case LES: return "LES";
			case OES: return "OES";
		}
		return "???";
	}

	public int plotDir(int t, int q){
		if(opTravaDirPlot!=0)
			dHit=opTravaDirPlot;		
		if(shiftOn) dHit = J.CMA; // foi???
		int m=xaHit, n=yaHit, o=zaHit;
		int lm=1,qp=0; // quantidade plotada
		if(ctrlOn)lm=opNumMultPlot;
		
		for(int w=0; w<lm; w++){
			switch(dHit){			
				case CMA: n++; break;
				case BXO: n--; break;
				case NOR: o++; break;
				case SUL: o--; break;
				case LES: m++; break;
				case OES: m--; break;
				default: n++; break;
			}
			int v = getCub(m,n,o,xtHit,ytHit,ztHit);
			if(v==0 || tCub.get(v).opFazRio){
				setCub(t,q, m,n,o,xtHit,ytHit,ztHit);
				qp++;
			}
		}
		return qp; 
	}

	public float distJoe(int xa, int ya, int za, int xt, int yt, int zt){
			int meio = (tamTab>>1) * tamArea + (tamArea>>1);
			return 
				J.dist3d(
					meio -J.xCam,
					yJoe,
					meio -J.zCam, 
					xa+xt*tamArea,
					ya+yt*tamArea,
					za+zt*tamArea);
	}

	public float distCub(int xa, int ya, int za, int xt, int yt, int zt, int xaa, int yaa, int zaa, int xtt, int ytt, int ztt){
		return J.dist3d( // serah q funcionou???
			xa+xt*tamArea, 
			ya+yt*tamArea, 
			za+zt*tamArea, 
			xaa+xtt*tamArea, 
			yaa+ytt*tamArea, 
			zaa+ztt*tamArea);					
	}

		boolean opMultimetro=true;
	public void impCur(){  // regCur regCir
		if(opSoltaMouse) return; // algum win aberto
		
		if(cPlot>0) cPlot--;

		// bloco plotar
		{ // ajustando espessura do bloco. tag finura
			float r = 0.5f;				
			tCub.get(tPlotar).tc=r;
			tCub.get(tPlotar).tb=r;
			tCub.get(tPlotar).tn=r;
			tCub.get(tPlotar).ts=r;
			tCub.get(tPlotar).tl=r;
			tCub.get(tPlotar).to=r;		
			if(tCub.get(joeInv.naMao()).ehFerr){
				float e = -0.4f;				
				switch(dHit){
					case CMA: tCub.get(tPlotar).tc=e; break;
					case BXO: tCub.get(tPlotar).tb=e; break;
					case NOR: tCub.get(tPlotar).tn=e; break;
					case SUL: tCub.get(tPlotar).ts=e; break;
					case LES: tCub.get(tPlotar).tl=e; break;
					case OES: tCub.get(tPlotar).to=e; break;
				}
			} 
		}
		plotDir(tPlotar,0);

		// abrindo fornalha
		if(ms.b2)
		if(cPlot<=0){
			int t = getCub(xaHit,yaHit,zaHit,xtHit,ytHit,ztHit);
			if(J.vou(t,tFornalha,tDefumador, tAltoForno)){
				// esse inf deve referenciar a fornalha em questao
				int i = getInf(xaHit,yaHit,zaHit,xtHit,ytHit,ztHit);
				abreForn(t, i);
				cPlot=tmpPlot;
				return;
			}	
		}
		

		
		// abrindo maquinas de interface
		if(ms.b2)
		if(ctrlOn)
		if(cPlot<=0){
			int t = getCub(xaHit,yaHit,zaHit,xtHit,ytHit,ztHit);
			if(getCubMod(t).opEhMaqInt){
				int i = getInf(xaHit,yaHit,zaHit,xtHit,ytHit,ztHit);
				abreMaq(t, i);
				cPlot=tmpPlot;
				return;
			}	
		}

		// abrindo baus
		if(ms.b2)
		if(cPlot<=0)
		if(ehEsse(tBau,xaHit,yaHit,zaHit,xtHit,ytHit,ztHit)){
			cPlot=tmpPlot;
			// esse inf deve referenciar o bau em questao
			int b = getNumBau(xaHit,yaHit,zaHit,xtHit,ytHit,ztHit);
			if(b==-1) {
				b=0; // correcao estranha
				impSts("ERRO: Estranho indice de bau. Corrigindo p zero.");
				setInf(0,xaHit,yaHit,zaHit,xtHit,ytHit,ztHit);
			}
			abreBau(b);
			cPlot=tmpPlot;
			return;
		}			

		// abrindo bancada
		if(ms.b2)
		if(cPlot<=0)
		if(J.vou(getCub(xaHit,yaHit,zaHit,xtHit,ytHit,ztHit),tBanc,getInd("bigorna"))){
			abreBanc(); // nao precisa especificar o tipo aqui, serah deduzido
			cPlot=tmpPlot;
			return;
		}			

		
		if(ms.b2)
		if(!altOn)
		if(getCubMod(joeInv.naMao()).ehPlotavel) // nem todo cubo eh plotavel (mais algum???)
		if(cPlot<=0){
			cPlot=tmpPlot;
			if(getCubMod(joeInv.naMao()).ehFerr) 
				opAniBaixaMao=true;
			else 
				opAniMaoGolpe=true; // igual a plotar, dah p variar depois
			plotDir();
		}
		
		
		if(delOn)
		if(!shiftOn)
		if(cPlot<=0){
			setCub(0, 0, xaHit, yaHit, zaHit, xtHit, ytHit, ztHit);
			cPlot = 6;
		}
	
		int w=4;
		J.impRetRel(
			0, 31-J.cont%10,
			(J.maxXf>>1)-w, (J.maxYf>>1)-w, 
			w<<1, w<<1);
		esc((J.maxXf>>1), (J.maxYf>>1), "");	
		if(!J.iguais(stHit,"???")){
			esc(""+stHit);
			esc(""+stDir(dHit));		
			esc("xa: "+xaHit+":"+yaHit+":"+zaHit);
			esc("xt: "+xtHit+":"+ytHit+":"+ztHit);
		}
		int t = 0;

		if(stHit!=null){
			t = getInd(stHit);			
			esc("inf:  "+getInf( xaHit,yaHit,zaHit,xtHit,ytHit,ztHit));
			esc("dist: "+distJoe(xaHit,yaHit,zaHit,xtHit,ytHit,ztHit));
			if(opInfoDebug){
				Cub c = getCubMod(xaHit,yaHit,zaHit,xtHit,ytHit,ztHit);
				if(c!=null) if(c.crCma!=null) c.crCma.imp(100,100);
				if(c!=null) if(c.crNor!=null) c.crNor.imp(100,114);
			}
			if(opMultimetro){ // ?isso jah virou um item???
				if(t==tRede1 || getCubMod(t).ehCond)
					esc("el: "+getLife(xaHit,yaHit,zaHit,xtHit,ytHit,ztHit));				
					// eu sei q o certo seria "tensao eletrica", mas achei este termo ruim
			}
		}
		
		if(opInfoDebug)
		if(stHit!=null){ 
			esc("tip:"+t+"   inf:"+getInf(xaHit,yaHit,zaHit,xtHit,ytHit,ztHit));
			esc("life: "+tCub.get(t).life);
			esc("dir: "+tCub.get(t).dir+"   dis:"+getDis(xaHit,yaHit,zaHit,xtHit,ytHit,ztHit));
			esc("cnt: "+tCub.get(t).cnt);
			esc("operavel: "+tCub.get(t).operavel);
			esc("ehConEl: "+tCub.get(t).ehConEl);
		}
		
		if(opInfoDebug){
			esc("xtHit: "+xtHit+":"+ytHit+":"+ztHit);
			esc("xaHit: "+xaHit+":"+yaHit+":"+zaHit);
		}
		Area a = getTarea(xtHit,ytHit,ztHit);
		
		if(a!=null){			

			if(shiftOn)
			if(ctrlOn)
			if(cPlot<=0)
			if(ms.b1){
				cPlot=12;
				rodarCubo();
				wPulo.play();
			}
		
			if(altOn){ // empurrar/puxar
				if(ms.b1 || ms.b2){
					if(!ehMob(xaHit,yaHit,zaHit,xtHit,ytHit,ztHit))
					if(cPlot<=0){ // com b1 puxa, com b2 empurra
						puxarEmpurrar(ms.b1);
						opAniMaoGolpe=true;
						wPulo.play();
						cPlot=tmpPlot;
					}				
				}
			} else { // extracao normal				
				if(ms.b1)
					iniExt();
			}

			if(opInfoDebug){			
				esc("Area.xw: "+a.xw+" : "+a.yw+" : "+a.zw);

				if(a.inf!=null)
					esc("Area.inf[xaHit][][].inf: "+a.inf[xaHit][yaHit][zaHit].inf);
				esc("Area.e: "+a.e);
				esc("Area.cCalcOc: "+a.cCalcOc);
				//esc("Area.cVobNor: "+a.cVobNor);
				//esc("Area.cVobSul: "+a.cVobSul);
				//esc("Area.cVobLes: "+a.cVobLes);
				//esc("Area.cVobOes: "+a.cVobOes);
				//esc("Area.cVobCma: "+a.cVobCma);
				//esc("Area.cVobBxo: "+a.cVobBxo);
				esc("Area.cSmoothArea: "+a.cSmoothArea);
				esc("Area.cSombrear: "+a.cSombrear);
				esc("Area.jahSombreado: "+a.jahSombreado);
			}
		}
	}

// === HORIZONTE ===========================
		int altHor = -32000, incHor=0;
		boolean opSemChao=false, opSemCeu=false;
		float mulFov = 0;
	public void setFov(float p){
		// nao mecha em nada disso!!!
		J.fov = 1f; 
		//mulFov = (float)(3*280/p);
		mulFov = (float)(300/p);		
		//joeShape.zoom=p*0.038f;
		joeShape.zoom=0.1f;
		// 3*380/2 NAO
		// 3*260/p		
	}	
	public void regHor(){
		if(mulFov==0) setFov(3f);
		// fov=2f: altHor = (int)(-J.angX*380+(J.maxYbuf>>1));
		//altHor = (int)(-J.angX*260+(J.maxYbuf>>1));
		altHor = (int)(-J.angX*mulFov+(J.maxYbuf>>1));
		// 260 = 2*380/3
		/*essa eh a proporcao:
			fov		mul
			2			380
			3			260
			p			x
		*/
			
		if(!opUnderWater){
			J.bufRet(220,0, 0,0,J.maxXbuf,altHor); // ceu
			J.bufRet(210,0, 0,altHor,J.maxXbuf,J.maxYbuf); // chao			
		} else { 
			J.bufRetRel(crFogAgua,null, 0,0,J.maxXbuf,J.maxYbuf); 
			// melhor fazer um degrade aqui. Depois.
			return;
		}
		
		int 
			h=3, // variar segundo o tamanho do buffer depois, mesmo q este estiver com impressao duplicada
			w=2; // degrais de "escada"
		if(!opSemChao || !opSemCeu)	
		for(int j=0; j<=9; j++){
			// ceu
			J.bufRetRel(J.cor[229-j],null, 
				0,altHor+incHor-j*h-h,
				//J.maxXbuf-j*w, h);			
				J.maxXbuf, h);			
			// chao
			J.bufRetRel(J.cor[219-j],null, 
				0,altHor+incHor+j*h,
				//J.maxXbuf-j*w, h);
				J.maxXbuf, h);
		}

  }
	public void defHor(int crChao, int crCeu, int crAtmos){
		defHor(J.cor[crChao],J.cor[crCeu],J.cor[crAtmos]);
	}
	public void defHor(Color crChao, Color crCeu, Color crAtmos){
		J.cor[220] = crCeu;
		J.cor[229] = crAtmos;
		J.cor[210] = crChao;
		J.cor[219] = J.mixColor(crAtmos,crChao);		
		J.degrade(220,229);
		J.degrade(210,219);
	}
	
// === EXTRACAO DE BLOCOS ===============
			int 
				xaExt=intNull, yaExt=intNull, zaExt=intNull,
				xtExt=intNull, ytExt=intNull, ztExt=intNull,
				cExt=0, bExt=0, fExt=intNull;
			JWav wExt = null;	
			
		public void pacv(int p, int a, int c, int v){
			// proteina, agua, carboidrato, vitamina
			joeProt+=p;
			joeAgua+=a;
			joeCarb+=c;
			joeVit+=v;
		}	
		public void comerBeber(int p){ // bebercomer
			// corrigindo um bug
			ms.b1=false;
			Cub c = tCub.get(p);
			if(c.ehComivel) wChomp.play();
			if(c.ehBebivel) wGlop.play();			
			opAniMaoGolpe=true;
			cCarrMao=tmpCarrMao;
			cPlot=tmpPlot;
			if(p==getInd("agua")){
				pacv(0,30,0,0);
				// ?beber agua diretamente do rio deveria dar alguma doenca???
				return;
			}
			if(p==getInd("agua_cel")){
				pacv(0,30,0,0);
				joeLife = J.corrInt(joeLife+30, 0,100);				
				return;
			}									
			if(p==getInd("balde_agua")){
				pacv(0,100,0,0); 
				joeInv.decSel();
				bau.get(0).insBau("balde_vazio");
				// e se nao tem espaco??? Plotar? descartar?
				return;				
			}			
			if(p==getInd("frasco_agua")){
				pacv(0,20,0,0);
				joeInv.decSel();
				bau.get(0).insBau("frasco_vazio");
				// e se nao tem espaco??? Plotar? descartar?
				return;				
			}			
			if(p==getInd("pocao_life")){
				joeLife+=20;
				joeInv.decSel();
				bau.get(0).insBau("frasco_vazio");
				return;				
			}						
			if(p==getInd("veneno")){
				joeLife=10; // cuidado p nao quebrar o desafio
				pacv(-80,-80,-80,-80);
				wNojo.play();
				joeInv.decSel();
				bau.get(0).insBau("frasco_vazio");
				iniOculos(J.cor[12], 12);				
				// e se nao tem espaco no inv??? Plotar? Jogar em bau0? descartar?				
				return;				
			}						
			if(p==tAlga){
				pacv(0,3,1,1); // pouca comida pq cresce muito, abundancia tira o desafio
				joeInv.decSel();
				return;
			}

			if(p==getInd("pocao_cel")){
				joeLife=100; // cuidado p nao quebrar o desafio
				
				pacv(100,100,100,100);
				joeInv.decSel();
				wMagia.play();
				
				if(bau.size()<=0) bau.add(new Bau());
				bau.get(0).insBau("frasco_vazio");
				iniOculos(J.cor[209], 12);
				// e se nao tem espaco no inv??? Plotar? Jogar em bau0? descartar?				
				return;				
			}						
			if(p==getInd("frasco_suco_maca")){
				joeInv.decSel();
				bau.get(0).insBau("frasco_vazio");
				joeLife+=12; // a <> da maca p suco eh q este dah life tb
				pacv(0,6,0,12);				
				// e se nao tem espaco no inv??? Plotar? Jogar em bau0? descartar?				
				return;				
			}					
			if(p==getInd("frasco_garapa")){
				joeInv.decSel();
				bau.get(0).insBau("frasco_vazio");
				joeLife+=12; // a <> do talo p  g a r a p a  eh q este dah life tb
				pacv(0,6,6,0);				
				// e se nao tem espaco no inv??? Plotar? Jogar em bau0? descartar?				
				return;				
			}								
			if(p==tTaloCana){
				pacv(0,6,6,0);
				joeInv.decSel();
				return;
			}
			if(J.vou(p,tMaca,tMacaVerde,tMorango,tFatiaMelao,tFatiaMelancia,tFatiaCoco)){
				// coco nao pode dar semente, p plantar coqueiro, deve-se plantar o coco inteiro (mas nao sei se jah implementei isso)
				if(p==tFatiaMelao) joeInv.addItm(J.R(2)*J.R(2),"sem_melao"); // semente mais dificil pq 1 melao dah varias fatias. Idem p melancia.
				if(p==tMorango) joeLife+=6; // parece q o joe gosta de morangos
				if(p==tFatiaMelancia) {
					joeInv.addItm(J.R(2)*J.R(2),"sem_melancia");
					pacv(0,12,0,3); // isso eh bom???
				} else 	pacv(0,3,0,12);
				joeInv.decSel();				
				return;				
			}
			if(J.vou(p,tAlface,tRepolho,tCenoura,tBeterraba)){
				pacv(0,6,6,6); // ?isso tah bom???
				if(p==tRepolho)
					wPeido.agPlay(100+J.R(100));				
				joeInv.decSel();				
				return;				
			}						
			if(J.vou(p,getInd("milho_cozido"),getInd("abobora_cozida"))){
				pacv(0,3,12,3);
				joeInv.decSel();								
				return;				
			}
			if(J.vou(p,getInd("pao"),getInd("batata_assada"))){
				pacv(0,0,24,0);
				joeInv.decSel();				
				return;				
			}			
			if(p==getInd("torta_banana_assada")){
				joeInv.decSel();
				joeLife+=12; 
				wMinham.agPlay(50);
				pacv(0,0,12,12);				
				return;				
			}
			if(p==getInd("banana_madura")){
				pacv(0,0,12,12);
				joeInv.decSel();				
				return;				
			}						
			if(J.vou(p,getInd("pao_cru"),getInd("batata"),getInd("banana_verde"),getInd("torta_banana_crua"))){
				joeInv.decSel();				
				joeLife-=6;
				pacv(0,0,6,0); // ?isso eh bom???
				iniOculos(J.cor[12], 6);
				wNojo.play();
				return;				
			}

			if(p==getInd("carne")
			 ||p==getInd("peixe1")
			 ||p==getInd("peixe2")
			 ||p==getInd("peixe3") ){
				joeInv.decSel();				
				joeLife-=6;	
				pacv(6,0,0,0);
				iniOculos(J.cor[12], 6);
				wNojo.play();
				return;				
			}			
			if(p==getInd("bife")
			 ||p==getInd("peixe_assado")  ){
				joeInv.decSel();				
				pacv(24,0,0,0);				
				return;				
			}						
			if(p==getInd("ovo")){
				joeInv.decSel();				
				joeLife-=6;	
				pacv(6,0,0,0);				
				iniOculos(J.cor[12], 6);
				wNojo.play();
				return;				
			}			
			if(p==getInd("ovo_cozido")){
				pacv(12,0,0,0);				
				joeInv.decSel();				
				wPeido.agPlay(100+J.R(100));
				//wNham.play();
				return;				
			}						
			J.impErr("!comida ou bebida nao especificada: "+p+" "+getName(p), "comerBeber()");
		}
		public int cauleEmMadeira(int cau){
			if(cau==getInd("caule_cel")) return getInd("madeira_cel");
			// mais tipos de  m a d e i r a  depois
			return getInd("madeira"); // esse eh o default			
		}	
		public void iniExt(){

			// tem q ver se tem espaco no inv p extracao, exceto se jah for gerar packs
			// tem q ver se nao tah apontando p nada
			// depois, tocar som de extracao inicial
			

			if(!ms.b1) return;


			fExt = joeInv.naMao();			
			bExt = getCub(xaHit,yaHit,zaHit,xtHit,ytHit,ztHit);			
			

			// interceptando comida/bebida
			if(ctrlOn) // comer/beber com control + clique esq. Isso precisaria estar como dicas no inicio do programa.
			if(tCub.get(fExt).ehComivel
			|| tCub.get(fExt).ehBebivel) 
			//if(!tCub.get(bExt).opCresce100) // nao comer cana apontando p outra q tah crescendo
			//if(!tCub.get(bExt).ehFolha) // prevenindo p nao comer maca preh apanhada
			//if(!J.vou(bExt,tAgua,tTerra,tGrama,tTerraArada,tTerraAradaRegada)) // previne q se coma beterraba na hora errada
			if(cPlot<=0){ // o cod abaixo p e g a  c u b o  de  a g u a ( j o e   b e  b e) no inv, mas nao no  m a p a 
				comerBeber(fExt);
				cPlot=12;
				return;
			}
			
			xaExt = xaHit;
			yaExt = yaHit;
			zaExt = zaHit;
			xtExt = xtHit;
			ytExt = ytHit;
			ztExt = ztHit;
			


			// isso tah atrapalhando. Ver se pode tirar depois.
			// precisa jogar direto p bau0 neste caso
			if(!joeInv.temEspacoNoInv(1,bExt)){
				if(cPlot<=0) wInvCheio.play(); 
				cPlot=6;
				impSts("sem espaco no inventario.");
				zExt();
				return;
			}

			{// especial eliminando mato com machado (tava atrapalhando)
				if(cPlot<=0) // !NAO USE cExt aqui!!!
				if(fExt==getInd("MACHADO")){
					
					if(bExt==getInd("mato")) // capim???
					{
						cPlot=tmpArar;
						wPlotFolha.play();
						setCub(0,xaExt,yaExt,zaExt,xtExt,ytExt,ztExt);
						opAniMaoGolpe=true;
						return;
					}					
				}				
			}
			{// especial abrindo caixotes
				if(cPlot<=0)				
				if(joeInv.naMao()==getInd("caixote_frasco"))	// outros depois
				if(joeInv.addItm(20,conteudo(joeInv.naMao())))
				{
					joeInv.zItm(); // zera apenas o slot selecionado
					cPlot=24;
					wMachado.play(); // mudar isso depois
					return;
				}
			}
			{// especial arremessando cabecas de creeper (q sao explosivas)
				if(cPlot<=0)
				if(fExt==getInd("cabeca_creeper")){
					setCub("seed_exp", xaHit, yaHit, zaHit, xtHit,ytHit,ztHit);
					cPlot=12;
					joeInv.decSel();						
					if(joeInv.qtdNaMao()<=0) joeInv.selectRel(0);
					opAniMaoGolpe=true;
					return;
				}
			}
			{// especial ateando fogo
				if(cPlot<=0)
				if(fExt==getInd("fogo"))
				if(inflamavel(getCub(xaHit, yaHit, zaHit, xtHit,ytHit,ztHit))){ // +isqueiro depois
					cPlot = tmpPlot;
					wFogoIni.play();
					int v = getCub(xaHit, yaHit, zaHit, xtHit,ytHit,ztHit);
					v = comFogo(v);
					setCub(v, xaHit, yaHit, zaHit, xtHit,ytHit,ztHit);
					joeInv.decSel();
					return;
				}									
			}
			{// especial enchendo tanque de latex				
				if(cPlot<=0) 
				if(fExt==getInd("caneca_latex_cheia"))
				if(J.vou(bExt,getInd("tanque_latex"),getInd("tanque_vazio"))){
					int nv = getInf(xaExt,yaExt,zaExt,xtExt,ytExt,ztExt);
					if(nv<maxVolTanque){
							cPlot=12;						
							setCub(getInd("tanque_latex"),nv+1,xaExt,yaExt,zaExt,xtExt,ytExt,ztExt);
							joeInv.decSel();
							if(joeInv.qtdNaMao()<=0) joeInv.selectRel(0);
							joeInv.addItm("caneca_latex_vazia");
							wPlotLama.play();
							opAniMaoGolpe=true;
							return;
						}
					}										
			}
			{// especial frasco vazio
				if(cPlot<=0)
				if(fExt==getInd("frasco_vazio")){
					if(bExt==tAgua) // mais liquidos depois
					if(joeInv.addItm("frasco_agua")){
						cPlot=6;
						wPlotAgua.play();
						joeInv.decSel();						
						if(joeInv.qtdNaMao()<=0) joeInv.selectRel(0);
						opAniMaoGolpe=true;
						return;
					}						
				}
			}
			{// especial balde enchendo com agua (qq depois)
				if(cPlot<=0) 
				if(fExt==getInd("balde_vazio")){
					if(bExt==tAgua) // mais liquidos depois
					if(joeInv.addItm("balde_agua")){
						cPlot=tmpPlot;
						wPlotAgua.play();
						joeInv.decSel();		
						if(joeInv.qtdNaMao()<=0) joeInv.selectRel(0);
						opAniMaoGolpe=true;
						return;
					}						
				}
			}
			{// especial balde esvaziando
				if(cPlot<=0)  // eh muito estranho alagar o mapa inteiro com um unico balde. Nao tah certo.
				if(fExt==getInd("balde_agua")){
					cPlot=tmpPlot;
					wPlotAgua.play();
					plotDir(tAgua,0);
					//setCub("agua",xaHit,yaHit+1,zaHit,xtHit,ytHit,ztHit);
					joeInv.decSel();		
					joeInv.addItm(1,"balde_vazio");		
					opAniMaoGolpe=true;
					return;
				}
			}
			{// especial b e b e r a g u a  p l o t a d a  do rio
				if(cPlot<=0) 
				if(fExt==0) // deveria dar doenca aqui. Pensar em agua do mar tb.
				if(ctrlOn) // deve ajudar
				if(bExt==tAgua || bExt==tAguaCorr){
					cPlot = tmpPlot;
					comerBeber(bExt);
					return;					
				}
			
			}
			{// especial criando tijolos de adobe
				if(cPlot<=0)
				if(joeInv.naMao()==tLama){
					cPlot=12;
					opAniMaoGolpe=true;
					int m=0, n=0, o=0;
					switch(dHit){
						case NOR: o++; break;
						case SUL: o--; break;
						case LES: m++; break;
						case OES: m--; break;
						case CMA: n++; break;
						case BXO: n--; break;
					}
					setCub("degrau_adobe_umido",
						xaHit+m, 
						yaHit+n,
						zaHit+o, xtHit,ytHit,ztHit);
					wPlotLama.play();
					joeInv.decItm();
					ms.b1=false; // removendo bug na marra
					return;
				}
				
			}
			{// cinzel p ladrilho
					if(fExt==getInd("cinzel"))
					if(bExt==getInd("rocha")){
						cPlot=tmpPlot;
						wPlotRocha.play();
						joeInv.decSel();						
						if(joeInv.qtdNaMao()<=0) joeInv.selectRel(0);
						setCub("LADRILHO4",xaExt,yaExt,zaExt,xtExt,ytExt,ztExt);
						opAniMaoGolpe=true;
						return;
					}			
			}
			{// cinzel arranhando seringueira // mas nao se usa esta ferramenta na vida real. 
					if(fExt==getInd("cinzel"))
					if(bExt==getInd("caule_seringueira"))
					if(getCub(xaExt,yaExt+1,zaExt,xtExt,ytExt,ztExt)==getInd("caule_seringueira"))
					if(getCub(xaExt,yaExt-1,zaExt,xtExt,ytExt,ztExt)==getInd("caule_seringueira")){
						cPlot=tmpPlot;
						wPlotFolha.play();
						joeInv.decSel();
						if(joeInv.qtdNaMao()<=0) joeInv.selectRel(0);
						setCub("caule_seringueira_vazia",xaExt,yaExt,zaExt,xtExt,ytExt,ztExt);
						opAniMaoGolpe=true;
						return;
					}			
			}

			{// especial colher de pedreiro para lama
				if(cPlot<=0) // !NAO USE cExt aqui!!!
				if(fExt==getInd("colher_pedreiro")){
					
					if(bExt==getInd("terra"))
					if(temCubViz("agua",xaExt,yaExt,zaExt,xtExt,ytExt,ztExt)){
						cPlot=tmpArar;
						wPah.play();
						joeInv.decSel();						
						if(joeInv.qtdNaMao()<=0) joeInv.selectRel(0);
						setCub("lama",xaExt,yaExt,zaExt,xtExt,ytExt,ztExt);
						opAniMaoGolpe=true;
						return;
					}
					
					if(bExt==tGrama || bExt==getInd("degrau_grama")){
						cPlot=tmpArar;
						wEnxada.play();
						joeInv.decSel();						
						if(joeInv.qtdNaMao()<=0) joeInv.selectRel(0);
						setCub("terra",xaExt,yaExt,zaExt,xtExt,ytExt,ztExt);
						opAniMaoGolpe=true;
						return;
					}					
				}
			}
			{// especial colher de pedreiro extraindo meio bloco
				if(cPlot<=0) 
				if(fExt==getInd("colher_pedreiro"))
				if(getCubMod(bExt).extComColPed || getCubMod(bExt).extComPah ){ // esse "pah" eh bom??? Se for, daria p deletar este flag  e x t C o m C o l P e d ...
					if(bExt==tGrama) bExt=tTerra;
					//bExt = comDegrau(bExt);	
					wPah.play();
					cPlot=tmpPlot;
					joeInv.decSel();						
					if(joeInv.qtdNaMao()<=0) joeInv.selectRel(0);
					//joeInv.addItm(1,bExt);
					if(getCubMod(bExt).ehDegrau) 
						setCub(0,xaHit,yaHit,zaHit,xtHit,ytHit,ztHit);
					else
						setCub(comDegrau(bExt),xaHit,yaHit,zaHit,xtHit,ytHit,ztHit);
					iniPack(1, comDegrau(bExt), xaHit,yaHit,zaHit,xtHit,ytHit,ztHit);
					opAniMaoGolpe=true;				
					ms.b1=false;
					zExt();
					return;
				}
			}
			{// especial acender lampada com a mao (isso vai mudar)
				if(cPlot<=0)
				if(J.vou(fExt,0,tLampadaOn,tLampadaOff)){ // precisa disso?
					cPlot=tmpArar;
					opAniMaoGolpe=true;					
					
					if(bExt==tLampadaOff){
						wLampOn.play();
						setCub("lampada_on",xaExt,yaExt,zaExt,xtExt,ytExt,ztExt);
						return;
					}
					if(bExt==tLampadaOn){
						wLampOff.play();
						setCub("lampada_off",xaExt,yaExt,zaExt,xtExt,ytExt,ztExt);
						return;
					}					
					
				}
			}

			{// especial enxada para terra arada
				if(cPlot<=0) // !NAO USE cExt aqui!!!
				if(fExt==getInd("enxada")){
					if(bExt==tGrama || bExt==getInd("degrau_grama")){
						cPlot=tmpArar;
						wEnxada.play();
						joeInv.decSel();						
						if(joeInv.qtdNaMao()<=0) joeInv.selectRel(0);
						setCub("terra",xaExt,yaExt,zaExt,xtExt,ytExt,ztExt);
						opAniMaoGolpe=true;
						return;
					}
					if(bExt==tTerra){
						cPlot=tmpArar;						
						wEnxada.play();
						joeInv.decSel();
						if(joeInv.qtdNaMao()<=0) joeInv.selectRel(0);						
						setCub("terra_arada",xaExt,yaExt,zaExt,xtExt,ytExt,ztExt);
						opAniMaoGolpe=true;						
						return;												
					}
				}
			}
			{// especial pah para lama (p adobe)
				if(cPlot<=0) // !NAO USE cExt aqui!!!
				if(fExt==getInd("pah")){
					if(bExt==tGrama || bExt==getInd("degrau_grama")){
						cPlot=tmpArar;
						wPah.play();
						joeInv.decSel();						
						if(joeInv.qtdNaMao()<=0) joeInv.selectRel(0);
						setCub("terra",xaExt,yaExt,zaExt,xtExt,ytExt,ztExt);
						opAniMaoGolpe=true;
						return;
					}
				}
			}
			{// especial apanhando bambu maduro
				if(cPlot<=0) 
				if(bExt==tBambuMaduro)
				if(J.vou(fExt,getInd("foice"),getInd("machado"))){				
					// este nao darah semente, plantar com muda_bambu
					joeInv.addItm(J.R(3), "varinha_bambu"); // nao serve p varinha de pesca
					if(J.B(6)) joeInv.addItm(1, "varinha_bambu_grande"); // boa p varinha de pesca
					if(J.B(3)) joeInv.addItm(J.R(3), "muda_bambu");				 // ajustar dificuldade aqui

					setCub(0,xaExt,yaExt,zaExt,xtExt,ytExt,ztExt);
					cPlot=6;
					wFoice.play();
					wGet.play();
					opAniMaoGolpe=true;
					return;
				}					
				
			}

			{// especial peh de milho VERDE maduro, irah virar bege se nao apanhar logo
				if(cPlot<=0) // !NAO USE cExt aqui!!!
				if(bExt==tMilhoMaduro)
				if(J.vou(fExt,0,bExt,tEspigaVerde))
				if(joeInv.addItm(1+J.R(3),"espiga_verde")){					
					// nao deve ferir a escasses p nao perder a graca, logo, milho deve demorar p crescer. Ajustar isso depois.
					// veja q cada peh de milho pode dar mais de uma espiga. Isso tb tem q ser pensado.
					
					// este nao darah semente
					// joeInv.addItm(J.R(3), "sem_milho");				
					setCub(0,xaExt,yaExt,zaExt,xtExt,ytExt,ztExt);
					cPlot=6;
					wFoice.play();
					wGet.play();
					opAniMaoGolpe=true;
					return;
				}					
				
			}
			{// especial peh de milho BEGE, esse jah nao dah p cozinhar
				if(cPlot<=0) // !NAO USE cExt aqui!!!
				if(bExt==tMilhoSeco)
				if(J.vou(fExt,0,bExt,tEspiga))
				if(joeInv.addItm(1+J.R(3),"espiga")){					
					// nao deve ferir a escasses p nao perder a graca, logo, milho deve demorar p crescer. Ajustar isso depois.
					// veja q cada peh de milho pode dar mais de uma espiga. Isso tb tem q ser pensado.
					joeInv.addItm(J.R(3), "sem_milho");					
					setCub(0,xaExt,yaExt,zaExt,xtExt,ytExt,ztExt);
					cPlot=6;
					wFoice.play();
					wGet.play();
					opAniMaoGolpe=true;
					return;
				}					
				
			}
			{// especial extraindo morangos
				if(cPlot<=0) // !NAO USE cExt aqui!!!
				if(bExt==tMorangueiro) // outros mais aqui
				if(joeInv.addItm(1+J.R(3),"morango")){
					if(J.B(2))
						joeInv.addItm(1+J.R(2),"sem_morango");
					setCub(0,xaExt,yaExt,zaExt,xtExt,ytExt,ztExt);
					cPlot=6;
					wPlotFolha.play();
					wGet.play();
					opAniMaoGolpe=true;
					return;
				}					
				
				
			}
			{// especial extrair hortalicas hhhhhhhhhhhhhhhhhhhh
				if(cPlot<=0) // !NAO USE cExt aqui!!!
				if(J.vou(bExt, new int[]{tTrigo, tMato, tAlface, tRepolho, tCenoura-1, tBeterraba-1,tBatata-1, tAbobora, tMelao, tMelancia})) // outros mais aqui
				if(J.vou(fExt,0,bExt) || getCubMod(fExt).ehSemente) // mao nua ou bExt, semente tb
				if(joeInv.addItm(bExt)){ 
									// extraindo sementes de mato					
			
					if(bExt==tMato) joeInv.addItm(J.R(3)*J.R(2), "sem_trigo");
					if(bExt==tMato) joeInv.addItm(J.R(2)*J.R(2), "fibra_vegetal");
					
					if(bExt==tTrigo) joeInv.addItm(J.R(3)*J.R(2), "sem_trigo"); // inseri um bug aqui???
					if(bExt==tAlface) joeInv.addItm(J.R(3)*J.R(2)*J.R(2), "sem_alface"); // nao seria melhor deixar esta semente soh com trade p aumetar a raridade dela???
					if(bExt==tRepolho) joeInv.addItm(J.R(3)*J.R(2)*J.R(2), "sem_repolho"); 
					if(bExt==tCenoura) joeInv.addItm(J.R(3)*J.R(2)*J.R(2), "sem_cenoura"); 
					if(bExt==tBeterraba) joeInv.addItm(J.R(3)*J.R(2)*J.R(2), "sem_beterraba"); 
					if(bExt==tBatata) joeInv.addItm(J.R(3)*J.R(2)*J.R(2), "sem_batata"); 
					
					setCub(0,xaExt,yaExt,zaExt,xtExt,ytExt,ztExt);
					cPlot=6;
					wFoice.play();
					wGet.play();
					opAniMaoGolpe=true;
					return;
				}					
				
			}
			{// especial extrair muda
				if(cPlot<=0)
				if(fExt==getInd("foice"))
				if(getCubMod(bExt).ehFolha){				
					setCub(0,0,xaExt,yaExt,zaExt, xtExt,ytExt,ztExt);
					cPlot=12;
										
					if(J.B(16)){ // cuidado com erros de logica neste bloco abaixo
									if(bExt==getInd("folha_coq")) ; // nada aqui, eh soh p nao bugar
						else if(bExt==getInd("folha_macieira")) joeInv.addItm(1,"muda_macieira");					
						else if(bExt==getInd("folha_seringueira")) joeInv.addItm(1,"muda_seringueira");					
						else joeInv.addItm(1,"muda_silvestre"); 
						wPop2.play();
					}
					
					wPlotFolha.play();
					opAniMaoGolpe=true;
					return;					
				}								
			}
			{// especial plantando sementes, qq
				if(cPlot<=0)
				if(tCub.get(fExt).ehSemente || fExt==getInd("coco"))
				if(J.vou(bExt,getInd("terra_arada"),getInd("terra_arada_regada"))){  
					int bc = getCub(xaExt,yaExt+1,zaExt, xtExt,ytExt,ztExt);
					if(bc==0 || bc == tPlotar){ // haaaa sim...
						setCub(plantaDaSemente(fExt),6,xaExt,yaExt+1,zaExt, xtExt,ytExt,ztExt);
						// 6 acima p visualizar melhor quando acabar de plantar						
						cPlot=tmpPlot;
						joeInv.decSel(); 
						if(joeInv.qtdNaMao()<=0) joeInv.selectRel(0);
						opAniMaoGolpe=true;
						wPlotMuda.play();
						return;
					}
					
				}								
			}
			{// especial apanhar maca
				if(bExt==tFolhaMaca || bExt==tFolhaMacaVerde)
				if(J.vou(joeInv.naMao(),0,tMaca,tMacaVerde)){
					if(joeInv.addItm(1,"maca")){
						wPlotFolha.play();
						setCub("FOLHA_MACIEIRA",xaExt,yaExt,zaExt,xtExt,ytExt,ztExt);
						opAniMaoGolpe=true;
						return;
					}				
				}
			}
			{// especial varinha
				if(cPlot<=0) // !NAO USE cExt aqui!!!
				if(J.vou(bExt,tAgua,tIsca)) // mais tipos de fluidos depois. Existirao peixes aliens.
				if(fExt==getInd("varinha")){
					opAniMaoGolpe=true;
					cPlot=tmpPlot;
					if(!opVarinhaOn){ // lancando varinha
						wPlotAgua.play();
						setCub("splash", // esse cubo some sozinho
							xaHit,yaHit+2,zaHit, // o shape jah eh deslocado p baixo
							xtHit,ytHit,ztHit);						
						setCub("isca", // esse cubo some sozinho
							xaHit,yaHit+1,zaHit,
							xtHit,ytHit,ztHit);
					} else { // recolhendo varinha e fisgando
						if(cIsca>0){
							wGet.play();
							joeInv.addItm(1,J.rndSt("peixe1","peixe2","peixe3"));
							joeInv.decItm(1,"varinha"); // zera automatico, ne???
						}
					}
					opVarinhaOn=!opVarinhaOn;
					return;
				}
			}
			{// especial vestindo jetPack
				if(cPlot<=0)
				if(fExt==getInd("jetPack")){
					cPlot=12;
					// mudar shape do joe aqui
					opTemJetPack = true;
					joeJetPack = new Itm(joeInv.qtdNaMao(), fExt);
					joeInv.zItm();
					wPoeJetPack.play();
					cCarrMao=12;
					opAniBaixaMao=true;
					return;
				}				
			}			
			{// especial vestindo astrosuit
				if(cPlot<=0)
				if(J.vou(fExt,tAstroSuit,tAquaSuit,tHeatSuit,tColdSuit,tMarsSuit,tRadioSuit,tZettaSuit)){
					cPlot=12;
					joeShape = new J3d("astronauta01.j3d",0.001f);
					if(fExt==tAquaSuit) joeShape.tingir(119,299);
					if(fExt==tColdSuit) joeShape.tingir(119,89);
					if(fExt==tHeatSuit) joeShape.tingir(119,49);
					if(fExt==tMarsSuit) joeShape.tingir(119,109);
					if(fExt==tRadioSuit) joeShape.tingir(119,159);
					if(fExt==tZettaSuit) joeShape.tingir(119,169);
					
					cCarrMao=12;
					if(opJoeSuit!=0) joeInv.addItm(joeSuit.qtd,joeSuit.tip);					
					joeSuit = new Itm(joeInv.qtdNaMao(), fExt);
					opJoeSuit = fExt;
					joeInv.zItm();
					wPoeJetPack.play();
					cCarrMao=12;
					opAniBaixaMao=true;
					return;
				}				
			}			

			{// especial pegando com clique unico
				if(getCubMod(bExt).opGetComClick){
					int q=1;
					
					if(getCubMod(bExt).ehFerr) 
						q = getInf(xaExt,yaExt,zaExt, xtExt,ytExt,ztExt);
					
					if(joeInv.addItm(q,bExt)){
						setCub(0,xaExt,yaExt,zaExt, xtExt,ytExt,ztExt);
						wGet.play();
						cCarrMao = tmpCarrMao;
					  opAniMaoGolpe=true;						
						cPlot = tmpPlot;
						ms.b1=false; // tirando bug no desespero
					}
					return; 
				}				
			}

			{// extracao normal
				Cub c = tCub.get(bExt);
				cExt = -1; 
				
				
				if(c.extComMao)
				if(ctrlOn)// deve ajudar... parece q nao; Confundiu na hora de extrair melancia plotada
				if(cExt==-1)
				if(fExt==0){
					cExt=26;
					wExt = wFoice; // mas nao sei se isso serah bom sempre
					return;
				}
				
				if(c.extComPah)
				if(cExt==-1)
				if(fExt==getInd("pah")){
					cExt=6;
					wExt = wPah;
					return;
				}

				if(c.extComColPed)				
				if(cExt==-1)
				if(fExt==getInd("colher_pedreiro")){
					cExt=6;
					wExt = wPah;
					return;
				}

				
				if(c.extComPic)
				if(cExt==-1)
				if(fExt==getInd("picareta")){
					cExt=16;			
					wExt = wPicareta;
					return;
				}
				
				if(c.extComMach)
				if(cExt==-1)
				if(fExt==getInd("machado")){
					cExt=16;						
					wExt = wMachado;
					return;
				}
				
				if(cExt==-1) // esse eh um pouco diferente do machado
				if(c.ehCaule)
				if(fExt==getInd("serrote")){
					cExt=16;					
					wExt = wSerrote;					
					return;
				}				
				
				if(c.extComFoice)
				if(cExt==-1)
				if(fExt==getInd("foice")){
					cExt=6; 				
					wExt = wFoice;
					return;
				}

				if(c.extComChFenda)
				if(cExt==-1)					
				if(fExt==getInd("chave_fenda")){
					cExt=6;
					wExt = wChFenda;
					return;
				}

				if(c.extComChBoca)
				if(cExt==-1)
				if(fExt==getInd("chave_boca")){
					cExt=6;						
					wExt = wChBoca;
					return;
				}				
			}
		}
		public void regExt(){
			if(cExt<=0) return;
			

			
			// interrompendo a extracao
			if(xaHit!=xaExt) cExt=0;
			if(yaHit!=yaExt) cExt=0;
			if(zaHit!=zaExt) cExt=0;
			
			cExt--;
			if(cExt%6==0){
				if(wExt!=null) wExt.play();
				opAniMaoGolpe=true; // 3333333333333333
				//opAniMaoGolpee=true; // mas acho q nao serah sempre assim
				par.add(119, 20, J.xPonto<<1, J.yPonto<<1);				
			}
			if(cExt%6==2){
				//if(wExt!=null) wExt.play();
				//opAniMaoGolpe=true; // 3333333333333333
				opAniMaoGolpee=true; // mas acho q nao serah sempre assim
				par.add(119, 20, J.xPonto<<1, J.yPonto<<1);				
			}			
			if(cExt==0) finExt();
		}
		public void finExt(){
			int c0=0; // cubo zero q ficarah no lugar extraido; Acontece q pode ser extracao parcial com control pressionado
			if(fExt==getInd("serrote")){				
			// nao deve iniciar a extracao se tiver com pouca estamina
				if(tCub.get(bExt).ehCaule){
					// mais tipos de caule depois.
					// veja q existirah conversoes tb. Ex: um caule de resina em caule degrau.
					J.esc("---------> bExt: "+bExt+" "+getName(bExt));
					if(bExt==getInd("caule")) setCub("degrau_alto_caule",xaHit,yaHit,zaHit,xtHit,ytHit,ztHit);
					if(bExt==getInd("degrau_alto_caule")) setCub("degrau_caule",xaHit,yaHit,zaHit,xtHit,ytHit,ztHit);
					if(bExt==getInd("degrau_caule")) setCub("degrau_baixo_caule",xaHit,yaHit,zaHit,xtHit,ytHit,ztHit);
					if(bExt==getInd("degrau_baixo_caule")) setCub(0,xaHit,yaHit,zaHit,xtHit,ytHit,ztHit);
					
					// ainda nao tah funcionando
					//iniPack(1, "madeira", xaHit,yaHit,zaHit,xtHit,ytHit,ztHit);
					joeInv.addItm(1, "madeira");
					//joeInv.addItm(1, cauleEmMadeira(bExt));
					wPop.play();
					joeInv.decSel(); // decrementando ferr					
					return; // precisa disso aqui!
				}
					
			} else if(tCub.get(bExt).ehCaule){
				// remover arvore inteira e gerar pack
				extArvore(xaHit,yaHit,zaHit,xtHit,ytHit,ztHit);
			} else if(bExt==tMato){
				
				if(J.B(6)) {
					joeInv.addItm(1+J.R(4),getInd("sem_trigo")); 
					wGet.play(); 
				}	else {
					setCub(0,xaHit,yaHit,zaHit,xtHit,ytHit,ztHit);
					wPlotFolha.play();
				}
				
			} else if(bExt==tRede1){
				int q = trocaTudo(tRede1,0); 
				joeInv.addItm(q, tRede1);
			} else {
				iniPack(1,bExt,xaHit,yaHit+1,zaHit,xtHit,ytHit,ztHit);
			}			
/* NAO DEU CERTO AQUI, APAGAR DEPOIS
			{ // meio bloco com pah
				if(fExt==getInd("pah"))
				if(ctrlOn){
					bExt = comDegrau(bExt);
					c0 = comDegrau(bExt);
				}
			}
*/			
			joeInv.decSel(); // decrementando ferr
			setCub(c0,xaExt,yaExt,zaExt,xtExt,ytExt,ztExt);
			wGet.play(); // esse jah deve resolver;

			{ // abrindo comporta? // isso tah funcionando mesmo???
				// qq cubo extraido pode liberar um fluxo de agua (ou qq liq)
				int vv = getRioViz(xaExt,yaExt,zaExt, xtExt,ytExt,ztExt);
				// plota agua corrente automaticamente
				setCub(emRioCorr(vv),xaExt,yaExt,zaExt, xtExt,ytExt,ztExt);					
			}
			{ // foice finalizacao especial
				if(fExt==getInd("foice")){					
					int r = 1, v=0;
					for(int m=-r; m<=+r; m++)
					for(int o=-r; o<=+r; o++){
						v = getCub(xaExt+m, yaExt,zaExt+o, xtExt,ytExt,ztExt);
						if(tCub.get(v).extComFoice)
						if(joeInv.addItm(v)){
							if(v==tTrigo) joeInv.addItm(J.R(4), getInd("sem_trigo"));
							if(v==tMato)  joeInv.addItm(J.R(4)*J.R(2), getInd("sem_trigo"));
							setCub(0,0, xaExt+m, yaExt,zaExt+o, xtExt,ytExt,ztExt);
						}						
					}
				}
					
				
			}
			if(joeInv.qtdNaMao()<=0) joeInv.selectRel(0);
			setOc('s', xaExt, yaExt,zaExt+1, xtExt,ytExt,ztExt);
			setOc('n', xaExt, yaExt,zaExt-1, xtExt,ytExt,ztExt);
			setOc('l', xaExt-1, yaExt,zaExt, xtExt,ytExt,ztExt);
			setOc('o', xaExt+1, yaExt,zaExt, xtExt,ytExt,ztExt);
			setOc('c', xaExt, yaExt-1,zaExt, xtExt,ytExt,ztExt);
			setOc('b', xaExt, yaExt+1,zaExt, xtExt,ytExt,ztExt);
			zExt();
		}
		public void zExt(){
			bExt=0;
			fExt=intNull;
			xaExt=intNull;
  		xtExt=intNull;		
			wExt = null;
		}
		public int contCubVizD(String t, int xa, int ya, int za, int xt, int yt, int zt){
			// em cruz no plano horizontal + Diagonal. Nao ve a coord do pararmetro (o centro nao conta)
			int nc = 0;
			int tt = getInd(t);
			if(getCub(xa,ya,za+1,xt,yt,zt)==tt) nc++;
			if(getCub(xa,ya,za-1,xt,yt,zt)==tt) nc++;
			if(getCub(xa+1,ya,za,xt,yt,zt)==tt) nc++;
			if(getCub(xa-1,ya,za,xt,yt,zt)==tt) nc++;

			// diagonais
			if(getCub(xa+1,ya,za-1,xt,yt,zt)==tt) nc++;						
			if(getCub(xa+1,ya,za+1,xt,yt,zt)==tt) nc++;			
			if(getCub(xa-1,ya,za+1,xt,yt,zt)==tt) nc++;			
			if(getCub(xa-1,ya,za-1,xt,yt,zt)==tt) nc++;						
			
			return nc;			
		}
		public int trocaCubVizD(String ta, String tn, int xa, int ya, int za, int xt, int yt, int zt){
			int tta = getInd(ta);
			int ttn = getInd(tn);
			int ct =  0;
			if(getCub(xa,ya,za+1,xt,yt,zt)==tta) {setCub(ttn, xa,ya,za+1,xt,yt,zt); ct++;}
			if(getCub(xa,ya,za-1,xt,yt,zt)==tta) {setCub(ttn, xa,ya,za-1,xt,yt,zt); ct++;}
			if(getCub(xa+1,ya,za,xt,yt,zt)==tta) {setCub(ttn, xa+1,ya,za,xt,yt,zt); ct++;}
			if(getCub(xa-1,ya,za,xt,yt,zt)==tta) {setCub(ttn, xa-1,ya,za,xt,yt,zt); ct++;}
			
			if(getCub(xa-1,ya,za+1,xt,yt,zt)==tta) {setCub(ttn, xa-1,ya,za+1,xt,yt,zt); ct++;}
			if(getCub(xa-1,ya,za-1,xt,yt,zt)==tta) {setCub(ttn, xa-1,ya,za-1,xt,yt,zt); ct++;}
			if(getCub(xa+1,ya,za-1,xt,yt,zt)==tta) {setCub(ttn, xa+1,ya,za-1,xt,yt,zt); ct++;}
			if(getCub(xa+1,ya,za+1,xt,yt,zt)==tta) {setCub(ttn, xa+1,ya,za+1,xt,yt,zt); ct++;}
			return ct;			
		}		
		public int trocaCubCol(String taa, String tnn, int r, int alt, int xa, int ya, int za, int xt, int yt, int zt){
			// r é o raio da coluna
			// a coluna comeca em zero e vai ateh altura, inclusive
			// o retorno é o numero de blocos que foram substituidos
			int ta = getInd(taa), tn=getInd(tnn);
			if(J.iguais(tnn,"null")) tn=0;
			int troc = 0;
			for(int m=-r; m<=+r; m++)
			for(int n=0; n<=+alt; n++)				
			for(int o=-r; o<=+r; o++){
				if(getCub(xa+m, ya+n, za+o, xt,yt,zt)==ta){
					setCub(tn,xa+m, ya+n, za+o, xt,yt,zt);
					troc++;
				}
			}
			return troc;
		}

		public void extArvore(int xa, int ya, int za, int xt, int yt, int zt){ // extrairArvore extArv getArv remArv
			int 
				r = 0, 
				v = 0, 
				tc = getInd("caule"),
				tf = getInd("folha"),
				nc = 0, // qtd de cubos caule
				nf=0; // folhas; frutas depois
				
			// determinando caule e folha
			v = getCub(xa,ya,za,xt,yt,zt);
			if(tCub.get(v).ehCaule) tc = v;
			
			// interceptando coqueiro
			if(tc==getInd("caule_coq")){ 
				int nq=0;
				for(int q=0; q<30; q++)
					if(getCub(xa,ya+q,za,xt,yt,zt)==tc){
						nq++;
						setCub(0, xa,ya+q,za,xt,yt,zt);
					} else {
						if(getCub(xa,ya+q,za,xt,yt,zt)==getInd("folha_coq"))
							setCub(0, xa,ya+q,za,xt,yt,zt);
						int ncc = contCubVizD("coco",xa,ya+q-1,za,xt,yt,zt);
						trocaCubVizD("coco","nulo",xa,ya+q-1,za,xt,yt,zt);
						if(nq>0)iniPack(nq, tc, xa,ya,za,xt,yt,zt);						
						if(ncc>0)iniPack(ncc, "coco", xa,ya+1,za,xt,yt,zt);						

						return;
					}
				return;
			}
			
			// interceptando bananeira
			if(J.vou(tc,getInd("caule_bananeira"),getInd("penca_banana_madura"))){ 
				int nCaule = trocaCubCol("caule_bananeira","null",1,10, xa,ya,za,xt,yt,zt);
				/*este perde*/ trocaCubCol("penca_banana_crescendo","null",1,10, xa,ya,za,xt,yt,zt);
				int nPenca = trocaCubCol("penca_banana_madura","null",1,10, xa,ya,za,xt,yt,zt);
				int nFolha = trocaCubCol("folha_bananeira","null",1,10, xa,ya,za,xt,yt,zt);
				iniPack(nCaule, "caule_bananeira", xa,ya,za,xt,yt,zt);
				joeInv.addItm(2+J.R(2),"cacho_banana_verde"); // banana deveria amadurecer no inventario???
				return;				
			}


			for(int q=r+r; q>0; q--){
				v = getCub(xa,ya+q,za,xt,yt,zt);
				if(tCub.get(v).ehFolha){
					tf = v;
					q=0;
				}
			}
			
			// extraindo folhas
			r=4; // raio de extracao
			for(int m=-r; m<=+r; m++)
			for(int n=0; n<=+r+r+r; n++)
			for(int o=-r; o<=+r; o++){
				v = getCub(xa+m,ya+n,za+o,xt,yt,zt);
				if(tCub.get(v).ehFolha) {
					nf++;
					setCub(0,xa+m,ya+n,za+o,xt,yt,zt);
				}		
			}
			
			// extraindo caule
			r=1; // raio de extracao
			for(int m=-r; m<=+r; m++)
			for(int n=0; n<=altTab*tamArea; n++)
			for(int o=-r; o<=+r; o++){
				v = getCub(xa+m,ya+n,za+o,xt,yt,zt);
				if(tCub.get(v).ehCaule) {
					nc++;
					setCub(0,xa+m,ya+n,za+o,xt,yt,zt);
				}		
			}			
			if(nf>9) nf = 9; // muita folha tb eh ruim. Quebra o desafio.
			if(nc>0) iniPack(nc, tc, xa,ya+1,za,xt,yt,zt);
			if(nf>0) iniPack(nf, tf, xa,ya+2,za,xt,yt,zt);
		}
		
// === MAO DO JOE =======================

			J3d maoEsq=null, maoDir=null;
			int cCarrMao = 0, tmpCarrMao=12,
				opTremeMao=0; // usei p  a r c o   e   f l e c h a 
 			boolean 
				opAniBaixaMao=false,
				opAniMaoGolpe=false, 
				opAniMaoGolpee=false;

		public void carrMao(){ 
			/* tipos
						zero
						ferr com dtCen
						cubos grama
						cubos caule
						cubos normais
				 tarefas
						determinar o caminho do arq
						carregar arq
						trocar paleta
						ajustar posicao p baixo, p jah subir
						ver se precisa da outra mao
			*/
			int v = joeInv.naMao();
			String cm = null;
			boolean me = true; // adicionar mao esq
			maoDir = null; maoEsq = null;
			int crPele=139;
			int crManga=89; // trocar isso depois			
			if(v==0){
				cm = "mao03.j3d";
				maoDir = new J3d(cm,0.01f);
				if(opJoeSuit==tAstroSuit){ crPele=89; crManga=119; }
				if(opJoeSuit==tAquaSuit) { crPele=89; crManga=299; }				
				if(opJoeSuit==tColdSuit) { crPele=89; crManga=89; }				
				if(opJoeSuit==tRadioSuit) { crPele=89; crManga=159; }				
				if(opJoeSuit==tHeatSuit) { crPele=89; crManga=49; }				
				if(opJoeSuit==tMarsSuit) { crPele=89; crManga=109; }				
				if(opJoeSuit==tZettaSuit) { crPele=89; crManga=169; }				

				maoDir.tingir(99, addPal(crPele));
				maoDir.tingir(89, addPal(crManga));
			} else if(J.vou(v,
						getInd("maca"),
						getInd("maca_verde")
						)){ // nao tah perfeito mas tá suficiente
				cm = "maca04.j3d";
				maoDir = new J3d(cm,0.01f,"4");
				int cr=49; // nao tah otimizado, mas este cod nao eh frequente, logo, nao tem impacto no leg
				if(v==getInd("maca")) maoDir.tingir(99, addPal(99));	
				if(v==getInd("maca_verde")) maoDir.tingir(99, addPal(65));					
				me=false;										
			} else if(J.vou(v,
						getInd("pilastra_rocha"),
						getInd("pilastra_madeira"),
						getInd("pilastra_marmore"),
						getInd("pilastra_granito"),
						getInd("pilastra_obsidian")						
						)){ // outros depois
				cm = "pilastra01.j3d";
				maoDir = new J3d(cm,0.01f,"4");
						int cr=49; // nao tah otimizado, mas este cod nao eh frequente, logo, nao tem impacto no leg
						if(v==getInd("pilastra_madeira")) cr=139;
						if(v==getInd("pilastra_marmore")) cr=119;
						if(v==getInd("pilastra_granito")) cr=179;
						if(v==getInd("pilastra_granito")) cr=169;
				maoDir.tingir(99, addPal(49));	
				me=false;			
			} else if(J.vou(v,getInd("arco"),getInd("arco"))){ // outros depois
				cm = "arco02c.j3d"; // mudar cores aqui segundo encantamento, tipo, etc
				maoDir = new J3d(cm,0.01f);
				maoDir.tingir(99, addPal(109));	
				maoDir.tingir(15, addPal(119));	
				me=false;
			} else if(J.vou(v,tMelancia,tMelao,tAbobora)){
				cm = "cubo02.j3d";
				Cub c = tCub.get(v);
				maoDir = new J3d(cm,0.01f);
				maoDir.defIPalts(-1);
				me=false;
				int cr = 159;
				if(v==tMelancia) cr = 149;
				if(v==tMelao) cr = 79;
				maoDir.tingir(99, addPal(cr));
			} else if(v==tTaloCana){
				// esse ficou estranho, mas resolvi assim:
				// como ferr plotada tah verde normal, com o j3d tb verde,
				// mas na mao ele fica com cor trocada. Tive entao q gerar um segundo arq com cores 99
				cm = "cana05.j3d"; // cana05.j3d com cr99, cana04.j3d tem cor 149, sendo q este ultimo arq eh usado quando plotado no mapa.
				maoDir = new J3d(cm,0.01f);
				maoDir.defIPalts(-1);
				me=false;
				maoDir.tingir(99, addPal(149));
			} else if(tCub.get(v).dtCen!=null){
				// ferr
				me = false;
				J3d w = tCub.get(v).dtCen;
				maoDir = new J3d(w.camJ3d, 0.01f);
				
				if(tCub.get(v).ehEmbrulho){
					maoDir.tingir(99,tCub.get(v).crDtCen);
				} else maoDir.copyPals(w);	
				
			} else if(tCub.get(v).ehCaule || v==getInd("bancada")){
				me = false;
				cm = "cubo04.j3d";
				Cub c = tCub.get(v);
				maoDir = new J3d(cm,0.01f);
				maoDir.defIPalts(-1);
				maoDir.tingir(89, c.crCma);
				maoDir.tingir(99, c.crLes);				
			} else if(tCub.get(v).ehGrama){
				me = false;
				cm = "cubo03.j3d";
				Cub c = tCub.get(v);
				maoDir = new J3d(cm,0.005f);
				maoDir.defIPalts(-1);
				maoDir.tingir(89, c.crCma);
				maoDir.tingir(99, c.crLes);			
			} else {
				// cubo chapado
				me = false;
				cm = "cubo02.j3d";
				Cub c = tCub.get(v);
				maoDir = new J3d(cm,0.005f);
				maoDir.defIPalts(-1);
				maoDir.tingir(99, c.crCma);
			}
			
			if(me){ // com escudo na mao esq com espada na mao dir ia ficar legal
				maoEsq = new J3d(cm,0.01f);
				maoEsq.inverter('x');
				maoEsq.tingir(99, addPal(crPele));
				maoEsq.tingir(89, addPal(crManga));				
			}
			if(maoDir!=null) maoDir.zoom*=1.3f;
			if(maoEsq!=null) maoEsq.zoom*=1.3f;
		}
		public void regMao(){ 
			JPal.setTing(0f, null);
			if(cCarrMao>0){
				cCarrMao--;
				if(cCarrMao<=0)
					carrMao();
			}
			reterXCam();
			J.xCam=0;
			J.yCam=0;
			J.zCam=0;
			J.angX=0; // esse parece nao ter efeito
			
			J.incZ=1.5f;

			float 
				//dx = 0.7f, dy=1f,
				dx = 0.45f, dy=1f,
				ay = -0.4f;
				

			// abaixo nao deu certo
			// precisei disso p calcular a iluminacao sobre a mao do joe
			// int m = (tamArea>>1) -(int)(J.xCam);
			// int o = (tamArea>>1) -(int)(J.zCam);		
// //			int sobPeh = getCub(m, (int)(yJoe-1f),	o, meioTab, 0, meioTab);
			// int lz = getLc(m,(int)(yJoe-1),o,  meioTab,0, meioTab); 
				
			int lz = opLuzGeral;
			
			if(maoDir!=null){
				J.incX=+dx;
				J.incY=dy;
				if(opAniMaoGolpe) {
					//maoDir.giroXr(null,"@eixo",10f,4); //4444444444444444444
					maoDir.giroXr(null,null,6f,3); //4444444444444444444
					maoDir.reg3d(); 
				}
				
				if(opAniBaixaMao) {
					maoDir.desBxoR(null,-6f,10); // mais p quando plota ferramenta. Vai recarregar em breve, nao precisa retornar.
					opAniBaixaMao=false;
				}
				
				maoDir.defGiroGeral(-J.angY-ay);				
				maoDir.reg3d();
				maoDir.impJ3dPal(0,+yBounceJoe,0, lz);// variar luz conforme solo depois
			}
			if(maoEsq!=null){
				J.incX=-dx;				
				J.incY=dy;				
				if(opAniMaoGolpee){
					//maoEsq.giroXr(null,"@eixo",1f,4);
					maoEsq.giroXr(null,null,6f,3);
					maoEsq.reg3d();
				}
				maoEsq.defGiroGeral(-J.angY+ay);
				maoEsq.reg3d();
				maoEsq.impJ3dPal(0,-yBounceJoe,0, lz); // variar luz depois
			}			
			
			restauraXCam();
			opAniMaoGolpe=false;
			opAniMaoGolpee=false;
		}
			Itm 
				joeJetPack = new Itm(0,0),
				joeSuit = new Itm(0,0);

		public void regEquips(){ // equipamentos
			// armaduras devem aparecer aqui
			// ?lembra do zettaGoogles??? :)
			if(opJoeSuit==tAstroSuit) {
				if(joeSuit.imp(110,J.maxYf-120, (J.cont %50<25) )) // ajustar isso p indicar quando o oxigenio ou eletricidade do  s u i t  estah sendo consumido.
					if(ms.b1)
						if(cPlot<=0)
							if(joeInv.addItm(joeSuit.qtd, joeSuit.tip)){
								joeSuit = new Itm(0,0);
								wPoeJetPack.play();
								joeShape = joeSemSuit;
								opJoeSuit=0;
								cPlot=12;
								cCarrMao=3;
								opAniBaixaMao=true;
							}
			}
			
			if(opTemJetPack) {
				if(joeJetPack==null) joeJetPack = new Itm(1, getInd("jetPack")); //?isso tah coerente??? Isso tira um bug, neh?
				if(joeJetPack.imp(80,J.maxYf-120, opJetPackOn)) // hehe. Jah mostra se tah ligado. Muito bom.
					if(ms.b1)
						if(cPlot<=0){
							cPlot=12;
							cCarrMao=3;
							opAniBaixaMao=true;									
							if(itmArr==null){
								itmArr = joeJetPack;
							} else {
								joeJetPack = itmArr;
								itmArr = null;
							}							
						}
			}


		}
			float 
				xCam_=0, yCam_=0, zCam_=0,
				incX_=0, incY_=0, incZ_=0,
				angX_=0;
		public void restauraXCam(){
			J.xCam = xCam_;
			J.yCam = yCam_;
			J.zCam = zCam_;
			J.incX=incX_;
			J.incY=incY_;
			J.incZ=incZ_;			
			J.angX = angX_;			
		}
		public void reterXCam(){
			xCam_ = J.xCam;
			yCam_ = J.yCam;
			zCam_ = J.zCam;			
			incX_=J.incX;
			incY_=J.incY;
			incZ_=J.incZ;
			angX_ = J.angX;
		}
		
// === JOE =================================


		float 
			tamPeh = 0.43f, 
			altJoe = 1.7f, 
			opGrav=0.06f,		
			lmVy= opGrav*10f,
			vyJoe=0f,
			potPulo=0.5f,
			yJoe=0f;
		int 
			opJoeSuit=0, // 0=roupa normal, pode ser  a s t r o S u i t aqui (ou qq)
			xJoeIni=0, yJoeIni=0, zJoeIni=0,
			XPesca=0, YPesca=0; // ambos 2d, por isso sao maiusculos
		boolean
			opVarinhaOn=true,
			opJetPackOn=false, opTemJetPack=false, 
			opJetPackInfinito=false,
			opSomJetPack=false,
			opFiltroInv=true,
			opTemBau0=true; // bolsa, mochila
		J3d
			joeSemSuit = new J3d("aldeao06.j3d",0.01f),
			joeShape = joeSemSuit;
		int cPeg=0,
			tmpSoco = (int)(tmpPlot*0.66f), // tempo do ataque de soco do joe. Poderia variar com poder, level, stamina, etc. Depois.
			joeMont = -1; // apontarah p mob0..mob9 em tCub
	
	public void joeApear(){ // joeApeia(), joeApiar (esta com grafia errada, mas inseri como tag)

		int m = (tamArea>>1) -(int)(J.xCam);
		int o = (tamArea>>1) -(int)(J.zCam);
		int ytj = J.dezena((int)(yJoe));
		int yaj = (int)(yJoe)-ytj;
//		int sobPeh      = getCub(m, (int)(yJoe-1f),	o, meioTab, 0, meioTab);
//		int noTornozelo = getCub(m, (int)(yJoe+00),	  o, meioTab, 0, meioTab);
//		int noPeito     = getCub(m, (int)(yJoe+1f),	o, meioTab, 0, meioTab);

		tCub.get(joeMont).ativo = true;
		tCub.get(joeMont).cnt = J.cont;

		int w = iniMob(tCub.get(joeMont).tipp,m,yaj,o, meioTab, ytj, meioTab);
		tCub.get(w).dtCen = tCub.get(joeMont).dtCen;
		
		//setCub(joeMont, m,yaj,o, meioTab, ytj, meioTab);		
//		setCub(joeMont, xaHit,yaHit+1,zaHit, xtHit,ytHit,ztHit);		
		
		joeMont = -1;
	}
	public void regMontJoe(){
		// ver "getLC()" depois. "getLC()" retorna a qtd de luz de cima do cubo em questao.
		// montando
		if(ms.b2)
		if(joeInv.naMao()==0)	// talvez um laco, ou arreio depois.
		if(joeMont==-1){
			// vai pegar o indice em tCub:
			int m = getCub(xaHit,yaHit,zaHit,xtHit,ytHit,ztHit);
			if(J.noInt(m, mob0, mob9)){
				joeMont = m;
				//setCub(0,xaHit,yaHit,zaHit,xtHit,ytHit,ztHit);				
				trocaRai(m,0, 6, xaHit,yaHit,zaHit,xtHit,ytHit,ztHit);
				wPouso.play();
				ms.b2=false;// soh p garantir
			}
			return;
		}
		// desmontando
		//   (nao qui)
		
		// atuando com montaria
		if(joeMont!=-1){
			tCub.get(joeMont).cnt = J.cont;
			tCub.get(joeMont).ativo = true;
			tCub.get(joeMont).regAniMob();
			tCub.get(joeMont).regSomMob();
			tCub.get(joeMont).regBotaOvo();			
			tCub.get(joeMont).regAniMob();			
			
			// habilidades e desabilidades da montaria
			// (depois)
			// cavalo deve correr sempre, sem gastar estamina do joe
			
			reterXCam();
			J.xCam=0;
			J.yCam=0;
			J.zCam=0;
			J.incY=2f;
			J.incZ=1f;
			tCub.get(joeMont).dtCen.defGiroGeral(-J.angY);
			
			int luz = 0; // variar depois
			tCub.get(joeMont).dtCen.impJ3dPal(0,0,0, luz);
			restauraXCam();
			return;
		}
	}
			
	public boolean regPeg(int xa,int ya, int za, int xt, int yt, int zt){
		int q = 0, v=0, t=0, r=3;
		if(cPeg>0) cPeg--;
		if(cPeg<=0){
			cPeg=3;
			for(int m=-r; m<+r; m++)	
			for(int n=-r; n<+r; n++)	
			for(int o=-r; o<+r; o++){
				v = getCub(xa+m,ya+n,za+o, xt,yt,zt);
				
				if(tCub.get(v).ehPack){
					t = tCub.get(v).tipp;
					q = tCub.get(v).life;
					if(joeInv.temEspacoNoInv(q,t)){					
						joeInv.addItm(q,t); // e se nao couber???
						tCub.get(v).ativo = false;
						setCub(0, xa+m,ya+n,za+o, xt,yt,zt);
						//cPeg=12;
						return true;
					}
				} else if(tCub.get(v).opAutoGet){
						if(joeInv.addItm(v)){
						setCub(0, xa+m,ya+n,za+o, xt,yt,zt);							        
						//cPeg=12;
						return true;			        
						}
				}
			}
		}
		return false;
	}	

		String camJoe="joeCraft_joeState.cfg";
	public void saveStateJoe(){
		joeInv.saveOpenInv(SAVE);		
		Area a = getTarea(meioTab,0,meioTab); // ?Esse zero tah certo???
		if(a==null) J.impErr("!Erro salvando estado do Joe","part2.saveStateJoe()");
		int 
				m = a.xw-meioTab, // dentro da tabela
				n = a.yw,
				o = a.zw-meioTab;
				
		//J.escCfg("COMENTARIO1","posicao dentro da tabela",camJoe);
		J.escCfg("REM1","posicao dentro da tabela",camJoe);
		J.escCfg("xt",m,camJoe);
		J.escCfg("yt",n,camJoe);
		J.escCfg("zt",o,camJoe);
		
		J.escCfg("REM2","posicao dentro da area",camJoe);
		J.escCfg("xa",J.xCam,camJoe); // mas nao tava lendo isso pelo outro esquema. Nao tem problema.
		J.escCfg("ya",J.yCam,camJoe);
		J.escCfg("za",J.zCam,camJoe);		
		
		J.escCfg("REM3","estado de life e similares",camJoe);
		J.escCfg("life",joeLife,camJoe);		
		J.escCfg("agua",joeAgua,camJoe);		
		J.escCfg("prot",joeProt,camJoe);		
		J.escCfg("carb",joeCarb,camJoe);		
		J.escCfg("vit",joeVit,camJoe);		
		J.escCfg("ar",joeAr,camJoe);		
		
		{ // jetPack
			J.escCfg("temJetPack",opTemJetPack,camJoe);
			J.escCfg("tipJetPack",joeJetPack.tip,camJoe);
			J.escCfg("fuelJetPack",joeJetPack.qtd,camJoe);
		}	
	}
	public void openStateJoe(){
		joeInv.saveOpenInv(OPEN);

		xJoeIni = J.leCfgInt("xt",camJoe);
		yJoeIni = J.leCfgInt("yt",camJoe);
		zJoeIni = J.leCfgInt("zt",camJoe);
		
		J.xCam = J.leCfgFloat("xa",camJoe);
		J.yCam = J.leCfgFloat("ya",camJoe);
		J.zCam = J.leCfgFloat("za",camJoe);

		joeLife = J.leCfgInt("life",camJoe);		
		joeAgua = J.leCfgInt("agua",camJoe);		
		joeProt = J.leCfgInt("prot",camJoe);		
		joeCarb = J.leCfgInt("carb",camJoe);		
		joeVit = J.leCfgInt("vit",camJoe);		
		joeAr = J.leCfgInt("ar",camJoe);		
		{ // jetPack
			opTemJetPack = J.leCfgBool("temJetPack",camJoe);
			int tj = J.leCfgInt("tipJetPack",camJoe);
			int fj = J.leCfgInt("fuelJetPack",camJoe);
			joeJetPack = new Itm(fj, tj);			
		}
/*	// tah meio bugado isso abaixo, mas acho q nao vai precisar
		float ax = J.leCfgFloat("angX",camJoe);				
		float ay = J.leCfgFloat("angY",camJoe);			
		
		J.rotCam(0,0,0);
		J.rotCam(ax,ay,0f);
*/		

	}	

	
	public void proMundo(String st){ // setAmb
		// ?e se o string estiver errado??? R: vai dar erro e sair do programa.
		altCamMundo(st);
		wPortalIn.play();
		iniBioma(st);
		iniOculos(J.cor[J.rndCr9()],24);
		tab = new Tab();		
	}

	
	public void regCapacete(){
		int cr=119-6;
		if(opJoeSuit==tAstroSuit){
			J.impRetRel(cr,0, 0,0,J.maxXf,64);
			J.impRetRel(cr,0, 0,J.maxYf-100,J.maxXf,J.maxXf);
		}
	}
	public void regJetPack(){
		if(joeJetPack!=null)
		if(joeJetPack.qtd<=1)
			opJetPackOn=false;
		
		if(opJetPackOn)
		if(!opJetPackInfinito)	
		if(J.C(30)){
			if(joeJetPack.qtd>1){
				joeJetPack.qtd--;
				if(joeJetPack.qtd==1) {
					opJetPackOn=false; // cair aqui
					wJetPackNoEl.play();
					wJetPack.stop();
				}
			}
		}
		
		
			
		if(opJetPackOn)
		if(!menuAberto)
		if(!winAberta)
		if(!bancAberta)
		if(maqAberta==-1)
		if(fornAberta==-1)
		if(bauAberto==-1)
		{		
			yBounceJoe=0f;
			if(spaceOn) yJoe+=0.25f;
			if(shiftOn) yJoe-=0.25f;
		}		
	}
	public void regJump(){// regPortal nao mais aqui, veja em ccc em regCub

		// verifica o pulo e entrada em portais.
		// coloquei junto p otimizar o clock
		
		if(yJoe<-10) yJoe = altTab*tamArea+tamArea;
		
		int m = (tamArea>>1) -(int)(J.xCam);
		int o = (tamArea>>1) -(int)(J.zCam);
		int ytj = J.dezena((int)(yJoe));
		int yaj = (int)(yJoe)-ytj;
		int sobPeh      = getCub(m, (int)(yJoe-1f),	o, meioTab, 0, meioTab);
		int noTornozelo = getCub(m, (int)(yJoe+00),	  o, meioTab, 0, meioTab);
		int noPeito     = getCub(m, (int)(yJoe+0.5f),	o, meioTab, 0, meioTab);
		int naCab     = getCub(m, (int)(yJoe+1f),	o, meioTab, 0, meioTab);
		// BUG AQUI!!! Este ultimo zero nem sempre serah consistente!
		// NAO TEM BUG NAO! getCub ajusta ya jah pulando p area de cima caso for maior q 9

		
		// shift p afundar, mas deve tar conflito com outras teclas. Ateh dah p deduzir o contexto p evitar isso.
		if(shiftOn)
		if(tCub.get(sobPeh).opFazRio || tCub.get(noTornozelo).opFazRio)
		if(tCub.get(sobPeh).ehGhostBlock)
			yJoe-=0.25f;
			

		
		if(J.C(3)) 
		if(regPeg(m,yaj,o, meioTab, 0, meioTab))
			wGet.play();

		opPisandoNaAgua=false; 
		// if(J.vou(sobPeh,tAgua,tAguaCel,tLava,tLama)) // outros fluidos depois
			// opPisandoNaAgua=true; // p ajuste y do joe
		if(getCubMod(sobPeh).tc==0) // degrais e aguas
			opPisandoNaAgua=true;

		if(sobPeh==tAguaCel)
		if(J.C(6))	
		if(J.B(6))	
			joeLife = J.corrInt(joeLife+1, 0,100);
		
		// isso tinha q tá em regUnderWater, mas tem var q ia dificultar. Melhor deixar aqui mesmo
		if(naCab==0) 
			if(joeAr<100)joeAr+=3;
		if(naCab!=0)
		if(!opRespDebaixoDagua)
			if(tCub.get(naCab).opFazRio)
			if(opJoeSuit!=tAstroSuit) // na orbita ou debaixo dagua... mas isso nao tá legal. O certo seria skuba/roupa de mergulhador, com peh de pato, cilindro e tudo.
				if(J.C(5)) 
					joeAr--;		
			
	
		{	// este trecho faz o joe NAO entrar dentro de montanha quando ir p frente dela. Ele sobe como um degrau.
			if(noTornozelo!=0) 
				if(!tCub.get(noTornozelo).ehGhostBlock)
					yJoe+=1f;
			if(noPeito!=0) 
				if(!tCub.get(noPeito).ehGhostBlock) 
					yJoe+=1f; 
		}


		if(sobPeh==0){
		
			if(!opJetPackOn){
				vyJoe-=opGrav; // no ar
				yJoe+=vyJoe;
				if(vyJoe>+lmVy) vyJoe=+lmVy;
				if(vyJoe<-lmVy) vyJoe=-lmVy;
			} 
			
		} else {

			if(vyJoe!=0){
				vyJoe=0;
				yJoe = (int)yJoe;				
				if(!opUnderWater){
					wPouso.play();
					tCub.get(sobPeh).wPass.play();
				}

				if(!opUnderWater)
				if(tCub.get(sobPeh).opFazRio)	// ?sempre isso???
					wSplash.play();
			}
			
			if(spaceOn)
			if(!bancAberta)
			if(fornAberta==-1)
			if(bauAberto==-1)				
			{ 
				if(!opUnderWater)wPulo.play();
				yJoe+=1f;
				vyJoe=potPulo;
			}
		}	
		
		
		
	}
		int eArco=0, cArco=0;
		Cub mobArco=null;		
		boolean opArcoComZoom = true;
		// CUIDADO o mob morto deve ficar = null no final do processo do mob
	public boolean naMao(String p){
		if(getInd(p)==joeInv.naMao())
			return true;
		return false;
	}
		int cCircFlecha=0;
		float zoom_=300, txZoomArco=1f, maxZoomArco=zoom_*3f;
		
	public void regArco(){
		float des = 0.12f, dess=-des*7, ag=0.3f;
		int pas=12;
		
		if(opArcoComZoom)
		for(int q=0; q<12; q++)
			if(J.zoom>zoom_) 
				J.zoom-=txZoomArco;
		
		
		if(cCircFlecha>0){
			// abaixo nao ficou tao legal assim
			//J.bufCirc(J.cor[119],null, cCircFlecha<<1, J.maxXbuf>>1, J.maxYbuf>>1);
			cCircFlecha--;
		}

		switch(eArco){
			case 0:{ // iniciando
				if(cPlot<=0)			
				if(ms.b1)
				if(naMao("arco")){
					//mobArco = getMob(xaHit,yaHit,zaHit,xtHit,ytHit,ztHit);// mais p baixo
					eArco=1; 
					cArco=0;
					ms.b1=false;
					cPlot=tmpSoco;
				}
			} break;
			case 1:{ // apontando, arco da lateral p meio
				maoDir.desEsq(null,des,pas);
				maoDir.desCma(null,dess,pas);
				//maoDir.giroY(null,null,-ag,pas);
				wArco.play();
				eArco=2;
			} break;
			case 2:{ // mirando e tremendo
				if(opArcoComZoom)			
				if(cArco>30)
				for(int q=0; q<12; q++)
					if(J.zoom<maxZoomArco) 
						J.zoom+=txZoomArco+txZoomArco;
				cArco++; // quantidade de shake por aqui
				opTremeMao=cArco;
				mobArco = getMob(xaHit,yaHit,zaHit,xtHit,ytHit,ztHit);				
				if(opTremeMao>12)opTremeMao=12;
				if(ms.b1) eArco=3; // tiro automatico por aqui
			} break;
			case 3:{ // atirando e gastando flechas				
				eArco=4;
				cCircFlecha=4; // aqui jah eh p flecha2d
				// este abaixo soh fica bom se estiver perto do joe. Ainda nao examinei como medir esta distancia. Depois.
				//par.geraPar(J.rndCr9(), 20, J.maxXbuf, J.maxYbuf);				
				wFlecha.play();
				joeInv.decItm(1,"flecha"); // mas nem sempre				
				if(mobArco!=null)	{
					// if(mobArco.ehPack)	 ???
					wFlechaHit.play();
					mobArco.mobPula();
					mobArco.life-=32;// variar depois
					if(mobArco.life<=0){
						mobArco.ativo=false; // efeitos depois
						//setCub(0,xaHit,yaHit,zaHit,xtHit,ytHit,ztHit);
						opPlotSobreSolo=true;
						mobArco.mobDropaItem(xaHit,yaHit,zaHit,ztHit,ytHit,ztHit);
						mobArco = null; // parece importante isso aqui
						opPlotSobreSolo=false;
					}
					mobArco.dtCen.crCint=12;
					mobArco.dtCen.cCint=12; // armas manuais de choque (encantadas) poderiam mudar p cr15
					mobArco.cTreme=12;
				} else {
					int m=xaHit, n=yaHit, o=zaHit;
					int v = getCub(xaHit,yaHit,zaHit,xtHit,ytHit,ztHit);
					boolean fr=tCub.get(v).opFazRio;
					if(fr) wSplash.play();
					else wFlechaHit.play();
					String st="flecha";
					switch(dHit){				
						case CMA: n++; st="flecha_bxo";break;
						case BXO: n--; st="flecha_cma";break;
						case OES: m--; st="flecha_les"; break;
						case LES: m++; st="flecha_oes"; break;
						case SUL: o--; st="flecha_nor"; break;
						case NOR: o++; st="flecha_sul"; break;
						default: n++;
					}
					if(fr) st="splash";
					setCub(st,m,n,o,xtHit,ytHit,ztHit); // se atirar na agua a flecha some
				}
			} break;
			case 4:{ // recolhendo e voltando mao p lateral
				maoDir.desDir(null,des,pas);
				maoDir.desBxo(null,dess,pas);
				opTremeMao=0;
				eArco=0; cArco=0;
				ms.b1=false;				
			} break;
		}
	}
	public void regGet(){
		Cub c = null;
		int r = 1, v=0;
		if(J.C(4))
		for(int m=-r; m<+r; m++)
		for(int n=-1; n<2; n++)
		for(int o=-r; o<+r; o++){
			v = getCub(m, (int)(yJoe+n),	o, meioTab, 0, meioTab);
			if(v!=0) c = tCub.get(v);
			
			if(v!=0)
			if(c!=null)
			// if(c.opAutoGet)
			if(c.opQtdEhSaude)
			if(joeInv.addItm(3, c.tip)){				
				wGet.play();
				setCub(0, m, (int)(yJoe+n),	o, meioTab, 0, meioTab);
				return;
			}			
		}
		
	}
		boolean joeAniVai=true;
	public void impJoe(){
		if(wPeido.reg())// espera p tocar quando agendado
			iniOculos(J.cor[2],4,6);
		
		// ecoando o joe no canto da tela
		if(joeShape==null) return;
		
		joeShape.reg3d();
		// animando pelo passo
		if(joeShape.semMovAtivo())
		if(!bancAberta)
		if(fornAberta==-1)
		if(bauAberto==-1)
		if(opAndando){
			float q = 2f;
			if(joeAniVai) q = -q;
			joeAniVai = !joeAniVai;
			// tag tagE ang pas			
			joeShape.giroXr("armE","@armE",-q,3);
			joeShape.giroXr("armD","@armD",+q,3);
			joeShape.giroXr("legE","@legE",+q,3);
			joeShape.giroXr("legD","@legD",-q,3);			
		}
		
		
		// reter a camera... esse esquema poderia ficar em J.java, neh?
		float cx = J.xCam;
		float cy = J.yCam;
		float cz = J.zCam;
			float ix = J.incX;
			float iy = J.incY;
			float iz = J.incZ;		
		int xp = J.xPonto, yp=J.yPonto;
		
		J.xCam=0; J.yCam=0; J.zCam=0;
		// fov=2f: J.incX=-7.0f; J.incY=+4.6f; J.incZ=15;
		//J.incX=-8.0f; J.incY=+4.9f; J.incZ=10.9f;
		J.incX=0; J.incY=0; J.incZ=1;
		J.xPonto = 20; J.yPonto = J.maxYbuf-20;
		joeShape.opInvAng=false;
		if(opRealParal){
			J.xPonto=J.maxXbuf-J.xPonto;
			joeShape.opInvAng=true;
		}
		// CUIDADO! o zoom de shapeJoe eh afetado por "setFov()"
		float zzz = J.zoom;
		J.zoom = zoom_;
		joeShape.impJ3d(0,0,0);
		J.zoom = zzz;
		
		// restaurar a camera
		J.xPonto = xp; J.yPonto = yp;
		J.xCam = cx;
		J.yCam = cy;
		J.zCam = cz;
			J.incX = ix;
			J.incY = iy;
			J.incZ = iz;	
		
		
	}
	public void setJetPack(char p){
		if(!opTemJetPack){
			opJetPackOn=false;
			return;
		}
		if(p=='a') p = (opJetPackOn?'-':'+');
		switch(p){
			case '+':				
				yJoe+=0.5f; // pequeno pulo
				if(joeJetPack!=null)
				if(joeJetPack.qtd<=1){
					wMaqNoEl.play();
					return;
				}
				opJetPackOn=true;
				wJetPackOn.play();
				wJetPack.stop();
				if(opSomJetPack)wJetPack.agLoop(10);
				break;
			case '-':
				opJetPackOn=false;
				wJetPackOff.play();
				wJetPack.stop();
				break;
		}
	}
	
		Long 
			tmpGuarPas = J.nanoTime(),
			tmpContPas = 30000000L;	
		boolean opAndando=false;	
		int contBounceJoe=0;
		float yBounceJoe=0f;
	public void regPassJoe(){				
		opAndando = (cmaOn || bxoOn || esqOn || dirOn);
	
		//contBounceJoe=0;
		if(opAndando) {
			contBounceJoe++;
			//if(contBounceJoe>360)contBounceJoe=0;
			yBounceJoe = (float)(0.07f*Math.sin(contBounceJoe>>1));
		}
	
		if(J.nanoTime()-tmpGuarPas>tmpContPas){
			tmpGuarPas = J.nanoTime();
			// ... e continua neste metodo
		} else return; // senao sai.
		if(bancAberta) return;
		if(fornAberta!=-1) return;
		if(maqAberta!=-1) return;
		if(bauAberto!=-1) return;
		if(menuAberto) return;
		if(winAberta) return;
		
		int ps = 7;
		float peh = tamPeh;
		if(shiftOn) {// correndo
			peh*=2.3f;
			ps = 3;
		}
		
		float cz = (float)(Math.cos(J.angY)*peh);
		float cx = (float)(Math.sin(J.angY)*peh);
		
		boolean foi = false;
		
		if (cmaOn) {
			J.zCam-=cz;
			J.xCam-=cx;
			if(J.C(ps)) foi=true;
		}	
		if (bxoOn) {
			J.zCam+=cz;
			J.xCam+=cx;			
			if(J.C(ps)) foi=true;			
		}	
		
		boolean 
			d = dirOn,
			e = esqOn;
		if(opRealParal){
			if(dirOn) {e=true; d=false; }
			if(esqOn) {d=true; e=false; }			
		}
			
		
		if (e) {
			J.zCam-=cx;			
			J.xCam+=cz;
			if(J.C(ps)) foi=true;			
		}	
		if (d) {
			J.zCam+=cx;			
			J.xCam-=cz;
			if(J.C(ps)) foi=true;			
		}			
		
		if(foi) {
			int m = (tamArea>>1) -(int)(J.xCam);
			int o = (tamArea>>1) -(int)(J.zCam);
			int sobPeh  = getCub(m, (int)(yJoe-1f),	o, meioTab, 0, meioTab);
			if(sobPeh!=0)
				if(!opUnderWater)
					tCub.get(sobPeh).wPass.play();
		}
		
		
		if (J.xCam>+tamArea) { tab.desLes(); J.xCam=0; }
		if (J.xCam<-tamArea) { tab.desOes(); J.xCam=0; }
		if (J.zCam>+tamArea) { tab.desSul(); J.zCam=0; }
		if (J.zCam<-tamArea) { tab.desNor(); J.zCam=0; }
	}	
	public void regJoeCAC(){ // regCAC
		// ataque corpo a corpo, melee atack
		
		if(eArco>0) return; // nao aqui
			
		if(cPlot<=0)
		if(ms.b1) // hurtMob regHurtMob
		if(!naMao("arco")) // mais arcos depois
		if(ehMob(xaHit,yaHit,zaHit,xtHit,ytHit,ztHit)){
			Cub w = getMob(xaHit,yaHit,zaHit,xtHit,ytHit,ztHit);
		
			
			// Isso pode variar. Quanto mais baixo, mas rapido eh a sequencia de socos.
			// deveria ser algum poder
			// poderia variar com o nivel de estamina (implementar adrenalina???)
			cPlot=tmpSoco; 
			if(w!=null){

				if(w.ehPack) return; // corrigindo bug
				

				
				w.mobPula();
				w.life-=10;
				if(w.life<=0){
					w.ativo=false; // efeitos depois
					setCub(0,xaHit,yaHit,zaHit,xtHit,ytHit,ztHit);
					opPlotSobreSolo=true;
					w.mobDropaItem(xaHit,yaHit,zaHit,ztHit,ytHit,ztHit);
					opPlotSobreSolo=false;
				}
				w.dtCen.crCint=12;
				w.dtCen.cCint=12; // armas manuais de choque (encantadas) poderiam mudar p cr15
				wSoco.play(); // mudar o som de acordo com a ferr, depois
				w.cTreme=12;
				if(J.B(2))
					opAniMaoGolpe=true;
				else
					opAniMaoGolpee=true;
			}
		}

		
	}
	public boolean ehMob(int xa,int ya, int za, int xt, int yt, int zt){
		if(tCub.get(getCub(xa,ya,za,xt,yt,zt)).ehMob)		
			return true;
		return false;
	}
	
// =========================================
		int cArq = 0;

	public void iniAll(){ // iiiiiiiiiiiiiii		
		J.cor[290] = J.cor[16]; // na falta de um editor de paleta...
		J.cor[299] = J.cor[11];
		J.degrade(290,299);
		//J.saveOpenPlt2(true, "jf1/joe3d_09.plt");		
	}

	public Cub getCubMod(String st){
		// limites depois
		return tCub.get(getInd(st));
	}		
	public Cub getCubMod(int t){
		// limites depois
		return tCub.get(t);				
	}		
	public Cub getCubMod(int xa, int ya, int za, int xt, int yt, int zt){
		int v = getCub(xa,ya,za,xt,yt,zt);
		if(v>=0) return tCub.get(v);		
		return null;
	}
	public boolean ehUmDesses(int t, int tt, int xa, int ya, int za, int xt, int yt, int zt){
		return J.vou(getCub(xa,ya,za,xt,yt,zt),t,tt);
	}
	public boolean ehUmDesses(String st,String stt, int xa, int ya, int za, int xt, int yt, int zt){
		int v = getCub(xa,ya,za,xt,yt,zt);
		return J.vou(v,getInd(st),getInd(stt));
	}
	public boolean ehUmDesses(String st,String stt,String sttt, int xa, int ya, int za, int xt, int yt, int zt){
		int v = getCub(xa,ya,za,xt,yt,zt);
		return J.vou(v,getInd(st),getInd(stt),getInd(sttt));
	}	
	public boolean ehUmDesses(String st, String stt, String sttt, String stttt, int xa, int ya, int za, int xt, int yt, int zt){
		int v = getCub(xa,ya,za,xt,yt,zt);
		return J.vou(v,getInd(st),getInd(stt),getInd(sttt),getInd(stttt));
	}		

	public boolean ehEsse(String st, int xa, int ya, int za, int xt, int yt, int zt){
		int v = getCub(xa,ya,za,xt,yt,zt);
		return v==getInd(st);
	}
	public boolean ehEsse(int t, int xa, int ya, int za, int xt, int yt, int zt){
		int v = getCub(xa,ya,za,xt,yt,zt);
		return v==t;
	}
	public boolean ehEsse(int t, int tt, int xa, int ya, int za, int xt, int yt, int zt){
		// comparacao "OU", ou t ou tt retorna true
		int v = getCub(xa,ya,za,xt,yt,zt);
		return v==t || v==tt;
	}	
	public void trocaPos(int d, int xa, int ya, int za, int xt, int yt, int zt){
		int t = getCub(xa,ya,za,xt,yt,zt);
		int x = xa, y=ya, z=za;
		switch(d){
			case NOR: z++; break;
			case SUL: z--; break;
			case LES: x++; break;
			case OES: x--; break;
			case CMA: y++; break;
			case BXO: y--; break;
				case SUL2: z-=2; break;			
				case BS: y--; z--; break;			
				case NOR2: z+=2; break;			
				case BN: y--; z++; break;
				case LES2: x+=2; break;			
				case BL: y--; x++; break;				
				case OES2: x-=2; break;			
				case BO: y--; x--; break;								
			default: J.impErr("!direcao nao definida","trocaPos()");
		}
		int tt = getCub(x,y,z,xt,yt,zt);
		setCub(tt, xa,ya,za,xt,yt,zt);
		setCub(t, x,y,z,xt,yt,zt);
	}
	public boolean moveSV(int d, int xa, int ya, int za, int xt, int yt, int zt){
		if(getCubMod(xa,ya,za,xt,yt,zt).opVatorPodeMover==false) return false;
		int m=0, n=0, o=0;
		switch(d){
			case NOR: o++; break;
			case SUL: o--; break;
			case LES: m++; break;
			case OES: m--; break;
			case CMA: n++; break;
			case BXO: n--; break;
			default: J.impErr("!direcao nao definida: "+d,"moveSV(d,xa,ya,za,xt,yt,zt)");
		}
		int v = getCub(xa,ya,za,xt,yt,zt);
		int vi = getInf(xa,ya,za,xt,yt,zt);
		int vs = getCub(xa+m,ya+n,za+o,xt,yt,zt); // valor de substituicao
		if(vs==0 || vs==getInd("plotar") || getCubMod(vs).opFazRio){ // ficou bom isso???
			setCub(0,0,xa,ya,za,xt,yt,zt);
			setCub(comCai(v), vi,xa+m,ya+n,za+o,xt,yt,zt);			
			return true;
		}
		return false;
	}
	public void move(int d, int xa, int ya, int za, int xt, int yt, int zt){
		int v = getCub(xa,ya,za,xt,yt,zt);
		int vi = getInf(xa,ya,za,xt,yt,zt);
		int m=0, n=0, o=0;
		switch(d){
			case NOR: o++; break;
			case SUL: o--; break;
			case LES: m++; break;
			case OES: m--; break;
			case CMA: n++; break;
			case BXO: n--; break;
			default: J.impErr("!direcao nao definida: "+d,"move(d,xa,ya,za,xt,yt,zt)");
		}
		setCub(0, 0, xa,ya,za,xt,yt,zt);
		setCub(comCai(v), vi, xa+m,ya+n,za+o,xt,yt,zt);
	}
	
	public void ligaVator(int xa, int ya, int za, int xt, int yt, int zt){
		int vv=0, v = getCub(xa,ya,za,xt,yt,zt);
		boolean foi=false;
		if(v==tVatorNor){
			vv = getCub(xa,ya,za+1,xt,yt,zt);
			if(tCub.get(vv).opVatorPodeMover)
			if(moveSV(NOR, xa,ya,za+1,xt,yt,zt)){
				setCub(tHasteNS,xa,ya,za+1,xt,yt,zt);
				foi = true;
			}
		}
		if(v==tVatorLes){
			vv = getCub(xa+1,ya,za,xt,yt,zt);
			if(tCub.get(vv).opVatorPodeMover)
			if(moveSV(LES, xa+1,ya,za,xt,yt,zt)){
				setCub(tHasteLO,xa+1,ya,za,xt,yt,zt);
				foi = true;
			}
		}		
		if(v==tVatorSul){
			vv = getCub(xa,ya,za-1,xt,yt,zt);
			if(tCub.get(vv).opVatorPodeMover)
			if(moveSV(SUL , xa,ya,za-1,xt,yt,zt)){
				setCub(tHasteNS,xa,ya,za-1,xt,yt,zt);
				foi = true;
			}
		}		
		if(v==tVatorOes){
			vv = getCub(xa-1,ya,za,xt,yt,zt);
			if(tCub.get(vv).opVatorPodeMover)
			if(moveSV(OES , xa-1,ya,za,xt,yt,zt)){
				setCub(tHasteNS,xa-1,ya,za,xt,yt,zt);
				foi = true;
			}
		}				
		if(foi) wVatorOn.agPlay(1000);
	}
	public void desligaVator(int xa, int ya, int za, int xt, int yt, int zt){
		int vv=0, v = getCub(xa,ya,za,xt,yt,zt);
		boolean foi=false;
		if(v==tVatorNor){
			if(tHasteNS==getCub(xa,ya,za+1,xt,yt,zt)){
				move(SUL, xa,ya,za+2,xt,yt,zt);
				foi = true; // engolir haste
			}
		}
		if(v==tVatorLes){
			if(tHasteLO==getCub(xa+1,ya,za,xt,yt,zt)){
				move(OES, xa+2,ya,za,xt,yt,zt);
				foi = true; // engolir haste
			}
		}		
		if(v==tVatorSul){
			if(tHasteNS==getCub(xa,ya,za-1,xt,yt,zt)){
				move(NOR, xa,ya,za-2,xt,yt,zt);
				foi = true; // engolir haste
			}
		}		
		if(v==tVatorOes){
			if(tHasteLO==getCub(xa-1,ya,za,xt,yt,zt)){
				move(LES, xa-2,ya,za,xt,yt,zt);
				foi = true; // engolir haste
			}
		}		

		if(foi) wVatorOff.agPlay(1000);
	}	

	public void altVator(int xa, int ya, int za, int xt, int yt, int zt){
		int v = getCub(xa,ya,za,xt,yt,zt);
		int q=1000;
		if(v==tVatorNor){
			if(ehEsse("haste_ns",xa,ya,za+1, xt,yt,zt)){				
				// DEVE MOVER COM A HASTE, MAS NAO EMPURRAR UMA HASTE QQ Q JAH EXISTE
				move(SUL, xa,ya,za+2,xt,yt,zt);
				wVatorOff.agPlay(q);
			}	else {
				if(moveSV(NOR, xa,ya,za+1,xt,yt,zt)){
					setCub("haste_ns",xa,ya,za+1, xt,yt,zt);
					wVatorOn.agPlay(q);
				}
			}		
		}
		if(v==tVatorSul){
			if(ehEsse("haste_ns",xa,ya,za-1, xt,yt,zt)){
				move(NOR, xa,ya,za-2,xt,yt,zt);
				wVatorOff.agPlay(q);				
			}	else {
				if(moveSV(SUL, xa,ya,za-1,xt,yt,zt)){
					setCub("haste_ns",xa,ya,za-1, xt,yt,zt);
					wVatorOn.agPlay(q);
				}
			}		
		}
		if(v==tVatorLes){
			if(ehEsse("haste_lo",xa+1,ya,za, xt,yt,zt)){
				move(OES, xa+2,ya,za,xt,yt,zt);
				wVatorOff.agPlay(q);				
			}	else {
				if(moveSV(LES, xa+1,ya,za,xt,yt,zt)){
					setCub("haste_lo",xa+1,ya,za, xt,yt,zt);
					wVatorOn.agPlay(q);
				}
			}
		}
		if(v==tVatorOes){
			if(ehEsse("haste_lo",xa-1,ya,za, xt,yt,zt)){
				move(LES, xa-2,ya,za,xt,yt,zt);
				wVatorOff.agPlay(q);
			}	else {
				if(moveSV(OES, xa-1,ya,za,xt,yt,zt)){
					setCub("haste_lo",xa-1,ya,za, xt,yt,zt);
					wVatorOn.agPlay(q);
				}
			}
		}		
		if(v==tVatorCma){ 
			if(ehEsse("haste_cb",xa,ya+1,za, xt,yt,zt)){
				move(BXO, xa,ya+2,za,xt,yt,zt);
				wVatorOff.agPlay(q);
			}	else {
				if(moveSV(CMA, xa,ya+1,za,xt,yt,zt)){
					setCub("haste_cb",xa,ya+1,za, xt,yt,zt);
					wVatorOn.agPlay(q);
				}
			}
		}
		if(v==tVatorBxo){ 
			if(ehEsse("haste_cb",xa,ya-1,za, xt,yt,zt)){
				move(CMA, xa,ya-2,za,xt,yt,zt);
				wVatorOff.agPlay(q);
			}	else {
				if(moveSV(BXO, xa,ya-1,za,xt,yt,zt)){
					setCub("haste_cb",xa,ya-1,za, xt,yt,zt);
					wVatorOn.agPlay(q);
				}
			}
		}
	}
		JWin amb = new JWin(ms);
		boolean winAberta = false;
		String camCom = "joeCraft-listCom.txt";
	public void regWin(){
		amb.reg();
		
		if(amb.onConfirmOk()){
			amb.remove("confirm");
			prendeMouse(); // p 3d de novo			
		}
		if(amb.onWinOk("winSaveCtx")){
			camUltCtxEsc = camCtx+amb.getRet();
			saveCtxLandMark(camUltCtxEsc);			
			trocaTudo("land_mark","nulo");
			trocaTudo("land_mark_area","nulo");
			amb.remove("winSaveCtx");
			prendeMouse(); // p 3d de novo						
			return;
		}							
		if(amb.onWinOk("winOpenCtx")){
			camUltCtxEsc = camCtx+amb.getRet();
			openCtxLandMark(camUltCtxEsc, xaCtt, yaCtt, zaCtt, xtCtt, ytCtt, ztCtt);
			amb.remove("winOpenCtx");
			prendeMouse(); // p 3d de novo						
			xaCtt=INTNULO; // importante!
			return;
		}							
		if(amb.onWinCancel("winSaveCtx")) {
			trocaTudo("land_mark",null); 
			trocaTudo("land_mark_area",null); 
			amb.remove("winSaveCtx"); 
			return;			
		}
		if(amb.onWinCancel("winOpenCtx")) {
			amb.remove("winOpenCtx"); 
			xaCtt=INTNULO; // importante!
			return;
		}
		
		if(!winAberta) return;
		if(amb.getComp("win").onCreate()){
			amb.setText("txtCom",amb.getText("lstCom"));
			amb.getComp("txtCom").selAll();
			amb.setFocus("txtFil");
		}
		if(amb.getComp("txtCom").onDownPress()){
			amb.getComp("lstCom").selProx();
			amb.setText("txtCom",amb.getText("lstCom"));
		}
		if(amb.getComp("txtCom").onUpPress()){
			amb.getComp("lstCom").selAnt();
			amb.setText("txtCom",amb.getText("lstCom"));
		}		
		if(amb.getComp("txtFil").onDownPress()){
			amb.setFocus("lstCom");
		}
		if(amb.getComp("txtFil").onUpPress()){ // nao sei se jah tem JWin.onLeftPress()
			amb.setFocus("txtCom");
		}		
		if(amb.onEnterPress("txtFil")){
			amb.acClick("ok");
		}
		if(amb.onChange("txtFil")){
			// filtrar itens de lista depois 
			// tah em "joeCraft-listCom.txt"
			// mas seria melhor filtrar a listagem de todos os cubos
			String st = amb.getText("txtFil");
			st = J.emMaiusc(st);
			ArrayList<String> lst = new ArrayList<>();
			for(Cub c:tCub)
				if(J.tem(st,c.name))
					lst.add(c.name);
			amb.getComp("lstCom")	.clear();
			amb.getComp("lstCom")	.setList(lst);
		}				
		if(amb.getComp("lstCom").onLeftPress()){
			amb.setFocus("txtCom");
		}
		if(amb.getComp("lstCom").onRightPress()){
			amb.setFocus("txtFil");
		}
		if(amb.onChange("lstCom")){
			amb.setText("txtCom",amb.getText("lstCom"));
		}		
		if(amb.onEscPress()){
			amb.getComp("win").close();
		}
		if(amb.onEnterPress("lstCom")){ 
			amb.acClick("ok");
		}
		if(amb.onEnterPress("txtFil")){ 
			amb.acClick("ok");
		}
		if(amb.onEnterPress("txtCom")){ 
			amb.acClick("ok");
		}
		if(amb.onClick("ok")){ // intGiveme
			String st = amb.getText("txtCom");
			// mas irah alem de pegar cubos
			// deverah alterar ambientes e comandos mais complexos tb

			// mais prefiltros depois
			if(J.iguais(st,"switch")) st = "switch_on";			
			
			if(temCub(st)){
				amb.opAddInicioLista=true; 
				joeInv.addItmS(30,getInd(st));
				amb.getComp("lstCom").addItemSNT(amb.getText("txtCom"));
				J.saveStrings(amb.getComp("lstCom").getList(),camCom);				
				if(!shiftOn) amb.getComp("win").close();
			} else if(J.iguais(st,"van")){
/*				
real_paralel
vanilla
desert
polar
lua
aether
marte
zetta
nether				
*/
				proMundo("vanilla"); // outros depois
				amb.getComp("win").close(); // mais vai ter q mudar o esquema desse trecho
			} else if(J.iguais(st,"net")){
				proMundo("nether");
				amb.getComp("win").close(); 				
			} else if(J.iguais(st,"zet")){
				proMundo("zetta");
				amb.getComp("win").close(); 								
			} else {
				amb.getComp("rotAviso").setText("_nao encontrado:_|"+st+"|");				
				amb.setFocus("txtCom").selAll();
			}
		}
		if(amb.onClick("btCancel")){
			amb.getComp("win").close();
		}
		if(amb.getComp("win").onClose()){
			prendeMouse();
			winAberta=false;
		}
	}

	public void ajPaletaGeral(){
		// BUG: esse ajuste vai bem nos icones, mas a criacao de cubos nao tah obedecendo a paleta modificada
		J.cor[129] = J.mixColor(129,59);
		J.cor[129-9] = J.cor[16];
		J.degrade(120,129);
		
		J.cor[260] = J.cor[16];
		J.cor[269] = J.mixColor(0.3f, 129,119);
		J.degrade(260,269);	// terra cel
		
		J.cor[180] = J.cor[16];
		J.cor[189] = J.cor[9];
		J.degrade(180,189);		
		
		J.cor[270] = J.cor[16]; // folhagens cel
		J.cor[279] = J.mixColor(0.3f, 69,119);
		J.degrade(270,279);				
		
		J.cor[280] = J.cor[16]; // caule cel
		J.cor[289] = J.mixColor(0.3f, 59,119);
		J.degrade(280,289);
	}	
		boolean opAniTrigo=true;
	public void regAniCubs(){ // regAniTrigo
		if(!opAniTrigo) return;
		//getTime();
		// acho q eh a melhor forma de fazer isso. Nao fica bom se ficar em ccc pq o reg abaixo eh ativado a cada impressao, assim como o start de animacao, e isso consome muito clock.
		tCub.get(tCanaMadura-1).aplicarVentoTrigo(1f);
		tCub.get(tCanaMadura).aplicarVentoTrigo(1f);
		tCub.get(tBambuMaduro-1).aplicarVentoTrigo(1f);
		tCub.get(tBambuMaduro).aplicarVentoTrigo(1f);
		tCub.get(tMato-1).aplicarVentoTrigo(1f);
		tCub.get(tMato).aplicarVentoTrigo(1f);
		tCub.get(tTrigo-1).aplicarVentoTrigo(1f);
		tCub.get(tTrigo).aplicarVentoTrigo(1f);
		tCub.get(tCenoura-1).aplicarVentoTrigo(1f);
		tCub.get(tCenoura-2).aplicarVentoTrigo(1f);
		tCub.get(tBeterraba-1).aplicarVentoTrigo(1f);
		tCub.get(tBeterraba-2).aplicarVentoTrigo(1f);
		tCub.get(tBatata-1).aplicarVentoTrigo(1f);
		tCub.get(tBatata-2).aplicarVentoTrigo(1f);
		tCub.get(tFolhaCoq).aplicarVentoTrigo(3f);
		tCub.get(tFolhaBananeira).aplicarVentoTrigo(3f);
		tCub.get(tMilhoMaduro).aplicarVentoTrigo(2f);
		tCub.get(tMilhoCrescendo).aplicarVentoTrigo(2f);
		tCub.get(tMilhoSeco).aplicarVentoTrigo(2f);
		tCub.get(tMorangueiro-1).aplicarVentoTrigo(0.25f);
		tCub.get(tMorangueiro).aplicarVentoTrigo(0.25f);
		//tCub.get(tAlga).aplicarOndaAlga(0.25f);
		//repTime("regAniCubs()");
	}
		int ultTransp=tra0; // mecanismo p economizar clocks
	public int getSlotLivreTransp(){
		
		// cortando o caminho
		if(tCub.get(ultTransp+1).ehTransp)
		if(tCub.get(ultTransp+1).ativo==false){
			ultTransp++;
			return ultTransp;
		}
		
		// mais demorado aqui, mas somente se nao achar
		for(int q = tra0; q<tra9; q++)
			if(tCub.get(q).ehTransp)
			if(tCub.get(q).ativo==false){
				ultTransp=q;
				return q;
			}
		return -1; // sem slot disponivel. Nunca chegou ateh aqui, mas existe a possibilidade. Acho q deve crashar o programa.
	}

	public int getNumBau(int xa, int ya, int za, int xt, int yt, int zt){
		if(ehEsse(tBau,xa,ya,za,xt,yt,zt)){
			int ind = getInf(xa,ya,za,xt,yt,zt);		
			if(ind<bau.size()) return ind;
			setInf(bau.size(), xa,ya,za,xt,yt,zt);
			bau.add(new Bau());
		}
		return -1;	
	}
	public int getSlotLivreMob(){ // verSlotLivreMob
		for(int q = mob0; q<mob9; q++)
			if(tCub.get(q).ehMob){
				if(tCub.get(q).ativo==false) 
					return q;
			}
		return -1;	
	}
	public String stMob(int p){
		if(p==tAldea) return "aldea";
		if(p==tAldeao) return "aldeao";
		if(p==tBruxa) return "bruxa";
		if(p==tMago) return "mago";
		if(p==tGhost) return "ghost";
		if(p==tMinotauro) return "minotauro";
		if(p==tCaveira) return "caveira";
		if(p==tCreeper) return "creeper";
		if(p==tGalinha) return "galinha";
		if(p==tAvestruz) return "avestruz";
		if(p==tAranha) return "aranha";
		if(p==tPorco) return "porco";
		if(p==tVaca) return "vaca";
		if(p==tCavalo) return "cavalo";
		if(p==tOvelha) return "ovelha";
		if(p==tUrso) return "urso";
		if(p==tZumbi) return "zumbi";
		return "???";
	}
		final int 
			tAldea=30001,
			tAldeao=30002,
			tUrso=30003,
			tBruxa=30004,
			tMago=30005,
			tGhost=30006,
			tMinotauro=30007,
			tCaveira=30008,			
			tCreeper=30009,
			tGalinha=30010,
			tAvestruz=30011,
			tPorco=30012,
			tVaca=30013,
			tCavalo=30014,
			tOvelha=30015,
			tAranha=30016,
			tZumbi=30017; // fechar aldea..zumbi (a..z)
	
	public int iniMob(int t, int xa, int ya, int za, int xt, int yt, int zt){
		int i = getSlotLivreMob();

		if(opPlotSobreSolo){
			for(int r=0; r<altTab*tamArea; r++)
			if(getCub(xa,r,za,xt,yt,zt)==0){
				ya=r;
				break;
			}
			opPlotSobreSolo=false;
		}
		
		if(i!=-1){			
			trocaTudo(i,0); // ?eliminou o bug???
		
			if(t==-1) 
				t = tAldea+J.R(tZumbi-tAldea+1);
			
			if(!J.noInt(t,tAldea,tZumbi)) J.impErr("!mob invalido:"+t,"iniMob()");

			Cub c = tCub.get(i);
			c.cntMob=J.cont;
			
			// soh p garantir:		
			c.dtCen=null; 
			c.ehPack=false;
			c.opGirDtCen=false;
			
			if(t==tZumbi){ 
				c.setName("zumbi"); 
				c.dtCen = new J3d("zumbi02.j3d",0.01f); // zumbi03.j3d tem ossos p desmembramento, mas tah meio falhada a impressao
				c.setBracoDeZumbi();
			  c.intMob = PERSEGUIR_JOE;				
				c.insPlts(); // NAO USE COM "TINGIR"				
			}
			if(t==tAldea){
				c.setName("Aldea"); 
				c.dtCen = new J3d("aldea05.j3d",0.01f); 			
			  c.intMob = PASSEAR;
				c.insPlts(); // NAO USE COM "TINGIR"								
			}
			if(t==tAldeao){
				c.setName("Aldeao"); 
				c.dtCen = new J3d("aldeao06.j3d",0.01f); 
			  c.intMob = PASSEAR;
				c.insPlts(); // NAO USE COM "TINGIR"								
			}			
			if(t==tBruxa){
				c.setName("bruxa"); 
				c.dtCen = new J3d("bruxa01.j3d",0.01f); 
			  c.intMob = PERSEGUIR_JOE;
				c.insPlts(); // NAO USE COM "TINGIR"								
			}						
			if(t==tMago){
				c.setName("mago"); 
				c.dtCen = new J3d("mago01.j3d",0.01f); 
			  c.intMob = PASSEAR;
				c.insPlts(); // NAO USE COM "TINGIR"								
			}									
			if(t==tGhost){
				c.setName("ghost"); 
				c.dtCen = new J3d("ghost01.j3d",0.01f); 
				c.setBracoDeZumbi();								
			  c.intMob = PERSEGUIR_JOE;
				c.dtCen.tingir(99,addPal(J.rnd(119,179,49)*10+9));// variar segundo ambiente depois. ?transparencia???
				c.dtCen.defIPalts(0);
			}									
			if(t==tMinotauro){
				c.setName("minotauro"); 
				c.dtCen = new J3d("minotauro01.j3d",0.02f); 
			  c.intMob = PERSEGUIR_JOE;
				c.insPlts(); // NAO USE COM "TINGIR"								
			}												
			if(t==tCaveira){
				c.setName("caveira"); 
				c.dtCen = new J3d("caveira01.j3d",0.01f); 
			  c.intMob = PERSEGUIR_JOE;
				c.insPlts(); // NAO USE COM "TINGIR"								
			}
			if(t==tCreeper){				
				c.setName("creeper"); 			
				c.dtCen = new J3d("creeper02.j3d",0.01f);
				c.dtCen.defIPalts(0);
				c.dtCen.tingir(10,addPal(16));
				c.tippp=J.rnd(7,8,9,11,12,14,15,16,17)*10+9;
		//c.tippp=129; creeper barroso
				c.dtCen.tingir(99,addPal(c.tippp));
			  c.intMob = PERSEGUIR_JOE;
			}
			if(t==tPorco){				
				c.setName("porco"); 			
				c.dtCen = new J3d("porco03.j3d",0.01f);
				c.dtCen.tingir(99,addPal(J.rnd(4,5,10,12,13,17)*10+9));				
				c.dtCen.defIPalts(0);
			  c.intMob = PASSEAR;								
			}			
			if(t==tAranha){				
				c.setName("aranha"); 			
				c.dtCen = new J3d("spider01.j3d",0.01f);
				c.dtCen.tingir(99,addPal(J.rnd(10,12,17)*10+9));				
				c.dtCen.defIPalts(0);
			  c.intMob = PERSEGUIR_JOE;
			}			
			if(t==tVaca){				
				c.setName("vaca"); 			
				c.dtCen = new J3d("vaca01.j3d",0.01f);
				c.dtCen.tingir(99,addPal(J.rnd(4,10,11,12,13,17)*10+9));
				c.dtCen.defIPalts(0);
			  c.intMob = PASSEAR;								
			}						
			if(t==tOvelha){				
				c.setName("ovelha"); 			
				c.dtCen = new J3d("ovelha03.j3d",0.01f);
				int la=119, pl=139;
				switch(J.R(4)){
					case 1: pl=139; la=179; break; // fididinha
					case 2: pl=179; la=109; break; // castanha
					case 3: pl=179; la=179; break; // n e g r a
				}
				c.dtCen.tingir(89,addPal(la));
				c.dtCen.tingir(99,addPal(pl));
				c.dtCen.defIPalts(0);
			  c.intMob = PASSEAR;						 		
			}				
			if(t==tCavalo){				
				c.setName("cavalo"); 			
				c.dtCen = new J3d("cavalo04.j3d",0.01f);				
				c.dtCen.tingir(99,addPal(J.rnd(5,10,11,12,13,17)*10+9));
				c.dtCen.tingir(89,addPal(J.rnd(5,10,11,12,13,17)*10+9));
				c.dtCen.defIPalts(0);
			  c.intMob = PASSEAR;								
			}							
			if(t==tUrso){				
				c.setName("urso"); 			
				c.dtCen = new J3d("urso01.j3d",0.01f);				
				c.dtCen.tingir(99,addPal(J.rnd(59,109,119)));
				c.dtCen.tingir(89,addPal(179));
				c.dtCen.defIPalts(0); // variar pelo amb polar depois
			  c.intMob = PASSEAR;								
			}										
			if(t==tGalinha){
				c.setName("galinha"); 				
				c.dtCen = new J3d("galinha03.j3d",0.01f);
				c.dtCen.defIPalts(0);				
				c.dtCen.tingir(119,addPal(J.rnd(5,7,10,12,13,15,17,19,24)*10+9));
				c.dtCen.tingir(99,addPal(99));
				c.intMob=PASSEAR;
			}
			if(t==tAvestruz){
				c.setName("avestruz"); 				
				c.dtCen = new J3d("avestruz02.j3d",0.01f);
				c.dtCen.defIPalts(0);				
				c.dtCen.defIPalts(0);				
				c.dtCen.tingir(89,addPal(179));
				c.dtCen.tingir(99,addPal(139));
				c.dtCen.tingir(119,addPal(119));
				c.intMob=PASSEAR;
			}			
			
			if(c.dtCen==null){
				J.impErr("!mob desconhecido: |"+t+"|","iniMob()");
				return -1;
			}

			
			c.insPntLife();	
			c.ehMob=true;
			c.ehPack=false;
			c.ativo = true;
			c.opImpLife = true;
			c.tipp = t; // nao mecha em tip!
			c.life = 100;
			c.mobOlhaAng(J.R(700)/100f);
			setCub(c.tip, xa,ya,za,xt,yt,zt);
			//setCub(c.tipp, xa,ya,za,xt,yt,zt);
			return i;
		}
		impSts("sem mais slots disponiveis para mobs");
		return -1;
	}
	public void iniPack(int q, String t, int xa, int ya, int za, int xt, int yt, int zt){
		iniPack(q, getInd(t), xa,ya,za,xt,yt,zt);
	}
	public void iniPack(int q, int t, int xa, int ya, int za, int xt, int yt, int zt){
		if(q<=0) return;
		int liv = getSlotLivreMob();
		// se liv=-1, entao nao tem slot disponivel.
		// tratar disso depois

		
		Cub 
			c = tCub.get(liv),
			tc = tCub.get(t);
		c.selecionavel=false;	
		String p = (q>1?"multi":"cubo");
		JPal pl = null;
		if(c!=null){
			// deve amenizar o bug
			trocaRai(liv,0, 6, xa,ya,za,xt,yt,zt);
			
			
			// variar arq segundo o tipo do cubo: chapado, caule, grama, gelo ou minerio
			// jah tem arq "multiGelo01.j3d" e "multiMinerio01.j3d". falta grama?			
			// caule, 
			// grama, 
			// gelo ou 
			// minerio			
			// chapado, 
			if(tc.ehCaule){
				c.dtCen = new J3d(p+"Caule01.j3d",0.01f);
				pl = tc.crCma;
				c.dtCen.tingir(89,pl);
				pl = tc.crNor;
				c.dtCen.tingir(99,pl);
			} else if(tc.ehGrama || tc.ehMadeira){ // madeira aqui eh um teste. Mudar o shape depois
				c.dtCen = new J3d(p+"Grama01.j3d",0.01f);
				pl = tc.crCma;
				c.dtCen.tingir(89,pl);
				pl = tc.crNor;
				c.dtCen.tingir(99,pl);
/*	DEPOIS, se eu tiver paciencia
			} else if(c.ehTijolo){
				c.dtCen = new J3d("multiBrick01.j3d",0.01f);
				pl = tCub.get(t).crCma;
				c.dtCen.tingir(99,pl);
				pl = tCub.get(t).crDtNor;
				c.dtCen.tingir(89,pl);				
*/				
			} else if(tc.ehGelo){
				c.dtCen = new J3d(p+"Gelo01.j3d",0.01f);
				pl = tc.crCma;
				c.dtCen.tingir(99,pl);
				pl = tc.crDtNor;
				c.dtCen.tingir(89,pl);
			} else if(tc.ehMinerio){
				c.dtCen = new J3d(p+"Minerio01.j3d",0.01f);
				pl = tc.crCma;
				c.dtCen.tingir(99,pl);
				pl = tc.crDtNor;
				c.dtCen.tingir(89,pl);
			} else { // chapado
				c.dtCen = new J3d(p+"cubo01.j3d",0.01f);						
				pl = tc.crCma;
				c.dtCen.tingir(99,pl);				
			}
			
			c.insPntLife();	
			c.dtCen.defIPalts(0);
			c.ativo = true;
			// c.tip = tPack; // NAO AQUI!
			c.opImpLife = false;
			c.tipp = t;
			c.life = q;	
			c.dx=0; c.dy=0; c.dz=0;
			c.intMob = PACK_PERSEGUE_JOE;
			c.setName(q+" x "+tCub.get(t).name);
			c.ehPack=true;
			c.opGirDtCen=true;
			c.dtCen.desCma(null,-0.25f,1); // eh invertido mesmo
			//c.insPlts(); // revisar estes dois depois
			//c.ajPals();

			{
				int m=0, n=0, o=0;
				for(int r=0; r<100; r++){
					m=J.RS(3);
					n=J.RS(3);
					o=J.RS(3);					
					if(setCubSV(c.tip,0, xa+m, ya+n,za+o, xt,yt,zt)) return;						
				}
				joeInv.addItm(c.life,c.tipp);
				return; // direto p inv
			}
		}
	}
	
		int cIsca = 0;
	public void regPesca(){
		if(cIsca>0)cIsca--;
		if(opVarinhaOn)
		if(XPesca!=0)	
		if(joeInv.naMao()==tVarinha){
			int w = 80;
			if(opGrowFast) w = 22; // hehe
			if(J.C(w))
				if(J.B(7)){ // podia mudar isso: com chuva, pescar mais facil
					cIsca=tmpPlot<<1; // se b 1 aqui, puxa um  p e i x e  p inv
					wPlotAgua.play();
					//tCub.get(tIsca).dtCen.desBxoR(null,-0.25f,1);
					tCub.get(tIsca).dtCen.desBxoR(null,-0.6f,6);					
				}
			if(maoDir==null) return;
			J3d.Ponto p = maoDir.getPnt("ponta"); // truque interessante
			J.bufLin(J.cor[15], p.X, p.Y, XPesca, YPesca);
			XPesca=0;
		}		
	}
	
		Long repTime=0L;
		String stRepTime=""; // abaixo seria p avaliar o tempo de algum metodo. Talvez use mais depois.
	public void getTime(){
		repTime = J.nanoTime();
	}
	public void repTime(String st){
		stRepTime = st+": "+ ((J.nanoTime()-repTime)/10000);
	}
	public boolean setTransp(int tp, int xa, int ya, int za, int xt, int yt, int zt){ // iniTransp
		if(tTranspVazio==getCub(xa,ya,za,xt,yt,zt)){
			int t = getSlotLivreTransp();
			if(t==-1) return false;
			tCub.get(t).ativo=true;// isso eh importante!!!
			int d=0;
			
			// ?a prioridade abaixo tah certa???
					 if(getCub(xa,ya,za+1,xt,yt,zt)==tTranspVazio)	d=NOR;
			else if(getCub(xa,ya,za-1,xt,yt,zt)==tTranspVazio)	d=SUL;
			else if(getCub(xa+1,ya,za,xt,yt,zt)==tTranspVazio)	d=LES;
			else if(getCub(xa-1,ya,za,xt,yt,zt)==tTranspVazio)	d=OES;
			else if(getCub(xa,ya+1,za,xt,yt,zt)==tTranspVazio)	d=CMA;
			else if(getCub(xa,ya-1,za,xt,yt,zt)==tTranspVazio)	d=BXO;

			tCub.get(t).dir = d;	
			tCub.get(t).dxx=0;
			tCub.get(t).dyy=0;
			tCub.get(t).dzz=0;
			Itm it = new Itm(1,tp);
			tCub.get(t).tipp=it.tip; 
			tCub.get(t).dtCen = carrDtPack(it);
			tCub.get(t).dtCen.deslTudo(0,0.25f,0);
			//impSts("criado: "+getName(it.tip));

			setCub(t,xa,ya,za,xt,yt,zt);
			return true;
		}
		return false;
	}
	public boolean setTransp(String t, int xa, int ya, int za, int xt, int yt, int zt){ // iniTransp
		return setTransp(getInd(t), xa,ya,za,xt,yt,zt);
	}	
		// quanto maior o valor abaixo, maior eh a qtd de cubos analizados por hit, o q faz o clock cair. Se ficar baixo, o clock melhora, mas atrapalha p selecionar cubos. Se ficar alto, o hit eh melhor avaliado, mas o clock sofre com isso, principalmente cubos mais proximos. Equilibrio.
		//int margemXhit=70, margemYhit = margemXhit;
		int margemXhit=50, margemYhit = margemXhit, cSpawn=0;

	public void regSlotMobIni(){ 
		// funcao: libera slots de mobs inativos
		// ini pq deve ser evocado no comeco do loop principal.
		if(!J.C(100)) return;
		
		boolean foi = false;
		for(int q = mob0; q<mob9; q++)
		if(J.cont-tCub.get(q).cntMob>100){
			tCub.get(q).zMob();
			foi=true;
		}							
		//if(foi) wPop.play(); // soh p debug
	}
	public void regSlotMobFim(){
		for(int q = mob0; q<mob9; q++)
			tCub.get(q).jahFoi=false;		
	}
		int cntIni=0;
	public void regIni(){		
		if(cntIni<=-1) return;		
		cntIni++;
		
		if(cntIni>1000) cntIni=-1;

		if(cntIni==17){
			J.opDebug=true;
			ms.hide();
			ms.sens*=4f;			
			altCamera(opCamera);
			iniBuffer();						
		}
		if(cntIni==18){
			setFov(J.fov);
			//setFov(J.fov+(ms.dr/10f));
			saveOpenMaqs(OPEN);
		}
		if(cntIni==19){ 				
			saveOpenBaus(OPEN);
		}
		if(cntIni==20){ 				
			saveOpenForns(OPEN);
		}				
		if(cntIni==21){ 
				wEst.opEco=false; // esse eh muito repetitivo
				ajPaletaGeral();
		
				par.opVel*=20;
				par.opRed = 1;
				par.opTam=10;
				par.opGrav=0;
				//par.opGrav=100;
//      VALORES DEFAULT:				
//			rv = 12, // velocidade de esparcamento: quanto maior, mais rapido a particula anda
//			rt=40; // lerdeza de reducao de tamanho: quanto menor, mais rapido a particula diminui ateh sumir
				
		}
		if(cntIni==22){
				JWin.tmp = 2;			
				JWin.opAddInicioLista=true;
		}
		if(cntIni==23) resetJoeBars();
		if(cntIni==24){ // inventario
				cCarrMao=22;
				wPortalProx.opEco=false;				
				yJoe=20;
				openStateJoe(); // tem q vir antes da criacao da tabela
		}
		if(cntIni==25) iniCubs();
		if(cntIni==26) iniBioma("vanilla"); // mas tem q pegar o ultimo amb armazenado (?foi armazenado???)
		if(cntIni==27){ // configuracoes retidas 00000000000000000
			if(!J.arqExist(camCfg)) {
				criarArqCfg(); // com configuracoes padrao
			} else {
				opSaveArea = J.leCfgBool("opSaveArea",camCfg);
				opSempreMeioDia = J.leCfgBool("opSempreMeioDia",camCfg);
				opCanBounceFaces = J.leCfgBool("opCanBounceFaces",camCfg);
				opModoFaquir = J.leCfgBool("opModoFaquir",camCfg);
				
				opFiltroInv = J.leCfgBool("opFiltroInv",camCfg);
				opJetPackInfinito = J.leCfgBool("opJetPackInfinito",camCfg);
				opSomJetPack = J.leCfgBool("opSomJetPack",camCfg);
				opSemChuva = J.leCfgBool("opSemChuva",camCfg);
				opImpNorAlt = J.leCfgBool("opImpNorAlt",camCfg);
			opImpCmaAlt = J.leCfgBool("opImpCmaAlt",camCfg);
			opEcoForn = J.leCfgBool("opEcoForn",camCfg);
			opEcoMaq = J.leCfgBool("opEcoMaq",camCfg);
			opEcoMobs = J.leCfgBool("opEcoMobs",camCfg);
			opImpPals = J.leCfgBool("opImpPals",camCfg);
			opAniTrigo = J.leCfgBool("opAniTrigo",camCfg);
			opImpPalPadrao = J.leCfgBool("opImpPalPadrao",camCfg);
			opImpTab2d = J.leCfgInt("opImpTab2d",camCfg);
			//opMouseMedia = J.leCfgBool("opMouseMedia",camCfg);
			opInfoDebug = J.leCfgBool("opInfoDebug",camCfg);
			opGerarMobs = J.leCfgBool("opGerarMobs",camCfg);
			opGastaItem = J.leCfgBool("opGastaItem",camCfg);
			opSaveInv = J.leCfgBool("opSaveInv",camCfg);
			opRespDebaixoDagua = J.leCfgBool("opRespDebaixoDagua",camCfg);
			opAltAgua = J.leCfgInt("opAltAgua",camCfg);
			opDensArv = J.leCfgInt("opDensArv",camCfg);
			
			// acho q tah na hora de deixar isso carregar no proximo ciclo
			// nao sei se o disco vai dar conta de consultar tudo isso de uma soh vez
			

				
			}
		}
		if(cntIni==28) tab = new Tab();				
		//if(J.cont==40) proMundo("marte"); // teleport instantaneo
		if(cntIni==60) J.opDebug=false;
		if(cntIni==100) setImpMargem(2);

		if(cntIni<1000) {
			// adeus tela branca
			J.impRet(2,0, 0,0, J.maxXf, J.maxYf);
		}
	}
		int opHora=16, opMinuto=30;
		boolean opSomNoite = !J.noInt(opHora,5,19);
		boolean opPausaHora=false, opSempreMeioDia=false, opSempreMeiaNoite=false;
	
		int opLuzGeral_=0, cTrovao=0;
		boolean opPause=false;
		Color crPause = new Color(0,0,0, 127);
	public void setPause(boolean p){
		
		if(p){ // pausar
			opPause=true;
			wAmb.stop();
			wUnderWater.stop();
			wForn.stop();
			//wOn.play();
		} else {
			opPause=false;			
			//wOff.play();			
			if(opUnderWater){
				wUnderWater.loop();				
			} else {
				wAmb.loop();				
			}
		}
	}	
		int cSomFogo=0, tmpSomFogo=30;
		J3d shGota = new J3d("chuva02.j3d",0.1f);
		ArrayList<Gota> gotas = new ArrayList<>();
		boolean opChuva=false;
	public class Gota{
		int x=0, y=0, z=0;		
	}	
	public void iniChuva(){
		impSts("iniChuva()");
		opChuva=true;
		wChuva.loop(); // ?e o som de chuva abafada quando dentro da casa???	
		gotas.clear(); // pq isso???
		if(gotas.size()<=0) {
			shGota.trocaCor(99,89);
			for(int q=0; q<300; q++) gotas.add(new Gota());
		}		
	}
	public void fimChuva(){
		impSts("fimChuva()");
		opChuva=false;
		wChuva.stop();		
	}		
	public void regChuva(){ // regNeve
		if(opSemChuva) return;
		if(J.C(200)) if(J.B(20)) if(opChuva) fimChuva(); else iniChuva();
		if(opUnderWater)  return; 
		if(!opChuva) return;
		
		if(J.C(200)) 
			if(J.B(20)) 
				cTrovao=100+J.RS(20); // acho q nao vai precisar agendar		
	



		for(Gota gg:gotas){
			gg.y--;
			gg.y--;
			if(gg.y<0) { // dah p melhorar esta condicao. Pensei na altura yHit. Depois.
				gg.y=100-J.R(100); 
				gg.x=J.RS(40); 
				gg.z=J.RS(40); 
			}
			shGota.impJ3d(gg.x,gg.y-6f,gg.z);
		}
	}
	public void regTrovao(){
	
		if(cTrovao<=0) return;
		cTrovao--;

		if(cTrovao==29) opLuzGeral_=opLuzGeral;
		if(J.noInt(cTrovao,26,28)) opLuzGeral=100; // luz saturada
		if(cTrovao==25) opLuzGeral=opLuzGeral_; // restaurando
		
		if(cTrovao==24) opLuzGeral_=opLuzGeral;
		if(J.noInt(cTrovao,21,23)) opLuzGeral=100; // luz saturada
		if(cTrovao==20) opLuzGeral=opLuzGeral_; // restaurando

		if(cTrovao==10) wTrovao.play();
	}

			float disFog=50f, disFogAr=100f, disFogAgua=10f; // com a distancia do joe acima deste valor, o fog passa a ser de 100%. Abaixo, diminui até zero (seria no pé do joe)
			Color crFog=null, crAgua=J.cor[1], crFogAr = J.cor[15], crFogAgua = J.cor[11];
	public void regFog(){
		//JPal.setTing( J.pulsar(J.cont>>2, 100)/100f,  J.cor[169]);		
		// ver depois. 
		if(evSubmergiu){
			crFog = crFogAgua;
			disFog = disFogAgua;
		} else if(evEmergiu){			
			crFog = crFogAr;			
			disFog = disFogAr;			
		}
	}
		boolean evEmergiu=false, evSubmergiu=false, opUnderWater=false; // tag evUnderWater
	public void regUnderWater(){		 // setUnderWater
		if(evEmergiu){			
			// tem um bug por aqui
			wUnderWater.stop();
			wAmb.loop();
			//J.pTont=0f; // esquece isso q pesa demais.

		}
		if(evSubmergiu){
			//J.pTont=0.1f; // cuidado com o clock. Isso pesa muito!
			wUnderWater.loop();			
			wAmb.stop();
		}		

		int m = (tamArea>>1) -(int)(J.xCam);
		int o = (tamArea>>1) -(int)(J.zCam);
		int naCab = getCub(m, (int)(yJoe+1f),	o, meioTab, 0, meioTab);				
		
		evEmergiu=false;
		evSubmergiu=false;
		if(opUnderWater){ // precisa ficar abaixo do cod acima p poder dar a volta no loop principal
			if(!tCub.get(naCab).opFazRio){
				opUnderWater=false;
				evEmergiu=true;
			}
		} else {
			if(tCub.get(naCab).opFazRio){
				opUnderWater=true;
				evSubmergiu=true;
			}		
		}
	}
	public void regAll(){ // sssssssssssssss	

		if(1==0) if(ms.b1){// calibre do cubo especifico da coordenada (nao do cubo modelo)
			getInfImp(xaHit,yaHit,zaHit,xtHit,ytHit,ztHit).cTreme=tmpRede;
			//getInfImp(xaHit,yaHit,zaHit,xtHit,ytHit,ztHit).setICal(J.RS(125)/1000f); // p cubo de coord especifica, nao cubo-modelo
			// !!!!!!!!!!!!! 
			getInfImp(xaHit,yaHit,zaHit,xtHit,ytHit,ztHit).ic=0.125f;
			getInfImp(xaHit,yaHit,zaHit,xtHit,ytHit,ztHit).ib=0.125f;
			getInfImp(xaHit,yaHit,zaHit,xtHit,ytHit,ztHit).in=-0.25f;
			getInfImp(xaHit,yaHit,zaHit,xtHit,ytHit,ztHit).is=-0.25f;
			getInfImp(xaHit,yaHit,zaHit,xtHit,ytHit,ztHit).il=+0.25f;
			getInfImp(xaHit,yaHit,zaHit,xtHit,ytHit,ztHit).io=+0.25f;
			// 
		}	
	
	
		if(opPause){
			J.impBufx2();
			J.impRetRel(crPause,null, 0,0,J.maxXf,J.maxYf);
			return;
		}
		if(cSomFogo>0){
			cSomFogo--;
			if(cSomFogo==0) wFogo.stop();
		}
		//J.cont++;			
		regIni(); 
		if(cSpace>0) cSpace++; // ?a quanto tempo a tecla espaco foi acionada???
		
		// mais p debug
		//if(ms.b1)	if(stHit!=null)	setDis((char)0, xaHit,yaHit,zaHit,xtHit,ytHit,ztHit);	
		
		tmpIni = J.nanoTime();
		
		{ // sons
			wJetPack.reg();
			
			wMinham.reg();
			wFiss.playSeAg();
			wVatorOn.playSeAg();
			wVatorOff.playSeAg();
			wSerra.playSeAg();
			wPloter.playSeAg();
			wPump.playSeAg();
			wEnvas.playSeAg();
			wMaqFunc.playSeAg();
			wExtractor.playSeAg();		
			wDerretedor.playSeAg();		
			wIdent.playSeAg();
			wGerCombustao.playSeAg();
			if(J.C(6))wEst.playSeAg();
		}
		
		regSlotMobIni();
		regFog();
		regTerr();
		regExt();
		regAniCubs();

		


		if(opEliminarMobs) // ajuda no debug
			if(J.C(12)) 
				opEliminarMobs=false;
		
		if(cArq>0) cArq--;
		if(cPlot>0) cPlot--;
		buss = calcBuss();
		
		if(ms.dr!=0)
		if(!ctrlOn){
			joeInv.selectRel(ms.dr);
			opVarinhaOn=false;
		}
		
		//if(ms.dr!=0) if(ctrlOn) setFov(J.fov+(ms.dr/10f));

		regUnderWater();
		regAnis();
		regPassJoe();
		
		regJoeCAC();
		regHor();
		if(tab!=null)
			tab.reg(); // consulta arquivos		


		
		zHits();		
		regCapacete();
		regJetPack();
		regJump();
		if(tab!=null)
			tab.imp3d();

		regMontJoe(); // precisa ficar depois da impressao da tab		


		//J.opDebug=true;
		
		if(J.opDebug)
			J.bufRet(0,15, 
				J.xPonto-margemXhit,
				J.yPonto-margemYhit,
				J.xPonto+margemXhit,
				J.yPonto+margemYhit);		

		regPesca();		
		regMargem();
		if(cOculos>0) regOculos();

		regArco();		
		regMao(); // imprimindo no buffer

		impJoe();
		regChuva(); 
		regTrovao();
		
		//J.impBufx2();
		J.impBuf(10,10,J.maxXf-10, J.maxYf-10-70);
		
		regBarras();		
	
		// J.impBuf(1,1,J.maxXf, J.maxYf);
		joeInv.imp(100,J.maxYf-70-70);
		regEquips();
		if(mobScp!=null)
			mobScp.ecoarScp();
		

		regGet();
	
		if(cntIni>100) tab.imp2d(100,100);
		if(opImpPals) impPals(J.maxXf-160,12);		
		
		impCur();
		
		regSts();
		regBanc();
		regForn();
		regMaq();
		regBau();
		
		regWin();		
		regSlotMobFim();
		if(menuAberto) regMenu();		
		par.reg(); // aparece fora do buffer		
		mouseMedia(); // precisa ficar antes de regMouse
		regMouse();
		impInfo();
		//J.impPlt(12,12);
	}
		float msdx=0, msdy=0, ft=0.6f;
		boolean opMouseMedia=true;
	public void mouseMedia(){
		// daria p melhorar o nome das vars e otimizar calculos, mas jah ajudou muito.
		if(!opMouseMedia) return;
		//esc(600,300, ""+ms.dx+":"+ms.dy);
		if(ms.foiProMeio) return;
		
		if(ms.dx!=0) msdx=ms.dx;
		if(ms.dy!=0) msdy=ms.dy;
		
		int sens = 600;		
		J.rotCam(-msdy/sens,-msdx/sens,0);
		
		if(msdx!=0) msdx*=ft;
		if(msdy!=0) msdy*=ft;
		float q = 0.001f;
		if(J.noInt(msdx,-q,+q)) msdx=0;
		if(J.noInt(msdy,-q,+q)) msdy=0;		
	}
	
//	Jf1 ff = new Jf1("shift01");

// === PARTICULAS =======================
		JPar par = new JParr();			
	public class JParr extends JPar{ // ss par
		public JParr(){
			//JPar.opShine=false;
		}
		public void reg(){
			boolean temRem = false;
			for(Par p:par){
				p.reg();
				if(p.rem) temRem=true;
			}	
			if(temRem){
				Iterator<Par> it = par.iterator();
				while(it.hasNext()){
					if (it.next().rem) it.remove();
				}
			}
		}
		
	}

// === AMBIENTE =============================

		
		int
			opAltAgua=16, // variar de acordo com o mundo
			aguaDoAmb=tAgua,
			tSolo = 1,
			tSSolo = 2,
			tSSSolo = 3,
			tSSSSolo = 4,
			opLuzGeral=0;
		final int lmLuzGeral=28; // precisa ir alem de JPal.lmCrPal, jah q esse valor irah p uma formula e interagirah com outros valores.
		JWav wAmb = null;
		String stAmb="???", stAmb_="???";;		

	
	public boolean iniBioma(int p){ // iniamb regAmb setAmb oooooooooooooooo
		{ // default e setup
			if(wAmb!=null) wAmb.stop();		
			//stAmb=p; // já foi feito la em cima
			opGrav=0.06f; // padrao vanilla
			lmVy = opGrav*10f;
			opLuzGeral = 0;
			J.pTont=0;
			J.opQtdNoise=0;
		
			crFogAr= null; // valores padrao
			disFogAr=100f;
			crFogAgua = new Color(130,130,255);
			crAgua=J.cor[1];
			disFogAgua= 50f;
			ms.opInvHor=false;
		}
		if(p==0){ // vanilla
			//defHor(69-1,9,89);
			defHor(1,89,116);
			tSolo = getInd("grama");
			tSSolo = getInd("terra");
			tSSSolo = getInd("rocha");
			tSSSSolo = getInd("rocha");			
			//wAmb = new JWav("ambiente10.wav");
			//wAmb = new JWav("ambiente61.wav");
			wAmb = new JWav("ambiente64.wav");
			wAmb.loop();			
		}
		if(p==2){ // desert
			defHor(J.altColor(J.cor[139],20),J.cor[9],J.cor[7]);
			tSolo = getInd("areia");
			tSSolo = getInd("rocha_deserto");
			tSSSolo = getInd("rocha");
			tSSSSolo = getInd("rocha");	
			wAmb = new JWav(J.rndSt("ambiente15.wav","ambiente55.wav"));
			wAmb.loop();
		}
		if(p==3){ // polar
			defHor(119-2,9,119);
			tSolo = getInd("neve");
			tSSolo = getInd("neve");
			tSSSolo = getInd("gelo");
			tSSSSolo = getInd("rocha");			
			wAmb = new JWav("ambiente32.wav");
			wAmb.loop();			
		}
		if(p==4){ // lua
			opGrav = 0.01f; // padrao vanilla
			crFogAgua = J.cor[171]; // 16 parece nao funcionar
			lmVy = 0.3f;
			defHor(49,16,16);
			tSolo = getInd("AREIA_LUNAR");
			tSSolo = getInd("AREIA_LUNAR");
			tSSSolo = getInd("ROCHA_LUNAR");
			tSSSSolo = getInd("ROCHA_LUNAR");			
			wAmb = new JWav("ambiente17.wav");
			wAmb.loop();			
		}
		if(p==5){ // aether
			opGrav = 0.03f; // padrao vanilla
			lmVy = 0.3f;
			defHor(119,9,89);
			crFogAgua = J.cor[11];
			tSolo = getInd("grama_cel");
			tSSolo = getInd("terra_cel");
			tSSSolo = getInd("areia_cel");
			tSSSSolo = getInd("terra_cel");			
			wAmb = new JWav("ambiente58.wav");
			wAmb.loop();			
		}
		if(p==6){ // marte
			//defHor(59,109,4); // chao, ceu e atmosfera
			defHor(
				new Color(.5f,.5f,.5f), // consertar isso depois. Um tom mais rozado seria melhor.
				new Color(170,130,150),
				new Color(255,255,255)); // chao, ceu e atmosfera
			crFogAr = J.cor[99];
			crFogAgua = J.cor[109];
			tSolo = getInd("AREIA_MARCIANA");
			tSSolo = getInd("AREIA_MARCIANA");
			tSSSolo = getInd("ROCHA_MARCIANA");
			tSSSSolo = getInd("ROCHA_MARCIANA");			
			wAmb = new JWav("ambiente55.wav");
			wAmb.loop();			
		}
		if(p==7){ // zetta
			//defHor(179,162,169);
			defHor(251,244,166);
			crFogAgua = J.cor[169];			
			crAgua=J.cor[169];
			tSolo = getInd("AREIA_ZETTA");
			tSSolo = getInd("AREIA_ZETTA");
			tSSSolo = getInd("ROCHA_ZETTA");
			tSSSSolo = getInd("ROCHA_ZETTA");
			wAmb = new JWav("ambiente01.wav");
			wAmb.loop();			
		}
		if(p==8){ // nether
			crFogAgua = J.cor[179];
			defHor(175,16,109-8);
			tSolo = getInd("CINZAS");
			tSSolo = getInd("CINZAS");
			tSSSolo = getInd("ROCHA_MAGMA");
			tSSSSolo = getInd("ROCHA_MAGMA"); 
			//tentar este tb: 05 12 25 26 31 35 37 46 47
			// 26 dah medo
			// 31 ruido de terra profunda
			// 35 e 37 gritos
			wAmb = new JWav("ambiente46.wav");
			wAmb.loop();			
		}
		getCubMod(tAgua).setCrCma(crAgua);
		return true;
	}
	public boolean iniBioma(String p){ // iniamb regAmb setAmb		
		if(J.iguais(p,stAmb="vanilla")) return iniBioma(0);
		if(J.iguais(p,stAmb="real_paralel")) return iniBioma(1);
		if(J.iguais(p,stAmb="desert")) return iniBioma(2);
		if(J.iguais(p,stAmb="polar")) return iniBioma(3);
		if(J.iguais(p,stAmb="lua")) return iniBioma(4);
		if(J.iguais(p,stAmb="aether")) return iniBioma(5);
		if(J.iguais(p,stAmb="marte")) return iniBioma(6);
		if(J.iguais(p,stAmb="zetta")) return iniBioma(7);
		if(J.iguais(p,stAmb="nether")) return iniBioma(8);
		J.impErr("!string de bioma nao indexado: ["+p+"]","iniBioma(String p)");
		return false;
	}
	
// === BUSSOLA ============================	
		int buss=J.NOR;
			
	public int calcBuss(){
		float in=0.36f, q = J.angY-in;
		if(q<in) return NOR;
		if(q<0.78f) return NL;
		if(q<1.58f) return LES;
		if(q<2.35f) return SL;
		if(q<3.15f) return SUL;
		if(q<3.92f) return SO;
		if(q<4.73f) return OES;
		if(q<5.52f) return NO;
		if(q>5.52f) return NOR;
		return NOR;
	}
	public String stBuss(int v){
		switch(v){
			case NOR: return "NOR";
			case SUL: return "SUL";
			case LES: return "LES";
			case OES: return "OES";
			case NL: return "NL";
			case SL: return "SL";
			case SO: return "SO";
			case NO: return "NO";
		}
		return "???";
	}
// =======================================	
	  boolean 
			opInfoDebug=false,
			opImpPalPadrao=false;
		Long tmpIni=0L;
		int frameRate=100, lmFrameRate=800;
	public void impInfo(){
		frameRate = (int)((J.nanoTime()-tmpIni)/100000f);
		esc(12,12,"");
		//esc("cSpace: "+cSpace);
		esc("frameRate: "+frameRate);		
		//esc("J.cont: "+J.cont);		
		
		if(opImpPalPadrao)		J.impPlt(1,1);
		
		if(opInfoDebug){					

			esc(stRepTime);
			esc("par.size(): "+par.size());
			esc("");
			esc("J.cont: "+J.cont);
			esc("opSaveArea: "+opSaveArea);
			esc("opGrowFast: "+opGrowFast);
			esc("numPlts: "+numPlts);
			esc("cTerr: "+cTerr);
			esc("winAberta: "+winAberta);
			esc("J.fov: "+J.fov);
			esc("yJoe: "+yJoe+"   vyJoe:"+vyJoe);
			esc("J.angY: "+J.angY);
			esc("J.angX: "+J.angX);
			esc("JPal.qTing: "+JPal.qTing);
			esc("numCubImp: "+numCubImp);
			esc("numJ3dImp: "+numJ3dImp);
			esc("numAreaImp: "+numAreaImp);
			esc("numAreaVazia: "+numAreaVazia);
			esc("numAreaOculta: "+numAreaOculta);
			esc("J.numPolImp: "+J.numPolImp);
			esc("bussola: "+stBuss(buss));
			esc("J.incZ: "+J.incZ);
			esc("tCub.size(): "+tCub.size());
			esc("opLuzGeral: "+opLuzGeral);
		}
		numCubImp=0;
		numJ3dImp=0;
		numAreaImp=0;
		numAreaVazia=0;
		numAreaOculta=0;
		J.numPolImp=0;
		
		if(opEcoMobs){
			esc(J.maxXf-100,12,"");
			String st="";
			for(int q=mob0; q<mob9; q++){
				st = ""+q+"  ";
				if(tCub.get(q).ativo) st+=stMob(tCub.get(q).tipp);
				esc(st);
			}
		}
	}
		String stMouse=null;
	public void regMouse(){
		{// ajustando o multPlot, tag multiPlot
			if(ms.dr!=0)
			if(ctrlOn){
				opNumMultPlot = J.corrInt(opNumMultPlot-ms.dr,2,24);		
				impSts("plotagem multipla ajustada para: "+opNumMultPlot);
			}
		}		
		
		if(!opSoltaMouse){
			mouseMedia(); // precisa ficar aqui
			ms.regMouse3D();					
		} else { // p interface de fornalhas, bancadas, baus e menus			
			if(itmArr!=null){				
				itmArr.imp(ms.x-10, ms.y-30, true);
				//esc(ms.x+20, ms.y, stItm(itmArr.tip));
				stMouse = getName(itmArr.tip);
			}
			if(stMouse!=null){
				J.impRetRel(16,15, ms.x+20, ms.y-12, J.larText(stMouse+"  "),16);
				esc(ms.x+20, ms.y, " "+stMouse);
			}
			stMouse=null;			
			ms.zMouse();
		}
	}	
	Itm itmArr = null;
	
// === MAQUINARIOS ============================
		int maqAberta=-1;
		final int MP=0, MPP=1, MPPP=2, BAT_IN=3, PD=4, PDD=5, PDDD=6, BAT_OUT=7;
		final int SLOT=1, TANQ=2, SHOW=SLOT;
		boolean opEcoMaq=false;	
		Jf1 fMaq = new Jf1("maq03.jf1"), fMaqOn = new Jf1("on01.jf1"), fMaqOff = new Jf1("off01.jf1");
		ArrayList<Maq> maq = new ArrayList<>();
	public void fechaMaq(){
		maqAberta=-1;
		prendeMouse();		
	}
	public void abreMaq(int t, int i){ // abrirMaq, openMaq
		soltaMouse();		
		if(altOn) return;			
		// limites?
		maq.get(i).tip=t;
		maq.get(i).xa = xaHit; // servico porco mas deve funcionar
		maq.get(i).ya = yaHit;
		maq.get(i).za = zaHit;
		maq.get(i).xt = xtHit;
		maq.get(i).yt = ytHit;
		maq.get(i).zt = ztHit;
		maqAberta=i;
	}

		String camMaq="maps/maqs.bin";
		int maxMaq=64; // eh o bastante? Nao quero sobrecarregar o sistema.
	public boolean saveOpenMaqs(boolean s){
		if(!s) if(!J.arqExist(camMaq)) {
			J.esc("o arquivo de maquinas nao existe. Serah criado um na saida do programa");
			// ?mesmo???
			return false;
		}
		RandomAccessFile arq = null;
		try{
			arq = new RandomAccessFile(camMaq,"rw");
			if(s){				
				arq.writeInt(maq.size());
				for(int q=0; q<maxMaq; q++){
					arq.writeInt(maq.get(q).tip);
					arq.writeInt(maq.get(q).el);
					arq.writeInt(maq.get(q).pronto);
					
					arq.writeInt(maq.get(q).mp.tip);
					arq.writeInt(maq.get(q).mp.qtd);
					arq.writeInt(maq.get(q).mpp.tip);
					arq.writeInt(maq.get(q).mpp.qtd);					
					arq.writeInt(maq.get(q).mppp.tip);
					arq.writeInt(maq.get(q).mppp.qtd);										
					
					arq.writeInt(maq.get(q).pd.tip);
					arq.writeInt(maq.get(q).pd.qtd);
					arq.writeInt(maq.get(q).pdd.tip);
					arq.writeInt(maq.get(q).pdd.qtd);					
					arq.writeInt(maq.get(q).pddd.tip);
					arq.writeInt(maq.get(q).pddd.qtd);										

					arq.writeInt(maq.get(q).ext.tip);
					arq.writeInt(maq.get(q).ext.qtd);
					arq.writeInt(maq.get(q).extt.tip);
					arq.writeInt(maq.get(q).extt.qtd);					
					arq.writeInt(maq.get(q).exttt.tip);
					arq.writeInt(maq.get(q).exttt.qtd);					
					
					arq.writeInt(maq.get(q).batIn.tip);
					arq.writeInt(maq.get(q).batIn.qtd);
					arq.writeInt(maq.get(q).batOut.tip);					
					arq.writeInt(maq.get(q).batOut.qtd);					

				}
			} else {
				maq.clear();
				int max = (int)arq.readInt(); // nao usou mas precisa ler
				Maq ff = null;
				int tp=-1;
				for(int q=0; q<maxMaq; q++){
					tp = (int)arq.readInt();
					
							 if(tp==tMaqBorracha) ff = new MaqBorracha();
					else if(tp==tMaqCompressor) ff = new MaqCompressor(); 
					else if(tp==tMaqDuct) ff = new MaqDuct(); 
					else if(tp==tMaqFio) ff = new MaqFio(); 
					else if(tp==tMaqFogaoEl) ff = new MaqFogaoEl(); 
					else if(tp==tMaqMoedor) ff = new MaqMoedor(); 
					else ff= new Maq(); // mmmmmmmmmmmmmmmmmmmmm
					// mais tipos depois. Esta linha eh muito importante. 
					
					
					
					ff.tip=tp;
					ff.el = (int)arq.readInt();
					ff.pronto = (int)arq.readInt();
					
					ff.mp.tip = (int)arq.readInt();
					ff.mp.qtd = (int)arq.readInt();
					ff.mpp.tip = (int)arq.readInt();
					ff.mpp.qtd = (int)arq.readInt();					
					ff.mppp.tip = (int)arq.readInt();
					ff.mppp.qtd = (int)arq.readInt();					
					
					ff.pd.tip = (int)arq.readInt();
					ff.pd.qtd = (int)arq.readInt();
					ff.pdd.tip = (int)arq.readInt();
					ff.pdd.qtd = (int)arq.readInt();					
					ff.pddd.tip = (int)arq.readInt();
					ff.pddd.qtd = (int)arq.readInt();					
					
					ff.ext.tip = (int)arq.readInt();
					ff.ext.qtd = (int)arq.readInt();
					ff.extt.tip = (int)arq.readInt();
					ff.extt.qtd = (int)arq.readInt();
					ff.exttt.tip = (int)arq.readInt();
					ff.exttt.qtd = (int)arq.readInt();					
					
					ff.batIn.tip = (int)arq.readInt();
					ff.batIn.qtd = (int)arq.readInt();
					ff.batOut.tip = (int)arq.readInt();
					ff.batOut.qtd = (int)arq.readInt();
					
					
					
					maq.add(ff);
				}
				
			}
		}catch(IOException e){
			e.printStackTrace();
			J.impErr("!Falha "+(s?"salvando":"abrindo")+" arquivo de maquinas","part2.saveOpenMaqs()");
		}		
		return true;
	}	

	// o certo seria linhas de comentário explicando como cada cubo vizinho a maquinas deve ser disposto p q ela funcione.
	public class Maq{
			Itm 
				mp = new Itm(0,0), mpp = new Itm(0,0), mppp = new Itm(0,0),
				pd = new Itm(0,0), pdd = new Itm(0,0), pddd = new Itm(0,0),
				ext = new Itm(0,0), extt = new Itm(0,0), exttt = new Itm(0,0), // slots extras inertes (?mesmo???)
				batIn = new Itm(0,0), batOut = new Itm(0,0);
			boolean 
				opImpBarPronto=true, opImpBarEl=true,
				rem=false, ligado=true;
			Cub condCon=null; // cond# conectado, p funcionamento remoto no novo esquema	
			int sel = 0, cTec=0, el=0, pronto=0,
				opMp=1, opMpp=1, opMppp=1, opPd=1, opPdd=1, opPddd=1, opBi=1, opBo=1, // 0=oculto, 1=mostrar como slot, 2=mostrar como tamque (este nao se aplica aa  b a t e r i a s f i x a s)
				opExt=1, opExtt=1, opExttt=1, // slots extras
				tip=-1, // isso eh automatico, indica o tipo de maq. Alterado automaticamente quando se abre a inteface... mas tb serah salvo
				maxTanqMaq=20, // nivel maximo dos tanques internos (todos)
				xt=intNull, yt=intNull, zt=intNull,
				xa=intNull, ya=intNull, za=intNull;
		public Maq(){
			mp = new Itm(0,0);
			mpp = new Itm(0,0);
			mppp = new Itm(0,0);
			pd = new Itm(0,0);
			pdd = new Itm(0,0);
			pddd = new Itm(0,0);
			batIn = new Itm(0,0);
			batOut = new Itm(0,0);
		}
		public void reg(){ // deve funcionar remotamente, por isso precisa estar separado de  i m p M a q
			// comum p todas as maquinas
			
			// esquema novo
			//if(ligado) // melhor assim
			if(el<100)
			if(xa!=intNull)
			if((J.cont+xa+ya+za)%10==0){
				condCon = getCond1Viz(xa,ya,za,xt,yt,zt);
				if(decCond1Viz(1,0, xa,ya,za,xt,yt,zt)) // bem prático este
					el++;			
			}
			
			// esquema antigo
			// pegando el de rede1
			//if(ligado) // melhor assim
			if(el<100)
			if(xa!=intNull)
			if((J.cont+xa+ya+za)%10==0)
			if(getCubMod(tRede1).life>0)
			if(temCubViz(tRede1,xa,ya,za,xt,yt,zt))
			{
				//wGet.play();
				getCubMod(tRede1).life--;
				el++;
			}
				

			// corrigindo bug
			if(mp.tip<=0) pronto=0;
			if(mp.qtd<=0) pronto=0;			

			if(mp.qtd<0) { mp.qtd=0; mp.tip=0; }
			if(mpp.qtd<0) { mpp.qtd=0; mpp.tip=0; }
			if(mppp.qtd<0) { mppp.qtd=0; mppp.tip=0; }
			
			int vBat=3; // velocidade em q as   b a t e r i a s f i x a s   (e similares) sao carregadas e descarregadas nos slots respectivos
			
			if(opBi==SHOW)
			if(J.vou(batIn.tip,tBateria,tJetPack))
			for(int w=0; w<vBat; w++)
			if(batIn.qtd>1)
			if(el<100){
				el++;
				batIn.qtd--;
				// nao zerar o item aqui. A  b a t e r i a   f i x a  (ou qq) deve estar descarregada aqui.
			}

			if(opBo==SHOW)
			if(J.vou(batOut.tip,tBateria,tJetPack))
			for(int w=0; w<vBat; w++)				
			if(batOut.qtd<100)
			if(el>0){
				el--;
				batOut.qtd++;
			}

		}
		
		public boolean temMp(int t){
			if(mp.tip==t)
			if(mp.qtd>0)
				return true;
			return false;
		}
		public boolean temMp(int t, int tt){
			if(mp.qtd>0)
			if(mpp.qtd>0){
				if(mp.tip==t) if(mpp.tip==tt)	return true; // ?fica bom assim???
				if(mp.tip==tt) if(mpp.tip==t)	return true;
			}
			return false;
		}
		public void proSlot(){			
			if(cTec>0) return;
			cTec=3;
			cCarrMao = tmpCarrMao;			
			int mao = joeInv.naMao();
			int q = joeInv.qtdNaMao();
			switch(sel){
				case MP: 
					if(q>0)
					if(mp.tip==mao || mp.tip==0){
						mp.qtd++;
						if(mp.tip==0) mp = new Itm(1,mao);
						joeInv.decSel();
					}
					break;
				// case MPP depois
				// case MPPP depois
				// case PD depois
				// case PDD depois
				// case PDDD depois
				// case batIn depois
				// case batOut depois
				case PD: 
					if(q>0)				
					if(pd.tip==mao || pd.tip==0){
						pd.qtd++;
						if(pd.tip==0) pd = new Itm(1,mao);
						joeInv.decSel();
					}
					break;		
			}
			if(mp.qtd==0) mp.fig=null; // tentando remover o bug do icone
			if(mpp.qtd==0) mpp.fig=null;
			if(mppp.qtd==0) mppp.fig=null;
			if(pd.qtd==0) pd.fig=null;
			if(pdd.qtd==0) pdd.fig=null;
			if(pddd.qtd==0) pddd.fig=null;
		}
		public void proInv(){
			if(cTec>0) return;
			cTec=3;
			int mao = joeInv.naMao();
			cCarrMao = tmpCarrMao;
			switch(sel){
				case MP:
					if(mp.qtd>0)
					//if(mao==0 || car.tip==mao)
					{
						if(joeInv.addItm(mp.tip))
							mp.qtd--;
					}
					break;
				//case BAT_IN: 
				//case BAT_OUT: 
				//case MPP: 
				//case MPPP: 
				//case PDD: 
				//case PDDD: 
				case PD: 
					if(pd.qtd>0)				
					//if(mao==0 || len.tip==mao)
					{
						if(joeInv.addItm(pd.tip))
							pd.qtd--;
					}
					break;				
			}
		}
		public void impEco(int i, int j, boolean sl){
			fMaq.crAnt=12;
			fMaq.zoom=2;
			int alt=20;
			if(el>0) fMaq.crNov=99-J.R(9);
			else fMaq.crNov=16;
			
			J.impRetRel((sl?crFornSel:null),(sl?J.cor[16]:null), i-3,j-3,260,20);
			
			fMaq.impJf1Rel(i,j-3,alt,alt); // variar o icone segundo o tipo de maquina depois
			
			// barras
			if(condCon!=null) {
				J.impRetRel(condCon.crCma.crSem,J.cor[16], i+65,j, 13,13);
				esc(i+80,j+12,""+J.emMinusc(condCon.name)+" "+condCon.life);
			}
				
			J.impBar(el,     100, 11,3, i+20, j, i+60, j+4);
			J.impBar(pronto, 100, 7,8, i+20, j+4, i+60, j+8);
			//impBarH(el,     9,1, i+50, j,    100);
			//impBarH(pronto, 7,8, i+50, j+16, 100);
		}
		public void regTec(){
			// depois
			if(enterOn) proInv();
			if(spaceOn) proSlot();

			if(cTec<=0){
				if(dirOn) sel = J.corrInt(++sel,0,7);
				if(esqOn) sel = J.corrInt(--sel,0,7);
				if(bxoOn) if(sel<=4) sel = J.corrInt(sel+=4,0,7); // ?bug aqui???
				if(cmaOn) if(sel>=4) sel = J.corrInt(sel-=4,0,7);
			}
				
				/*
			switch(sel){
				case MP: 
					if(dirOn) sel = MPP;
					if(bxoOn) sel = PD;
					break;					
				case MPP: 
					if(dirOn) sel = MPPP;
					if(bxoOn) sel = PDD;
					if(esqOn) sel = MP;
					break;										
				case MPPP: 
					if(bxoOn) sel = PDDD;
					if(esqOn) sel = MPP;
					break;
			}
			*/
			int q=3;
			if(dirOn) cTec=q;
			if(esqOn) cTec=q;
			if(bxoOn) cTec=q;
			if(cmaOn) cTec=q;
		}
		public void impMaq(){
			if(cTec>0) cTec--; // melhor q esteja aqui
			{// interruptor
				int i=xForn+larForn, j=yForn;
				fMaqOn.zoom=2; // preguica de colocar em iii
				fMaqOff.zoom=2;
				if(ligado) fMaqOn.impJf1(i,j);
				else fMaqOff.impJf1(i,j);
				if(ms.b1)
				if(ms.naAreaRel(i,j,40,40))
				if(cPlot<=0){
					cPlot=20;
					ligado=!ligado;
					if(ligado) wOn.play();
					else { wOff.play(); wMaqFunc.stop();}
				}
			}
			{// fundo
				int cr = 119; // default fornalha
				J.impRetRel(cr,49, 
					xForn,yForn, // pode usar estas vars mesmo.
					larForn,altForn);
			}	
			{// slots	
				// true se mouse sobre
				
				// a programacao abaixo ficou meio porca, mas funciona
				// nao tah muito eficiente nem otimizada. Nao tah elegante.
				// minha mente anda meio travada
				// melhorar depois, se precisar
				
				boolean smp = false; 
				if(opMp!=0) 
					smp = mp.imp(30+xForn,30+yForn, (sel==0));
				
				boolean smpp = false;
				if(opMpp!=0) 
					smpp = mpp.imp(60+xForn,30+yForn, (sel==1));
				
				boolean smppp = false;
				if(opMppp!=0) 
					smppp = mppp.imp(90+xForn,30+yForn, (sel==2));
				esc(10+xForn,30+yForn,"MP");

				boolean spd = false;
				if(opPd!=0) 
					spd = pd.imp(30+xForn,80+yForn, (sel==4));
				
				boolean spdd = false; 
				if(opPdd!=0) 
					spdd = pdd.imp(60+xForn,80+yForn, (sel==5));				
				
				boolean spddd = false;
				if(opPddd!=0) 
					spddd = pddd.imp(90+xForn,80+yForn, (sel==6));				
				
				boolean sext = false;
				if(opExt!=0) sext = ext.imp(170+xForn,yForn-30, (sel==7));
				boolean sextt = false;
				if(opExtt!=0) sextt = extt.imp(200+xForn,yForn-30, (sel==8));				
				boolean sexttt = false;
				if(opExttt!=0) sexttt = exttt.imp(230+xForn,yForn-30, (sel==9));
				
				esc(10+xForn,130+yForn,"Prod");				

				boolean sbi = false;
				if(opBi==SHOW) sbi = batIn.imp(260+xForn,30+yForn, (sel==3));
				
				boolean sbo = false;
				if(opBo==SHOW) sbo = batOut.imp(260+xForn,80+yForn, (sel==7));
				
				esc(260+xForn,22+yForn,"Bat");

					if(smp) stMouse=getName(mp.tip);
					if(smpp) stMouse=getName(mpp.tip);
					if(smppp) stMouse=getName(mppp.tip);

					if(spd) stMouse=getName(pd.tip);
					if(spdd) stMouse=getName(pdd.tip);
					if(spddd) stMouse=getName(pddd.tip);

					if(sext) stMouse=getName(ext.tip);
					if(sextt) stMouse=getName(extt.tip);
					if(sexttt) stMouse=getName(exttt.tip);

					if(sbi) stMouse=getName(batIn.tip);
					if(sbo) stMouse=getName(batOut.tip);
					
					boolean vai=false;
					if(smp) vai=true;
					if(smpp) vai=true;
					if(smppp) vai=true;
					if(spd) vai=true;
					if(spdd) vai=true;
					if(spddd) vai=true;					
					if(spddd) vai=true;
					if(sext) vai=true;
					if(sextt) vai=true;
					if(sexttt) vai=true;
					if(sbi) vai=true;
					if(sbo) vai=true;
					
					if(opSoltaMouse)
					if(vai)
					if(ms.b1)	
					if(ms.cClick>6){		

						// direto p inventario
						if(shiftOn){
							if(smp) if(joeInv.addItm(mp.qtd,mp.tip)) mp = new Itm(0,0);
							if(smpp) if(joeInv.addItm(mpp.qtd,mpp.tip)) mpp = new Itm(0,0);
							if(smppp) if(joeInv.addItm(mppp.qtd,mppp.tip)) mppp = new Itm(0,0);
							
							if(spd) if(joeInv.addItm(pd.qtd,pd.tip)) pd = new Itm(0,0);
							if(spdd) if(joeInv.addItm(pdd.qtd,pdd.tip)) pdd = new Itm(0,0);
							if(spddd) if(joeInv.addItm(pddd.qtd,pddd.tip)) pddd = new Itm(0,0);
							
							if(sext) if(joeInv.addItm(ext.qtd,ext.tip)) ext = new Itm(0,0);
							if(sextt) if(joeInv.addItm(extt.qtd,extt.tip)) extt = new Itm(0,0);
							if(sexttt) if(joeInv.addItm(exttt.qtd,exttt.tip)) exttt = new Itm(0,0);
							
							if(sbi) if(joeInv.addItm(batIn.qtd,batIn.tip)) batIn = new Itm(0,0);
							if(sbo) if(joeInv.addItm(batOut.qtd,batOut.tip)) batOut = new Itm(0,0);
							return;
						}

						if(itmArr==null){
							if(smp) if(mp.tip==0) return; // esse nao pode
							if(smpp) if(mpp.tip==0) return; // esse nao pode
							if(smppp) if(mppp.tip==0) return; // esse nao pode
							if(spd) if(pd.tip==0) return;							
							if(spdd) if(pdd.tip==0) return;							
							if(spddd) if(pddd.tip==0) return;							
							
							if(sext) if(ext.tip==0) return;							
							if(sextt) if(extt.tip==0) return;							
							if(sexttt) if(exttt.tip==0) return;							
							
							if(sbi) if(batIn.tip==0) return;							
							if(sbo) if(batOut.tip==0) return;							
							
							
							if(smp) { itmArr = mp; mp=new Itm(0,0);}
							if(smpp) { itmArr = mpp; mpp=new Itm(0,0);}
							if(smppp) { itmArr = mppp; mppp=new Itm(0,0);}
							
							if(spd) { itmArr = pd; pd=new Itm(0,0);}
							if(spdd) { itmArr = pdd; pdd=new Itm(0,0);}
							if(spddd) { itmArr = pddd; pddd=new Itm(0,0);}
							
							if(sext) { itmArr = ext; ext=new Itm(0,0);}							
							if(sextt) { itmArr = extt; extt=new Itm(0,0);}							
							if(sexttt) { itmArr = exttt; exttt=new Itm(0,0);}							
							
							if(sbi) { itmArr = batIn; batIn=new Itm(0,0);}							
							if(sbo) { itmArr = batOut; batOut=new Itm(0,0);}							
						} else {
							
							if(smp) if(mp.tip!=0) return; // trocar item depois
							if(smpp) if(mpp.tip!=0) return; 
							if(smppp) if(mppp.tip!=0) return; 
							
							if(spd) if(pd.tip!=0) return; // trocar item depois
							if(spdd) if(pdd.tip!=0) return; 
							if(spddd) if(pddd.tip!=0) return; 
							
							if(sext) if(ext.tip!=0) return; 
							if(sextt) if(extt.tip!=0) return; 
							if(sexttt) if(exttt.tip!=0) return; 
							
							if(sbi) if(batIn.tip!=0) return; 
							if(sbo) if(batOut.tip!=0) return;
							
							if(smp) mp=itmArr;
							if(smpp) mpp=itmArr;
							if(smppp) mppp=itmArr;
							if(spd) pd=itmArr;
							if(spdd) pdd=itmArr;
							if(spddd) pddd=itmArr;
							
							if(sext) ext=itmArr;
							if(sextt) extt=itmArr;
							if(sexttt) exttt=itmArr;
							
							if(sbi) batIn=itmArr;
							if(sbo) batOut=itmArr;
							itmArr=null;
						}
					}
				
				esc(10+xForn,10+yForn, getName(tip));
			}
			{// tanques embutidos
				int i = 130, j=30, esp=30;
				//esc(ms.x, ms.y, ""+(ms.x-xForn)+":"+(ms.y-yForn));
				
				// ATENCAO: assim desabilita o cheat de tanques embutidos
				//J.maisOn=false; J.menosOn=false;

				int mul = (int)(100/maxTanqMaq); 
				if(opMp==TANQ) if(impBarV(mp.qtd*mul, getCrCma0(mp.tip),xForn+i+esp*0,yForn+j, 100)) {stMouse = getName(mp.tip)+" "+mp.qtd; if(J.maisOn) mp.qtd++; if(J.menosOn) mp.qtd--; }
				if(opMpp==TANQ) if(impBarV(mpp.qtd*mul, getCrCma0(mpp.tip),xForn+i+esp*1,yForn+j, 100)) {stMouse = getName(mpp.tip)+" "+mpp.qtd; if(J.maisOn) mpp.qtd++; if(J.menosOn) mpp.qtd--; }
				if(opMpp==TANQ) if(impBarV(mppp.qtd*mul, getCrCma0(mppp.tip),xForn+i+esp*2,yForn+j, 100)) {stMouse = getName(mppp.tip)+" "+mppp.qtd; if(J.maisOn) mppp.qtd++; if(J.menosOn) mppp.qtd--; }
				j+=120;
				if(opPd==TANQ) if(impBarV(pd.qtd*mul, getCrCma0(pd.tip),xForn+i+esp*0,yForn+j, 100)) {stMouse = getName(pd.tip)+" "+pd.qtd; if(J.maisOn) pd.qtd++; if(J.menosOn) pd.qtd--; }
				if(opPdd==TANQ) if(impBarV(pdd.qtd*mul, getCrCma0(pdd.tip),xForn+i+esp*1,yForn+j, 100)) {stMouse = getName(pdd.tip)+" "+pdd.qtd;if(J.maisOn) pdd.qtd++; if(J.menosOn) pdd.qtd--; }
				if(opPddd==TANQ) if(impBarV(pddd.qtd*mul, getCrCma0(pddd.tip),xForn+i+esp*2,yForn+j, 100)) {stMouse = getName(pddd.tip)+" "+pddd.qtd;if(J.maisOn) pddd.qtd++; if(J.menosOn) pddd.qtd--; }
			}
			{// barras de pronto e el
				el = J.corrInt(el,0,100);
				pronto = J.corrInt(pronto,0,100);
				if(opImpBarEl)if(impBarV(el, 11,3,xForn+30,yForn+160, 100)) stMouse = "energia: "+el;
				if(opImpBarPronto)if(impBarV(pronto,  7,8,xForn+60,yForn+160, 100)) stMouse = ""+pronto+"% pronto";
			}
		}		
	}
	public class MaqDuct extends Maq{
		// maq ductisadora, deve criar fios desencapados a partir de poh de minerios. Deve ter um nome técnico melhor q este.
		public MaqDuct(){
			tip=tMaqDuct;
			opMp=SLOT; opMpp=0; opMppp=0;
			opPdd=SLOT; opPdd=0; opPddd=0;
		}
		public void reg(){
			super.reg();

			if(ligado)
			if(pronto<=0)
			if(el>0)
			if(temMp(tPohCobre,tPohCobre)){ // mais minerios depois
				pronto=1;
				wMaqFunc.stop();
				wMaqFunc.loop();
				wMaqStart.play();
			}
			if(el<=0) pronto=0;
			
			if(ligado)			
			if(el>0)
			if(mp.tip==tPohCobre)
			if(mp.qtd>0)
			if(pd.tip==0 || pd.tip==tFioCobre){
				esc(100,100, "xa"+xa);
				
				if(J.C(12))el--;
				if(el<=0) { wMaqNoEl.play(); wMaqFunc.stop(); }
				
				if(pronto<100) pronto++;
				if(pronto>=100){
					// som de producao feita aqui
					pronto=1;
					pd.tip = tFioCobre;
					pd.qtd++; // limites?
					wMaqPronto.play();
 					mp.qtd--; // ?tem correcao automatica aqui???
					if(mp.qtd<=0) mp.fig=null; // isso precisava tah embutido na impressao de itens, lah na outra classe
				}
			}				
		}		
	}
	public class MaqFio extends Maq{
		public MaqFio(){
			tip=tMaqFio;
			opMp=SLOT; opMpp=SLOT; opMppp=0;
			opPdd=0; opPddd=0;
		}
		public void reg(){
			super.reg();

			if(ligado)
			if(pronto<=0)
			if(el>0)
			if(temMp(tBorracha,tFioCobre)){
				pronto=1;
				wMaqFunc.stop();
				wMaqFunc.loop();
				wMaqStart.play();
			}
			if(el<=0) pronto=0;
			
			if(ligado)			
			if(el>0)
			if(mp.tip==tBorracha) 
			if(mp.qtd>0)
			if(mpp.tip==tFioCobre) 
			if(mpp.qtd>0)				
			if(pd.tip==0 || pd.tip==tRede0){
				esc(100,100, "xa"+xa);
				
				if(J.C(12))el--;
				if(el<=0) { wMaqNoEl.play(); wMaqFunc.stop(); }
				
				if(pronto<100) pronto++;
				if(pronto>=100){
					// som de producao feita aqui
					pronto=1;
					pd.tip = tRede0; 
					pd.qtd++; // limites?
					wMaqPronto.play();
 					mp.qtd--; // ?tem correcao automatica aqui???
 					mpp.qtd--; 
					if(mp.qtd<=0) mp.fig=null; // isso precisava tah embutido na impressao de itens, lah na outra classe
					if(mpp.qtd<=0) mpp.fig=null;
				}
			}				
		}		
	}
	public class MaqCompressor extends Maq{
		/* COMO FUNCIONA:
				disponibilize el com rede1
				coloque:
					-OU- um cilindro de oxigenio com qtd<100 no slot "prod"
					-OU- um tanque vazio abaixo
					-OU- um cano vazio abaixo
				deixe em "on", na interface				
				nao precisa de switch (?seria bom colocar um externo tb???)
		*/
		public MaqCompressor(){			
			tip=tMaqCompressor;
			opImpBarPronto=false;
			opMp=0; opMpp=0; opMppp=0;
			opPd=SLOT; opPdd=0; opPddd=0;
		}
		public void reg(){
			super.reg();
			
			/* existem dois processos aqui:
						- encher cilindro diretamente no compressor
						- encher tanque de oxigenio abaixo
						- encher cano de oxigenio abaixo
				 se nao estiver enchendo algum cilindro, entao encher o tanque/cano abaixo se disponivel		
			*/
			
			{// enchendo cilindro
				if(ligado)
				if(xa!=intNull) // esse eh importante p nao bugar
				if(pd.tip==tCilOxigenio)
				if(pd.qtd<100)
				if((J.cont+xa+ya+za)%10==0)
				if(J.iguais(stAmb,"vanilla") || J.iguais(stAmb,"desert") || J.iguais(stAmb,"polar")) // mais algum???				
				if(temCubViz(0, xa,ya,za,xt,yt,zt))  // ar disponivel... ou tanque de oxigenio... depois
				{
					wMaqFunc.stop();
					wMaqFunc.play();
					pd.qtd++;
					if(pd.qtd==100) {
						wMaqFunc.stop();
						wMaqStop.play();
					}
					return; // assim evita q se encha o tanque/cano abaixo simultaneamente... se bem q nem daria diferenca, mas...
				}			
			}
			
			{ // enchendo tanque/cano abaixo
				if(ligado)
				if(xa!=intNull) // esse eh importante p nao bugar
				if((J.cont+xa+ya+za)%10==0)
				if(J.iguais(stAmb,"vanilla") || J.iguais(stAmb,"desert") || J.iguais(stAmb,"polar")) // mais algum???				
				if(temCubViz(0, xa,ya,za,xt,yt,zt)){
					
					// enchendo tanques
					if(getInf(xa,ya-1,za,xt,yt,zt)<maxTanqMaq) // ?tah certo esse "maxTanqMaq"???					
					if(ehEsse(tTanqueOxigenio,tTanqueVazio,xa,ya-1,za,xt,yt,zt)){						
						int nv = getInf(xa,ya-1,za,xt,yt,zt);
						setCub(tTanqueOxigenio,1+nv, xa,ya-1,za,xt,yt,zt);					
						wMaqFunc.stop();
						wMaqFunc.play();					
					}

					// enchendo canos
					if(getInf(xa,ya-1,za,xt,yt,zt)<maxVolCano)
					if(ehEsse(tCanoOxigenio,tCanoVazio,xa,ya-1,za,xt,yt,zt)){						
						int nv = getInf(xa,ya-1,za,xt,yt,zt);
						setCub(tCanoOxigenio,1+nv, xa,ya-1,za,xt,yt,zt);					
						wMaqFunc.stop();
						wMaqFunc.play();					
					}					
					
				}
			}
		}
	}
	public class MaqBorracha extends Maq{
		public MaqBorracha(){			
			tip=tMaqBorracha;
			opMp=TANQ;
			opMpp=0; opMppp=0;
			opPdd=0; opPddd=0;
		}
		public void reg(){
			super.reg();
			
			
			// puxando do tanque acima
			if(ligado)
			if(mp.qtd<maxTanqMaq-1)	
			if(xa!=intNull) // esse eh importante p nao bugar
			if((J.cont+xa+ya+za)%10==0)
			if(getCub(xa,ya+1,za,xt,yt,zt)==tTanqLatex)
			if(getInf(xa,ya+1,za,xt,yt,zt)>0)
			{
				//wGet.play();
				// som de recarregamento aqui
				decInf(xa,ya+1,za,xt,yt,zt);
				mp.tip=tLatex;
				mp.qtd++;
			}
			
			
			if(ligado)
			if(el>0)
			if(temMp(tLatex)){
				if(J.C(12))el--;				
				
				if(pronto<100) pronto++;
				if(pronto>=100){
					// som de producao feita aqui
					pronto=0;
					pd.tip = getInd("borracha"); 
					pd.qtd++;
 					mp.qtd--; // ?tem correcao automatica aqui???
					if(mp.qtd<=0) mp.fig=null;
				}
			}				
		}
	}
	public class MaqFogaoEl extends Maq{
		public MaqFogaoEl(){ // soh serve p comida
			tip=tMaqFogaoEl;
			opMp=SLOT; opMpp=SLOT; opMppp=SLOT;
			opPdd=0; opPddd=0;
		}
		public void reg(){
			super.reg();
			
			if(J.C(100)){ // rodizio de slots
				if(mp.tip==0)	if(mpp.tip!=0) {mp=mpp; mpp=new Itm(0,0); }
				if(mpp.tip==0)	if(mppp.tip!=0) {mpp=mppp; mppp=new Itm(0,0); }
			}
			
			if(ligado)
			if(el>0)
			if(mp.tip!=0)
			if(getCubMod(getCozido(mp.tip)).ehComivel) // soh serve p comida, senao fica muito overPower
			if(pd.tip==getCozido(mp.tip) || pd.tip==0)
			{
				if(J.C(12))el--;				
				
				if(pronto<100) pronto++;
				if(pronto>=100){
					// som de producao feita aqui
					pronto=0;
					pd.tip = getCozido(mp.tip);
					pd.qtd++;
 					mp.qtd--; // ?tem correcao automatica aqui???
					if(mp.qtd<=0) mp.fig=null;
				}
			}				
		}
	}
	public class MaqMoedor extends Maq{
		public MaqMoedor(){			
			tip=tMaqMoedor;
			opMpp=0; opMppp=0;
			opPdd=0; opPddd=0;
			opPd=TANQ;
		}
		public void reg(){
			super.reg();
			// jogar p transp aqui tah gerando muito lag
			
			if(ligado)
			if(el>0)
			if(J.vou(pd.tip,0,tPoFerro)) 
			if(temMp(tMinFerro)){ // outros depois
				if(J.C(12))el--;				
				
				if(pronto<100) pronto++;
				if(pronto>=100){
					// som de producao feita aqui
					pronto=0;
					pd.tip = tPoFerro; // em embrulho
					if(xt!=intNull){
						// abaixo nao eh certo pq treme todos os cubos deste tipo plotados
						//getCubMod(xa,ya,za,xt,yt,zt).cTreme = tmpRede;
					}				
					pd.qtd++;
 					mp.qtd--; // ?tem correcao automatica aqui???
					if(mp.qtd<=0) mp.fig=null;
				}
			}				
		}
	}

	public Color getCrCma0(int t){
		Cub c = getCubMod(t);
		if(c!=null)
		if(c.crCma!=null)	
			return c.crCma.get(0);
		return J.cor[12];
	}
	public void regMaq(){
		if(maq.size()<=0)
			for(int q=0; q<maxMaq; q++)
				maq.add(new Maq());
			
		int i = J.maxXf-300, j=12, c=0;
		for(Maq f:maq){
			f.reg();
			if(opEcoMaq)
				f.impEco(i,j, c==maqAberta);
			if(c==maqAberta){
				f.regTec();
				f.impMaq();
			}
			j+=20;
			c++;
		}
	}

// === FORNALHA ============================
		final int
			xForn=200, yForn=100, larForn=300, altForn=300,
			CAR=0, LEN=1, BIF=2;
		int 
			fornAberta=-1;
		boolean opEcoForn=false;	
		Jf1 fForn = new Jf1("forn06.jf1");	
		ArrayList<Forn> forn = new ArrayList<>();
	public void fechaForn(){
		fornAberta=-1;
		prendeMouse();		
	}
	public void abreForn(int t, int i){
		soltaMouse();		
		if(altOn) return;			
		// limites?
		forn.get(i).tip=t; // fornalha, defumador, altoForno
		fornAberta=i;
	}

		Color crFornSel = J.altAlfa(J.cor[15],127);
		String camForn="forns.bin";
		int maxForn=32; // bastante, neh?
	public boolean saveOpenForns(boolean s){
		if(!s) if(!J.arqExist(camForn)) {
			J.esc("o arquivo de fornalhas nao existe. Serah criado um na saida do programa");
			// ?mesmo???
			return false;
		}
		RandomAccessFile arq = null;
		try{
			arq = new RandomAccessFile(camForn,"rw");
			if(s){				
				arq.writeInt(forn.size());
				for(int q=0; q<maxForn; q++){
					arq.writeInt(forn.get(q).fogo);
					arq.writeInt(forn.get(q).pronto);
					
					arq.writeInt(forn.get(q).car.tip);
					arq.writeInt(forn.get(q).car.qtd);
					
					arq.writeInt(forn.get(q).len.tip);
					arq.writeInt(forn.get(q).len.qtd);
					
					arq.writeInt(forn.get(q).bif.tip);
					arq.writeInt(forn.get(q).bif.qtd);					
				}
			} else {
				forn.clear();
				int max = (int)arq.readInt(); // nao usou mas precisa ler
				Forn ff = null;
				for(int q=0; q<maxForn; q++){
					ff = new Forn();
					ff.fogo = (int)arq.readInt();
					ff.pronto = (int)arq.readInt();
					ff.car.tip = (int)arq.readInt();
					ff.car.qtd = (int)arq.readInt();
					ff.len.tip = (int)arq.readInt();
					ff.len.qtd = (int)arq.readInt();
					ff.bif.tip = (int)arq.readInt();
					ff.bif.qtd = (int)arq.readInt();
					forn.add(ff);
				}
				
			}
		}catch(IOException e){
			e.printStackTrace();
			J.impErr("!Falha "+(s?"salvando":"abrindo")+" arquivo de fornalhas","part2.saveOpenForns()");
		}		
		return true;
	}	
	public class Forn{
			Itm 
				car = new Itm(0,0),
				len = new Itm(0,0),
				bif = new Itm(0,0);
			boolean ativo=false, rem=false;	
			int sel = 0, cTec=0, fogo=0, pronto=0,
				tip=-1, // isso eh automatico, indica se eh fornalha, defumador ou altoForno. Alterado automaticamente quando se abre a inteface.
				xw=intNull, yw=intNull, zw=intNull,
				xa=intNull, ya=intNull, za=intNull;
		public Forn(){
			car = new Itm(0,0);
			len = new Itm(0,0);
			bif = new Itm(0,0);
			ativo = true;
		}
		public Forn(int qc, int tc, int ql, int tl, int qb, int tb){
			ativo = true;
			car = new Itm(qc,tc); // icones resetar aqui
			len = new Itm(ql,tl);
			bif = new Itm(qb,tb);			
		}		
		public void reg(){ // deve funcionar remotamente, por isso precisa estar separado de  i m p F o r n 
		
			if(J.C(10)) tryIgnition();		

			if(len.qtd<0) { len.qtd=0; len.tip=0; }
			if(len.tip==0) if(car.tip==0) pronto=0;

			if(fogo>0){
				fogo--;
				if(fogo==0) wForn.stop();
				if(pronto<100) pronto++;
				
				// corrigindo bug
				if(car.tip<=0) pronto=0;
				if(car.qtd<=0) pronto=0;
				
				if(pronto>=100){
					pronto=0;
					bif.tip = getCozido(car.tip);
					bif.qtd+= getCubMod(car.tip).opMultBife; // ?e se estourar o slot???
 					car.qtd-= getCubMod(car.tip).opMultCarne;
 					//car.qtd--; // ?tem correcao automatica aqui???
					if(car.qtd<=0) car.fig=null;
				}
			}	
		}
		public void proSlot(){			
			if(cTec>0) return;
			cTec=3;			
			cCarrMao = tmpCarrMao;			
			int mao = joeInv.naMao();
			int q = joeInv.qtdNaMao();
			switch(sel){
				case CAR: 
					if(q>0)
					if(car.tip==mao || car.tip==0){
						car.qtd++;
						if(car.tip==0) car = new Itm(1,mao);
						joeInv.decSel();
					}
					break;
				case LEN: 
					if(q>0)				
					if(len.tip==mao || len.tip==0){
						len.qtd++;
						if(len.tip==0) len = new Itm(1,mao);
						joeInv.decSel();
					}
					break;		
				case BIF: 
					if(q>0)				
					if(bif.tip==mao || bif.tip==0){
						bif.qtd++;
						if(bif.tip==0) bif = new Itm(1,mao);
						joeInv.decSel();
					}
					break;	
			}
			if(car.qtd==0) car.fig=null; // tentando remover o bug do icone
			if(len.qtd==0) len.fig=null;
			if(bif.qtd==0) bif.fig=null;
		}
		public void proInv(){
			if(cTec>0) return;
			cTec=3;
			int mao = joeInv.naMao();
			cCarrMao = tmpCarrMao;
			switch(sel){
				case CAR:
					if(car.qtd>0)
					//if(mao==0 || car.tip==mao)
					{
						if(joeInv.addItm(car.tip))
							car.qtd--;
					}
					break;
				case LEN: 
					if(len.qtd>0)				
					//if(mao==0 || len.tip==mao)
					{
						if(joeInv.addItm(len.tip))
							len.qtd--;
					}
					break;				
				case BIF: 
					if(bif.qtd>0)				
					//if(mao==0 || bif.tip==mao)
					{
						if(joeInv.addItm(bif.tip))
							bif.qtd--;
					}				
					break;	
			}
		}
		public void impEco(int i, int j, boolean sl){
			fForn.crAnt=12;
			fForn.zoom=2;
			if(fogo>0) fForn.crNov=99-J.R(9);
			else fForn.crNov=16;
			
			J.impRetRel((sl?crFornSel:null),(sl?J.cor[16]:null), i-3,j-3,260,42);
			
			// icone piscante
			fForn.impJf1(i,j-3);
			
			
			// car, len, bif
			car.imp(i+ 40,j-1, false);
			len.imp(i+ 70,j-1, false);
			bif.imp(i+100,j-1, false);
			
			// barras
			impBarH(fogo,   12,4, i+130, j, 100);
			impBarH(pronto,  7,8, i+130, j+16, 100);
		}
		public void putToDebug(){
			len = new Itm(10,"madeira");
			car = new Itm(10,"agua");
			bif = new Itm(0,0);			
		}
		public void tryIgnition(){
			// preparo p condicoes
			//EXAMINAR CONDICAO AQUI !!!!!!!!!!!!!!!!!
			// se for defumador e bif for comivel (bebivel tb???)
			// se for alto forno e car com tag "ehMinerio"
			
			
			// examinando condicoes
			if(fogo<=1) // 1 p auto ignicao depois

			// se tem lenha
			if(len.tip!=0)
			if(tCub.get(len.tip).potCalor>0)

			// se tem carne cozivel
			if(car.tip!=0)
			if(tCub.get(car.tip).stCozido!=null)
			if(getInd(tCub.get(car.tip).stCozido)!=-1)
			if(car.qtd >= getCubMod(car.tip).opMultCarne) // default=1 // opMultBife depois

			// se tem slot vazio ou o mesmo
			if(bif.tip==0 
			|| bif.tip== getCozido(car.tip))

			// entao acender com a potencia de calor da lenha
			{
				wForn.stop(); 
				wForn.loop(); // e se tiver mais de uma fornalha acesa??? E se o joe estiver longe?
				fogo+= tCub.get(len.tip).potCalor;
				len.qtd--;
			}
		}
		public void regTec(){
			if(enterOn) proInv();
			if(spaceOn) proSlot();
			switch(sel){
				case CAR: // car=0; len=1; bif=2;
					if(dirOn) sel = BIF;
					if(bxoOn) sel = LEN;
					break;
				case LEN:
					if(dirOn) sel = BIF;
					if(cmaOn) sel = CAR;
					break;
				case BIF:
					if(esqOn) sel = LEN;
					if(bxoOn) sel = LEN;
					if(cmaOn) sel = CAR;
					break;					
					
			}
		}
		public void impForn(){
			if(cTec>0) cTec--; // melhor q esteja aqui
			{// fundo
				int cr = 179; // default fornalha
				if(tip==tDefumador) cr = 109;
				if(tip==tAltoForno) cr = 49;
				J.impRetRel(cr,16, 
					xForn,yForn,
					larForn,altForn);
			}	
			{// boca			
				int cr = 109-9;
				if(fogo>0) cr+=J.R(9);
				J.impCirc(cr, 80, xForn+(larForn>>1),yForn+(altForn>>1));
				J.impRet(cr,0, 
					xForn+(larForn>>1)-80,
					yForn+(altForn>>1),
					xForn+(larForn>>1)+80,
					yForn+altForn);
			}
			{// slots	
				boolean sc = car.imp(10+xForn,10+yForn, (sel==0));
				boolean sl = len.imp(10+xForn,50+yForn, (sel==1));
				boolean sb = bif.imp(70+xForn,30+yForn, (sel==2));
				
					if(opSoltaMouse)
					if(sc || sl || sb)
					if(ms.b1)	
					if(ms.cClick>12){						
						
						// direto p inventario
						if(shiftOn){
							if(sc) if(joeInv.addItm(car.qtd,car.tip)) car = new Itm(0,0);
							if(sl) if(joeInv.addItm(len.qtd,len.tip)) len = new Itm(0,0);
							if(sb) if(joeInv.addItm(bif.qtd,bif.tip)) bif = new Itm(0,0);								
							return;
						}
				
						if(itmArr==null){
							
							if(sc) if(car.tip==0) return; // esse nao pode
							if(sl) if(len.tip==0) return;							
							if(sb) if(bif.tip==0) return;							
							
							if(sc) { itmArr = car; car=new Itm(0,0);}
							if(sl) { itmArr = len; len=new Itm(0,0);}
							if(sb) { itmArr = bif; bif=new Itm(0,0);}							
						} else {
							
							if(sc) if(car.tip!=0) return; // trocar item depois
							if(sl) if(len.tip!=0) return; // trocar item depois
							if(sb) if(bif.tip!=0) return; // trocar item depois
							
							if(sc) car=itmArr;
							if(sl) len=itmArr;
							if(sb) bif=itmArr;
							itmArr=null;
						}
					}
				
				esc(10+xForn,10+yForn, getName(car.tip));
				esc(10+xForn,50+yForn+altItm+9, getName(len.tip));
				esc(70+xForn,30+yForn+altItm+9, getName(bif.tip));
			}
			{// barras
				fogo = J.corrInt(fogo,0,100);
				pronto = J.corrInt(pronto,0,100);
				impBarV(fogo, 12,4,xForn+10,yForn+100, 100);
				impBarV(pronto,  7,8,xForn+30,yForn+100, 100);
			}
		}		
	}
	public void regForn(){
		if(forn.size()<=0)
			for(int q=0; q<maxForn; q++)
				forn.add(new Forn());
			
		int i = J.maxXf-300, j=12, c=0;
		for(Forn f:forn){
			f.reg();
			if(opEcoForn)
				f.impEco(i,j, c==fornAberta);
			if(c==fornAberta){
				f.regTec();
				f.impForn();
			}
			j+=48;
			c++;
		}
	}
	
// === BAU =================================
		int bauAberto=-1;
		ArrayList<Bau> bau = new ArrayList<>();
		final int 
			xBau=xForn, yBau=yForn,
			maxXbau=12, maxYbau=4,
			larBau=(larItm+6)*maxXbau+larItm+12, 
			altBau=(altItm+6)*maxYbau+altItm+12;
		
	public void abreBau(int p){
		if(altOn) return; // nao abrir se estiver arrastando (puxando ou empurrando) o bau com alt		
		// limites depois
		// se o parametro acima nao for absurdo, daria p criar o bau na hora caso nao existisse. Corrigir inf nesse processo.
		bauAberto=p;
		if(bau.size()<=0) bau.add(new Bau());
		
		if(p>=bau.size()){
			//bauAberto=0;
			bau.add(new Bau());
			setCub(tBau,bau.size()-1, xaHit,yaHit,zaHit,xtHit,ytHit,ztHit);
			bauAberto=bau.size()-1; 
		}
		soltaMouse();		
	}	

	public void fechaBau(){
		// ?deve salvar o bau aqui depois??? Acho q nao. Pode ficar na memoria, mas sem icones.
		if(bauAberto!=-1)
			bau.get(bauAberto).zIcos(); // liberando a memoria
		bauAberto=-1;		
		prendeMouse();				
	}

		String camBaus = "maps//baus.bin";
	/* DEPOIS 
	DEPOIS
	converter o cod abaixo p baus
	

	public void saveBaus(){
		ArrayList<String> ln = new ArrayList<>();
		String st = J.intEmSt000(bau.size())+" ";
		ln.add(st);
		for(Bau b:bau) 
		for(int m=0; b.maxX){
			st="";
			st+= J.intEmSt000(b.cel.qtd)+" ";
			st+= J.intEmSt000(f.car.tip)+" ";
			st+= J.intEmSt000(f.len.qtd)+" ";
			st+= J.intEmSt000(f.len.tip)+" ";			
			st+= J.intEmSt000(f.bif.qtd)+" ";
			st+= J.intEmSt000(f.bif.tip)+" ";
			ln.add(st);
		}
		J.saveStrings(ln,camForn);
	}	
	public void openForns(){
		ArrayList<String> ln = null;
		ln = J.openStrings(camForn);
		int nf = J.stEmInt(ln.get(0));
		tForn.clear();
		Forn f = null;
		int qc=0, tc=0, ql=0, tl=0, qb=0, tb=0;
		String st = "";
		for(int n=1; n<nf; n++){
			st = ln.get(n);
			J.extTokens(st);
			qc = J.stEmInt(J.tk1);
			tc = J.stEmInt(J.tk2);
			ql = J.stEmInt(J.tk3);
			tl = J.stEmInt(J.tk4);			
			qb = J.stEmInt(J.tk5);
			tb = J.stEmInt(J.tk6);			
			f = new Forn(qc, tc, ql, tl, qb, tb);
			tForn.add(f);
		}
	}	
*/
	public void saveOpenBaus(boolean s){
		if(!s) if(!J.arqExist(camBaus)) {
			J.esc("o arquivo de baus nao existe. Serah criado um na saida do programa");
			return;
		}
		RandomAccessFile arq = null;
		try{
			arq = new RandomAccessFile(camBaus,"rw");
			if(s){
				arq.writeInt(bau.size());
				for(int q=0; q<bau.size(); q++)
				for(int m=0; m<maxXbau; m++)
				for(int n=0; n<maxYbau; n++){
					arq.writeInt(bau.get(q).cel[m][n].tip);
					arq.writeInt(bau.get(q).cel[m][n].qtd);
				}
			} else {
				int nb=(int)arq.readInt();
				Bau b = null;
				int it=0, qt=0;
				bau.clear();
				for(int q=0; q<nb; q++){
					b = new Bau();
					for(int m=0; m<maxXbau; m++)
					for(int n=0; n<maxYbau; n++){
						it = (int)arq.readInt();
						qt = (int)arq.readInt();
						b.cel[m][n].tip=it;
						b.cel[m][n].qtd=qt;
					}						
					bau.add(b);
				}				
			}
		}catch(IOException e){
			e.printStackTrace();
			J.impErr("!Falha "+(s?"salvando":"abrindo")+" arquivo de baus","part2.saveOpenBaus()");
		}
	}
	public void regBau(){
		// limites jah foram vistos???
		if(bauAberto!=-1){
			bau.get(bauAberto).imp(xBau,yBau);
			bau.get(bauAberto).regTec();
		}
	}

	public class Bau{ // ss bay
			Itm cel[][] = null;
			int xSel=3, ySel=3, cTec=0;			
		public Bau(){
			zBau();
		}		
		public void zBau(){
			Itm w[][] = new Itm[maxXbau][maxYbau];
			for(int m=0; m<maxXbau; m++)
			for(int n=0; n<maxYbau; n++)
				w[m][n] = new Itm(0,0);			
			cel = w;			
		}
		public int contItm(int t){
			int c=0;
			for(int m=0; m<maxXbau; m++)
			for(int n=0; n<maxYbau; n++)
				if(cel[m][n].tip==t) c+=cel[m][n].qtd;
			return c;
		}
		public void imp(int i, int j){
			if(cTec>0) cTec--;
			{// fundo
				J.impRetRel(4,0, i,j,larBau,altBau);
			}
			{// itens
				for(int m=0; m<maxXbau; m++)
				for(int n=0; n<maxYbau; n++)
					if(
					cel[m][n].imp(
						i+larItm+m*(larItm+2),
						j+altItm+n*(altItm+2),
							(m==xSel && n==ySel && (!bancAberta)))
						) {
									xSel=m; ySel=n;
									stMouse = getName(cel[m][n].tip);
									if(ms.cClick>12)
									if(ms.b1){
										//cTec=12; // LEMBRETE: ms.cClick eh sempre incrementado, mas zera quando o clique ocorre.
									
										// direto p inv
										if(shiftOn)
										if(joeInv.addItm(cel[m][n].qtd,cel[m][n].tip)){
											cel[m][n] = new Itm(0,0);
											return;
										}

										if(itmArr==null){
											if(cel[m][n].tip!=0){
												itmArr=cel[m][n];
												cel[m][n]= new Itm(0,0);
											}
										} else {
											if(cel[m][n].tip==0){
												cel[m][n] = itmArr;
												itmArr=null;
											}
										}
									}

							}
			}
			{// eco do item selecionado e numero do bau
				int v = cel[xSel][ySel].tip;
				int q = cel[xSel][ySel].qtd;
				esc(i+6,j+12,"");
				esc("# bau: "+bauAberto);
				esc(J.intEmSt000(q)+" "+getName(v));
			}
			{ // bancada inicial
			
				// if(!joeInv.tem(1,"bancada"))
				// if(joeInv.tem(1,"caule"))
					tCub.get(tBanc).getIcon().impJf1Rel(i+larBau-22,j+2,20,20);
				
				if(ms.b1)
				if(cTec<=0)
				if(ms.naAreaRel(i+larBau-22,j+2,20,20))
				if(joeInv.tem(1,"caule")){
					cTec=1000;
					joeInv.decItm(1,"caule");
					joeInv.addItm(1,"bancada");
					joeInv.select("bancada");
					wPop.play();
					impSts("bancada inicial criada");
				}
			}
		}
		public void regTec(){
			if(cTec>0) return;			
			
			int q=1; // lentidao das setas
			if(dirOn) {xSel = J.corrInt(++xSel,0,maxXbau-1); cTec=q;}
			if(esqOn) {xSel = J.corrInt(--xSel,0,maxXbau-1); cTec=q;}
			if(cmaOn) {ySel = J.corrInt(--ySel,0,maxYbau-1); cTec=q;}
			if(bxoOn) {ySel = J.corrInt(++ySel,0,maxYbau-1); cTec=q;}
			if(enterOn) {proInv(); cTec=q;}
			if(spaceOn) {proBau(); cTec=q;}
		}
		public void zIcos(){
			for(int m=0; m<maxXbau; m++)
			for(int n=0; n<maxYbau; n++)
				if(cel[m][n]!=null)
				  cel[m][n].fig = null;
			
		}
		public Itm retiraItem(){			
			Itm it = null;
			for(int m=0; m<maxXbau; m++)
			for(int n=0; n<maxYbau; n++)
				if(cel[m][n].tip!=0){
					it = new Itm(1,cel[m][n].tip);
					cel[m][n].qtd--;
					if(cel[m][n].qtd<=0)
						cel[m][n] = new Itm(0,0);
					return it;
				}			
			return null;
		}
		public boolean decItm(int t){
			for(int m=0; m<maxXbau; m++)
			for(int n=0; n<maxYbau; n++)
				if(cel[m][n].tip==t)
				if(cel[m][n].qtd>0){
					cel[m][n].qtd--;
					if(cel[m][n].qtd<=0) {
						cel[m][n].tip=0;
						cel[m][n].fig=null;
					}
					return true;
				}
			return false;
		}
		public boolean decItm(int q, int t){
			if(contItm(t)<q) return false;
			for(int qq=0; qq<q; qq++) // tah certo este "<="???
				decItm(t); // nao muito elegante mas funciona
			return true;
		}
		public void proInv(){
			// isso ta impreciso. Bug se nao tiver especo no inv
			// melhorar depois
			if(shiftOn){
				joeInv.addItmS(cel[xSel][ySel].qtd, cel[xSel][ySel].tip);
				cel[xSel][ySel].qtd=0;
				cel[xSel][ySel].tip=0;
				cel[xSel][ySel].fig=null;
			}	else if(joeInv.addItm(cel[xSel][ySel].tip))
				cel[xSel][ySel].qtd--;			
		}
		public boolean tem(int p){
			
			for(int m=0; m<maxXbau; m++)
			for(int n=0; n<maxYbau; n++){
				if(cel[m][n].tip==p){
					xSel=m; ySel=n;
					return true;
				}
			}
			return false;
		}
		public boolean tem(int q, int t){
			int c=0;
			for(int m=0; m<maxXbau; m++)
			for(int n=0; n<maxYbau; n++)
				if(cel[m][n].tip==t) 
					c++;
			if(c>=q)	return true;
			return false;
		}
		public void rnd(){
			wGet.play();
			
			Itm[][] w = new Itm[maxXbau][maxYbau];
			for(int m=0; m<maxXbau; m++)
			for(int n=0; n<maxYbau; n++)
				w[m][n] = new Itm(J.R(200), J.R(tCub.size()));
			cel = w;
		}
		public void proBau(){
			if(shiftOn){
				if(insBau(joeInv.qtdNaMao(), joeInv.naMao()))
					joeInv.zItm();
			} else {
				if(insBau(1, joeInv.naMao()))
					joeInv.decItm();
			}
		}
		public boolean insBau(int q, String st){
			return insBau(q, getInd(st));
		}
		public boolean insBau(String st){
			return insBau(1, getInd(st));
		}
		public boolean insBau(int q, int t){
			// retorna false se nao coube
			// nao existe insercao parcial
			
			// se slots identicos
			for(int m=0; m<maxXbau; m++)
			for(int n=0; n<maxYbau; n++){
				if(cel[m][n].tip==t){
					cel[m][n].qtd+=q;
					return true;
				}
			}
			// se nao tem slot igual, mas tem vazio
			for(int m=0; m<maxXbau; m++)
			for(int n=0; n<maxYbau; n++){			
				if(cel[m][n].tip==0){
					cel[m][n].qtd+=q;
					cel[m][n].tip=t;
					return true;
				}				
			}
			return false;
		}
		public void rnd2(){ 
			wGet.play();
			zBau();
			insBau(3,"areia");
			insBau(3,"areia_zetta");			
		}
	}

// === BANCADA ========================
	public boolean decInvBauJoe(int q, String st, int qq, String stt) {
		return decInvBauJoe(q, getInd(st)) && decInvBauJoe(qq, getInd(stt));
	}
	public boolean decInvBauJoe(int q, String st){
		return decInvBauJoe(q, getInd(st));
	}
	public boolean decInvBauJoe(int q, int t){
		// ISSO TAH BUGADO!!!
		// tah dando problema quando decrementa itens quebrados: (ex: 2trigo no inv + 1trigo no bau: tah eliminando certo do inv mas eliminando todos os itens do bau. Se tiver 10, vai todos.)
		
		// deve ser possivel decrementar quebrado: um tanto no inv e outro no bau.
		// decrementa preferencialmente do inventario
		
		// se tem no inv, sem contar o bau:
		if(joeInv.contItm(t)>=q){
			joeInv.decItm(q,t);
			return true;
		}
		
		// se soh tem no bau (to^ removendo bugs na marra)
		if(joeInv.contItm(t)<=0)
		if(bau.get(0).contItm(t)>=q){
			bau.get(0).decItm(q,t); 
			return true;
		}
		
		int qt = joeInv.contItm(t) + bau.get(0).contItm(t);
		if(qt<q) return false;
		
		
		
		int qi = joeInv.contItm(t);		
		joeInv.decItm(qi, t);		
		
		//bau.get(0).decItm(qrb,t);
		return true;
		
	}

		Banc banc = new Banc();
		boolean bancAberta=false;
	public void abreBanc(){ // abrirBanc openBanc
		if(altOn) return;
		bancAberta=true;
		banc.tip=getCub(xaHit,yaHit,zaHit,xtHit,ytHit,ztHit); // importante
		banc.verMontaveis();
		soltaMouse();		
	}
	public void fechaBanc(){
		bancAberta=false;
		if(bau.size()>0)
			bau.get(0).zIcos();
		// ?mais algum comando aqui???
		prendeMouse();		
	}	
	public void regBanc(){
		if(!bancAberta) return;
		
		banc.reg();
	}
	public class Banc{ 
		ArrayList<RegMont> regMont = new ArrayList<>();
		public class RegMont{ // depois. A mente tem q tah calibrada.
			int tp=0, tpp=0, tppp=0;
			int qp=0, qpp=0, qppp=0; // producao
			int tm=0, tmm=0, tmmm=0; // materia-prima
			int qm=0, qmm=0, qmmm=0;
		}
			int 
				tip=-1,
				xBanc=100, yBanc=40,
				lar=larBau, alt=altBau,
				espX=24, espY=24+12+4,
				maxX=12, maxY=4, 
				xSel=3, ySel=3;
			Itm cel[][] = null;	
			
		public Banc(){
			zCels();
		}	
		public void zCels(){
			Itm w[][] = new Itm[maxX][maxY];
			for(int m=0; m<maxX; m++)
			for(int n=0; n<maxY; n++)
				w[m][n] = new Itm(0,0);
			cel = w;
		}
		public void addBanc(int q, String st){
			for(int n=0; n<maxY; n++)			
			for(int m=0; m<maxX; m++)
				if(cel[m][n].tip==0){
					cel[m][n] = new Itm(q,getInd(st));
					return;
				}
			J.impErr("!sem mais slots disponiveis na bancada","addBanc");
		}
		public void addReg(int qp, String tp, int qm, String tm){
			addReg(qp,getInd(tp), qm,getInd(tm),0,0);				
		}
		public void addReg(int qp, String tp, int qm, String tm, int qmm, String tmm){
			addReg(qp,getInd(tp), qm,getInd(tm),qmm,getInd(tmm));
		}
		public void addReg(int qp, String tp, int qm, String tm, int qmm, String tmm, int qmmm, String tmmm){
			addReg(qp,getInd(tp), qm,getInd(tm),qmm,getInd(tmm),qmmm,getInd(tmmm));
		}		
		public void addReg(int qp, int tp, int qm, int tm, int qmm, int tmm){
			addReg(qp, tp, qm,tm, qmm,tmm, 0,0);
		}
		public void addReg(int qp, int tp, int qm, int tm, int qmm, int tmm, int qmmm, int tmmm){
			// centralizar aqui
			RegMont r = new RegMont();
			r.qp = qp;			r.tp = tp;
			r.tm = tm;			r.qm = qm;
			r.tmm = tmm;		r.qmm = qmm;
			r.tmmm = tmmm;	r.qmmm = qmmm; // se precisar tah aqui
			if(tem(qm, tm))
			if(tem(qmm, tmm))
			if(tem(qmmm, tmmm)){
				regMont.add(r);
				addBanc(r.qp, getCubMod(r.tp).name);
			}
		}
		public void decReq(Itm it){
			for(RegMont r:regMont)
				if(it.tip==r.tp)
				if(it.qtd==r.qp){ // abaixo tem q ser melhorado, mas jah funciona
					if(r.qmm==0) decInvBauJoe(r.qm, getName(r.tm));
					else decInvBauJoe(r.qm, getName(r.tm), r.qmm, getName(r.tmm));
					
					if(it.tip==getInd("borracha"))
						joeInv.addItm(1,"caneca_latex_vazia"); // bugou??
				}
		}
		public void verMontaveis(){ // bbbbbbbbbbbbbb
			zCels();
			regMont.clear();
			if(tip==tBigorna){
				addReg(1,"balde_vazio",  3,"barra_ferro");
				addReg(100,"arco",  6,"madeira", 6, "fibra_vegetal"); 
				addReg(100,"picareta",  3,"madeira",3,"barra_ferro"); 
				addReg(100,"enxada",  3,"madeira",2,"barra_ferro");
				addReg(100,"cinzel",  1,"barra_ferro");
				addReg(100,"colher_pedreiro",  1,"barra_ferro");
				addReg(100,"machado",  1,"madeira",2,"barra_ferro");
				addReg(100,"pah",  3,"madeira",1,"barra_ferro");
				addReg(100,"foice",  1,"madeira",1,"barra_ferro");
				addReg(100,"espada",  6,"barra_ferro"); // ? + uma pedra preciosa??? A pedra ajudaria p encantamentos, certo?
				addReg(12,"caneca_latex_vazia",  1,"barra_ferro"); // folha/lamina de metal depois, assim como p balde.
				
			} else { // bancada comum
				// lembre do esquema de bancada inicial
				
				// fundamento
				addReg(1,"fornalha",  8,"rocha");
				addReg(1,"bau",  8,"madeira");
				addReg(1,"bigorna",  12,"barra_ferro");  // soh no alto-forno depois
				
				// baderna
				addReg(1,"borracha",  1,"caneca_e_borracha");
				addReg(4,"madeira",  1,"caule"); 				
				
			
				// ?Uma bancada p preparar comida???
				addReg(9,"sem_milho",  1,"espiga"); // mais q isso acho q quebra o desafio... deve compensar demorando p crescer.
				addReg(6,"fatia_coco",  1,"coco"); 
				addReg(1,"farinha",  9,"trigo"); 
				addReg(3,"pao_cru",  3,"farinha", 1,"ovo");  
				addReg(6,"fatia_melao",  1,"melao"); 
				addReg(6,"fatia_melancia",  1,"melancia"); 
				addReg(6,"fatia_abobora",  1,"abobora"); 
				addReg(6,"banana_verde",  1,"cacho_banana_verde"); 
				addReg(6,"banana_madura",  1,"cacho_banana_madura"); 
				addReg(1,"torta_banana_crua",  6,"farinha", 6,"banana_madura", 6,"ovo"); 

				// ?uma mesa de artesao ???
				addReg(10,"varinha",  1,"varinha_bambu_grande",6,"fibra_vegetal");
				addReg(1,"pilastra_obsidian",  1,"obsidian",1,"cinzel"); // ?gastar mais cinzel aqui???
				addReg(1,"pilastra_granito",  1,"granito",1,"cinzel"); 
				addReg(1,"pilastra_marmore",  1,"marmore",1,"cinzel"); 
				addReg(1,"pilastra_madeira",  1,"madeira",1,"cinzel"); 
				addReg(1,"pilastra_rocha",  1,"rocha",1,"cinzel"); 
				addReg(1,"ladrilho4",  1,"rocha",1,"cinzel");  // mais ladrilhos depois
			

			}
		}
		public boolean tem(int q, String st, int qq, String stt){
			return tem(q,st) && tem(qq, stt);
		}		
		public boolean tem(int q, String st, int qq, String stt, int qqq, String sttt){
			return tem(q,st) && tem(qq, stt) && tem(qqq,sttt);
		}
		public boolean tem(int q, String st){
			return tem(q, getInd(st));
		}
		public boolean tem(int q, int t){
			int qb = bau.get(0).contItm(t);
			int qi = joeInv.contItm(t);
			if(qb+qi>=q) return true;
			return false;
		}		
		public void impItens(){
			boolean s = false;
			for(int m=maxX-1; m>=0; m--)
			for(int n=0; n<maxY; n++){
				s = (xSel==m)&&(ySel==n); 
				if(
				cel[m][n].imp(
					xBanc+m*espX+espX, 
					yBanc+n*espY+espY, 
					s)
					) {xSel=m; ySel=n; if(ms.b1) craft(); }
				if(s)	
					esc(
						xBanc+m*espX+espX+24, 
						yBanc+n*espY+espY+24, 
						J.cor[9],
						getName(cel[m][n].tip));
			}
		}
		public void regTec(){
			if(cmaOn) ySel = J.corrInt(--ySel,0,maxY-1);
			if(bxoOn) ySel = J.corrInt(++ySel,0,maxY-1);
			if(esqOn) xSel = J.corrInt(--xSel,0,maxX-1);
			if(dirOn) xSel = J.corrInt(++xSel,0,maxX-1);
			
			if(enterOn) craft();
		}
		public void craft(){ 
			if(cPlot<=0) 
			if(cel[xSel][ySel].tip!=0){ // "construa maquinas!!!"
				cPlot=12;
				wPop.play();// esse tb
				
				decReq(cel[xSel][ySel]);
				// depois; 
				
				joeInv.addItm(cel[xSel][ySel].qtd, cel[xSel][ySel].tip);
				verMontaveis();
			}			
		}
		public void reg(){
			imp();
			regTec();
		}
		public void imp(){
			// fundo
			int cr=139;
			if(tip==tBigorna) cr=179;
			
			J.impRetRel(cr,0, xBanc,yBanc,lar,alt);
			impItens();
			if(bau!=null)
			if(bau.size()>0)
				bau.get(0).imp(xBanc,yBanc+alt);
		}
	}

// === MENU ================================
		boolean 
			menuAberto = false,
			opSaveInv=true,
			opEcoMobs=true,
			opGerarMobs=false,
			opRespDebaixoDagua=false,
			opGastaItem = true; // soh p debug
		int 
			selMenu=0, cMenu=0, 
			maxMenu=0, cFechaMenu=0;
		String camCfg="JoeCraft.cfg";
	public void criarArqCfg(){
		// cria com cfg padrao
		// deveria salvar outras vars... mas acho q quando fechar a win de cfg jah vai gerar, certo?
		J.escCfg("opSaveArea",false,camCfg);
	}
	public void openMenu(){ // iniMenu
		menuAberto=true;
		soltaMouse();
		int lar=600, alt=430, larBt=100, altBt=30; 
		amb.clear();
		amb.addWin("Opcoes",100,60,lar,alt).setName("winOp");
		
		amb.addTextButton("limpar inventario",lar-140,20, larBt, altBt).setName("opLimpaInv");
		amb.addTextButton("limpar baus",lar-140,60, larBt, altBt).setName("opLimpaBaus");
		amb.addTextButton("resetar barras",lar-140,100, larBt, altBt).setName("resetBars");
		amb.addTextButton("reload mapa",lar-140,180, larBt, altBt).setName("reloadMap");
		amb.addTextButton("sem chuva",lar-140,220, larBt, altBt).setName("semChuva");
		
		amb.addCheckBox(opSaveArea,"gravar areas","opSaveArea",20,20);
		amb.addCheckBox(opSaveInv,"gravar inventario","opSaveInv");

		amb.addCheckBox(opRespDebaixoDagua,"respirar debaixo da agua","opRespDebaixoDagua");
		amb.addCheckBox(opCanBounceFaces,"oscilar faces animadas","opCanBounceFaces");
		amb.addCheckBox(opGastaItem,"decrementar item ao plota-lo","opGastaItem");
		amb.addCheckBox(opGerarMobs,"gerar mobs","opGerarMobs");
		amb.addCheckBox(opInfoDebug,"informacao de debug","opInfoDebug");
		//amb.addCheckBox(opMouseMedia,"suavizar mouse","opMouseMedia");
		amb.addCheckBox(opImpPalPadrao,"exibir paleta padrao","opImpPalPadrao");
		amb.addCheckBox(opAniTrigo,"animar trigo e similares","opAniTrigo");
		amb.addCheckBox(opImpPals,"exibir paletas dos cubos","opImpPals");
		amb.addCheckBox(opEcoMobs,"ecoar mobs","opEcoMobs");
		amb.addCheckBox(opEcoForn,"ecoar fornalhas","opEcoForn");
		amb.addCheckBox(opEcoMaq,"ecoar maquinas","opEcoMaq");
		amb.addCheckBox(opImpCmaAlt,"imp alternada cima","opImpCmaAlt");
		amb.addCheckBox(opImpNorAlt,"imp alternada norte","opImpNorAlt");
		amb.addTextBox(""+opAltAgua, amb.xIns, (amb.yIns+=36), 60,16).setName("opAltAgua").geraRotulo("altura da agua:").setTag("click para yJoe atual");
		amb.addTextBox(""+opDensArv, amb.xIns, (amb.yIns+=36), 60,16).setName("opDensArv").geraRotulo("densidade de arvores: [30..2000] 30 = muito denso");
		
		// segunda  c o l u n a 
		amb.addCheckBox(opSempreMeioDia,"sempre meio dia","opSempreMeioDia",220,20).setTag("pressione ASTERISCO no _numerico para pausar o _tempo quando esta opcao _estiver desmarcada");
		amb.addCheckBox(opModoFaquir,"modo faquir","opModoFaquir");
		amb.addCheckBox(opSemChuva,"sem chuva","opSemChuva");
		amb.addCheckBox(opSomJetPack,"som jetPack","opSomJetPack");
		amb.addCheckBox(opJetPackInfinito,"jetPack infinito","opJetPackInfinito");
		amb.addCheckBox(opFiltroInv,"filtro do inventa'rio","opFiltroInv");
		
		
		//amb.addCheckBox(opImpTab2d,"exibir mapa 2d","opImpTab2d");
		// um JWin.turnButton("estado1","estado2","estado3") seria bom
/*
		if(escM("modo de impressao da margem: "+opImpMargem)){
			setImpMargem(++opImpMargem);
		}				
		if(escM("teste de tingimento: "+ (JPal.crTing!=null))){
			if(JPal.crTing==null){
				JPal.setTing(0.80f, J.cor[J.rndCr9()]);
			}	else JPal.setTing(0, null);			
		}		
*/		
	
		amb.xIns = lar-200-20;
		amb.yIns = alt-40;
		amb.addTextButton("cancel",amb.xIns,amb.yIns,larBt,altBt).setName("cancel");
		amb.xIns+=110;
		amb.addTextButton("ok",amb.xIns,amb.yIns,larBt,altBt).setName("ok");
	}
	public void regMenu(){ // impMenu
		if(!menuAberto) return;
		if(amb.onClick("fimChuva")) fimChuva(); // ?tah funcionando isso???
		if(amb.onClick("opLimpaInv")){
			resetJoeInv();
		}
		if(amb.onClick("opLimpaBaus")){
			for(Bau b:bau)
				b.zBau();
		}	
		if(amb.onClick("resetBars"))	resetJoeBars();
		if(amb.onClick("reloadMap"))	tab = new Tab();
		if(amb.onClick("opAltAgua")) {
			amb.setText("opAltAgua",""+((int)yJoe));
		}
		
		if(amb.onEscPress()) amb.acClick("cancel");
		if(amb.onEnterPress("opDensArv")) amb.acClick("ok");
		if(amb.onEnterPress("opAltAgua")) amb.acClick("ok");
		
		if(amb.onClick("cancel")) closeMenu(); // soh fecha sem alterar
		if(amb.onClick("ok")) { // altera e fecha
			J.opDelayCfg=true; // distante, mas deve funcionar					
			opFiltroInv = amb.isChecked("opFiltroInv"); J.escCfg("opFiltroInv",opFiltroInv,camCfg);
			opSomJetPack = amb.isChecked("opSomJetPack"); J.escCfg("opSomJetPack",opSomJetPack,camCfg);
			opJetPackInfinito = amb.isChecked("opJetPackInfinito"); J.escCfg("opJetPackInfinito",opJetPackInfinito,camCfg);
			opSemChuva = amb.isChecked("opSemChuva"); J.escCfg("opSemChuva",opSemChuva,camCfg);
			opImpNorAlt = amb.isChecked("opImpNorAlt"); J.escCfg("opImpNorAlt",opImpNorAlt,camCfg);
			opImpCmaAlt = amb.isChecked("opImpCmaAlt"); J.escCfg("opImpCmaAlt",opImpCmaAlt,camCfg);
			opEcoForn = amb.isChecked("opEcoForn");J.escCfg("opEcoForn",opEcoForn,camCfg);
			opEcoMaq = amb.isChecked("opEcoMaq");J.escCfg("opEcoMaq",opEcoMaq,camCfg);
			opEcoMobs = amb.isChecked("opEcoMobs");J.escCfg("opEcoMobs",opEcoMobs,camCfg);
			opImpPals = amb.isChecked("opImpPals");J.escCfg("opImpPals",opImpPals,camCfg);
			opAniTrigo = amb.isChecked("opAniTrigo");J.escCfg("opAniTrigo",opAniTrigo,camCfg);
			opImpPalPadrao = amb.isChecked("opImpPalPadrao");J.escCfg("opImpPalPadrao",opImpPalPadrao,camCfg);
			//opMouseMedia = amb.isChecked("opMouseMedia");J.escCfg("opMouseMedia",opMouseMedia,camCfg);
			opInfoDebug = amb.isChecked("opInfoDebug");J.escCfg("opInfoDebug",opInfoDebug,camCfg);
			opGerarMobs = amb.isChecked("opGerarMobs");J.escCfg("opGerarMobs",opGerarMobs,camCfg);
			opGastaItem = amb.isChecked("opGastaItem");J.escCfg("opGastaItem",opGastaItem,camCfg);
			opSaveInv = amb.isChecked("opSaveInv"); J.escCfg("opSaveInv",opSaveInv,camCfg);
			opModoFaquir = amb.isChecked("opModoFaquir"); J.escCfg("opModoFaquir",opModoFaquir,camCfg);
			opSaveArea = amb.isChecked("opSaveArea"); J.escCfg("opSaveArea",opSaveArea,camCfg);
			opSempreMeioDia = amb.isChecked("opSempreMeioDia"); J.escCfg("opSempreMeioDia",opSempreMeioDia,camCfg);
				if(!opSempreMeioDia) opPausaHora=false;

			opRespDebaixoDagua = amb.isChecked("opRespDebaixoDagua"); J.escCfg("opRespDebaixoDagua",opRespDebaixoDagua,camCfg);
			opCanBounceFaces = amb.isChecked("opCanBounceFaces"); J.escCfg("opCanBounceFaces",opCanBounceFaces,camCfg);
			
			opAltAgua = J.stEmInt(amb.getText("opAltAgua")); J.escCfg("opAltAgua",opAltAgua,camCfg);
			opDensArv = J.stEmInt(amb.getText("opDensArv"));J.escCfg("opDensArv",opDensArv,camCfg);

			
			// um ultimo detalhe
			if(opCanBounceFaces) tCub.get(tAgua).setIncCal(0.2f);
			else tCub.get(tAgua).setIncCal(0f);			
			
			closeMenu();
		}
	}
	public void closeMenu(){		
		amb.close("winOp");		//amb.clear();
		menuAberto=false;		
		prendeMouse();
	}				
	
		boolean opSoltaMouse=false;
	public void soltaMouse(){
		opSoltaMouse=true;
		ms.show();
	}
	public void prendeMouse(){
		opSoltaMouse=false;				
		ms.hide();		
	}	

// === INVENTARIO, p qq mob ===================

	public int filtroAddItm(int t){ // filtros
		if(!opFiltroInv) return t;
/* atrapalhou a colher de pedreiro
		if(!J.vou(t,
			getInd("DEGRAU_ADOBE_UMIDO"),
			getInd("DEGRAU_ADOBE_SECO"),
			getInd("DEGRAU_ADOBE")))
				t = semDegrau(t);
*/				
				
		if(t==getInd("caule_seringueira_cheia")) t = getInd("caule_seringueira");
		if(t==getInd("caule_seringueira_vazia")) t = getInd("caule_seringueira");
		// ajustando dificuldade		
		if(t==getInd("folha_seringueira")) t = getInd("folha"); // menos muda p borracha 
		if(t==getInd("caule_sequoia")) t = getInd("caule"); // menos muda p madeira
		
		if(t==getInd("folha_maca")) t = getInd("folha"); 
		if(t==getInd("folha_maca_verde")) t = getInd("folha"); 
		if(t==getInd("folha_macieira")) t = getInd("folha");
		if(t==getInd("caule_macieira")) t = getInd("caule");
		
		if(t==getInd("cacto_topo")) t = getInd("cacto_base"); // melhor assim pq dah p plantar 
		if(t==getInd("mato_crescendo")) t = getInd("mato"); // ?isso eh bom???
		
		if(t==getInd("flecha_cma")) t = getInd("flecha");
		if(t==getInd("flecha_bxo")) t = getInd("flecha");
		if(t==getInd("flecha_nor")) t = getInd("flecha");
		if(t==getInd("flecha_sul")) t = getInd("flecha");
		if(t==getInd("flecha_les")) t = getInd("flecha");
		if(t==getInd("flecha_oes")) t = getInd("flecha");
		
		if(t==getInd("cana_madura")) t = getInd("talo_cana");
		if(t==getInd("bambu_maduro")) t = getInd("varinha_bambu");
		
		if(t==getInd("cenoura_madura")) t = tCenoura;
		if(t==getInd("beterraba_madura")) t = tBeterraba;
		if(t==getInd("batata_madura")) t = tBatata;
		if(t==getInd("terra_arada")) t = tTerra;
		if(t==getInd("terra_arada_regada")) t = tTerra;
		
		if(t==getInd("degrau_adobe_seco")) t = getInd("degrau_adobe");
		if(getCubMod(t).ehCond) t = tCond0;

		// outros depois
		return t;
	}

		Inv joeInv = new Inv(); // do joe
		boolean opAddItemNoFinal=false; // auto reset
		final int 
			maxInv=maxXbau*2;

	public class Inv{
			Itm itm[] = null;
			int sel = 3, selI=-1; // selecao interna
			
		public Inv(){
			zInv();
			rnd();
		}	
		public void rnd(){
			for(int q=0; q<maxInv; q++)
				itm[q] = new Itm(J.R(200)*J.R(2),J.R(tCub.size()));
		}
		public void zItm(){
			itm[sel] = new Itm(0,0);
		}
		public void zInv(){
			Itm w[] = new Itm[maxInv];
			for(int q=0; q<maxInv; q++)
				w[q] = new Itm(0,0);
			itm = w;			
		}
		public int contItm(int t){
			int c=0;
			for(int w=0; w<maxInv; w++)
				if(itm[w].tip==t) c+=itm[w].qtd;
			return c;
		}
		public void saveOpenInv(boolean s){
			String camInv = "maps//joeInv.bin";
			if(!s)
				if(!J.arqExist(camInv)){
					J.esc("arquivo de inventario nao foi encontrado");
					//zInv();
					return;
				}
			RandomAccessFile arq = null;
			try{
				arq = new RandomAccessFile(camInv,"rw");
				
				if(s) arq.writeInt(sel);
				else sel = (int)arq.readInt();
				
				for(int q=0; q<maxInv; q++)
					if(s){
						arq.writeInt(itm[q].qtd);
						arq.writeInt(itm[q].tip);
					} else {
						itm[q] = new 
							Itm(
								(int)arq.readInt(),
								(int)arq.readInt());
					}
				arq.close();
				J.esc("ok:  inventario "+(s?"salvo":"aberto")+": "+camInv);
			}catch(IOException e){
				J.impErr("falha "+(s?"salvando":"abrindo")+" o inventario: "+camInv,"Inv.saveOpen()");
				e.printStackTrace();
				try{arq.close(); }catch(IOException ee){};				
			}
		}
		public void imp(int i, int j){
			//if(!opSoltaMouse)
			if(ms.y<j-30)	
				esc(i+sel*(larItm+1),j-7,tCub.get(itm[sel].tip).name);
			for(int q=0; q<maxInv; q++){
				if(
					itm[q].imp(i+q*(larItm+1),j, (sel==q))
					) 
					if(opSoltaMouse){
						sel=q;
						stMouse = getName(itm[q].tip);
						if(ms.b1)	
						if(ms.cClick>12){
							
							if(itmArr==null){
								
								// direto p bau
								if(shiftOn)
								if(bauAberto!=-1)
								if(bau.get(bauAberto).insBau(itm[q].qtd,itm[q].tip))	 // nao existe insercao parcial
									itm[q] = new Itm(0,0);
								
							
								// direto p slot carne
								if(tCub.get(itm[q].tip).stCozido!=null)
								if(fornAberta!=-1)	
								if(forn.get(fornAberta).car.tip==0){
									forn.get(fornAberta).car=itm[q];
									itm[q]= new Itm(0,0);
									return;
								}
								
								// direto p slot lenha
								if(tCub.get(itm[q].tip).potCalor>0)
								if(fornAberta!=-1)	
								if(forn.get(fornAberta).len.tip==0){
									forn.get(fornAberta).len=itm[q];
									itm[q] = new Itm(0,0);
									return;
								}								
				
								if(itm[q].tip==0) return; // esse nao pode
								
								itmArr = itm[q];
								itm[q]=new Itm(0,0);
							} else {
								
								if(itm[q].tip!=0) return;
								
								itm[q] = itmArr;
								itmArr=null;
							}
							
						}						
					}
			}
		}
		public boolean decSel(){
			if(itm[sel].qtd>0){
				itm[sel].qtd--;
				if(itm[sel].qtd<=0) select("");					
				return true;
			}	
			return false;
		}
		public void selProx(){
			select(sel+1);
		}
		public void selAnt(){
			select(sel-1);
		}		
		public void selectRel(int i){
			select(sel+i);
		}
		public boolean select(String st){
			if(st=="") st="nulo";
			int v = getInd(st);
			for(int q=0; q<maxInv; q++)
				if(itm[q].tip==v){
					select(q);
					return true;
				}
			return false;	
		}
		public void select(int i){
			if(i<0) i=0;
			if(i>maxInv-1) i=maxInv-1;
			sel = i;			
			if(joeInv==this){ // nao sei se esse tipo de programacao eh uma boa pratica, mas funciona
				maoDir=null;
				maoEsq=null;
				cCarrMao = tmpCarrMao; 
				// dicas. dica.
				if(tCub.get(itm[sel].tip).ehComivel) impSts("DICA: para comer isto, clique com o bt esquerdo com control pressionado");
				if(tCub.get(itm[sel].tip).ehBebivel) impSts("DICA: para beber isto, clique com o bt esquerdo com control pressionado");
				if(itm[sel].tip==tSeedPreencher) impSts("DICA: segure CONTROL para alastrar, depois plote algo SOBRE o cubo escuro para substituir");
				if(itm[sel].tip==tSeedCirc) impSts("DICA: use para plotar um circulo no plano horizontal usando o cubo logo abaixo");
				if(tCub.get(itm[sel].tip).opEhMaqInt) impSts("DICA: para abrir a interface desta maquina plotada, clique com o bt esquerdo pressionando CONTROL");
				
				if(itm[sel].tip==getInd("colher_pedreiro")) impSts("DICA: use sobre terra margeada com agua para criar lama, que eh materia prima para ADOBE");
				if(itm[sel].tip==getInd("lama")) impSts("DICA: clique com bt esquedo sobre uma area aberta usando lama para criar um ADOBE UMIDO. Espere secar para remove-lo. Precisa ser lugar arejado. ");



				


		//String stHintAdobe="COMO USAR:|1- plote o DEGRAU_ADOBE_UMIDO p secar em area aberta|2- remova o DEGRAU_ADOBE_SECO com a mao| 3- plote o ADOBE_SECO no chao ou sobre outro ADOBE_SECO|";
				
//!CTRL+cliqueEsq
//@comer/beber o item do inventario.
				
			}			
		}
		public boolean addItm(String st){
			return addItm(getInd(st));
		}
		public boolean addItm(int t){
			return addItm(1,t);
		}
		public boolean addItmS(int q, int t){
			// adiciona e jah seleciona
			boolean b = addItm(q,t);
			select(tCub.get(t).name);
			return b;
		}
		public boolean addItm(int q, String t){
			return addItm(q,getInd(t));
		}
		public boolean addItm(int q, int t){
			// centralizar aqui
			t = filtroAddItm(t);
			
			// procurar repetido
			if(!J.vou(t, // nao blessing, nao blesser, tag.
				getInd("machado"),
				getInd("picareta"),
				getInd("pah"),
				getInd("foice"),
				getInd("varinha"),
				getInd("cinzel"),
				getInd("arco"))) // mais algum???	
			if(tem(t)){	
				itm[selI].qtd+=q; 
				itm[selI].tip = t;
//				sel = selI; // isso nao ajuda na extracao
				return true;
			}
				
			// se nao tem repetido, entao procurar slot vazio
			if(tem(0)) {
				itm[selI] =  new Itm(q,t); 
//				sel = selI;				
				return true;
			}
			return false;
		}
		public boolean decItm(){
			if(itm[sel].qtd<=0) return false;
			itm[sel].qtd--;
			
			// padronizar assim:
			if(itm[sel].qtd<=0) select("");					
			return true;
		}
		public boolean decItm(int q, String t){
			return decItm(q, getInd(t));
		}
		public boolean decItm(int q, int t){
			// soh retornarah true se puder remover toda a quantidade informada do item em questao
			// nao funcionarah direito se o mesmo item tiver a soma, mas estiver em slots diferentes.
			// imperfeito mas jah quebra o galho
			if(tem(q,t)){
				for(int w=0; w<maxInv; w++)
					if(itm[w].tip==t)
					if(itm[w].qtd>=q){
						itm[w].qtd-=q;
						if(itm[w].qtd<=0){
							itm[w].qtd=0;
							itm[w].tip=0;
							itm[w].fig=null;
							return true;							
						}
					}
				return true;
			}
			return false; // veja q nao alterou o inventario aqui.			
		}


		public boolean temEspacoNoInv(int q, int t){
			// se tem slot 0 
			// ou se tem slot t com qtd <999
			for(int m=0; m<maxInv; m++){
				if(itm[m].tip==0)
					return true;
				if(itm[m].tip==t)
						return true;
			}
			return false;
		}
		public boolean tem(int q, String st){
			return tem(q, getInd(st));
		}
		public boolean tem(int q, int t){
			// nao funcionarah direito se o mesmo item estiver em 2 slots diferentes
			for(int i=0; i<maxInv; i++)
				if(itm[i].tip==t)
				if(itm[i].qtd>=q)
					return true;
			return false;
		}
		public boolean tem(int t){
			if(opAddItemNoFinal){
				for(int q=maxInv-1; q>=0; q--)
				if(itm[q].tip==t){
					selI=q;
					return true;
				}
				opAddItemNoFinal=false;
			} else {
				for(int q=0; q<maxInv; q++)
				if(itm[q].tip==t){
					selI=q;
					return true;
				}
			}
			return false;	
		}
		public int naMao(){
			return itm[sel].tip;
		}
		public int qtdNaMao(){
			return itm[sel].qtd;
		}		
	}
	
// =========================================

	public void trocaTudo(){
		trocaTudo(
			getCub(xaHit,yaHit,zaHit,xtHit,ytHit,ztHit),
			joeInv.naMao()
			);
	}
	public void trocaTudo(String va, String vn){
		trocaTudo(getInd(va), getInd(vn));
	}
	public int trocaTudo(int va, int vn){
		// retorna quantos foram trocados. Usei em redes eletricas.
		int c = 0;
		if(tab!=null)		
		for(int m=0; m<tamTab; m++)
		for(int n=0; n<altTab; n++)
		for(int o=0; o<tamTab; o++)
			for(int mm=0; mm<10; mm++)
			for(int nn=0; nn<10; nn++)
			for(int oo=0; oo<10; oo++)
				if(getCub(mm,nn,oo,m,n,o)==va){
					c++;
					setCub(vn,mm,nn,oo,m,n,o);
				}
		return c;		
	}
	public void troca(){
		if(stHit==null) return;
		int a = getInd(stHit);
		a = comCai(a);
		int nn = joeInv.naMao();
		int q = opNumMultPlot;
		for(int m=-q; m<+q; m++)
		for(int n=-q; n<+q; n++)
		for(int o=-q; o<+q; o++)
			if(a==getCub(xaHit+m,yaHit+n,zaHit+o, xtHit,ytHit,ztHit))
				setCub(nn,xaHit+m,yaHit+n,zaHit+o, xtHit,ytHit,ztHit);
	}
	public void trocaRai(int va, int vn, int r, int xa, int ya, int za, int xt, int yt, int zt){
		// troca cubos va por vn no raio informado
		for(int m=-r; m<=+r; m++)
		for(int n=-r; n<=+r; n++)
		for(int o=-r; o<=+r; o++)
			if(va==getCub(xa+m,ya+n,za+o, xt,yt,zt))
				setCub(vn, xa+m,ya+n,za+o, xt,yt,zt);
	}
	public boolean rodarCubo(){		
		if(winAberta) return false;	
		String st = null;
		if(st==null) if(J.iguais(stHit,"vator_nor")) st = "vator_les";
		if(st==null) if(J.iguais(stHit,"vator_les")) st = "vator_sul";
		if(st==null) if(J.iguais(stHit,"vator_sul")) st = "vator_oes";
		if(st==null) if(J.iguais(stHit,"vator_oes")) st = "vator_cma";
		if(st==null) if(J.iguais(stHit,"vator_cma")) st = "vator_bxo";
		if(st==null) if(J.iguais(stHit,"vator_bxo")) st = "vator_nor";

		if(st==null) if(J.iguais(stHit,"esteira_nor")) st = "esteira_les";
		if(st==null) if(J.iguais(stHit,"esteira_les")) st = "esteira_sul";
		if(st==null) if(J.iguais(stHit,"esteira_sul")) st = "esteira_oes";
		if(st==null) if(J.iguais(stHit,"esteira_oes")) st = "esteira_nor";

		if(st==null) if(J.iguais(stHit,"telhado_nor")) st = "telhado_les";
		if(st==null) if(J.iguais(stHit,"telhado_les")) st = "telhado_sul";
		if(st==null) if(J.iguais(stHit,"telhado_sul")) st = "telhado_oes";
		if(st==null) if(J.iguais(stHit,"telhado_oes")) st = "telhado";
		if(st==null) if(J.iguais(stHit,"telhado")) st = "telhado_nor";
		
		if(st==null) if(J.iguais(stHit,"prensa_les")) st = "prensa_sul";
		if(st==null) if(J.iguais(stHit,"prensa_sul")) st = "prensa_oes";
		if(st==null) if(J.iguais(stHit,"prensa_oes")) st = "prensa_nor";
		if(st==null) if(J.iguais(stHit,"prensa_nor")) st = "prensa_les";

		
		if(st!=null){
			setCub(st,xaHit,yaHit,zaHit,xtHit,ytHit,ztHit);		
			return true;
		}
		return false;		
	}
	public boolean puxarEmpurrar(boolean p){ // empurrarPuxar
		if(winAberta) return false;
		if(menuAberto) return false;
		if(fornAberta!=-1) return false;
		if(bauAberto!=-1) return false;
		

		
		int d = dHit;
		if(!p) d = invDir(d);
		switch(d){
			case NOR: if(moveSV(d, xaHit,yaHit,zaHit,xtHit,ytHit,ztHit)) return true; else return false;
			case SUL: if(moveSV(d, xaHit,yaHit,zaHit,xtHit,ytHit,ztHit)) return true; else return false;
			case LES: if(moveSV(d, xaHit,yaHit,zaHit,xtHit,ytHit,ztHit)) return true; else return false;
			case OES: if(moveSV(d, xaHit,yaHit,zaHit,xtHit,ytHit,ztHit)) return true; else return false;
			case CMA: if(moveSV(d, xaHit,yaHit,zaHit,xtHit,ytHit,ztHit)) return true; else return false;
			case BXO: if(moveSV(d, xaHit,yaHit,zaHit,xtHit,ytHit,ztHit)) return true; else return false;
		}
		return false;
	}

	public void setRealParalel(boolean par){
// 	x	horizonte
// 	x	wAmb
// 	x	opRealParal;
// 	x	efeito clarao
//	x	som de travessia
// 	x	tontura leve, como de amarantos
// 	x	inverter buffer de impressao
//		inverter mouse

	

		opRealParal=par;
		iniOculos(J.cor[J.rndCr9()],16);		
		wRealParalelIn.play();
		if(par){
			wAmb = new JWav("ambiente20.wav");
			wAmb.loop();						
			J.opInvHorBuf = true;
			//ms.opInvHor=true; // talvez algum des-poder temporario altere isso
		} else {		
			J.opInvHorBuf = false;
			//ms.opInvHor=false;
		}
	}		

			final int INTNULO=-32000000;		
			int xaCtt=INTNULO, yaCtt=0, zaCtt=0, xxaCtt=0, yyaCtt=0, zzaCtt=0,
					xtCtt=0, ytCtt=0, ztCtt=0;
			String camCtx = "c://java//maps//ctx//";
			String camUltCtxEsc = camCtx+"padrao.ctx";
	public void saveCtxLandMark(String cam){
		// lembrete: 
		//    - tCubLandMark deduz os limites da area, é cubo plotavel, sempre em dupla.
		//    - tCaubLandMarkArea preenche as areas vazias apenas como indicacao visual		
		if(xaCtt==INTNULO) {
			J.impErr("impossivel salvar area. Coordenadas xaCtt nulas","saveCtxLandMark() ["+cam+"]");
			amb.confirm("impossivel salvar area._Coordenadas xaCtt nulas_saveCtxLandMark() ["+cam+"]","K");
			return;
		}
		RandomAccessFile arq = null;
		if(!J.tem('.',cam)) cam = cam+".ctx"; // nao ".ctt", nem ".ctj"		

		if(!J.arqExist(cam)) J.criaArqVazio(cam);						
			
		try{
			arq = new RandomAccessFile(cam, "rw");
			
			arq.writeInt(xxaCtt-xaCtt); // primeiro o tamanho da tabela
			arq.writeInt(yyaCtt-yaCtt); // coords já foram ordenadas no outro metodo
			arq.writeInt(zzaCtt-zaCtt);
			
			int v = 0, vi=0;
			for(int m=xaCtt; m<=xxaCtt; m++)
			for(int n=yaCtt; n<=yyaCtt; n++)
			for(int o=zaCtt; o<=zzaCtt; o++){
				v = getCub(m,n,o, xtCtt, ytCtt, ztCtt);
				vi = getInf(m,n,o, xtCtt, ytCtt, ztCtt); // sempre salva byte extra, diferente dos arquivos de area.
				
				if(v==tLandMark) v=0;
				if(v==tLandMarkArea) v=0;
				if(v==tAgua) v=0; // mais seguro assim
				if(getCubMod(v).ehCond) v = tCond0;
				
				arq.writeInt(v);
				arq.writeInt(vi);				
			}			
			arq.close();
			J.esc("save ctt ok: ["+cam+"]");
		} catch(IOException e){
			try{ arq.close(); }catch(IOException ee){}
			e.printStackTrace();
			J.impErr("!falha salvando ["+cam+"]","saveCtxLandMark()");
		}
	}
	public void openCtxLandMark(String cam, int xa,int ya,int za, int xt, int yt, int zt){
		// esta NAO usa landMark (= cubo tLandMark, q indica coordenadas xaCtt, xxaCtt, xtCtt e similares)
		// deixei este nome no metodo pq são metodos "casados". Apenas o save usa landMark
		RandomAccessFile arq = null;
		if(!J.tem('.',cam)) cam = cam+".ctx"; // nao ".ctt", nem ".ctj"		
		if(!J.tem(':',cam)) cam = camCtx+cam;

		try{
			arq = new RandomAccessFile(cam, "rw");
			
			int lar = arq.readInt(); // primeiro o tamanho da tabela
			int alt = arq.readInt(); // coords já foram ordenadas no outro metodo
			int pro = arq.readInt();
			
			int v = 0, vi=0;
			for(int m=0; m<=lar; m++)
			for(int n=0; n<=alt; n++)
			for(int o=0; o<=pro; o++){
				
				if(n==0) setCub(tSeedDesceCol, xa+m-(lar>>1), ya-1, za+o-(pro>>1), xt,yt,zt);
				
				v = arq.readInt();
				vi = arq.readInt();
				// ?algum filtro aqui??? landMark já foi filtrado no save.
				setCubSV(v,vi, xa+m-(lar>>1),ya+n,za+o-(pro>>1), xt,yt,zt); // lembre q pode SIM extrapolar coordenadas de area, o sistema ajusta.
			}			
			arq.close();
			J.esc("open ctt ok: ["+cam+"]");
		} catch(IOException e){
			try{ arq.close(); }catch(IOException ee){}
			e.printStackTrace();
			J.impErr("!falha abrindo ["+cam+"]","openCtxLandMark()");
		}		
	}
	public void autoSelFerr(){
		int v = getCub(xaHit,yaHit,zaHit,xtHit,ytHit,ztHit);
		Cub c = tCub.get(v);
		String st = null;
		
		if(c.extComMao) st = "";
		if(c.extComPah) st = "pah";
		if(c.extComFoice) st = "foice";
		if(c.extComPic) st = "picareta";
		if(c.extComMach) st = "machado";
		if(c.extComChBoca) st = "chave_boca";
		if(c.extComChFenda) st = "chave_fenda";
		if(st!=null)
		if(joeInv.select(st))
			wGet.play();
		
	}
	public void setTravaDirPlot(int d){
		opTravaDirPlot=d;
		if(d==0) {
				wOff.play();				
				impSts("trava de direcao desligada.");							
		} else {
				wOn.play();
				impSts("trava de direcao ligada: "+stDir(d));			
		}
	}
	
		boolean 
			opEliminarMobs=false;
		
		boolean 
			ctrlOn=false, altOn=false, 
			shiftOn=false, capsOn=false,
			apostOn=false, enterOn=false,
			delOn=false, spaceOn=false,
			cmaOn=false, bxoOn=false, 
			esqOn=false, dirOn=false;	
		int cSpace=0;                                                             // indica a quanto tempo a tecla space foi pressionada. Usei p  j o e   a p e a r	 de  m o n t a r i a
	public void keyTyped(KeyEvent k){	}
	public void keyPressed(KeyEvent k){
		if(winAberta || menuAberto
		|| amb.temComp("confirm")
		|| amb.temComp("winSaveCtx")
		|| amb.temComp("winOpenCtx")
			) {
					amb.reg(k);
					return;
				}

		if(k.isControlDown()) ctrlOn=true;
		if(k.isAltDown()) altOn=true;
		if(k.isShiftDown()) shiftOn=true;
		
		J.regPress(k);
		if(winAberta) return;
		
		
		//if (k.getKeyCode()=='<') iniBioma();

		
		if (k.getKeyCode()=='[') opLuzGeral = J.corrInt(--opLuzGeral,-lmLuzGeral,+lmLuzGeral);
		if (k.getKeyCode()==']') opLuzGeral = J.corrInt(++opLuzGeral,-lmLuzGeral,+lmLuzGeral);
		


		
		
		if (k.getKeyCode()==k.VK_X)	if(cPlot<=0){
			setCub(0, 0, xaHit, yaHit, zaHit, xtHit, ytHit, ztHit);
			cPlot = 6;
		}
		
		
	  if (k.getKeyCode()==k.VK_ESCAPE){ // kkkkkkkkkkkk
			if(opTravaDirPlot!=0)setTravaDirPlot(0);
			if(opPause) setPause(false);
			if(menuAberto) amb.acClick("cancel"); // p menu. Tomara q nao de problema com outra win		
			fechaBanc();
			fechaForn();
			fechaMaq();
			fechaBau();
			impSts("pressione SHIFT+ESC para sair");
			if(shiftOn){
				//joeInv.saveOpenInv(SAVE); // jah tah embutido no metodo abaixo
				saveStateJoe();
				saveOpenForns(SAVE);
				saveOpenBaus(SAVE);
				saveOpenMaqs(SAVE);
				J.sai();
			}
		}
		
		if (k.getKeyCode()==92){ // "\", barra descendo
			autoSelFerr();
		}
		if (k.getKeyCode()==222){ // apostrofo
			if(opPause) setPause(false);
			if(opTravaDirPlot!=0)setTravaDirPlot(0);
			if(fornAberta!=-1) fechaForn();
			if(maqAberta!=-1) fechaMaq();
			if(bauAberto!=-1) fechaBau();		
			else if(opTemBau0) abreBau(0);			
			if(shiftOn) abreMaq(0,0); // mais p debug. Tirar depois.
			apostOn=true;
		}			

		if (k.getKeyCode()==106) opPausaHora=!opPausaHora; // "*" no numerico
		if (k.getKeyCode()==109) opMinuto = J.corrInt(--opMinuto,0,59); // menos no numerico
		if (k.getKeyCode()==107) opMinuto = J.corrInt(++opMinuto,0,59); // mais no numerico
		
		if (k.getKeyCode()==k.VK_T){
			wPortalIn.play();
			if(ctrlOn) trocaTudo();
			else troca();
		}

		if (k.getKeyCode()==k.VK_1) setRealParalel(!opRealParal);
		
		if (k.getKeyCode()==k.VK_6){
			if(tab.are[xtHit][ytHit][ztHit]!=null){
				tab.are[xtHit][ytHit][ztHit].smoothArea();
				//tab.are[xtHit][ytHit][ztHit].cSmoothArea=tmpSmoothArea;
				tab.are[xtHit][ytHit][ztHit].e=1;
			}
		}					
		
		if (k.getKeyCode()==k.VK_F2){
			soltaMouse();
			if(xaCtt==INTNULO) amb.confirm("Defina a area com landMark primeiramente_antes de salvar uma construcao ctx","K");
			else amb.winSaveOpen(camCtx,"padrao.ctx","ctx",null).setName("winSaveCtx").setText("salvando ctx");
		}
		if (k.getKeyCode()==k.VK_F3){
			soltaMouse();
			xaCtt = xaHit;			yaCtt = yaHit;			zaCtt = zaHit;
			xtCtt = xtHit;			ytCtt = ytHit;			ztCtt = ztHit;
			if(dHit==J.CMA) yaCtt++;
			amb.winSaveOpen(camCtx,"padrao.ctx","ctx",null).setName("winOpenCtx").setText("abrindo ctx");
		}
		
		if (k.getKeyCode()==k.VK_F4) opJetPackInfinito = !opJetPackInfinito;
		if (k.getKeyCode()==k.VK_F5) opInfoDebug = !opInfoDebug;

			
		if (k.getKeyCode()==k.VK_F1){ // panic key
			if(shiftOn){
				wPortalIn.play();
				trocaTudo(tAgua,0);
				trocaTudo(tAguaCorr,0);			
				trocaTudo(tAguaCel,0);
				trocaTudo(tAguaCelCorr,0);						
				trocaTudo(tPetroleo,0);						
				trocaTudo(tPetroleoCorr,0);						
				trocaTudo(tAcido,0);						
				trocaTudo(tAcidoCorr,0);						
				trocaTudo(tLava,0);
				trocaTudo(tLavaCorr,0);						
			} else {
				opEliminarMobs=true;
				for(int q=mob0; q<mob9; q++)
					tCub.get(q).ativo = false;
				wGet.play();
			}
		}

		if (k.getKeyCode()==k.VK_F6) plotPortal(-1, xaHit,yaHit,zaHit,xtHit,ytHit,ztHit);

		if (k.getKeyCode()==k.VK_BACK_SPACE) setPause(!opPause);
		if(k.getKeyCode()==k.VK_F8) { // pausa a gravacao do YTC e o jogo
			setPause(true);
		}				
		if(k.getKeyCode()==k.VK_F7) { // inicia a gravacao do YTC e roda o jogo
			setPause(false);		
		}		

		


		if (k.getKeyCode()==k.VK_F9)	{
			opImpTab2d++; // autocorrecao lah p cima
		}
		
		if (k.getKeyCode()==k.VK_F12){ // deletando arq "are" apontado (usa xw:yw:zw). Bom p resetar areas defeituosas.
			tab.delAreHit(xtHit,ytHit,ztHit);

		}
		
		if (k.getKeyCode()==k.VK_CAPS_LOCK)	if(opTemJetPack){			
			if(cPlot<=0) setJetPack('a');
			cPlot=6;
			capsOn= !capsOn; // esse eh diferente		
		}
		
		if (k.getKeyCode()==k.VK_HOME) joeInv.selAnt();
		if (k.getKeyCode()==k.VK_END) joeInv.selProx();
		
		if (k.getKeyCode()==k.VK_DELETE) delOn=true;
		if (k.getKeyCode()==k.VK_DELETE) {
			if(shiftOn){
				joeInv.zItm();
				joeInv.selectRel(0);
			}
		}


		if (k.getKeyCode()==k.VK_Y) setCub("seed_exp",xaHit,yaHit,zaHit,xtHit,ytHit,ztHit);
		if (k.getKeyCode()=='='){
			if(shiftOn) joeInv.addItm(1,joeInv.naMao()); // bom p debug
			else {	
				incInf(xaHit,yaHit,zaHit,xtHit,ytHit,ztHit);
				incLife(xaHit,yaHit,zaHit,xtHit,ytHit,ztHit);				
			}
		}
		if (k.getKeyCode()=='-'){
			if(shiftOn) joeInv.decItm(1,joeInv.naMao()); // bom p debug
			else {	
				decInf(xaHit,yaHit,zaHit,xtHit,ytHit,ztHit);
				decLife(xaHit,yaHit,zaHit,xtHit,ytHit,ztHit);				
			}
		}
		
		
		
	  if (k.getKeyCode()==k.VK_NUMPAD0){
			if(opTravaDirPlot==0) 	setTravaDirPlot(dHit);
			else 	setTravaDirPlot(0);		
		}
		
	  if (k.getKeyCode()==k.VK_F) openMenu();
	  if (k.getKeyCode()==k.VK_F) if(shiftOn){
			// GERANDO ARQUIVO DE CONSULTA DE BLOCOS
			// defina o criterio abaixo, dentro do loop
			ArrayList<String> st = new ArrayList<>();
			for(Cub c:tCub)
			if(c.ehConEl)	
				st.add(c.name);
			String cam = "c://java//'joeCraft-info.txt";
			st.add(0,"=== NUMERO DE BLOCOS: "+st.size());
			J.saveStrings(st, cam);
			J.sai("lista salva em "+cam+"  (OLHA O APO'STROFO!)");			
		}
			
		//if (k.getKeyCode()==k.V K _ B A C K _ S P A  CE) mobScp.proxLin();
		
		if (k.getKeyCode()==k.VK_SPACE){
			spaceOn=true;		
			if(cSpace>2)
			if(cSpace<10)
			if(joeMont!=-1)
				joeApear();
			cSpace=1;
		}
		if (k.getKeyCode()==k.VK_ENTER) enterOn=true;
		if (k.getKeyCode()==k.VK_ENTER){
			if(!winAberta)
			if(!bancAberta)
			if(fornAberta==-1)
			if(bauAberto==-1)
			if(!amb.temComp("confirm"))
			if(!amb.temComp("winSaveCtx"))
			if(!amb.temComp("winOpenCtx")) // :)
			if(!menuAberto){
				soltaMouse();
				J.opEmMaiusc=false;
				amb.openWin("joeCraft01.win").setName("win");
				if(J.arqExist(camCom))
					amb.getComp("lstCom").setList(J.openStrings(camCom));
				amb.setFocus("txtCom");
				winAberta=true;
			}
		}
		
		if (k.getKeyCode()==k.VK_W) {cmaOn=true; if(opPause) setPause(false); }
		if (k.getKeyCode()==k.VK_S) bxoOn=true;
		if (k.getKeyCode()==k.VK_A) esqOn=true;
		if (k.getKeyCode()==k.VK_D) dirOn=true;
		
		if (k.getKeyCode()==k.VK_Q) {joeInv.selAnt();			opVarinhaOn=false;}
		if (k.getKeyCode()==k.VK_E) {joeInv.selProx();			opVarinhaOn=false;}
		
		if (k.getKeyCode()==k.VK_UP) cmaOn=true;
		if (k.getKeyCode()==k.VK_DOWN) bxoOn=true;
		if (k.getKeyCode()==k.VK_LEFT) esqOn=true;
		if (k.getKeyCode()==k.VK_RIGHT) dirOn=true;
		
		if (k.getKeyCode()==k.VK_PAGE_UP) { if(!opJetPackOn) setJetPack('+'); yJoe+=0.25f; }
		if (k.getKeyCode()==k.VK_PAGE_DOWN) { if(!opJetPackOn) setJetPack('+'); yJoe-=0.25f; }

		
	}
	public void keyReleased(KeyEvent k){
		// if(k.isControlDown()) ctrlOn=false;
		// if(k.isAltDown()) altOn=false;
		// if(k.isShiftDown()) shiftOn=false;	

		ctrlOn=false;
		shiftOn=false;
		altOn=false;		
		apostOn=false;
		
		J.regRelease(k);
		
		
		if (k.getKeyCode()==k.VK_INSERT) {
			joeInv.addItm(12, getInd(stHit));
			joeInv.select(stHit);
		}
		if (k.getKeyCode()==k.VK_DELETE) delOn=false;
			
		
		if (k.getKeyCode()==k.VK_G) { opGrowFast = !opGrowFast; impSts("opGrowFast: "+opGrowFast);}
		if (k.getKeyCode()==k.VK_R) { 
			if(fornAberta==-1)
			if(bauAberto==-1)
			if(!menuAberto)		
			if(!winAberta)
			if(cPlot<=0){
				cPlot=12;
				rodarCubo();
				wPulo.play();		
			}
		}
		
		if (k.getKeyCode()==k.VK_SPACE) spaceOn=false;
		if (k.getKeyCode()==k.VK_ENTER) enterOn=false;
		if (k.getKeyCode()==k.VK_W) cmaOn=false;
		if (k.getKeyCode()==k.VK_S) bxoOn=false;
		if (k.getKeyCode()==k.VK_A) esqOn=false;
		if (k.getKeyCode()==k.VK_D) dirOn=false;
		
		if (k.getKeyCode()==k.VK_UP) cmaOn=false;
		if (k.getKeyCode()==k.VK_DOWN) bxoOn=false;
		if (k.getKeyCode()==k.VK_LEFT) esqOn=false;
		if (k.getKeyCode()==k.VK_RIGHT) dirOn=false;
		
	}


	
	
}