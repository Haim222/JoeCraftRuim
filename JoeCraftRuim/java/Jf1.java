// jf1 versao 2.0
// Usado para ícones 20x20 usando a paleta padrão.
import java.awt.Color;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.io.File;


public class Jf1{
		int 
			opRot=0, opInv=0, // estes dois sao obsoletos		
			opMod=0; // usar este no lugar
		final int 
			INV_VERT=1,
			INV_HOR=2,
			ROT_DIR=3,
			ROT_DIRR=4,
			ROT_ESQ=5,
			ROT_DIR_INV_HOR=6,
			ROT_DIRR_INV_HOR=7,
			ROT_ESQ_INV_HOR=8,
			ROT_DIR_INV_VERT=9,
			ROT_DIRR_INV_VERT=10,
			ROT_ESQ_INV_VERT=11,
			INV_VERT_HOR=12;	
		int 
			crAnt=-1, crNov=-1, 
			crAnt2=-1, crNov2=-1, 
			crAnt3=-1, crNov3=-1, 
			crAnt4=-1, crNov4=-1, 
			crAnt5=-1, crNov5=-1, 
			opMask=0, // expandir p todas as impressoes (conforme demanda)			
			crNull=-1, 
			iPal=0,
			yCorteAbaixo=0, // 0=sem corte. Use de 1..20; Omite a impressao abaixo a partir desta altura.
			zoom=1,
				cr15=-1, cr7=-1, cr9=-1, cr1=-1, cr8=-1;
		int cel[][] = new int[21][21];
		String 
			cam = null,
			camErr = "jf1/erro03.jf1";
		boolean SAVE=true, OPEN=false;
		static boolean opEco=false;
		int HOR=1, VERT=2; // ateh daria p juntar as duas. Depois.
public Jf1(){
		cam = "???";
	}
public Jf1(int cr){
		int[][] w = new int[21][21];
		for(int m=1; m<=20; m++)
		for(int n=1; n<=20; n++)
			w[m][n] = cr;
		cel = w;
	}
public Jf1(String cm, String op){
		saveOpenJf1(OPEN, cm);
		if(J.tem('v',op)) invVert();
		if(J.tem('h',op)) invHor(); // mais tags depois, se precisar
}
public Jf1(Jf1 f){
		if(f==null) {
			f = new Jf1(camErr);
			cel = f.cel;
		} else {
			for(int m=1; m<=20; m++)
			for(int n=1; n<=20; n++)
				cel[m][n] = f.cel[m][n];
		}
			
	}
public Jf1(String cm){
		saveOpenJf1(OPEN, cm);
	}
public Jf1(String cam, int fn){
		// troca as cores 15,7,9,1,8 pela cor da faixa
		saveOpenJf1(OPEN, cam);
		trocaCores(fn);
	}	
public Jf1(String cam, int cr15, int cr7, int cr9, int cr1, int cr8){
		saveOpenJf1(OPEN, cam);	
		trocaCores(cr15,cr7,cr9,cr1,cr8);
	}		
public void tingir(int fa, int fn){
		// testar depois
		int cr=0;
		fa = fa- fa%10;
		fn = fn- fn%10;
		for(int m=0; m<=20; m++)
		for(int n=0; n<=20; n++){
			cr = cel[m][n];
			if(J.noInt(cr, fa,fa+9))
				cr=fn+cr%10;
			cel[m][n]=cr;
		}		
	}
public void impJf1(float x, float y, float xx, float yy){
		impJf1((int)x,(int)y,(int)(xx),(int)(yy));
	}		
public void impJf1Rel(float x, float y, float xx, float yy){
		//int mar = 50;
		if(x>J.maxXf) return;
		if(y>J.maxYf) return;
		if(x+xx<0) return;
		if(y+yy<0) return;
		impJf1((int)x,(int)y,(int)(x+xx),(int)(y+yy));
	}	
public void impJf1Rel(int x, int y, int xx, int yy){
		if(x>J.maxXf) return;
		if(y>J.maxYf) return;
		if(x+xx<0) return;
		if(y+yy<0) return;	
		impJf1(x,y,x+xx,y+yy);
	}
public void impRet(int cr, int i, int j, int ii, int jj){
	{ // limites
		i = J.corrInt(i,1,20);
		ii = J.corrInt(ii,1,20);
		j = J.corrInt(j,1,20);
		jj = J.corrInt(jj,1,20);		
	}
	{ // inversoes
		int aux=0;
		if(i>ii) {aux = i; i=ii; ii=aux; }
		if(j>jj) {aux = j; j=jj; jj=aux; }		
	}
	for(int m=i; m<=ii; m++)
	for(int n=j; n<=jj; n++)
		cel[m][n] = cr;
}	
public void impRetV(int cr, int i, int j, int ii, int jj){
	{ // limites
		i = J.corrInt(i,1,20);
		ii = J.corrInt(ii,1,20);
		j = J.corrInt(j,1,20);
		jj = J.corrInt(jj,1,20);		
	}
	{ // inversoes
		int aux=0;
		if(i>ii) {aux = i; i=ii; ii=aux; }
		if(j>jj) {aux = j; j=jj; jj=aux; }		
	}

	for(int m=i; m<=ii; m++){
		cel[m][j] = cr;
		cel[m][jj] = cr;
	}
	for(int n=j; n<=jj; n++){
		cel[i][n] = cr;
		cel[ii][n] = cr;		
	}
}	
public void impParteJf1(int x, int y, int xx, int yy, int i, int j, int ii, int jj){
		if(i>ii) {int aux=i; i=ii; ii=aux; }
		if(j>jj) {int aux=j; j=jj; jj=aux; }
		if(i<1)i=1;
		if(j<1)j=1;
		if(ii>20)ii=20;
		if(jj>20)jj=20;
		
		float
			tamX=(xx-x)/(ii-i),
			tamY=(yy-y)/(jj-j);		
			// tamX=(xx-x)/20f,
			// tamY=(yy-y)/20f;
		int cr=0, cr_=0, mm=0, nn=0, z=zoom,
			tmX=(int)tamX, tmY=(int)tamY;
			
		if(tmX<=0)tmX=1;
		if(tmY<=0)tmY=1;
		
		if(xx-x>20) tmX++;
		if(yy-y>20) tmY++;
		
		// if(crAnt!=-1) J.esc("ant/nov: "+crAnt+"/"+crNov);
		
		boolean opTrocaCor_InternoDesseMetodo=false;
		if(cr15!=-1) opTrocaCor_InternoDesseMetodo=true;
		

		
		for(int m=i; m<=ii; m++)	
		for(int n=j; n<=jj; n++){
			
			// inversoes e rotacoes aqui
			mm = m;
			nn = n;			

			/*
			if(opInv==VERT) nn = 21-n;
			if(opInv==HOR) mm = 21-m;
			if(opRot==1) { mm=n; nn=21-m; }
			if(opRot==2) { mm=21-m; nn= 21-n; }
			if(opRot==3) { mm=21-n; nn= m; }
			*/

			// CODIGO REPETIDO, MAS COERENTE, devidamente testado
			if(opMod!=0){
				if(opMod==INV_VERT) nn = 21-n; //1
				if(opMod==INV_HOR) mm = 21-m; //2
				if(opMod==ROT_DIR) { mm=n; nn=21-m; } //3
				if(opMod==ROT_DIRR) { mm=21-m; nn= 21-n; }//4
				if(opMod==ROT_ESQ) { mm=21-n; nn= m; } //5
				
				if(opMod==ROT_DIR_INV_HOR) { mm=n; nn= m; } //6
				if(opMod==ROT_DIRR_INV_HOR) { mm=m; nn=21-n;  }//7 = 1
				if(opMod==ROT_ESQ_INV_HOR) { mm=21-n; nn=21-m; } //8
				
				if(opMod==ROT_DIR_INV_VERT) { mm=21-n; nn= 21-m;  } //9
				if(opMod==ROT_DIRR_INV_VERT) { mm=21-m; nn= n; }//10
				if(opMod==ROT_ESQ_INV_VERT) { mm=n; nn= m;  } //11
				if(opMod==INV_VERT_HOR) {mm = 21-m; nn = 21-n; } //12			
			}	
			
			
			
			// transfomacoes de cor aqui
			cr = cel[mm][nn];
			if(cr==0) continue; // deve ajudar
			if(cr==crAnt) cr=crNov;
			
			if(cr!=0)	cr+=iPal;
			
			if(cr<0) cr=12; // apontar erro
			if(cr>512) cr=12;
			
			if(cr!=cr_){
				if(opTrocaCor_InternoDesseMetodo){
					if(cr==15) cr = cr15; else
					if(cr== 7) cr = cr7; else 
					if(cr== 9) cr = cr9; else 
					if(cr== 1) cr = cr1; else
					if(cr== 8) cr = cr8;
				}
				J.g.setColor(J.cor[cr]);
			}	
			if(cr!=0)	  
				J.g.fillRect(
					x+(int)((m-i)*tamX), 
					y+(int)((n-j)*tamY), 
						tmX, tmY);
			cr_=cr;			
		}
	}

