// impressao de imagens JPG, PNG, com carregamento condicional: soh carrega quando for imprimir.






// abaixo serviu p rotacao de imagem em angulos finos
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;

import java.util.ArrayList;


import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.awt.Graphics;
// import java.awt.image.BufferStrategy; // p escolher o modo de impressao mais rapido



public class JFig{
		BufferedImage fig = null;
		static boolean opCarrOnImp = true;
		int maxX=100, maxY=100, opMod=0; 
		final int 
			INV_VERT=1, // p usar com "opMod", mas não sei se todas as opcoes estão funcionando.
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
		String cam = null;
		Graphics ggg = null;


		// ROTACAO DE IMAGEM!!! Deu certo, mas precisa adaptar o codigo abaixo.
		// no loop principal:
		// 	if(J.C(10))f.fig = f.rotateImage(f.fig, 1f);
		// precisa entender o eixo e outras coisas, mas já rotaciona
		// !!!!!!!!!!!!!!!!!!
		
	public static BufferedImage rotateImage(BufferedImage image, float ang){
		// este tá mais enxuto
		final double rads = Math.toRadians(ang);
		final double sin = Math.abs(Math.sin(rads));
		final double cos = Math.abs(Math.cos(rads));
		final int w = (int) Math.floor(image.getWidth() * cos + image.getHeight() * sin);
		final int h = (int) Math.floor(image.getHeight() * cos + image.getWidth() * sin);
		final BufferedImage rotatedImage = new BufferedImage(w, h, image.getType());
		final AffineTransform at = new AffineTransform();
		at.translate(w / 2, h / 2);
		at.rotate(rads,0, 0);
		at.translate(-image.getWidth() / 2, -image.getHeight() / 2);
		final AffineTransformOp rotateOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		return rotateOp.filter(image,rotatedImage);		
		//return image;
	}
	public static BufferedImage rotateImage2(BufferedImage rotateImage, double angle) {
		// este aqui tem um eixo, mas tem q entender
    AffineTransform tx = new AffineTransform();
    tx.rotate(
			Math.toRadians(angle), 
			rotateImage.getWidth() / 2.0f, 
			rotateImage.getHeight() / 2.0f);

    // Rotaciona as coordenadas dos cantos da imagem
    Point2D[] aCorners = new Point2D[4];
    aCorners[0] = tx.transform(new Point2D.Double(0.0, 0.0), null);
    aCorners[1] = tx.transform(new Point2D.Double(rotateImage.getWidth(), 0.0), null);
    aCorners[2] = tx.transform(new Point2D.Double(0.0, rotateImage.getHeight()), null);
    aCorners[3] = tx.transform(new Point2D.Double(rotateImage.getWidth(), rotateImage.getHeight()), null);

    // Obtém o valor de translação para cada eixo com um canto "escondido"
    double dTransX = 0;
    double dTransY = 0;
    for(int i = 0; i < 4; i++) {
        if(aCorners[i].getX() < 0 && aCorners[i].getX() < dTransX)
            dTransX = aCorners[i].getX();
        if(aCorners[i].getY() < 0 && aCorners[i].getY() < dTransY)
            dTransY = aCorners[i].getY();
    }

    // Aplica a translação para evitar cortes na imagem
    AffineTransform translationTransform = new AffineTransform();
    translationTransform.translate(-dTransX, -dTransY);
    tx.preConcatenate(translationTransform);

    return new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR).filter(rotateImage, null);
	
	
	}

		BufferedImage fAng=null;
		float ang_=0;
	public void impAng(float ang, float zm, float i, float j){
		// CONSEGUI!!! GRAÇAS A DEUS!!!
		// Saiba: o ponto de rotação sempre será o centro da imagem.
		// Veja: este método tende a renderizar a rotação em tempo de execução cada vez q a imagem é rotacionada (e não imprimida). Logo, mesmo q se use um buffer intermediário, o clock pode ser afetado a cada mudança de angulo. Amenizar isto seria pre-renderizar as imagens nos angulos comuns e colocah-las em um buffer reserva, daí, imprimir na hora devida. Futuramente talvez eu implemente isto.
		if(fAng==null || ang_!=ang){			
			fAng = rotateImage(fig, ang);
			ang_=ang;
		}
		
		int mx = (int)(fAng.getWidth()*0.5f*zm); // a chave p alterar o centro de rotação deve ser este "0.5" aí.
		int my = (int)(fAng.getHeight()*0.5f*zm); // se precisar eu implemento depois.
		
		J.g.drawImage(fAng,
			(int)i-mx,(int)j-my,
			(int)i+mx,(int)j+my,
				0,0, fAng.getWidth(),fAng.getHeight(), null);
				
//J.g.drawImage(bufImp, ii,j,i,jj, 0,0, maxXbuf,maxYbuf, null);				
				
	}
	
	public static ArrayList<JFig> carr8fig(String rad){ // open8Jfig carr8Jfig carr9Jfig open9Jfig open9Png open9fig
		// usei em machine wars, 
		// serve p abrir as 9 imagens p cada direção do casco da unidade. 
		// Ex: hoverA0.png, hoverA1.png ... hoverA8.png
		// repare q começa em 0 e vai até 8
		// nao é "00" e apenas "0", só um char
		ArrayList<JFig> lis = new ArrayList<>();
		for(int q=0; q<9; q++)
			lis.add(new JFig(rad+q+".png"));
		return lis;
	}
	public JFig(String cm, float ang){
		cam = cm;
		//if(!opCarrOnImp) 
			carr();
		BufferedImage f = rotateImage(fig, ang);
		fig = f;
	}	
	

	public void blur_(int p){ // copiei de Jf3. É boa.
		Color cr,crc,crb,cre,crd;
		for(int w=0; w<=p; w++)
		for(int m=0; m<=maxX; m++)
		for(int n=0; n<=maxY; n++)
		if((m+n)%2==0){ 
				cr = getPixel(m,n);
				crc = getPixel(m,n-1);
				crb = getPixel(m,n+1);
				crd = getPixel(m+1,n);
				cre = getPixel(m-1,n);
				if(crc==null) crc=cr;
				if(crb==null) crb=cr;
				if(cre==null) cre=cr;
				if(crd==null) crd=cr;
				cr = J.mixColor(cr,crc,crb,cre,crd);
				setPixel(cr, m,n);
				//setPixel(15, m,n);								
			}
		}
	public void blur(){ // copiei de Jf3. É boa.
		Color cr,crc,crb,cre,crd,crcc,crbb,cree,crdd;
		//for(int w=0; w<=2; w++)
		int q = J.R(2);
		for(int m=0; m<=maxX; m++)
		for(int n=0; n<=maxY; n++)
		if((m+n)%2==q)
		{
				crc=null; crcc=null;
				crb=null; crbb=null;
				cre=null; cree=null;
				crd=null; crdd=null;
				cr = getPixel(m,n);
				crc = getPixel(m,n-1);
				crcc = getPixel(m,n-2);
				crb = getPixel(m,n+1);
				crbb = getPixel(m,n+2);
				crd = getPixel(m+1,n);
				crdd = getPixel(m+2,n);
				cre = getPixel(m-1,n);
				cree = getPixel(m-2,n);
				if(crc==null) crc=cr;
				if(crb==null) crb=cr;
				if(cre==null) cre=cr;
				if(crd==null) crd=cr;
				if(crcc==null) crcc=cr;
				if(crbb==null) crbb=cr;
				if(cree==null) cree=cr;
				if(crdd==null) crdd=cr;				
				cr = J.mixColor(cr,crc,crb,cre,crd);
				cr = J.mixColor(cr,crcc,crbb,cree,crdd);
				setPixel(cr, m,n);
				//setPixel(15, m,n);								
			}
		}


