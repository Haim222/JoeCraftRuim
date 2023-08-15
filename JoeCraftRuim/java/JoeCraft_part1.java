import javax.swing.JFrame;

import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.Color;


public class JoeCraft_part1 extends JFrame{
	public static void main(String aaa[]){
	  new JoeCraft_part2();
	}
	Graphics g;
// === SONS =================================
	JWav // ATENÇÃO: TODOS OS SONS ABAIXO FORAM DESABILITADOS!
		wUnderWater	= new JWav("ambiente21.wav"),		
		wOn = new JWav("turnon01.wav"),		
		wOff = new JWav("turnoff01.wav"),		
		wNoite = new JWav("ambiente56.wav", "ambiente28.wav","ambiente42.wav"),
		wTrovao = new JWav("trovao01.wav", "trovao02.wav","trovao12.wav"),
		wChuva = new JWav("chuva04.wav"),
		wLampOn = new JWav("turnon01.wav"),		
		wLampOff = new JWav("turnoff01.wav"),				
		wGrowTrigo = new JWav("get04.wav"),		
		wForn = new JWav("fogo01b.wav"),		
		wFogoIni = new JWav("fogo02b.wav"),
		wFogo = wForn,
		wGet = new JWav("get05.wav"),		
		wPop = new JWav("get05.wav"), // quando galinha bota ovo
		wPop2 = new JWav("ruido36.wav"), // quando extrai muda (nao tem como confundir com pop comum)
		wBlop = new JWav("!Blop.wav"), // quando um transportador(mais algum outro?) plotar
		wGrow = new JWav("!blop.wav"), // quando uma muda se torna arvore
		wSoco = new JWav("soco01","soco02","soco03"),
		wRealParalelIn = new JWav("magia01.wav"),
		wPortalProx = new JWav("portal05.wav",4200000000L),
		wPortalIn = new JWav("magia03.wav"),
		wArco = new JWav("arco01.wav","arco02.wav","arco03.wav"),
		wFlecha = new JWav("flecha03.wav","flecha03b.wav","flecha03c.wav"),
		wFlechaHit = new JWav("flecha04b","flecha04c","flecha04d"),
		wPoeJetPack = wArco, // trocar depois
		wRemJetPack = wArco, // trocar depois
		wMagia = new JWav("magia02.wav"),
		wMorde = new JWav("!morde.wav"),
		wChomp = new JWav("!chomp.wav"),
		wGlop = new JWav("glop01.wav","glop02.wav","glop03.wav"),
		wPeido =new JWav("peido02","peido04","peido05"),
		wPeidao = new JWav("peido04"),
		wMinham = new JWav("minham04","minham02","minham03"),
		wNojo = new JWav("nojo02b.wav"),
		wPorta = new JWav("porta05.wav","porta06.wav","porta10.wav"),
		wPulo = new JWav("pulo04.wav","pulo05.wav","pulo06.wav"),
		wFiss = new JWav("creeper06.wav"),
		wElev = new JWav("maquina01.wav"),
		wMaqNoEl = new JWav("ruido24b.wav"), // acabou a el, parou
		wMaqFunc = new JWav("maquina01b.wav"),
		wMaqPronto = new JWav("up01.wav"),
		wMaqStart = new JWav("ruido24.wav"), // acabou de ligar
		wMaqStop = new JWav("maquina01c.wav"), // desligou
		wJetPackNoEl = wMaqNoEl, // trocar depois
		wJetPack = new JWav("jet01b.wav"),
		wJetPackOn = new JWav("jet02b.wav"),
		wJetPackOff = new JWav("jet03b.wav"),
		wDerretedor = new JWav("creeper07.wav"),
		wPump = new JWav("gerador04.wav"),
		wRefinaria = wPump,
		wGuar = new JWav("ruido31.wav"), // guardador, do transportador p bau
		wSelecionador = new JWav("ruido18.wav"), 
		wEst = new JWav("gerador05.wav"),
		wEnvas = new JWav("envasadora01.wav"),
		wGerCombustao = new JWav("gerador02b.wav"),
		wSerra = new JWav("serra03.wav"),
		wBlip = new JWav("computador04.wav"),
		wTerr = new JWav("terremoto01.wav"),
		wSwitchOn = new JWav("on01.wav"),
		wSwitchOff = new JWav("off01.wav"),
		wInvCheio = new JWav("errado02.wav"),
		wExp = new JWav("bum06.wav","bum08.wav","bum24.wav"),
		wExtractor = new JWav("maquina02.wav"),//gerador04.wav
		wPloter = new JWav("ruido06.wav"),
		wChFenda = new JWav("ruido12.wav"),
		wChBoca = wChFenda, // mudar isso depois. Pegar som melhor na net
		wIdent = new JWav("identificador01.wav"),
		wVatorOn = new JWav("vatorOn02.wav"),
		wVatorOff = new JWav("vatorOff02.wav"),
		wEnxada = new JWav("pah04.wav","pah05.wav","pah06.wav"),
		wSerrote = new JWav("serrote01.wav","serrote02.wav","serrote03.wav"),
		wPah = new JWav("pah04.wav","pah05.wav","pah06.wav"),
		wMachado = new JWav("passo09.wav","passo09b.wav","passo09c.wav"),
		wPicareta = new JWav("picareta01.wav","picareta02.wav","picareta03.wav"),
		wFoice = new JWav("passo14.wav","passo14b.wav","passo14c.wav"),
		wPlotPadrao = new JWav("passo05.wav"),		
		wPlotAgua = new JWav("agua07.wav","agua08.wav","agua09.wav"),
		wPlotFolha = new JWav("passo14.wav","passo14b.wav","passo14c.wav"),		
		wPlotMuda = new JWav("impacto16.wav"),
		wPlotLama = new JWav("lama01.wav","lama02.wav","lama03.wav"),
		wPlotRocha = new JWav("passo05.wav","passo05b.wav","passo05c.wav"),
		wPlotMadeira = new JWav("madeira05.wav","madeira05b.wav","madeira05f.wav"),
		wPlotAreia = new JWav("passo08.wav","passo08b.wav","passo08c.wav"),
		wPlotTerra = new JWav("passo01a.wav","passo01b.wav","passo01c.wav"),
		wSplash = new JWav("splash03"),
		wPouso = new JWav("tomp01b"),
		wUrso = new JWav("Urso01","Urso02","Urso03"),
		wOvelha = new JWav("Ovelha04","Ovelha02","Ovelha03"),
		wMago = new JWav("mago01","mago03","mago04"),
		wAldea = new JWav("aldea01","aldea02","aldea03"),
		wCavalo = new JWav("cavalo04","cavalo02","cavalo03"),
		wVaca = new JWav("vaca01","vaca02","vaca03"),
		wGalinha = new JWav("galinha01","galinha02","galinha03"),		
		wPorco = new JWav("porco01","porco02","porco03"),
		wAranha = new JWav("aranha01","aranha02","aranha03"),
		wMinotauro = new JWav("bicho10","bicho10b","bicho10c"),
		wGhost = new JWav("ghost01a","ghost01b","ghost01c"),
		wBruxa = new JWav("bruxa08","bruxa02","bruxa03"),
		wCaveira = new JWav("caveira01","caveira02","caveira03"),
		wPeconha = new JWav("caveira01","caveira02","caveira03"),
		wZumbi = new JWav("zumbi01.wav","zumbi02.wav","zumbi03.wav");		


//		wComer = new JWav("!chomp"),
//		wBeber = new JWav("glop02"),
//		wNojo = new JWav("nojo02b"),
//		wMorder = new JWav("!morde"),
//		wMagia = new JWav("magia03"),
//		wPortalIn = new JWav("magia05"),
//		wFlechaHit= new JWav("impacto05b"),
//		wTerr = new JWav("terremoto01.wav"),

// ===================================================
		int colEsc=10, linEsc=12, crEsc=15, crEscPadrao=15, altLin=12;// o ultimo eh p carrFont
/*		
	public void esc(String p){
		esc(colEsc,linEsc,null, p);
	}
	public void esc(int x, int y, String p){
		esc(colEsc,linEsc,null, p);
	}
	public void esc(int x, int y, Color crf, String p){
		if(crf!=null){
			int tm = J.larText(p);
			J.impRetRel(crf,null, x,y-10,tm,12);
		}
		g.setColor(J.cor[16]);
		g.drawString(p,x+1,y+1);		
		
		g.setColor(J.cor[crEsc]);
		g.drawString(p,x,y);
		colEsc=x;
		linEsc=y+altLin;
		crEsc=crEscPadrao;
	}
*/

