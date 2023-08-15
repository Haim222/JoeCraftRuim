/* === PARTICULAS ======================

- o cod abaixo ficou bem otimizado 
	- nao usa float, logo, fica mais rapido
	- usa deslocamento de bits p acelerar, nao tem operacoes com floats
	- tah enxuto (eu tava inspirado, (e nao era de madrugada!))
	- usa "iterator" p otimizar remocoes
	
INSTRUCOES:
fora e acima do loop principal:
	JPar par = new JPar();

dentro do loop principal:
	par.reg();
	
no trecho onde as particulas devem ser geradas:	
	add(cor, qtd, x,y); // cor aceita int ou Color

para saber o numero de particulas ativas
	int numPar = par.size();
	// obsoleto:
	int numPar = par.getNumPar()// este eh obsoleto, mas conservei por compatibilidade
*/

import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;



public class JPar{
		static boolean opPause=false; // adivinha...
		boolean	opShine=false; // contorno cintilante de particulas. O nome nao tah apropriado. Mudar depois. Blink talvez.
			// opGrav = true; // NAO MAIS ASSIM, vide abaixo
		int // mudar esses dois nomes depois. Precisa ser mais intuitivo.
			opGrav = 0, // esquema novo
			opLentGeral=1, // normal=1, esta eh reducao de processamento
			xTerr=0, yTerr=0, // dah p amarrar com outro fonte por aqui
			ajClock=1, // ajuste geral de velocidade p fontes lentos (usei em JomberMan)
			opTam=5, // tamanho das particulas
			incX=0, incY=0, //usei no jogo de canhoes 2d, tipo diep.io
			xCam=0, yCam=0, // incX e xCam fazem a mesma coisa, deixei as duas vars por compatibilidade
			opVel = 12, //antigo "rv", velocidade de esparcamento: quanto maior, mais rapido a particula anda
			opRed = 40; //antigo "rt", lerdeza de reducao de tamanho: quanto menor, mais rapido a particula diminui ateh sumir
		ArrayList<Par> par = new ArrayList<>();


	public int getNumPar(){ // nome obsoleto, mas precisei conservar por compatibilidade
		return par.size();
	}	
	public int size(){ // mais intuitivo assim
		return par.size();
	}		
	public void add(int cr, int qtd, float i, float j){
		add(J.cor[cr], qtd, i,j);
	}
	public void add(Color cr, int qtd, float i, float j){
		int ii=(int)i;
		int jj=(int)j;
		for(int q=0; q<qtd; q++){
			par.add(new Par(cr, ii,jj,0,0));
		}
	}
	public void add(int cr, int qtd, float i, float j, float pivx, float pivy){
		add(J.cor[cr], qtd, i,j, pivx, pivy);
	}
	public void add(Color cr, int qtd, float i, float j, float pivx, float pivy){
		for(int q=0; q<qtd; q++){
			par.add(new Par(cr, i,j, pivx, pivy));
		}
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
		int mult = 4;
	public class Par{
			boolean rem=false;
			int 			
				x=0, y=0, 
				vx=0, vy=0, 
				tam=opTam;
			Color cr=null;	
		public Par(Color crp, float ii, float jj, float pivx, float pivy){
			int i = (int)ii;
			int j = (int)jj;
			x=(i<<mult); y=(j<<mult);
			vx = J.RSn0(opVel);
			vy = J.RSn0(opVel);
			
			if(pivx!=0)	vx+=(((int)(pivx))<<mult);
			if(pivy!=0)	vy+=(((int)(pivy))<<mult);
			
			vx*=ajClock;
			vy*=ajClock;
			//cr = crp;
			cr=J.altColor(crp,-J.R(100));
		}	
		public void reg(){
			if(!opPause) 
			if(J.cont%opLentGeral==0){
				
				if(J.R(opRed)==0) tam--;
				if(tam<=0) rem=true;
				x+=vx; y+=vy;			
				
				vy+=opGrav;
			}

			imp();
		}
		public void imp(){			
			Color crr=null;
			if(cr==J.cor[12]) crr = J.cor[32+J.R(8)];
			else crr = cr;
			
			Color crc = null;
			if(opShine) crc = J.cor[12+J.R(3)*2];
			int m = (x>>>mult)+xTerr+incX+xCam; // precisaria trocar incX por xCam, mas tem q ajustar todos os outros fontes.
			int n = (y>>>mult)+yTerr+incY+yCam; // acho q assim nao detona a compatibilidade
			
			if(m>J.maxXf) rem=true; // adeus bug fdp
			if(n>J.maxYf) rem=true;
			if(m<0) rem=true;
			if(n<0) rem=true;
			
			J.impRetRel(crr, crc,
			  m, n, 
				tam,tam);
		}
	}	
}	