/* COMO CLONAR, (implementar depois). tag @@@@@@@@@@@@@@@
1- em ss, adicionar "implements Cloneable". Cuidado com a grafia.
2- add metodo clone q retorna o mesmo tipo dentro dessa classe
3- add no metodo "clone() throws CloneNotSupportedException"
4- no corpo do metodo clone: "return (Cub)super.clone();", com cast e tudo
5- add um "@Override" logo acima do metodo clone

	public class Cub implements Cloneable{
	
		@Override	
		public Cub clone() throws CloneNotSupportedException{
			return (Cub)super.clone();
		}
	}

*/		
		public JFig clonar_(){
			// nao eh o ideal, mas funciona.
			return new JFig(cam); 
		}

		public JFig clonar__(){
			if(fig==null) carr();
			JFig f = new JFig("tela320x200.png"); 
			// ?!?!?!?!?! porcamente, mas funciona!
			// bem... se for assim, é melhor abrir o arquivo de novo. :|
			f.carr();
			int cr = 0;
			for(int m=0; m<maxX; m++)
			for(int n=0; n<maxY; n++){
				cr = fig.getRGB(m,n);
				f.fig.setRGB(m,n, cr);
			}			
			f.maxX = maxX; // gambiarra
			f.maxY = maxY;
			return f;
		}
		public JFig clonar(){ // clone()
			JFig f = new JFig();
			f.fig = copyImage(fig);
			f.maxX=maxX;
			f.maxY=maxY;
      f.cam= new String(cam);// ok???
			return f;
		}
		public JFig clone(){ // mais pratico
			return clonar();
		}
		