	public void esc(String p){
		esc(colEsc,linEsc, p);
	}

	public void esc(int x, int y, Color crf, String p){
		p = " "+p+" ";
		int tm = J.larText(p);
		J.impRetRel(crf, null, x,y-10,tm,12);		
		esc(x,y,p);
	}
	public void esc(int x, int y, String p){
		g.setColor(J.cor[16]);
		g.drawString(p,x+1,y+1);		
		
		g.setColor(J.cor[crEsc]);
		g.drawString(p,x,y);
		colEsc=x;
		linEsc=y+altLin;
		crEsc=crEscPadrao;
	}
		String sts = null; int cSts=0;
	public void impSts(String st){
		sts = st;
		cSts=300;
	}
	public void regSts(){
		if(cSts>0){
			cSts--;
			esc(0,12, sts);
		}
	}

	
	float VOLTA = (float)(-Math.PI*2); // identico de j3d
	final int
		intNull = -2147483648, // -2.147.483.648, um valor bem improvahvel p indicar uma area sem definicao desse numero. Zero nao serviria bem aqui.	
		CMA = J.CMA, BXO = J.BXO,
		NOR = J.NOR, SUL = J.SUL, 
		LES = J.LES, OES = J.OES,
		CEN = J.CEN,
		NL = J.NL, SL = J.SL,
		SO = J.SO, NO = J.NO,
			NOR2=30000,
			LES2=30001,
			SUL2=30002,
			OES2=30003,
			BXO2=30004,
			CMA2=30005,
				BN=30100, // baixo-nor, diagonais
				BL=30101,
				BS=30102,
				BO=30103,
					CN=30104, // cima-nor, diagonais
					CL=30105,
					CS=30106,
					CO=30107;			

