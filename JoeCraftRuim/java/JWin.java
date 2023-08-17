// jWin versao 3 
// para interface com janelas, botoes, etc.
// Tem muito a corrigir, mas tem ajudado


/* JWin, INSTRUCOES GERAIS:
ADICIONAR: 
-1- JWin amb = new JWin() acima e fora do loop principal
-2- um metodo regWin() com chamada no loop principal
-3- amb.reg() dentro de regWin() (manejar comps dentro deste metodo)
-4- amb.reg(k) dentro da leitura de teclas, em VK_@@@@@@
-5- se necessário, win de save/open predefinidos neste fonte (veja guia logo abaixo)
-6- se necessário, prompt de confirmacao predefinidos tb neste fonte[EX DEPOIS]
-7- carregar win pre-salvas no editor ou criar comps no proprio codigo[EX DEPOIS]

*/
/* winSaveOpen, COMO USAR:
-1- chame a win por:
			amb.winSaveOpen(cam,nome,ext,fil)
				.setName("openWin") // CUIDADO!!! USE O MESMO NOME
				.setText("abrindo"); // isto ajuda
-2-	insira o cod:
			if(amb.onWinOk("openWin")){
				String resposta = amb.getRet(); // este eh um exemplo
				amb.close("openWin")
			}			
-3- insira o cod:
			if(amb.onWinCancel("openWin")){
				; // nao precisa fazer nada aqui
			}			
*/
/* confirm, COMO USAR:
    amb.confirm("tem certeza?","nS").setName("sobreporArq"); // "sobreporArq" aqui identifica qual ? a cx de confirma??o, importante p tratamento da resposta do usu?rio
		if(amb.onConfirmOk("sobreporArq")) 
				J.esc("tenho certeza sim.");
 		if(amb.onConfirmCancel("sobreporArq")) 
				amb.closeConfirm("sobreporArq"); // s? p fechar a bud?ga
 os botoes "sim" e "ok" estao se sobrepondo, mas isso nao eh problema.
 'S' maiusculo foca "sim" como botao padrao.
 o mesmo p "NCK" respectivamente.
 
VARIACOES DE CHAMADA DESTE METODO
	public Comp confirm(String av,String bts){
	public Comp confirm(String av,String tit, String bts){
	public Comp confirm(String av, String bts, Jf1 f){	
	public Comp confirm(String av, String tit, String bts, Jf1 f){
*/
/* prompt, COMO USAR:
     amb.prompt("aviso","padrao");
     if(amb.onPromptOk()) 
       st = amb.getRet();
 o problema eh q se tiver varios prompts, vai dar conflito. Teria q identificar qual prompt eh... ?Mas serah q precisarah de mais de um prompt???
*/
import java.awt.Font;

import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.Color;
import java.awt.event.KeyEvent;


public class JWin{
	
	
		String stTag=null;					
		//static int tmp=9; //P WINXP!!! este valor pode variar em funcao do peso da aplicacao q usa essa classe JWin; P aplicacoes pesadas (como joeCraft), eh melhor ajustar essa var p "2", q eh seu valor minimo. Por aqui dah p ajustar o tempo de resposta dos comandos;
		static int tmp=30; // P WIN10. Cuidado, isto afeta o clique duplo!
		final int
			tmpArq=3, tmpTag=32, tmpChange=3,
			LIVRE=0, SOBRE=6, PRESS=12, BLOCKED=13, 
					ESQ=J.ESQ, CEN=J.CEN, DIR=J.DIR,
				CMP = 0,
				ICO = 1, 		PIC=2, 
				IMG_BUT=3, 	TXT_BUT=4,
				ROT=5,			TXT_BOX=6,
				WIN=7,      SLD_BAR=8,
				CLR_BUT=9,	LST_BOX=10,
				CMB_BOX=12,	DB=14, BD=14, // p nao bugar aa toa
				CHO_LST=16, CHE_LST=17, // cuidado! CHK_BOX abaixo
				MNU=18,			ICO_LST=19,
				POP=20,			J3D=21,
				PCK=22,			CUR=23,
				TIM=24, 		CLR_LST_BOX=25,
				PLT=26,			CLR_CHO=27,
				DIR_LST_BOX=28,
				PIC_BUT=29,
				CHE_BOX=30, // cuidado! Este nao eh  c h e c k L i s t , eh opcao avulsa
				TXT_PLUS=32,
				PIC_LST=33;
		boolean 
			opSaiSeCompPerdido=false, // um fail e getComp("compQueNaoExiste") encerra o programa (e lista os comps disponiveis p facilitar)
			opSomTec=true, // corrige bugs, desabilite p parar o som de maquina de escrever
			opIncSt=true, // funciona meia-boca, é p incrementar strings quando parecerem com nomes de arquivo. Usei em  t e x t B o x
			opBtRespondeTecEspaco=true, // precisei p ajuste no editor. Se false, quando se pressiona espaco, o botao nao serah acionado.
		opLeuAcentoAgudo=false, // interno; p correcao do bug de leitura de caracteres com acento
		opLeuCrase=false, 
		opLeuTil=false, 
		opLeuCircunflexo=false;
			// veja tb opHomeEnd e opPageUpDown, q sao p  c o m p s  especificos. ?Daria p enxugar???
		public String stTip(int t){
			switch(t){
				case  CMP: return "CMP";
				case  ICO: return "ICO";
				case  PIC: return "PIC";
				case  IMG_BUT: return "IMG_BUT";
				case  PIC_BUT: return "PIC_BUT";
				case  TXT_BUT: return "TXT_BUT";
				case  ROT: return "ROT";
				case  TXT_BOX: return "TXT_BOX";
				case  WIN: return "WIN";
				case  SLD_BAR: return "SLD_BAR";
				case  CLR_BUT: return "CLR_BUT";
				case  LST_BOX: return "LST_BOX";
				case  DIR_LST_BOX: return "DIR_LST_BOX";
				case  CMB_BOX: return "CMB_BOX";
				case  PCK: return "PCK";
				case  DB: return "DB";
				case  CHO_LST: return "CHO_LST";
				case  CHE_LST: return "CHE_LST";
					case  CHE_BOX: return "CHE_BOX"; // este eh opcao avulsa, nao confunda com  c h k L i s t
				case  MNU: return "MNU";
				case  POP: return "POP";
				case  ICO_LST: return "ICO_LST";
				case  J3D: return "J3D";
				case  TIM: return "TIM";
				case 	CLR_LST_BOX: return "CLR_LST_BOX";
				case  PLT: return "PLT";
				case  CLR_CHO: return "CLR_CHO";
				case  TXT_PLUS: return "TXT_PLUS";
				case  PIC_LST: return "PIC_LST";
			}
			J.impErr("!Componente nao especificado: "+t,"Win.stTip()");
			return "???";
		}		
		ArrayList<Comp>	cmp = new ArrayList<>();
		ArrayList<Comp>	grp = new ArrayList<>();// p editor
		Mouse ms = null;
		static boolean 
			opAddInicioLista=false; // todo novo item da lista serah inserido no comeco dela se true
		boolean 
			opCarregandoWin=false,	// evita o bug de concurrent modification exception		
			opDoubleSizeJf1=true,
			opAjustarCam3d=true, // cuidado, se a aplicacao for 3d (como o editorJ3d) melhor deixar essa opcao como FALSE e evitar usar " s s   S h a p e 3 d "
			opMovGrid=false,
			opDebug=false, // quando true, mostra o nome do controle na dica (quando o mouse espera sobre o controle)
			opComSomb=true, // desabilitado pode salvar clocks em interfaces mistas. Serve p menu e win.
			temRem=false,
			opInsTog=false; // inserir botoes togglaveis na barra de ferr
		Jf1 
			fXis = new Jf1("xis01.jf1"),
			fAlcaWin = new Jf1("alca01.jf1"),
			fSeta = new Jf1("seta03.jf1"),
			fQuad = new Jf1("quad02.jf1"),			
			fChoOn = new Jf1("cho03On.jf1"),
			fChoOff = new Jf1("cho03Off.jf1"),
			fCheOn = new Jf1("chk04On.jf1"),
			fCheOff = new Jf1("chk04Off.jf1"),						
			fErro = new Jf1("erro01.jf1"),
			fCor = new Jf1("color03.jf1"),
			fCorb = new Jf1("color04.jf1"),
			fAm = new Jf1("cursor04.jf1"); // p paleta
		Comp 
			este = null, // ultimo g e t C o m p
			focado = null,			
			lastFocus = null, // o ultimo comp q perdeu o foco, mas CUIDADO: se o foco for definido manualmente, esta var nao serah definida!
			lastWin= null,
			lastComp = null,
			lastMenu=null,
			// p r o m p t  deveria estar aqui, como um  c o m p 
			c=null, cc=null, ccc=null, // atalhos secretos
			lastBD=null;
		int
			cArq=0,
			cAddComp=0, // usei p reordenar os componentes apos uma insercao. Menus precisam ficar por ultimo na lista.
			xIns=3,
			yIns=3;
		Color 
			crBt = J.cor[49],
			crText = J.cor[119],
			crFocus = J.cor[12], // crBlink crCint; essa eh automatica
			crSomb= crBt.darker(),
			crBox= crSomb.darker(),
			crLuz= crBt.brighter(),
			crBlk= crSomb,
			crBlkk= crSomb.darker();
			
		// pensar melhor nisso
		JWav 
			//wTec = new JWav("tiro04.wav"), // tecla reconhecida
			//wDel = new JWav("hit01.wav"), // del e backSpace
			wTec = new JWav("maq_escrever02.wav","maq_escrever05.wav","maq_escrever06.wav"),
			wAcento = new JWav("maq_escrever01.wav"),
			wDel = new JWav("maq_escrever02.wav"),
			wBut = new JWav("click03.wav"),
			wPop = new JWav("pocao01.wav"); // menu popUp
			
	public JWin(Mouse m){
		wTec.opEco=false;// nao aparecer no prompt
		wAcento.opEco=false;
		wDel.opEco=false;
		ms = m;
		setColor(J.cor[49]);
	}
	

    boolean opTemMenuAberto = false;
		Color crEsmaecer = J.altAlfa(16,0.25f);
	public void reg(){ //rrrrrrrrrrrrrr
		if(cAddComp==1) 
			corrArrCmp(); // primeiro win, depois packs, depois qq, depois menus.
	
		if(focado==null) 
		if(cmp.size()>0)
			cmp.get(0).setFocus();

		regDial();
		
		
		if(opCarregandoWin) return;
		
		if(cArq>0)cArq--; // usei em l i s t I c o n
		if(cEsc>0)cEsc--; // tecla escape, esc
		if(cKey>0)cKey--; // qualquer tecla. Examine com "key.vkblabla"
		if(cAddComp>0) cAddComp--;
		boolean temAviso=false;
		// menus precisam sempre ser o ultimo na lista
		for(Comp c:cmp){
			if(c.rem) temRem=true;
			
			// esmaecer fundo quando aparecer cx de dialogo
			if(c.opWinDial) 
				J.impRetRel(crEsmaecer,null, 0,0,J.maxXf,J.maxYf);
			
			c.reg();

			if( opTemMenuAberto)
			if(c.tip!=MNU){
				c.cClick=0; // e clique duplo?
				c.cSobre=0;
			}	

			
			if(c.cAviso>0) temAviso=true;
		}	
		
		if(temRem){
			temRem=false;
			Iterator<Comp> it = cmp.iterator();
			while(it.hasNext()){				
				if (it.next().rem) 
					it.remove();				
			}
		}
		
		{ // cor do contorno do foco, como pulsador
		
			//int q = J.cont%255; // ficou perfeito. Adotar este "pulsador" como modelo.
			//if(q>127) q = 255-q;
			
			//int q = J.cont%127;
			//if(q>64) q = 127-q;
			
			int q = J.cont%64; // pulsador ppppppppppppppppppppp
			if(q>32) q = 64-q;			
			q = q<<2; // 1..256

			crFocus=J.altAlfa(J.cor[15],q);
		}

		if(stTag!=null){
			if(!J.tem('_',stTag)){ // esquema antigo normal
				int i=ms.x+16+16, j=ms.y+16;
				String st=stTag, stt="";
				J.impRetRel(crText, crSomb, i-16,j,J.larText(stTag)+16+16,16);
				J.impText(i, j+12, crSomb, stTag);
				stTag=null;
			} else { // esquema com "_" divisor multiline
			//split(String s, char div, int n){
				int nl = J.contChar('_',stTag);
				int i=ms.x+16+16, j=ms.y+16;				
				int tm = J.larTextMaiorSplit(stTag,'_');
				String st="";
				for(int q=0; q<=nl; q++){
					st = J.split(stTag,'_',q);
					J.impRetRel(crText, null, i-16,j+q*16,tm+16+16,16);					
					J.impText(i, j+12+q*16, crSomb, st);
				}
				stTag=null;				
			}
		}
		if(temAviso){
			int xx=0, yy=0, l=0,nl=0;
			for(Comp c:cmp)
			if(c.cAviso>0){				
				xx = c.x;
				yy = c.y;
				if(c.lk!=null) {
					xx+=c.lk.x;
					yy+=c.lk.y;
				}					
				if(c.pk!=null) {
					xx+=c.pk.x;
					yy+=c.pk.y;
				}
				xx+=22; yy-=32;
				nl = J.contChar('_',c.stAviso);
				l = J.larTextMaiorSplit(c.stAviso,'_')+12+12;
				J.impRet(14,0, xx,yy, xx+l,yy+32+16*nl);
				J.impTri(14,0, xx,yy, xx,yy+32+16*nl, xx-12, yy+42);
				nl++;
				for(int q = 0; q<nl; q++){
					//l = J.larText(c.stAviso)+12+12;
					J.impText(xx+12, yy+22, crSomb, J.split(c.stAviso,'_',q));
					yy+=16;
				}
			}	
		}

	}
		KeyEvent key=null; // nao precisa mais de contadores de tecla, eh soh armazenar.
	public void reg(KeyEvent k){
		key = k;
		cKey=tmp;
		
		boolean s = k.isShiftDown();
		boolean c = k.isControlDown();
		boolean a = k.isAltDown();					
		
		char ca = k.getKeyChar();
		int  cd = k.getKeyCode();					
	
		if(lastMenu!=null)
		if(a)	
		if(!c)	
		if(!s)
		if(lastMenu.cChange<=0){			
			lastMenu.setFocus();
			lastMenu.cChange=tmp;
			lastMenu.abreMenu();
		}	
		
	  if(k.getKeyCode()==222){ // apostrofo, jah q o Tab nao pega de jeito nenhum...
			if(s) selCompAnt();
			else selProxComp();				
		}	
		
		// aqui eh geral	
		if(cd==k.VK_ESCAPE) cEsc = tmp;					
		if(cd==k.VK_F1 && s) {
			opDebug=!opDebug; // p qq programa
			J.playArqWav((opDebug?"on01.wav":"off01.wav"));
			J.esc("DEBUG: "+opDebug);
		}

		if(lastBD!=null){ 
			// soh p banco de dados
			// se ao menos 1 tiver sido inserido, todos os possiveis serah examinados
			
			if(k.getKeyCode()==k.VK_PAGE_UP)
			for(Comp cc:cmp)
			if(cc.tip==BD)
				cc.selAnt();
			
			if(k.getKeyCode()==k.VK_PAGE_DOWN)
			for(Comp cc:cmp)
			if(cc.tip==BD)
				cc.selProx();		
			
			
			if(k.getKeyCode()==k.VK_ENTER || k.getKeyCode()==222)
			for(Comp cc:cmp)
			if(cc.tip==BD)
				cc.saveRec(); // "assentando" registros nos bancos de dados... kkkkkk
		}
		
		if(focado!=null) focado.reg(k);
	}
	
// === FUNCOES DE APOIO =================
// fffffffffff
		int 
			cKey=0, // p qq tecla
			cEsc=0; // contador p tecla esc geral. Pega em qq controle. Eh de ambiente.
	public void setPos(String nm, int p){
		getComp(nm).setPos(p);
	}
	public void setPos(String nm, int i, int j){
		getComp(nm).setPos(i,j);
	}
	public int size(){
		return cmp.size(); // mais intuitivo
	}
	public boolean temNoGrupo(Comp p){
		for(Comp c:grp)
			if(c==p) return true;
		return false;
	}
	public boolean addGrp(String nm){
		return addGrp(getComp(nm));
	}
	public boolean addGrp(Comp c){
		// nao adiciona repetidos
		if(temNoGrupo(c)) return false;
		grp.add(c);
		return true;
	}	
	public boolean onAddComp(){ // dispara se adicionar qq tipo de comp (mas nao comp primitivo)
		if(cAddComp==1) return true;
		return false;
	}	
	public boolean isChecked(String nm){
		Comp c = getComp(nm);
		if(c==null) J.impErr("!componente nao encontrado: |"+nm+"|","JWin.isChecked()");
		if(c!=null)
			if(c.tip==CHE_BOX)
				if(c.tog) return true;
		return false;
		// ?adaptar p outros controles???
		// ?como fica um checkList aqui??? Lembre q itens destas listas nao tem nomes, apenas seus containers
	}
	public Comp geraRotulo(String st){
		if(focado==null) return null;
		
		if(focado.tip==TXT_BOX){
			if(st==null) st=focado.getText();
			addRotulo(
				st, 
				focado.x, 
				focado.y-18, 				
				focado.lar,
				12);
		}
		
		if(focado.tip==DB) {
			focado.geraInterf();
		}

		return focado;
	}
	
	public int getVal(String nm){
		return getComp(nm).getVal();
		//return getComp(nm).sel;
	}
	public int getSel(String nm){
		// ?E se retornar null???
		// nao sei se isso tá bugado
		return getComp(nm).sel;
	}	
	public void corrArrCmp(){		
		if(temComp("prompt")) return; // tirou o bug??? Parece q sim. Tinha dado problema na interface do IA, aquela sem win no fundo.
		
		// deixa packs logo apos win, p q packs nao ocultem controles
		// menus precisam ficar sempre por ultimo
		// evita q botoes fiquem embaixo de win
		// lembrar: nao ajuda deixar packs com fundo transparente jah q isso impossibilita usa-los como guias
		ArrayList<Comp> lc = new ArrayList<>();
		
		// 1- win
		// 2- packs
		// 3- qq outro comp
		
		// 1- win
		for(Comp c:cmp)
			if(c.tip==WIN)
				if(!c.opWinDial)
					lc.add(c);
		// 2- packs
		for(Comp c:cmp)
			if(c.tip==PCK)
				lc.add(c);			
		// 3- qq outro
		for(Comp c:cmp)
			if( (c.tip==WIN&&!c.opWinDial) || c.tip==PCK || c.tip==MNU)
				; // nao faz nada
			else
				lc.add(c);			
		// 4- menus por ultimo	
		for(Comp c:cmp)
			if(c.tip==MNU)
				lc.add(c);						
		cmp = lc;// serah q isso buga???		
	}
	public void praCima(String nm){
		// ?e se nao achar???
		praCima(getComp(nm));
	}
	public void praBaixo(String nm){
		// ?e se nao achar???
		praBaixo(getComp(nm));
	}	
	public void praCima(Comp p){
		// vai mais pra frente da lista
		int ind = cmp.indexOf(p);
		if(ind==cmp.size()-1) return;
		
		cmp.remove(ind);
		cmp.add(ind+1,p);
		corrArrCmp();		
	}
	public void praBaixo(Comp p){		
		// vai mais pro comeco da lista
		int ind = cmp.indexOf(p);
		if(ind==0) return;		
		
		cmp.remove(ind);
		cmp.add(ind-1,p);	
		corrArrCmp();
	}
	public void proTopo(Comp p){
		// "p" fica como ultimo da lista
		if(p==null) return;
		if(cmp==null) return;
		if(cmp.size()<=0) return;
		
		ArrayList<Comp>ls = new ArrayList<>();
		
		for(Comp c:cmp)
			if(c!=p)
				ls.add(c);
		for(Comp c:cmp)
			if(c==p)
				ls.add(c);			
		cmp = ls;		
		corrArrCmp();		
	}	
	public void praBase(Comp p){
		// "p" fica no comeco da lista, porem logo apos win
		if(p==null) return;
		if(cmp==null) return;
		if(cmp.size()<=0) return;
		
		ArrayList<Comp>ls = new ArrayList<>();
		for(Comp c:cmp)
			if(c.tip==WIN)
				ls.add(c);			
		for(Comp c:cmp)
			if(c==p)
				ls.add(c);					
		for(Comp c:cmp)
			if(c!=p && c.tip!=WIN)
				ls.add(c);

		cmp = ls;		
		corrArrCmp();		
	}	
	public void showPack(String nm){
		// deve exibir somente o pack nm e esconder todos os outros
		// deve ajudar p simular guias
		// assume q os packs jah foram pareados, ou seja, jah foram postos um sobre os outros. 
		// use: amb.getComp("pk1").move("pk0"), para mover 1 sobre 0
		for(Comp c:cmp)
			if(c.tip==PCK)
				c.hide();
		getComp(nm).show();
	}		
	public void show(String nm){
		getComp(nm).show();
	}
	public void close(String nm){
		getComp(nm).close();
	}	
	public void hideAllPacks(){
		// isso ajuda no esquema de guias
		for(Comp c:cmp)
			if(c.tip==PCK)
				c.hide();
	}
	public boolean onClickMenu(String st){
		if(lastMenu==null) return false;
		if(lastMenu.onClickItem(st)) return true;
		if(!lastMenu.temItem(st)) 
			J.impErr("!item de menu nao encontrado: |"+st+"|");		
		return false;
	}
	public void impText(int x, int y, Color cr, String st){
		J.impText(x+1,y+1,crSomb,st);
		J.impText(x,y,cr,st);
	}
	public boolean focado(String nm){
		if(getComp(nm)==focado)
			return true;
		return false;
	}
	public boolean onOpen(String nm){
		if(getComp(nm).onOpen()) return true;
		// ?e se nao achar o comp???
		return false;
	}

	public boolean onClose(String nm){
		if(getComp(nm).onClose()) return true;
		return false;
	}
	public boolean onCreate(String nm){ // usei p BD
		if(getComp(nm).onCreate()) return true;
		return false;
	}
	public boolean onClick(String nm){
		nm = J.emMaiusc(nm);
		// se nao achar o bt, nao dah erro
		// J.esc("?click: "+nm);
		for(Comp c:cmp)
			if(nm.equals(c.getName()))
				if(c.onClick())
					return true;		
		return false;
	}
	public boolean onDoubleClick(String nm){
		nm = J.emMaiusc(nm);
		// se nao achar o bt, nao dah erro
		// J.esc("?click: "+nm);
		for(Comp c:cmp)
			if(nm.equals(c.getName()))
				if(c.onDoubleClick())
			return true;		
		return false;
	}
	public boolean onRightClick(String nm){
		// esse eh com o botao dir
		if(getComp(nm).cClickk==1) return true;
		return false;
	}	
	public boolean onClickk(String nm){
		return onRightClick(nm);
	}		
	public void hide(String nm){		
		nm = J.emMaiusc(nm);
		if(temComp(nm))
		getComp(nm).hide();
	}
	public Comp setFocus(String nm){		
		nm = J.emMaiusc(nm);
		for(Comp c:cmp)
			if(c.getName().equals(nm)){
				c.setFocus();
				este = c;
				return c;
			}	
		J.impErr("!componente nao encontrado: |"+nm+"|", "JWin.setFocus()");
		return null;
	}
	public boolean onBackSpacePress(String nm){
		nm = J.emMaiusc(nm);
		for(Comp c:cmp)
			if(nm.equals(c.getName()))
				if(c.onBackSpacePress()) 
					return true;		
		return false;
	}	
	public boolean onSpacePress(String nm){
		nm = J.emMaiusc(nm);
		for(Comp c:cmp)
			if(nm.equals(c.getName()))
				if(c.onSpacePress()) 
					return true;		
		return false;
	}	
	public boolean onLostFocus(String nm){
		nm = J.emMaiusc(nm);
		for(Comp c:cmp)
			if(nm.equals(c.getName()))
				if(c.onLostFocus()) 
					return true;		
		return false;
	}
	
	public boolean onEnterPress(String nm){
		nm = J.emMaiusc(nm);
		for(Comp c:cmp)
			if(nm.equals(c.getName()))
				if(c.onEnterPress()) 
					return true;		
		return false;
	}
	public boolean onDelPress(String nm){
		nm = J.emMaiusc(nm);
		for(Comp c:cmp)
			if(nm.equals(c.getName()))
				if(c.onDelPress()) 
					return true;		
		return false;		
	}
	public boolean onEscPress(){
		if(cEsc==1) { // esse eh de ambiente
			cEsc=0;
			return true;
		}
		return false;
	}
	public boolean onSpacePress(){
		// esse eh de ambiente
		// isso pode ser melhor pensado.
		if(key!=null)
		if(key.getKeyCode()==key.VK_SPACE)	
		if(cKey==1) { 
			cKey=0;
			return true;
		}
		return false;
	}	
	public boolean onMouseEnter(String nm){
		return getComp(nm).onMouseEnter();
	}		
	public boolean onMouseExit(String nm){
		return getComp(nm).onMouseExit();
	}			
	public void hideAll(){
		for(Comp c:cmp)
			c.setHid(true);
	}
	public void showAll(){
		for(Comp c:cmp)
			c.setHid(false);
	}
	public boolean tog(String nm){
		nm = J.emMaiusc(nm);		
		Comp c = getComp(nm);
		if(c!=null)
			return c.tog;
		J.impErr("!componente nao encontrado: |"+nm+"|", "JWin.tog()");		
		return false;
	}
	public Comp getSobre(){
		for(Comp c:cmp)
			if(c.sobre()) 
				return c;
		return null;
	}
	public boolean sobre(String nm){
		nm = J.emMaiusc(nm);
		if(!temComp(nm))
			J.impErr("!componente nao encontrado: |"+nm+"|", "JWin.sobre()");
		return getComp(nm).sobre();
	}
	public boolean onChange(String nm){
		return getComp(nm).onChange();
	}
	public int getInt(String nm){
		// ajudou p calculos no BD de produtos
		String st = getText(nm);
		return J.stEmInt(st);
	}
	public String getCam(String nm){
		nm = J.limpaSt(nm);
		nm = J.emMaiusc(nm);		
		
		if(temComp(nm))
			return este.getCam();
		J.impErr("!componente nao encontrado: |"+nm+"|", "JWin.getCam(nm)");		
		return "???";
	}
	
	public String getText(String nm){
		nm = J.limpaSt(nm);
		nm = J.emMaiusc(nm);		
		
		if(temComp(nm))
			return este.getText();
		J.impErr("!componente nao encontrado: |"+nm+"|", "JWin.getText(nm)");		
		return "???";
	}
	public Comp setText(String nm, int v){
		return setText(nm, J.intEmSt(v));
	}
	public void selAll(){
		// repare q nao tem parametro de nome
		// seleciona todos os componentes. Usado com CTRL+A. Util p, por exemplo, deslocar todos os componentes ao mesmo tempo.
		grp.clear();
		for(Comp c:cmp)
			grp.add(c);
	}
	public void selAll(String nm){
		getComp(nm).selAll();
	}	
	public void tremer(String nm){
		getComp(nm).tremer();
	}		
	public Comp setText(String nm, String st){
		if(!temComp(nm)) 
			J.impErr("!componente nao encontrado: "+nm,"JWin.setText(nm,st)");
		return getComp(nm).setText(st);
	}
	public Comp addText(String nm, String st){
		if(!temComp(nm)) 
			J.impErr("!componente nao encontrado: "+nm,"JWin.setText(nm,st)");
		else 
			este.setText(este.text+st);
		return este;
	}	
	public boolean copyText(String o, String d){
		// de "Origem" para "Destino"
		if(temComp(o))
		if(temComp(d)){
			getComp(d).setText(getComp(o).getText());
			return true;
		}			
		return false;
	}
	public boolean doubleClick(String nm){
		nm = J.emMaiusc(nm);
		for(Comp c:cmp)
		if(c.dcl)
		if(nm.equals(c.getName()))
			return true;
		return false;
	}
	public void acClick(String nm){
		getComp(nm).acClick();
	}
	public Comp getClicked(){
		// retorna o ultimo c m p clicado
		// jah ajusta "este"
		// nao vale p win
		for(Comp c:cmp)
			if(c.cClick>0)
				if(c.tip!=WIN){
					este = c;
					return c;
				}	
		return null;		
	}
	public Comp getComp(String nm){
		nm = J.limpaSt(nm);
		nm = J.emMaiusc(nm);
		
		for(Comp c:cmp)
			if(c.getName().equals(nm)){
				este = c;
				return c;
			}	
		if(!opSaiSeCompPerdido)
			J.esc("aviso: componente nao encontrado: |"+nm+"|");	
		else{
			listAllComps();
			J.esc("dica: 'JWin.opSaiSeCompPerdido = false' evita esta interrupcao. ");
			J.impErr("!componente nao encontrado: |"+nm+"|","JWin.getComp()");	
		}
		este = new Comp(); 
		return este; // melhor assim
	}
	public void listAllComps(){
		J.esc("=== LISTANDO COMPONENTES DE JWIN ===");
		for(Comp c:cmp)
			J.esc(c.getName());
		J.esc("=====================================");
	}
	public boolean temComp(String nm){
		nm = J.limpaSt(nm);
		nm = J.emMaiusc(nm);		
		for(Comp c:cmp)
			if(c.getName().equals(nm)){
				este = c;
				return true;
			}			
		return false;	
	}
	public boolean temComp(String nm, String nmm){
		// se tiver um OU outro retorna true
		nm = J.limpaSt(nm);
		nm = J.emMaiusc(nm);		
		
		nmm = J.limpaSt(nmm);
		nmm = J.emMaiusc(nmm);		
		
		for(Comp c:cmp)
			if(c.getName().equals(nm)
			|| c.getName().equals(nmm)) {
				este = c;
				return true;
			}			
		return false;	
	}	
	public void impCh(int i, int j, Color cr, char ch){
		// cr precisou ser passado como parametro pela impressao de cx de t e x t o bloqueada.
		impText(i,j, cr, ""+ch); 
	}
	public void impCx_(Color cr, Color crl, Color crs, int i, int j, int ii, int jj, int est){
		// coordenadas relativas
		// e = estado: LIVRE, SOBRE, PRESS... mas ainda nao apliquei isso...
		
		int q=1;
		if(est==SOBRE) q=2;
		if(est==PRESS) q=0;
		
		J.impRetRel(crs,null, i+q,j+q,ii,jj);
		J.impRetRel(crl,null, i-1,j-1,ii,jj);
		J.impRetRel(cr,null, i,j,ii,jj);
		//J.impMoldRel(crl, crs, i,j,ii,jj);	

	}
	public void impContBlk(int i, int j, int ii, int jj){
		// coods relativas
		i-=2;
		j-=2;
		ii+=5;
		jj+=5;
		J.impRetRel(null, crBlk, i,j, ii,jj);		
	}
	public void impFocus(int i, int j, int ii, int jj){
		// coods relativas
		i-=3;
		j-=3;
		ii+=6;
		jj+=6;
		J.impRetRel(null, crFocus, i,j, ii,jj);
	}
	public boolean remove(String nm){
		// tolera sem dar erro caso nao existir
		nm = J.emMaiusc(nm);
		for(Comp c:cmp)
			if(c.name.equals(nm)){
				c.remove();
				return true;
			}
		return false; // nao achou o controle, mas nao precisa dar erro por isso.	
	}
	public String newName(int t){
		int c=0;
		for(Comp cc:cmp)
		if(cc.tip==t) c++;
		return stTip(t)+J.intEmSt00(c);
	}
	public void selProxComp(){
		boolean vai=false;
		if(cmp.size()<=0) return;
		for(Comp c:cmp){
			if(vai) {
				c.setFocus();
				return;
			}	
			if(c==focado) vai=true;
		}
		// esse eh caso nenhum selecionado antes ou caso a l i s t a tenha acabado.
		cmp.get(0).setFocus();
	}
	public void selCompAnt(){ 
		if(focado==cmp.get(0)) {
			// pegar o ultimo da lista
			cmp.get(cmp.size()-1).setFocus();
			return;
		}	
	
		Comp ant = null;		
		for(Comp c:cmp){
			if(c==focado) { // jamais serah o primeiro aqui
				ant.setFocus();
				return;
			}	
			ant = c;
		}
		cmp.get(0).setFocus(); 
	}	
	public void rndColor(){
		setColor(J.cor[J.R(512)]);
	}
	public void setAmbColor(Color cr){ 
		// precisei fazer isso p poder definir a cor do ambiente pela cor de win
		// esse metodo nao eh dispensavel
		setColor(cr);
	}
	public void setColor(int cr){
		setColor(J.cor[cr]); // limites??? Depois. Se precisar.
	}
	public void setColor(Color cr){
		crBt = cr;
		crText = J.cor[15]; // acho q eh melhor deixar isso fixo
		crFocus = J.cor[12]; // essa eh automatica
		int q=24;
		crSomb= J.altColor(cr,-q*3);
		crBox= J.altColor(cr,-q);
		crLuz= J.altColor(cr,+q);
		crBlk= J.altColor(cr,-q-q);
	}
	public void saveWin(String cam){
		// deve sempre salvar win primeiro; Normalmente ela eh o primeiro controle.
		// PRECISA SER REFORMULADO P SALVAR TAGS E HINTS
		// extrair depois com J.extTokens e usar "_" no lugar de " "
		cam = J.corrCam(cam,"win");
		
		// bkps automaticos
		if(cam.equals("props.win")){
			J.delArq("props.win.bkp");
			J.renameArq(cam, "props.win.bkp");
		}
		if(cam.equals("colors.win")){
			J.delArq("colors.win.bkp");
			J.renameArq(cam, "colors.win.bkp");
		}					
		
		ArrayList<String> lin = new ArrayList<>();
		lin.add("// JWin versao 3.0 ");
		lin.add("AMB "+J.colorEmStc(crBt));
		for(Comp c:cmp)
			if(c.tip!=PCK)
				lin.add(c.getScript());		
		for(Comp c:cmp) // packs por ultimo
			if(c.tip==PCK)
				lin.add(c.getScript());					
		J.saveStrings(lin, cam);
	}
		boolean opCarrSavedColors=false;
	public boolean closeWin(String nm){
		if(temComp(nm))
		if(getComp(nm).tip==WIN){
			este.close();
			return true;
		}	
		return false;
	}
	public Comp openWin(String cam){
		/*	ESTE METODO PRECISA SER REFORMULADO
					salvar tags e hints usando "_" no lugar de " " e abrindo com J.extTokens() p tudo;
					veja q tags e hints ainda nao estao sendo salvos
					acentos e cedilha ainda aparecem com defeito; Testar utf-8 p save e open depois
		*/
		grp.clear();
		cam = J.corrCam(cam,"win");
		if(!J.arqExist(cam)) J.impErr("!arquivo perdido: "+cam,"JWin.openWin()");
		ArrayList<String> lin = J.openStringsUTF8(cam);
		int i=0, j=0, ii=0, jj=0;
		String tx="", nm="", tg="";
		Comp w = null;
		for(String s:lin){
			opCarregandoWin=true;			
			J.extTokens(s);
			if(s.charAt(0)=='/') continue; // comentarios com "//"
			if(s.equals("")) continue; // pular linhas em branco
			
			// 1		2		3			4			5		6			7
			// WIN WIN1 0100 0100 0500 0300: Exemplo
			nm = J.tk2;
			tg = J.truncarAntes('@',s); if(tg.equals("")) tg=null;
			i = J.stEmInt(J.tk3);
			j = J.stEmInt(J.tk4);
			ii = J.stEmInt(J.tk5);
			jj = J.stEmInt(J.tk6);
			tx = J.truncarAntes(':',s); // ignora tudo antes de ":", inclusive			
			tx = J.remChar('%',tx);
			tx = J.semSpcIni(tx);
			tx = J.truncarAte('@',tx); // arroba aponta tag de dica, sempre no final
			
			if(tx.equals("null")) tx=null;
			
			// esse eh especial
			if(opCarrSavedColors)
			if(J.tk1.equals("AMB")) setColor(J.stcEmColor(J.tk2));

			// WIN precisa ser primeiro
			// PCK foi programado p ser o ultimo, deve entao ser aberto por ultimo tb
			if(J.tk1.equals("WIN")) w = addWin(tx, i,j,ii,jj);
			if(J.tk1.equals("PCK")) addPack(tx,i,j,ii,jj); // com um ";" deve-se automaticamente adicionar os componentes, mas isso embutido na classe pack
			if(J.tk1.equals("ICO")) addIcon(tx, i,j);
			if(J.tk1.equals("TIM")) addTimer(tx, i,j);
			if(J.tk1.equals("PIC")) addPicture(tx, i,j,ii,jj);
			if(J.tk1.equals("IMG_BUT")) addImageButton(tx, i,j,ii,jj);
			{ // multistate p image button
				if(J.tem(';',tx)){
					lastComp.cam = J.palavraK(0,tx);
					lastComp.text = tx;
				}				
			}
			if(J.tk1.equals("PIC_BUT")) addPictureButton(tx, i,j,ii,jj);
			if(J.tk1.equals("TXT_BUT")) addTextButton(tx, i,j,ii,jj);
			if(J.tk1.equals("ROT")) addRotulo(tx, i,j,ii,jj);
			if(J.tk1.equals("TXT_BOX")) addTextBox(tx, i,j,ii,jj);
			if(J.tk1.equals("SLD_BAR")) addSlideBar(i,j,ii,jj)
					.setMinMax(J.stEmInt(J.tk8),J.stEmInt(J.tk9))
					.setVal(J.stEmInt(J.tk7))
					.setColor(J.tk10,J.tk11);
			if(J.tk1.equals("CLR_BUT")) addColorButton(J.stcEmColor(tx), i,j,ii,jj).setTag(tg);
			if(J.tk1.equals("ICO_LST")) addIconList(i,j,ii,jj).addItens(tx);
			if(J.tk1.equals("PIC_LST")) addPicList(i,j,ii,jj).addItens(tx);
			if(J.tk1.equals("LST_BOX")) addListBox(i,j,ii,jj).addItens(tx);
			if(J.tk1.equals("DIR_LST_BOX")) addDirListBox(i,j,ii,jj).lstArqs(tx,null,null); // parece ser suficiente; seria cam fil ext
			if(J.tk1.equals("CHO_LST")) addChoiceList(i,j,ii,jj).addItens(tx);
			if(J.tk1.equals("CHE_LST")) addCheckList(i,j,ii,jj).addItens(tx);

			if(J.tk1.equals("CHE_BOX")) addCheckBox(tx,nm,i,j); // nm vai ser atribuido de novo no final, mas nao gera problema
			
			if(J.tk1.equals("CMB_BOX")) addComboBox(i,j,ii,jj).addItens(tx);
			if(J.tk1.equals("DB")) addDataBase(J.limpaSt(tx),i,j,ii,jj);			
			if(J.tk1.equals("MNU")) addMenu(J.limpaSt(tx), i,j,ii,jj);
			if(J.tk1.equals("J3D")) addShape3d(tx, i,j,ii,jj);
			if(J.tk1.equals("CLR_LST_BOX")) addColorListBox(i,j,ii,jj);
			if(J.tk1.equals("PLT")) addPaleta(tx, i,j,ii,jj);
			if(J.tk1.equals("CLR_CHO")) addColorChooser(i,j,ii,jj);
			if(J.tk1.equals("TXT_PLUS")) addTextPlus(i,j,ii,jj);

			if(J.iguais(J.tk1,"AMB")) continue;
			
			if(lastComp==null)
				J.impErr("!"+J.tk1);
			
			lastComp.setTag(tg);
			lastComp.setName(nm);

			// ?mas e se existirem duas win num mesmo arq???
			// esse cam precisa sim ser definido.
			if(lastComp.tip==WIN) lastComp.setCam(cam);

			// abaixo eh melhor fazer no codigo mesmo
			// soh quando aparece jf1
			// if(J.tem('^',tx)) lastComp.setOption("v"); 
			// if(J.tem('>',tx)) lastComp.setOption("h"); // soh p jf1
			
			if(J.tem('%',s)) lastComp.opMultiLine = true;
			if(J.tem('>',tx)) lastComp.aln = J.DIR;
			if(J.tem('^',tx)) lastComp.aln = J.CEN;
			if(J.tem('!',tx))	lastComp.blk = true; // bloqueado
			if(J.tem('~',tx))	lastComp.canTog = true; // alternavel
			if(J.tem('$',tx))	lastComp.setFocus();
			lastComp.cCreate=tmp<<1;
			

			// no momento, ignora qq tipo nao reconhecido
			// seria bom exibir aviso. Depois.
		}
		opCarregandoWin=false;
		if(w!=null)// pode ser um arq win sem algum comp win
			w.cOpen=tmp+tmp;
		return w;
	}
	public void clear(){
		cmp.clear();
	}
	public Comp clear(String c){
		Comp cc = getComp(c);
		cc.clear(); // dah p limpar textos tb
		return cc;
	}	
		Color crSombMenu = J.altAlfa(J.cor[16],24);
	public void impSombMenu(int i, int j, int ii, int jj){
		if(!opComSomb) return; // pode salvar clocks em interfaces mistas (tipo joeCraft q eh pesado + JWin)
		int s = 12, q=3;
		i+=s;
		j+=s;
		jj+=6;
		for(int ss=0; ss<4; ss++)
			J.impRetRel(crSombMenu, null, i+ss*q,j+ss*q,ii-ss*2*q,jj-ss*2*q);
	}
	
// === COMP PRIMITIVO ===================	