/*
"Modifier and Type Constant Field Value"
BufferedImage.getType()

TYPE_3BYTE_BGR 5 
TYPE_4BYTE_ABGR 6 
TYPE_4BYTE_ABGR_PRE 7 
TYPE_BYTE_BINARY 12 
TYPE_BYTE_GRAY 10 
TYPE_BYTE_INDEXED 13 
TYPE_CUSTOM 0 
TYPE_INT_ARGB 2 
TYPE_INT_ARGB_PRE 3 
TYPE_INT_BGR 4 
TYPE_INT_RGB 1 
TYPE_USHORT_555_RGB 9 
TYPE_USHORT_565_RGB 8 
TYPE_USHORT_GRAY 11 

*/		

private BufferedImage copyImage(BufferedImage source){
// codigo de terceiros, mas funciona
// organizar melhor isso depois
/*	
    BufferedImage b = 
			new BufferedImage(
				source.getWidth(), 
				source.getHeight(), 
				source.getType());
*/				
    BufferedImage b = 
			new BufferedImage(
				maxX, 
				maxY, 
				6);

    Graphics g = b.getGraphics();
    g.drawImage(source, 0, 0, null);
    g.dispose();
    return b;
}		
	public JFig crop(int i, int j, int ii, int jj){		
		// FUNCAO: "colhe" apenas uma area da imagem retornando esta área como um JFig
		{ // inversao de i ii se ii<i, idem p j
			if(ii<i){int aux=i; i=ii; ii=aux; }
			if(jj<j){int aux=j; j=jj; jj=aux; }
		}
		{ // limitar se i<0, idem p j; // limitar se i>maxX, idem p j
			if(i<0) i=0;
			if(j<0) j=0;
			if(i>maxX)i=maxX;
			if(j>maxY)j=maxY;
		}
		{ // reportar erro se esta matriz estiver nula
			if(fig==null) J.impErr("!a matriz original de pixels está nula!!!", "JFig.crop()");
		}
		
		JFig f = new JFig(ii-i, jj-j);
		Color cr = null;
		for(int m=i; m<ii; m++)
		for(int n=j; n<jj; n++){
			cr = getPixel(m,n);
			f.setPixel(cr, m-i, n-j);
		}
		return f;
	}
	public JFig(){
//		cam = "nulo100x100.png";
//		if(!opCarrOnImp) carr();		
	}
	public JFig(Color cr, int lar, int alt){
		cam = null;
		maxX = lar; maxY=alt;
		fig = new BufferedImage(lar, alt, BufferedImage.TYPE_INT_ARGB);						
		impRet(cr,null, 0,0,lar,alt);
	}
	public JFig(int lar, int alt){
		// GRAÇAS A DEUS!!!
		// a imagem criada será transparente, claro q precisa preencher os pixels.
		// suporta alfa. Este "TYPE_INT_ARGB", neste "A" especifica isto. Existem outros tipos, mas são só complicação.
		// parece funcionar bem.
		cam = null;
		maxX = lar; maxY=alt;
		fig = new BufferedImage(lar, alt, BufferedImage.TYPE_INT_ARGB);				
	}
	public JFig(String cm){
		cam = cm;
		if(!opCarrOnImp) carr();
	}
	public JFig(BufferedImage doClipBoard){
		// CONSEGUI!!! GRAÇAS A DEUS!!!
		// aqui é só copiar uma imagem pelo GThumb e usar:
		// imagem = new JFig(J.getFigClipBoard());
		cam = "???";
		fig = doClipBoard;
		maxY = doClipBoard.getHeight();
		maxX = doClipBoard.getWidth();
	}	
	public void colar(JFig f, int i, int j){
		// ?Não seria melhor um "impFig()"??? Já q JFig pode ser usado como um buffer.
		// andou falhando este metodo. Não acho q funciona sempre. Precisa ajustar.
		Color v=null;
		for(int m=0; m<f.maxX; m++)
		for(int n=0; n<f.maxY; n++){
			v = f.getPixel(m,n);
			setPixel(v, m+i, n+j); 
		}		
	}
	public void colar_(JFig f, int i, int j){
		// ?Não seria melhor um "impFig()"??? Já q JFig pode ser usado como um buffer.
		// andou falhando este metodo. Não acho q funciona sempre. Precisa ajustar.
		int v;
		for(int m=0; m<f.maxX; m++)
		for(int n=0; n<f.maxY; n++){
			v = f.getInt(m,n);
			setInt(v, m+i, n+j); // limite embutido
		}
	}
	public void colar(JFig f, float i, float j){
		colar(f,(int)i, (int)j); // pq ficar fazendo cast é um cocô
	}	
	public void reload(){
		carr(); // pressupoe cam jah definido
	}
	public void carr(){
		// já dá p abrir um jf1 por aqui normalmente.
		if(cam==null) J.impErr("!o caminho da imagem esta nulo!","JFig.carr()");
		//cam = J.corrCam(cam,"png");
		// mas deve procurar jpg tb!
		
		{// abrindo um Jf1 aqui
			if(J.tem(".jf1",cam)){ // ?maiusculas???
				// nao precisa corrigir o caminho
				Color crNula = new Color(255,0,0, 127);
				JFig f = new JFig(crNula, 20,20);
				Jf1 ff = new Jf1(cam); 
				for(int m=0; m<20; m++)
				for(int n=0; n<20; n++)
					f.putPixel(ff.cel[m+1][n+1], m,n);				
				fig = f.fig;
				cam=ff.cam;
				maxX=20;
				maxY=20;
				return;
			}
		}
		
		/* só no windows
		// corrigindo caminho aqui mesmo
		if(!J.tem("/",cam))
		if(!J.tem("\\",cam))
		if(!J.tem(":",cam))	
			cam = "jpg/"+cam;
			//cam = "c:\\java\\jpg\\"+cam;
		*/
		if(!J.tem("/",cam)) cam = "/home/haim/JJJ/java/jpg/"+cam;
		if(!J.arqExist(cam)) J.impErr("!>>>arquivo perdido: "+cam);		
		
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(cam));
			if(img==null) return; // ?Sem bug???
			maxY = img.getHeight();
			maxX = img.getWidth();			
			fig = img;
			J.esc("ok: "+cam);
			ggg = fig.getGraphics();
		} catch (IOException e) {	
			J.impErr("!falha abrindo arquivo: "+cam);
		}
	}


	public JFig truncar(int mxx, int mxy){
		// corta bordas direita e abaixo
		// deve ser melhor lapidada, mas jah funciona
		fig = fig.getSubimage(0,0,mxx,mxy);
		maxX=mxx; maxY=mxy;
		return this;
	}
	public void clear(Color cr){
		if(ggg==null) ggg = fig.getGraphics();
		ggg.setColor(cr);
		ggg.fillRect(0,0,maxX,maxY);
	}	

	public void blow(){ // detonar(), detonate(), destruir(), borrar()
		int m,n,qq=(int)(maxX*5/100f);
		Color cr = null, cr0 = new Color(0,0,0,0);
		for(int q=0; q<10; q++){
			m = J.R(maxX);
			n = J.R(maxY);
			for(int mm=0; mm<=10; mm++)
			for(int nn=0; nn<=10; nn++){
				cr = getColor(m+mm,n+nn);
				cr = J.mixColor(0.5f, cr, J.cor[16]);
				//cr = J.cor[15];
				setColor(cr,m+mm,n+nn);
			}
		}
	}
	public void blow_(){ // detonar(), detonate(), destruir(), borrar()
		// usei p quando se dá dano num sprite png
		// tem q melhorar este efeito, mas já é alguma coisa
		int m,n,qq=(int)(maxX*5/100f);
		Color cr = null, cr0 = new Color(0,0,0,0);
		for(int q=0; q<10; q++){
			m = J.R(maxX);
			n = J.R(maxY);
			for(int mm=-qq; mm<=+qq; mm++)
			for(int nn=-qq; nn<=+qq; nn++){
				setPixel(cr0,m+mm,n+nn);
			}
		}
	}
	public void blow__(){
		int m=0, n=0, r=(int)(maxX*10/100f);
		Color cr0 = J.altAlfa(J.cor[12],1f);
		Color cr16 = J.altAlfa(J.cor[16],1f);
		for(int q=0; q<5; q++){
			m = J.R(maxX);
			n = J.R(maxY);
			impCirc(cr16, null, (int)r, m,n);
			impCirc(cr0,  null, (int)(r*0.8f), m,n);
		}
	}