	boolean 
		opGrowFast=false,
		opModoFaquir=false,		
		opImpCamMaps=false, 
		opImpPals=false;
		int 
			opImpTab2d=0, // ajuda p debug
			opImpMargem=0,
			margemX=0, // vai ajudar no debug
			maxXmap=intNull, maxZmap=intNull,
			margemY=0;
		char opCamera='j';	 // 'j' ou 't'
		
// === APOIO ==============================
	public int invDir(int d){
		switch(d){
			case NOR: return SUL;
			case SUL: return NOR;
			case LES: return OES;
			case OES: return LES;
			case CMA: return BXO;
			case BXO: return CMA;
		}
		return d;
	}
// === CAMINHOS DE ARQ ====================
		String // veja q eh necessario um metodo p alterar os caminhos devidamente, jah q o reflexo nao eh automatico p todos os caminhos.
			// maps//vanilla//
			// maps//vanilla-caves//
			// maps//zetta//
			camMundo = "maps//vanilla//",
			camMap = camMundo+"relevo.map",
			camPosJoe = camMundo+"joePos.txt";


	public String stCamAres(int i, int j, int k){
		// SEM EXTENSAO, CUIDADO!!!
		i = i % maxXmap;
		k = k % maxZmap;
		String st = camMundo; 
		st+= J.intEmSt_000000(i)+"_";
		st+= J.intEmSt_000000(j)+"_";
		st+= J.intEmSt_000000(k); // menos a extensao
		return st;
	}
	
	public void altCamMundo(String p){
		camMundo = "maps//"+p+"//";
		camMap = camMundo+"relevo.map";
		camPosJoe = camMundo+"joePos.txt";
		if(!J.arqExist(camMundo))
			J.mkDir(camMundo);
		J.esc("diretorio alterado: ");
		J.esc("    |"+camMundo+"|");
	}			
	
// === PALETAS ==============================

