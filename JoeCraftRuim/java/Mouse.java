/*
PASSOS:
	1 - use "extends JFrame" na sua classe principal
	2 - declare o objeto "Mouse meuMouse=null;" como atributo dessa classe
	3 - crie um metodo de inicialização e chame-o externamente pelo construtor com "new iniciaMouse()"
	4 - no metodo "iniciaMouse()" chamado pelo construtor, insira "meuMouse = new Mouse(this)". Esse "this" faz referencia a sua classe principal derivada do "JFrame"
	5 - no loop principal(veja abaixo) do seu programa, insira "zMouse()" como o ultimo comando desse loop
	6 - calibre dClick0=10, dClick9=100 para reconhecer clique duplo no seu fonte. Veja q varia conforme o "peso" da aplicação q se está construindo

SOBRE O LOOP PRINCIPAL:
	- Sua classe principal deve derivar da classe "javax.swing.JFrame".
	- esta, por sua vez, deve conter uma subClasse MeuPainel derivada de "JPanel"
	- "MeuPainel" deve ser adicionado à classe principal com um "add(MeuPainel)"
	- o método "paint" de "meuPainel" deve ser sobrescrito. Esse metodo se comportaria como o loop principal.
	
EXPLICANDO ATRIBUTOS:
	- boolean clicou: mostra se QUALQUER botao foi pressionado
	- boolean b1 e b2: "true" se o botao respectivo foi acionado	- boolean moveu: "true" se houve algum deslocamento do mouse
	- int x e y: coordenadas atuais
	- int dx e dy: deslocamento atual positivo ou negativo. É zerado por "zMouse()"
	- boolean rodou: "true" se a roda do mouse foi movida
	- int dr: o quanto a roda foi girada
	
EXPLICANDO METODOS:	
	- boolean naArea(x,y,xx,yy): "true" se mouse dentro desse retangulo. Coordenadas absolutas cimaEsquerda e baixoDireita.
	- void proMeio(): move o mouse pro meio da tela. Bom p jogos tipo Minecraft
	- void zMouse(): é importantíssimo q seja inserido no ultimo passo DENTRO do seu loop principal
*/

import java.awt.event.MouseAdapter;
import javax.swing.JFrame;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.Robot;

// p customizar a imagem do cursor
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

// p debug
// import java.applet.AudioClip;


public class Mouse extends MouseAdapter implements MouseListener, MouseMotionListener,MouseWheelListener {	
		JFrame frameDoCursor = null; // definido na criacao do mouse
		int 
			corrX = -12, corrY=-40, // calibragem fina
			cClick = 0, dClick0=10, dClick9=100, // clique duplo calibravel
			cLastClick=-1, cLastClickk=-1, cLastClickkk=-1; // -1 quando nenhum clique foi feito.
			//corrX = -4-3, corrY=-22+8-16; // calibragem fina p linux
		boolean 
			opInvHor=false, // inverte horizontalmente o mouse 3d, usei na inversao de buffer de impressao em JoeCraft
			opInvVert=false, // ... mas nao tá funcionando na versao mais nova do joeCraft. Fiquei em duvida se removo.

			b1=false, b2=false, b3=false, // clique do bt do meio NAO DETECTAVEL[mentira, já funciona]
			moveu=false, clicou=false, clicouu=false, clicouuu=false, rodou=false,
			hidden=false; // escondido?
		boolean	// repare a diferenca:
			arrastando=false, // CONTINUO
			arrastandoo=false, // botao dir
			arrastandooo=false, // botao da roda
			arrastou=false, // REPENTINO
			arrastouu=false, // botao dir
			arrastouuu=false; // botao da roda
		boolean opImpArea=false;			
		int xArr=-1, yArr=-1; // POS INI DO ARRASTO
		int x=J.xPonto, y=J.yPonto, x_=x, y_=y, dx=0, dy=0, dr=0;

//  p debug	
//	AudioClip wSom = J.carrSom("bum03.wav");