/*	ATENÇÃO!!! Macete na troca de cor:
		crAnt = fig.getColor(ms.x, ms.y);
		fig.trocaInt(crAnt.getRGB(),crNov.getRGB());
		assim funciona, mas variações sutis da cor na compactação não são detectadas.
		este problema é contornável
*/
	public void trocaInt(int cra, int crn){
		// não tem nada a ver com a paleta 256!
		// esse tá funcionando bem
		// apenas tomar cuidado p esperar a imagem acabar de carregar antes de trocar algum pixel
		// alfa não tá trocando
		for(int m=1; m<maxX; m++)
		for(int n=1; n<maxY; n++){
			if(getInt(m,n)==cra)
				setInt(crn,m,n);
		}
	}
	public void trocaColor(Color cra, Color crn){
		// acho q não tá funcionando tão bem assim, precisa testar melhor.
		// prefira o trocaInt() acima q parece melhor
		// leia as observações antes de testar mais p cima antes de tratar disto.
		for(int m=1; m<maxX; m++)
		for(int n=1; n<maxY; n++){
			if(getColor(m,n)==cra)
				setColor(crn,m,n);
		}		
	}

	// impressao	
	public void imp(int i, int j){
		imp(i,j,i+maxX,j+maxY);
	}
	public void imp(float i, float j){
		// já q é um saco ficar fazendo cast
		imp((int)i,(int)j,(int)i+maxX,(int)j+maxY);
	}	
	public void impRel(Color crFrente, int i, int j, int ii, int jj){
		// Usei p imprimir equipamentos habilitados/desabilitados no jogo da nave2d				
		// crFrente é bom com transparencia p mostrar item desabilitado. 
		//if(crFundo!=null) J.impRet(crFundo, null, i,j,ii+i, jj+j); 
		imp(i,j,ii+i, jj+j);
		if(crFrente!=null) J.impRet(crFrente, null, i,j,ii+i, jj+j); 
	}
	public void impRel(int i, int j, int ii, int jj){
		imp(i,j,ii+i, jj+j);
	}
	public void impRel(float i, float j, float ii, float jj){
		imp((int)(i),(int)(j),(int)(ii+i), (int)(jj+j));
	}	
	public void imp(float i, float j, float ii, float jj){
		imp((int)i, (int)j,(int)ii,(int)jj);
	}