	public class Comp{ // cccccccccccc
				int xCalMs=0; // calibragem fina do mouse, usei no slideBarHor (precisou), meio gambiarra mas funciona.
			boolean canMod=true, canAdd=true, canDel=true; // estes 3 p bds, mas daria p adaptar p outros comps tb
			public Comp save(){ return this; } // usei no banco de dados, salva registros (modificados e novos) q estao na memoria ram
			public Comp open(){ return this; } // usei no banco de dados
			public void delRec(){} // esse tres p banco de dados
			public void newRec(){}
			public void saveRec(){} // apenas "assenta" o registro do BD atual na memoria RAM. Chamada autom?tica, mas deixei este metodo assessivel tb.
		
		public void copiarParaClipBoard(){
			if(tip!=TXT_BOX) return; // cuidado com isso
			// examinar melhor  s e l M i n   e   s e l M a x  depois do processo			
			
			if(selMax!=-1)
				J.setTxtClipBoard(J.pegaTrecho(selMin,selMax,text));
			
		}
		public boolean colarDoClipBoard(){
			if(tip!=TXT_BOX) return false; // cuidado com isso
			// examinar melhor  s e l M i n   e   s e l M a x  depois do processo
			
			// retorna true se conseguiu colar
			String cl = J.getTxtClipBoard();
			String st = getText();
			int len = st.length();
			
			if(J.iguais(st,"")){
				setText(cl);
				return true;
			} 
			if(cur==len) if(selMax==-1) if(len>0){ // sem selecao e cur no fim do st
				setText(st+cl);
				return true;							
			} 
			if(cur==0) if(len>0){ // no comeco do string nao nulo
				setText(cl+st);
				return true;							
			}
			if(selMax==-1) if(len>0){
				st = J.insText(cl,st,cur); 
				setText(st);
				return true;
			}
			if(selMax!=-1){
				st = J.remTrecho(selMin,selMax,st);				
				st = J.insText(cl,st,selMin); 
				setText(st);				
				return true;
			}
			


			return false;
		}

		public int size(){ // mais intuitivo q getSize() // usei em  l i s t B o x , mas pode/deve expandir p outros controles
			return -1; 
		}
			
		// abaixo, usei p  t e x t P l u s	(eu queria fazer este text plus com JText.java, seria mais legal)
		public Comp insText(String st){ 
			// nao coloquei "a d d T e x t ()" p nao confundir com insersores de  c o m p, q sao os metodos logo acima das classes
			return this;
		}
		public Comp setFont(String cm, int tm){
			return this;
		}
			
		public Comp geraInterf(){ return this; } // usei p banco de dados
		public Comp geraRotulo(String st){
			if(st==null) st=getText(); // precisava gerar a esq tb, como opcao. Depois.
			addRotulo(st, x, y-18, lar,12);
			return this;
		}
		public Comp setMinMax(int mn, int mx){ // usei em slide bar
			selMin = mn; // repare q usa as mesmas vars de selecao de texto
			selMax = mx;
			return this;
		}

		public void impCx(Color cr, Color crl, Color crs, int i, int j, int ii, int jj, int est){
			// coordenadas relativas
			// e = estado: LIVRE, SOBRE, PRESS... mas ainda nao apliquei isso...


			
			int q=1;
			if(est==SOBRE) q=2;
			if(est==PRESS) q=0;

			if(cBlink>0) cr = crFocus;
			
			J.impRetRel(crs,null, i+q,j+q,ii,jj);
			J.impRetRel(crl,null, i-1,j-1,ii,jj);
			J.impRetRel(cr,null, i,j,ii,jj);
			//J.impMoldRel(crl, crs, i,j,ii,jj);	

		}


			// todos os outros componentes derivam desse
			int x=0, y=0, lar=22, alt=12, 
				tab1=-1, tab2=80, tab3=120, tab4=160, // usei p listBox, p colunas. Expandir p outros comps depois se precisar.
				xx=0, yy=0, // coordenadas absolutas dentro da tela, calculadas por "c a l c A b s ()"
				larArq=100, altArq=100, // p p i c t u r e
				cAjuste=0, tip=0, ipl=-1, // ipl=i t e n s por l i n h a
				cClick=0, cClickk=0, 
				cFocus=0, cLostFocus=0,
				cSobre=0, cAdd=0, cRem=0, cSpace=0, cBackSpace=0,
				cCreate=0, // p onCreate, assim q o comp for criado. Usei no bd prod.
				
				cFecha=0, // fecha c o m b o b o x com atrazo, menu tb
				cChange=0, cEnter=0, cOpen=0, // cOpen quando o comp acaba de ser carregado com o win.
				cTcUp=0, cTcDown=0, cTcLeft=0, cTcRight=0, cTcDel=0,
				cAviso=0,
				cTreme=0, cBlink=0, // estes dois sao mais p debug
				cClose=0, // p  w i n,  r e m o v e com atrazo
				aln= J.ESQ, 
				selMin=-1, selMax=-1, cur=-1, sel=0; // cur p cx de t e x t o, sel p l i s t B o x 
			Color // precisou especificar p s l i d e B a r  e  c o l o r B u t t o n 
				crFrente=crBt, 
				crFundo=crBox; 
			Comp 
				lk=null, // ponteiro p janela do comp
				pk=null; // ponteiro p pack q o comp pertence 
			String 
				text = null, cam=null, name=null, cam_=null,
				hint = "", // dica p quando a cx de texto (ou outra possivel... pensei em combo box e textPlus; talvez eu implemente p estes outros comps). N?o salvei no arquivo ".win" ainda, nem coloquei na interface de propriedades no editor, mas j? pode definir isso na inicializacao do fonte em quest?o q j? funciona. Se o texto for nulo ou fazio, este "hint" aparecer? em cor mais escura indicando a dica do campo em quest?o (como no cel);
				tag = null, stAviso=null, // use '_' para multiline no aviso
				fil = null, ext=null, // esses sao p listagem de arquivos, usei em lst
				itemClicado=null, // p menu... depois mais algum
				itemFocado=null; // focado no menu, mas nao necessariamente clicado
			boolean 
				opPageUpDown=true, // usei p  l i s t B o x  num fonte(editor de animacao com png. Vingou?). Precisei desabilitar estas teclas num comp especifico.
				opHomeEnd=true, // idem... nao sei se funciona em outros controles (testei soh no  l i s t b o x)
				opWinDial=false, // pequena gambiarra p remover bug de cx de dialogo abaixo de  c o m p s antigos. 
				opSelItmComEspaco=true, // usei p  d i r L i s t B o x , mas poderia expandir p outros controles. Depois, se precisar.			
				canResize=true,
				resizing=false, // usei em win, mas dah p expandir p outros comps
				opTemX=true, // esse eh p win
				aberto=false,
				opMultiLine=false, // interno, mais p rotulos. Usar "_" no primeiro caracter do string o omite e habilita a divisao de linhas nos caracteres posteriores. (ninguem colocaria um "_" no comeco de um rotulo comum, logo, usei de flag);
				blk=false,
				hid=false,				


				msExit=false, // interno, nao use isso; Eh um flag q indica quando o  m o u s e  saiu de cima do  c o m p 
				tog=false,
				canTog=false,
				dcl=false, // interno, flag p c l i q u e duplo
				rem=false;	
				// cccccccccccccc
				
		public void saveList(String cm){}	// usei em  L i s t B o x  e  D i r L i s t B o x 
		public void openList(String cm){}				
		public Comp setOption(String st){
			return this;
		}		
		public Comp setAll(boolean v){ // usei p  c h e c k L i s t
			return this;
		}
		public boolean setValItem(String i, boolean b){ // usei p  m e n u
			return false;
		}
		public boolean getValItem(String i){ // usei p  m e n u
			return false;		
		}
		public String getItem(int p){ // usei em "l i s t B o x". Expandir p similares depois;
			return "";
		}
		public Comp linkComps(String p){ return null; }// usei em pack, mas dah p expandir o esquema p win
		public void regTreme(){ 
			if(cTreme>0){
				cTreme--;
				int q=cTreme;
				if(q>6) q=6;
				xx+=J.RS(q);
				yy+=J.RS(q);
			}		
		}
		public boolean remItemm(String st){ 
			return false;
		}
		public boolean remItemmm(String st){ 
			return false;
		}
		public boolean remItemmmm(String st){ 
			return false;
		}
		public boolean remItemmmmm(String st){ 
			return false;
		}


		public Comp addItemm(String st){
			return this;
		}		
		public Comp addItemmm(String st){
			return this;
		}
		public Comp addItemmmm(String st){
			return this;
		}		
		public Comp addItemmmmm(String st){
			return this;
		}		

		public boolean selectt(String par){		
			return false;
		}		
		public boolean selecttt(String par){		
			return false;
		}
		public boolean selectttt(String par){		
			return false;
		}
		public boolean selecttttt(String par){		
			return false;
		}
		
		
		public String getTextt(){		
			return "";
		}
		public String getTexttt(){		
			return "";
		}		
		public String getTextttt(){		
			return "";
		}				
		public String getTexttttt(){		
			return "";
		}		
		
		public boolean onResize(){return false; } // usei p  w i n  , expandir p outros  c o m p s  se necessario
		
		public boolean trocaItemm(String sta, String stn){		
			return false;
		}		
		public boolean trocaItemmm(String sta, String stn){		
			return false;
		}
		public boolean trocaItemmmm(String sta, String stn){		
			return false;
		}
		public boolean trocaItemmmmm(String sta, String stn){		
			return false;
		}
		
		public boolean onClickItemm(String st){		
			return false;
		}
		public boolean onClickItemmm(String st){		
			return false;
		}		
		public boolean onClickItemmmm(String st){		
			return false;
		}		
		public boolean onClickItemmmmm(String st){		
			return false;
		}		

		public boolean temItemm(String st){		
			return false;
		}
		public boolean temItemmm(String st){		
			return false;
		}		
		public boolean temItemmmm(String st){		
			return false;
		}		
		public boolean temItemmmmm(String st){		
			return false;
		}		
		


		public void reg(KeyEvent k){
			//if(k.getID()==k.KEY_RELEASED) J.regRelease(k); // nao pega!!!!
			//if(k.getID()==k.KEY_PRESSED) J.regPress(k); // esse ateh pega, mas tem q revisar
			// acho q tem um bug aqui em cima
			boolean c = k.isControlDown();
			boolean s = k.isShiftDown();
			int cd = k.getKeyCode();
			
			J.regPress(k); 
			
			
		// copiando... mas eh mais p  t e x t o
			if(c) if(cd==k.VK_INSERT) copiarParaClipBoard();
			if(c) if(cd==k.VK_C) copiarParaClipBoard();
			
			// colando
			if(s) if(cd==k.VK_INSERT) colarDoClipBoard();
			if(c) if(cd==k.VK_V) colarDoClipBoard();			
			
			
			if(cd==k.VK_BACK_SPACE)	cBackSpace = tmp;						
			if(cd==k.VK_SPACE) cSpace = tmp;			
			if(cd==k.VK_ENTER) cEnter = tmp;					
			if(cd==k.VK_DELETE) cTcDel = tmp;					
			if(cd==k.VK_DOWN) cTcDown = tmp;								
			if(cd==k.VK_UP) cTcUp = tmp;											
			if(cd==k.VK_LEFT) cTcLeft = tmp;
			if(cd==k.VK_RIGHT) cTcRight = tmp;
		}
		public void reg(){
			/*			
			if(focado!=null)
			if(focado.tip==ROT)	
				selProxComp();
			
			// caso precisar isolar a selecao, dah p fazer assim
			// mas nao eh muita vantagem isso no editor, talvez num fonte separado sim.
			// o certo eh um flag "selecionavel=false" aqui. 
			// se precisar, depois eu faco
			
			*/			
	
			if(!hid) imp();

			if(!hid) verMouse();
			
			if(cClick==tmp-3)
				if(!J.vou(tip, WIN, PCK))	
					wBut.play();
			
			
			if(cRem>0) { // remocoes devem ser agendadas, do contrario o evento o n C l o s e () de w i n nao aparece.
				cRem--;
				if(cRem==0) rem=true;
				if(tip==POP) mnAberto=false; //  m e n u   p o p U p  //11111111111111
			}	
			if(cBlink>0) cBlink--;

			if(cEnter>0) cEnter--;
			if(cTcDel>0) cTcDel--;
			if(cBackSpace>0) cBackSpace--;
			if(cSpace>0) cSpace--;
			if(cTcLeft>0) cTcLeft--;
			if(cTcRight>0) cTcRight--;
			if(cTcUp>0) cTcUp--;
			if(cTcDown>0) cTcDown--;
			if(cChange>0) cChange--;
			if(cFocus>0) cFocus--;
			if(cLostFocus>0) cLostFocus--;
			if(cClick>0) cClick--;
			if(cCreate>0) cCreate--;
			//if(cTreme>0) cTreme--; // jah foi visto em  r e g T r e m e()
			if(cAviso>0) cAviso--;
			if(cAdd>0) cAdd--;
			if(cClickk>0) cClickk--;
			if(cOpen>0) cOpen--; // p win e qq outro c o m p quando carregado com esse win 
			if(cFecha>0) cFecha--; // p menu e depois p c o m b o b o x

			// "cFecha" acima nao fica muito intuitivo com "cClose" abaixo
			
			if(cClose>0){ // p  w i n,  r e m o v e com atrazo p captar o evento
				cClose--; 
				if(cClose==0) remove();
			}
			
			if(cSobre>0)
			if(ms.dr!=0)
				setFocus();
			
			if(cSobre>tmpTag) {
				stTag=tag;
				if(opDebug) stTag = name;
			}	
			
			if(cAjuste>0){ 
				cAjuste--;
				if(cAjuste==0)
					ajuste();
			}

		}
		public void calcAbs(){
			xx = x;
			yy = y;
			if(pk!=null) {
				xx+=pk.x;
				yy+=pk.y;
			} 			
			if(lk!=null) {
				xx+=lk.x;
				yy+=lk.y;
			}			
			regTreme();
		}
		public void abreMenu(){}
		public boolean openFig(String cam){ return false;	} // esse eh bom adaptar p outros controles.
		public boolean saveFig(String cam){ return false;	} // usei p p i c t u r e, mas dah p adaptar p outros. Precisa?
		public Comp centralizar(){ // mais p win, mas ateh daria p expandir p outros controles
			return this;
		}
		public void aviso(String st){
			// nao existe "r e g A v i s o()"
			// eh feito no "r e g" geral la p cima.
			//tremer(); // 
			stAviso = st; 
			cAviso = tmp*20;
		}
		public void close(){
			setHid(true); // isso ajuda
			cClose=tmp;
			// r e m o v e r a h em breve
		}
		public void imp(){
			J.impRetRel(12,0, x,y,lar,alt);
		}
		public Comp clonar(boolean hor){
				// hor=true cria o clone a dir
				// hor=false cria esse clone a bxo
			// esse clone ficou correto
			// deve ser modelo p clonagem j3d
			// esse proprio metodo jah adiciona ao arr cmp
			Comp c = null;
			switch(tip){				
				case ICO: c = new Icon(); break;
				case PIC: c = new Picture(); break;
				case IMG_BUT: c = new ImageButton(); break;
				case PIC_BUT: c = new PictureButton(); break;
				case TXT_BUT: c = new TextButton(); break;
				case ROT: c = new Rotulo(); break;
				case TXT_BOX: c = new TextBox(); break;
				case WIN: c = new Win(); break;
				case SLD_BAR: c = new SlideBar(); break;
				case CLR_BUT: c = new ColorButton(); break;
				case LST_BOX: c = new ListBox(); break;
				case DIR_LST_BOX: c = new DirListBox(); break;
				case CMB_BOX: c = new ComboBox(); break;
				case DB: c = new DataBase(); break;
				case CHO_LST: c = new ChoiceList(); break;
				case CHE_LST: c = new CheckList(); break;
				case CHE_BOX: c = new CheckBox(); break;
				case PCK: c = new Pack(); break;
				case CLR_CHO: c = new ColorChooser(); break;
				default: J.impErr("!tipo nao implementado: "+tip+"  "+stTip(tip),"JWin.clonar()");
			}
			
			
			c.x = x;
			c.y = y;			
			if(hor) c.x+=lar+6;
			else c.y+=alt+6;
			
			c.lar = lar;
			c.alt = alt;
			c.tip = tip;
			c.cAjuste = tmp;
			c.aln = aln;
			c.crFrente= crFrente;
			c.crFundo=crFundo;
			c.selMin = selMin;
			c.selMax = selMax;
			c.lk = lk;
			c.pk = pk;
			c.cur = cur;
			c.sel = sel;
			c.text = text;
			c.cam=cam;
			c.name= newName(c.tip);
			// c.tag = tag; // esse nao
			c.canTog = canTog;
			c.tog = tog;
			c.blk = blk;
			c.setFocus();
			cmp.add(c);
			return c;
		}
		public void blink(){
			cBlink=tmp;
		}
		public void tremer(){
			cTreme=tmp+tmp;
		}
		public void setList(ArrayList<String> l){
			// usei p   l i s t B o x
			// nao sei se precisarah de  g e t L i s t
		}
		public ArrayList<String> getList(){ return null; } // usei p  l i s t B o x. Precisa expandir p outros??? Se sim, deveria ser usado "+item" e "-item" p outras l i s t a s derivadas. // Usei tb p BD
		public ArrayList<String> getListt(){ return null; } // fazer as outras se precisar
		// J.ArrLstStrEmStr() converte um arrayList de Strings num String puro com #10#13

		public boolean sobre(){
			return cSobre>0;
		}
		public Comp resetSize(){ return this; } // p  j f 1  e  j p g 
		public void refresh(){	// usei p  l i s t b o x  na  r e l i s t a g e m de  arq

			
			// relist() ia ser bom aqui
			// PELO AMOR DE DEUS! 
			// ISSO NAO SE TRADUZ COMO "REFRESCAR"
			// EH "ATUALIZAR"
			
			// o jogo "halo" tb tem um problema irritante
			// muita gente diz "rahlu", o q eh ridiculo.
		} 
		public void setPixel(Color cr, int i, int j){	} // usei p p i c t u r e, mas pode expandir p i c o n se precisar
		public Color getPixel(int i, int j){	return null; } 
		public int getXabs(){ // retorna a posicao a b s o l u t a do componente na tela. Bom p desenhar com o mouse sobre p i c t u r e 
			int i=x;
			if(pk!=null) i+=pk.x;			
			if(lk!=null) i+=lk.x;
			return i;
		}
		public int getYabs(){
			int j=y;
			if(pk!=null) j+=pk.y;			
			if(lk!=null) j+=lk.y;
			return j;
		}		

		public void altTog(){
			tog=false;
			canTog=!canTog;
		}		
		public Comp setBlk(boolean b){ // altERA
			blk=b; // precisou pelo p a c k
			// nao teria um " c C h a n g e " aqui???
			return this;
		}
		public Comp setBlkItm(String i, boolean b){ // altERA
			// este eh p  m e n u s , mas da p adaptar p outros
			return this;
		}		
		public void altBlk(){ // apenas altERNA
			blk = !blk;			
		}
		public void altHid(){ // apenas altERNA
			hid = !hid;			
		}
		public Comp setHid(boolean h){ 
			hid = h;
			if(J.vou(tip,MNU,CMB_BOX)){
				mnAberto=false;
				cFecha=2;
			}	
			return this;
		}		
		public void hide(){ // mais intuitivo. R e m o v e r outras???
			setHid(true);
		}
		public void show(){ // mais intuitivo. R e m o v e r outras???
			setHid(false);
		}		
		public Comp lstArqs(String cm, String fil, String ext){ return this; } // p l i s t B o x 	 e similares
		public boolean onMouseEnter(){
			if(cSobre==1)
			if(ms.naAreaRel(xx,yy,lar,alt))	
				return true;
			return false;
		}
		public boolean onMouseExit(){
			// parece nao funcionar quando o mouse passa muito rapido, mas jah ajuda
			return msExit; // flag interno
		}
		public boolean onMouseMove(){
			// parece funcionar bem
			if(ms.moveu)
			if(ms.naAreaRel(xx,yy,lar,alt))	
				return true;
			
			return false;
		}
		public boolean onBackSpacePress(){ if(cBackSpace==1) return true; return false; }
		public boolean onSpacePress(){ if(cSpace==1) return true; return false; }
		public boolean onEnterPress(){ if(cEnter==1) return true; return false; }
		public boolean onDelPress(){ if(cTcDel==1) return true; return false; }
		public boolean onLeftPress(){ if(cTcLeft==1) return true; return false; }
		public boolean onRightPress(){ if(cTcRight==1) return true; return false; }
		public boolean onUpPress(){ if(cTcUp==1) return true; return false; }
		public boolean onDownPress(){ if(cTcDown==1) return true; return false; }
		public boolean onClose(){
			if(cClose>0){
				cClose=0; 
				remove();
				return true;
			}
			return false;
		}
		public boolean onOpen(){ // usei p win
			if(cOpen==1) return true;
			return false;
		}
		public boolean onAdd(){ // usei p caixas de listagem
			if(cAdd==1) return true;
			return false;
		}
		public boolean onFocus(){
			// quando acabou de receber o foco
			if(cFocus>0){
				cFocus=0;
				return true;
			}	
			return false;			
		}
		public boolean onLostFocus(){
			// quando perdeu o foco
			if(cLostFocus>0) {
				cLostFocus=0;
				return true;
			}	
			return false;			
		}		
		public boolean onChange(){
			if(cCreate>0) return false;
			if(cChange==1) return true;
			return false;
		}
		public boolean onClick(){
			// bug aqui: nao pode chamar mais de uma vez.
			if(cClick==1) {
				cClick=0;
				return true;
			}	
			return false;
		}
		public boolean onRightClick(){
			// bt dir
			if(cClickk==1) return true;
			return false;
		}		
		public boolean onDoubleClick(){ 
			if(dcl){ // assim eh o certo
				dcl=false;
				return true;
			}
			return false;
		}
		public boolean onCreate(){
			// melhor nao colocar change aqui. Jah vi fontes q bugam.
			if(cCreate>0){
				cCreate=0;
				return true;
			}
			return false; 
		}		
		public boolean onClickItem(String st){
			return false; 
			}		
		public boolean onCont(){ // apenas p timer
			if(sel==selMax)
				return true;
			return false;
		}
			
		public Comp setFocus(){
			// cuidado! Se o foco for definido manualmente, lastFocus nao serah definido!
			if(focado!=null) {
				focado.cLostFocus=tmp;
				lastFocus=focado;
			}
			if(cFocus>0) return this; // bug removido??? Parece q sim. 
			focado = this;
			cFocus = tmp;
			J.esc("comp focado: |"+name+"|");
			return this;
		}
		public void verMouse(){
			int xx=x, yy=y;
			if(pk!=null){
				xx+=pk.x;
				yy+=pk.y;
			}			
			if(lk!=null){
				xx+=lk.x;
				yy+=lk.y;
			}

			if(focado==this)
				if(ms.dr!=0)
					if(!blk)
						select(sel+ms.dr);
			
			int al=alt;
			if(tip==CMB_BOX)
				if(!aberto)al=16;

			msExit=false;
			if(ms.naAreaRel(xx,yy,lar,al)) {
				cSobre++;
				if(ms.b1){
				
					// caso especial: soh selecionar win se NAO tiver outro comp sob o mouse
					if(tip==WIN)
					for(Comp c:cmp)
					if(c!=this)	
					if(c.cSobre>0)
						return;
					
					// corrigindo bug
					// if(focado==this) return; // nao corrigiu, inseriu outro;
					
					if(tip!=POP)
					//if(tip!=MNU)
					//if( !(tip==WIN && blk) )	
					if(!opTemMenuAberto)	
						setFocus();
									
					if(!blk)
						if(tip==TXT_BOX)
							selAll();
						
					if(!blk){
						if(cClick<=0)
							if(canTog)
								tog=!tog;							
							
						dcl=false;	// p c l i q u e duplo
						if(cClick>0)	
						if(cClick<tmp*0.8f)	
						//if(cClick<tmp)	// isso era p winXP, no win10 tah descalibrado
						//if(J.noInt(cClick,8,100))	
							dcl=true;
						
						cClick=tmp;		
					}
					
				}	
				if(ms.b2) cClickk=tmp>>1;
			} else {
				if(cSobre>0) msExit = true;
				cSobre = 0;	
			}	
		}
		public Comp setAln(int d){
			if(J.vou(d,J.ESQ,J.CEN,J.DIR))
				aln = d;
			else J.impErr("!alinhamento desconhecido: "+d, "JWin.setAln()");
			cAjuste = 2;
			return this;
		}
		public Comp setName(String st){
			name = J.emMaiusc(st);
			return this;
		}

		public String getName(){
			return name;
		}
		public String getText(){
			return text;
		}		
		public String getCam(){
			if(cam==null) return "";
			return cam;
		}				
		public String getTag(){
			return tag;
		}		
		public String getHint(){
			return hint;
		}				
		public Color getColor(){
			return crFrente; // usei em  c o l o r   b u t t o n
		}
		public int getNumItens(){ 
			return 0; 
		}

		
		public Comp setTag(String st){ // setHint setDica addTag addDica
			if(st!=null)
				if(st.equals(""))
					st=null;
			tag = st;
			return this;
		}			
		public Comp setCam(String st){
			cAjuste = 2;
			cam = st;			
			return this;
		}		
		public Comp setFig(BufferedImage f){
			// setFig seria irma dessa
			return this; // usei em i c o n  e   i m a g e B u t t o n
		}		
		public Comp setIcon(String cm){
			return this;
		}
		public Comp setJf1(Jf1 f){
			// setFig seria irma dessa
			return this; // usei em i c o n  e   i m a g e B u t t o n
		}
		public Jf1 getJf1(){
			return null;
		}
		public Comp setText(String st){
			cAjuste = tmp; // isso eh bom. ... vi um dejavu'
			cChange = tmp;
			text = st;			
			return this;
		}		
		public Comp setColor(String cr, String crr){
			//J.esc("|||||||"+cr+"|||||||||");
			//J.esc("|||||||"+crr+"|||||||||");
			Color c = J.stcEmColor(cr);
			Color cc = J.stcEmColor(crr);
			return setColor(c,cc);
		}
		public Comp setColor(Color cr, Color crr){
			if(cr!=null) crFrente = cr;
			if(crr!=null)crFundo = crr;
			return this;
		}