		ArrayList<JPal> pal = new ArrayList<>();
		int numPlts=0;
	public JPal getPal(Color cr){
		// eu nao sei se isso tah funcionando direito
		for(JPal p:pal)
			if(p.crSem==cr)
				return p;
		return null;	
	}
	public JPal addPal(int cr){
		return addPal(J.cor[cr]);
	}
	public JPal addPal(Color cr){
		if(cr==null) return null;
		int r=0, rr=0, g=0, gg=0, b=0, bb=0, a=0, aa=0;
		for(JPal p:pal){ 
			if(p.crSem.getRGB() == cr.getRGB()) // ?Serah q isso tá funcionando mesmo???
				return p;
		}
		/*
		for(JPal p:pal){ // isso nao tah legal
			r = p.crSem.getRed();
			rr = cr.getRed();
			g = p.crSem.getGreen();
			gg = cr.getGreen();
			b = p.crSem.getBlue();
			bb = cr.getBlue();
			a = p.crSem.getAlpha();
			aa = cr.getAlpha();

			if(r==rr)	if(g==gg)
			if(b==bb)	if(a==aa)
				return p;	
		}
		*/

		JPal q = new JPal(cr);
		pal.add(q);
		numPlts = pal.size();
		return q;
	}
	public void impPals(int i, int j){
		int c=0;
		esc(i,j,pal.size()+" paletas");
		for(JPal p:pal)
			p.imp(i, j+ (c++)*16);
	}		

// === TRUQUE DO SENO ======================
		float[] arrSin = null; // eh mais rapido p computador acessar um float num array q calcular um seno
		final int maxSin = 38; // nao mecha nisso
	public void iniArrSin(){
		float w[] = new float[maxSin];
		for(int q = 0; q<maxSin; q++)
			w[q] = (float)Math.sin(q*0.5f);
		arrSin = w;
	}
	public float getSin(int p){ // de 0 a maxSin
		if(arrSin==null) iniArrSin(); // autoInicializacao
		if(p<0) p = -p;
		p = p%maxSin;
		return arrSin[p];
	}
	
// ==============
	public void altCamera(char p){
		J.iniCam();		
		J.angY=0; // olhando p norte			
		J.incZ=0;
		J.zoom=760;
		
		J.zCam=0;	
		J.incZ=0;
		J.zoom=760;
		J.zoom=380;
		J.zoom=190;
		J.zoom=(190+380)>>1;
		J.fov=1f;
		
		// J.zCam=10;			
		switch(p){
			case 'j': 
				J.incZ=0; 
				break;
			case 't': 
				J.incZ=28; 
				break;				
		}
	}	
	public void iniBuffer(){
		// J.iniBuf(1);  // 320x200
		//J.iniBuf(2);  // 400x280
		J.iniBuf(3);  // 640x400
		//J.iniBuf(5); // 800x600 // essa nao gosta da tabela
		//J.iniBuf(7); // 1000x800 novo, e fora do padrao
		setImpMargem(1);
		
		J.xPonto = J.maxXbuf>>1;
		J.yPonto = J.maxYbuf>>1;



	}
	public void regMargem(){		
		J.bufRet(0,9,  
			J.xPonto-margemX, J.yPonto-margemY, 
			J.xPonto+margemX, J.yPonto+margemY );		
	}
	public void setImpMargem(int p){
		margemX = (J.maxXbuf>>1)+1+30;
		margemY = (J.maxYbuf>>1)+1+100;
		// opImpMargem = J.incIntR(opImpMargem,0,2);
		opImpMargem = J.corrInt(p, 0,2);
		switch(opImpMargem){
			case 0: break; // normal
			case 1: // reduzida
				margemX-=30;
				margemY-=30;
				break;
			case 2: // extendida, alem da tela
				margemX+=30;
				margemY+=30;
				break;				
		}

	}	

// === BARRAS =================
		Mouse ms = new Mouse(this);
	public boolean impBarV(int v, Color cr, int i, int j, int tam){
		return impBarV(v,cr,J.altColor(cr,-100),i,j,tam);
	}		
	public boolean impBarV(int v, int crOn, int crOff, int i, int j, int tam){
		return impBarV(v,J.cor[crOn],J.cor[crOff],i,j,tam);
	}
	public boolean impBarV(int v, Color crOn, Color crOff, int i, int j, int tam){
		// cuidado! Essa impressao nao estah no buffer!		
		// retorna true se mouse sobre
		int alt = tam, lar=12;
		J.impRet(crOff,null, i,j,i+lar,j+alt);
		J.impRet(crOn, null, i,j+alt-v,i+lar,j+alt);
		return ms.naAreaRel(i,j,lar,alt);
	}
	public void impBarH(int v, int crOn, int crOff, int i, int j, int tam){
		impBarH("",v,crOn,crOff,i,j,tam,false);
	}
	public void impBarH(String st, int v, int crOn, int crOff, int i, int j, int tam, boolean trem){
		// cuidado! Essa impressao nao estah no buffer!
		int alt = 12, lar=tam;
		if(trem){
			i+=J.RS(2); j+=J.RS(2);
		}
		J.impRet(crOn,  16, i,j,i+v,j+alt);
		J.impRet(crOff, 16, i+v-1,j,i+lar,j+alt);
		esc(i,j+9,""+st);
	}	
	public void impBarHbuf(int v, int crOn, int crOff, int i, int j, int ii, int jj){
		J.bufRet(crOn,  0, i,j,i+v,jj);
		J.bufRet(crOff, 0, i+v,j,ii,jj);
	}		
		
// === BARRAS P JOE ======================