// !!!!!!!!!!!!!!!!!! EXPERIMENTAL
/*
	public void imp(int i, int j, int ii, int jj, int iii, int jjj, int iiii, int jjjj){
		/*
        int x1 = 100;
        int y1 = 100;
        int x2 = 200;
        int y2 = 100;
        int x3 = 250;
        int y3 = 200;
        int x4 = 50;
        int y4 = 200;

        int[] xPoints = { x1, x2, x3, x4 };
        int[] yPoints = { y1, y2, y3, y4 };
		
		    int[] xPoints = {i,ii,iii,iiii};
		    int[] yPoints = {j,jj,jjj,jjjj};

        J.g.drawImage(fig, xPoints, yPoints, null);		
	}
	*/

	public void imp(float zm, float i, float j){
		imp((int)i,(int)j,(int)(i+maxX*zm),(int)(j+maxY*zm));
	}
		int omtCmaCrt=30; // omitir a parte de cima da cortina; Mais direcoes depois se precisar.
	public void imp(int i, int j, int ii, int jj){
		// agrupar aqui
		if(fig==null) carr();
		// nao usar "J.impFig()" pq tah zuado
		
		if(opMod!=0){
			int aux=0;
			if(opMod==INV_VERT) {aux=j; j=jj; jj=aux; }; //1
			if(opMod==INV_HOR) {aux=i; i=ii; ii=aux; }; //2
			
			// fazer estes abaixo conforme precisar
			//if(opMod==INV_HOR) mm = 21-m; //2
			// if(opMod==ROT_DIR) { mm=n; nn=21-m; } //3
			// if(opMod==ROT_DIRR) { mm=21-m; nn= 21-n; }//4
			// if(opMod==ROT_ESQ) { mm=21-n; nn= m; } //5
			
			// if(opMod==ROT_DIR_INV_HOR) { mm=n; nn= m; } //6
			// if(opMod==ROT_DIRR_INV_HOR) { mm=m; nn=21-n;  }//7 = 1
			// if(opMod==ROT_ESQ_INV_HOR) { mm=21-n; nn=21-m; } //8
			
			// if(opMod==ROT_DIR_INV_VERT) { mm=21-n; nn= 21-m;  } //9
			// if(opMod==ROT_DIRR_INV_VERT) { mm=21-m; nn= n; }//10
			// if(opMod==ROT_ESQ_INV_VERT) { mm=n; nn= m;  } //11
			//if(opMod==INV_VERT_HOR) {mm = 21-m; nn = 21-n; } //12			
			if(opMod==INV_VERT_HOR) {//12			
				aux=i; i=ii; ii=aux; 
				aux=j; j=jj; jj=aux;				
			} 

		}	

		J.g.drawImage(fig, i,j,ii,jj, 0,0, maxX,maxY, null);
		if(cortina!=null) J.g.drawImage(cortina.fig, i,j+omtCmaCrt,ii,jj, 0,0+omtCmaCrt, maxX,maxY, null);
			// nao sei se tá certo acima
	}
		JFig cortina=null;
	public void addCortina(Color p){
		// usei p meio-underWater em machine wars
		// cortina seria uma mascara com a cor p somente onde existe uma cor válida(meio opaca) na imagem original
		// claro q aceita p como cor meio transparente
		// use omtCmaCrt p omitir a cortina na parte de cima da imagem
		int a=0, r=0, g=0, b=0;
		Color cr = null;//111111111
		r = p.getRed();
		g = p.getGreen();
		b = p.getBlue();
		a = p.getAlpha();
		int ac=0, ai=0;
		cortina = new JFig(p, maxX,maxY);		
		for(int m=0; m<maxX; m++)
		for(int n=0; n<maxY; n++){
			ai = getAlfa(m,n);
			if(ai<a) ac=ai; else ac=a;
			cortina.setPixel(r,g,b,ac, m,n);			
			//cortina.setAlfa(a, m,n);			
		}
	}

	public void impArea(float i, float j, float ii, float jj, float m, float n, float mm, float nn){
		impArea((int)i,(int)j,(int)ii,(int)jj,(int)m,(int)n,(int)mm,(int)nn);
	}
	public void impArea(int i, int j, int ii, int jj, int m, int n, int mm, int nn){
		if(fig==null) carr();
		// nao use "J.impFig()" q tah zuado
		J.g.drawImage(fig, i,j,ii,jj, m,n,mm,nn, null);
	}	
	
	public boolean save(String cam){ // ?qual eh a extencao??? R: Informe PNG q o programa da conta até de salvar transparências.
		File outputfile = new File(cam);
		boolean ok=false;
		
		String ext = J.extArq(cam);
		ext = J.emMinusc(ext);		
		
		try{ 
			ImageIO.write(fig, ext, outputfile);
			ok = true;
			J.esc("ok: "+cam);
		} catch(IOException e){
			ok = false;
			J.impErr("!falha salvando imagem: "+cam,"JFig.save()");
		}
		return ok;	
	}	