		public void acClick(){
			cClick=tmp;
		}
		public void acClickItem(String st){} // usei p menu. Parece bom p acionar um item de menu por tecla v k _ b l a b l a
		public void resizeRel(int i, int j){
			cAjuste=tmp;
			lar+=i;
			alt+=j;
		}
		public void resize(int i, int j){ 
			cAjuste=tmp;
			if(i!=-1) lar=i;
			if(j!=-1) alt=j;
		}
		public Comp move(String nm){ 
			// este deve ajudar com packs simulando guias
			// deve mover este controle sobre "nm"
			// repare q nao meche na fila de exibicao
			Comp c = getComp(nm);
			if(c!=null)
				move(c.x, c.y);
			return this; // isso tb deixa pratico p jah dar um "h i d e" na pseudo guia
		}
		public Comp setSize(int l, int a){
			lar = l;
			alt = a;
			cChange = tmp;
			return this;
		}
		public Comp setPos(int p){return this; } // ?Apagar este???
		public Comp setPos(int i, int j){
			x=i; y=j; 
			// ?limites??? Não seria bom ficar muito fora da tela até sumir dela, né??? Depois.
			return this; 
		}
		public void moveW(int i, int j){ 
			// desconta a posicao do win q o contem (caso exista)
			cAjuste=tmp;
			x=i;
			y=j;
			if(lk!=null){
				x-=lk.x;
				y-=lk.y;
			}
		}
		public void move(int i, int j){ // nao seria melhor um "setPos" ou "setXY"?
			cAjuste=tmp;
			if(i!=-1) x=i;
			if(j!=-1) y=j;
		}
		public void moveRel(int i, int j){
			cAjuste=tmp;
			x+=i;
			y+=j;
		}
		public void moveMeio(int i, int j){
			cAjuste=2;
			int al = alt;
			if(J.vou(tip,CMB_BOX,WIN)) al=12;
			x = i - (int)(lar/2f);
			y = j - (int)(al/2f);
			if(pk!=null){
				x-=pk.x;
				y-=pk.y;
			}			
			if(lk!=null){
				x-=lk.x;
				y-=lk.y;
			}
			if(opMovGrid){
				x-= x%opTamGrade;
				y-= y%opTamGrade;
			}
		}

		public void remove(){
			// eliminar uma win por aqui faz o evento  c l o s e  nao ser registrado. Use apenas se nao for usar  o n C l o s e () (mas vai ser dificil lembrar disso  :(  ) CUIDADO!!!
			// if(tip==WIN) cRem=3; //???
			// else rem = true; //???
			
			rem=true; //???
			
			for(Comp c:cmp)
				if(c.lk==this)
					c.rem=true;
			if(tip==PCK)
				for(Comp c:cmp)
					if(c.pk==this)
						c.rem=true;
		}
		public void ajuste(){}
		public boolean temItem(String st){return false; }
		public Comp addArq(String cm){ return this; }
		public boolean trocaItem(String a, String n){ return false; }		
		public Comp addItemSNT(String st){ return this; }// SNT = "se nao tem", usei em  l i s t B o x
		public Comp addItem(String st){ return this; }
		public Comp addItem(boolean v, String st){ return this; }
		public Comp addItens(String st){ return this; }// varios separador por virgula. Usei em l i s t B o x  e derivados.
		public Comp addItens(ArrayList<String> st){ return this; }
		public Comp addItens(String[] st){ return this; } // usei p adicionar lista de arquivos em listBox e similares
		public Comp clear(){ return this; }		
		public boolean remItem(String st){ return false; }
		public boolean remItem(int i){ return false; }// util p caixas de listagem, expandir p outros controles depois
		public boolean remItem(){ return false; } // deve remover o item selecionado
		public void selAll(){
			if(text!=null){
				selMin=0;
				selMax=text.length()-1;
			}
		}

		public boolean select(String col, String val){ return false; } // usei no d a t a B a s e 
		public boolean select(String st){ return false; }		
		public void select(int i){}		


		public boolean getVal(int i){return false; } // usei p c h e c k L i s t	; ?precisa expandir p  c h o i c e  tb ???
		public boolean getVal(String st){return false; } // usei p c h e c k L i s t		
		public int getVal(){ return sel; } // usei em s l i d e B a r. Seria util em outro controle?
		public Comp setVal(Color cr){ return this; } // usei em p a l e t a
		public Comp setVal(int i){ return this; } // usei em c h o i c e   l i s t; Deve marcar como true somente esse item, os demais ficam false. Usei tb p s l i d e b a r
		public Comp setVal(boolean v, int i){ return this; } // pra c h e c k L i s t
		public Comp setVal(boolean v, String st){ return this; } // pra c h e c k L i s t; pode espandir p cho depois
		public Comp altVal(int i){ return this; } // pra c h e c k L i s t
		public void selAnt(){
			select(sel-1);
		}
		public void selProx(){
			select(sel+1);
		}		
		public void selPrim(){
			select(0);
		}
		public void selUlt(){			
			select(-2);
		}		

		
		public String getScript(){
			int xx=x, yy=y;
			if(pk!=null){
				// xx+=pk.x;
				// yy+=pk.y;
			}
			String st="";
			st+=stTip(tip)+" ";
			st+=name+" ";
			st+=J.intEmSt0000(xx)+" ";
			st+=J.intEmSt0000(yy)+" ";
			st+=J.intEmSt0000(lar)+" ";
			st+=J.intEmSt0000(alt)+":";
			if(tip==CHE_BOX) if(tog)st+=" +"; else st+=" -";
			if(canTog) if(tip!=CHE_BOX) st+="~"; 
			if(blk) st+="!";
			if(opMultiLine) st+="%";
			if(aln==J.DIR) st+=">";
			if(aln==J.CEN) st+="^";
			return st;
		}
	}

// === ICONES ===========================

	public Comp addIcon(String cm, int i, int j){
		cAddComp=tmp;
		Comp c = new Icon();
		if(lastWin!=null) c.lk=lastWin;
		
		Comp p = packEm(i,j);
		if(p!=null)	{
			c.pk=p;		
			i-=p.x;
			j-=p.y;
		}	
		
		c.cCreate = tmp;
		c.tip = ICO;
		c.setName(newName(c.tip));
		c.crFrente = null;
		c.crFundo = null;		
		c.x = i; c.y=j;		
		c.lar = c.alt = 40; // sempre double size?
		c.cam = cm;
		c.cAjuste = tmpArq;
		cmp.add(c);
		lastComp=c;		
		este=c;
		return c;
	}
	public class Icon extends Comp{
			Jf1 fig = null;
		public void imp(){
			calcAbs();
			 			

			if(fig!=null){
				if(!blk){
					if(crFrente!=null)
						fig.impMask(crFrente, xx,yy,xx+lar,yy+alt);						
					else 
						fig.impJf1(xx,yy,xx+lar,yy+alt);
				} else
					fig.impMask(crBlk, xx,yy,xx+lar,yy+alt);
			}	
			if(focado==this) impFocus(xx,yy,lar,alt);
			if(blk)	impContBlk(xx,yy,lar,alt);			
		}
		public void ajuste(){
			cAjuste=0;
			if(fig==null)
			if(cam!=null){
				String st = J.corrCam(cam,"jf1");
				if(J.arqExist(st)){
					fig = new Jf1(st);
					fig.zoom=2;
				} else {
					fig = new Jf1(10);
				}
			}
		}
		public Comp resetSize(){
			resize(22,22);
			return this;
		}
		public Comp setCam(String cm){
			super.setCam( J.corrCam(cm,"jf1"));
			fig=null;
			cAjuste = 2;
			return this;
		}		
		public Comp setJf1(Jf1 f){
			fig = f; // instantaneo
			return this;
		}
		public Jf1 getJf1(){
			return fig;
		}
		public String getText(){
			return cam;
		}
		public String getScript(){
			String st = super.getScript()+" ";
			st+=cam;
			return st;
		}
	}
// === TIMER ============================

	public Comp addTimer(String tm, int i, int j){
		cAddComp=tmp;		
		Comp c = new Timer();
		if(lastWin!=null) c.lk=lastWin;
		
		Comp p = packEm(i,j);
		if(p!=null)	{
			c.pk=p;		
			i-=p.x;
			j-=p.y;
		}	
		
		c.cCreate = tmp;
		c.tip = TIM;
		c.setName(newName(c.tip));
		c.x = i; c.y=j;		
		c.lar = c.alt = 40; 
		c.text = tm;
		c.cAjuste = tmpArq;
		cmp.add(c);
		lastComp=c;		
		este=c;
		return c;
	}
	public class Timer extends Comp{
			// fig eh o icone fixo do timer. Dah p ocultar se quizer.
			// o limite de tempo eh dado por "selMax"
			// o tempo atual incrementado por ciclo eh dado por "sel"
			// selMin ficarah sempre zero e nao eh usado.
			// "setText()" altera o intervalo, que deve ser um string conversivel em int
			Jf1 fig = null;
			boolean foi = false;
		public void reg(){
			super.reg();
			if(sel!=-1) sel++;
			foi = false;
			if(sel>selMax) sel = 0;
		}	
		public void imp(){
			calcAbs();
			 			

			if(fig!=null){
				if(!blk) fig.impJf1(xx,yy,xx+lar,yy+alt);
				else fig.impMask(crBlk, xx,yy,xx+lar,yy+alt);
			}	
			if(focado==this) impFocus(xx,yy,lar,alt);
			if(blk)	impContBlk(xx,yy,lar,alt);			
		}
		public void ajuste(){
			cAjuste=0;
			selMin=0;
			selMax = J.stEmInt(text);
			sel = 0;
			fig = new Jf1("timer01.jf1"); 
		}
		public String getText(){
			return J.intEmSt(selMax);
		}
		public String getScript(){
			String st = super.getScript()+" ";
			st+= J.intEmSt(selMax);
			return st;
		}
	}
	
// === PICTURE ==========================

	public Comp addPicture(String cm, int i, int j, int ii, int jj){
		cAddComp=tmp;				
		Comp c = new Picture();
		if(lastWin!=null) c.lk=lastWin;		
		
		Comp p = packEm(i,j);
		if(p!=null)	{
			c.pk=p;		
			i-=p.x;
			j-=p.y;
		}	

		c.cCreate = tmp;		
		c.tip = PIC;
		c.setName(newName(c.tip));
		c.x = i; c.y=j;
		c.lar = ii; // todas as coords devem ser RELATIVAS
		c.alt = jj;
		c.cam = cm;
		c.cAjuste = tmpArq;
		cmp.add(c);
		lastComp=c;		
		este=c;		
		return c;
	}
	public class Picture extends Comp{
			BufferedImage fig = null;
			int minX=0, minY=0, maxX=larArq, maxY=altArq;
		public void imp(){
			calcAbs();
			if(fig!=null)
				J.g.drawImage(fig, xx,yy,xx+lar,yy+alt, minX,minY,maxX,maxY,null);
				// J.impFig(fig, xx,yy,xx+lar,yy+alt, larArq, altArq);
			 				
			if(focado==this) impFocus(xx,yy,lar,alt);
			if(blk)	impContBlk(xx,yy,lar,alt);			
		}
		public void ajuste(){
			cAjuste=0;
			if(fig==null)
			if(cam!=null){
				String st = J.corrCam(cam);
				if(J.arqExist(st)){
					fig = J.carrBFig(st); // altera larPng e altPng 
					larArq = J.larPng;
					altArq = J.altPng;
					minX=0; minY=0; // usei p impressao de areas 
					maxX=larArq; maxY=altArq;
					cOpen=tmp;
				} else {
					fig = J.carrBFig("erro01.png");
					J.esc("arquivo perdido: "+st);
				}
			}
		}
		public Comp resetSize(){
			// nao funciona antes de "ajuste"
			//   ser acionado na abertura.
			// deve ser melhor lapidado.
			// poderia ser usado p redimensionar b o t o e s de t e x t o ao tamanho padrao
			if(tip==PIC)
				resize(larArq,altArq);			
			return this;
		}
		public Comp setCam(String cm){
			cam = null;
			fig = null;
			super.setCam(cm);
			cAjuste=2;
			return this;
		}
		public String getScript(){
			String st = super.getScript()+" ";
			st+=cam;
			return st;
		}
		public int getXabs(){
			int i=x;
			if(pk!=null) i+=pk.x;			
			if(lk!=null) i+=lk.x;
			return i;
		}
		public int getYabs(){
			int j=y;
			if(pk!=null) j+=pk.y;			
			if(lk!=null) j+=lk.y;
			return j;
		}		
		public Comp setFig(BufferedImage f){
			// instantaneo, irma de s e t J f 1
			fig = f;
			return this; 
		}		
		public boolean openFig(String cam){
			// carregamento agendado
			setCam(cam); 
			if(J.arqExist(cam)) return true;
			return false;
		}
		public boolean saveFig(String cam){ 
			if(J.saveBFig(fig, cam))
				return true;
			return false;	
		}
		public void setPixel(Color cr, int i, int j){
			if(!blk) // precisei p editor
			if(fig!=null)
			if(cr!=null)
			if(i>=0)
			if(j>=0)
			if(i<larArq)
			if(j<altArq)
				fig.setRGB(i,j, cr.getRGB());
		}
		public Color getPixel(int i, int j){
			if(fig!=null)
			if(i>=0)
			if(j>=0)
			if(i<larArq)
			if(j<altArq)  // sem alfa por enquanto
				return new Color(fig.getRGB(i,j));
			return null;
		}
	}
	
// === BOTAO DE IMAGEM ==================
/*	INSTRUCOES
O QUE EH MULTISTATE:
	A cada clique sobre o botao, sua imagem muda e o valor "sel" eh incrementado em ciclo: ao chegar ao valor maximo, volta p zero. Este conceito tb eh aplicado a botoes de texto. 
	Veja q os varios caminhos de imagem nao sao atribuidos em "cam", mas sim em "text". O Editor nao possui interface p este mecanismo (ao menos ateh agora), logo, ha de se fazer isto diretamente no codigo.
	
COMO FAZER:
para fazer multiState, escreva: (repare q NAO EH EM *CAM*)
  	text="apagar01.jf1; badb01.jf1; #119.jf1";
lembre q tb eh possivel usar:
		text="#69.jf1; #89.jf1; #119.jf1";
nesta forma acima, uma bolinha com a cor correspondente da paleta padrao eh atribuida. Veja q nao existe este arq, faz-se apenas referencia a cor. Isso eh util quando se estah criando um botao p se desenhar o icone depois.
*/
	public Comp addImageButton(String cm){
		cAddComp=tmp;				
		// eh barra de ferr horizontal?
		int q=24+2;
		if(opDoubleSizeJf1) q=48+2;
		
		Comp c = addImageButton(cm, xIns, yIns);
		
		boolean hor = true;
		if(lastWin!=null)
			if(lastWin.lar<lastWin.alt) 
				hor=false;
			
		if(hor)	xIns+=q;
		else yIns+=q;	
		
		return c;
	}
	public Comp addImageButton(String cm, int i, int j){
		cAddComp=tmp;				
		int q=22;
		if(opDoubleSizeJf1)	q = 42;		
		return addImageButton(cm,i,j,q,q);		
	}
	public Comp addImageButton(String cm, int i, int j, int ii, int jj){
		cAddComp=tmp;				
		Comp c = new ImageButton();
		if(lastWin!=null) {
			c.lk=lastWin;		
			lastWin.cAjuste=tmp;
		}	
		
		Comp p = packEm(i,j);
		if(p!=null)	{
			c.pk=p;		
			i-=p.x;
			j-=p.y;
		}	
				
		c.cCreate = tmp;				
		c.tip = IMG_BUT;
		c.setName(newName(c.tip));
		c.crFrente = null;
		c.crFundo = null;
		c.x = i; c.y=j;		
		if(opInsTog)c.canTog=true;
		c.lar = ii;
		c.alt = jj;
		if(!J.tem(';',cm)) c.setCam(cm);
		else {
			c.text = cm;
			c.setCam(J.palavraK(0,cm));
		}
		c.cAjuste = tmpArq;
		cmp.add(c);
		lastComp=c;		
		este=c;		
		return c;		
	}
	public class ImageButton extends Comp{
			Jf1 fig = null;
		public Comp setOption(String st){
			// mais opcoes depois
			if(fig==null)	return this;
			
			if(J.tem("0",st)){
				fig.opMod=0;
				fig.opMod=0;
			}	
			if(J.tem("h",st))	fig.opMod=fig.INV_HOR;
			if(J.tem("v",st))	fig.opMod=fig.INV_VERT;
			if(J.tem("d",st))	fig.opMod=fig.ROT_DIR;
			if(J.tem("e",st))	fig.opRot=fig.ROT_ESQ;
			return this;
		}		
		public void imp(){
			int e=0, d=0;
			Color 
				cr=crBt,
				crl=crLuz,
				crs=crSomb;

			int est=LIVRE;
			if(cSobre>0) {d=-1; cr=crLuz; est=SOBRE; }
			if(cClick>0 || tog) {d=+1; cr=crBox; est=PRESS; }
			if(blk) {d=0; cr=crBt; }

			calcAbs();
			impCx(cr, crl, crs, xx+d,yy+d,lar,alt,est);
			 			

				
			if(fig!=null) {
				if(!blk) {
					if(crFrente!=null) fig.impMask(crFrente, xx+d+1,yy+d+1,xx+d-1+lar,yy+d-1+alt);
					else fig.impJf1(xx+d+1,yy+d+1,xx+d-1+lar,yy+d-1+alt);

					{ // checando multiState
						String st=text;
						if(J.tem(';',st)) {
							
							if(cClick==1) {						
								sel++; 
								if(sel>=J.numPalavras(text)) sel=0; 
							}					
							
							st = J.palavraK(sel,st);
							st = J.remChar(';',st);					
							
							if(cClick==1) {
								cam = st;
								fig=null;
								cAjuste=1;
							}
						}					
						
					}
					
				}	else {
					fig.impMask(crLuz, xx+d+2,yy+d+2,xx+d+lar,yy+d+alt);
					fig.impMask(crBlk, xx+d+1,yy+d+1,xx+d-1+lar,yy+d-1+alt);
				}	
			}	
// if(focado==this)			
// impText(400,32,J.cor[12], ""+((yy+d+1)-(yy+d-1+alt)));
			if(focado==this) impFocus(xx+d,yy+d,lar,alt);
			if(blk) impContBlk(xx,yy,lar,alt);
		}
		public Comp resetSize(){
			resize(22,22);
			return this;
		}
		public void ajuste(){
			cAjuste=0;
			if(fig==null)
			if(cam!=null){
				
				{ // truques uteis, quando se quer definir o icone depois, ou nao se tem um disponivel ainda.
					String stc = J.nomeArq(cam);
					if(stc!=null)
					if(stc.charAt(0)=='#'){
						String st = J.nomeArq(cam);
						st = J.remPrimChar(st);
						int num = J.stEmInt(st);
						fig = new Jf1("ball05.jf1");
						fig.tingir(99,num); // isso ficou bom
						fig.zoom= (opDoubleSizeJf1?2:1);					
						return;
					}
				}

				boolean opv=false, oph=false;
				// nao sei se isso tah funcionando. Conferir depois
				if(J.tem('^',cam)) { opv=true; cam = J.remChar('^',cam); }
				if(J.tem('>',cam)) { oph=true; cam = J.remChar('>',cam); }
				if(J.tem('<',cam)) { oph=true; cam = J.remChar('<',cam); }
				cam = J.limpaSt(cam);
				
				String st = J.corrCam(cam,"jf1");
				if(J.arqExist(st)){
					fig = new Jf1(st);
					fig.zoom= (opDoubleSizeJf1?2:1);
					
					if(oph)fig.opMod=fig.INV_HOR;
					if(opv)fig.opInv=fig.INV_VERT;
					
				} else {
					fig = new Jf1(12);
					J.esc("FALHA: arquivo perdido: |"+st+"|");
				}
			}
		}		
		public String getCam(){
			if(fig!=null)
				return fig.cam;
			return "???";
		}
		public Comp setCam(String cm){
			super.setCam( J.corrCam(cm,"jf1"));			
			fig=null;
			cAjuste=2;
			return this;
		}
		public Comp setIcon(String cm){ // esse eh mais intuitivo
			return setCam(cm);
		}
		public Comp setJf1(Jf1 f){
			fig = f; // instantaneo
			return this;
		}
		public Jf1 getJf1(){
			return fig;
		}
		public String getScript(){
			String st = super.getScript()+" ";
			st+=cam;
			if(fig!=null){
				if(fig.opInv==1) st+=" >"; // rotacoes depois
				if(fig.opInv==2) st+=" ^"; // mas qual caracter???
			}
			return st;
		}
	}

// === BOTAO PICTURE	
		BufferedImage fAcrilico = J.carrBFig("brilho01.png");
		boolean opEfAcrilico = true; // efeito sobre este botao abaixo. Ficou bom em JExplorer.
	public Comp addPictureButton(String cm){
		return addPictureButton(cm,xIns,yIns,40,40);		
	}
	public Comp addPictureButton(String cm, int i, int j, int ii, int jj){
		cAddComp=tmp;				
		Comp c = new PictureButton();
		if(lastWin!=null) {
			c.lk=lastWin;		
			lastWin.cAjuste=tmp;
		}	
		
		Comp p = packEm(i,j);
		if(p!=null)	{
			c.pk=p;		
			i-=p.x;
			j-=p.y;
		}	
				
		c.cCreate = tmp;				
		c.tip = PIC_BUT;
		c.setName(newName(c.tip));
		c.crFrente = null;
		c.crFundo = null;
		c.x = i; c.y=j;		
		if(opInsTog)c.canTog=true;
		c.lar = ii;
		c.alt = jj;
		c.setCam(cm);
		c.cAjuste = tmpArq;
		cmp.add(c);
		lastComp=c;		
		este=c;		
		return c;		
	}
	public class PictureButton extends Comp{
			BufferedImage fig = null;
			int minX=0, minY=0, maxX=larArq, maxY=altArq;			
		public void imp(){			
			int e=0, d=0;
			Color 
				cr=crBt,
				crl=crLuz,
				crs=crSomb;

			int est=LIVRE;
			if(cSobre>0) {d=-1; cr=crLuz; est=SOBRE; }
			if(cClick>0 || tog) {d=+1; cr=crBox; est=PRESS; }
			if(blk) {d=0; cr=crBt; }			

			calcAbs();
			impCx(cr, crl, crs, xx+d,yy+d,lar,alt, est);
			 			

				
			if(fig!=null) {
				J.g.drawImage(fig, xx+d,yy+d,xx+lar+d,yy+alt+d, minX,minY,maxX,maxY,null);
				if(blk) J.impRetRel(J.altAlfa(crBox,127),null, xx+d+1,yy+d+1,xx+d-1+lar,yy+d-1+alt);
			}
			if(opEfAcrilico){
				J.g.drawImage(fAcrilico, xx+d,yy+d,xx+lar+d,yy+alt+d, minX,minY,maxX,maxY,null);
			}
// if(focado==this)			
// impText(400,32,J.cor[12], ""+((yy+d+1)-(yy+d-1+alt)));
			if(focado==this) impFocus(xx+d,yy+d,lar,alt);
			if(blk) impContBlk(xx,yy,lar,alt);
		}
		public Comp resetSize(){
			resize(22,22);
			return this;
		}
		public void ajuste(){
			cAjuste=0;
			if(fig==null)
			if(cam!=null){
				String st = J.corrCam(cam);
				if(J.arqExist(st)){
					fig = J.carrBFig(st); // altera larPng e altPng 
					larArq = J.larPng;
					altArq = J.altPng;
					minX=0; minY=0; // usei p impressao de areas 
					maxX=larArq; maxY=altArq;
					cOpen=tmp;
				} else {
					fig = J.carrBFig("erro01.png");
					J.esc("arquivo perdido: "+st);
				}
			}
		}		
		public String getCam(){
			if(fig!=null)
				return cam;
			return "???";
		}
		public Comp setCam(String cm){
			super.setCam( J.corrCam(cm,"png")); // acho melhor usar sempre png
			fig=null; 
			cAjuste=2;
			return this;
		}
		public String getScript(){
			String st = super.getScript()+" ";
			st+=cam;
			return st;
		}
	}

// === SHAPE3D ==========================

	public Comp addShape3d(String cm, int i, int j, int ii, int jj){
		cAddComp=tmp;		
		
		// abaixo eh pq esse comp numa aplicacao 3d gera um bug de camera.
		// ajustar depois. Deveria ser implementado um array de cameras.
		if(!opAjustarCam3d) {
			J.esc("AVISO: JWin.Shape3d nao foi criado p evitar um bug de camera");
			return null;
		}	
		
		Comp c = new Shape3d();
		if(lastWin!=null) {
			c.lk=lastWin;		
			lastWin.cAjuste=tmp;
		}	
		
		Comp p = packEm(i,j);
		if(p!=null)	{
			c.pk=p;		
			i-=p.x;
			j-=p.y;
		}	
				
		c.cCreate = tmp;				
		c.tip = J3D;
		c.setName(newName(c.tip));
		c.crFrente = null;
		c.crFundo = null;
		c.x = i; c.y=j;		
		c.lar = ii;
		c.alt = jj;
		c.setCam(cm);
		c.cAjuste = tmpArq;
		cmp.add(c);
		lastComp=c;		
		este=c;		
		

		if(opAjustarCam3d){
			J.iniCam();
			J.posCam(0.4f,0,0);
			J.incZ=0;
			J.zoom=760;
			J.zCam=0;
		}

		
		
		return c;		
	}
	public class Shape3d extends Comp{ // ss j3d
			J3d j3d = null;
		public void imp(){
			calcAbs();

			impCx(null,crSomb,crLuz, xx,yy,lar,alt,LIVRE);
			 			
			
			if(j3d!=null){ // meio gambiarrento mas funciona
				int x_=J.xPonto;
				int y_=J.yPonto;
				J.xPonto = xx+(lar>>1);
				J.yPonto = (int)(yy+alt*0.8f);
				j3d.giraTudo(0.03f);
				j3d.reg3d();
				j3d.impJ3d(0,0,0);
				J.incZ=10;				
				J.xPonto = x_;
				J.yPonto = y_;
			}	
			
			if(focado==this) impFocus(xx,yy,lar,alt);
			if(blk)	impContBlk(xx,yy,lar,alt);						
		}
		public Comp setCam(String cm){
			cam = cm;
			cAjuste = tmp;
			cChange = tmp;
			return this;
		}
		public void ajuste(){
			cAjuste = 0;

			cam = J.corrCam(cam,"j3d");
			if(J.arqExist(cam)){
				j3d = new J3d(cam,0.005f);
			} else {
				J.esc("FALHA: arquivo perdido: |"+cam+"|");
			}

		}
		public String getScript(){
			String st = super.getScript()+" ";
			st+=cam;
			return st;
		}		
	}
	
	
// === BOTAO DE TEXTO COMUM =============	
	
	public Comp packEm(int i, int j){
		int m=0, n=0, mm=0, nn=0;
		for(Comp c:cmp)
		if(c.tip==PCK){
			m=c.x;
			n=c.y;
			// if(c.lk!=null){ // nao precisa
				// m-=c.lk.x; // mesmo???
				// n-=c.lk.y;
			// }
			mm=m+c.lar;
			nn=n+c.alt;
			if(J.noInt(i, m,mm))
			if(J.noInt(j, n,nn))
				return c;
			
		}
		return null;
	}
	public Comp addTextButton(String st, int i, int j, int ii, int jj){
		cAddComp=tmp;				
		Comp c = new TextButton();
		if(lastWin!=null) c.lk=lastWin;		
	
		Comp p = packEm(i,j);
		if(p!=null)	{
			c.pk=p;		
			i-=p.x;
			j-=p.y;
		}	
		
		c.cCreate = tmp;		
		c.tip = TXT_BUT;
		if(opInsTog)c.canTog=true;		
		c.setName(newName(c.tip));
		c.x = i; c.y=j;		
		c.lar = ii;
		c.alt = jj;
		c.setText(st);
		c.cAjuste = 2;
		cmp.add(c);
		lastComp=c;		
		este=c;		
		return c;		
	}
	public class TextButton extends Comp{
			int desX=0, desY=0;
		public void reg(KeyEvent k){
			super.reg(k);
			
			if(k.getKeyCode()==k.VK_ENTER){
				cChange=tmp;
				cEnter = tmp;
				cClick = tmp; // ?um cPress seria melhor?
			}	
			if(k.getKeyCode()==k.VK_DELETE){
				cChange=tmp;
				cTcDel = tmp;
				//cClick = tmp; // pq isso???
			}				
			if(k.getKeyCode()==k.VK_SPACE)
			if(opBtRespondeTecEspaco){
				cChange=tmp;
				cSpace = tmp;
				cClick = tmp;
			}				
		}
		public void imp(){			
		
			Color
				cr=crBt,
				crt=crText,
				crl=crLuz,
				crs=crSomb;
			int e=0, d=0;

			int est=LIVRE;
			if(cSobre>0) {d=-1; cr=crLuz; est=SOBRE;}
			if(cClick>0 || tog) {d=+1; cr=crBox; est=PRESS;}
			if(blk) {d=0; cr=crBt; crt=crSomb; crs=crSomb; }
			
			calcAbs();			
			impCx(cr, crl, crs, xx+d,yy+d,lar,alt, est);
			 			

			if(!opMultiLine) {
				String st=text;
				if(J.tem(';',st)) { // tratando multi state
					
					if(cClick==1) {						
						sel++; 
						if(sel>=J.numPalavras(text)) sel=0; 
					}					
					
					st = J.palavraK(sel,st);
					st = J.remChar(';',st);					
					
					if(cClick==1) {
						desX = (int)(lar/2f - J.larText(st)/2f);
					}
				}
				impText(xx+desX+d, yy+desY+10+d, crt, st);
			} else {
				String[] ls = text.split("_");
				int c=-1, altLin=16;
				int q=0, qq=0;
				qq = 1+J.contChar('_',text);
				qq = (alt>>1)-(qq*8);
				for(String s:ls){
					// mas o "_" nao tah sendo descontado, por isso o texto nao estah exatamente no meio
					c++;
					q = (lar>>1)-(J.larText(s)>>1)-3; // esse 3 eh a meia largura do "_"
					impText(xx+q, yy+qq+12+c*altLin, crt, s);
				}
			}			
			if(focado==this) impFocus(xx+d,yy+d,lar,alt);
			if(blk)	impContBlk(xx,yy,lar,alt);						
		}
		public Comp setText(String st){
			if(st!=null)
			if(st.length()>0)
			if(st.charAt(0)=='_') {
				opMultiLine=true;
				st = J.remPrimChar(st);
			}				
			super.setText(st);
			ajuste(); 
			return this;
		}
		public void ajuste(){
			cAjuste=0;
			String st=text;
			if(opMultiLine) {
				desY=0;
				desX=0;
			} else {
				
				if(J.tem(';',st)){
					sel=0;
					st = J.palavraK(sel,st);
					st = J.remChar(';',st);						
				}
				
				desX = (int)(lar/2f - J.larText(st)/2f);
				desY = (int)(alt/2f - 6);
			}
		}		
		public void acClick(){
			cClick = tmp;
		}
		public String getScript(){
			String st = super.getScript();
			if(tip!=CHE_BOX) st+=" "; // estranho, mas cheBox eh derivado de textButton
			if(opMultiLine)st+='_';
			st+=text;
			return st;
		}
	}

// === ROTULO ============================
	public Comp addRotulo(String st, int i, int j, int ii, int jj){
		// dica: -1 em ii = auto ajuste, de acordo com o tamanho do texto
		//  -1 em jj faz a altura de 1 linha (=16) (mas nao calcula multiline)
		// parece ser bom extender este mecanismo p outros controles. Depois.
		if(st.length()>0)
		if(st.charAt(0)==94) st = J.remPrimChar(st); // removendo bug na marra
		
		cAddComp=tmp;				
		Comp c = new Rotulo();
		c.crFundo=crBt; // assim dah p mudar. Usei p cx  c o n f i r m 
		if(lastWin!=null) c.lk=lastWin;		
		
		Comp p = packEm(i,j);
		if(p!=null)	{
			c.pk=p;		
			i-=p.x;
			j-=p.y;
		}	
				
		c.cCreate = tmp;				
		c.tip = ROT;
		c.setName(newName(c.tip));
		c.x = i; c.y=j;		
		if(ii==-1) ii = J.larText(st);
		c.lar = ii;
		if(jj==-1) jj=12;
		c.alt = jj;
		c.aln=J.ESQ;
		if(J.tem('^',st)) c.aln=J.CEN;
		if(J.tem('>',st)) c.aln=J.DIR;
		st = J.trocaChar('^',' ',st);
		st = J.trocaChar('>',' ',st);
		st = J.trocaChar('<',' ',st); // padrao
		c.setText(st);
		c.cAjuste = tmpArq;
		cmp.add(c);
		lastComp=c;		
		este=c;		
		return c;		
	}
	public class Rotulo extends Comp{
			int desX=0, desY=0; // posso usar depois p alinhamento
			
			
		public void imp(){			
			calcAbs();
			
			Color crt=crText;
			if(blk)crt=crSomb;
			
			// if(opMultiLine) desX=0;
			
			J.impRetRel(crBt,null, xx,yy,lar,alt); 
			//J.impRetRel(crFundo,null, xx,yy,lar,alt); 
			 			
			
		
			if(!opMultiLine) {
				impText(xx+desX, yy+desY+12, crt, text);				
			} else {
				String[] ls = text.split("_");
				int c=-1, altLin=16;
				for(String s:ls){
					c++;
					impText(xx+desX, yy+desY+12+c*altLin, crt, s);
				}
			}
			
			if(focado==this) impFocus(xx,yy,lar,alt);			
			if(blk)	impContBlk(xx,yy,lar,alt);						
		}
		public void ajuste(){
			cAjuste=0;
			switch(aln){
				case J.ESQ: 
					desX=0;
					break; // normal, a esq, ou char "<"
				case J.CEN: // char "^"
					desX = (int)(lar/2f - J.larText(text)/2f);
					// desY = (int)(alt/2f - 6);
					break;
				case J.DIR: // char ">"
					desX = (int)(lar - J.larText(text));
					break;					
				default: J.impErr("!alinhamento desconhecido: "+aln,"JWin.Rotulo.ajuste()");
			} 
			if(opMultiLine) desX=0;
			
		}		
		public String getScript(){
			String st = super.getScript()+" ";
			if(opMultiLine)st+='_';
			st+=text;
			return st;
		}
		public Comp setText(String st){
			if(st!=null)
			if(st.length()>0)
			if(st.charAt(0)=='_') {
				opMultiLine=true;
				st = J.remPrimChar(st);
			}				
			super.setText(st);
			ajuste(); // calibragem fina. Ajudou na calculadora.
			return this;
		}
	}

// === CAIXA DE T E X T O	===============

	public Comp addTextBox(String st){ 
		Comp c = addTextBox(st,xIns,yIns,33,16);
		yIns+=20;
		return c;		
	}
	public Comp addTextBox(String st, int i, int j, int ii, int jj){
		cAddComp=tmp;				
		Comp c = new TextBox();
		if(lastWin!=null) c.lk=lastWin;		
		
		Comp p = packEm(i,j);
		if(p!=null)	{
			c.pk=p;		
			i-=p.x;
			j-=p.y;
		}	
				
		c.cCreate = tmp;		
		c.tip = TXT_BOX;
		c.cur=0;
		c.setName(newName(c.tip));
		c.x = i; c.y=j;
		if(jj==-1) jj=16; // de rot era 12, mas assim fica melhor aqui
		c.lar = ii;
		c.alt = jj;
		c.setText(st);
		c.cAjuste = tmpArq;
		cmp.add(c);
		lastComp=c;		
		este=c;		
		return c;		
	}
	public class TextBox extends Comp{
			int altLin=16;
			boolean barV=false, barH=false; // barras de rolagem			