		int 
			xBar = J.maxXf-120, 
			yBar = J.maxYf-190,
			joeLife = 50,
			joeAgua = 50,
			joeProt = 50,
			joeCarb = 50,
			joeVit = 50,
			joeAr = 50;

	public void regBarras(){ // impBarras impLifeJoe regLifeJoe
		//joeLife = J.cont % 100;
		
		if(!opModoFaquir) if(J.C(100)){ // decremento da alimentacao
				// ar depois
			joeAgua-=J.R(2);
			joeProt-=J.R(2);
			joeCarb-=J.R(2);
			joeVit-=J.R(2);
		}
		
		int bx = 33, al=66;		
		
		
		if(joeAr<=0) if(J.C(3)) joeLife--;

		if(!opModoFaquir) if(J.C(200)){ // descendo do life com comida baixa
					 if(joeAgua<bx) joeLife-=1; 
			else if(joeProt<bx) joeLife-=1; 
			else if(joeCarb<bx) joeLife-=1; 
			else if(joeVit<bx)  joeLife-=1;			
		}
		if(J.C(100)){ // subindo do life
			 if(joeAgua>al)
			 if(joeProt>al)
			 if(joeCarb>al)
			 if(joeVit>al) 
				 joeLife+=1;			
		}		
		{ // corrigindo valores
			joeAr = J.corrInt(joeAr,0,100); // mas poderia extrapolar 100 com algum poder. Depois.
			
			joeLife = J.corrInt(joeLife,0,100); // isso tb poderia extrapolar 100 com algum poder. Depois.
			joeAgua = J.corrInt(joeAgua,0,100);
			joeProt = J.corrInt(joeProt,0,100);
			joeCarb = J.corrInt(joeCarb,0,100);
			joeVit = J.corrInt(joeVit,0,100);			
		}
		{ // imprimindo
			int w=14; // espacamento vert
			if(joeAr<100) // automatico
			// soh depois essa aih embaixo
			impBarH("ar",joeAr, 15,7,    xBar,yBar+w*-1,100,joeAr<bx);		
			impBarH("life",joeLife, 12,4,    xBar,yBar+w*0,100,joeLife<bx); 
			
			impBarH("agua",joeAgua,  89,1, xBar,yBar+w*3,100,joeAgua<bx);
			impBarH("proteinas",joeProt,96,93, xBar,yBar+w*4,100,joeProt<bx);
			impBarH("carboidratos",joeCarb, 76,73, xBar,yBar+w*5,100,joeCarb<bx);
			impBarH("vitaminas",joeVit,  69,63, xBar,yBar+w*6,100,joeVit<bx);
		}
	}				
		
// === INFORMACAO DE IMPRESSAO =================
		final int APAGAR = -40; // p apagar luz
	public class InfImp{ // infOc ss impInf
		// informa luminosidade e face oculta do cubo em questao
		// mostra de q forma o cubo modelo serah impresso naquela coord especifica.
			boolean 
				bbb=true,
				ocTudo=bbb, // p ganhar clock
				ocNor=bbb, ocSul=bbb, 
				ocLes=bbb, ocOes=bbb, 
				ocCma=bbb, ocBxo=bbb,
				opSupAgua=false; // forcando a impressao de cima da superficie da agua, mesmo q estiver submergido.
			char dis = 0; // distorcao para canos, transportadores, rede el, etc	
			int	inf=0, // subtipo do cubo em questao, informacao de crescimento da planta, etc
				cTreme=0, // p tremer um cubo especifico numa coordenada exata. Nao confunda com "ss Cub.cTreme", q treme o modelo de cubo (todos os cubos de um tipo W plotados no mapa)
				lc=0, lb=0, ln=0, ls=0, ll=0, lo=0, // esta varia com o sombreamento
				lt=APAGAR; // esta eh de tochas e nao se altera
			float idx=0f, idy=0f, idz=0f, // eh daqui q sairah uma esteira descente
				// calibre do cubo especifico... interessante.
				in=0f, is=0f, il=0f, io=0f, ic=0f, ib=0f; 