// Manipulando pixels. Os dois abaixo são mais intuitivos
	public void setPixel(Color cr, int i, int j){
		setColor(cr,i,j);
	}
	public Color getPixel(int i, int j){
		return getColor(i,j); // por compatibilidade
	}

	public void putPixel(int cr, int i, int j){
		if(cr<0) cr=0;
		if(cr>512) cr=512;
		setColor(J.cor[cr],i,j);
	}
	public void setPixel(int r, int g, int b, int a, int i, int j){
		// limites p cores???
		setColor(new Color(r,g,b,a), i,j); // ?tah criando com alfa??? R: SIM!!! Mas a imagem tem q ser do tipo q aceita alfa. O novo construtor desta classe (com lar e alt) faz bem o serviço.
	}
	public void setColor(Color cr, int i, int j){
		if(fig!=null)
		if(cr!=null)
		if(i>=0)
		if(j>=0)
		if(i<maxX)
		if(j<maxY)
			fig.setRGB(i,j, cr.getRGB()); //2222222222
	}
	public Color getColor(int i, int j){
		if(fig!=null)
		if(i>=0)
		if(j>=0)
		if(i<maxX)
		if(j<maxY)  // sem alfa por enquanto. Este "true" indicaria um "hasAlpha", mas tem q ver isso melhor.
			return new Color(getInt(i,j),true); // ?Isso tah funcionando com alfa???
		if(fig==null) J.impErr("!Erro: figura nao carregada ","JFig.getColor()");			
		return null;
	}
	public int getInt(int i, int j){
		if(fig!=null)
		if(i>=0)
		if(j>=0)
		if(i<maxX)
		if(j<maxY)  // sem alfa por enquanto... mas tem como contornar. Ver trecho "J.getIntFromColor()", depois.
			return fig.getRGB(i,j);
		if(fig==null) J.impErr("!Erro: figura nao carregada ","JFig.getInt()");
		return 0;
	}
	public int getRed(int i, int j){ 
		Color cr = getPixel(i,j);
		if(cr!=null) return cr.getRed();
		if(fig==null) J.impErr("!Erro: figura nao carregada ","JFig.getRed()");		
		return 0;
	}
	public int getGreen(int i, int j){ 
		Color cr = getPixel(i,j);
		if(cr!=null) return cr.getGreen();
		if(fig==null) J.impErr("!Erro: figura nao carregada ","JFig.getGreen()");		
		return 0;
	}  
	public int getBlue(int i, int j){ 
		Color cr = getPixel(i,j);
		if(cr!=null) return cr.getBlue();
		if(fig==null) J.impErr("!Erro: figura nao carregada ","JFig.getBlue()");		
		return 0;
	}  
	public int getAlfa(int i, int j){ //getAlpha
		Color cr = getPixel(i,j);
		if(cr!=null) return cr.getAlpha();
		if(fig==null) J.impErr("!Erro: figura nao carregada ","JFig.getAlfa()");		
		return 0;
	}
	public void setAlfa(int a, int i, int j){  // setAlpha
    if(a>255) a=255;
    if(a<0) a=0;
		Color cr = getPixel(i,j);
    cr = J.altAlfa(cr, a); 
    setPixel(cr, i,j); // foi???		
	}  
	public void setInt(int cr, int i, int j){
		if(fig!=null)
		if(i>=0)
		if(j>=0)
		if(i<maxX)
		if(j<maxY)
			fig.setRGB(i,j, cr);
	}	
	// nao existe "setRed()" e similares

	public int[][] getMatRed(){
		// retorna a matriz int do canal red		
		// nao sei se green, blue e alfa serao necessarias. O tempo dirá.
		int mat[][] = new int[maxX][maxY];
		int cr = 0;
		for(int m=0; m<maxX; m++)
		for(int n=0; n<maxY; n++){
			cr = getRed(m,n);
			mat[m][n] = cr;
		}
		return mat;
	}
	public JFig getFigRed(){
		// retorna o JFig do canal red
		JFig f = new JFig(maxX, maxY);
		int r = 0, a=0;
		for(int m=0; m<maxX; m++)
		for(int n=0; n<maxY; n++){
			r = getRed(m,n);
			a = getAlfa(m,n);
			f.setPixel(r,0,0,a, m,n);			
		}
		return f;		
	}
	public JFig getFigGreen(){

		JFig f = new JFig(maxX, maxY);
		int g = 0, a=0;
		for(int m=0; m<maxX; m++)
		for(int n=0; n<maxY; n++){
			g = getGreen(m,n);
			a = getAlfa(m,n);
			f.setPixel(0,g,0,a, m,n);			
		}
		return f;		
	}
	public JFig getFigBlue(){
		
		JFig f = new JFig(maxX, maxY);
		int b = 0, a=0;
		for(int m=0; m<maxX; m++)
		for(int n=0; n<maxY; n++){
			b = getBlue(m,n);
			a = getAlfa(m,n);
			f.setPixel(0,0,b,a, m,n);			
		}
		return f;		
	}
	public JFig getFigAlfa(){
		
		JFig f = new JFig(maxX, maxY);
		int a=0;
		for(int m=0; m<maxX; m++)
		for(int n=0; n<maxY; n++){
			a = getAlfa(m,n);
			f.setPixel(a,a,a,a, m,n);			
		}
		return f;		
	}
	public JFig getResized(int i, int j){ // resize()
		JFig f = new JFig(i,j);
		if(f.ggg==null) f.ggg = f.fig.getGraphics();
		f.ggg.drawImage(fig, 0,0,i,j, 0,0, maxX,maxY, null);
		return f; // foi???
	}
	public JFig getFigPB(){
		// filtro preto e branco p estudos de qrCode
		return getFigPB(127,127,127);
	}
	public JFig getFigPB(int lr, int lg, int lb){
		// filtro preto e branco p estudos de qrCode
		// lr[0..255]: limite red, p canal respectivo. Acima disto será branco, abaixo preto.
		int r=0, g=0, b=0, a=0, v=0;
		JFig f = new JFig(maxX, maxY);
		for(int m=0; m<maxX; m++)
		for(int n=0; n<maxY; n++){
			r = getRed(m,n);
			g = getGreen(m,n);
			b = getBlue(m,n);
			a = getAlfa(m,n);
			if(r>lr) r=255; else r=0;
			if(g>lg) g=255; else g=0;
			if(b>lb) b=255; else b=0;
			//if(a>la) a=255; else a=0;
			v = r+g+b;
			if(v>255) v=255;
			
			f.setPixel(v,v,v,a, m,n);
		}
		return f;
	}
	public void tingir(int qr, int qg, int qb){ 
		// ainda nao perfeito...
    // ?E o alfa??? tá faltando outro parametro... Seria bom p definir a transparencia geral.
		
		// qq canal de cor q for acima de qr, qg, qb serah reduzido a qr, qg e qb
		
		if(fig==null) carr(); // isso mesmo se tiver "opCarrOnImp=true", claro q isso eh necessario.
		
		// o certo seria trocar a cor somente pela quantidade de branco do pixel original, mas talvez tenha ocasioes em q seja necessario diminuir o contraste da imagem. Estudar melhor isso depois.
		
		Color cr = null;
		int crr=0, crg=0, crb=0, cra=0;
		for(int m=0; m<maxX; m++)
		for(int n=0; n<maxY; n++){
			cr = getPixel(m,n);
			crr = cr.getRed();
			crg = cr.getGreen();
			crb = cr.getBlue();
			cra = cr.getAlpha();			
			if(crr+crg+crb==0) { // gambiarra p corrigir o alfa;
				setPixel(new Color(0,true), m,n);
				continue;
			}
			if(crr>qr) crr=qr;
			if(crg>qg) crg=qg;
			if(crb>qb) crb=qb;
			
			crr = J.corrInt(crr,0,255);
			crg = J.corrInt(crg,0,255);
			crb = J.corrInt(crb,0,255);
			cra = J.corrInt(cra,0,255);			
			
			setPixel(new Color(crr,crg,crb,cra), m,n);
		}		
	}
	public void tingir(Color p, float q){
		// esta tb tá boa. Leva em conta o canal alfa (todas deveriam ver isto)
		// ... mas parece q nem sempre tá funcionando. Acho q depende do formato interno do png. Precisa investigar.
		
		if(fig==null) carr();
		
		q = 1f-q; // o parametro tava invertido, nao sei se alterou os outros fontes.

		Color cr=null;
		int al=0;
		for(int m=0; m<maxX; m++)
		for(int n=0; n<maxY; n++){
			cr = getColor(m,n);
			if(cr==null) continue;
			al = cr.getAlpha();
			if(al<=0) continue;
			cr = J.mixColor(q, cr,J.altAlfa(p,al));
			setPixel(cr, m,n);
		}
	}