	public void zAcentos(){
		opLeuAcentoAgudo=false;	
		opLeuCrase=false;
		opLeuTil=false;
		opLeuCircunflexo=false;		
	}
	public char corrAcentos(int cd, char ca){
		if(cd==(char)129){ // acento agudo ou grave, se shift
			if(J.shiftOn) {zAcentos(); opLeuCrase=true;}
			else {zAcentos(); opLeuAcentoAgudo=true;}
			wAcento.play();
			return 0;
		}
		if(cd==(char)131) { // til ou circunflexo se shift
			if(J.shiftOn) {zAcentos(); opLeuCircunflexo=true;}
			else {zAcentos(); opLeuTil=true;}
			wAcento.play();
			return 0;				
		}			
		if(opLeuAcentoAgudo){
			if(ca=='a') ca = (char)225;
			if(ca=='A') ca = (char)193;
			if(ca=='e') ca = (char)233;
			if(ca=='E') ca = (char)201;
			if(ca=='i') ca = (char)237;
			if(ca=='I') ca = (char)205;
			if(ca=='o') ca = (char)243;
			if(ca=='O') ca = (char)211;
			if(ca=='u') ca = (char)250;
			if(ca=='U') ca = (char)218;
			opLeuAcentoAgudo=false;
			return ca;			
		}
		if(opLeuTil){
			if(ca=='a') ca = (char)227;
			if(ca=='A') ca = (char)195;
			if(ca=='o') ca = (char)245;
			if(ca=='O') ca = (char)213;
			opLeuTil=false;
			return ca;
		}
	if(opLeuCrase){
			if(ca=='a') ca = (char)224;
			if(ca=='A') ca = (char)192;
			opLeuCrase=false;
			return ca;			
		}		
		if(opLeuCircunflexo){
			if(ca=='a') ca = (char)226;
			if(ca=='A') ca = (char)194;
			if(ca=='e') ca = (char)234;
			if(ca=='E') ca = (char)202;
			if(ca=='o') ca = (char)244;
			if(ca=='O') ca = (char)212;
			opLeuCircunflexo=false;
			return ca;			
		}			

		return ca;
	}

		public void reg(KeyEvent k){			
			if(hid) return;
			super.reg(k);
			
			boolean semSel= selMax==-1;
			boolean temSel=!semSel;
			
			int cd = k.getKeyCode();
			char ca = k.getKeyChar();
			ca = corrAcentos(cd,ca);
			if(ca==0) return;
			
			boolean s = k.isShiftDown();
			boolean c = k.isControlDown();
			boolean a = k.isAltDown();					
			
	
		
			if(cd==k.VK_ENTER){ // !!!TRUQUES
				if(J.iguais(name, "FALAR")) // tem um mais completo no editorWin; Veja os comentários no metodo J.falar().
					J.falar(getText());
					//J.esc("falando: "+getText());
			}
				
			if(cd==k.VK_ENTER) cEnter=tmp; // mas isso jah tah lah em cima, ne'???
			if(cd==k.VK_DELETE) cTcDel=tmp;
			
			if(cd==k.VK_UP) { // esse incSt/decSt tah funcionando meia-boca, mas jah ajuda. Basta aperfeicoa-lo em J.java. Aqui nao precisa mecher em nada.				
				if(opIncSt)
				if(truncIni==0){ // quer dizer q eh um texto pequeno, tipico de nomes de arquivo
					String st = J.incSt(text);
					if(st!=text) cChange = tmp;
					text = st;
				}
			}
			if(cd==k.VK_DOWN) {
				if(opIncSt)				
				if(truncIni==0){ // quer dizer q eh um texto pequeno, tipico de nomes de arquivo
					String st = J.decSt(text);
					if(st!=text) cChange = tmp;
					text = st;					
				}
			}			
			
			if(cd==k.VK_ENTER)
			if(opMultiLine)
				ca='_';
			
			
		
			
			if(J.charRec(ca)) if(cd!=222){ // ignorando aspas
			
				if(opSomTec) wTec.play();
				
				cChange = tmpChange; 
				if(semSel){ 
					text = J.insChar(ca, text, cur);
					cur++;
					return;
				} else {
					text = J.remChars(selMin,selMax, text);
					text = J.insChar(ca, text, selMin);
					cur = selMin+1;
					selMin=selMax=-1;
				}
			}
			
			if(!s){
				if(cd==k.VK_RIGHT) { cur++; selMin=selMax=-1; }
				if(cd==k.VK_LEFT) { cur--; selMin=selMax=-1; }
				if(cd==k.VK_HOME) { cur=0; selMin=selMax=-1; }
				if(cd==k.VK_END) { cur=text.length(); selMin=selMax=-1; }
			}
			if(s){
				if(cd==k.VK_RIGHT){					
					if(temSel){
						if(cur==selMax+1) selMax++;
						if(cur==selMin) selMin++;
						cur++;
					} else {
						selMin=selMax=cur;
						cur++;
					}
				}	
				if(cd==k.VK_LEFT){					
					if(temSel){
						if(cur==selMax+1) selMax--;
						if(cur==selMin) selMin--;
						cur--;
					} else {
						cur--;
						selMin=selMax=cur;
					}
				}	
				if(cd==k.VK_HOME) {
					selMax=cur-1; 
					cur=selMin=0; 
				}
				if(cd==k.VK_END) {
					if(text==null){
						cur=0;
						selMin=-1;
						selMax=-1;
					} else {
						selMin=cur; 
						cur=selMax=text.length();
					}
				}
			}			
			
			if(cd==k.VK_DELETE) {
				wDel.play();				
				cChange = tmpChange;
				if(semSel)
					text = J.remChar(cur, text);
				else{
					text = J.remChars(selMin,selMax,text);
					cur=selMin;
					selMin=selMax=-1;
				}	
			}	
			if(cd==k.VK_BACK_SPACE){
				wDel.play();	
				cChange = tmpChange;
				if(semSel)
					text = J.remChar(--cur, text);
				else{
					text = J.remChars(selMin,selMax,text);
					cur=selMin;
					selMin=selMax=-1;					
				}
			}	

			if(text!=null)
				cur = J.corrInt(cur, 0,text.length());			
		}
			int truncIni=0;
		public void imp(){
			if(onClick()) selAll(); // o certo seria "o n G e t F o c u s()". Depois.
			
			calcAbs();
			
			impCx(crBox,crSomb,crLuz,xx,yy,lar,alt, LIVRE);
			 
			
			if(focado==this) impFocus(xx,yy,lar,alt);
			int dc=0, dl=0, // deslocamentos de linha e coluna p cada caracter, automatico
				ntl=1, // numero total de linhas, automatico
				ncl=-1; // numero de caracteres por linha(baseado na primeira linha de um multiline), automatico, aproximado (eh o suficiente)
				//nci=0; // numero de caracteres imprimidos, automatico
			char ch=0;
			boolean fora=false;
			Color cr=null, crr=null, crt=crText;
			if(blk)	crt=crSomb;				
			
			String st=text+"              ";
			boolean opHint=false;
			if(text==null || J.iguais(text,"") || J.iguais(text," ")) {st = hint; opHint=true;}
//opMultiLine=true; // debug
			if(cur<truncIni) truncIni--;
			if(truncIni<0) truncIni=0;
			if(text!=null) if(truncIni>text.length()) truncIni=0; // nao exato, mas evita um bug, melhorar depois
			for(int q=truncIni; q<st.length(); q++){
				{ // multiline
					/* falta:
						.		- barras de rolagem, 
								- rolar texto linha abaixo/acima com mouse, 
								- pular p linha de baixo/acima com setas (com e sem selecao)
					*/
					if(opMultiLine)
					if(dc>lar-22){
						if(ncl==-1) ncl=q; // numero de caracteres por linha baseado na primeira linha
						dc=0;
						dl+=altLin; // altura da linha
						ntl++;
						barH=false; 
						barV=true; 
						if(dl>alt-altLin) fora=true;
					}
					if(!opMultiLine) 
					if(dc>lar-12) {  // truncar texto 
						//barH=true;
						//barV=false;
						if(cur>q-2) truncIni++;
						fora=true;
					}
				}				
				if(fora) break; // precisa estar aqui

				cr=crBox;
				ch = st.charAt(q);				
				
				{ // texto destacado
					//if(J.noInt(q, selMin, selMax)) cr= (blk?crBt:crLuz);
					if(J.noInt(q, selMin, selMax)){
						if(focado==this) cr = crLuz;
						else cr = crBt;
						if(blk) cr = crBt;
/*
		crBt = J.cor[49],
			crText = J.cor[119],
			crFocus = J.cor[12], // essa eh automatica
			crSomb= crBt.darker(),
			crBox= crSomb.darker(),
			crLuz= crBt.brighter(),
			crBlk= crSomb,
			crBlkk= crSomb.darker();
*/
					}
					J.impRetRel(cr,null, xx+3+dc,yy+dl+1, J.larChar(ch),14);
				}
				{ // cursor
					if(cur==q)
					if(focado==this){
						Color crrr = (J.cont%60<30?J.cor[15]:J.cor[16]);
						J.impRetRel(crrr,null, xx+2+dc,yy+dl+1, 1,14);
					}
				}
				{ // caracteres					
					if(opHint) crt=crBt;
					//if(focado!=this) crt = crLuz; // nao ficou bom aqui
					//if(focado!=this) crt = J.cor[118]; // ateh ficou bonito, mas tem q trocar nos outros controles tb: list, rot, etc... depois talvez
					impCh(xx+2+dc,yy+9+3+dl,crt, ch);
					dc+=J.larChar(ch);
					// PARECE Q AINDA N?O TEM ALINHAMENTO DE CHARS AA DIR :(
					
					if(opMultiLine)
					if(ch=='_'){
						dc = 0;
						dl+=altLin;
					}
				}				
			}	
			
			if(focado==this)
			if(key!=null)
			if(cKey==tmp-2){
				// este ncl (numero de caracteres por linha) eh atualizado a cada impressao e eh var local, por isso esta gambiarra aqui. 
				if(key.getKeyCode()==key.VK_UP)   cur+=ncl;
				if(key.getKeyCode()==key.VK_DOWN) cur-=ncl;
				
				if(cur<0) cur=0;
				if(text==null) text=""; // isso eh bom??? Coloquei p tirar um bug.
				if(cur>text.length()) cur=text.length();
			}
				
// 333333333333 depois
//barV=false;
//barH=false;

			if(barV){ // barra de rolagem vertical
				crt=crText;
//crt=J.cor[12];
				fSeta.opRot=0;
				fSeta.impMask(crt, xx+lar-12-3,yy-2);
				fSeta.opRot=2;
				fSeta.impMask(crt, xx+lar-12-3,yy+alt-18);			
				//if(itm.size()>1)
				{
					int aq = (int)((alt-10-10-10-10) * ((sel)*100/(ntl))/100f);
					aq+=14;
					fQuad.impMask(crt, xx+lar-10,yy+1+aq);				
				}				
			}
			if(barH){ // barra de rolagem hor
				crt=crText;
//crt=J.cor[12];
				fSeta.opRot=3;
				fSeta.impMask(crt, xx-3,yy+alt-16);
				fSeta.opRot=1;
				fSeta.impMask(crt, xx+lar-12-3,yy+alt-16);
				//if(itm.size()>1)
				{
					//sel = ntl>>1;
					//int aq = (int)((lar-10-10-10-10) * ((cur)*100/(ntl))/100f);
					//int aq = (int)(lar * ((lar/text.length())*cur));
					int aq = 1;
					if(text!=null) aq = (int)(cur*((lar-32)/text.length()));
					aq+=14;
					fQuad.impMask(crt, xx+aq,yy+alt-12);
				}				
			}

			if(blk)	impContBlk(xx,yy,lar,alt);			
		}
		public String getScript(){
			String st = super.getScript()+" ";
			st+=text;
			if(hint!="") st+="; "+hint;
			return st;
		}		
		public Comp clear(){
			setText("");
			cur=0;
			return this;
		}
		public int getVal(){
			return J.stEmInt(text); // deve ajudar
		}
	}
	
// === TEXTPLUS ==============================
	public Comp addTextPlus(int i, int j, int ii, int jj){
		cAddComp=tmp;				
		Comp c = new TextPlus();		
		if(lastWin!=null) c.lk=lastWin;		
		
		Comp p = packEm(i,j);
		if(p!=null)	{
			c.pk=p;		
			i-=p.x;
			j-=p.y;
		}	
				
		c.cCreate = tmp;		
		c.tip = TXT_PLUS;
		c.cur=0;
		c.setName(newName(c.tip));
		c.x = i; c.y=j;
		c.lar = ii;
		c.alt = jj;
		c.cAjuste = tmpArq;
		cmp.add(c);
		lastComp=c;		
		este=c;		
		return c;
	}
	
	// usar JText.java
	// ainda vou reescrever esta classe
	
	public class TextPlus extends Comp{ //ttttttttttttt
				int altChar=16, larChar=12, tam=26;
				Color crFrente = J.cor[14];
				ArrayList<CharPlus> chr = new ArrayList<>();
				JText txt = new JText();				
			public class CharPlus{
					Color crFundo=null, crFrente=null;
					char ch='X';
				public CharPlus(char chp, Color cr, Color crf){
					if(chp!=0) ch=chp;
					if(cr!=null)crFrente=cr;
					if(crf!=null)crFundo=crf;
				}
				public void imp(int i, int j, boolean sl){
					Color cr=null;
					
					{ // fundo
						cr=crFundo;					
						if(crFundo!=null)
							J.impRetRel(null,cr, i,j-altChar,larChar,altChar);
					}
					{ // selecao
						if(sl)
							J.impRetRel(J.cor[12],null, i,j-altChar,larChar,altChar);							
					}
					
					int ddx=0, ddy=0;
					{ // ajuste; use somente p fontes monospace
						// mas usarei JText...
						//ddy=-(int)(altChar*0.3f);
						//ddy-=altChar;
						
						ddx=0;
						if(J.tem(ch,"mM")) ddx=-4;
						else if(J.tem(ch,"ijlIft,.;!")) ddx=+(int)(larChar*0.3f);
						else ddx=+(int)(larChar*0.15f);					

						if(1==1){ // gracinhas
							if(ch=='i') {ddx=J.RS(4); ddy=J.RS(4); }
							if(ch=='p') ddx=(J.cont>>4)%10;

							if(ch=='c') ddx = J.pulsar(J.cont>>3, 10);
							if(ch=='u') ddy = J.pulsar(J.cont>>3, 20);
							if(ch=='o') {
								ddx = J.pulsar(J.cont>>5, -10,+10);
								ddy = J.pulsar(J.cont>>5, -14,+14);
							}													
							if(ch=='b') {
								ddx = J.pulsar(J.cont>>2, -10,+10);
								ddy = J.pulsar(J.cont>>2, -6,+6);
								J.esc(""+ddx);
							}
							if(ch=='n') {
								float ang=J.cont/100f;
								ddx = (int)J.desXang(ang, 10);
								ddy = (int)J.desYang(ang, 10);
								J.esc(""+ddx);
							}							
						}
					}
					{ // cor de frente
						cr=crFrente;
						if(cr==null) cr=crText;
					}
					if(ch=='\n') J.impCirc(12,4, i+ddx,j+ddy);
					if(ch=='\r') J.impCirc(10,4, i+ddx,j+ddy);
					txt.larChar = larChar;
					txt.altChar = altChar;
					txt.imp(cr, i+ddx,j+ddy-altChar, ""+ch); // Claro q tem q ser refeito!!!
				}
			}
			public void remChars(int min, int max){
				ArrayList<CharPlus> lis = new ArrayList<>();
				for(int q=0; q<chr.size(); q++){
					if(!J.noInt(q,min,max)) lis.add(chr.get(q));
				}
				chr = lis;
			}			
		public Comp setColor(Color cr, Color crf){
			// se tem selecao, atribuir somente ao texto selecionado
			// senao, apenas mudar a cor de insercao
			
			crFrente=cr;
			crFundo=crf;
			
			if(selMax!=-1)
			for(int q=selMin; q<selMax; q++){
				chr.get(q).crFrente = cr;
				chr.get(q).crFundo = crf;
			}
			return this;
		}
		public Comp insText(String st){
			int c=-1;
			for(int q=0; q<st.length(); q++){
				c++;
				chr.add(new CharPlus(st.charAt(q),crFrente,crFundo));
			}
			return this;
		}
		public void reg(KeyEvent k){			
			super.reg(k);
			
			boolean semSel= selMax==-1;
			boolean temSel=!semSel;
			
			int cd = k.getKeyCode();
			char ca = k.getKeyChar();
			
			boolean s = k.isShiftDown();
			boolean c = k.isControlDown();
			boolean a = k.isAltDown();					
			
		
			if(cd==k.VK_ENTER) cEnter=tmp;
			if(cd==k.VK_DELETE) cTcDel=tmp;
			if(J.charRec(ca)) if(cd!=222){ // ignorando aspas
			
				wTec.play();
				
				cChange = tmpChange; 
				if(semSel){ 
					//text = J.insChar(ca, text, cur);
					chr.add(cur, new CharPlus(ca,crFrente,crFundo));
					cur++;
					return;
				} else {
					//text = J.remChars(selMin,selMax, text);
					//text = J.insChar(ca, text, selMin);
					J.remIntArrLst(selMin,selMax,chr);
					cur=selMax;
					cur-=(selMax-selMin);
					chr.add(cur, new CharPlus(ca,crFrente,crFundo));					
					cur++;
					selMin=selMax=-1;
				}
			}
			
			if(!s){
				if(cd==k.VK_RIGHT) { cur++; selMin=selMax=-1; }
				if(cd==k.VK_LEFT) { cur--; selMin=selMax=-1; }
				if(cd==k.VK_HOME) cur = comDaLinha();
				if(cd==k.VK_END) cur = fimDaLinha();
				
				if(cd==k.VK_UP) { 
					cur-= (int)(lar/larChar);
					if(cur<0)cur=0;
					selMin=selMax=-1;
				}
				if(cd==k.VK_DOWN) { 
					cur+= (int)(lar/larChar);
					if(cur>text.length())cur=text.length();
					selMin=selMax=-1;
				}				
			}
			if(s){
				if(cd==k.VK_RIGHT){
					if(temSel){
						if(cur==selMax+1) selMax++;
						if(cur==selMin) selMin++;
						cur++;
					} else {
						selMin=selMax=cur;
						cur++;
					}
				}
				if(cd==k.VK_LEFT){					
					if(temSel){
						if(cur==selMax+1) selMax--;
						if(cur==selMin) selMin--;
						cur--;
					} else {
						cur--;
						selMin=selMax=cur;
					}
				}	
				if(cd==k.VK_HOME) {
					selMax=cur-1; 
					cur=selMin=0; 
				}
				if(cd==k.VK_END) {
					if(text==null){
						cur=0;
						selMin=-1;
						selMax=-1;
					} else {
						selMin=cur; 
						cur=selMax=text.length();
					}
				}

				if(cd==k.VK_UP) { 
					if(selMax==-1)selMax=cur;
					cur-= (int)(lar/larChar);
					if(cur<0)cur=0;
					selMin=cur;
				}
				if(cd==k.VK_DOWN) { 
					if(selMin==-1)selMin=cur;
					cur+= (int)(lar/larChar);
					if(cur>text.length())cur=text.length();
					selMax=cur-1;
				}				

			}			
			
			if(cd==k.VK_DELETE) {
				wDel.play();	
				cChange = tmpChange;
				if(semSel){
					J.remItArrLst(cur,chr);					
				} else {
					remChars(selMin,selMax);
					cur = selMin;					
					selMin=-1; selMax=-1;
				}				
			}	
			if(cd==k.VK_BACK_SPACE){
				wDel.play();	
				cChange = tmpChange;
				if(semSel){
					J.remItArrLst(--cur,chr);
				} else {
					remChars(selMin,selMax);
					cur = selMin;
					selMin=-1; selMax=-1;					
				}
			}	

			if(text!=null)
				cur = J.corrInt(cur, 0,text.length());			
		}
		public Comp clear(){
			chr.clear();
			return this;
		}
					private void impCur(int i, int j){
						//txt.imp( (J.cont%60<30?J.cor[10]:J.cor[9]), i-(larChar>>1), j, ""+(char)32000);						
						
						J.impRetRel(
							(J.cont%60<30?J.cor[15]:J.cor[16]),null,
							i-2, j, 
							2, altChar); // esse 3 tah bom? Serah sempre assim p qq fonte?
						
					}
					private int comDaLinha(){
						// procura o comeco da linha atual
						return 0;
					}
					private int fimDaLinha(){
						// procura o fim da linha atual
						for(int q=0; q<30; q++)
							if(chr.get(q+cur).ch=='\r') return q+cur;
						return cur;						
					}					
		public void imp(){
			
			if(chr.size()<=0) {
				//setColor(J.cor[J.rndCr9()],J.cor[J.rndCr9()]);
				setColor(J.cor[15],J.cor[18]);
				insText("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890?!;.,!@#$%&*()-=");
			}
			
			calcAbs();			
			impCx(crFundo,crSomb,crLuz,xx,yy,lar,alt, LIVRE);			
			if(focado==this) impFocus(xx,yy,lar,alt);
			int col=0, lin=0,c=0;
			String txt="";
			for(CharPlus ca:chr){

				if(ca.ch=='\n') { lin+=altChar; continue; }
				if(ca.ch=='\r') { col=0; continue; }				
				ca.imp(xx+col, yy+lin+altChar,J.noInt(c,selMin,selMax));
				if(focado==this){ // cursor
					if(c==cur) impCur(xx+col,yy+lin);
				}								
				txt+=ca.ch;
				col+=larChar;
				if(col>lar-larChar){col=0; lin+=altChar;}
				if(lin>alt-altChar) break; // barras de rolagem depois
				c++;
			}
			//if(cur==chr.size()) impCur(xx+col,yy+lin);
			text = txt; // cuidado com isso.
			if(blk)	impContBlk(xx,yy,lar,alt);						
		}
	}
// === WIN ==============================
		boolean opImpGrade=false; // mais p editor. Imprime a grade sobre cada win.
		boolean opGrade=true; // p grade invisivel (usei p win)
		int opTamGrade=10;
	public Comp addWin(String st, int i, int j, int ii, int jj){
		cAddComp=tmp;				
		xIns = 3; // isso eh importante
		yIns = 3;
		Comp c = new Win();
		lastWin=c;
		
		c.cCreate = tmp;		
		c.tip = WIN;
		c.setName(newName(c.tip));
		c.x = i; c.y=j;		
		c.lar = ii;
		c.alt = jj;		
		c.setText(st);
		c.cAjuste = tmpArq;
		if(st==null) c.canResize=false; // barra de ferramentas fixa
		cmp.add(c);
		lastComp=c; // p win tb, pq usa no mecanismo de reconhecimento de bloqueio.
		este=c;		
		return c;		
	}
	public class Win extends Comp{
			boolean opMovendo=false;
			int ixMove=0, iyMove=0;
		public void imp(){
			//ms.opImpArea=true;
			// mover com bt1 em qq outra interface
			if(text!=null){ // isso tah funcionando muito bem.
				if(!opMovendo)
				if(ms.b1)
				if(ms.naAreaRel(x,y-16,lar-22,16)){
					// isso ficou lindo!!!
					ixMove = (x+(lar>>1))-ms.x;
					iyMove=0;
					opMovendo=true;
				}
				if(!ms.b1) opMovendo=false;
				if(ms.arrastou)
					opMovendo=false;
				if(opMovendo) 
					moveMeio(ms.x+ixMove, ms.y+22+iyMove);
			}
		
			// titulo
			if(text!=null){
				
				impSombMenu(x,y-18,lar,alt+18); // barras de ferramenta nao tem sombra
				
				J.impRetRel(crBox,null, x-1,y-18,lar+1,18);
				J.impMoldRel(crBt,null, x-1,y-18,lar+1,18);
				impText(x+2,y+12-18,   crText, text);

				if(opTemX){
					fXis.impJf1(x+lar-14,y+2-18);
					if(ms.b1)
					if(ms.naAreaRel(x+lar-18,y+2-18,18,18)){
						cClick=tmp;
						close(); // permite captar o evento 
					}	
				}
			}
			
			// corpo
			// nao calcula coordenadas absolutas
			int q=0, qq=0;
			if(cTreme>0){q=J.RS(3); qq=J.RS(3); if(cTreme>0) cTreme--; } // pequena gambiarra
			impCx(crBt,crLuz,crSomb,x+xx+q,y+yy+qq,lar,alt,LIVRE);

			{ // pequena gambiarra
				xx+=x;	yy+=y;
				 
				xx-=x;	yy-=y;
			}
			
			if(focado==this) {
				int d=0;
				if(text!=null) d=16;
				impFocus(x+q,y-d+qq,lar,alt+d); // q e qq eh pela gambiarra do cTreme
			}	
			
			if(canResize) verResizing();
			if(opImpGrade){
				int lg=opTamGrade,px=2;
				for(int m=lg; m<lar; m+=lg)
				for(int n=lg; n<alt; n+=lg)
					J.impRetRel(crBox,null, x+m,y+n,px,px);
			}

		}
		
			private boolean opOnResize=false;
		public boolean onResize(){
			return opOnResize;
		}
		public void verResizing(){
			int q=16, // tamanho da alca 
				qq=16; // margem alem da win
			
			fAlcaWin.impJf1(x+lar-20, y+alt-20, crLuz, crSomb, J.cor[12], J.cor[10], J.cor[16]);
			
			// isso eh p debug
			// J.impRetRel(0,12, x+lar-q, y+alt-q, q+qq,q+qq);
			
			opOnResize=false;
			if(resizing && ms.arrastando)
				; // nada aqui
			else {
				resizing = false; 
				if(ms.arrastou) {
					opOnResize=true;
					for(Comp c:cmp) // ?isso eh bom???
						if(c.lk==this)
						if(c.tip==MNU)
							c.lar = lar;
				}
			}

			
			if(ms.b1)
			if(ms.naAreaRel(x+lar-q, y+alt-q, q+qq,q+qq))
				resizing=true;
			if(resizing){
				lar = (ms.x-x+6);
				alt = (ms.y-y+6);
				if(opGrade){ // nao confunda com "opImpGrade", q eh a impressao da grade. A grade pode entao funcionar, mas ser invisivel, que eh o caso desta var "opGrade"
					lar-=(ms.x%opTamGrade);
					alt-=(ms.y%opTamGrade);
				}
			}			
		}
		public boolean onMouseEnter(){
			if(cSobre==1) // este precisa ser diferente
			if(!opMovendo)
			if(ms.naAreaRel(x,y,lar,alt))	
				return true;
			return false;
		}		
		public void ajuste(){
			cAjuste=0;
			int i=30000, j=30000, ii=-30000, jj=-30000;
			for(Comp c:cmp)
			if(c!=this)
			if(c.lk==this){
				// if(c.x<i) i=c.x;
				// if(c.y<j) j=c.y;
				if(c.lar>ii) ii=c.lar;
				if(c.alt>jj) jj=c.alt;
			}
			int q=3;
			i+=q;
			j+=q;
			ii+=q+q;
			jj+=q+q;
			if(x>i) x=i;
			if(y>j) y=j;
			if(lar<ii) lar=ii;
			if(alt<jj) alt=jj;
		}
		public Comp setBlk(boolean b){
			super.setBlk(b);
			for(Comp c:cmp)
			if(c!=this)
			if(c.lk==this)
				c.setBlk(b);			
			return this;
		}
		public String getScript(){
			String st = super.getScript()+" ";
			st+=text;
			return st;
		}
		public Comp setPos(int p){
			centralizar();
			switch(p){ // outras posicoes depois, se precisar
				case J.ESQ: x=6; break;
				case J.DIR: x=J.maxXf-lar-3; break;
				case 1000+J.DIR: x=J.maxXf-lar-lar-lar; break;
				case J.CMA: y=6; break;
				case J.BXO: y=J.maxYf-alt-3; break;
				default: J.impErr("!posicao nao definida: "+p,"JWin.Win.setPos()"); break;
			}
			return this;
		}
		public Comp centralizar(){
			int i = (J.maxXf-lar)>>1;
			int j = (J.maxYf-alt)>>1;
			x=i; y=j;
			ajuste();
			return this;
		}
		public Comp setColor(Color cr, Color crr){
			setAmbColor(cr);
			return this;
		}
		public Comp setHid(boolean h){
			super.setHid(h);
			for(Comp c:cmp)
			if(c!=this)
			if(c.lk==this)
				c.setHid(h);			
			return this;
		}	
	}

// === PACK =============================

	public Comp addPack(String stt, int i, int j, int ii, int jj){
		cAddComp=tmp;				
		//se st for algo do tipo "packUm; txt01, combo04, image00", os a listagem de componentes eh linkada ao pack automaticamente. Programei p q packs fossem abertos por ultimo nos arqs win.
		String st = J.truncarAte(';',stt);
		
		
		Comp c = new Pack();
		if(lastWin!=null) c.lk=lastWin;
		
		c.cCreate = tmp;		
		c.tip = PCK;
		c.lk = lastWin;
		c.setName(newName(c.tip));
		c.x = i; c.y=j;		
		c.lar = ii;
		c.alt = jj;
		c.cAjuste = tmpArq;
		c.linkComps(stt);		// linkar listagem de comps
		c.text = J.stAntesDe(';',stt); // esta linha nao tah funcionando. Em "ajuste()" o bug foi removido.
		cmp.add(c);
		corrArrCmp();
		lastComp=c;		
		este=c;		
		return c;		
	}	
	public class Pack extends Comp{
		public Comp linkComps(String st){
			// funcao: linka um ou varios itens ao pack
			// varios itens separados por virgula
			// deve-se ignorar o primeiro item delimitato por ";" q seria o nome do pack
			// os itens seguintes seriam os nomes de cada comp
			
			if(st==null) return this;
			
			// IGNORAR O PRIM ITEN SE TIVER ";"
			st = J.truncarAntes(';',st);
			st = J.limpaSt(st); // remove espacos iniciais e finais

			
			String it="";
			char ch=0;
			Comp cp = null;
			for(int q=0; q<st.length(); q++){
				ch = st.charAt(q);
				if(ch!=',') it+=ch;
				else{
					// addItem(it);
					it = J.trocaChar(',',' ',it);
					it = J.limpaSt(it);
					
					cp = getComp(it);
					if(cp.tip==0) J.impErr("!nao achou o comp |"+it+"|");
					cp.pk = this;
					it="";
				}
			}
			it = J.trocaChar(',',' ',it);
			it = J.limpaSt(it);			
			// addItem(it); // o ultimo tb
			
			cp = getComp(it);
			if(cp.tip==0) J.impErr("!nao achou o comp no final|"+it+"|");
			cp.pk = this;

			return this;		
		}

		public void imp(){
			calcAbs(); // poderia um pack estar dentro de outro pack???
			
			int q=3;
			
			// caixa
			Color cr = crBt;
			if(cBlink>0) cr = crFocus;
			J.impRetRel(cr, crSomb, xx+1,yy+1+q,lar-1,alt-q);
			J.impRetRel(null, (blk?crSomb:crLuz), xx,yy+q,lar-1,alt-q);

			// fundo do texto
			if(text!=null)
			if(!text.equals(""))	
			if(!text.equals(" "))	
			J.impRetRel(crBt, null, xx+6,yy,J.larText(text+"JJ"),12);
			
			if(text!=null)			
			if(!text.equals(""))	
			if(!text.equals(" ")){
				
				impText(xx+12,yy+10,(blk?crSomb:crText), text);
			}
		
			if(focado==this) impFocus(xx,yy,lar,alt);
			 			
			if(blk)	impContBlk(xx,yy,lar,alt);						
		}
		public Comp setBlk(boolean b){
			super.setBlk(b);
			for(Comp c:cmp)
			if(c!=this)
			if(c.pk==this)
				c.setBlk(b);			
			return this;
		}
		public void ajuste(){
				// arrancando bug na marra >:(
				if(J.tem(';',text)) text = J.stAntesDe(';',text); 			
		}
		public void altBlk(){
			super.altBlk();
			for(Comp c:cmp)
			if(c!=this)				
			if(c.pk==this)
				c.altBlk();
		}
		public void altHid(){
			super.altHid();
			for(Comp c:cmp)
			if(c!=this)				
			if(c.pk==this)
				c.altHid();
		}		
		public Comp setHid(boolean h){
			super.setHid(h);
			for(Comp c:cmp)
			if(c!=this)				
			if(c.pk==this)
				c.setHid(h);
			return this;
		}				
		public String getScript(){
			String st = super.getScript();
			st+=" "+text+"; ";
			for(Comp c:cmp)
				if(c.pk==this)
					st+= c.getName()+", ";
			st = J.truncUltimos(2,st);
			return st;
		}
	}

// === SLIDE BAR ========================

