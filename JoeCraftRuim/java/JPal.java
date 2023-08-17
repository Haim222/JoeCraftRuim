// para manuseio de cores em JoeCraft
import java.awt.Color;

public class JPal{
		static int 
			opContraste = 16,
			opBrilho = 40;
		static float opAlfa=0; // mas tah invertido o parametro. 0=transparente; 1=opaco;	
		static final int 
			lmCrPal = 10; // p mais e p menos
		static boolean opProBuf=false;
		Color crSem = null;
		Color[] cr = null;
		static JPal swapPal=null; // nao aponte p este mesmo.
		static Color crTing = null;
		static float qTing = 0.25f; //use [0f..1f] quantidade de tingimento: 0= sem tingimento; 1 = tingimento completo em 100%; Para remover o tingimento use "setTing(0f,cor)" ou "setTing(#,null)"
	public JPal(Color crp){
		geraCores(crp);
	}
	public static void setTing(float qtd, Color crt){
		// com crt = null, o tingimento eh removido
		if(qtd>1f)qtd=1f;
		if(qtd<0.001f)qtd=0f;
		crTing = crt;
		qTing = qtd;
	}	
		static boolean opSuperClaro=false;
		int vr=J.R(255), vg=J.R(255), vb=J.R(255);

	public Color get(int nv){
		if(nv<-lmCrPal) nv = -lmCrPal;
		if(nv>+lmCrPal) nv = +lmCrPal;		

		if(crTing!=null)
			if(qTing>0f)
				if(qTing<=1f)
					return J.mixColor(qTing,crTing,cr[nv+lmCrPal]);		
		
		return cr[nv+lmCrPal];
	}
	public Color _get(int nv){
		if(opSuperClaro) nv = +lmCrPal; // chato, mas todas as outras cores sao desperdicadas. Usei p lampadas;
		
		if(nv<-lmCrPal) nv = -lmCrPal;
		if(nv>+lmCrPal) nv = +lmCrPal;
		
			
		if(crTing!=null)
		if(qTing>0f)
		if(qTing<=1f){
			return J.mixColor(qTing,crTing,cr[nv+lmCrPal]);
		}
		
		if(opAlfa!=0) 
		// mas o parametro tah invertido: 0% de alfa seria opaco! Agora jah foi.
		// leia "opAlfa" como "opOpaco"
			return J.altAlfa(cr[nv+lmCrPal], opAlfa);
			// return crEsp[nv+lmCrPal];
			
		if(swapPal!=null)
			return swapPal.cr[nv+lmCrPal]; 
					// pequena gambiarra p underWater eficiente
					// repare de quem eh nv e lmCrPal
			
		return cr[nv+lmCrPal]; // nv zero deve bater com a cor semente.
		// chato, mas java nao permite arrays de indice negativo ou do tipo 100..200. Deve ser calculado na unha mesmo.
	}
	public void geraCores(int r, int g, int b, int a){
		geraCores(new Color(r,g,b,a));
	}
	public void geraCores_(Color crp){
		// o ideal eh a cor zero como semente, indices negativos indicariam "para o escuro", indices altos positivos indicariam "cores saturadas". Isso deve ser ajustado em "get()"
		crp = J.altColor(crp, opBrilho);
		crSem = crp;		
		Color[] w = new Color[(lmCrPal<<1)+1];
		for(int q=-lmCrPal; q<=+lmCrPal; q++){
			w[q+lmCrPal] = J.altColor(crSem, q*opContraste);
		}
		cr = w;
	}
	public void geraCores(Color crp){
		crp = J.altColor(crp, opBrilho);
		crSem = crp;		
		Color[] w = new Color[(lmCrPal<<1)+1];
		for(int q=-lmCrPal; q<=+lmCrPal; q++)
			w[q+lmCrPal] = crSem;
		w[0] = J.mixColor(0.9f, J.cor[161],crSem); // melhor q cor 0 0 0 
		//w[0] = J.mixColor(0.9f, J.cor[61],crSem); // esverdeado
		//w[0] = J.mixColor(0.9f, J.cor[16],crSem); 
		w[(lmCrPal<<1)] = J.mixColor(0.9f, J.cor[15],crSem);
		cr = w;
		degrade(0,lmCrPal-1);
		degrade(lmCrPal+1, lmCrPal+lmCrPal);
	}
	public void degrade(int min, int max){
		int lar = max-min;
		float r,g,b,rr,gg,bb, a,aa;
		r = cr[min].getRed();
		g = cr[min].getGreen();
		b = cr[min].getBlue();
		a = cr[min].getAlpha();
		rr = cr[max].getRed();
		gg = cr[max].getGreen();
		bb = cr[max].getBlue();		
		aa = cr[max].getAlpha();		
		float pr,pg,pb,pa;
		pr=(rr-r)/lar;
		pg=(gg-g)/lar;
		pb=(bb-b)/lar;
		pa=(aa-a)/lar;
		Color w=null;
		r-=pr;
		g-=pg;
		b-=pb;
		a-=pa;
		for(int k=min; k<=max; k++){			
			r+=pr;
			g+=pg;
			b+=pb;
			a+=pa;
			w = new Color((int)r,(int)g,(int)b,(int)a);
			cr[k]=w;
		}
	}	
	
	
	public void imp(int i, int j){
		// soh p debug
		int lar = 6, alt=12; // dimensoes de cada retangulo
		Color w=null;
		for(int q=0; q<=(lmCrPal<<1); q++){
			w = null;
			if(q==lmCrPal) w = J.cor[15];
			
			if(opProBuf)
				J.bufRetRel(
					cr[q], w, 
					i+q*lar, 
					j,
					lar-1,
					alt);
			else 	
				J.impRetRel(
					cr[q], w, 
					i+q*lar, 
					j,
					lar-1,
					alt);			
		}	
	}
}