	public Mouse(JFrame f){
		f.addMouseListener(this); // clicar, soltar, entrar e sair
		f.addMouseMotionListener(this); // mover e arrastar 
		f.addMouseWheelListener(this);	// p roda			
		frameDoCursor = f; // util p customizacao do cursor
	}
		static int xCal=-8, yCal=-12; // calibragem fina
	public boolean naArea(float i, float j, float ii, float jj){
		return naArea((int)i, (int)j, (int)ii, (int)jj);
	}
	public boolean naArea(int i, int j, int ii, int jj){
		// coordenadas absolutas, não são relativas como "fillRect"
		int w=0;
		if(i>ii){w=i; i=ii; ii=w;}
		if(i>ii){w=j; j=jj; jj=w;}
		if(opImpArea) // bom p debug
			J.impRet(0,10, i,j,ii,jj);		
		if(J.noInt(x,i+xCal,ii+xCal))
		if(J.noInt(y,j+yCal,jj+yCal))
			return true;
		return false;
		//return (x>=i+xCal) && (x<=ii+xCal) && (y>=j+yCal) && (y<=jj+yCal);
	}

	public boolean naAreaRel(float i, float j, float ii, float jj){
		return naAreaRel((int)i, (int)j, (int)ii, (int)jj);
	}
	public boolean naAreaRel(int i, int j, int ii, int jj){
		// esse eh relativo
		ii+=i;
		jj+=j;
		if(opImpArea) // bom p debug
			J.impRet(0,10, i,j,ii,jj);
		return (x>=i+xCal) && (x<=ii+xCal) && (y>=j+yCal) && (y<=jj+yCal);
	}	

	// move mouse p meio de tela. Bom p joeCraft. 
	// Cuidado! o catch abaixo consome clock
		boolean foiProMeio=false;
	public void proMeio(){
		try { 
			Robot rob = new Robot();
			rob.mouseMove(J.maxXf>>1,J.maxYf>>1); 
			foiProMeio=true;// eureca!!! Bom p joe3d. 
		} catch(Exception e){
			J.impErr("!Robo^ do mouse falhou","Mouse.proMeio()");
		}		
	}
	public void move(int i, int j){
		try { 
			if(i==-1) i=x;
			if(j==-1) j=y;
			
			Robot rob = new Robot();
			rob.mouseMove(i+7,j+35); // 7 e 35 são p calibragem fina dentro do Linux
		} catch(Exception e){
			J.impErr("Robo^ do mouse falhou","Mouse.move()");
		}		
	}	

	public boolean doubleClick(){		// clique duplo com esq
		if(b1)
		if(J.noInt(cClick,dClick0,dClick9))
			return true;
		return false;
	}	
	public boolean doubleClickk(){		// clique duplo com dir
		if(b2)
		if(J.noInt(cClick,dClick0,dClick9))
			return true;
		return false;
	}		
	
	// deve ser inserido no fim do loop principal de cada programa.
	// zera os flags de evento do mouse
	public void zMouse(){
		cClick++; // gambiarra q funciona. Acho q essa classe precisa ser toda reescrita.
		if(b1 || b2 || b3) cClick=0;
		
		{ // contando tempo desde o ultimo clique
			// eu sei q isso nao zera nada, mas eh q este bloco age como um "reg()". Jah q nao existe um "ms.reg()" p colocar no loop principal dos outros fontes, coube bem aqui.
		
			if(cLastClick!=-1) cLastClick++; 
			if(cLastClickk!=-1) cLastClickk++; 			
			if(cLastClickkk!=-1) cLastClickkk++; 			
			
			// removendo o bug de quando fica muuuuuuitooooo tempo sem clicar: o contador estoura e retorna a -1 automaticamente num dado momento.
			if(cLastClick<0) if(cLastClick!=-1) cLastClick=100000; // adeus bug
			if(cLastClickk<0) if(cLastClickk!=-1) cLastClickk=100000; 			
			if(cLastClickkk<0) if(cLastClickkk!=-1) cLastClickkk=100000; 			
		}
		
		if(b1)
			if((xArr!=x) || (yArr!=y)) 
				arrastando=true; // CONTINUO
		if(b2)
			if((xArr!=x) || (yArr!=y)) 
				arrastandoo=true; // CONTINUO			
		if(b3)
			if((xArr!=x) || (yArr!=y)) 
				arrastandooo=true; // CONTINUO							
		foiProMeio=false;		
		arrastou=false; // REPENTINO	
		arrastouu=false; 
		arrastouuu=false; 
		dx=0; dy=0; 
		dr=0; 
		moveu=false;
		rodou=false;
		clicou=false; // pq nao existe um "clicando"
		// abaixo nao, assim da p ver o drag
		// b1 e b2
	}	