	public Comp addSlideBar(int i, int j, int ii, int jj){
		cAddComp=tmp;				
		Comp c = new SlideBar();
		if(lastWin!=null) c.lk=lastWin;		
		
		Comp p = packEm(i,j);
		if(p!=null)	{
			c.pk=p;		
			i-=p.x;
			j-=p.y;
		}	
		
		c.cCreate = tmp;		
		c.tip = SLD_BAR;
		c.setName(newName(c.tip));
		c.x = i; c.y=j;		
		c.lar = ii;
		c.alt = jj;
		c.cAjuste = tmpArq;
		cmp.add(c);
		lastComp=c;		
		este=c;		
		return c;		
	}
	public class SlideBar extends Comp{			
		public void imp(){
			
			calcAbs();

			if(ms.dr!=0)
			if(focado==this)	
				setVal(sel-ms.dr);
			
			if(cClick>0)
			if(ms.b1)
			if(ms.naAreaRel(xx,yy,lar,alt)){
				// sel = alt-(ms.y-yy);
				if(alt>lar) // vert
					sel = -4+(int)J.prop(alt-(ms.y-yy),0,alt,selMin,selMax);
				else // hor
					sel = +selMin+selMax+2-(int)J.prop(lar-(ms.x-xx+xCalMs),0,lar,selMin,selMax);
				sel = J.corrInt(sel,selMin,selMax);
			}	
			
			int val = sel;
		
			// corpo
			impCx(null,crSomb,crLuz,xx,yy,lar,alt,LIVRE);
			// deixei o fundo da cx transparente
			
			//J.impRetRel(crFundo, null, xx+1,yy+1,lar-1,alt-val);
			//J.impRetRel(crFrente,null, xx+1,yy+alt-val,lar-1,val);
			int v = 0;
//selMin=100; selMax=200;
			Color crt=crText;
			if(blk)crt=crSomb;			
			if(alt>lar){ // vertical
				v = (int)J.prop(val,selMin,selMax,0,alt);
				J.impRetRel(crFundo, null, xx+1,yy+1,lar-1,alt-v+1);
				J.impRetRel(crFrente,null, xx+1,yy+alt-v,lar-1,v);
				impText(xx,yy+alt-v-2, crt, ""+val);				
			} else { // hor
				v = (int)J.prop(val,selMin,selMax,0,lar-4);
//				crFrente=J.cor[12];
//				crFundo=J.cor[4];
				J.impRetRel(crFundo, null, xx+v+1,yy+1,lar-v-3,alt-3);
				J.impRetRel(crFrente, null, xx+1,yy+1,v,alt-3);
				impText(xx+v,yy+14, crt, ""+val);								
			}
						
			
			 						
			
			if(focado==this) impFocus(xx,yy,lar,alt);
			if(blk)	impContBlk(xx,yy,lar,alt);			
		}
		public boolean onChange(){
			if(cClick==tmp-1) return true;
			if(cChange==tmp-1) return true;
			return false;
		}
		public Comp setVal(int v){
			sel = J.corrInt(v,selMin,selMax); // deve ser ajustado depois, quando for implementada a proporcionalidade.
			cChange = tmp;
			return this; 
		}
		public String getText(){
			return ""+sel;
		}
		public String getScript(){
			String st = super.getScript()+" ";
			st+= ""+sel;
			st+= ", "+selMin;
			st+= ", "+selMax;
			st+= ", "+J.colorEmStc(crFrente);
			st+= ", "+J.colorEmStc(crFundo);
			return st;
		}
	}

// === COLOR BUTTON =====================

	public Comp addColorButton(String st){
		cAddComp=tmp;				
		// eh barra de ferr horizontal?
		// int q=24+2;
		// if(opDoubleSizeJf1) q=48+2;
		int q=24+2;
		if(opDoubleSizeJf1) q=48+2;		
		
		Comp c = addColorButton(st, xIns, yIns, q,q);
		c.lar-=4;
		c.alt-=4;
		
		boolean hor = true;
		if(lastWin!=null)
			if(lastWin.lar<lastWin.alt) 
				hor=false;
			
		if(hor)	xIns+=q;
		else yIns+=q;	
		
		return c;		
	}
	public Comp addColorButton(String st, int i, int j, int ii, int jj){
		cAddComp=tmp;				
		Color cr = null;
		if(J.tem(' ',st)) cr = J.stEmColor(st);
		else cr=J.stcEmColor(st);
		// seria bom verificar se st eh conversivel em cor, tanto a compacta quanto a normal.		
		return addColorButton(cr,i,j,ii,jj);
	}
	public Comp addColorButton(Color cr, int i, int j, int ii, int jj){
		cAddComp=tmp;				
		Comp c = new ColorButton();
		if(lastWin!=null) c.lk=lastWin;		
		
		Comp p = packEm(i,j);
		if(p!=null)	{
			c.pk=p;		
			i-=p.x;
			j-=p.y;
		}	
				
		c.cCreate = tmp;				
		c.tip = CLR_BUT;
		if(opInsTog)c.canTog=true;		
		c.setName(newName(c.tip));
		c.x = i; c.y=j;		
		c.lar = ii;
		c.alt = jj;
		c.cAjuste = tmpArq;
		c.setColor(cr, null);
		cmp.add(c);
		lastComp=c;		
		este = c;
		return c;		
	}
	public class ColorButton extends Comp{
		public void imp(){			
		
			int e=0, d=0;
			
			int est=LIVRE;
			if(cSobre>0){ d=-1; est=SOBRE; }
			if(cClick>0 || tog){ d=+1; est=PRESS; }
			if(blk){ d=0; }

			calcAbs();
			
			impCx(crBt,crLuz,crSomb,xx+d,yy+d,lar,alt, est);
			 			

			Jf1 f = null;
			if(blk) f = fCorb;
			else f = fCor;			
			
			f.impJf1(
				xx+d+3,
				yy+d+3+1, 
				xx+d+3+lar-7,
				yy+d+3+alt-7);
			J.impRetRel(crFrente, null, 
				xx+d+3,
				yy+d+3, 
				lar-6,alt-6);			
			if(focado==this) impFocus(xx+d,yy+d,lar,alt);
			if(blk)	impContBlk(xx,yy,lar,alt);						
		}
		public String getText(){
			return J.colorEmStc(crFrente);
		}
		public Comp setText(String st){
			cChange=tmp;
			text = st;
			crFrente = J.stcEmColor(st);
			return this;			
		}		
		public Color getColor(){
			return crFrente;
		}
		public String getScript(){
			String st = super.getScript();
			st+=" "+getText();
			return st;
		}
	}

// === CAIXA DE LISTAGEM DE CORES ==========

	public Comp addColorListBox(int i, int j, int ii, int jj){
		cAddComp=tmp;				
		Comp c = new ColorListBox();
		if(lastWin!=null) c.lk=lastWin;		
		
		Comp p = packEm(i,j);
		if(p!=null)	{
			c.pk=p;		
			i-=p.x;
			j-=p.y;
		}	
				
		c.cCreate = tmp;				
		c.tip = CLR_LST_BOX;
		c.setName(newName(c.tip));
		c.x = i; c.y=j;		
		c.lar = ii;
		c.alt = jj;
		c.cAjuste = tmpArq;
		cmp.add(c);
		lastComp=c;		
		este=c;		
		return c;		
		
	}
	public class ColorListBox extends Comp{
			ArrayList<Color>itm = new ArrayList<>();
			int altLin=16;
		public void rnd(){ // remover depois
			Color cr = null;
			for(int q=0; q<30; q++){
				// itm.add(new Color(J.R(255),J.R(255),J.R(255),127+J.R(2)*127));
				cr = new Color(J.R(255),J.R(255),J.R(255),127+J.R(2)*127);
				addItem(J.colorEmStc(cr));
			}	
		}	
			int 
				ipl=100, // interno, itens por linha. Nao mecha nisso.
				ini=0; // interno tb, onde comeca a impressao
		public void reg(KeyEvent k){
			super.reg(k);
			if(k.getKeyCode()==k.VK_UP) selAnt();
			if(k.getKeyCode()==k.VK_DOWN) selProx();
			if(k.getKeyCode()==k.VK_END) selUlt();
			if(k.getKeyCode()==k.VK_HOME) selPrim();
		}

		
		public void imp(){
			if(itm.size()<=0) rnd(); // remover depois
			
			calcAbs();			
			impCx(crBox,crSomb,crLuz,xx,yy,lar,alt,LIVRE);			
			 			
			int n=-altLin-3;
			Color crc=null; // contorno do item, p quando estiver selecionado
			
			Jf1 f = null;
			if(blk) f = fCorb;
			else f = fCor;							
			
			if(focado==this)
				if(ms.dr!=0)
					select(sel+ms.dr);
				
			int lr = lar -20, qq=3;
			ipl = 0;
			boolean foi=false;
				
			if(itm!=null)
			if(itm.size()>0)
			for(int q=ini; q<1000; q++){
				n+=altLin+3;
				ipl++;
				if(n>alt-altLin-3-2) break;
				if(q>=itm.size()) break;
				
				if(sel==q) {crc=crLuz; foi=true;}
				else crc=null;
				J.impRetRel(crc, null, xx+4, yy+3+n, lr,altLin+2);
				
				f.impJf1Rel(xx+5+qq, yy+5+n, lr-4-qq-qq,altLin-4);
				
				J.impRetRel(itm.get(q), null, xx+5+qq, yy+5+n, lr-2-qq-qq,altLin-2);
				
				if(ms.b1)
					if(ms.naAreaRel(xx+5+qq, yy+5+n, lr-2-qq-qq,altLin-2))
						select(q);
			}
			if(!foi){ // parece q eu consegui
				if(sel<ini) ini--;
				if(ini<0)ini=0;
				
				if(sel>ini+ipl-2) ini++;
				if(ini>itm.size()-1) ini=itm.size()-1;
			}
			
			// barra de rolagem
			Color crt=crText;
			fSeta.opRot=0;
			fSeta.impMask(crt, xx+lar-12-3,yy-2);
			fSeta.opRot=2;
			fSeta.impMask(crt, xx+lar-12-3,yy+alt-18);			
			if(itm.size()>1){
				int aq = (int)((alt-10-10-10-10) * ((sel)*100/(itm.size()-1))/100f);
				aq+=14;
				fQuad.impMask(crt, xx+lar-10,yy+1+aq);				
			}
			
			
			if(focado==this) impFocus(xx,yy,lar,alt);
			if(blk)	impContBlk(xx,yy,lar,alt);		
		}
		public Comp clear(){
			if(itm!=null)
				itm.clear();
			return this;
		}
		public int getNumItens(){
			if(itm==null) return 0;
			return itm.size();
		}
		public Comp addItem(String st){
			Color cr = J.stcEmColor(st);
			if(cr!=null)
				itm.add(cr);
			return this;
		}
		public String getScript(){
			String st = super.getScript();
			
			return st;
		}
		public Comp setText(String st){
			// muda a cor selecionada
			Color cr = J.stcEmColor(st);
			itm.set(sel,cr);
			return this;
		}
		public String getText(){
			if(itm==null) return "";
			if(itm.size()<=0) return "";
			
			// autocorrecao. Serah q isso insere um bug?
			if(sel>=itm.size()) sel = itm.size()-1;
			
			Color cr = itm.get(sel);
			String st = J.colorEmStc(cr);
			return st;
		}
		public void select(int i){
			
			if(i==-2) // isso deveria ser padrao
				if(itm!=null)
					select(itm.size()-1);
				
			if(itm!=null)
			if(itm.size()>0)
			if(J.noInt(i,0,itm.size()-1))	{
				sel = J.corrInt(i, 0,itm.size()-1);			
				setText(J.colorEmStc(itm.get(sel)));
				cChange = tmpChange;
			}
		}
	}
// === CAIXA DE LISTAGEM =================
		boolean mnAberto=false;

	public Comp addListBox(int i, int j, int ii, int jj){
		cAddComp=tmp;				
		Comp c = new ListBox();
		if(lastWin!=null) c.lk=lastWin;		
		
		Comp p = packEm(i,j);
		if(p!=null)	{
			c.pk=p;		
			i-=p.x;
			j-=p.y;
		}	
				
		c.cCreate = tmp;				
		c.tip = LST_BOX;
		c.setName(newName(c.tip));
		c.x = i; c.y=j;		
		c.lar = ii;
		c.alt = jj;
		c.cAjuste = tmpArq;
		cmp.add(c);
		lastComp=c;		
		este=c;		
		return c;		
	}

	public class ListBox extends Comp{
			ArrayList<String> itm = new ArrayList<>();
			ArrayList<String> itmm = new ArrayList<>();
			ArrayList<String> itmmm = new ArrayList<>();
			ArrayList<String> itmmmm = new ArrayList<>();
			ArrayList<String> itmmmmm = new ArrayList<>();

		public void openList(String cm){
			itm = J.openStrings(cm);
		}
		public void saveList(String cm){			
			J.saveStrings(itm,cm);
			// ?e os outros itens???
			// ?e se nao conseguir salvar??? O controle deveria avisar. (J.java jah o faz pelo console, mas seria legal se a interface o fisesse)
			// Depois
		}
		public int size(){ // getSize()
			// ?E se a lista for nula???
			int v = itm.size();
			if(v<itmm.size()) v = itmm.size();
			if(v<itmmm.size()) v = itmmm.size();
			if(v<itmmmm.size()) v = itmmmm.size();
			if(v<itmmmmm.size()) v = itmmmmm.size();
			return v;
		}
		public void ajuste(){
			if(tab1==-1){
				tab1= (int)(lar*0.2f);
				tab2= (int)(lar*0.4f);
				tab3= (int)(lar*0.6f);
				tab4= (int)(lar*0.8f);
			}
		}	
		public void reg(KeyEvent k){
			// p list e p comboBox q eh derivado desta
			super.reg(k);
			
			int cd=k.getKeyCode();
			
			if(cd==k.VK_ENTER) cEnter=tmp;			
			if(cd==k.VK_DELETE) cTcDel=tmp;			

			boolean c = k.isControlDown();
			boolean s = k.isShiftDown();
			boolean a = k.isAltDown();
			
			if(!c)
			if(!a)// p nao dar conflito com incremento de string de c o m b o  q eh derivada desta
			if(!s){
				if(cd==k.VK_UP)   select(sel-1);
				if(cd==k.VK_DOWN) select(sel+1);
			}

			if(ipl==-1)ipl=1;
			
			if(opPageUpDown){
				if(cd==k.VK_PAGE_DOWN) select(sel+ipl);
				if(cd==k.VK_PAGE_UP) select(sel-ipl);
			}
			
			if(tip!=CMB_BOX){ // precisa pq derivei esta classe p combo box
				if(opHomeEnd){
					if(cd==k.VK_HOME) selPrim();
					if(cd==k.VK_END) selUlt();
				}
				if(J.charRec(k.getKeyCode()))
					selectPrimChar(k.getKeyChar()); // este nao expande p   s s  c h e    e   s s  c h o 
			}
			
		}
		public void imp(){			
			calcAbs();
			
			if(cClick>0) cChange=tmp;
			
			impCx(crBox,crSomb,crLuz,xx,yy,lar,alt,LIVRE);
			 			

			Color crt=crText;
			if(blk) crt=crSomb;
			
			// itens
			int al=14; // altura da linha
			int ult=-1;
			ipl=0;
			String st="", stt="", sttt="", stttt="",sttttt="";
			Jf1 f = null;
			if(selMin<0) selMin=0;
			if(itm!=null)
			if(itm.size()>0)
			for(int q=0; q<itm.size(); q++){
				ipl++;
				ult++;
				st = "000";
				stt = "";
				sttt = "";
				stttt = "";
				sttttt = "";
				
				if(q+selMin<itm.size()){
					st = itm.get(q+selMin);
					if(q+selMin<itmm.size())
						stt = itmm.get(q+selMin);
					if(q+selMin<itmmm.size())
						sttt = itmmm.get(q+selMin);
					if(q+selMin<itmmmm.size())
						stttt = itmmmm.get(q+selMin);
					if(q+selMin<itmmmmm.size())
						sttttt = itmmmmm.get(q+selMin);					
				}	else break;
					
				if(sel==q+selMin)
					J.impRetRel(crBt, null, xx+1, yy+q*al+1, lar-12, 14);
				
				
				
				if(ms.b1 || ms.b2) // ????
					//if(!mnAberto) //1111111111
					if(ppu==null)
					if(!blk)
					if(ms.naAreaRel(xx+1, yy+q*al+1, lar-12, 14)){
						sel=q+selMin;
						cChange=tmpChange;
					}	
					

				impText(xx+1,			yy+q*al+12, crt, J.truncLar(lar, st));
				impText(xx+1+tab1,	yy+q*al+12, crt, J.truncLar(lar-tab1, stt));
				impText(xx+1+tab2,	yy+q*al+12, crt, J.truncLar(lar-tab2, sttt));
				impText(xx+1+tab3,	yy+q*al+12, crt, J.truncLar(lar-tab3, stttt));
				impText(xx+1+tab4,	yy+q*al+12, crt, J.truncLar(lar-tab4, sttttt));
				
				if(yy+q*al>yy+alt-12-12-6) {
					ipl=q;
					break;
				}
			}
			
			J.impRet(crBox,null, xx+lar-12+1,yy,xx+lar+1,yy+alt);
			if(ult+selMin<sel) selMin=sel;
			if(sel<selMin) selMin--;
			if(itm!=null)
				if(sel>itm.size()-1) 
					sel--;
			if(sel<0)sel=0;
			
			// separadoes discretos
			if(itmm.size()>0)
				J.impLin(crLuz, xx+tab1,yy, xx+tab1,yy+alt);
			if(itmmm.size()>0)
				J.impLin(crLuz, xx+tab2,yy, xx+tab2,yy+alt);			
			if(itmmmm.size()>0)
				J.impLin(crLuz, xx+tab3,yy, xx+tab3,yy+alt);						
			if(itmmmm.size()>0)
				J.impLin(crLuz, xx+tab4,yy, xx+tab4,yy+alt);						
		
			// barra de rolagem
			fSeta.opRot=0;
			fSeta.impMask(crt, xx+lar-12-3,yy-2);
			fSeta.opRot=2;
			fSeta.impMask(crt, xx+lar-12-3,yy+alt-18);			
			if(itm!=null)
			if(itm.size()>1){
				int aq = (int)((alt-10-10-10-10) * ((sel)*100/(itm.size()-1))/100f);
				aq+=14;
				fQuad.impMask(crt, xx+lar-10,yy+1+aq);				
			}
			
	
			if(focado==this) impFocus(xx,yy,lar,alt);
			if(blk)	impContBlk(xx,yy,lar,alt);						
			
			if(focado==this)
			if(itm.size()>0){
				String sti = ""+(sel+1)+" / "+(itm.size());
				int d = J.larText(sti);
				J.impRetRel(crBox, crLuz, xx+lar-d, yy+3-12, d, 14);
				impText(xx+lar-d, yy+3, crText, sti);
			}
		}
		public void setList(ArrayList<String> l){
			if(itm!=null)
				itm.clear();
			itm = null;
			itm = l;
		}
		public ArrayList<String> getList(){
			return itm;
		}		
		public ArrayList<String> getListt(){
			return itmm; // fazer as outras se precisar
		}				
		public Comp clear(){ 
			itm.clear(); 
			itmm.clear(); 
			itmmm.clear(); 
			itmmmm.clear(); 
			itmmmmm.clear(); 
			cChange = tmpChange; 
			return this;
		}
		public boolean onEnterPress(){			
			if(cEnter>0){
				cEnter =0;
				return true;
			}
			return false;
		}
		public boolean onDelPress(){			
			if(cTcDel>0){
				cTcDel =0;
				return true;
			}
			return false;
		}		
		public boolean onChange(){			
			// if(cClick==1) return true;
			
			// evitou um bug. Considera-se mudado bem depois de ter sido criado
			if(cCreate>0) return false;
			
			if(cChange>0){
				cChange=0;
				return true;
			}	
			if(focado==this)
				if(ms.dr!=0)
					return true;
			return false;	
		}
		public void refresh(){
			lstArqs(cam_,fil,ext);
		}
		public Comp lstArqs(String cm, String fl, String e){
			// e = extencao
			
			/*	INSTRUCOES
				Para listar todos os diretorios da pasta "meleca":
					MENTIRA! arquivos de qq extencao estao aparecendo tb
					cm="c://meleca//"
					fl=null
					e=null
			*/
			if(fl=="null") fl=null; // por desencargo de consciencia
			cm = J.emMinusc(cm);
			fl = J.emMinusc(fl);
			e  = J.emMinusc(e);
			clear(); 			
			fil = fl;
			ext = e;
			cam = cm;
			selMin=-1; // ISSO EH MUITO IMPORTANTE!!!
			cChange = tmp;
			setList(J.listarArqs(cm,fil,ext));
			select(0);
			return this;
		}
		public String getItem(int p){
			// fazer p outras colunas depois
			if(itm!=null)
			if(itm.size()>0)
			if(p<itm.size())
				return itm.get(p);
			return "???";
		}
		public boolean temItem(String st){
			st = J.emMaiusc(st);
			for(String it:itm)
				if(st.equals(J.emMaiusc(it)))
					return true;
			return false;	
		}
		public boolean temItemm(String st){
			st = J.emMaiusc(st);
			for(String it:itmm)
				if(st.equals(J.emMaiusc(it)))
					return true;
			return false;	
		}		
		public boolean temItemmm(String st){
			st = J.emMaiusc(st);
			for(String it:itmmm)
				if(st.equals(J.emMaiusc(it)))
					return true;
			return false;	
		}				
		public boolean temItemmmm(String st){
			st = J.emMaiusc(st);
			for(String it:itmmmm)
				if(st.equals(J.emMaiusc(it)))
					return true;
			return false;	
		}				
		public boolean temItemmmmm(String st){
			st = J.emMaiusc(st);
			for(String it:itmmmmm)
				if(st.equals(J.emMaiusc(it)))
					return true;
			return false;	
		}				
		


		public boolean onClickItem(String st){
			st = J.emMaiusc(st);
			if(cClick>0)
			if(itm.size()>0)
			if(J.noInt(sel,0,itm.size()-1))
			if(J.emMaiusc(itm.get(sel)).equals(st))
				return true;
			return false; 
		}		
		public boolean onClickItemm(String st){
			st = J.emMaiusc(st);
			if(cClick>0)
			if(itmm.size()>0)
			if(J.noInt(sel,0,itmm.size()-1))
			if(J.emMaiusc(itmm.get(sel)).equals(st))
				return true;
			return false; 
		}		
		public boolean onClickItemmm(String st){
			st = J.emMaiusc(st);
			if(cClick>0)
			if(itmmm.size()>0)
			if(J.noInt(sel,0,itmmm.size()-1))
			if(J.emMaiusc(itmmm.get(sel)).equals(st))
				return true;
			return false; 
		}		
		public boolean onClickItemmmm(String st){
			st = J.emMaiusc(st);
			if(cClick>0)
			if(itmmmm.size()>0)
			if(J.noInt(sel,0,itmmmm.size()-1))
			if(J.emMaiusc(itmmmm.get(sel)).equals(st))
				return true;
			return false; 
		}		
		public boolean onClickItemmmmm(String st){
			st = J.emMaiusc(st);
			if(cClick>0)
			if(itmmmmm.size()>0)
			if(J.noInt(sel,0,itmmmmm.size()-1))
			if(J.emMaiusc(itmmmmm.get(sel)).equals(st))
				return true;
			return false; 
		}		
		


		public int getNumItens(){
			return itm.size(); // eh isso mesmo. Jah conferido.
		}
		
		public boolean trocaItem(String sta, String stn){
			int oc = itm.indexOf(sta); // aprendi mais dois metodos bons
			if(oc==-1) return false; // string antigo nao encontrado
			itm.set(oc, stn); // substitui antigo pelo novo
			return true;
		}
		public boolean trocaItemm(String sta, String stn){
			int oc = itmm.indexOf(sta); 
			if(oc==-1) return false; 
			itmm.set(oc, stn);
			return true;
		}		
		public boolean trocaItemmm(String sta, String stn){
			int oc = itmmm.indexOf(sta); 
			if(oc==-1) return false; 
			itmmm.set(oc, stn);
			return true;
		}				
		public boolean trocaItemmmm(String sta, String stn){
			int oc = itmmmm.indexOf(sta); 
			if(oc==-1) return false; 
			itmmmm.set(oc, stn);
			return true;
		}				
		public boolean trocaItemmmmm(String sta, String stn){
			int oc = itmmmmm.indexOf(sta); 
			if(oc==-1) return false; 
			itmmmmm.set(oc, stn);
			return true;
		}					

		public Comp addItemSNT(String st){
			if(!temItem(st))
				addItem(st);
			return this;
		}
		public Comp addItem(String st){
			if(st==null) return this;
			if(st.equals("")) return this;
			if(st.equals(" ")) return this;
			cChange=tmpChange;
			st = J.semSpcIni(st);
			st = J.semSpcFinal(st);
			if(opAddInicioLista)
				itm.add(0,st);
			else
				itm.add(st); // no final mesmo
			cAdd=tmp;
			return this;
		}
		public Comp addItemm(String st){
			// sempre append na coluna do meio
			if(st==null) return this;
			if(st.equals("")) return this;
			if(st.equals(" ")) return this;
			cChange=tmpChange;
			
			st = J.semSpcIni(st);
			st = J.semSpcFinal(st);
			itmm.add(st);
			
			cAdd=tmp;
			return this;
		}		
		public Comp addItemmm(String st){
			if(st==null) return this;
			if(st.equals("")) return this;
			if(st.equals(" ")) return this;
			cChange=tmpChange;
			
			st = J.semSpcIni(st);
			st = J.semSpcFinal(st);
			itmmm.add(st);			
			
			cAdd=tmp;
			return this;
		}				
		public Comp addItemmmm(String st){
			if(st==null) return this;
			if(st.equals("")) return this;
			if(st.equals(" ")) return this;
			cChange=tmpChange;
			
			st = J.semSpcIni(st);
			st = J.semSpcFinal(st);
			itmmmm.add(st);			
			
			cAdd=tmp;
			return this;
		}				
		public Comp addItemmmmm(String st){
			if(st==null) return this;
			if(st.equals("")) return this;
			if(st.equals(" ")) return this;
			cChange=tmpChange;
			
			st = J.semSpcIni(st);
			st = J.semSpcFinal(st);
			itmmmmm.add(st);			
			
			cAdd=tmp;
			return this;
		}				
	
		public boolean remItem(){
			return remItem(sel);
		}
		public boolean remItem(int i){
			if(J.noInt(i, 0, itm.size()-1)){
				J.remItArrLst(i,itm);							
				J.remItArrLst(i,itmm);
				J.remItArrLst(i,itmmm);
				
				cChange=tmpChange;				
				return true;
			}	
			return false;
		}

		public boolean remItem(String st){
			if(st==null) return false;
			if(st.equals("")) return false;
			if(st.equals(" ")) return false;
			cChange=tmpChange;
			st = J.semSpcIni(st);
			st = J.semSpcFinal(st);
			st = J.emMaiusc(st);
			int c=-1;
			for(String it:itm){
				c++;
				if(st.equals( J.emMaiusc(it))){
					J.remItArrLst(c, itm);
					J.remItArrLst(c, itmm);
					J.remItArrLst(c, itmmm);
					return true; // sair logo p nao bugar
				}	
			}	
			return false;
		}		
		public boolean remItemm(String st){
			if(st==null) return false;
			if(st.equals("")) return false;
			if(st.equals(" ")) return false;
			cChange=tmpChange;
			st = J.semSpcIni(st);
			st = J.semSpcFinal(st);
			st = J.emMaiusc(st);
			int c=-1;
			for(String it:itmm){
				c++;
				if(st.equals( J.emMaiusc(it))){
					J.remItArrLst(c, itm);
					J.remItArrLst(c, itmm);
					J.remItArrLst(c, itmmm);
					return true; // sair logo p nao bugar
				}	
			}	
			return false;
		}		
		public boolean remItemmm(String st){
			if(st==null) return false;
			if(st.equals("")) return false;
			if(st.equals(" ")) return false;
			cChange=tmpChange;
			st = J.semSpcIni(st);
			st = J.semSpcFinal(st);
			st = J.emMaiusc(st);
			int c=-1;
			for(String it:itmmm){
				c++;
				if(st.equals( J.emMaiusc(it))){
					J.remItArrLst(c, itm);
					J.remItArrLst(c, itmm);
					J.remItArrLst(c, itmmm);
					return true; // sair logo p nao bugar
				}	
			}	
			return false;
		}		
		public boolean remItemmmm(String st){
			if(st==null) return false;
			if(st.equals("")) return false;
			if(st.equals(" ")) return false;
			cChange=tmpChange;
			st = J.semSpcIni(st);
			st = J.semSpcFinal(st);
			st = J.emMaiusc(st);
			int c=-1;
			for(String it:itmmmm){
				c++;
				if(st.equals( J.emMaiusc(it))){
					J.remItArrLst(c, itm);
					J.remItArrLst(c, itmm);
					J.remItArrLst(c, itmmm);
					J.remItArrLst(c, itmmmm);
					return true; // sair logo p nao bugar
				}	
			}	
			return false;
		}		
		public boolean remItemmmmm(String st){
			if(st==null) return false;
			if(st.equals("")) return false;
			if(st.equals(" ")) return false;
			cChange=tmpChange;
			st = J.semSpcIni(st);
			st = J.semSpcFinal(st);
			st = J.emMaiusc(st);
			int c=-1;
			for(String it:itmmmmm){
				c++;
				if(st.equals( J.emMaiusc(it))){
					J.remItArrLst(c, itm);
					J.remItArrLst(c, itmm);
					J.remItArrLst(c, itmmm);
					J.remItArrLst(c, itmmmm);
					J.remItArrLst(c, itmmmmm);
					return true; // sair logo p nao bugar
				}	
			}	
			return false;
		}		


		public Comp addItens(ArrayList<String> ls){ 
			for(String s:ls)
				addItem(s);
			return this;
		}
		public Comp addItens(String[] ls){ // bom p lista de arquivos
			// ACHO QUE ISSO PODE SER ELIMINADO
			// File arq = new File(""); 
	    // String[] lst = arq.list(); // isso retorna a listagem inteira do diretorio atual
			// lst = J.filtraExt(lst,"win");
			for(String st:ls)
				addItem(st);
			return this;
		}
		public Comp addItens(String st){
			// varios itens separados por virgula. Usei na abertura de s c r i p t de win
			String it="";
			char ch=0;
			for(int q=0; q<st.length(); q++){
				ch = st.charAt(q);
				if(ch!=',') it+=ch;
				else{
					addItem(it);
					it="";
				}
			}
			addItem(it); // o ultimo tb
			return this;
		}

		public boolean select(String par){
			par = J.limpaSt(par);
			par = J.emMaiusc(par);
			for(int c=0; c<itm.size(); c++){
				if(J.iguais(par, itm.get(c))){
					select(c);
					return true;
				}
			}
			J.esc("item nao encontrado: |"+par+"|");
			return false;
		}
		public boolean selectt(String par){
			par = J.limpaSt(par);
			par = J.emMaiusc(par);
			for(int c=0; c<itmm.size(); c++){
				if(J.iguais(par, itmm.get(c))){
					select(c);
					return true;
				}
			}
			return false;
		}		
		public boolean selecttt(String par){
			par = J.limpaSt(par);
			par = J.emMaiusc(par);
			for(int c=0; c<itmmm.size(); c++){
				if(J.iguais(par, itmmm.get(c))){
					select(c);
					return true;
				}
			}
			return false;
		}				
		public boolean selectttt(String par){
			par = J.limpaSt(par);
			par = J.emMaiusc(par);
			for(int c=0; c<itmmmm.size(); c++){
				if(J.iguais(par, itmmmm.get(c))){
					select(c);
					return true;
				}
			}
			return false;
		}				
		public boolean selecttttt(String par){
			par = J.limpaSt(par);
			par = J.emMaiusc(par);
			for(int c=0; c<itmmmmm.size(); c++){
				if(J.iguais(par, itmmmmm.get(c))){
					select(c);
					return true;
				}
			}
			return false;
		}				

		public void select(int i){
			if(i==-2) // melhor q isso fosse padrao
				if(itm!=null)
					i=itm.size()-1;
			
			if(itm!=null)
			if(itm.size()>0)
			if(J.noInt(i,0,itm.size()-1))	{
				sel = i;			
				setText(itm.get(sel));
				cChange = tmpChange;
			}
		}
		public void selectPrimChar(char ca){
			// procura o proximo item da lista com o caracter inicial "ca"
			// parece funcionar bem
			if(itm==null) return;
			if(itm.size()<=0) return;
			
			ca = J.emMaiusc(ca);
			char w=0;
			for(int c=sel+1; c<itm.size(); c++){
				w = itm.get(c).charAt(0);
				w = J.emMaiusc(w);
				if(w==ca){
					select(c);
					return;
				}
			}
		}
		public Comp addArq(String cam){
			// deveria reconhecer tabulacoes e inserir em listas duplas/triplas, mas jah funciona bem
			itm = J.openStrings(cam);
			cAdd=tmp;
			return this; 
		}

		public String getText(){
			if(itm==null) return "";
			if(itm.size()>0)
			if(J.noInt(sel,0,itm.size()-1))
				return itm.get(sel);				
			return "";
		}
		public String getTextt(){
			if(itmm==null) return "";
			if(itmm.size()>0)
			if(J.noInt(sel,0,itmm.size()-1))
				return itmm.get(sel);				
			return "";
		}		
		public String getTexttt(){
			if(itmmm==null) return "";
			if(itmmm.size()>0)
			if(J.noInt(sel,0,itmmm.size()-1))
				return itmmm.get(sel);				
			return "";
		}				
		public String getTextttt(){
			if(itmmmm==null) return "";
			if(itmmmm.size()>0)
			if(J.noInt(sel,0,itmmmm.size()-1))
				return itmmmm.get(sel);				
			return "";
		}				
		public String getTexttttt(){
			if(itmmmmm==null) return "";
			if(itmmmmm.size()>0)
			if(J.noInt(sel,0,itmmmmm.size()-1))
				return itmmmmm.get(sel);				
			return "";
		}						

		
		public String getScript(){
			// depois: guardar outras colunas
			String st = super.getScript();
			
			for(String s:itm)
				st+=" "+s+",";
			
			st = J.remUltChar(st);
			return st;
		}
	}

// === CAIXA DE LISTAGEM DE ARQUIVOS ==============
///////////////////
		Jf1 
			icoDir = new Jf1("fold02.jf1",139),
			icoJava = new Jf1("arq01.jf1",119),
			icoWav = new Jf1("arq01.jf1",89-3),
			icoTorr = new Jf1("arq01.jf1",219),
			icoPdf = new Jf1("arq01.jf1",59),
			icoMp3 = new Jf1("arq01.jf1",79),
			icoXls = new Jf1("arq01.jf1",69),
			icoDoc = new Jf1("arq01.jf1",89),
			icoFil = new Jf1("arq01.jf1",109),
			icoGen = new Jf1("arq01.jf1",239);
			//icoJf1 = new Jf1("arq01.jf1",69);
			