	int qBlurX = 0, qBlurY=0, drx=0, dry=0;
public void impJf1(int x, int y, int xx, int yy){
		float // sssssssss
			tamX=(xx-x)/20f,
			tamY=(yy-y)/20f;
		int cr=0, cr_=0, mm=0, nn=0, z=zoom,
			mmm=0, nnn=0,// p efeito blur
			tmX=(int)tamX, tmY=(int)tamY;
		
			
		if(tmX<=0)tmX=1;
		if(tmY<=0)tmY=1;
		
		if(xx-x>20) tmX++;
		if(yy-y>20) tmY++;
		
		// if(crAnt!=-1) J.esc("ant/nov: "+crAnt+"/"+crNov);
		
		boolean opTrocaCor_InternoDesseMetodo=false;
		if(cr15!=-1) opTrocaCor_InternoDesseMetodo=true;

		for(int n=1; n<=20; n++) // inverti p nao bugar o mercanismo de corte
		for(int m=1; m<=20; m++)	{

			if(yCorteAbaixo!=0)
			if(n==yCorteAbaixo){
				yCorteAbaixo=0; // ?isso eh bom???
				return;
			}

	
			// inversoes e rotacoes aqui
			mm = m;
			nn = n;			


			/*
			if(opInv==VERT) nn = 21-n;
			if(opInv==HOR) mm = 21-m;
			if(opRot==1) { mm=n; nn=21-m; }
			if(opRot==2) { mm=21-m; nn= 21-n; }
			if(opRot==3) { mm=21-n; nn= m; }
			*/

			// CODIGO REPETIDO, MAS COERENTE, devidamente testado

			if(opMod!=0){			
				if(opMod==INV_VERT) nn = 21-n; //1
				if(opMod==INV_HOR) mm = 21-m; //2
				if(opMod==ROT_DIR) { mm=n; nn=21-m; } //3
				if(opMod==ROT_DIRR) { mm=21-m; nn= 21-n; }//4
				if(opMod==ROT_ESQ) { mm=21-n; nn= m; } //5
				
				if(opMod==ROT_DIR_INV_HOR) { mm=n; nn= m; } //6
				if(opMod==ROT_DIRR_INV_HOR) { mm=m; nn=21-n;  }//7 = 1
				if(opMod==ROT_ESQ_INV_HOR) { mm=21-n; nn=21-m; } //8
				
				if(opMod==ROT_DIR_INV_VERT) { mm=21-n; nn= 21-m;  } //9
				if(opMod==ROT_DIRR_INV_VERT) { mm=21-m; nn= n; }//10
				if(opMod==ROT_ESQ_INV_VERT) { mm=n; nn= m;  } //11
				if(opMod==INV_VERT_HOR) {mm = 21-m; nn = 21-n; } //12			
			}
			
			if(drx!=0) mm = 1+(mm+drx)%20;
			if(dry!=0) nn = 1+(nn+dry)%20;						
			
			
			// transfomacoes de cor aqui
			cr = cel[mm][nn];
			if(cr==0) continue; // deve ajudar

			if(cr==crAnt) cr=crNov;
			else if(cr==crAnt2) cr=crNov2;
			else if(cr==crAnt3) cr=crNov3;
			else if(cr==crAnt4) cr=crNov4;
			else if(cr==crAnt5) cr=crNov5;


			//if(cr!=0)	cr+=iPal;
			if(cr>=40)	cr+=iPal; 
			// === PORQUE "40" =======
			// fora cor nula 0
			// fora cores elementares 1..16
			// fora cinza especial 17..31 (bom p cintilancia de cursor, melhor reservar)
			// fora cor especial fogo 32..39
			// 40 p cima jah eh cinza normal

			if(opMask!=0) // util p blink na impressao de naves acertadas
				cr=opMask; // ISTO FAZ impMask() OBSOLETO!!! (Se eu lembrasse disso talvez eu nao tivesse desenvolvido "impMask()")
			
			if(cr<0) cr=12; // apontar erro
			if(cr>512) cr=12;
			
			if(cr!=cr_){
				if(opTrocaCor_InternoDesseMetodo){
					if(cr==15) cr = cr15; else
					if(cr== 7) cr = cr7; else 
					if(cr== 9) cr = cr9; else 
					if(cr== 1) cr = cr1; else
					if(cr== 8) cr = cr8;
				}
				J.g.setColor(J.cor[cr]);
			}
			
			mmm=0; nnn=0;
			if(qBlurX!=0) mmm=J.RS(qBlurX);
			if(qBlurY!=0) nnn=J.RS(qBlurY);
			
			if(qSkx!=0) mmm=(int)(-n*qSkx+20*qSkx);
			if(qSky!=0) nnn=(int)(m*qSky);
				
			
				
			if(cr!=0)	  
				J.g.fillRect(
					x+(int)(m*tamX-tamX)+mmm, 
					y+(int)(n*tamY-tamY)+nnn, 
						tmX, tmY);
			cr_=cr;			
			}
		}
		
		
		float qSkx=0f, qSky=0f;

	
public void impJf1(float x, float y){
	impJf1((int)x, (int)y);
}
public void impJf1(int x, int y){
		int cr=0, cr_=0, mm=0, nn=0, z=zoom;

		for(int m=1; m<=20; m++)
		for(int n=1; n<=20; n++){
			
			// inversoes e rotacoes aqui
			mm = m;
			nn = n;			
			if(opInv==VERT) nn = 21-n;
			if(opInv==HOR) mm = 21-m;
			if(opRot==1) { mm=n; nn=21-m; }
			if(opRot==2) { mm=21-m; nn= 21-n; }
			if(opRot==3) { mm=21-n; nn= m; }
			
			// transfomacoes de cor aqui
			cr = cel[mm][nn];
			if(cr==0) continue; // deve ajudar			
			if(cr==crAnt) cr=crNov;
			
			if(cr!=0)	cr+=iPal;
			
			if(cr<0) cr=12; // apontar erro
			if(cr>512) cr=12;
			
			if(cr!=cr_)	J.g.setColor(J.cor[cr]);
			if(cr!=0)	  J.g.fillRect(x+m*z-z, y+n*z-z, z, z);
			cr_=cr;
		}
	}

public void impJf1(int x, int y, Color cr15, Color cr7, Color cr9, Color cr1, Color cr8){
		int cr=0, cr_=0, mm=0, nn=0, z=zoom;
		Color crr=null;

		for(int m=1; m<=20; m++)
		for(int n=1; n<=20; n++){
			
			// inversoes e rotacoes aqui
			mm = m;
			nn = n;			
			if(opInv==VERT) nn = 21-n;
			if(opInv==HOR) mm = 21-m;
			if(opRot==1) { mm=n; nn=21-m; }
			if(opRot==2) { mm=21-m; nn= 21-n; }
			if(opRot==3) { mm=21-n; nn= m; }
			
			// transfomacoes de cor aqui
			cr = cel[mm][nn];
			if(cr==0) continue; // deve ajudar			
			//if(cr==crAnt) cr=crNov;
			
			//if(cr!=0)	cr+=iPal;
			
			if(cr<0) cr=12; // apontar erro
			if(cr>512) cr=12;

			if(cr==15) crr = cr15;
			if(cr== 7) crr = cr7;
			if(cr== 9) crr = cr9;
			if(cr== 1) crr = cr1;
			if(cr== 8) crr = cr8;
			
			if(cr!=cr_)	J.g.setColor(crr);
			if(cr!=0)	  J.g.fillRect(x+m*z-z, y+n*z-z, z, z);
			cr_=cr;
		}
	}
	
public void sobre(String cam){
		// coloca o jf1 de cam sobre este
		Jf1 f = new Jf1(cam);
		for(int m=1; m<=20; m++)
		for(int n=1; n<=20; n++)
			if(f.cel[m][n]!=0)
				cel[m][n] = f.cel[m][n];		
	}
public void impLevelRel(int nv, Color crm, int x, int y, int xx, int yy){
		// cel[][] q for <=nv terah o pixel impresso com a cor crm
		// copiei, colei e enxuguei. Deixei soh o codigo q precisa p ficar otimizado.
		
		xx+=x; // isso deveria estar por "opRel=true"
		yy+=y;
		
		float
			tamX=(xx-x)/20f,
			tamY=(yy-y)/20f;
		int mm=0, nn=0, z=zoom, cr=0,
			tmX=(int)tamX, tmY=(int)tamY;
			
		if(tmX<=0)tmX=1;
		if(tmY<=0)tmY=1;
		
		if(xx-x>20) tmX++;
		if(yy-y>20) tmY++;
		
		J.g.setColor(crm);		
		
		for(int n=1; n<=20; n++)
		for(int m=1; m<=20; m++)	{

			
			// inversoes e rotacoes aqui
			mm = m;
			nn = n;			
			if(opInv==VERT) nn = 21-n;
			if(opInv==HOR) mm = 21-m;
			if(opRot==1) { mm=n; nn=21-m; }
			if(opRot==2) { mm=21-m; nn= 21-n; }
			if(opRot==3) { mm=21-n; nn= m; }
			
			// CODIGO REPETIDO, MAS COERENTE, devidamente testado
			if(opMod!=0){			
				if(opMod==INV_VERT) nn = 21-n; //1
				if(opMod==INV_HOR) mm = 21-m; //2
				if(opMod==ROT_DIR) { mm=n; nn=21-m; } //3
				if(opMod==ROT_DIRR) { mm=21-m; nn= 21-n; }//4
				if(opMod==ROT_ESQ) { mm=21-n; nn= m; } //5
				
				if(opMod==ROT_DIR_INV_HOR) { mm=n; nn= m; } //6
				if(opMod==ROT_DIRR_INV_HOR) { mm=m; nn=21-n;  }//7 = 1
				if(opMod==ROT_ESQ_INV_HOR) { mm=21-n; nn=21-m; } //8
				
				if(opMod==ROT_DIR_INV_VERT) { mm=21-n; nn= 21-m;  } //9
				if(opMod==ROT_DIRR_INV_VERT) { mm=21-m; nn= n; }//10
				if(opMod==ROT_ESQ_INV_VERT) { mm=n; nn= m;  } //11			
				if(opMod==INV_VERT_HOR) {mm = 21-m; nn = 21-n; } //12
			}
			
			cr = cel[mm][nn];
			if(cr==0) continue; // deve ajudar			
			if(cr>nv) continue;
			if(cr<=nv)
				J.g.fillRect(
					x+(int)(m*tamX-tamX), 
					y+(int)(n*tamY-tamY), 
						tmX, tmY);

		}		
	}

public Jf1 cloneMod15(int tin){ 
	// 15 7 9 1 8
	Jf1 f = clone();
	f.trocaCores(tin);
	return f;
}
public Jf1 cloneMod99(int tin){ 
	// pressupoe original na faixa 90..99
	// assim, tin=79 deixará a imagem amarela
	Jf1 f = clone();
	f.tingir(99,tin);
	return f;
}
public Jf1 cloneTing(int fa, int fn){ 
	// pressupoe original na faixa 90..99
	// assim, tin=79 deixará a imagem amarela
	Jf1 f = clone();
	f.tingir(fa,fn);
	return f;
}
public Jf1 clone(){ 
	// parece q tah funcionando bem
	Jf1 w = new Jf1();
	for(int m=1; m<=20; m++)
	for(int n=1; n<=20; n++)
		w.cel[m][n] = cel[m][n];
	w.cam = cam;// ?mais alguma outra prop???	
	w.crAnt=  crAnt;
	w.crAnt2= crAnt2;
	w.crAnt3= crAnt3;
	w.crAnt4= crAnt4;
	w.crAnt5= crAnt5;
	w.crNov=  crNov;
	w.crNov2= crNov2;
	w.crNov3= crNov3;
	w.crNov4= crNov4;
	w.crNov5= crNov5;
	w.opMask =  opMask;
	w.crNull =  crNull;
	w.iPal =  iPal;
	w.yCorteAbaixo= yCorteAbaixo;
	w.zoom= zoom;
	w.cr15= cr15;
	w.cr7= cr7; 
	w.cr9= cr9;
	w.cr1= cr1;
	w.cr8= cr8; // acho q foi tudo...
	return w;
}	
public void blow2(){
	// usei no joe street figther, quando destroi muro no bonus
	int cr=0,m,n;
	for(int q=0; q<20; q++){
		m = 1+J.R(20);
		n = 1+J.R(20);
		cr = getPixel(m,n);
		cr--;
		if(cr<90)cr=0;
		setPixel(cr,m,n);
	}
}
public void blow(){
	// usei no joe street figther, quando destroi muro no bonus
	int cr=0,m,n,qq=2;
	for(int q=0; q<5; q++){
		m = 1+J.R(20);
		n = 1+J.R(20);
		for(int mm=-qq; mm<=+qq; mm++)
		for(int nn=-qq; nn<=+qq; nn++){
			cr = getPixel(m+mm,n+nn);
			cr--;
			if(cr<90)cr=0;
			setPixel(cr,m+mm,n+nn);
		}
	}
}
public void reload(){
	if(cam==null || cam=="???"){
		J.impErr("!caminho invalido: ["+cam+"]", "Jf1.reload()");
	}
	saveOpenJf1(OPEN, cam);
}

public void impMask(Color cr, int x, int y){
		int mm=0, nn=0, z=zoom;
		J.g.setColor(cr);
		for(int m=1; m<=20; m++)
		for(int n=1; n<=20; n++){
			
			// inversoes e rotacoes aqui
			mm = m;
			nn = n;			
			
			
			if(opInv==VERT) nn = 21-n;
			if(opInv==HOR) mm = 21-m;
			if(opRot==1) { mm=n; nn=21-m; }
			if(opRot==2) { mm=21-m; nn= 21-n; }
			if(opRot==3) { mm=21-n; nn= m; }
			
			
			// CODIGO REPETIDO, MAS COERENTE, devidamente testado
			if(opMod!=0){			
				if(opMod==INV_VERT) nn = 21-n; //1
				if(opMod==INV_HOR) mm = 21-m; //2
				if(opMod==ROT_DIR) { mm=n; nn=21-m; } //3
				if(opMod==ROT_DIRR) { mm=21-m; nn= 21-n; }//4
				if(opMod==ROT_ESQ) { mm=21-n; nn= m; } //5
				
				if(opMod==ROT_DIR_INV_HOR) { mm=n; nn= m; } //6
				if(opMod==ROT_DIRR_INV_HOR) { mm=m; nn=21-n;  }//7 = 1
				if(opMod==ROT_ESQ_INV_HOR) { mm=21-n; nn=21-m; } //8
				
				if(opMod==ROT_DIR_INV_VERT) { mm=21-n; nn= 21-m;  } //9
				if(opMod==ROT_DIRR_INV_VERT) { mm=21-m; nn= n; }//10
				if(opMod==ROT_ESQ_INV_VERT) { mm=n; nn= m;  } //11
				if(opMod==INV_VERT_HOR) {mm = 21-m; nn = 21-n; } //12			
			}

			
			if(cel[mm][nn]!=0)			
				J.g.fillRect(x+m*z-z, y+n*z-z, z, z);
			
		}
	}
public void impMask(Color crm, int x, int y, int xx, int yy){
		// copiei, colei e enxuguei. Deixei soh o codigo q precisa p ficar otimizado.
		
		
		
		float
			tamX=(xx-x)/20f,
			tamY=(yy-y)/20f;
		int mm=0, nn=0, z=zoom, cr=0,
			tmX=(int)tamX, tmY=(int)tamY;
			

		
		if(xx-x>20) tmX++;
		if(yy-y>20) tmY++;
		
		if(tmX<=0)tmX=1;
		if(tmY<=0)tmY=1;		
		
		J.g.setColor(crm);		
		
		for(int n=1; n<=20; n++)
		for(int m=1; m<=20; m++)	{

			
			// inversoes e rotacoes aqui			
			mm = m;
			nn = n;			
			/*
			if(opInv==VERT) nn = 21-n;
			if(opInv==HOR) mm = 21-m;
			if(opRot==1) { mm=n; nn=21-m; }
			if(opRot==2) { mm=21-m; nn= 21-n; }
			if(opRot==3) { mm=21-n; nn= m; }
			*/
			// CODIGO REPETIDO, MAS COERENTE, devidamente testado
			if(opMod!=0){			
				if(opMod==INV_VERT) nn = 21-n; //1
				if(opMod==INV_HOR) mm = 21-m; //2
				if(opMod==ROT_DIR) { mm=n; nn=21-m; } //3
				if(opMod==ROT_DIRR) { mm=21-m; nn= 21-n; }//4
				if(opMod==ROT_ESQ) { mm=21-n; nn= m; } //5
				
				if(opMod==ROT_DIR_INV_HOR) { mm=n; nn= m; } //6
				if(opMod==ROT_DIRR_INV_HOR) { mm=m; nn=21-n;  }//7 = 1
				if(opMod==ROT_ESQ_INV_HOR) { mm=21-n; nn=21-m; } //8
				
				if(opMod==ROT_DIR_INV_VERT) { mm=21-n; nn= 21-m;  } //9
				if(opMod==ROT_DIRR_INV_VERT) { mm=21-m; nn= n; }//10
				if(opMod==ROT_ESQ_INV_VERT) { mm=n; nn= m;  } //11
				if(opMod==INV_VERT_HOR) {mm = 21-m; nn = 21-n; } //12			
			}
			
			
			
			cr = cel[mm][nn];
			if(cr==0) continue; // deve ajudar			
			if(cr!=0)
				J.g.fillRect(
					x+(int)(m*tamX-tamX), 
					y+(int)(n*tamY-tamY), 
						tmX, tmY);

		}		
	}
public void impMaskRel(Color crm, int x, int y, int xx, int yy){
		// copiei, colei e enxuguei. Deixei soh o codigo q precisa p ficar otimizado.
		
		xx+=x; // isso deveria estar por "opRel=true"
		yy+=y;
		
		float
			tamX=(xx-x)/20f,
			tamY=(yy-y)/20f;
		int mm=0, nn=0, z=zoom, cr=0,
			tmX=(int)tamX, tmY=(int)tamY;
			
		if(tmX<=0)tmX=1;
		if(tmY<=0)tmY=1;
		
		if(xx-x>20) tmX++;
		if(yy-y>20) tmY++;
		
		J.g.setColor(crm);		
		
		for(int n=1; n<=20; n++)
		for(int m=1; m<=20; m++)	{

			
			// inversoes e rotacoes aqui
			mm = m;
			nn = n;			
			if(opInv==VERT) nn = 21-n;
			if(opInv==HOR) mm = 21-m;
			if(opRot==1) { mm=n; nn=21-m; }
			if(opRot==2) { mm=21-m; nn= 21-n; }
			if(opRot==3) { mm=21-n; nn= m; }
			
			// CODIGO REPETIDO, MAS COERENTE, devidamente testado
			if(opMod!=0){			
				if(opMod==INV_VERT) nn = 21-n; //1
				if(opMod==INV_HOR) mm = 21-m; //2
				if(opMod==ROT_DIR) { mm=n; nn=21-m; } //3
				if(opMod==ROT_DIRR) { mm=21-m; nn= 21-n; }//4
				if(opMod==ROT_ESQ) { mm=21-n; nn= m; } //5
				
				if(opMod==ROT_DIR_INV_HOR) { mm=n; nn= m; } //6
				if(opMod==ROT_DIRR_INV_HOR) { mm=m; nn=21-n;  }//7 = 1
				if(opMod==ROT_ESQ_INV_HOR) { mm=21-n; nn=21-m; } //8
				
				if(opMod==ROT_DIR_INV_VERT) { mm=21-n; nn= 21-m;  } //9
				if(opMod==ROT_DIRR_INV_VERT) { mm=21-m; nn= n; }//10
				if(opMod==ROT_ESQ_INV_VERT) { mm=n; nn= m;  } //11			
				if(opMod==INV_VERT_HOR) {mm = 21-m; nn = 21-n; } //12
			}
			
			cr = cel[mm][nn];
			if(cr==0) continue; // deve ajudar			
			if(cr!=0)
				J.g.fillRect(
					x+(int)(m*tamX-tamX), 
					y+(int)(n*tamY-tamY), 
						tmX, tmY);

		}		
	}
	
public void impMaskRel(Color cr15, Color cr7, Color cr9, Color cr1,Color cr8, int x, int y, int xx, int yy){
		xx+=x; // isso deveria estar por "opRel=true"
		yy+=y;
		float
			tamX=(xx-x)/20f,
			tamY=(yy-y)/20f;
		int mm=0, nn=0, z=zoom, cr=0,
			tmX=(int)tamX, tmY=(int)tamY;
			
		if(tmX<=0)tmX=1;
		if(tmY<=0)tmY=1;
		
		if(xx-x>20) tmX++;
		if(yy-y>20) tmY++;
		

		Color crr=null;
		for(int m=1; m<=20; m++)	
		for(int n=1; n<=20; n++){
			
			// inversoes e rotacoes aqui
			mm = m;
			nn = n;			
			if(opInv==VERT) nn = 21-n;
			if(opInv==HOR) mm = 21-m;
			if(opRot==1) { mm=n; nn=21-m; }
			if(opRot==2) { mm=21-m; nn= 21-n; }
			if(opRot==3) { mm=21-n; nn= m; }
			
			
			
			cr = cel[mm][nn];
			
			crr = null;
			if(cr==15) crr = cr15;
			if(cr== 7) crr = cr7;
			if(cr== 9) crr = cr9;
			if(cr== 1) crr = cr1;
			if(cr== 8) crr = cr8;
			if(crr==null) continue;
			if(cr==0) continue; // deve ajudar			

			J.g.setColor(crr);					
			
			J.g.fillRect(
				x+(int)(m*tamX-tamX), 
				y+(int)(n*tamY-tamY), 
					tmX, tmY);

		}		
	}
public void impMaskRel(Color pl[], int x, int y, int xx, int yy){
		// extrai o sombreamento da unidade
		// parametro como uma "sub-paleta"
		// a primeira cor (cr=0) seria a mais escura
		// vetor de 9 unidades, alem desta é ignorado
		
		xx+=x; // isso deveria estar por "opRel=true"
		yy+=y;
		
		float
			tamX=(xx-x)/20f,
			tamY=(yy-y)/20f;
		int mm=0, nn=0, z=zoom, cr=0,
			tmX=(int)tamX, tmY=(int)tamY;
			
		if(tmX<=0)tmX=1;
		if(tmY<=0)tmY=1;
		
		if(xx-x>20) tmX++;
		if(yy-y>20) tmY++;
		
		//J.g.setColor(crm);				
		int u = 0;
		for(int n=1; n<=20; n++)
		for(int m=1; m<=20; m++)	{

			
			// inversoes e rotacoes aqui
			mm = m;
			nn = n;			
			if(opInv==VERT) nn = 21-n;
			if(opInv==HOR) mm = 21-m;
			if(opRot==1) { mm=n; nn=21-m; }
			if(opRot==2) { mm=21-m; nn= 21-n; }
			if(opRot==3) { mm=21-n; nn= m; }
			
			// CODIGO REPETIDO, MAS COERENTE, devidamente testado
			if(opMod!=0){			
				if(opMod==INV_VERT) nn = 21-n; //1
				if(opMod==INV_HOR) mm = 21-m; //2
				if(opMod==ROT_DIR) { mm=n; nn=21-m; } //3
				if(opMod==ROT_DIRR) { mm=21-m; nn= 21-n; }//4
				if(opMod==ROT_ESQ) { mm=21-n; nn= m; } //5
				
				if(opMod==ROT_DIR_INV_HOR) { mm=n; nn= m; } //6
				if(opMod==ROT_DIRR_INV_HOR) { mm=m; nn=21-n;  }//7 = 1
				if(opMod==ROT_ESQ_INV_HOR) { mm=21-n; nn=21-m; } //8
				
				if(opMod==ROT_DIR_INV_VERT) { mm=21-n; nn= 21-m;  } //9
				if(opMod==ROT_DIRR_INV_VERT) { mm=21-m; nn= n; }//10
				if(opMod==ROT_ESQ_INV_VERT) { mm=n; nn= m;  } //11			
				if(opMod==INV_VERT_HOR) {mm = 21-m; nn = 21-n; } //12
			}
			
			cr = cel[mm][nn];
			if(cr==0) continue; // deve ajudar			
			if(cr!=0)
				u = cr%10;
				J.g.setColor(pl[u]);
				J.g.fillRect(
					x+(int)(m*tamX-tamX), 
					y+(int)(n*tamY-tamY), 
						tmX, tmY);

		}		
	}
public static Color[] iniSubPlt(Color cr){ // p usar na sub-paleta da impressao de mascara acima
	Color w[] = new Color[10];
	for(int q=0; q<10; q++)
		w[q] = J.altColor(cr,(q-10)*10);
	return w;
}


public void paste(Jf1 f){
	for(int m=1; m<=20; m++)
	for(int n=1; n<=20; n++)		
		if(f.cel[m][n]!=0)
			cel[m][n] = f.cel[m][n];
}
public void clear(int cr){
	// ?limite da cor???
	for(int m=1; m<=20; m++)
	for(int n=1; n<=20; n++)	
		cel[m][n] = cr;
}
	public void desCma(){
		for(int m=1; m<=20; m++)	
		for(int n=1; n<20; n++)	
			cel[m][n] = cel[m][n+1];
		//for(int m=1; m<=20; m++) cel[m][20] = 0;
	}
	public void desBxo(){
		for(int m=1; m<=20; m++)	
		for(int n=20; n>1; n--)	
			cel[m][n] = cel[m][n-1];
		//for(int m=1; m<=20; m++) cel[m][1] = 0;
	}