		float sens=300f;// sensibilidade 3d
	public void regMouse3D(){		

		float w = -dx;
		float ww = -dy;
		
		if(!foiProMeio)
			J.rotCam(ww/sens,w/sens,0);
		
		int q=200;
		zMouse(); // isso precisa ser aqui!;
		int i = J.maxXf>>1;
		int j = J.maxYf>>1;
		if(!naArea(
			i-q, j-q,
			i+q, j+q)) 
				proMeio();
	}		
	
		String camCur = "cursor00.png";
		String camCurNulo = "cursornulo.png";
	public void setCursor(String cam){
		cam = J.emMinusc(cam);
		setCur(cam);
	}
	public void setCur(String cam){
		cam = J.emMinusc(cam);
		try{
			if(!cam.equals(camCurNulo)) {camCur=cam; hidden=false; }
			if(cam==null) cam=camCurNulo;
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			Image image = J.carrFig(cam);
			Point hotspot = new Point(0, 0);
			Cursor cursor = toolkit.createCustomCursor(image, hotspot, "irrelevante");
			frameDoCursor.setCursor(cursor);
		} catch (Exception e){
			J.impErr("falha customizando o cursor: "+cam,"Mouse.java:setCur()");
			e.printStackTrace(); 
		}		
	}
	public void hide(){ 
		setCursor(camCurNulo);
		hidden=true;
		// tecnica simples
	}
	public void show(){
		setCursor(camCur);
		hidden=false;
	}	
	
	
	// nao use abaixo, 
	// prefira "clicou","rodou","x","y", b1 e b2. Veja instruções.
	public void mousePressed(MouseEvent e){//1111111111
		
		if (e.getButton()==1) {b1=true; clicou=true; cLastClick=0; }
		if (e.getButton()==3) {b2=true; clicouu=true; cLastClickk=0; }// eh assim mesmo, com este "3" estranho
		if (e.getButton()==2) {b3=true; clicouuu=true; cLastClickkk=0; }
		
		xArr = x;
		yArr = y;
		arrastou=false;
		arrastouu=false;
		// O botao do meio nao esta sendo detectado. Tente:
		// J.esc("->"+e.getButton());
	}
	public void mouseReleased(MouseEvent e){//22222222222
		if (e.getButton()==1) b1=false;
		if (e.getButton()==3) b2=false; // eh assim mesmo	
		if (e.getButton()==2) b3=false; // botao da roda do mouse
		if(arrastando)arrastou=true;
		if(arrastandoo)arrastouu=true;
		if(arrastandooo)arrastouuu=true;
		arrastando=false;		
		arrastandoo=false;		
		arrastandooo=false;		
		clicou=false;
		clicouu=false;
		clicouuu=false;
	}
	
	public void mouseMoved(MouseEvent e){
		//wSom.play();
		sinc(e);
	}
	public void mouseWheelMoved(MouseWheelEvent e){ 
		dr= e.getWheelRotation();
		rodou=true;
	}
	
/*wwwwwwwwww
  public void mouseWheelMoved(MouseWheelEvent e) {
        int button = e.getButton();
        if (button == MouseEvent.BUTTON2) {
            System.out.println("Middle mouse button scrolled");
        }
    }
*/	
	
	// interno; Nao use isso
	private void sinc(MouseEvent e){	
		x = e.getX()+corrX;
		y = e.getY()+corrY;
		dx= x_-x;
		dy= y_-y;
		x_=x;
		y_=y;
		if (dx!=0 || dy!=0) moveu=true;	
	}	
	
	// nao use abaixo, eh só p nao dar erro
	public void mouseEntered(MouseEvent e){
	}
	public void mouseExited(MouseEvent e){
	}
	public void mouseClicked(MouseEvent e){
	}
	public void mouseDragged(MouseEvent e){
		sinc(e);
	}


}