	public Comp addDirListBox(int i, int j, int ii, int jj){
		// SAIBA:	
		//    p personalizar o icone de um fonte java, faca com q a segunda linha do fonte tenha o formato: "//#icone.jf1"
		//    p deixar o icone como auto-ajuste, basta faze-lo null
		//    jah dah p mudara cor do texto de cada item, basta fazer itm.get(X).cr = J.cor[K]; Veja q aceita color, nao se limita a apenas a paleta.
		//    p listar apenas diretorios, ext = " "
		cAddComp=tmp;				
		Comp c = new DirListBox();
		if(lastWin!=null) c.lk=lastWin;		
		
		Comp p = packEm(i,j);
		if(p!=null)	{
			c.pk=p;		
			i-=p.x;
			j-=p.y;
		}	
				
		c.cCreate = tmp;				
		c.tip = DIR_LST_BOX;
		c.setName(newName(c.tip));
		c.x = i; c.y=j;		
		c.lar = ii;
		c.alt = jj;
		c.cAjuste = tmpArq;
		cmp.add(c);
		lastComp=c;		
		este=c;		
		return c;		
	}
	public class DirListBox extends Comp{
				public class Itm{
						String st="";
						Jf1 ico = null;
						boolean sl=false;
						Color cr=null; // se for nulo, imprime com a cor geral
					public Itm(String s, Jf1 f){
						st = s;
						ico = f;
					}
				}
			ArrayList<Itm> itm = new ArrayList<>();
//			int tab1=60, tab2=120; // remover depois
		public Comp addItem(String st){ 
			Itm i = new Itm(st,null);
			itm.add(i);
			return this; 
		}
		public void openList(String cm){
			ArrayList<String>ls = J.openStrings(cm);
			if(ls==null) return; // acho q J.java jah avisarah			
			itm.clear();
			for(String s:ls)
				itm.add(new Itm(s,null));
		}
		public void saveList(String cm){			
			ArrayList<String>ls = new ArrayList<>();
			for(Itm i:itm)
				ls.add(i.st);
			J.saveStrings(ls,cm);
			// ?e se nao conseguir salvar??? O controle deveria avisar. (J.java jah o faz pelo console, mas seria legal se a interface o fisesse)
			// Depois
		}
		public void ajuste(){
			// tab1= (int)(lar*0.33f);
			// tab2= (int)(lar*0.66f);
		}	
		public void reg(KeyEvent k){
			// p list e p comboBox q eh derivado desta
			super.reg(k);
			
			int cd=k.getKeyCode();
			
			if(cd==k.VK_ENTER) cEnter=tmp;			
			if(cd==k.VK_DELETE) cTcDel=tmp;			

			boolean c = k.isControlDown();
			boolean s = k.isShiftDown();
			boolean a = k.isAltDown();
			
			if(!c)
			if(!a)// p nao dar conflito com incremento de string de c o m b o  q eh derivada desta
			if(!s){
				if(cd==k.VK_UP)   select(sel-1);
				if(cd==k.VK_DOWN) select(sel+1);
			}

			if(ipl==-1)ipl=1;
			
			if(cd==k.VK_PAGE_DOWN) select(sel+ipl);
			if(cd==k.VK_PAGE_UP) select(sel-ipl);
			
			if(cd==k.VK_HOME) selPrim();
			if(cd==k.VK_END) selUlt();

			// removendo a selecao
			if(focado==this)	
			if(cd==k.VK_ESCAPE){
				// talvez isso devesse estar num metodo separado
				for(Itm it:itm)	it.sl=false;
			}
			
			// alternando a selecao com espaco
			if(opSelItmComEspaco)
			if(cd==k.VK_SPACE) { 
				// cSpace=tmp; ja ajustado em ss  c o m p 
				if(itm!=null)
				if(itm.size()>0)
					itm.get(sel).sl=!itm.get(sel).sl;
				cChange=tmpChange; 
			}			
			// estendendo a selecao com shift + cma/bxo
			if(s){ // vai dar conflito com resize do editor, mas nao eh problema
				boolean vai=false;
				if(cd==k.VK_DOWN)  {vai=true; sel = J.corrInt(++sel, 0, itm.size()-1); }
				if(cd==k.VK_UP)    {vai=true; sel = J.corrInt(--sel, 0, itm.size()-1); }
				if(vai){
					if(itm!=null)
					if(itm.size()>0)
						itm.get(sel).sl=true;
					cChange=tmpChange; 					
				}
			}			
			
			if(J.charRec(k.getKeyCode()))
				selectPrimChar(k.getKeyChar()); // este nao expande p  s s   c h e   e   s s   c h o 
		}
		public String getCam(){
			return cam;
		}
		public void imp(){
			calcAbs();
			
			if(cClick>0) cChange=tmp;
			
			impCx(crBox,crSomb,crLuz,xx,yy,lar,alt,LIVRE);
			 			

			Color crt=crText;
			if(blk) crt=crSomb;
			
			// itens
			//int al=14; // altura da linha
			int al=19;
			int ult=-1, recuo=20;
			ipl=0;
			String st="";
			Jf1 f = null;
			Color cr = null;
			if(selMin<0) selMin=0;
			if(itm!=null)
			if(itm.size()>0)
			for(int q=0; q<itm.size(); q++){
				ipl++;
				ult++;
				st = "000";
				
				if(q+selMin<itm.size()){
					st = itm.get(q+selMin).st;
					cr = itm.get(q+selMin).cr;
				}	else break;
					
				if(sel==q+selMin)
					J.impRetRel(crBt, null, xx+1, yy+q*al+2, lar-12, 16);
				if(itm.get(q+selMin).sl)
					J.impRetRel(null, crLuz, xx+1, yy+q*al+2, lar-12, 16);
/*
			crBt = J.cor[49],
			crText = J.cor[119],
			crFocus = J.cor[12], // essa eh automatica
			crSomb= crBt.darker(),
			crBox= crSomb.darker(),
			crLuz= crBt.brighter(),
			crBlk= crSomb,
			crBlkk= crSomb.darker();
*/				
				
				if(ms.b1 || ms.b2)
					if(!mnAberto)
					if(ppu==null)
					if(!blk)
					if(ms.naAreaRel(xx+1, yy+q*al+1, lar-12, al)){
						sel=q+selMin;
						cChange=tmpChange;
					}	
					
				if(itm.get(q+selMin).ico==null){
// JJJJJJJJJJJJJJJJJJJJJJJJJ

					String e = J.extArq(itm.get(q+selMin).st);
					
					// esse eh especial
					if(J.iguais(e,"jf1") && ((J.cont>>4)-q)%30==0) itm.get(q+selMin).ico = new Jf1(st);
					
					if(J.iguais(e,"java")) {
						// Da Hora:
						// 		p personalizar o icone do fonte java, faca com q a segunda linha do fonte tenha o formato: "//#icone.jf1"
						String cmj=cam+"//"+itm.get(q+selMin).st;
						String lf = J.leLinKarq(1, cam+"//"+itm.get(q+selMin).st);
						lf+=" ????????????????"; // p nao bugar
						if(lf.charAt(2)=='#')							
							itm.get(q+selMin).ico = new Jf1("c://java//jf1//"+J.truncarAntes('#',lf));
						else 
							itm.get(q+selMin).ico = icoJava;
					}	
					else if(J.iguais(e,"wav")) itm.get(q+selMin).ico = icoWav;
					else if(J.iguais(e,"pdf")) itm.get(q+selMin).ico = icoPdf;
					else if(J.iguais(e,"mp3")) itm.get(q+selMin).ico = icoMp3;
					else if(J.iguais(e,"doc")) itm.get(q+selMin).ico = icoDoc;
					else if(J.iguais(e,"docx")) itm.get(q+selMin).ico = icoDoc;
					else if(J.iguais(e,"xls")) itm.get(q+selMin).ico = icoXls;
					else if(J.iguais(e,"xlsx")) itm.get(q+selMin).ico = icoXls;
					else if(J.iguais(e,"avi")) itm.get(q+selMin).ico = icoFil;
					else if(J.iguais(e,"mkv")) itm.get(q+selMin).ico = icoFil;
					else if(J.iguais(e,"mp4")) itm.get(q+selMin).ico = icoFil;
					else if(J.iguais(e,"torrent")) itm.get(q+selMin).ico = icoTorr;
					else if(J.iguais(e,"jf1")) ; // nao faz nada
					else if(J.iguais(e,"")) itm.get(q+selMin).ico = icoDir;
					else itm.get(q+selMin).ico = icoGen;
				}
				if(itm.get(q+selMin).ico!=null)
					itm.get(q+selMin).ico.impJf1(xx+1,	yy+q*al);
				impText(recuo+xx+1, yy+q*al+14, (cr==null?crt:cr), J.truncLar(lar-recuo-5, st));
				
				if(yy+q*al>yy+alt-12-12-12) {
					ipl=q;
					break;
				}
			}
			
			J.impRet(crBox,null, xx+lar-12+1,yy,xx+lar+1,yy+alt);
			if(ult+selMin<sel) selMin=sel;
			if(sel<selMin) selMin--;
			if(itm!=null)
				if(sel>itm.size()-1) 
					sel--;
			if(sel<0)sel=0;
			
			// barra de rolagem
			fSeta.opRot=0;
			fSeta.impMask(crt, xx+lar-12-3,yy-2);
			fSeta.opRot=2;
			fSeta.impMask(crt, xx+lar-12-3,yy+alt-18);			
			if(itm!=null)
			if(itm.size()>1){
				int aq = (int)((alt-10-10-10-10) * ((sel)*100/(itm.size()-1))/100f);
				aq+=14;
				fQuad.impMask(crt, xx+lar-10,yy+1+aq);				
			}
			
	
			if(focado==this) impFocus(xx,yy,lar,alt);
			if(blk)	impContBlk(xx,yy,lar,alt);						
			
			if(focado==this)
			if(itm.size()>0){
				String sti = ""+(sel+1)+" / "+(itm.size());
				int d = J.larText(sti);
				J.impRetRel(crBox, crLuz, xx+lar-d, yy+3-12, d, 14);
				impText(xx+lar-d, yy+3, crText, sti);
			}
		}
		public Comp clear(){ 
			itm.clear(); 
			cChange = tmpChange; 
			return this;
		}
		public boolean onEnterPress(){			
			if(cEnter>0){
				cEnter =0;
				return true;
			}
			return false;
		}
		public boolean onDelPress(){			
			if(cTcDel>0){
				cTcDel =0;
				return true;
			}
			return false;
		}		
		public boolean onChange(){			
			// if(cClick==1) return true;
			
			// evitou um bug. Considera-se mudado bem depois de ter sido criado
			if(cCreate>0) return false;
			
			if(cChange>0){
				cChange=0;
				return true;
			}	
			if(focado==this)
				if(ms.dr!=0)
					return true;
			return false;	
		}
		public Comp setCam(String st){
			// use lstArqs p mais detalhes
			cam_=st;
			refresh();
			return this;
		}
		public void refresh(){ // tag relist
			lstArqs(cam_,fil,ext);
		}
		public Comp lstArqs(String cm, String fl, String e){
			// e = extencao
			
			/*	INSTRUCOES
				Para listar todos os diretorios da pasta "meleca":
					MENTIRA! arquivos de qq extencao estao aparecendo tb
					TESTAR COM e=" ", exatamente assim
					cm="c://meleca//"
					fl=null
					e=null
			*/
			if(fl=="null") fl=null; // por desencargo de consciencia
			cm = J.emMinusc(cm);
			fl = J.emMinusc(fl);
			e  = J.emMinusc(e);
			
			cam_=cm;
			
			clear(); 			
			fil = fl;
			ext = e;
			cam = cm;
			selMin=-1; // ISSO EH MUITO IMPORTANTE!!!
			cChange = tmp;
			setList(J.listarArqs(cm,fil,ext));
			select(0);
			return this;
		}
		public void setList(ArrayList<String> l){
			if(itm!=null)
				itm.clear();
			itm = new ArrayList<>();
			for(String s:l)
				itm.add(new Itm(s,null));
		}		
		public String getItem(int p){
			if(itm!=null)
			if(itm.size()>0)
			if(p<itm.size())
				return itm.get(p).st;
			return "???";
		}
		public boolean temItem(String st){
			st = J.emMaiusc(st);
			for(Itm it:itm)
				//if(st.equals(J.emMaiusc(it)))
				if(J.iguais(st, it.st))
					return true;
			return false;	
		}
		public void setIcon(Jf1 f, int i){
			if(itm!=null)
			if(itm.size()>=0)	
			if(J.noInt(i,0,itm.size()-1))
			if(f!=null)	
				itm.get(i).ico = f;	
			// deveria avisar um erro aqui
		}
		public boolean onClickItem(String st){
			st = J.emMaiusc(st);
			if(cClick>0)
			if(itm.size()>0)
			if(J.noInt(sel,0,itm.size()-1))
			//if(J.emMaiusc(itm.get(sel)).equals(st))
			if(J.iguais(st, itm.get(sel).st))	
				return true;
			return false; 
		}		
		public int getNumItens(){
			return itm.size(); // eh isso mesmo. Jah conferido.
		}
		public boolean trocaItem(String sta, String stn, Jf1 f){
			for(Itm it:itm)
			if(J.iguais(it.st, sta)){
				it.st=stn;
				it.ico = f; // sempre existirah uma  i m a g e m  aqui
				return true;
			}
			// int oc = itm.indexOf(sta); // aprendi mais dois metodos bons
			// if(oc==-1) return false; // string antigo nao encontrado
			// itm.set(oc, stn); // substitui antigo pelo novo
			return false;
		}
		public Comp addItem(String st, Jf1 f){
			if(st==null) return this;
			if(st.equals("")) return this;
			if(st.equals(" ")) return this;
			cChange=tmpChange;
			st = J.semSpcIni(st);
			st = J.semSpcFinal(st);
			Itm it = new Itm(st,f);
			if(opAddInicioLista)
				itm.add(0,it);
			else
				itm.add(it); // no final mesmo
			cAdd=tmp;
			return this;
		}
		public boolean remItem(){
			return remItem(sel);
		}
		public boolean remItem(int i){
			if(J.noInt(i, 0, itm.size()-1)){
				cChange=tmpChange;				
				J.remItArrLst(i,itm);				
				return true;
			}	
			return false;
		}
		public boolean remItem(String st){
			if(st==null) return false;
			if(st.equals("")) return false;
			if(st.equals(" ")) return false;
			cChange=tmpChange;
			st = J.semSpcIni(st);
			st = J.semSpcFinal(st);
			st = J.emMaiusc(st);
			int c=-1;
			for(Itm it:itm){
				c++;
				if(J.iguais(st, it.st)){
					J.remItArrLst(c, itm);
					return true; // sair logo p nao bugar
				}	
			}	
			return false;
		}		
		public boolean select(String par){
			par = J.limpaSt(par);
			par = J.emMaiusc(par);
			for(int c=0; c<itm.size(); c++){
				if(J.iguais(par, itm.get(c).st)){
					select(c);
					return true;
				}
			}
			J.esc("item nao encontrado: |"+par+"|");
			return false;
		}
		public void select(int i){
			if(i==-2) // melhor q isso fosse padrao
				if(itm!=null)
					i=itm.size()-1;
			
			if(itm!=null)
			if(itm.size()>0)
			if(J.noInt(i,0,itm.size()-1))	{
				sel = i;
				setText(itm.get(sel).st);
				cChange = tmpChange;
			}
		}
		public void selectPrimChar(char ca){
			// procura o proximo item da lista com o caracter inicial "ca"
			// parece funcionar bem
			if(itm==null) return;
			if(itm.size()<=0) return;
			
			ca = J.emMaiusc(ca);
			char w=0;
			for(int c=sel+1; c<itm.size(); c++){
				w = itm.get(c).st.charAt(0);
				w = J.emMaiusc(w);
				if(w==ca){
					select(c);
					return;
				}
			}
		}
		public String getText(){
			if(itm==null) return "";
			if(itm.size()>0)
			if(J.noInt(sel,0,itm.size()-1))
				return itm.get(sel).st;				
			return "";
		}
		public String getScript(){
			String st = super.getScript();
			
			st+=cam_;
			return st;
		}
	}

// === CAIXA DE CHECAGEM (q nao eh uma lista)================
// nao eh uma lista, eh apenas um item avulso, nao confunda com c h e c k L i s t
// acho q esta classe vai desbancar o   c h e c k L i s t. Parece mais pratica de usar.
	public Comp addCheckBox(String st, String nm){ 
		return addCheckBox(false, st,nm,xIns, yIns+=16);
	}
	public Comp addCheckBox(String st, String nm, int i, int j){ 
		return addCheckBox(false, st,nm,i,j);
	}
	public Comp addCheckBox(Boolean v, String st, String nm){ 
		return addCheckBox(v,st,nm,xIns, yIns+=16);
	}
	public Comp addCheckBox(Boolean v, String st, String nm, int i, int j){ 
		// altura e largura deduzidas
		// nm null gera um nome na sequencia: o pc se vira p gerar o nome
		xIns=i; yIns=j+5;
		cAddComp=tmp;				
		Comp c = new CheckBox();
		if(lastWin!=null) c.lk=lastWin;		
		
		Comp p = packEm(i,j);
		if(p!=null)	{
			c.pk=p;		
			i-=p.x;
			j-=p.y;
		}	
				
		c.cCreate = tmp;				
		c.tip = CHE_BOX; // nao eh  c h e c k L i s t , nao confunda
		
		if(nm==null) 	c.setName(newName(c.tip));
		else	c.setName(nm);
		
		c.canTog=true; // viu como parece um bt com tog???
		c.tog=v; // ?a sobreposicao abaixo tah boa???
		if(st.charAt(0)=='+') {c.tog=true; st=J.remPrimChar(st); }
		if(st.charAt(0)=='-') {c.tog=false; st=J.remPrimChar(st); }		
		
		c.setText(st);
		c.x = i; c.y=j;		
		c.lar = J.larText(st)+32;
		c.alt = 16;
		c.cAjuste = tmpArq;
		cmp.add(c);
		lastComp=c;		
		este=c;		
		return c;					
	}
	public class CheckBox extends TextButton{
		public void reg(KeyEvent k){
			if(k.getKeyCode()==k.VK_ENTER) {tog=!tog; cChange=tmp; }
			if(k.getKeyCode()==k.VK_SPACE) {tog=!tog; cChange=tmp; }
		}
		public void imp(){
			calcAbs();
			//J.impRetRel(crBt,null,xx,yy,lar,alt);			
			 			
			Jf1 f=null;
			int c=-1;
			Color crt = crText;
			if(blk) crt=crSomb;

				impText(xx+21,yy+12, crt, text);
				if(tog) f = fCheOn;
				else f = fCheOff;
				
				if(!blk)
					f.impJf1(xx,yy-4);
				else
					f.impMask(crSomb, xx,yy-4);
			if(blk)	J.impRetRel(null, crSomb, xx-3,yy-3,lar+7,alt+7);
			if(focado==this) impFocus(xx,yy,lar,alt);
		}
		public String getScript(){
			//String st = super.getScript()+": ";
			String st = super.getScript();
			//if(tog)st+="+"; // '-' implicito
			//st+=text;
			return st;
		}
	}
	

// === LISTA DE CHECAGEM ================
	public Comp addCheckList(int i, int j, int ii, int jj){
		cAddComp=tmp;				
		Comp c = new CheckList();
		if(lastWin!=null) c.lk=lastWin;		
		
		Comp p = packEm(i,j);
		if(p!=null)	{
			c.pk=p;		
			i-=p.x;
			j-=p.y;
		}	
				
		c.cCreate = tmp;				
		c.tip = CHE_LST;
		c.setName(newName(c.tip));
		c.x = i; c.y=j;		
		c.lar = ii;
		c.alt = jj;
		c.cAjuste = tmpArq;
		cmp.add(c);
		lastComp=c;		
		este=c;		
		return c;		
	}
	public class CheckList extends ChoiceList{
		public void reg(KeyEvent k){
			int cd = k.getKeyCode();
			
			if(cd==k.VK_ENTER
			|| cd==k.VK_SPACE) { 
				// cEnter=tmp;		// ja ajustados em ss  c o m p 
				// cSpace=tmp;
				altVal(sel);  // veja q este eh ALT e nao SET
				cChange=tmpChange; 
			}

			if(cd==k.VK_UP)   selAnt();
			if(cd==k.VK_DOWN) selProx();			
			
		}
		public void imp(){
			calcAbs();

			J.impRetRel(crBt,null,xx,yy,lar,alt);			
			 			
			Jf1 f=null;
			int c=-1;
			Color crt = crText;
			String st="";
			if(blk) crt=crSomb;
			for(Item it:itm){
				c++;

				if(c==sel)
					J.impRetRel(null, crFocus, xx+2,yy+c*al,lar-4,al);				
					//J.impRetRel(null, crLuz, xx+2,yy+c*al,lar-4,al);				


				impText(xx+21,yy+c*al+al-4, crt, it.text);
				if(it.val) f = fCheOn;
				else f = fCheOff;
				
				if(!blk)
					f.impJf1(xx,yy+c*al-4);
				else
					f.impMask(crSomb, xx,yy+c*al-4);

				if(!blk)
				if(cChange<=0)
				if(ms.b1)
				if(ms.naAreaRel(xx,yy+c*al,lar,al)){
					altVal(c);
					select(c);
					cChange = tmp;
				}
			}			
			

			if(blk)	J.impRetRel(null, crSomb, xx-3,yy-3,lar+7,alt+7);
			if(focado==this) impFocus(xx,yy,lar,alt);
		}
		public String getText(){
			if(itm!=null)
			if(itm.size()>0)	
			if(J.noInt(sel,0,itm.size()-1))
				return itm.get(sel).text;
			return "???";
		}
		public Comp setAll(boolean v){
			for(Item i:itm)
				i.val = v;
			return this;
		}
		public Comp setVal(boolean v, String st){
			for(Item i:itm)
				if(J.iguais(i.text,st))
					i.val = v;
			return this;	
		}
		public Comp setVal(boolean v, int i){
			if(itm!=null)
			if(itm.size()>0)
			if(J.noInt(i,0,itm.size()-1)){
				itm.get(i).val = v;
				cChange = tmp;
			}	
			return this;
		}
		public Comp altVal(int i){
			if(itm!=null)
			if(itm.size()>0)
			if(J.noInt(i,0,itm.size()-1)){
				itm.get(i).val = !itm.get(i).val;
				cChange = tmp;
			}	
			return this;
		}
		public boolean getVal(int i){
			if(itm!=null)
			if(itm.size()>0)
			if(J.noInt(i,0,itm.size()-1)){
				return itm.get(i).val;
			}				
			return false; 
		} 
	}
	
	
// === CAIXA DE ESCOLHA ================
// =  r a d i o   b u t t o n 
	public Comp addChoiceList(int i, int j, int ii, int jj){
		cAddComp=tmp;				
		Comp c = new ChoiceList();
		if(lastWin!=null) c.lk=lastWin;		
		
		Comp p = packEm(i,j);
		if(p!=null)	{
			c.pk=p;		
			i-=p.x;
			j-=p.y;
		}	

		c.cCreate = tmp;		
		c.tip = CHO_LST;
		c.setName(newName(c.tip));
		c.x = i; c.y=j;		
		c.lar = ii;
		c.alt = jj;
		c.cAjuste = tmpArq;
		cmp.add(c);
		lastComp=c;		
		este=c;		
		return c;		
	}
	public class ChoiceList extends ListBox{
			ArrayList<Item>itm = new ArrayList<>();
			public class Item {String text=""; boolean val=false; }
			int al=16; 
		public void reg(KeyEvent k){
			super.reg(k);
			int cd = k.getKeyCode();
			
			if(cd==k.VK_ENTER
			|| cd==k.VK_SPACE) { 
				// cEnter=tmp;		// ja ajustados em ss  c o m p 
				// cSpace=tmp;
				setVal(sel); 
				cChange=tmpChange; 
			}

//			if(cd==k.VK_UP)   selAnt();
//			if(cd==k.VK_DOWN) selProx();			
		}
		public void imp(){
			calcAbs();

			J.impRetRel(crBt,null,xx,yy,lar,alt);			
			 			
			Jf1 f=null;
			int c=-1;
			Color crt = crText;
			String st="";
			if(blk) crt=crSomb;
			for(Item it:itm){
				c++;

				if(c==sel)
					J.impRetRel(null, crFocus, xx+2,yy+c*al,lar-4,al);				
					//J.impRetRel(null, crLuz, xx+2,yy+c*al,lar-4,al);				

	
				st = it.text;
				if(J.vou(st.charAt(0),'+','-')) st = J.remPrimChar(st);

				impText(xx+21,yy+c*al+al-4, crt, st);
				if(it.val) f = fChoOn;
				else f = fChoOff;
				
				if(!blk)
					f.impJf1(xx,yy+c*al-4);
				else
					f.impMask(crSomb, xx,yy+c*al-4);

				if(!blk)
				if(ms.b1)
				if(cChange<=0)
				if(ms.naAreaRel(xx,yy+c*al,lar,al)){
					select(c);					
					setVal(c);					
					cChange = tmp;
				}					
				

			}			
			

			if(blk)	J.impRetRel(null, crSomb, xx-3,yy-3,lar+7,alt+7);
			if(focado==this) impFocus(xx,yy,lar,alt);
		}
		public Comp addArq(String cam){
			ArrayList<String> ls = J.openStrings(cam);
			for(String st:ls)
				addItem( 
					(st.charAt(0)=='+'), 
					J.trocaChar('+',' ',st));
			return this; 
		}		
		public Comp setVal(int i){
			// podia alterar  c C h a n g e  apenas se o valor foi alterado, mas isso jah quebra o galho.
			cChange = tmp; 
			
			// seta apenas o item indicado
			// todos os outros ficam "false"
			int c=-1;
			for(Item it:itm){
				c++;
				it.val= (c==i);
			}		
			return this;
		}
		public Comp addItens(String st){
			// varios itens separados por virgula.
			// "+blablabla" jah deixa o item ticado
			String it="";
			char ch=0;
			for(int q=0; q<st.length(); q++){
				ch = st.charAt(q);
				if(ch!=',') it+=ch;
				else{
					addItem(
						J.tem('+',it), 
						J.remChar('+',it));
					it="";
				}
			}
			addItem(it); // o ultimo tb
			return this;
		}
		public Comp addItem(String st){
			return addItem(false, st);
		}
		public Comp addItem(boolean v, String st){			
			// maiusculas???
			if(st.charAt(0)=='+'){
				v=true;
				st = J.remPrimChar(st);
			}
			if(st.charAt(0)=='-'){
				v=false;
				st = J.remPrimChar(st);
			}			

			st = J.limpaSt(st);
			cAjuste = 2;
			if(st!=null)
			if(!st.equals("")){
				Item it = new Item();
				it.val = v;
				it.text = J.semSpcIni(st);
				itm.add(it);
			}
			return this;
		}
		public boolean remItem(String st){
			// maiusculas???
			cAjuste = 2;			
			for(Item it:itm)
				if(it.text.equals(st)){
					J.remItArrLst(it,itm); // será q buga aqui???
					return true;
				}
			return false;
		}
		public boolean select(String st){
			// maiusculas???			
			int c=-1;
			for(Item it:itm){
				c++;
				if(it.text.equals(st)){
					sel=c;
					return true;
				}	
			}	
			return false;	
		}
		public String getText(){
			for(Item it:itm)
				if(it.val){
					String st = it.text;
					st = J.trocaChar('+',' ',st);
					st = J.limpaSt(st);
					return st;
				}	
			return "???";
		}

		public void ajuste(){
			alt = al*itm.size();
			for(int c=0; c<itm.size(); c++)
				if(J.comecaCom(itm.get(c).text,"+")){
					setVal(c);
					itm.get(c).text = J.remPrimChar(itm.get(c).text);
				}
		}
		public Comp clear(){
			itm.clear();
			return this;
		}		
		public boolean temItem(String st){
			st = J.emMaiusc(st);
			for(Item it:itm)
				if(st.equals(J.emMaiusc(it.text)))
					return true;
			return false;	
		}
		public void select(int i){
			// precisou repetir aqui pq itm nao eh o mesmo de super
			sel = J.corrInt(i, 0,itm.size()-1);			
			if(itm.size()<=0) sel=0;
		}
		public boolean getVal(String st){
			// retornara o boolean correspondente ao texto exibido no item
			if(st==null)return false;
			if(st.equals(""))return false;
			if(st.equals(" "))return false;
			if(st.equals("???"))return false;
			
			st = J.emMaiusc(st);
			for(Item it:itm)
				if(st.equals(J.remChar('+',J.emMaiusc(it.text))))
					return it.val;
			
			// deve facilitar p debug de qq programa q utilize JWin.java
			J.esc("===================");
			for(Item it:itm)
				J.esc(""+(it.val?"+":'-')+"  "+it.text);
			J.esc("===================");				
			J.esc("acima, todos os valores de "+getName()+": ");
			J.impErr("!texto nao encontrado: |"+st+"|", "JWin.CheckList.getVal() ou ChoiceList()");
			
			return false;	
		}
		public boolean trocaItem(String sta, String stn){
			sta = J.emMaiusc(sta);
			for(Item it:itm)
				if(J.emMaiusc(it.text).equals(sta)){
					it.text = stn;
					return true;
				}	
			return false;
		}
		public int getNumItens(){
			return itm.size();
		}
		public boolean onClickItem(String st){
			st = J.emMaiusc(st);
			if(cClick>0)
			if(itm.size()>0)
			if(J.noInt(sel,0,itm.size()-1))
			if(J.emMaiusc(itm.get(sel).text).equals(st))
				return true;
			return false; 
		}		
		public String getScript(){
			String st = super.getScript()+":";
			for(Item it:itm){
				st+= " "+ (it.val?"+":"");
				st+= it.text+",";
			}
			st = J.remUltChar(st);
			return st;
		}
	}
	
// === CAIXA DE COMBINACAO ==============
	