// TÁ REPETIDO !!!
		public void tingir(float q, int cr){
			// limites depois
			tingir(q, J.cor[cr]);
		}
		public void tingir(float q, Color cr){
			// CONSEGUI!!!... mas acho q não funciona p qq imagem
			// mas acho q falta ver quando eh com transparencia
			int v = 0;
			Color w = null;
			for(int m=0; m<maxX; m++)
			for(int n=0; n<maxY; n++){
				v = getInt(m,n);
				if(v!=0){ // rbg 0:0:0 eh a cor transparente
					w = J.mixColor(q, cr, new Color(v));
					setPixel(w, m,n);
				}
			}	
		}


	// linhas
	public void impLin(int cr, int i, int j, int ii, int jj){
		impLin(J.cor[cr], i,j,ii,jj);
	}
	public void impLin(Color cr, int i, int j, int ii, int jj){
		// agrupar aqui
		if(ggg==null)	ggg = fig.getGraphics();
		
		// ?seria bom verificar limites? Ex: se estiver fora da tela, nem imprime.
		if(cr!=null){
			ggg.setColor(cr);
			ggg.drawLine(i,j,ii,jj);
		}	
	}
	
	// retangulo
	public void impRet(int cr, int crr, int i, int j, int ii, int jj){
		// coordenadas absolutas		
		Color cr_ = J.cor[cr];
		Color crr_ = J.cor[crr];
		if(cr==0) cr_=null; // assim omite a cor devida quando for retangulo vazado
		if(crr==0) crr_=null;		
		impRet(cr_,crr_,i,j,ii,jj);
	}
	public void impRetRel(int cr, int crr, int i, int j, int ii, int jj){
		impRet(cr,crr,i,j,i+ii,j+jj);
	}
	public void impRetRel(Color cr, Color crr, int i, int j, int ii, int jj){
		impRet(cr,crr, i,j,i+ii,j+jj);
	}
	public void impRet(Color cr, Color crr, int i, int j, int ii, int jj){
		// agrupar aqui
		// coordenadas absolutas
		if(ggg==null)	ggg = fig.getGraphics();
		
		if(cr!=null){
			ggg.setColor(cr);
			ggg.fillRect(i,j, ii-i,jj-j);			
		}
		if(crr!=null){
			ggg.setColor(crr);
			ggg.drawRect(i,j, ii-i,jj-j);			
		}		
	}

	// triangulo
	public void impTri(int cr, int crr, int i, int j, int ii, int jj, int iii, int jjj){
		impTri(J.cor[cr], J.cor[crr], i,j,ii,jj,iii,jjj);
	}
	public void impTri(Color cr, Color crr, int i, int j, int ii, int jj, int iii, int jjj){
		// agrupar aqui
		if(ggg==null)	ggg = fig.getGraphics();
		int m[]= {i,ii,iii};
		int n[]= {j,jj,jjj};
		
		if(cr!=null){
			ggg.setColor(cr);
			ggg.fillPolygon(m,n,m.length);		
		}		
		if(crr!=null){
			ggg.setColor(crr);
			ggg.drawPolygon(m,n,m.length);		
		}				
	}
	
	// circulo
	public void impCirc(Color cr, Color crr, int r, int x, int y){
		if(cr!=null){
			ggg.setColor(cr);
			ggg.fillArc(x-r,y-r, r+r,r+r, 1,360);	
			// 			  x, y, width, height, angIni, angFin
		}
		if(crr!=null)
		if(crr!=cr){
			ggg.setColor(crr);				
			ggg.drawArc(x-r,y-r, r+r,r+r, 1,360);	
		}
	}		


	public void setAlfa(int q){ // escrita errada, contornando...
		setAlpha(q);
	}
	public void setAlpha(int q){ // setAlpha
		// aplica alfa na imagem inteira.
		// funciona bem
		// q = 0..255; 0=totalmente transparente
		Color cr;
		int al;
		for(int m=0; m<maxX; m++)
		for(int n=0; n<maxY; n++){
			cr = getColor(m,n);
			al = cr.getAlpha();
			if(al>q) al = q;
			cr = J.altAlfa(cr,al);
			setPixel(cr, m,n);
		}		
	}
}