	public void desEsq(){
		for(int m=1; m<20; m++)	
		for(int n=1; n<=20; n++)	
			cel[m][n] = cel[m+1][n];
		//for(int n=1; n<=20; n++) cel[20][n] = 0;
	}	
	public void desDir(){
		for(int m=20; m>1; m--)	
		for(int n=1; n<=20; n++)	
			cel[m][n] = cel[m-1][n];
		//for(int n=1; n<=20; n++) cel[1][n] = 0;
	}		
	public void impMask(int cr, int x, int y){
			J.impErr("!funcao nao implementada:","Jf1.impMask(cr,x,y)");
	}
	public void trocaCores(int p){
		int q=2,v=0;
		for(int m=1; m<=20; m++)
		for(int n=1; n<=20; n++){
			v = cel[m][n];
			if(v==15) cel[m][n] = p;
			if(v== 7) cel[m][n] = p-q;
			if(v== 9) cel[m][n] = p-q*2;
			if(v== 1) cel[m][n] = p-q*3;
			if(v== 8) cel[m][n] = p-q*4;
		}
	}
	public Jf1 trocaCores2(
			int p15, int p7, int p14, int p6, int p13, int p5, int p12, int p4, 
			int p11, int p3, int p10, int p2, 
			int p9, int p1){
		int q=2,v=0;
		for(int m=1; m<=20; m++)
		for(int n=1; n<=20; n++){
			v = cel[m][n];
			if(v==15) cel[m][n] = p15;
			if(v== 7) cel[m][n] = p7;			
			if(v==14) cel[m][n] = p14;
			if(v== 6) cel[m][n] = p6;
			if(v==13) cel[m][n] = p13;
			if(v== 5) cel[m][n] = p5;
			if(v==12) cel[m][n] = p12;
			if(v== 4) cel[m][n] = p4;
			if(v==11) cel[m][n] = p11;
			if(v== 3) cel[m][n] = p3;			
			if(v==10) cel[m][n] = p10;
			if(v== 2) cel[m][n] = p2;
			if(v== 9) cel[m][n] = p9;
			if(v== 1) cel[m][n] = p1;
		}
		return this;
	}
/*
public void rotEsq(){
		if(maxX!=maxY) J.impErr("!A imagem precisa ter mesmas dimensoes X:Y para ser rotacionada sem perdas. Arq: "+cam);
		Jf3 f = new Jf3(0);
		for(int m=0; m<=maxX; m++)
		for(int n=0; n<=maxY; n++)
			f.cel[m][n] = cel[maxX-n][m];			
		cel = f.cel;				
	}
	public void rotDir(){
		if(maxX!=maxY) J.impErr("!A imagem precisa ter mesmas dimensoes X:Y para ser rotacionada sem perdas. Arq: "+cam);		
		Jf3 f = new Jf3(0);
		for(int m=0; m<=maxX; m++)
		for(int n=0; n<=maxY; n++)
			f.cel[m][n] = cel[n][maxY-m];
		cel = f.cel;
	}
*/
	public void rotDir(){ 
		Jf1 f = new Jf1(0);		
		for(int m=1; m<=20; m++)
		for(int n=1; n<=20; n++)
			f.cel[m][n] = cel[n][21-m];		
		cel = f.cel;
	}
	public void rotEsq(){ 
		Jf1 f = new Jf1(0);		
		for(int m=1; m<=20; m++)
		for(int n=1; n<=20; n++)
			f.cel[m][n] = cel[21-n][m];			
		cel = f.cel;
	}	
	public void girar(boolean d){ 
		// rotDir() e rotEsq() seriam mais intuitivos e práticos. 
		// ... tá pronto acima
		Jf1 f = new Jf1(0);
		if(d){ // aa dir
			for(int m=1; m<=20; m++)
			for(int n=1; n<=20; n++)
				f.cel[m][n] = cel[n][21-m];
		} else {
			for(int m=1; m<=20; m++)
			for(int n=1; n<=20; n++)
				f.cel[m][n] = cel[21-n][m];			
		}
		cel = f.cel;
	}
	public void contornoEsq(int cr){
		// use com giro p contornar tudo... serah?
		Jf1 f = new Jf1(0);
		{ // a esq
			for(int m=1; m<20; m++)
			for(int n=1; n<=20; n++){
				if(cel[m+1][n]!=0)
				if(cel[m][n]==0)
					f.cel[m][n] = cr;
			}
		}
		paste(f);
	}
	public void contorno(int cr){
		// efeito aura
		for(int q=0; q<4; q++){
			contornoEsq(cr);
			girar(true);
		}
	}
	
public void invHor(){
		Jf1 f = new Jf1(0);
		for(int m=1; m<=20; m++)
		for(int n=1; n<=20; n++)
			f.cel[21-m][n] = cel[m][n];
		cel = f.cel;
	}
public void invVert(){
		Jf1 f = new Jf1(0);
		for(int m=1; m<=20; m++)
		for(int n=1; n<=20; n++)
			f.cel[m][21-n] = cel[m][n];
		cel = f.cel;
	}
	public void impRet(int cr, int i, int j, int ii, int jj, boolean vazado){
		// coordenadas absolutas
		int aux=0;
		if(i>ii) {aux=i; i=ii; ii=aux; }
		if(j>jj) {aux=j; j=jj; jj=aux; }
		
		if(!vazado)
		for(int m=i; m<=ii; m++)
		for(int n=j; n<=jj; n++)
			cel[m][n] = cr;		
		
		if(vazado){
			for(int m=i; m<=ii; m++){
				cel[m][j] = cr;
				cel[m][jj] = cr;
			}		
			for(int n=j; n<=jj; n++){
				cel[i][n] = cr;
				cel[ii][n] = cr;
			}					
		}
		
	}
	public void trocaFaixaCores(int fa, int fn){
		// precisa testar 
		// pensei em usar no Jomberman
		fa = fa - fa%10;
		fn = fn - fn%10;
		int v=0;
		
		for(int m=1; m<=20; m++)
		for(int n=1; n<=20; n++){
			v = cel[m][n];
			if(J.noInt(v, fa,fa+9))
				cel[m][n]=fn+v%10;
		}
	}
	public void trocaCor(int cra, int crn){
		// parece ser mais intuitivo o nome acima.
		trocaCores(cra,crn);
	}
	public void trocaCores(int cra, int crn){
		for(int m=1; m<=20; m++)
		for(int n=1; n<=20; n++)
			if(cel[m][n]==cra) 
				cel[m][n]=crn;
	}	
	public void trocaCores(int cr15, int cr7, int cr9, int cr1, int cr8){
		for(int m=1; m<=20; m++)
		for(int n=1; n<=20; n++){
			if(cel[m][n]==15) cel[m][n]=cr15;
			if(cel[m][n]==7) cel[m][n]=cr7;
			if(cel[m][n]==9) cel[m][n]=cr9;
			if(cel[m][n]==1) cel[m][n]=cr1;
			if(cel[m][n]==8) cel[m][n]=cr8;
		}
	}	
	public void putPixel(int v, int i, int j){
		if(J.noInt(i,1,20))
		if(J.noInt(j,1,20))		
		if(J.noInt(v,0,512))
			cel[i][j]=v;
	}
	public int getPixel(int i, int j){
		// retorna -1 se fora do intervalo
		if(J.noInt(i,1,20))
		if(J.noInt(j,1,20))
			return cel[i][j];
		return -1;
	}
	public void setPixel(int cr, int i, int j){
		if(cr>512) return;
		if(cr<0) return;
		if(J.noInt(i,1,20))
		if(J.noInt(j,1,20))
			cel[i][j]=cr;
	}	
	public boolean salvarJf1(String cm){
		return saveJf1(cm); // por compatibilidade
	}
	public boolean saveJf1(String cm){ // saveJf1
		// mais intuitivo... mas não seria melhor simplesmente um "save()"???
		cm = J.corrCam(cm,"jf1");
		cam = cm;
		RandomAccessFile arq = null;
		try{
			arq = new RandomAccessFile(new File(cam),"rw");
			for(int m=1; m<=20; m++)
			for(int n=1; n<=20; n++)
				arq.write(cel[m][n]);
			arq.close();
			J.esc("jf1 salvo: "+cam);
			return true;
		}catch(IOException e ){
			J.impErr("falha salvando jf1: "+cam,"Jf1.saveJf1()");
		}
		return false;
	}
	
	
private void saveOpenJf1(boolean s, String cm){
		cm = J.corrCam(cm,"jf1");
		
		if(!s)
		if(!J.arqExist(cm)){
			J.impErr("arquivo perdido: ["+cm+"]");
			Jf1 f = new Jf1(camErr);
			cel = f.cel;
			return;
		}	
		
		RandomAccessFile arq = null;
		try{
			arq = new RandomAccessFile(cm,"rw");
			int v = 0;
			
			if(!s)
				for(int m=1; m<=20; m++)
				for(int n=1; n<=20; n++){
					v = (int)arq.read();
					cel[m][n] = v;
				}
			else 	
				for(int m=1; m<=20; m++)
				for(int n=1; n<=20; n++){
					v = cel[m][n];
					arq.writeInt(v);
				}			
			arq.close();
			if(opEco)J.esc("ok: ["+cm+"]");
			cam = cm;
		}catch(IOException e){
			e.printStackTrace();      			
			J.impErr("!falha "+(s?"salvando":"abrindo")+" arquivo: ["+cm+"]", "Jf1.saveOpenJf1()");
			Jf1 er = new Jf1(camErr);
			cel = er.cel;
			try{arq.close();}catch(IOException ee){}
		}
	}