	public Comp addComboBox(int i, int j, int ii, int jj){
		cAddComp=tmp;				
		Comp c = new ComboBox();
		if(lastWin!=null) c.lk=lastWin;		
		
		Comp p = packEm(i,j);
		if(p!=null)	{
			c.pk=p;		
			i-=p.x;
			j-=p.y;
		}	
				
		c.cCreate = tmp;				
		c.tip = CMB_BOX;
		c.setName(newName(c.tip));
		c.x = i; c.y=j;		
		c.lar = ii;
		c.alt = jj;
		c.cAjuste = tmpArq;
		cmp.add(c);
		lastComp=c;		
		este=c;		
		return c;		
	}
	public class ComboBox extends ListBox{
			int 
				al=16; // altura do controle quando fechado
		public void reg(KeyEvent k){
			super.reg(k);
			int cd = k.getKeyCode();
			char ca = k.getKeyChar();
			
			boolean s = k.isShiftDown();
			boolean c = k.isControlDown();
			boolean a = k.isAltDown();								
			
			if(aberto){
				if(cd==k.VK_UP) if(s) {aberto=false; return; }
				if(cd==k.VK_ESCAPE) {aberto=false; return; }
				
				if(cd==k.VK_ENTER){
					text = itm.get(sel);
					aberto=false; 
					return;
				}
				
				if(J.charRec(k.getKeyCode()))
					selectPrimChar(k.getKeyChar());
			}
			if(!aberto){ 
					// cod adaptado de textBox
					boolean semSel= selMax==-1;
					boolean temSel=!semSel;
					
					if(cur==-1) cur=0;// sem bug???				
					
					if(cd==k.VK_ENTER) if(canAdd){ // veio de BD						
						itm.add(text);
						select(text);
						selAll();
					}
					
					if(cd==k.VK_DOWN) if(s) {aberto=true; wPop.play(); return; }
		
					if(J.charRec(ca)) if(cd!=222){ // ignorando aspas
					
						wTec.play();
						
						cChange = tmpChange; 
						if(semSel){ 
							text = J.insChar(ca, text, cur);
							cur++;
							return;
						} else {
							text = J.remChars(selMin,selMax, text);
							text = J.insChar(ca, text, selMin);
							cur = selMin+1;
							selMin=selMax=-1;
						}
					}
					
					if(!s){
						if(cd==k.VK_RIGHT) { cur++; selMin=selMax=-1; }
						if(cd==k.VK_LEFT) { cur--; selMin=selMax=-1; }
						if(cd==k.VK_HOME) { cur=0; selMin=selMax=-1; }
						if(cd==k.VK_END) { cur=text.length(); selMin=selMax=-1; }
					}
					if(s){
						if(cd==k.VK_RIGHT){					
							if(temSel){
								if(cur==selMax+1) selMax++;
								if(cur==selMin) selMin++;
								cur++;
							} else {
								selMin=selMax=cur;
								cur++;
							}
						}	
						if(cd==k.VK_LEFT){					
							if(temSel){
								if(cur==selMax+1) selMax--;
								if(cur==selMin) selMin--;
								cur--;
							} else {
								cur--;
								selMin=selMax=cur;
							}
						}	
						if(cd==k.VK_HOME) {
							selMax=cur-1; 
							cur=selMin=0; 
						}
						if(cd==k.VK_END) {
							if(text==null){
								cur=0;
								selMin=-1;
								selMax=-1;
							} else {
								selMin=cur; 
								cur=selMax=text.length();
							}
						}
					}			
					
					if(cd==k.VK_DELETE) {
						wDel.play();				
						cChange = tmpChange;
						if(semSel)
							text = J.remChar(cur, text);
						else{
							text = J.remChars(selMin,selMax,text);
							cur=selMin;
							selMin=selMax=-1;
						}	
					}	
					if(cd==k.VK_BACK_SPACE){
						wDel.play();	
						cChange = tmpChange;
						if(semSel)
							text = J.remChar(--cur, text);
						else{
							text = J.remChars(selMin,selMax,text);
							cur=selMin;
							selMin=selMax=-1;					
						}
					}	

					if(text!=null)
						cur = J.corrInt(cur, 0,text.length());			




			}
		}
		public void reg(){
			super.reg();
			
			if(canMod)
			if(onClick()) 
				selAll(); // o certo seria "o n G e t F o c u s()". Depois.			
			
			if(focado!=this) {
				aberto=false;
				mnAberto=false;				
			}	
				
			if(cFecha==1){
				aberto=false;
				mnAberto=false;
				if(itm!=null)
				if(itm.size()>0)
				if(sel<itm.size())
					text = itm.get(sel);
			}						
			

		}	
		public Comp setText(String st){
			text = st;
			if(itm.size()>0)
			if(J.noInt(sel,0,itm.size()-1))	
				itm.set(sel,st);
			return this;
		}
		public String getText(){
			if(!aberto){
				return text;
			} else {
				if(itm!=null)
				if(itm.size()>0)
				if(J.noInt(sel,0,itm.size()-1))
					return itm.get(sel);				
			}
			return "";
		}
		public void imp(){

			impFechada();
			if(aberto) 
				if(!blk)
					impAberta();
		}
		public void impFechada(){
			calcAbs();

			impCx(crBox,crSomb,crLuz,xx,yy,lar,al,LIVRE);
			{// uma pequena gambiarra
				int r=alt; 
				alt=al; 
				 							
				alt = r;
			}
			Color crt=crText;
			if(blk)crt=crSomb;
			if(focado==this) impFocus(xx,yy,lar,al);

			String st = "";
			// opEditavel = false;
			if(canMod){
				st=text+" ";			
				Color cr=null;
				char ch=0;
				int dc =0;
				for(int q=0; q<st.length(); q++){
					cr=crBox;
					ch = st.charAt(q);				
					
					// texto destacado
					if(J.noInt(q, selMin, selMax)) cr= (blk?crBt:crLuz);
					// if(J.noInt(q, selMin, selMax)) cr=J.cor[12];
					J.impRetRel(cr,null, xx+3+dc,yy+1, J.larChar(ch),14);
					
					// cursor
					if(cur==q)
					if(focado==this){
						Color crrr = (J.cont%60<30?J.cor[15]:J.cor[16]);
						J.impRetRel(crrr,null, xx+dc,yy+1, 1,14);
						//J.impRetRel(crrr,null, xx+2+dc,yy+dl+1, 1,14);					
					}
					
						
					impCh(xx+1+dc,yy+9+3,crt, ch);
					dc+=J.larChar(ch);
				}	

			} else {
				st = "";
				if(itm.size()>0)
					if(J.noInt(sel,0,itm.size()-1))
						st = itm.get(sel);
				impText(xx+2,yy+12,crt,st);
			}
			fSeta.opRot= (aberto?0:2);
			fSeta.impMask(crt, xx+lar-12-6,yy-3);
			
			//ms.opImpArea=true;
			if(ms.b1)
			if(!blk)	
			if(cClick<=0)
			if(!aberto)
			if(ms.naAreaRel(xx,yy,lar,16)){
				aberto=true;
				wPop.play();
			}	
			
			if(blk)	J.impRetRel(null, crSomb, xx-3,yy-3,lar+7,al+7);
			if(focado==this) impFocus(xx,yy,lar,16);			
		}
		public void impAberta(){			
			mnAberto=true;
			calcAbs();
			
			impCx(crBox, crSomb, crLuz, xx,yy,lar,alt,LIVRE);
			 
			
			// itens
			int al=14; // altura da linha
			int ult=-1;
			String st="";
			if(selMin<0) selMin=0;
			if(itm.size()>0)
			for(int q=0; q<itm.size(); q++){
				ult++;
				st = ">>>";
				if(q+selMin<itm.size())
					st = itm.get(q+selMin);
					else break;
					
				if(sel==q+selMin)
					J.impRetRel(crBt, null, xx+1, yy+q*al+1, lar-12, 14);
				

				if(ms.b1)	if(cClick<=0)	
				if(ms.naAreaRel(xx+1, yy+q*al+1, lar-12, 14))
				{
						cClick=tmp;
						cChange=tmp;
						select(q+selMin);
						if(ms.y>yy+6) {
							cFecha=tmp; // esse fecha a caixa.
						}
						return; // isso faz um pequeno  f l i c k  ao clicar
					}	

					
				impText(xx+1,yy+q*al+12, crText, st);
				
				if(yy+q*al>yy+alt-12-12-6) break;
			}
			if(ult+selMin<sel) selMin++;
			if(sel<selMin) selMin--;
			if(sel>itm.size()-1) sel--;
			if(sel<0)sel=0;		
			// barra de rolagem
			fSeta.opRot=0;
			fSeta.impMask(crText, xx+lar-12-3,yy-2);
			fSeta.opRot=2;
			fSeta.impMask(crText, xx+lar-12-3,yy+alt-18);			
			if(itm.size()>1){
				int aq = (int)((alt-10-10-10-10) * (sel*100/(itm.size()-1))/100f);
				aq+=14;
				fQuad.impMask(crText, xx+lar-10,yy+1+aq);				
			}
			
	
			if(focado==this) impFocus(xx,yy,lar,alt);
			
			if(focado==this)	if(itm.size()>0){
				String stt = ""+(sel+1)+" / "+(itm.size());
				int d = J.larText(stt);
				yy-=16;
				J.impRetRel(crBox, crLuz, xx+lar-d, yy+7, d, 14);
				impText(xx+lar-d, yy+7+12, crText, stt);
				
			}			
		}
		public boolean onChange(){			
			// if(cClick==1) return true;
			
			// evitou um bug. Considera-se mudado bem depois de ter sido criado
			if(cCreate>0) return false;
			
			if(cChange>0){
				cChange=0;
				return true;
			}	
			if(focado==this)
				if(ms.dr!=0)
					return true;
			return false;	
		}

	}

// === BANCO DE DADOS ===================
/*	A FAZER NO COMP BD
ok		assentar registro na mem RAM quando pressionar ENTER			
ok		assentar registro na mem RAM quando pressionar apostrofo			
			assentar registro na mem RAM uma cx de texto perder o foco (ao clicar em outro comp)
			salvar o arrLst de registros quando fechar um fonte... mas isso seria no fonte-destino, nao aqui.

*/
	public Comp addDataBase(String cm, int i, int j, int ii, int jj){
		cAddComp=tmp;				
		Comp c = new DataBase();
		if(lastWin!=null) c.lk=lastWin;		
		
		Comp p = packEm(i,j);
		if(p!=null)	{
			c.pk=p;		
			i-=p.x;
			j-=p.y;
		}	

		c.cCreate = tmp;
		c.tip = DB;
		c.cam = cm;
		c.setTag(cm); // parece bom
		c.setName(newName(c.tip));
		c.x = i; c.y=j;
		c.lar = ii;
		c.alt = jj;
		c.cAjuste = tmpArq;
		cmp.add(c);
		lastComp=c;
		lastBD=c; // isso deve ajudar... cuidado se for abrir mais de um BD e for automatizar o save usando essa var.
		este=c;
		return c;
	}
	public class DataBase extends Comp{ // ss bd ss db
			ArrayList<String> rec = new ArrayList<>();
				public Comp save(){
					saveRec(); // s? p garantir
					J.saveStrings(rec,cam); // "ok" embutido... muito pratico :)
					return this;
				}
				public Comp open(){
// ?e se o arq nao for encontrado???					
					if(!J.arqExist(cam)) {
						 J.impErr("banco de dados nao encontrado: |"+cam+"|","JWin.DataBase.open()");
						 cam="bd.txt"; // tirar esta gambiarra depois						 
						 J.esc("banco de dados substituido por: |"+cam+"|");
					}
					ArrayList<String> lis = J.openStrings(cam);
					rec = J.remLinVazia(lis);					
					showRec();
					return this;
				}
				private void showRec(){
					if(sel==0) sel=1; // cabecalho nao
// e se text for nulo ou vazio???
					String tx = J.remChar(';',rec.get(0));
					int nc = J.numPalavras(tx);
					String v="",cc="";
					for(int q=0; q<nc; q++){
						cc = J.palavraK(q, tx);
						v = J.palavraK(q,rec.get(sel));
						v = J.remChar(';',v);
						v = J.trocaChar('_',' ',v);
						if(temComp(cc)) getComp(cc).setText(v);
					}
				}
		public void delRec(){
			if(!canDel) return;
			J.remItArrLst(sel, rec); // foi???
			cChange=tmpChange;
		}						
		public void newRec(){
			if(!canAdd) return;
			rec.add("");
			sel = rec.size()-1;
			showRec();
			cChange=tmpChange;
		}								
		public ArrayList<String> getList(){
			return rec;
		}
		public void saveRec(){ // record
			if(!canMod) return; // parece bom bloquear as cxs de texto e similares p melhorar
			// apenas assenta o registro atual, sem mecher em arquivos
			// chamada geral lah em cima.
			// quando se pressiona ENTER em qq controle E existe um banco de dados na listagem cmp este metodo serah acionado
			if(sel==0) return; // cabecalho nao
// e se text for nulo ou vazio???
			String tx = J.remChar(';',rec.get(0));
			int nc = J.numPalavras(tx);
			String v="",cc="", r="";
			for(int q=0; q<nc; q++){
				cc = J.palavraK(q, tx);
				if(temComp(cc))
					v = getComp(cc).getText();
				v = J.trocaChar(' ','_',v);
				r+=v+"; ";
			}					
			rec.set(sel,r);
			// ver se alterou o ultimo campo
		}
		public void ajuste(){
			open(); // usando cam
// e se o arq for vazio???			
			text = rec.get(0);
			// mostrar campos aqui
		}
		public Comp geraInterf(){ // tag geraRinterface
// e se text== null???
			int nc = J.numPalavras(text);
			int lin=22, col=100, lr=160,al=16;
			String cc ="";
			for(int q=0; q<nc; q++){
				cc = J.palavraK(q,text);
				cc = J.remChar(';',cc);
				addRotulo(J.primMaiusc(cc), col-60,lin,50,al).setAln(J.DIR);
				addTextBox(cc, col,lin,lr,al).setName(cc);
				lin+=22;
			}
			return this;
		}
		public void selAnt(){
			sel = J.corrInt(--sel,1,rec.size()-1);
			showRec();
			cChange=tmpChange;
		}
		public void selProx(){
			sel = J.corrInt(++sel,1,rec.size()-1);			
			showRec();			
			cChange=tmpChange;
		}		
		public void selPrim(){
			sel = 1;
			showRec();
			cChange=tmpChange;
		}				
		public void selUlt(){
			sel = rec.size()-1;
			showRec();
			cChange=tmpChange;
		}				
		public boolean select(String cp, String v){
			// procura o valor v no campo cp
			// se o trecho v existir no registro em questao, retorna true
			// repare q nao precisa bater o campo inteiro, apenas parte dele
			// pense em "procure o NOME JOSE", p memorizar a ordem dos parametros, campo e nome
			int nc = -1; // guardarah o numero do campo
			
			{ // pegando o indice do campo
				String vvv=""; // nao ficou muito elegante, mas funciona.
				for(int np=0; np<J.numPalavras(text); np++){
					vvv=J.palavraK(np,text);
					vvv=J.remChar(';',vvv);
					J.esc("campo: ["+vvv+"]");
					if(J.iguais(cp,vvv)) nc=np;
					if(J.iguais(cp,vvv)) break;
				}
				if(nc==-1) J.impErr("!Campo nao encontrado: ["+cp+"]","JWin.DataBase.select()");
			}
				
			int c=-1;
			v = J.trocaChar(' ','_',v);
			String vv="";
			//J.esc("ajustando parametro: v["+v+"] cp["+cp+"]");
			for(String s:rec){
				c++;
				if(c==0) continue; // pulando o cabecalho
				vv=J.palavraK(nc,s);
				vv=J.remChar(';',vv);
				//J.esc(c+"par["+v+"] bd["+vv+"]?");
				if(J.tem(v,vv)){
					//J.esc("ACHOU!");
					sel=c;
					showRec();
					cChange=tmpChange;					
					return true;
				}				
			}
			return false;			
		}

		public void reg(KeyEvent k){			
			super.reg(k);
			
			boolean semSel= selMax==-1;
			boolean temSel=!semSel;
			
			int cd = k.getKeyCode();
			char ca = k.getKeyChar();
			
			boolean s = k.isShiftDown();
			boolean c = k.isControlDown();
			boolean a = k.isAltDown();					
			
		
			if(cd==k.VK_ENTER) cEnter=tmp;
			if(cd==k.VK_DELETE) cTcDel=tmp;
			
			if(cd==k.VK_INSERT) newRec();
			if(cd==k.VK_F2) save();
			if(cd==k.VK_F3) open();
//			if(cd==k.VK_F4) saveRec(); // assenta o registro atual no arrayList, sem mecher em arquivos // enter geral jah faz o servico

			
			if(cd==k.VK_LEFT) selAnt();
			if(cd==k.VK_RIGHT) selProx();			
			if(cd==k.VK_UP) selAnt();			
			if(cd==k.VK_DOWN) selProx();			
			if(cd==k.VK_HOME) selPrim();			
			if(cd==k.VK_END) selUlt();
			
			if(cd==k.VK_DELETE) delRec();			
			
		}
		public Comp clear(){
			rec.clear(); // importante quando se cria um novo bd, no editor.
			return this;
		}
		public void imp(){
			if(onClick()) selAll(); // o certo seria "o n G e t F o c u s()". Depois.
			
			calcAbs();
			
			impCx(crBox,crSomb,crLuz,xx,yy,lar,alt, LIVRE);
			 
			
			if(focado==this) impFocus(xx,yy,lar,alt);
			int dc=0, dl=0, // deslocamentos de linha e coluna p cada caracter, automatico
				ntl=1, // numero total de linhas, automatico
				ncl=-1; // numero de caracteres por linha(baseado na primeira linha de um multiline), automatico, aproximado (eh o suficiente)
				//nci=0; // numero de caracteres imprimidos, automatico
			char ch=0;
			boolean fora=false;
			Color cr=null, crr=null, crt=crText;
			if(blk)	crt=crSomb;				
			
			String st=J.intEmSt000(sel)+"/"+J.intEmSt000(rec.size()-1);
			int ix = (lar>>1)-(J.larText(st)>>1);
			impText(xx+ix, yy+(alt>>1)+4, crText, st);

			{ // setas 
				crt=crText;
				fSeta.opRot=3;
				fSeta.impMask(crt, xx-3,yy+(alt>>1)-10);
				fSeta.opRot=1;
				fSeta.impMask(crt, xx+lar-12-5,yy+(alt>>1)-10);
			}
			if(blk)	impContBlk(xx,yy,lar,alt);			
		}
		public String getScript(){
			String st = super.getScript()+" ";
			st+=cam;
			return st;
		}
	}

// === MENU =================================
	/*	COMO USAR
-1-coloque num arquivo de texto:
		arquivo, abrir, salvar, sair
		editar, copiar, colar, recortar
		opcoes, +opcao1, -opcao2, !bloqueadoSimples, #bloqueadoMais, _bloqueadoMenos, ---, 1este, 1+eh, 1opcao, 1radio, 1button
		ajuda, sobre, teclas_de_atalho
-2-insira no cod fonte:
		addMenu("caminhoDoArqDeTextoSalvoComOExemploAcima.txt",1,2,3,4);
Tb dah p inserir diretamente pelo codigo... mas eh muito complicado. Faca por arquivo mesmo.
	*/
		int tmpMenu=500;
	public Comp addMenu(String its){
		cAddComp=tmp;				
		Comp c = addMenu(null,0,0, 100,28);
		c.addItens(its);
		return c;
	}
	public Comp addMenu(String cm, int i, int j, int ii, int jj){
		cAddComp=tmp;				
		Comp c = new Menu();
		// esse nao tem pack
		c.pk=null;

		c.blk=false;
		
		c.cCreate = tmp;		
		c.tip = MNU;		
		c.setName(newName(c.tip));
		c.x=i;
		c.y=j;
		c.lar = ii;
		c.cam = cm;
		c.alt = 16;
		c.cAjuste=2;
		lastComp=c;		
		lastMenu=c;		
		if(lastWin!=null){
			c.lk = lastWin;
			c.x=1;
			c.y=2;
			c.lar = lastWin.lar-2;
			lastWin.cAjuste=tmp;		
		}	
		cmp.add(c);
		este=c;		
		return c;
	}
	public class Menu extends Comp{
				ArrayList<Itm> cab = new ArrayList<>(); // p cabecalho
				int altLin=16,
					larC=60, // largura do cabecalho				
					larI=60; // largura dos itens dentro de cada cabecalho				
			public class Itm{
						ArrayList<Itm> itm = new ArrayList<>();	 // o item dentro de cada cabecalho
						String text="000";
						int tp=0; // 0:text; <0:check; >0=rad e seu grupo (1=grupo 1 do rad; 2=grupo 2; 100=grupo100); veja q chk nao precisa de grupo; Estas sao especificacoes internas, apenas grupo para rad tem q ser informado manualmente na criacao do item de menu.
						boolean blk=false, val=false; 
						Jf1 ico=null; // opcional						
					public Itm(String p){
						// veja q este soh serve p cabecalhos, logo, sem chk, rad e blk 
						text=p;
						lar = J.larText(p+"   ");
						if(lar>larI) larI=lar;
					}
					public Itm add(int gr, String p){
						cAjuste=tmp; // somente 1 radio do grupo deve estar ligado
						// este eh soh p  r a d i o  dentro de menus
						// gr(na verdade tp) seria o grupo  r a d i o, logo, se alterar qq um do grupo, todos serao verificados
						// com + ou - liga ou desliga. Se duplicado com mais, apenas o ultimo + ficarah ligado
						// se nao tiver -, entao considera como menos
						// veja q somente um item pode ter + e ficar ligado
						// de preferencia, isolar as opcoes radio no menu por linhas divisorias. O sistema ateh permite misturar tudo, mas fica ruim p user. Misturar seria intercalar um check no meio de varios radios. Ainda assim deve funcionar, mas fica uma baderna p user.
						char ch=p.charAt(0);
						char chh=p.charAt(1);
						p = J.remChar('-',p);
						p = J.remChar('+',p);
						p = J.remChar('!',p);
						Itm i = new Itm(p);
						i.tp=gr; // >0=radio, com referencia ao devido grupo; 1=grupo1, 2=grupo2...
						if(ch=='+' || chh=='+') i.val=true;
						if(ch=='!' || chh=='!') i.blk=true;
						itm.add(i);
						return i;
					}
					public Itm add(String p){
						// se tiver + ou - em um dos dois prim chars, entao eh tipo  c h e c k b o x 
						// se tiver "!" em um dos dois prim chars , entao tah bloqueado
						
						//if(J.B(2)) p = J.emMaiusc(p); // debug
						
						char ch = p.charAt(0);
						char chh = p.charAt(1);

						p = J.remChar('!',p);
						p = J.remChar('+',p);
						if(!J.iguais(p,"---"))	p = J.remChar('-',p);												
						
						Itm i = new Itm(p);
						i.val=false;
						if(ch=='!' || chh=='!') i.blk=true;
						if(ch=='+' || chh=='+') {i.tp=-12; i.val=true; } // qq num menor q zero serve, nao existem grupos aqui
						if(ch=='-' || chh=='-') i.tp=-12; // qq num menor q zero serve, nao existem grupos aqui
						

						
						itm.add(i);
						return i;
					}
			}
			Itm 
				opened=null, // p cabecalho (acho q isso deveria virar global p tirar aquele bug)
				itmSel=null; // item selecionado de um cabecalho especifico

		public void reg(){
			super.reg();			
			opTemMenuAberto = (opened==null?false:true);
			if(cam==null){ // menu padrao, claro q darah p mudar; tag menu exemplo
				if(cab.size()<=0){
					Itm i = null;					
					i = new Itm("ARQUIVO");
					i.add("novo");
					i.add("abrir");
					i.add("salvar");
					i.add("---");
					i.add("sair");
					cab.add(i);
					
					i = new Itm("EDITAR");
					i.add("recortar");
					i.add("copiar");
					i.add("colar");
					cab.add(i);					

					i = new Itm("EXIBIR");
					i.add("configuracoes");
					i.add("---");
					i.add("!bloqueado");
					i.add("teste");
					i.add("---");
					i.add("+checkOn");
					i.add("-checkOff");
					i.add("---");
					i.add(1,"+radio"); // nao use grupo 0, este eh reservado p texto simples
					i.add(1,"-op1");
					i.add(1,"-op2");
					i.add(1,"-op3");
					i.add(1,"-op4");
					i.add("---");
					i.add(2,"-op20");
					i.add(2,"+op21");
					i.add(2,"+op21");
					i.add(2,"!+op21 bloqueadoCh0");
					i.add(2,"+!op21 bloqueadoCh1");
					i.add("---");
					i.add("teste");
					cab.add(i);										
					
							
				}
			}
			if(cab.size()<=0) addArq(cam);				
			{ // atrazo p fechar quando se escolhe
				
				// isso eh p remover um bug
				if(cFecha==tmp) cFecha=-1;
				if(cFecha==tmp-1) cFecha=-1;
				if(cFecha==tmp-2) cFecha=-1;
				if(cFecha==70) cFecha=-1;
				if(cFecha==69) cFecha=-1;
				//J.esc("cFecha: "+cFecha);
				
				if(cFecha>0){
					cFecha--;
					if(cFecha==1){
						opened=null;
						//getComp("txt_but00").setText(itmSel.text); // debug
					}
				}
				
			}
			{ // abrindo com alt
				if(key!=null)
				if(key.getKeyCode()==key.VK_ALT)
				if(opened==null){
					wPop.play();
					opened=cab.get(0);
					if(opened.itm.size()>0)
						itmSel = opened.itm.get(0);
				}
			}							
			{ // escolhendo um item space, sem fechar o menu. P chk e radio
				if(cKey==tmp-5) // c S p a c e  nao tah pegando direito aqui
				if(key!=null)
				if(key.getKeyCode()==key.VK_SPACE){
					select(itmSel);
					cFecha=-1; // p nao fechar
				}
			}
			{ // escolhendo um item com enter
				if(cKey==tmp-5) 
				if(key!=null)
				if(key.getKeyCode()==key.VK_ENTER)
					select(itmSel);
			}			
			{ // fechando qq menu com esc
				if(cEsc>0) opened=null; // cEsc eh global				
			}
			{ // auto-ajuste no redimensionamento da win
				if(lk!=null)
				if(lk.resizing)
					lar = lk.lar-3;			
			}
			{ // menus ao lado com setas
				if(cKey==tmp-2)
				if(key!=null)								
				if(key.getKeyCode()==key.VK_RIGHT)
				if(opened!=null)
					selDir();
				
				if(cKey==tmp-2)				
				if(key!=null)				
				if(key.getKeyCode()==key.VK_LEFT)
				if(opened!=null)
					selEsq();				
			}
			{ // menu cma e bxo com setas
				if(cKey==tmp-2)
				if(key!=null)								
				if(key.getKeyCode()==key.VK_DOWN)
				if(opened!=null)
					selBxo();
				
				if(cKey==tmp-2)
				if(key!=null)								
				if(key.getKeyCode()==key.VK_UP)
				if(opened!=null)
					selCma();				
			}
		}
		public boolean setValItem(String i, boolean b){
			for(Itm c:cab){
				if(J.iguais(c.text,i)){
					c.val = b;
					return true;
				}
				for(Itm cc:c.itm)
					if(J.iguais(cc.text,i)){
						cc.val = b;
						return true;
					}
			}
			J.impErr("!Item de menu nao encontrado: |"+i+"|", "JWin.Menu.setValItem()");
			return false;
		}
		public boolean getValItem(String i){
			for(Itm c:cab){
				if(J.iguais(c.text,i))	
					return c.val;
				for(Itm cc:c.itm)
					if(J.iguais(cc.text,i)) 
						return cc.val;				
			}
			J.impErr("!Item de menu nao encontrado: |"+i+"|", "JWin.Menu.getValItem()");
			return false;
		}
		public Comp setBlkItm(String i, boolean b){
			for(Itm c:cab){
				if(J.iguais(c.text,i)){
					c.blk = b;
					return this;
				}
				for(Itm cc:c.itm){
					if(J.iguais(cc.text,i)){
						cc.blk = b;
						return this;
					}
				}
			}
			return this;
		}		
		public boolean trocaItem(String a, String n){
			for(Itm c:cab){
				if(J.iguais(c.text,a)){
					c.text = n;
					return true;
				}
				for(Itm cc:c.itm){
					if(J.iguais(cc.text,a)){
						cc.text = n;
						return true;
					}
				}
			}
			return false;
		}
		public boolean temItem(String i){
			for(Itm c:cab){
				if(J.iguais(c.text,i)) return true;				
				for(Itm cc:c.itm){
					if(J.iguais(cc.text,i))
						return true;
				}
			}
			return false;
		}
		public void selCma(){
			if(opened==null) return;
			int q = opened.itm.indexOf(itmSel);
			q--;
			if(q<0) q=opened.itm.size()-1;
			if(J.iguais(opened.itm.get(q).text,"---")) q--; // nao existirah duas linhas divisorias seguidas, certo?
			if(q<0) q=opened.itm.size()-1;
			itmSel = opened.itm.get(q);
			itemFocado=J.emMaiusc(itmSel.text);						
		}
		public void selBxo(){
			if(opened==null) return;
			int q = opened.itm.indexOf(itmSel);
			q++;
			if(q>opened.itm.size()-1) q=0;
			if(J.iguais(opened.itm.get(q).text,"---")) q++; // nao existirah duas linhas divisorias seguidas, certo?			
			if(q>opened.itm.size()-1) q=0;
			itmSel = opened.itm.get(q);
			itemFocado=J.emMaiusc(itmSel.text);			
		}		
		public void selDir(){
			if(opened==null) return;
			int q = cab.indexOf(opened);
			if(q==cab.size()-1) return;
			opened=cab.get(++q);
			for(Itm i:opened.itm)
				if(i==itmSel) return;
			itmSel = opened.itm.get(0);
			itemFocado=itmSel.text;
		}
		public void selEsq(){
			//opened=cab.get(0); // depois
			if(opened==null) return;
			int q = cab.indexOf(opened);
			if(q==0) return;
			if(q==-1) return; // quando nao acha
			opened=cab.get(--q);
			for(Itm i:opened.itm)
				if(i==itmSel) return;
			itmSel = opened.itm.get(0);			
			itemFocado=itmSel.text;			
		}		
		public Itm getItem(String i){
			for(Itm c:cab){
				if(J.iguais(c.text,i)) return c;
				for(Itm cc:c.itm){
					if(J.iguais(cc.text,i))
						return cc;
				}
			}
			J.impErr("!item de menu nao encontrado: |"+i+"|", "JWin.Menu.getItem()");
			return null;
		}
		public void select(Itm p){ // especifico p menu
			if(p==null) return; // sem bug
			
			wBut.play();
			itemClicado="???";
			if(p!=null)	itemClicado=J.emMaiusc(p.text);
			cFecha = tmp-5; // este 5 eh importante p nao bugar
			// tp<0:chk; =0:texto; >0:radio
			if(p.tp<0){
				if(!p.blk) p.val=!p.val;
			}
			if(p.tp>0) if(!p.blk){ // radio;  
				// 11111111111111111
				// se estiver bloqueado E ligado, nenhum outro valor do mesmo grupo deverah ser alterado; como se todo o grupo estivesse bloqueado.
				// se estiver bloqueado E desligado, funcionarah normalmente, mas sem mudar este valor false
				// pensar melhor em tudo isso acima
				for(Itm c:cab)
				for(Itm i:c.itm)
					if(i.tp==p.tp)
					if(!i.blk)
						i.val=false;
				p.val=true; 
			}
			//opened=null;		
		}		
		public boolean select(String st){
			for(Itm c:cab){
				if(J.iguais(st,c.text)) {select(c); return true; }
				for(Itm i:c.itm)
					if(J.iguais(st,i.text)) 
						{select(i); return true; }					
			}			
			return false;
		}
		public void acClickItem(String st){
			if(!select(st))
				J.impErr("!item de menu nao encontrado: |"+st+"|", "JWin.Menu.acClickItem()");
			cClick=tmp-5; // gambiarra, mas faz funcionar.			
		}		
		public void imp(){
			calcAbs();
			if(focado==this) impFocus(xx,yy,lar,alt);			
			
			J.impRetRel(crBt,null, xx,yy,lar,alt);
			int lin=12, col=3;
			Color cr=J.cor[12];
			Jf1 f = null; // p  c h e c k   e   r a d i o  dos menus
			for(Itm cb:cab){
				lin=12;				
				{ // mouse sobre o cabecalho (sem clicar); Destacar fundo
					if(ms.naAreaRel(xx+col, yy+lin-12, larC, altLin))	{
						J.impRetRel(crBox,null, xx+col, yy+lin-12, larC+6, altLin);
						if(opened!=null)
						if(opened!=cb){
							opened=cb;
							itemFocado = opened.text;
						}
					}
				}
				{ // texto do cabecalho
					impText(xx+col+6, yy+lin, crText, cb.text);
				}
				{ // ?clicou sobre cabecalho???
					if(ms.b1)
					if(ms.naAreaRel(xx+col, yy+lin-12, larC, altLin))
					if(cClick<=0){
						cClick=tmp;
						// on click cabecalho aqui						
						opened=cb;
						itemFocado = opened.text;						
						itemClicado = opened.text;							
						wPop.play();						
					}
				}
				

				
				if(opened==cb){ // itens de cada cabecalho
					impSombMenu(xx+col-4, yy+lin, larI+16, opened.itm.size()*altLin);
					for(Itm it: cb.itm){
						cr=crLuz;
						{ // mouse sobre um item
							if(ms.naAreaRel(xx+col, yy+lin+6, larI+16, altLin-3)){
								itmSel = it;
								itemFocado = it.text;														
								// ?clicou sobre o item???
								if(ms.b1) if(cClick<=0){
									cClick = tmp;
									itemClicado= it.text;
									select(it);									
								}
							} 
						}
						{ // fundo do item com/sem destaque + texto e lin divisoria
							if(itmSel==it) cr = crBox;
							J.impRetRel(cr,null, xx+col, yy+lin+4, larI+16, altLin);
							lin+=altLin;
							if(J.iguais(it.text,"---")) {// linha divisoria; Esta  c o m p a r a c a o  poderia ser mais eficiente, mas o PC dah conta
								J.impLinRel(crSomb, xx+col+3, yy+lin-1-4, larI+16-6,0);
							} else{
								cr = crText;
								if(it.blk) cr = crBox;								
								impText(20+xx+col, yy+lin, cr, it.text);
								
								if(it.tp!=0 || it.ico!=null){ // ?eh como  c h e c k B o x  ou  r a d i o  ???
									// este fig eh o icone do item de menu. Check e radio NAO podem ter icones.
									f=null;
									
									if(it.tp<0){
										if(it.val) f = fCheOn;
										else f = fCheOff;									
									} else {
										if(it.val) f = fChoOn;
										else f = fChoOff;																			
									}

									if(it.ico!=null) f = it.ico;
									
									if(!it.blk)	f.impJf1(xx+col, yy+lin-altLin);
									else	f.impMask(crSomb, xx+col, yy+lin-altLin);
								}
							}
						}
					}										
					
					{ // fechar menu com mouse fora
						if(ms.b1) if(cSobre<=0){
							if(ms.x<xx+col) opened=null;
							if(ms.x>xx+col+larI+16) opened=null;							
							if(ms.y>yy+lin) opened=null;
							if(ms.y<yy) opened=null;
						}
					}					
				}
				col+=larC+12;
			}
			 		
		}
		public boolean onClickItem(String st){
			if(cClick<=0) return false;			
			st = J.emMaiusc(st);			
			if(cClick>0)
			if(itemClicado!=null)
			if(J.iguais(st,itemClicado)){
				cClick=0; // deve evitar um bug chato
				itemClicado=null;				
				return true;
			}	
			return false;
		}		
		public Comp addArq(String cm){
			cam = cm;
			if(J.iguais(cam,"null")) return this;
			if(J.iguais(cam,"")) return this;
			/*	DEPOIS
				ArrayList<String> ls = J.openStrings(cm);
				ArrayList<String> ls = J.openStringsUTF8(cm,10,15);
				cab.clear();
				np = J.numPalavras(st);
				pl = J.palavraK(n,st);
				
formato do arquivo:
	arquivo, abrir, salvar, ---, sair
	editar, copiar, colar, recortar
	opcoes, +opcao1, -opcao2, !bloqueadoSimples, #bloqueadoMais, _bloqueadoMenos
	radios, 1+rad1, 1opcao, 1maisUmaNesteGrupo, ---, 2grupoSegundo, 2+esteLigado, 2esteDesligado
	ajuda, sobre
				
			*/
			if(!J.arqExist(cam))
				J.impErr("!arquivo perdido: |"+cam+"|","JWin.Menu.addArq()");
			cab.clear();
			ArrayList<String> lis = J.openStringsUTF8(cam);
			Itm i = null;
			cab.clear();
			String st="";
			int nsi=0, n=0; 
			for(String s:lis){
				st = J.palavraK(0,s);
				st = J.remChar(',',st);
				st = J.emMaiusc(st);
				i = new Itm(st);
				nsi = J.numPalavras(s);
				for(int q=1; q<nsi; q++){
					st = J.palavraK(q,s);
					st = J.remChar(',',st);
					if(J.ehNum(st.charAt(0))){ // radios
						n = J.stEmInt(""+st.charAt(0));
						st = J.remPrimChar(st);
						i.add(n,st);
					} else {
						i.add(st);						
					}
				}
				cab.add(i);
			}
			return null;
		}
		public void ajuste(){						
			if(lk!=null){
				lar = lk.lar-6;
				alt = 16;
				x=1; y=2;
			} else {
				// pressupoe um menu fora de win
				lar = 800; // impreciso mas jah deve ajudar
			}
			{ // verificando largura de cabecalho
				int lMaior = 0;
				if(cab.size()>0)
				for(Itm c:cab)
					if(J.larText(c.text)>lMaior) lMaior = J.larText(c.text);
				larC=lMaior;
			}
		}
		public String getScript(){
			String st = super.getScript();
			st+=cam;
			return st;
		}
	}

// === LISTA DE ICONES ===================