		public void setICal(float q){
			in=is=il=io=q;
			ic=ib=q;
		}
		public InfImp(char p){
			reset();
			if(p=='a') showAll();
			if(p=='c' || p=='b') { // usei p cond
				showAll();
				ocCma=true;
				ocBxo=true;
			}	
			if(p=='n' || p=='s') { 
				showAll();
				ocNor=true;
				ocSul=true;
			}				
			if(p=='l' || p=='o') { 
				showAll();
				ocLes=true;
				ocOes=true;
			}							
		}		
		public InfImp(){
			reset();
		}
		public void showAll(){
			ocNor=ocSul=ocLes=ocOes=ocCma=ocBxo=ocTudo=false;
		}		

		public void setLuz2(int nv){
			// define a luz de tochas
			// ?limites???			
			if(lt<nv) lt = nv;
			if(nv==APAGAR) lt=0;
		}
		public void reset(){
			lc=0;
			lb=-4;
			ln=-2;
			ls=-2; 
			ll=-1;
			lo=-3;
		}
	}

// === TERREMOTO =======================	
		int cTerr=0;
		float xTerr=0, yTerr=0, zTerr=0;
	public void iniTerr(){
		cTerr=20;
		// wTerr.play(); nao aqui
	}
	public void regTerr(){
		xTerr=0; 
		yTerr=0; 
		zTerr=0; 		
		if(cTerr>0) {
			cTerr--;
			int q = cTerr;
			xTerr = J.R(q)/60f;
			yTerr = J.R(q)/60f;
			zTerr = J.R(q)/60f;
		}
	}

// === EFEITO OCULOS ====================
			int 
				cOculos=0,
				cAgOculos=0; // agendador, p atraso
			Color crOculos=null;
		public void iniOculos(Color cr, int tmp){
			iniOculos(cr, tmp,0);
		}
		public void iniOculos(Color cr, int tmp, int ag){
			cOculos=tmp;
			crOculos=cr;
			cAgOculos=ag;
		}

		public void regOculos(){
			if(cAgOculos>0)cAgOculos--;
			
			if(cAgOculos<=0)
			if(cOculos>0){
				int c = J.corrInt(cOculos*30,0,255);
				Color cr = J.altAlfa(crOculos, c);
				J.bufRetRel(cr,null, 0,0, J.maxXbuf, J.maxYbuf);
				cOculos--;
			}
		}	
	
// ======================================
	
	
} 