		static Mouse ms = null;
	public boolean impISO(float i, float j, float zom){
		// impressao ISO, p sinCity, Age e similares
		// reconhece hit do mouse
	
		int cr=0;
		float alt=zom, lar=alt+alt, mm=0, nn=0;
		int l = (int)lar;	//if(l==0) l=1;
		int a = (int)alt;	//if(a==0) a=1;
		a++; l++;
		for(int m=1; m<=20; m++)
		for(int n=1; n<=20; n++){
/*
			ambas partem do ponto de impressao
			ambas multiplicam m e n
			mm sempre somarah na mult, mas nn fica - em m
			x sempre mul lar, y sempre mult alt
*/						
			cr = cel[m][n];
			mm = i+m*lar+n*lar  -zom-zom-zom    -20*lar;// p meio
			nn = j+n*alt-m*alt  -zom; // "+zom" é p calibragem fina
			J.impRetRel(cr,0, (int)mm,(int)nn,l,a+a);
		}
		
		// centro
		J.impRetRel(12,0, i+lar,j,lar,alt);
		
		if(ms==null) {
			// tolerancia p inicializacao do mouse
		  if(J.cont>1000) J.impErr("Mouse nao inicializado","Jf1.impISO()");			
			return false;
		}
		{// ver hit
			float q = zom*20f*1.6f;
			float qq =zom*10f*1f;
			int ii = (int)(i+lar-q);
			int iii = (int)(i+lar+q);
			int jj = (int)(j+alt-qq);
			int jjj = (int)(j+alt+qq);
			//ms.opImpArea=true;
			if(ms.naArea(ii,jj,iii,jjj)) return true;
			return false;
		}
	}
	public boolean impMaskISO(Color crp, float i, float j, float zom){
		// impressao ISO, p sinCity, Age e similares
		// reconhece hit do mouse
	
		float alt=zom, lar=alt+alt, mm=0, nn=0;
		int l = (int)lar;	//if(l==0) l=1;
		int a = (int)alt;	//if(a==0) a=1;
		Color cr = null;
		a++; l++;
		for(int m=1; m<=20; m++)
		for(int n=1; n<=20; n++){
/*
			ambas partem do ponto de impressao
			ambas multiplicam m e n
			mm sempre somarah na mult, mas nn fica - em m
			x sempre mul lar, y sempre mult alt
*/						
			if(cel[m][n]!=0) cr = crp;
			else cr = null;
			mm = i+m*lar+n*lar  -zom-zom-zom    -20*lar;// p meio
			nn = j+n*alt-m*alt  -zom; // "+zom" é p calibragem fina
			J.impRetRel(cr,null, (int)mm,(int)nn,l,a+a);
		}
		
		// centro
		//J.impRetRel(12,0, i+lar,j,lar,alt);
		
		if(ms==null) {
			// tolerancia p inicializacao do mouse
		  if(J.cont>1000) J.impErr("Mouse nao inicializado","Jf1.impISO()");			
			return false;
		}
		{// ver hit
			float q = zom*20f*1.6f;
			float qq =zom*10f*1f;
			int ii = (int)(i+lar-q);
			int iii = (int)(i+lar+q);
			int jj = (int)(j+alt-qq);
			int jjj = (int)(j+alt+qq);
			//ms.opImpArea=true;
			if(ms.naArea(ii,jj,iii,jjj)) return true;
			return false;
		}
	}
	
}