	public Comp addIconList(int i, int j, int ii, int jj){
		cAddComp=tmp;				
		Comp c = new IconList();
		
		c.cCreate = tmp;		
		c.tip = ICO_LST;		
		c.setName(newName(c.tip));
		c.x=i;
		c.y=j;
		c.cam = null;		
		c.lar = ii;
		c.alt = jj;
		c.cAjuste=2;
		lastComp=c;				
		if(lastWin!=null){
			c.lk = lastWin;
			c.lk.cAjuste=tmp;		
		}	
		
		Comp p = packEm(i,j);
		if(p!=null)	{
			c.pk=p;		
			i-=p.x;
			j-=p.y;
		}	
		
		cmp.add(c);
		este=c;		
		return c;		
	}	
	public class IconList extends ListBox{ // icoList
			ArrayList<Jf1>itm = new ArrayList<>();
			int min=0, max=1000;			
		public void reg(KeyEvent k){
			// esse tem q testar, mas deve estar funcionando bem.
			super.reg(k);
			
			int cd = k.getKeyCode();
			
			if(cd==k.VK_UP)    cChange=tmp;
			if(cd==k.VK_DOWN)  cChange=tmp;
			if(cd==k.VK_LEFT)  cChange=tmp;
			if(cd==k.VK_RIGHT) cChange=tmp;
				
			if(cd==k.VK_UP)    if(sel>=ipl) sel-=ipl;
			if(cd==k.VK_DOWN)  if(sel<getNumItens()-ipl) sel+=ipl;
			if(cd==k.VK_LEFT)  sel--;
			if(cd==k.VK_RIGHT) sel++;
			
			sel = J.corrInt(sel,0,getNumItens()-1);
		}
		public void imp(){
			// pequena gambiarra
			if(cCreate==1) {
				lstArqs("c://java//jf1",null,"jf1");
				min=0; max=1000;
			}	
			
			calcAbs();
			
			//fundo
			Color crr=null;
			if(focado==this) impFocus(xx,yy,lar,alt);
			if(blk)	impContBlk(xx,yy,lar,alt);
			
			J.impRetRel(crBox,crr, xx,yy,lar,alt);
			 							
			
			//itens
			int e=64, col=0, lin=0, c=0, des=12;
			Jf1 f = null;
			
			if(sel<0) sel=0;
			if(sel>itm.size()-1) sel=itm.size()-1;
			if(itm.size()==0) sel=0;

			if(sel<min){
				min-=ipl;
				max-=ipl;
			}	
			
			if(sel>max){
				min+=ipl;
				max+=ipl;
			}
			
			if(min<0){
				min=0;
				max=min+1000;
			}
			
			if(max>itm.size()-1)
				max=itm.size()-1;
		
			Color cr=null;
			int r=12, rr=6,rrr=0;
			String st = "";
		
			for(int w=min; w<=max; w++){
				if(col*e+e>lar){
					ipl=col;
					col=0;
					lin++;
				}
				
				if(lin*e+e>alt) {
					max=c-1;
					// max = ipl*lin;
					break;
				}	
				
				cr = null; crr=null;
				if(w==sel) {
					cr = crBt;
					crr = crLuz;
				}
				
				// selecionando com clique de mouse
				if(ms.clicou)
				if(ms.naAreaRel(xx+col*e+2, yy+lin*e+2, e,e)){
					sel = w;
					cChange = tmp;
				}	
				
				J.impRetRel(cr, crr, xx+col*e+2, yy+lin*e+2, e,e);
				f = itm.get(w);
				f.zoom=2;
				if(f.cel!=null)
					f.impJf1(2+xx+col*e+r, yy+lin*e+rr);
				st = J.nomeArq(f.cam);
				rrr = (int)(e/2f - J.larText(st)/2f);
				impText(2+xx+col*e+rrr, yy+lin*e+e-12+rr, crText, st);
				
				col++;
				c++;
				
				
				// tentando carregar
				if(f.cel==null)
				if(cArq<=0){
					cArq=tmpArq;
					String cm = J.corrCam(f.cam,"jf1");
					if(!J.arqExist(cm)){
						itm.get(w).cel = fErro.cel;
					}	else {
						Jf1 ff = new Jf1(cm);
						itm.get(w).cel = ff.cel;
					}
				}
				
			}
			
			// barra de rolagem
			Color crt=crText;
			if(blk) crt=crSomb;
			fSeta.opRot=0;
			fSeta.impMask(crt, xx+lar-12-3,yy-2);
			fSeta.opRot=2;
			fSeta.impMask(crt, xx+lar-12-3,yy+alt-18);			
			if(itm.size()>1){
				int aq = (int)((alt-10-10-10-10) * ((sel)*100/(itm.size()-1))/100f);
				aq+=14;
				fQuad.impMask(crt, xx+lar-10,yy+1+aq);				
			}			
			
			// numero detalhe
			if(focado==this)
			if(itm.size()>0){
				String stt = ""+(sel+1)+" / "+(itm.size());
				int d = J.larText(stt);
				J.impRetRel(crBox, crLuz, xx+lar-d, yy+3-12, d, 14);
				impText(xx+lar-d, yy+3, crText, stt);
			}			
			
		}
		public String getScript(){
			String st = super.getScript();
			st+=": ";
			
//NAO AQUI PQ BUGA!
//			for(Jf1 f:itm) st+= " "+f.cam+",";

			return J.remUltChar(st);
		}
		public int getNumItens(){
			return itm.size();
		}
		public String getText(){
			if(itm.size()<=0) return "";
			if(J.noInt(sel,0,itm.size()-1))
				return itm.get(sel).cam;
			return "???";
		}
		public Comp clear(){
			itm.clear();
			return this;
		}
		public Comp lstArqs(String cm, String fil, String ext){
			cm = "c://java//jf1//";
			ext = "JF1";
			
			if(ext!=null) ext = J.emMaiusc(ext);
			
			if(ext.equals("JF1")) 	
				if(cm==null)
					cm="jf1//";
				
			ArrayList<String> ls = J.listarArqs(cm,fil,ext);
			clear(); // isso eh bom???
			for(String st:ls)
				addItem(st);						
			cChange=tmp;
			return this;
		}
		public Comp addItem(String s){
			Jf1 f = new Jf1(10);
			f.cel = null;
			f.cam = J.corrCam(s,"Jf1");
			itm.add(f);
			return this;
		}
	}

// === LISTA DE IMAGENS ==================
	
	public Comp addPicList(int i, int j, int ii, int jj){
		cAddComp=tmp;				
		Comp c = new PicList();
		
		c.cCreate = tmp;		
		c.tip = PIC_LST;		
		c.setName(newName(c.tip));
		c.x=i;
		c.y=j;
		c.cam = null;		
		c.lar = ii;
		c.alt = jj;
		c.cAjuste=2;
		lastComp=c;				
		if(lastWin!=null){
			c.lk = lastWin;
			c.lk.cAjuste=tmp;		
		}	
		
		Comp p = packEm(i,j);
		if(p!=null)	{
			c.pk=p;		
			i-=p.x;
			j-=p.y;
		}	
		
		cmp.add(c);
		este=c;		
		return c;		
	}	
	public class PicList extends ListBox{ // PictureList ImageList
			ArrayList<JFig>itm = new ArrayList<>();
			int min=0, max=1000, tamPng=40;			
		public void reg(KeyEvent k){
			// esse tem q testar, mas deve estar funcionando bem.
			super.reg(k);
			
			int cd = k.getKeyCode();
			
			if(cd==k.VK_UP)    cChange=tmp;
			if(cd==k.VK_DOWN)  cChange=tmp;
			if(cd==k.VK_LEFT)  cChange=tmp;
			if(cd==k.VK_RIGHT) cChange=tmp;
				
			if(cd==k.VK_UP)    if(sel>=ipl) sel-=ipl;
			if(cd==k.VK_DOWN)  if(sel<getNumItens()-ipl) sel+=ipl;
			if(cd==k.VK_LEFT)  sel--;
			if(cd==k.VK_RIGHT) sel++;
			
			sel = J.corrInt(sel,0,getNumItens()-1);
		}
		public void imp(){
			// pequena gambiarra
			if(cCreate==1) {
				lstArqs("c://java//jpg",null,"png");
				min=0; max=1000;
			}	
			
			calcAbs();
			
			//fundo
			Color crr=null;
			if(focado==this) impFocus(xx,yy,lar,alt);
			if(blk)	impContBlk(xx,yy,lar,alt);
			
			J.impRetRel(crBox,crr, xx,yy,lar,alt);
			 							
			
			//itens
			int e=64, col=0, lin=0, c=0, des=12;
			JFig f = null;
			
			if(sel<0) sel=0;
			if(sel>itm.size()-1) sel=itm.size()-1;
			if(itm.size()==0) sel=0;

			if(sel<min){
				min-=ipl;
				max-=ipl;
			}	
			
			if(sel>max){
				min+=ipl;
				max+=ipl;
			}
			
			if(min<0){
				min=0;
				max=min+1000;
			}
			
			if(max>itm.size()-1)
				max=itm.size()-1;
		
			Color cr=null;
			int r=12, rr=6,rrr=0;
			String st = "";
		
			for(int w=min; w<=max; w++){
				if(col*e+e>lar){
					ipl=col;
					col=0;
					lin++;
				}
				
				if(lin*e+e>alt) {
					max=c-1;
					// max = ipl*lin;
					break;
				}	
				
				cr = null; crr=null;
				if(w==sel) {
					cr = crBt;
					crr = crLuz;
				}
				
				// selecionando com clique de mouse
				if(ms.clicou)
				if(ms.naAreaRel(xx+col*e+2, yy+lin*e+2, e,e)){
					sel = w;
					cChange = tmp;
				}	
				
				J.impRetRel(cr, crr, xx+col*e+2, yy+lin*e+2, e,e);
				f = itm.get(w);
				f.impRel(2+xx+col*e+r, yy+lin*e+rr, tamPng,tamPng); // 444444444
				st = J.nomeExtArq(f.cam);
				rrr = (int)(e/2f - J.larText(st)/2f);
				impText(2+xx+col*e+rrr, yy+lin*e+e-12+rr, crText, st);
				
				col++;
				c++;
				
			}
			
			// barra de rolagem
			Color crt=crText;
			if(blk) crt=crSomb;
			fSeta.opRot=0;
			fSeta.impMask(crt, xx+lar-12-3,yy-2);
			fSeta.opRot=2;
			fSeta.impMask(crt, xx+lar-12-3,yy+alt-18);			
			if(itm.size()>1){
				int aq = (int)((alt-10-10-10-10) * ((sel)*100/(itm.size()-1))/100f);
				aq+=14;
				fQuad.impMask(crt, xx+lar-10,yy+1+aq);				
			}			
			
			// numero detalhe
			if(focado==this)
			if(itm.size()>0){
				String stt = ""+(sel+1)+" / "+(itm.size());
				int d = J.larText(stt);
				J.impRetRel(crBox, crLuz, xx+lar-d, yy+3-12, d, 14);
				impText(xx+lar-d, yy+3, crText, stt);
			}			
			
		}
		public String getScript(){
			String st = super.getScript();
			st+=": ";
			
//NAO AQUI PQ BUGA!
//			for(Jf1 f:itm) st+= " "+f.cam+",";

			return J.remUltChar(st);
		}
		public int getNumItens(){
			return itm.size();
		}
		public String getText(){
			if(itm.size()<=0) return "";
			if(J.noInt(sel,0,itm.size()-1))
				return itm.get(sel).cam;
			return "???";
		}
		public Comp clear(){
			itm.clear();
			return this;
		}
		public Comp lstArqs(String cm, String fil, String ext){
			cm = "c://java//jpg//"; // 55555555555
			ext = "png";
			
			if(ext!=null) ext = J.emMaiusc(ext);
			
			if(cm==null)			
			if(  ext.equals("png") 
				|| ext.equals("jpg") 				
				|| ext.equals("jpeg") 				
				|| ext.equals("bmp") 				
				|| ext.equals("gif") ) 	
					cm="jpg//";
				
			ArrayList<String> ls = J.listarArqs(cm,fil,ext);
			clear(); // isso eh bom???
			for(String st:ls)
				addItem(st);						
			cChange=tmp;
			return this;
		}
		public Comp addItem(String s){
			//JFig f = new JFig("jpg//esfera01.png"); // 666666666666
			if(J.extArq(s).equals("")) return this; // diret?rio nao ? imagem. Isso remove o bug

			JFig f = new JFig(s);
			itm.add(f);
			return this;
		}
		public Comp _addItem(String s){
			//Jf1 f = new Jf1(10);
			//f.cel = null;
			//f.cam = J.corrCam(s,"Jf1");
			//itm.add(f);
			return this;
		}

	}
	
// === PALETA ================================

	public Comp addPaleta(String cm, int i, int j, int ii, int jj){
		cAddComp=tmp;
		Comp c = new Paleta();
		if(lastWin!=null) c.lk=lastWin;
		
		Comp p = packEm(i,j);
		if(p!=null)	{
			c.pk=p;		
			i-=p.x;
			j-=p.y;
		}	
		
		c.cCreate = tmp;
		c.tip = PLT;
		c.setName(newName(c.tip));
		c.crFrente = null;
		c.crFundo = null;		
		c.x = i; c.y=j;		
		c.lar = 255*2+4;
		c.alt = jj;
		c.cam = cm;		
		c.cAjuste = tmpArq;
		cmp.add(c);
		lastComp=c;		
		este=c;
		return c;
	}
	public class Paleta extends Comp{
		// tem mais coisa p desenvolver, ver zzz
			int pag=0;
		public void imp(){
			calcAbs();

			impCx(crBox,crSomb,crLuz,xx,yy,lar,alt,LIVRE);
			 							

			int w=12, qq=0, incPag=pag*255;

			fAm.crAnt=12;
			fAm.opInv=2;
			fAm.crNov=sel+incPag;
			fAm.impJf1(xx+sel*2-6,yy-6);
			
			for(int q=0; q<=255; q++){
				qq=q+incPag;
				if(q==0)qq=16;
				J.impRetRel(qq,0, xx+q*2+2, yy+2+w, 2, alt-4-w);
				if(ms.b1)
					if(ms.naAreaRel(xx+q*2+2, yy+2+w, 2, alt-4-w))
						select(qq);
				
			}
			int ww=0;
			if(sel>240) ww=-40;
			impText(xx+sel*2+12+ww,yy+12,crText,""+J.intEmSt000(sel+incPag));
			
			
			if(focado==this) impFocus(xx,yy,lar,alt);
			if(blk)	impContBlk(xx,yy,lar,alt);			
		}
		public void ajuste(){
			cAjuste=0;
			lar = 255*2+6; // paleta vertical depois
			if(cam!=null){
				String st = J.corrCam(cam,"plt");
				if(J.arqExist(st)){
					// depois
				} 
			}
		}
		public Comp setCam(String cm){
			super.setCam( J.corrCam(cm,"plt"));
			cAjuste = 2;
			return this;
		}		
		public void reg(KeyEvent k){
			super.reg(k);
			int cd=k.getKeyCode();
			boolean c=k.isControlDown();
			
			if(cd==k.VK_HOME) select(0);
			if(cd==k.VK_END) select(255);
			
			if(cd==k.VK_UP)   pag = J.corrInt(--pag, 0,1);
			if(cd==k.VK_DOWN) pag = J.corrInt(++pag, 0,1);
			
			if(!c){
				if(cd==k.VK_LEFT) select(sel-1);
				if(cd==k.VK_RIGHT) select(sel+1);
			} else {
				if(cd==k.VK_LEFT) select(sel-5);
				if(cd==k.VK_RIGHT) select(sel+5);				
			}

		}
		
		public void select(int i){
			// tem q mudar de pagina automaticamente aqui
			sel = J.corrInt(i,0,255);
		}
		public Color getColor(){
			if(sel<0) return null;
			if(sel>255*2) return null;
			return J.cor[sel];
		}
		public Comp setVal(Color cr){
			// precisa selecionar a cor primeiro antes de atribuir
			if(sel<0) return this;
			if(sel>255*2) return this;
			if(J.cor==null) return this;
			J.cor[sel] = cr;
			return this;
		}
		public int getVal(){
			return sel;
		}
		public String getScript(){
			String st = super.getScript()+" ";
			st+=cam;
			return st;
		}
	}

// === COLOR CHOOSER =========================

	public Comp addColorChooser(int i, int j, int ii, int jj){
		cAddComp=tmp;
		Comp c = new ColorChooser();
		if(lastWin!=null) c.lk=lastWin;
		
		Comp p = packEm(i,j);
		if(p!=null)	{
			c.pk=p;		
			i-=p.x;
			j-=p.y;
		}	
		
		c.cCreate = tmp;
		c.tip = CLR_CHO;
		c.setName(newName(c.tip));
		c.crFrente = null;
		c.crFundo = null;		
		c.x = i; c.y=j;		
		c.lar = ii;
		c.alt = jj;
		c.cAjuste = tmpArq;
		cmp.add(c);
		lastComp=c;		
		este=c;
		return c;
	}
	public class ColorChooser extends Comp{
			// a diferenca com a paleta eh q este nao permitira a abertura de arquivo de paleta. (mas nao sei se jah implementei a abertura de arquivo p paleta. Jah foi?)
		public void imp(){
			calcAbs();
			impCx(crBox,crSomb,crLuz,xx,yy,lar,alt,LIVRE);						
			int w=16+12, ww=alt-w-3, xxx=0;
			
			int cr = sel, crr=0;
			int me = lar>>1;
			
			if(cr==0)cr=16;
			J.impRetRel(cr,0, xx+3, yy+3+12, lar-6, 12);
			 							

			if(ms.b1)
			if(ms.naAreaRel(xx,yy,lar,alt)){
				if(ms.x>xx+(lar>>1)) select(sel+1);
				else select(sel-1);
				cChange = tmp; // ?ou tmpChange??? Parece q o cod tah inconsistente. Revisar depois.
			}
			
			// verificando change (acima tb tem)
			if(focado==this) { 
				if(ms.dr!=0) cChange=tmp;
				// if(J.homeOn) cChange=tmp; // acho q tem um bug aqui.
				// if(J.endOn) cChange=tmp;
				// if(J.dirOn) cChange=tmp;
				// if(J.esqOn) cChange=tmp;
			}
			
			for(int q=0; q<=512; q++){
				xxx = xx+q*4+4;
				crr = +1+q+sel-(int)((lar>>1)/4f);
				if(xxx>xx+lar-6) break;
				if(crr<0) continue;
				if(crr>512) continue;
				if(crr==0) crr=16;
				J.impRetRel(crr,0, xxx, yy+w, 3, ww);
			}
			
			impText(xx+me-1,yy+12+12,crText,"|");
			impText(xx+me-10,yy+12,crText,""+J.intEmSt000(sel));
	
			if(focado==this) impFocus(xx,yy,lar,alt);
			if(blk)	impContBlk(xx,yy,lar,alt);			
			
		}
		public void select(int p){
			sel = J.corrInt(p, 0,512);
		}
		public void reg(KeyEvent k){
			super.reg(k);
			int cd=k.getKeyCode();
			boolean c=k.isControlDown();
			
			if(cd==k.VK_HOME) {select(0);cChange=tmp;}
			if(cd==k.VK_END) {select(255);cChange=tmp;}
			
			if(!c){
				if(cd==k.VK_LEFT) {select(sel-1);cChange=tmp;}
				if(cd==k.VK_RIGHT) {select(sel+1);cChange=tmp;}
			} else {
				if(cd==k.VK_LEFT) {select(sel-5);cChange=tmp;}
				if(cd==k.VK_RIGHT) {select(sel+5);cChange=tmp;}
			}

		}
		public Color getColor(){
			if(J.noInt(sel, 0, 512))
				return J.cor[sel];
			return null;
		}
		public int getVal(){
			return sel;
		}
	}		
	
// === MENU POPUP ====================
/* como usar:
		if(ms.b2) // criando o menu diretamente no codigo
			amb.addPopUp("item1, item2, item3, itemMuitoGrande");
		if(amb.onPopUp("item1")) // detectando item clicado
			amb.clear();

pode-se tb escrever: 			
			amb.addPopUp("menuPopUp01.txt");
	Se existir '.' no string, serah buscado o arquivo correspondente, cujo formato eh: cada item em uma linha e nao itens separados por virgula. Este ultimo eh soh p escrever no proprio codigo.
*/
		Comp ppu=null; // interno, nao use isso.
	public boolean popUp(String st){
		// soh por compatibilidade
		return onPopUp(st);
	}
	public boolean onPopUp(String st){
		// retorna true se houve click no menu p o p U p com text = st no item especificado
		// a comparacao eh por text. Nada de name.
		// se true, jah elimina o menu. Isso eh sim necessario.
		if(ppu==null) return false;
		if(ppu.rem==true) return false;
		// if(ppu.cFecha>0) return false; // esse nao q buga
		

		
		st = J.emMaiusc(st);
		if(ppu.cClick==1)
		if(st.equals(J.emMaiusc(ppu.itemClicado))){
			ppu.cFecha = tmp;
			ppu.itemFocado = null;
			ppu.itemClicado = null;
			ppu=null;			
			// ppu.rem = true; // ou seria melhor isso???
			return true;
		}
		return false;
	}		
	public Comp addPopUp(String st){
		if(ppu!=null) return null; // remove o bug de varios popUps abertos seguidamente a cada loop
		
		cAddComp=tmp; // isso eh bom???		
		if(ppu!=null) ppu.remove();
		mnAberto=true;
		Comp c = new PopUp();
		
		c.cCreate = tmp;		
		c.tip = POP;
		c.setName(newName(c.tip));
		c.text = "popUp";
		c.x=ms.x;
		c.y=ms.y;
		c.cam = null;		
		c.lar = 1; 
		c.alt = 1;
		c.addItens(st);
		c.cAjuste=2;
		ppu = c;
		// lastComp = c; // isso eh bom??? Acho q nao.
		// este=c;
		cmp.add(c);
		wPop.play();
		return c;			
	}	
	public class PopUp extends Comp{
			ArrayList<String> itm = new ArrayList<>();
			//int al=16;
			int al=18;
		public void imp(){
			// nao calcula coordenadas absolutas
			if(cFecha==1) {
				rem=true;
				ppu=null;
			}	else mnAberto=true;
			
			if(ms.b1) cFecha=tmp; // pode ter clicado em outro controle. Este menu nunca terah o foco.

		
				
			impSombMenu(x,y,lar,alt);
			
			
			impCx(crLuz, crLuz, crSomb, x,y-3,lar-1,alt+6-1,LIVRE);
			//  // nao funciona aq pq nao calcula coods absolutas, mas acho q nao precisarah
			
			Color cr = null;
			char ca=0;
			int r=6, rr=9, ic=-1;
			String st="";
			boolean foi=false;
			for(int q=0; q<itm.size(); q++){
				if(ms.naAreaRel(x+12,y+q*al,lar,al-2)){
					cr = crBox;
					itemFocado = itm.get(q);
					foi = false;
					ic=-1;
					if(ms.b1) {
						itemClicado = itm.get(q);
						ic=q;
						if(cClick<=0)foi = true;						
					}	
					if(cClick>0) {
						cr=crLuz;
						cFecha=tmp;
					}	
					J.impRetRel(cr,null, x+3,y+q*al, lar-6,al);
				}	
						st = itm.get(q);
						ca = st.charAt(0);
						cr = crText;
						if(!J.iguais(st,"---"))
						if(J.vou(ca,'+','-','#','_','!')){
							st = J.removeIni(st,1);
							cr = crText;		
							switch(ca){
								case '!':
									cr = crSomb;
									break;
								case '+': 
									fCheOn.impJf1(x+4, y+q*al-12+rr);
									if(foi) 
										if(q==ic)
											itm.set(q, "-"+st);
									foi = false;									
									break;
								case '-': 
									fCheOff.impJf1(x+4, y+q*al-12+rr);
									if(foi) 
										if(q==ic)										
											itm.set(q, "+"+st);
									foi = false;
									break;									
								case '#': 
									fCheOn.impMask(crSomb, x+4, y+q*al-12+rr);
									cr = crSomb;
									break;
								case '_': 
									fCheOff.impMask(crSomb, x+4, y+q*al-12+rr);
									cr = crSomb;
									break;									
							}
						}
						
				impText(x+24,y+q*al+12,cr, st);
			}				
		}
		public Comp addItem(String st){
			if(st==null) return this;
			if(st.equals("")) return this;
			if(st.equals(" ")) return this;
			// cChange=tmpChange;
			st = J.semSpcIni(st);
			st = J.semSpcFinal(st);
			alt+=al;
			if(lar<J.larText(st)+42) lar = J.larText(st)+42;
			itm.add(st );
			return this;
		}
		public Comp addArq(String cm){
			ArrayList<String> l = J.openStrings(cm);
			for(String st:l)
				addItem(st);
			return this;
		}
		public Comp addItens(String st){
			// varios itens separados por virgula. Usei na abertura de s c r i p t de win
			
			if(J.tem('.',st)) 
				return addArq(st);
			
			String it="";
			char ch=0;
			for(int q=0; q<st.length(); q++){
				ch = st.charAt(q);
				if(ch!=',') it+=ch;
				else{
					addItem(it);
					it="";
				}
			}
			addItem(it); // o ultimo tb
			return this;
		}
		public boolean temItem(String st){
			st = J.emMaiusc(st);
			for(String it:itm)
				if(st.equals(J.emMaiusc(it)))
					return true;
			return false;	
		}		
	}


// === PROMPT ========================
// VEJA O GUIA NO TOPO DESTE FONTE
		String stRet=null;	// isso eh interno... mas usei p uma gambiarra no editor win
	public Comp prompt(String av, String def, Jf1 ic){
		// esse eh o principal. Dah p mudar o icone.
		
		//if(temComp("prompt")) return; // jah tah no meio do processo
		// desabilitei acima p nao bugar, j? q prompts ser?o renomeaveis (p avaliar o retorno)
		
		Comp cw = openWin("prompt.win").setName("prompt");
		getComp("rotAviso").setText(av);
		getComp("txtRet").setText(def).setFocus().selAll();		
		getComp("prompt").centralizar();
		if(ic!=null) getComp("ico").setJf1(ic);		
		return cw;
	} 
	public Comp prompt(String av, String def){
		// acessorio, icone padrao
		return prompt(av,def,null);
	}
	public String getRet(){ // serve p d i a l s a v e o p e n  tb
		return stRet; // acho q nao precisa zerar o valor aqui.
	}	
	public boolean promptOk(){
		// por compatibilidade
		return onPromptOk();
	}
	public boolean onPromptOk(){
		return onPromptOk("prompt");
	}
	public boolean promptCancel(){
		return onPromptCancel(); // por compatibilidade
	}
	public boolean onPromptCancel(){
		return onPromptCancel("prompt");
	}
	public boolean onPromptCancel(String nm){
		if(temComp(nm))
		if(temComp("btProCancel"))// evitou um bug			
		if(onClick("btProCancel")){
			stRet = null;
			remove(nm);
			// focado = null;			
			return true;
		}
		return false;
	}	
	public boolean promptOk(String nm){
		return promptOk(nm); 
	}
	public boolean onPromptOk(String nm){
		// esse esquema parece bom
		// veja q varios prompts podem existir
		nm = J.emMaiusc(nm);
		if(temComp(nm))
		if(onClick("btProOk")){
			stRet = getText("txtRet");
			getComp(nm).remove();
			// focado = null;			
			return true;
		}
		return false;
	}
	
	
// === CONFIRM =======================
// caixa de dialogo snck, VEJA O GUIA NO TOPO DESTE FONTE


// chame com esta abaixo (veja o guia no topo deste fonte)
	public Comp confirm(String av,String bts){
		return confirm(av, "confirme", bts,null);
	}
	public Comp confirm(String av,String tit, String bts){
		return confirm(av, tit, bts,null);
	}	
	public Comp confirm(String av, String bts,Jf1 f){
		return confirm("confirme",av, bts,f);		
	}
	public Comp confirm(String av, String tit, String bts,Jf1 f){
		// ?no parametro, tit deve vir antes ou depois de av??? O que fica mais intuitivo?
		// R: o av deve vir primeiro
		if(temComp("confirm")) return null; 
		
		Comp cc = openWin("confirm.win").setName("confirm").setText(tit);
		cc.centralizar();
		//cc.crFundo = J.altColor(crBt,32);
		cc.crFundo = crBt;
		cc.opWinDial=true; // tratamento especial p dialogos
		//getComp("rotAviso").setText(av).crFundo = cc.crFundo;
		getComp("rotAviso").setText(av);
		getComp("rotAviso").opMultiLine=true;
		getComp("ico").setJf1(f);
		
		boolean
			s=false, S=false,
			n=false, N=false,
			c=false, C=false,
			k=false, K=false;
		
		if(J.tem('s',bts)) s=true;
		if(J.tem('S',bts)) s=S=true;
		if(J.tem('n',bts)) n=true;
		if(J.tem('N',bts)) n=N=true;		
		if(J.tem('c',bts)) c=true;
		if(J.tem('C',bts)) c=C=true;		
		if(J.tem('k',bts)) k=true;
		if(J.tem('K',bts)) k=K=true;		
		
		if(!s) remove("btCnfSim");
		if(!n) remove("btCnfNao");
		if(!c) remove("btCnfCancel");
		if(!k) remove("btCnfOk");
		
		
		if(S) setFocus("btCnfSim");
		if(N) setFocus("btCnfNao");
		if(C) setFocus("btCnfCancel");
		if(K) setFocus("btCnfOk");
		
		return cc;
	}
// use num "if" estas abaixo
	public boolean onConfirmSim(){
		return onConfirmSim("confirm"); // nome padrao; deixei por compatibilidade
	}
	public boolean onConfirmNao(){
		return onConfirmNao("confirm"); // nome padrao; deixei por compatibilidade
	}	
	public boolean onConfirmOk(){
		return onConfirmOk("confirm");
	}		
	public boolean onConfirmCancel(){
		return onConfirmCancel("confirm");
	}			
	public boolean onConfirmOk(String nm){
		nm = J.emMaiusc(nm);
		if(temComp(nm))
		if(temComp("btCnfOk"))
			if(onClick("btCnfOk")){
				remove(nm);
				return true;
			}	
		return false;	
	}		
	public boolean onConfirmCancel(String nm){
		nm = J.emMaiusc(nm);
		if(temComp(nm))
		if(temComp("btCnfCancel"))
			if(onClick("btCnfCancel")){
				remove(nm);
				return true;
			}	
		return false;	
	}			
	public boolean onConfirmNao(String nm){
		nm = J.emMaiusc(nm);
		if(temComp(nm))
		if(temComp("btCnfNao"))
			if(onClick("btCnfNao")){
				remove(nm);
				return true;
			}	
		return false;	
	}	
	public boolean onConfirmSim(String nm){
		nm = J.emMaiusc(nm);
		if(temComp(nm))
		if(temComp("btCnfSim"))
			if(onClick("btCnfSim")){
				remove(nm);
				return true;
			}	
		return false;	
	}	
// esse eh soh p fechar a win
	public void closeConfirm(){ // nao "confirmClose()" p nao confundir com evento de fechar a win confirm.
		closeConfirm("confirm");
	}
	public void closeConfirm(String nm){ 
		// nao "confirmClose()" p nao confundir com evento de fechar a win confirm.
		// acho q nem precisa disso...
		if(temComp(nm))
			getComp(nm).remove();		
	}	

// === DIALOGO SALVAR/ABRIR ARQUIVO ==========
// veja o guia no topo deste fonte

		Comp winSaveOpen=null;
	public Comp winSaveOpen(String cm, String nmArq, String ext, String fil){
		winSaveOpen = openWin("saveOpenWin03.win"); // tudo em minusculas p Linux não reclamar
		//winSave.setText("salvando "+ext).setName("winSave");
		setText("camArq",cm);		
		getComp("lstArq").lstArqs(cm,null,ext).opSelItmComEspaco=false;
		if(fil!=null){
			setText("filArq",fil);
			getComp("lstArq").fil = fil;
			getComp("lstArq").refresh();							
		}				
		if(nmArq!=null) getComp("lstArq").select(nmArq);
		setText("nomeArq",nmArq).setFocus().selAll();
		//win.setText("salvar "+ext);		
		ocultarBotoesDial(ext); // interno		
		stRet=null;
		return winSaveOpen;
	}
	private void ocultarBotoesDial(String ext){ // interno. Nao use isso
		getComp("amJf1").hide();
		getComp("amJpg").hide();
		getComp("amJ3d").hide();
		getComp("btPlay").hide();
		getComp("btDelarq").hide();
		getComp("btEdit").hide();
		ext = J.emMaiusc(ext);
		if(ext.equals("WAV")) getComp("btPlay").show();
		if(ext.equals("J3D")) getComp("amJ3d").show();
		if(ext.equals("JF1")) getComp("amJf1").show();
		if(ext.equals("PNG")) getComp("amJpg").show();
		if(ext.equals("JPG")) getComp("amJpg").show();
		if(ext.equals("JPEG")) getComp("amJpg").show();		
	}

	public boolean onWinCancel(String nm){
		if(winSaveOpen!=null)
		if(nm==null || J.iguais(winSaveOpen.getName(),nm))			
		if(temComp("btCancel")) // pode dar conflito de nome aqui. Ajustar depois.
		if(onClick("btCancel")){
			stRet=null;			
			winSaveOpen.remove();
			winSaveOpen=null;
			return true;
		}
		return false;		
	}	
	public boolean onWinOk(String nm){
		if(winSaveOpen!=null)
		if(nm==null || J.iguais(winSaveOpen.getName(),nm))
		if(temComp("btOk")) // pode dar conflito de nome aqui. Ajustar depois.
		if(onClick("btOk")){
			stRet = getText("nomeArq");
			winSaveOpen.remove();
			winSaveOpen=null;
			return true;
		}
		return false;		
	}
	
	public void regDial(){ // regWin
		{ // prompt
			if(temComp("prompt"))
			if(temComp("txtRet"))
			if(este.onEnterPress())
				getComp("btProOk").acClick();		
		}
		{ // dialogo save open embutido aqui em JWin
			if(winSaveOpen!=null) {
				if(getComp("btPlay").hid==false){
					if(getComp("lstArq").onSpacePress())
						acClick("btPlay");
					if(onClick("btPlay")){
						String cm = J.corrCam(getText("nomeArq"),"wav");
						if(J.arqExist(cm)){
							if(J.wav!=null)
								J.wav.stop();
							J.playArqWav(cm);
						} else J.esc("arquivo perdido: "+cm);
					}
				}
		
				if(getComp("filArq").onDownPress()) setFocus("nomeArq");
				if(getComp("nomeArq").onUpPress()) if(!J.ctrlOn) setFocus("filArq");
				if(getComp("nomeArq").onDownPress()) if(!J.ctrlOn) setFocus("lstArq");

				// p jf1  ... poderia responder ao bt edit chamando o editor jf1, ne'?
				if(getComp("amJf1").hid==false){
					if(getComp("nomeArq").onChange())
						getComp("amJf1").setJf1(new Jf1(getText("nomeArq")));					
				}

				// p jpg e png 
				if(getComp("amJpg").hid==false){
					if(getComp("nomeArq").onChange())
						getComp("amJpg").openFig(getText("nomeArq"));
				}			
				
				boolean vai = false;
				
				if(getComp("lstArq").onChange())	copyText("lstArq","nomeArq");

				if(onEnterPress("lstArq")){
					copyText("lstArq","nomeArq");					
					acClick("btOk");
				}
				
				if(getComp("lstArq").onDoubleClick()){
					copyText("lstArq","nomeArq");
					vai=true;		
				}			

				if(getComp("filArq").onChange()){
						getComp("lstArq").clear();		
						getComp("lstArq").fil = getText("filArq");
						getComp("lstArq").refresh();	
				}

				if(onEnterPress("nomeArq"))	vai = true;

				// del arq depois
				 
				if(vai) acClick("btOk"); // serah q nao vai dar conflito???
			}		
		}
	}
	
}

