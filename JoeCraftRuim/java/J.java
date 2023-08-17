// Apenas uns 10% de toda esta classe é utilizada neste projeto. Tem muito lixo aqui. Ignore.


import java.awt.Robot;
import java.awt.AWTException;

// p capturar a tela do JFrame
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;

// p acessar clipboard do windows
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;

// p pegar a duracao de wavs
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioFormat;

// p formatar floats
import java.text.DecimalFormat;

import java.net.URL;
//import java.util.Collections.reverse(ret);
import java.util.Collections;

// p copiar arquivos
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

// p strings em arquivos ASCII
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;// p UTF-8

// p mudar o tipo da fonte
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.io.File;
import javax.swing.JPanel; 

// p dirOn e similares
import java.awt.event.KeyEvent;

/* EH BOM SABER: (sobre AWT e SWING)
		O pacote AWT (Abstract Window Toolkit) deixa a cargo do sistema operacional o desenho de janelas. Assim, uma janela feita em java para MS-Windows teria a aparencia tipica do windows. O problema eh q p SOs diferentes, a aparencia mudava ligeiramente, sendo isso suficiente para badernar a interface. O Pacote swing apareceu p corrigir isso e estabeleceu um padrao, assim, tanto no Linux quanto no Windows, uma janela feita pelo swing teria a mesma aparencia e posicionamento de elementos.
*/


// geral
import javax.swing.JOptionPane;
import java.util.Scanner;
import java.awt.RenderingHints;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Color;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.applet.Applet;
import java.applet.AudioClip;	

import java.util.ArrayList;
import java.util.Iterator;
import java.io.RandomAccessFile;
import java.io.IOException;



public class J{
  static Random r = new Random();  
	static Color cor[];

	
	public static int iniRes2(){
		// 800x600 t? caindo em desuso
		J.maxXf=1366; 
		J.maxYf=768-52;
		J.xPonto = (int)(J.maxXf/2f);
		J.yPonto = (int)(J.maxYf/2f);
		return 0; // truque
	}
	
	//static int maxXf=800, maxYf=560+J.iniPlt();	
	//static int maxXf2=1370, maxYf2=716;	// usei no editorJf2
	static int maxXf=1366, maxYf=768+J.iniPlt();	 // no linux
	static int xPonto = (int)(maxXf/2f); // meio da janela
	static int yPonto = (int)(maxYf/2f); // deve ser corrigido se acontecer um resize
	static int cont=0;
	static String stRet="???"; // gambiarras com caixas de dialogo
	static Graphics g = null;
	static boolean opDebug = false;

// === DELTA E BHASKARA ===============================
// tag bascara bhascara baskhara

	public static float delta(float a, float b, float c){
		/*
		
y = ax²+bx+c

"²" indica curva("função quadrática" ou "funcao de 2o grau"), nao uma reta 
"b>0" indica q a curva sobe, depois de cruzar o eixo vertical
"b<0" desce e 
"b=0" seria o ápice/fundo exatamente sobre este eixo
"a>0", então a curva é em "u", senão, é em "n"
"c" é a altura na ordenada onde a curva toca este eixo vertical
raizes são os 2 pontos onde a curva toca o eixo X horizontal = abscissas (o vertical é ordenada)
p equacao incompleta, isolar o x ou colocar o x em evidencia

Delta=0: apenas uma raiz será encontrada (ou duas identicas de mesmissimo sinal)
Delta>0: 2 raizes serão encontradas
Delta<0: não existem raizes, não tem solucao		
		
		
a=9 b=-12 c=4 -> Delta=0 -> Bhaskara=2/3 = 0.66666...		
		
		*/
		return (b*b)-4*a*c;
	}
	public static float bhaskaraMais(float a, float b, float c, float D){
		// "c" p nao confundir
		// se retornar NaN, é pq Delta é negativo, logo, não tem solução
		return (-b+(float)Math.sqrt(D))/(2*a);
	}
	public static float bhaskaraMenos(float a, float b, float c, float D){
		// "c" p nao confundir
		// se retornar NaN, é pq Delta é negativo, logo, não tem solução		
		return (-b-(float)Math.sqrt(D))/(2*a);
	}		
	public static float bhaskaraMais(float a, float b, float c){		
		// se retornar NaN, é pq Delta é negativo, logo, não tem solução		
		float D = delta(a,b,c);
		return (-b+(float)Math.sqrt(D))/(2*a);
	}	
	public static float bhaskaraMenos(float a, float b, float c){
		// se retornar NaN, é pq Delta é negativo, logo, não tem solução		
		float D = delta(a,b,c);
		return (-b-(float)Math.sqrt(D))/(2*a);
	}		


// === FALANDO ========================================
	public static int getPosChar(char ca, String st){
		// retorna a posicao do caracter ca dentro do string st
		// retorna -1 se nao encontrou
		for(int q=0; q<st.length(); q++)
			if(st.charAt(q)==ca)
				return q;
		return -1;	
	}
	public static String trocaCharSt(char ca, String ins, String st){ 
		// mais intuitivo
		// leia-se: trocar o char "ca" por "ins" no string st
		return trocaCharSt(ins, st,ca);
	}
	public static String trocaCharSt(String ins, String st, char ca){ // trocaTexto trochaChar
		// Leia-se: insira o trecho "ins" no string "st" quando aparecer "ca"
		int pos = getPosChar(ca,st);
		if(pos==-1) return st; // nao encontrou
		return insText(ins,st,pos);
	}
	public static String trocaTrecho(String ant, String nov, String st){
		/* Veja q ? possivel remover um trecho do string com o 2o parametro = ""	
					String st = "DEGRAU_AREIA";
					st = J.trocaTrecho("DEGRAU_","",st); // substituir por NADA
					J.esc(st); // saida: "AREIA";			
		*/
		
		st = st.replaceAll(ant,nov);
		return st;
	}
	
	public static void falar2(String p, int nv, int sp, int pt){ // tag: speak, fala, diga
		// nv = numero da voz: 1, 3 ou 4 segundo a instalação atual
		// sp = de "speed", numero de palavras por minuto, 160 é o padrao
		// pt = pitch, padrao 50
		// é só deixar em -1 ficar padrão
		if(nv==-1) nv=4;
		if(sp==-1) sp=160;
		if(pt==-1) pt = 50;
		String pp = J.trocaChar(' ','_',p);
		String st="espeak -vbrazil-mbrola-"+nv+" "+pp+" -p "+pt+" -s "+sp;		
		J.exec(st);
		J.esc("FALANDO2: "+p);
	}
	public static void falar(String p){ // tag: speak, fala, diga
			// VEJA O BLOCO COMENTADO MAIS ABAIXO PARA MAIS OPÇÕES
			/* O QUE DÁ P FAZER PELO TERMINAL DO LINUX COM SINTETIZAÇÃO DE VOZ:
					- salvar o som num arquivo wav 
					- ler o conteudo de um ascii
					- alterar o tipo de voz. No momento tem a 1, 3 e 4
					- alterar o pitch
					- alterar a velocidade de leitura
			*/
			String st="espeak -vbrazil-mbrola-4 "; // seria "M BR olá"; 4 seria uma das vozes disponíveis.
			String pp = J.trocaChar(' ','_',p); // senão buga
			//st+=(char)39; // aspas
			//st+=(char)'\"';			// não tô conseguindo colocar aspas. Vou improvisar.
			st+=pp;
			//st+=(char)'\"';
			st+=" -p 60 -s 100"; // "p" para pitch(padrao 50) e "s" p speed(padrao 160 palavras por minuto)
			J.exec(st);
			//J.esc(st);
			J.esc("FALANDO: "+p); // Parece ser bom p debug, mas tinha q ter uma opcao aqui.
/* INSTALANDO UM SINTETIZADOR DE VOZ PARA O LINUX: [[Achei bom incluir isto aqui]]
https://www.youtube.com/watch?v=pN8olCFZ4ZU&t=14s
https://www.vivaolinux.com.br/artigo/Fazendo-seu-Linux-falar-com-espeak

sudo apt install espeak
sudo apt search mbrola-br*

		listando as vozes disponiveis p brazil. "M" e "F" são masculino e feminino
espeak --voices=mbrola | grep -i brazil
 7  pt             M  brazil-mbrola-1      mb/mb-br1     
 7  pt             M  brazil-mbrola-3      mb/mb-br3     
 7  pt             F  brazil-mbrola-4      mb/mb-br4 
	
	falando com uma voz especifica instalada e alterando a velocidade
espeak -vbrazil-mbrola-4 "este é um teste" -s 30

	agora alterando o pitch "p", cujo padrao é 50, e speed "s", cujo padrão é 160 palavras por minuto.
espeak -vbrazil-mbrola-4 "este é um teste" -p 90 -s 30
	
	agora com a outra voz
espeak -vbrazil-mbrola-1 "este é um teste" -p 90 -s 30

	salvando num arquivo wav
espeak -vbrazil-mbrola-4 "este é um teste" -p 90 -s 90 -w deletar.wav
	
	lendo o texto de um ascii e jogando p um wav sintetizado
espeak -vbrazil-mbrola-4 -f txt.txt -p 90 -s 90 -w deletar.wav
.............
COMANDOS P "espeak"

"Manual" do espeak
	man espeak
	
Lista todas as vozes disponiveis
	espeak --voices

Peguei uma das vozes disponiveis "pt-br" e coloquei no parametro "-v" p especificar qual voz eu queria. Serve p qq voz instalada disponivel. Usei "grep" p listar apenas a que eu queria. Veja q copiei do terminal.
		 haim  ~  JJJ  java  espeak --voices | grep brazil
		5  pt-br          M  brazil               pt            (pt 5)
		 haim  ~  JJJ  java  espeak -vpt-br "isto é um teste"
		 haim  ~  JJJ  java  
	

*/			
	}
	
	public static void falar_(String st){ // tag: speak, fala, diga
		// ESTE É P WINDOWS, use o outro metodo "falar()" p linux acima.
		// na gambiarra, mas consegui!!!
			//st = trocaCharSt("ss",st,'?'); // Leia-se: insira o trecho "ss" no string "st" quando aparecer '?'
			// esqueci pq coloquei este comando acima... 
			// seria bom se pudesse mudar a linguagem tb. Procurei um pouco mas t? meio dificil. Depois.
			J.esc("FALANDO: "+st); // Parece ser bom p debug, mas tinha q ter uma opcao aqui.
			ArrayList<String> lis = new ArrayList<>();
			lis.add("CreateObject(\"sapi.spvoice\").Speak \""+st+"\"");
			J.saveStrings(lis,"deletar.vbs");
			J.exec("wScript.exe c://java//deletar.vbs");
			// fica rastro, mas acho q nao tem problema...
	}

// === SENDKEYS ========================================
		static Robot robot;
	public void sendKeys(String p){
		/*	SOBRE
			D? p expandir isto p muita coisa, mas est? restrito apenas a janela do fonte, ou seja, n?o d? p escrever alguma coisa do chrome nem em alguma caixa de di?logo do windows, d? apenas p mandar teclas p janela do meu proprio fonte. J? ? alguma coisa.
			O que daria p fazer:
					- um Sued
					- macros de tecla p algum fonte (jogo, digitador ou qq) especifico
					- um logador p sites chatinhos... n?o d? n?o pq s? funciona dentro da janela do programa. Talvez ainda d? p contornar isto. ?J? pensou em fazer o pr?prio browser???
		*/
		/* FALTA:
					- teclas especiais
					- pressionar agora e soltar somente depois de pressionar outras teclas
					- deixar pressionada a tecla tal por X milisegundos
		*/
		/* LEMBRETES:
            robot.keyPress(KeyEvent.VK_ALT);		
            robot.keyRelease(KeyEvent.VK_ALT);		
		*/
		p = J.emMinusc(p);
		char ca = 0;
		int k = 0;
		try{
			robot = new Robot();
			//robot.setAutoDelay(200); // tempo antes de iniciar a digita??o, mas n?o est? entre um char e outro, logo, n?o ajuda tanto assim
			for(int q=0; q<p.length(); q++){
				ca = p.charAt(q);
				k = 0;
				switch(ca){
					// T? FALTANDO SINAIS
					case '0': k = KeyEvent.VK_0; break;
					case '1': k = KeyEvent.VK_1; break;
					case '2': k = KeyEvent.VK_2; break;
					case '3': k = KeyEvent.VK_3; break;
					case '4': k = KeyEvent.VK_4; break;
					case '5': k = KeyEvent.VK_5; break;
					case '6': k = KeyEvent.VK_6; break;
					case '7': k = KeyEvent.VK_7; break;
					case '8': k = KeyEvent.VK_8; break;
					case '9': k = KeyEvent.VK_9; break;
					
					case 'a': k = KeyEvent.VK_A; break;
					case 'b': k = KeyEvent.VK_B; break;
					case 'c': k = KeyEvent.VK_C; break;
					case 'd': k = KeyEvent.VK_D; break;
					case 'e': k = KeyEvent.VK_E; break;
					case 'f': k = KeyEvent.VK_F; break;
					case 'g': k = KeyEvent.VK_G; break;
					case 'h': k = KeyEvent.VK_H; break;
					case 'i': k = KeyEvent.VK_I; break;				
					case 'j': k = KeyEvent.VK_J; break;
					case 'k': k = KeyEvent.VK_K; break;
					case 'l': k = KeyEvent.VK_L; break;
					case 'm': k = KeyEvent.VK_M; break;
					case 'n': k = KeyEvent.VK_N; break;
					case 'o': k = KeyEvent.VK_O; break;
					case 'p': k = KeyEvent.VK_P; break;
					case 'q': k = KeyEvent.VK_Q; break;
					case 'r': k = KeyEvent.VK_R; break;
					case 's': k = KeyEvent.VK_S; break;
					case 't': k = KeyEvent.VK_T; break;
					case 'u': k = KeyEvent.VK_U; break;
					case 'v': k = KeyEvent.VK_V; break;
					case 'w': k = KeyEvent.VK_W; break;
					case 'x': k = KeyEvent.VK_X; break;
					case 'y': k = KeyEvent.VK_Y; break;
					case 'z': k = KeyEvent.VK_Z; break;
				}
				if(k==0) J.impErr("!caracter n?o mapeado: ["+(char)ca+"]","sendKeys()");
				robot.keyPress(k);
				robot.keyRelease(k); // teclas modificadoras???
			}
		} catch (AWTException ex) {
        ex.printStackTrace();
				J.impErr("!erro em sendKeys()","sendKeys()");
		}
	}

// === ANGULOS =========================================
	public static class Ang{
			float catO=0f, catA=0f, hip=0f, cx=0f, cy=0f;
		public Ang(float i, float j, float ii, float jj){
			// cateto oposto
			catO = J.maior(i,ii)-J.menor(i,ii);			
			
			// cat adjacente
			catA = J.maior(j,jj)-J.menor(j,jj);
			
			// hipotenuza
			hip = (float)(Math.sqrt(catO*catO+catA*catA));			
			
			// coeficientes do cat oposto e adjacente
			cx = catO/hip; // sin, "sem o angulo"
			cy = catA/hip; // cos		"com o angulo"   
			//tang = catOp/catAdj
			
			// corrigir sinais dos coefs
			if(i>ii) cx=-cx;
			if(j>jj) cy=-cy;
			
			// depois eh soh fazer:
			//tiro.vx = velTir*cx;
			//tiro.vy = velTir*cy;			
		}
	}
		// cco = "componente do cateto oposto", cca = "adjacente"
		// depois eh soh fazer:
		// tiro.vx = velTir*cco;
		// tiro.vy = velTir*cca;		
		static float cco=0f, cca=0f, hip=0f, catA=0f, catO=0f;
	public static float getAng(float i, float j, float ii, float jj){
		// i:j seria o centro, ii:jj seria o ponto q orbita este centro
		// ang0 começa p cima, e, em sentido horário, vai p dir até dar a volta = 360;
		// arco-tangente para retornar o angulo sabendo apenas do cateto oposto e do adjacente
		// mais aq: https://youtu.be/BGIfpExpKSg?t=675
		float co = i-ii;
		float ca = j-jj;
		// 180/Math.PI p converter de radianos em graus
		float v = (float)(Math.atan(co/ca)*180/Math.PI); 
				 if(ii>=i && jj<=j) v=-v; // CD
		else if(ii>=i && jj>j) v=90+90-v; // BD
		else if(ii<i && jj>j) v=-v+180;
		else if(ii<i && jj<=j) v=270+90-v;
		else v=-9999999f;
		//if(v<0) sai("ERRO DE ANGULO"); // mas acho q nunca chegará aq... até chegou uma vez.
		if(v<0) v=0; // ?Sem bug???
		return v;
	}		
	public static void calcAng(float i, float j, float ii, float jj){ // por compatibilidade
		calcCCO(i,j,ii,jj);
	}
	public static void calcCCO(float an, float i, float j){ 
		/*
			A CIRCUNFERENCIA EM 4 FATIAS DE PIZZA:
				val+=(float)(ms.dr*(Math.PI*2)/ 4 );
			CORTANDO CADA FATIA NO MEIO:	
				val+=(float)(ms.dr*(Math.PI*2)/ 4/2 );			
			AGORA A METADE DA CADA METADE:
				val+=(float)(ms.dr*(Math.PI*2)/ 4/2/2 );
			
			0 -> 0
			PI -> 180
		*/
		//an = prop(an, (float)(Math.PI*2f), 360f);
		// ainda nao tah perfeito, mas jah eh alguma coisa
		calcCCO(i,j, i+(float)Math.sin(an), j-(float)Math.cos(an)); // ???
	}
	public static void calcCCO(float i, float j, float ii, float jj){ 
		// cateto oposto
		catO = J.maior(i,ii)-J.menor(i,ii);
		
		// cat adjacente
		catA = J.maior(j,jj)-J.menor(j,jj);
		
		// hipotenuza
		hip = (float)(Math.sqrt(catO*catO+catA*catA));
		
		// coeficientes do cat oposto e adjacente
		cco = catO/hip;
		cca = catA/hip;			
		
		// corrigir sinais dos coefs
		if(i>ii) cco=-cco;
		if(j>jj) cca=-cca;				
		
		// depois eh soh fazer:
		//tiro.vx = velTir*cco;
		//tiro.vy = velTir*cca;
		
		// saiba disso:
		// X = x*cos-y*sin
		// Y = x*sin+y*cos
			
/* SAIBA DISTO TB
JOE ANDANDO NO ESPAÇO 3D:
			float q=0.01f;
			if(J.bxoOn) {
				xCam+= q*Math.sin(angY); 
				zCam+= q*Math.cos(angY);
			}
			if(J.cmaOn) {
				xCam-= q*Math.sin(angY); 
				zCam-= q*Math.cos(angY);
			}			
			
			if(J.esqOn) {
				xCam+= q*Math.cos(angY); 
				zCam-= q*Math.sin(angY);
			}
			if(J.dirOn) {
				xCam-= q*Math.cos(angY); 
				zCam+= q*Math.sin(angY);
			}

*/			
	}
	public static float desXang(float ang, float vet){
		// desloca a coordenada X usando um angulo e um vetor (p tamanho)
		// parece q deu certo, mas eh melhor testar mais.
		return (float)(vet*Math.cos(ang));
	}
	public static float desYang(float ang, float vet){
		return (float)(vet*Math.sin(ang));
	}	

// === PROPORCOES ======================================
	public static float prop(float v, float max, float maxx){
		// dentro da faixa 0..max, existe o valor v; Na escala 0..maxx, onde este v estaria?
		if(max==0) return 0;
		if(maxx==0) return 0;
		
		float cf = v/max;
		return maxx*cf;
	}
	public static float prop(float v, float min, float max, float minn, float maxx){
		// dentro da faixa min..max, existe o valor v. Em que posicao proporcional na faixa minn..maxx este v estaria?
		v-=min;
		max-=min;
		maxx-=minn;
		return minn+prop(v,max,maxx);
	}

	public static int pulsar(int v, int max){  // oscilador pulsador
		// bom para oscilacoes
		// retorna sempre um numero entre 0..max, incluidos
		// exemplo: 
		//    ddx = J.pulsar(J.cont>>5, 10);
		//    aqui o valor chegarah gradualmente no maximo 10, na velocidade de J.cont distorcido e voltarah gradualmente ateh zero. Retornarah entao ao movimento.
		//    este ">>5" define a velocidade

		int meio=max;
		max = max<<1;
		int q=v%max;
		if(q>meio) q=max-q;
		return q;
	}
	public static int pulsar(float v, float max){ 
		return pulsar((int)v, (int)max);
	}
	public static int pulsar(int v, int min, int max){
		// 0..10
		if(max>min) 
			return pulsar(v-min, max-min)+min;
		else 
			return pulsar(v-max, min-max)+max;
	}

	public static float passos(float min, float max, int div, int pas){
		// min..max: ? o intervalo q se vai dividir
		// div: numero de partes q este intervalo vai ser dividido (nao pode ser zero)
		// pas: o passo em questao, que ateh pode extrapolar div (seria "tendencia")
		// eh boa ferramenta e talvez ajude p fazer algum degrad?
		float ps = J.maior(min,max) - J.menor(min,max);
		ps = (float)(ps / div);
		return ps*pas;
	}
// =====================================
	public static String ArrLstStrEmStr(ArrayList<String> lis){
		// converte um arrayList de Strings em um String único com #10 #13 inseridos
		// usei p copiar o conteudo de uma lista de JWin p clipboard
		if(lis==null) return "";
		if(lis.size()<=0) return "";
		String st = "";
		for(String s:lis)
			st+=s+(char)10; // estranho não precisar de 13 aqui, mas funciona.
			//st+=s+(char)10+(char)13;
		st = J.remUltChar(st);
		return st;	
	}
// === ACESSANDO O CLIPBOARD DO SISTEMA

	public static String getTxtClipBoard(){ // getTextWinClipboard() getClipBoard() getStClipBoard
		// pq java jah nao disponibilizou um metodo simples desse??? Tudo em java parece ser mais complicado.
		try{
				// Create a Clipboard object using getSystemClipboard() method
				Clipboard c=Toolkit.getDefaultToolkit().getSystemClipboard();
				// Get data stored in the clipboard that is in the form of a string (text)
				return (String) c.getData(DataFlavor.stringFlavor);
				
				// tem tb estes:
				//   imageFlavor
				//   javaFileListFlavor
				// tb seria interessante ver como colococar uma informacao do clipboard. 
				// Simples ctrl+c n?o funciona em JWin.java
			} catch(Exception e){
				// nem importa isso...
			}		
			return "???";
	}
	public static void setTxtClipBoard(String st){ 
		StringSelection data = new StringSelection(st);
		Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
		cb.setContents(data, data);
		// q pesadelo eh programar java... precisei pesquisar muito p fazer um negocio simples.
	}
	
	
	public static void setFigClipBoard(BufferedImage f){ 
		// depois
		J.impErr("!J.setFigClipBoard() ainda n?o foi implementado");
		//StringSelection data = new StringSelection(st);
		//Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
		//cb.setContents(data, data);		
	}
	public static BufferedImage getFigClipBoard(){ 
	// ?Image ou BufferedImage aqui???
		try{
				// Create a Clipboard object using getSystemClipboard() method
				Clipboard c=Toolkit.getDefaultToolkit().getSystemClipboard();
				// Get data stored in the clipboard that is in the form of a string (text)
				return (BufferedImage) c.getData(DataFlavor.imageFlavor);
			} catch(Exception e){
				// nem importa isso...
			}		
			return null;
	}

// === EXECUTANDO ARQUIVOS ============================
	public static void criaArqBatJava(String cam, String par){
		// NAO FUNCIONARÁ PQ NÃO ESTÁ MAIS NO WINDOWS
		// gera um bat de compilacao de arq java
		// usei no editor jf1, mas resolvi deixar aqui disponivel p alguma gambiarra
		par = J.corrCam(par,"java");
		
		// par = J.primMaiusc(par);
		
		ArrayList<String> lis = new ArrayList<>();
		lis.add("c:");
		lis.add("cd/");
		lis.add("cd java");
		lis.add("title "+par); // soh p prompt ficar com o titulo
// javac Fonte013.java
		lis.add("javac "+par);
// java Fonte013
		lis.add("java "+J.nomeArq(par));
// pause
		lis.add("pause");
// del *.class
		lis.add("del *.class");
		lis.add("exit");
		J.saveStrings(lis, cam);
	}	
	public static void runJava(String com){
		com = "cmd.exe /c c://java//runJava.bat "+com;
		esc(com);
		exec(com);
	}
	public static void exec(String com){

		/* EXPLICAÇÃO PARA O WINDOWS:
// aciona um arquivo bat, um exe dentro do windows ou ateh um txt diretamente, como se fosse um clique duplo sobre o arq
		// NAO USAR BARRAS DUPLAS NO CAMINHO... mentira. Assim tb funciona:
		// J.exec("c:\\java\\numerologia.txt");		
		// com barras subindo nao funciona, tem q usar barras duplas descendo
		
		// NO WIN 10 EH ASSIM
		// J.exec("cmd /c c:/java/EditorJf1.bat C:/java/jf1/apagar01.jf1");
		// "cmd /?" diz: "/c encerra, /k permanece"
		
		// NO WIN XP ERA ASSIM:
		// exemplo1: cam="c:/pasta/outraPasta/bat.bat"
		// exemplo2: cam="outraPasta/bat.bat", toma por base o diretorio atual		
		// exemplo3: cam="c:/txt.txt"
		// exemplo4: cam="D:/Documents and Settings/Haim/Desktop/info.txt"		
		*/
		
		try {
			// abaixo já funciona bem no linux
			Runtime.getRuntime().exec(com); 
			// ABAIXO ERA NO WINDOWS
			//Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL "+com); 
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}	
// === GERANDO PROXIMO ARQ FONTE DA SEQUENCIA ================
	boolean onLinux=true;// !!!!!!!!!!!!!!
	
	public static String verProxFonte0000(){
		// seria bom ajustar um metodo deste tipo p qq sequencia de nomes com numeracao
		String cm="";
		int q=0;
		for(q=1001; q<2000; q++){ // gambiarra mas funciona
			cm = "Fonte"+J.intEmSt0000(q)+".java";
			J.esc(cm);
			if(!J.arqExist(cm)) break;
		}
		if(q==2000) J.impErr("!Estouro de sequencia[0..2000]: "+cm);
		return cm;
	}
	
	public static String verProxFonte000_(){
		// ESTE ERA DO WINDOWS
		// seria bom ajustar um metodo deste tipo p qq sequencia de nomes com numeracao
		String cm="";
		int q=0;
		for(q=1; q<1000; q++){
			cm = "c:/java/Fonte"+J.intEmSt000(q)+".java";
			J.esc(cm);
			if(!J.arqExist(cm)) break;
		}
		if(q==1000) J.impErr("!Estouro de sequencia[0..1000]: "+cm);
		return cm;
	}
	public static String gerarProxFonte0000(){ // no linux
		// deduz o proximo arquivo da sequencia "Fonte####.java"
		// cria um fonte limpo com este proximo nome
		String cam = verProxFonte0000();
		String nome = J.nomeArq(cam); // sem extencao
		
		J.esc("O próximo arquivo seria: |"+nome+"|");
		
		// carregando arq modelo
		ArrayList<String> fnt = J.openStrings("MODELO04.TXT");		
		ArrayList<String> nov = new ArrayList<>();
		
		// substituindo trechos do nome
		String s = "";
		for(int q=0; q<fnt.size(); q++){
			s = fnt.get(q);
			if(J.tem("Fonte000",s)) {
				//J.esc("->"+s);
				s = s.replace("Fonte000",nome);
				//J.esc("=>"+s);
			}
			if(q==0) s = "// Fonte limpo";
			nov.add(s);
		}
		
		J.saveStrings(nov, nome+".java");
		return cam;
	}
	
// === TRABALHANDO COM NANOTIME ====================	
		static int contt=0;
		static Long tempoAnterior=nanoTime(), intervaloPadrao=1000000L;
	public static void regNanoTime(){
		// nao sei se tah resolvendo
		contt++;
		if(J.nanoTime()-J.tempoAnterior>intervaloPadrao){
			J.cont++;
			J.contt=0;
			J.tempoAnterior = J.nanoTime();
		}		
	}
	public static boolean CC(int v){
		if(contt==0)	
		if(cont%v==0) // divisoes p depois
			return true;
		return false;
	}	

	public static Long nanoTime(){
		//return System.nanoTime()/100000;
		return System.nanoTime();
		// use tambem "Long.toString();"
		/*
			esse nanoTime eh dado em nanosegundo, ou seja: eh preciso 1 bilhao de nanosegundos p fazer um segundo:
			1.000.000.000
		*/
	}
	public static Long getNanoTime(){
		// mais intuitivo
		return System.nanoTime();
		/*
			return System.nanoTime()/100000;
			use tambem "Long.toString();"
			esse nanoTime eh dado em nanosegundo, ou seja: eh preciso 1 bilhao de nanosegundos p fazer um segundo:
			1.000.000.000
		*/		
	}	

// === TECLAS ======================================================
/* MACETE, COMO DETECTAR O SOLTAR DE TECLAS:

	public void regSel(){
		// desenha um retangulo enquanto shift estiver pressionado, desde onde pressionou até a posicao atual do mouse.
		if(!J.shiftOn) if(xRet!=0){ // soltou o shift. Linha importante.
			//J.sai("soltou");
		}
		if(J.shiftOn){
			if(xRet==0) {
				xRet=cur.X;
				yRet=cur.Y;
			}
			J.impRet(0,9, xRet,yRet,cur.X,cur.Y);
		} else xRet=0;
	}
*/
	static boolean 
		capsLigado=Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK),
		// acima: https://stackoverflow.com/questions/7435221/how-can-i-get-the-caps-lock-state-and-set-it-to-on-if-it-isnt-already
		dirOn=false, esqOn=false, cmaOn=false, bxoOn=false, 
		enterOn=false, spaceOn=false, backSpaceOn=false, 
		zeroOn=false, //apostOn=false,
		umOn=false, //apostOn=false,
		doisOn=false, //apostOn=false,
		tresOn=false, //apostOn=false,
		pageUpOn=false, pageDownOn=false, 
		ctrlOn=false, shiftOn=false, altOn=false,
		homeOn=false, endOn=false,
		maisOn=false, menosOn=false,
		xisOn=false,
		delOn=false, insOn=false;
	public static void regPress(KeyEvent k){
		int cd = k.getKeyCode();
		int ca = k.getKeyChar();
		
		if(k.isControlDown()) ctrlOn=true;				
		if(k.isAltDown()) altOn=true;				
		if(k.isShiftDown()) shiftOn=true;				
		
		if(cd==k.VK_CAPS_LOCK) capsLigado=!capsLigado; // acho q nao ? assim
		


		if(cd==k.VK_LEFT || cd==k.VK_A) esqOn=true; 
		if(cd==k.VK_RIGHT || cd==k.VK_D) dirOn=true;
		if(cd==k.VK_UP || cd==k.VK_W) cmaOn=true; 
		if(cd==k.VK_DOWN || cd==k.VK_S) bxoOn=true;
	
		if(cd=='=' || cd=='+' || cd==107) maisOn=true; // pega no numerico e no normal
		if(cd=='-' || cd==109) menosOn=true;		
		if(cd==k.VK_HOME) homeOn=true;		
		if(cd==k.VK_END) endOn=true;		
		if(cd==k.VK_PAGE_UP) pageUpOn=true;		
		if(cd==k.VK_PAGE_DOWN) pageDownOn=true;		
			// cuidado q é "ca", nao "cd"
			//if(ca==222) apostOn=true; 
		if(cd==96) zeroOn=true; // ver depois
		if(cd==k.VK_1) umOn=true;
		if(cd==k.VK_2) doisOn=true;
		if(cd==k.VK_3) tresOn=true;		
		if(cd==k.VK_ENTER) enterOn=true;		
		if(cd==k.VK_SPACE) spaceOn=true;		
		if(cd==k.VK_BACK_SPACE) backSpaceOn=true;		
		if(cd==k.VK_DELETE) delOn=true;				
		if(cd==k.VK_X) xisOn=true;				
		if(cd==k.VK_INSERT) insOn=true;				
	}
	public static void regRelease(KeyEvent k){
		int cd = k.getKeyCode();
		int ca = k.getKeyChar();

		ctrlOn=false; // ?tah certo isso???
		shiftOn=false;						
		altOn=false;								

		if(cd==k.VK_LEFT || cd==k.VK_A) esqOn=false;
		if(cd==k.VK_RIGHT || cd==k.VK_D) dirOn=false;
		if(cd==k.VK_UP || cd==k.VK_W) cmaOn=false;
		if(cd==k.VK_DOWN || cd==k.VK_S) bxoOn=false;		
		
		
		if(cd=='=' || cd=='+' || cd==107) maisOn=false; // pega no numerico e no normal
		if(cd=='-' || cd==109) menosOn=false;		
		if(cd==k.VK_HOME) homeOn=false;	
		if(cd==k.VK_END) endOn=false;	
		if(cd==k.VK_PAGE_UP) pageUpOn=false;	
		if(cd==k.VK_PAGE_DOWN) pageDownOn=false;	
			// CUIDADO Q ESTE ABAIXO É "ca", NAO "cd"
			//if(ca==222) apostOn=false;	
		if(cd==96) zeroOn=false;	//97=1, 98=2, etc. Tudo no teclado numerico
		if(cd==k.VK_1) umOn=false;
		if(cd==k.VK_2) doisOn=false;
		if(cd==k.VK_3) tresOn=false;
		if(cd==k.VK_ENTER) enterOn=false;	
		if(cd==k.VK_SPACE) spaceOn=false;	
		if(cd==k.VK_BACK_SPACE) backSpaceOn=false;	
		if(cd==k.VK_X) xisOn=false;				
		if(cd==k.VK_DELETE) delOn=false;				
		if(cd==k.VK_INSERT) insOn=false;				
	}
	public static boolean setaOn(){ // acho q j? resolve
		return J.cmaOn || J.bxoOn || J.esqOn || J.dirOn;
	}	
// =================================================================

	public static String completSt(String st, char ca, int max){ // completarSt()
		// completa o string st com o caracter ca até lenght maximo max
		if(st==null)st="";
		if(st.length()>max) return st;
		for(int q=0; q<max-st.length(); q++)
			st+=""+ca;
		return st;
	}

	public static String repChar(char ca, int num){
		String st="";
		for(int q=0; q<=num; q++)
			st+=ca;
		return st;
	}
	
	public static int R(int p0,int p9){
		// randomizar apenas no intervalo
		// tb serve p numeros negativos
		if(p0>p9) // inverter
			{int aux=p0; p0=p9; p9=aux; }
		return p0+J.R(p9-p0+1);
		// 10,20
		// 10+J.R(20-10+1)
		// 10+J.R(10+1);
		// 10+J.R(11);
		// 10+[0;10]
		// [10;20]
	}
	public static int R(int p){
		r = new Random();
		if(p<0) return -r.nextInt(-p);
		if(p>0) return r.nextInt(p);
		return 0;
	}	
	public static float RR(float p, float pp){
		// RR(0.8f, 1.2f) deve retornar um numero randomico float entre estes dois, incluindo ambos
		int w = 1000000;
		int r = (int)(p*w);
		int rr = (int)(pp*w);		
		return R(r,rr)/w;
	}
	public static int RS(){
		// s? p sinal, claro q nao retorna zero
		if(R(100)>50) return +1;
		return -1;
	}
	public static int RS(int p){
		int v=R(p);		
		if(B(2)) return -v;
		return v;
	}	
	public static int RS(float pp){		
		int p = (int)pp;
		int v=R(p);		
		if(B(2)) return -v;
		return v;
	}		
	public static int RSn0(int p){
		// com sinal, mas nunca zero
		int v=R(p);
		if(v==0)v++;
		if(B(2)) return -v;
		return v;
	}		
	public static boolean B(int p){
		return R(p)==0;
	}
	public static String R00(int p){		
		return intEmSt00(R(p)); // p3: 0,1,2
	}
	public static String R01(int p){		
		p--;
		return intEmSt00(R(p)+1); // p3: 1,2
	}
	public static boolean C(int p){
		return cont % p ==0;
	}
	public static int intInd(int i, int v1, int v2, int v3){
		switch(i){
			case 0: return v1; 
			case 1: return v2; 
			case 2: return v3; 
		}		
		J.impErr("!fora do intervalo dos parametros: "+i,"J.intInd()");
		return 0;
	}	
	public static int intInd(int i, int v1, int v2, int v3, int v4){
		switch(i){
			case 0: return v1; 
			case 1: return v2; 
			case 2: return v3; 
			case 3: return v4; 
		}		
		J.impErr("!fora do intervalo dos parametros: "+i,"J.intInd()");
		return 0;
	}
	public static int intInd(int i, int v1, int v2, int v3, int v4, int v5){
		switch(i){
			case 0: return v1; 
			case 1: return v2; 
			case 2: return v3; 
			case 3: return v4; 
			case 4: return v5; 
		}		
		J.impErr("!fora do intervalo dos parametros: "+i,"J.intInd()");
		return 0;
	}
	public static int modulo(int v){
		return abs(v);
	}
	
	public static boolean noInt(int v, int min, int max){
		if(min>max) {int b=min; min=max; max=b; }
		return (v>=min) && (v<=max);
	}
	public static boolean noInt(float v, float min, float max){
		if(min>max) {float b=min; min=max; max=b; }
		return (v>=min) && (v<=max);
	}	
	public static int incIntR(int v, int min, int max){ // rodaInt
		v++;
		if(v>max)v=min;
		if(v<min)v=max;
		return v;
	}	
	public static int decIntR(int v, int min, int max){
		v--;
		if(v>max)v=min;
		if(v<min)v=max;
		return v;
	}		
	public static int corrInt(int v, int min, int max){
		if(v>max)v=max;
		if(v<min)v=min;
		return v;
	}
	public static float corrFloat(float v, float min, float max){
		if(v>max)v=max;
		if(v<min)v=min;
		return v;
	}	

	public static boolean vou(int v, int[]l){
		// saiba: vetor = new int[]{10,30,100};
		for(int k=0; k<l.length; k++)
			if(l[k]==v) return true;
		return false;	
	}
	public static boolean vou(int v, int p, int pp){
		return (v==p)||(v==pp);
	}
	public static boolean vou(int v, int p, int pp, int ppp){
		return (v==p)||(v==pp)||(v==ppp);
	}
	public static boolean vou(int v, int p, int pp, int ppp, int pppp){
		return (v==p)||(v==pp)||(v==ppp)||(v==pppp);
	}	
	public static boolean vou(int v, int p, int pp, int ppp, int pppp, int ppppp){
		return (v==p)||(v==pp)||(v==ppp)||(v==pppp)||(v==ppppp);
	}		
	public static boolean vou(int v, int p, int pp, int ppp, int pppp, int ppppp, int pppppp){
		return (v==p)||(v==pp)||(v==ppp)||(v==pppp)||(v==ppppp)||(v==pppppp);
	}			
	public static boolean vou(int v, int p, int pp, int ppp, int pppp, int ppppp, int pppppp, int ppppppp){
		return (v==p)||(v==pp)||(v==ppp)||(v==pppp)||(v==ppppp)||(v==pppppp)||(v==ppppppp);
	}				
	public static boolean vou(int v, int p, int pp, int ppp, int pppp, int ppppp, int pppppp, int ppppppp, int pppppppp){
		return (v==p)||(v==pp)||(v==ppp)||(v==pppp)||(v==ppppp)||(v==pppppp)||(v==ppppppp)||(v==pppppppp);	
	}	

	public static boolean tem(char ca, String st){
		if(st==null) return false;
		boolean t=false;
		for(int k=0; k<st.length(); k++)
		 if (st.charAt(k)==ca) t=true;
		return t; 
	}
	public static boolean tem(String trecho, String texto){
		// assim eh bem melhor, a antiga tava bugada. Acho q apanhei muito nos fontes antigos por causa do bug q tinha aqui. Nao mais.
		// ? NOME==nome??? Como tá a caixa alta???
		if(trecho==null) trecho="";
		if(texto==null) texto="";
		if(true){// opcao depois...
			trecho = emMaiusc(trecho);
			texto = emMaiusc(texto);
		}
		return texto.contains(trecho);
	}
	
/* FUNCOES DE CARREGAMENTO
a fazer: 
- revisar "." e extensoes; autoinserir
- deve ser possivel acessar por caminho completo
- revisar avisos de arq perdido
*/

	public static Image carrFig(String cam){
		//!!! NAO USE ESSE METODO, use JFig.java. 
    cam = emMinusc(cam);
		if(cam==null) return null;
		if(cam.equals("")) return null;
		
		if(!tem('.',cam)) cam = cam + ".png";
		if(!tem('/',cam)) cam = "jpg/"+cam; // nao tah aceitando "c:\\"
		
		// cam = corrCam(cam,extArq(cam));
		
			// File file = new File(cam);
			// if (file.exists()) J.esc("ok: "+cam);
			// else { 
				// J.esc("arq perdido: "+cam); 
				// sai();
			// }
			
		if(arqExist(cam))	 
			J.esc("ok: "+cam);
		else {
			J.impErr("arq perdido: |"+cam+"|", "J.carrFig()");
			sai();
		}
			
		ImageIcon w = null;
		// w = new ImageIcon(J.class.getResource(cam));
		w = new ImageIcon(cam);
		
		// aqui!
		altPng = w.getIconHeight();
		larPng = w.getIconWidth();		
		
		Image ww = w.getImage();	
		// ww.getWidth(null); // isso deve ser testado
		return ww;
	}
	public static void impFig(Image fig, int i, int j, int ii, int jj, int larArq, int altArq){
		//!!! NAO USE ESSE METODO, use JFig.java. 		
		g.drawImage(fig, i,j,ii,jj, 0,0, larArq,altArq, null);
		// daria p deduzir o tamanho, mas precisa saber usar o "imageObserver" q eh meio complicado
		// dah p tirar essa informacao de dimensoes na marra usando engenharia reversa e comparando arquivos
		// mas eh melhor procurar o formato interno na internet
		// repetir o mesmo com arq wav
	}
		static int larPng=100, altPng=100;
	public static void impFig(Image fig, int i, int j, int ii, int jj){
		//!!! NAO USE ESSE METODO, use JFig.java. 		
		if(bufImp==null) {
			g.drawImage(fig, i,j,ii,jj, 0,0, larPng,altPng, null);
			//													coord dentro do arq
		} else { //11111111111111111111
			grafBufImp.drawImage(fig, i,j,ii,jj, 0,0, larPng,altPng, null);
		}
	}
	
	public static BufferedImage carrBFig(String cam){
		//!!! NAO USE ESSE METODO, use JFig.java. 		
		if(cam==null) return null;
		if(cam.equals("")) return null;
		
		if(!tem('.',cam)) cam = cam + ".png";
		if(!tem('/',cam)) cam = "jpg//"+cam; 
			
		if(arqExist(cam))	 J.esc("ok: "+cam);
		else J.impErr("!arquivo perdido: "+cam,"J.carrBFig()");

		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(cam));
			if(img!=null){
				altPng = img.getHeight();
				larPng = img.getWidth();
				J.esc("   dimensoes: "+larPng+":"+altPng);
			} else {
				J.esc("FALHA ao colher dimensoes: J.carrBFig()");
			}
		} catch (IOException e) {	}
		
		return img;		
	}
	public static boolean saveBFig(BufferedImage fig, String cam){
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
			// J.esc("FALHOU: "+cam);
			J.impErr("!falha salvando imagem: "+cam,"J.saveBFig(BufferedImage fig, String cam)");
		}
		return ok;
	}

	public static String camWin(String p){ // umaBarra sohUmaBarra descendo 
		// caminho no formato windows
		// parece q ficou boa
		// 			"c://java//wav//inst01//do.wav"
		//		vira
		//			"c:\java\wav\inst01\do.wav"
		char ca = 0;
		int l=p.length(), e=1;
		String st="";
		boolean prim=true;
		for(int q=0; q<l; q++){
			ca = p.charAt(q);
			if(ca=='/'){
				if(prim) 
					{st+='\\'; prim=false; }
				else 
					{ /*nada*/ }
			} else {
				prim=true;
				st+=ca; 
			}
		}
		esc("-------------|"+st+"|");
		return st;		
	}	
	
	// ATENCAO!!! Este eh o jeito certo de carregar wavs en subpastas!!!
	// 		String cam = "wav/inst02/sol"; // ?No linux tb???
	public static AudioClip carrSom2(String cam){
		if(!tem('/',cam)) cam="wav//"+cam;
		if(!tem('.',cam)) cam+=".wav";
		if(!arqExist(cam)) J.impErr("!arquivo perdido: |"+cam+"|","J.carrSom2()");
		esc(cam);
		AudioClip	w = Applet.newAudioClip(J.class.getResource(cam));
		return w;
	}	
	public static AudioClip carrSom(String cam){
		
		cam = corrCam(cam,"wav");
		//cam = corrCam(cam);
		//if(!tem('.',cam)) cam+=".wav";
		//cam = camWin(cam);
		
		if(!arqExist(cam)){
			impErr("arquivo perdido: "+cam,"J.carrSom()... substituido por beep");
			//cam = corrCam("bip01","wav");
			cam = "wav/bip01.wav";
		}	else {
			esc("ok: "+cam); // mesmo???
		}
		// File file = new File(cam);
//22222222222222
		cam = camWin(cam); 
		esc("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		esc(cam);
		esc("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		AudioClip	w = Applet.newAudioClip(J.class.getResource(cam));
		// a duracao tah mais ou menos por aqui:
		// Long duracao = (Long)AudioFileFormat.getProperty("duration");
		// javax.sound.sampled
		// pesquisar mais em "j2se8.chm"


		return w;
	}
	public static Jf1 carrJf1(String cam){
		Jf1 f = new Jf1(cam);
		return f;
	}
	public static J3d carrJ3d(String cam){
		J3d q = new J3d(cam);
		return q;
	}
	public static Font carrFont(String cam, float tam){	
		// COMO FUNCIONA:
		// no seu fonte use 
		// "painel.setFont(carrFont("arial.ttf",12))"
		// "painel" eh derivado de "JPanel" 
		// deve estar em no construtor principal "jjj" de "jjj.java" ou similar
		// veja:
		// if (k.getKeyCode()==k.VK_SPACE) painel.setFont(J.carrFont("arial.ttf",12f));
		
		// PARA MUDAR A COR: 
		// use "g.setColor(Color cor);"

		// BONS: (c:\fontes\)
		// Instruction.ttf
		// UbuntuMono-B.ttf
		// MAKISUPA.TTF

		// valores padrao
		if(tam==0)tam=12f;
		if(tam==-1)tam=12f;
		if(cam==null) cam="arial.ttf";
		if(cam=="") cam="arial.ttf";
		
		if(!J.tem('.',cam)) cam+=".ttf";
		if(!J.tem(':',cam)) cam = "c://windows//fonts//"+cam;
		//cam = corrCam(cam,"ttf");
		if(!J.arqExist(cam)) {
			J.impErr("!arquivo de fonte nao encontrado: |"+cam+"|","J.carrFont()");
			return null;
		}	
		Font font = null;
		try{
			font = Font.createFont(Font.TRUETYPE_FONT, new File(cam));
			font = font.deriveFont(tam); // pode mudar depois
			// painel.setFont(font);							
			J.esc("ok: fonte: "+cam);
			return font;
		}catch(Exception e){
			J.impErr("!falha carregando fonte: "+cam,"J.carrFont()");
		}		
		return null;
	}	
	
// === MANIPULACAO DE FONTE ==============
// trata dos caracteres da fonte padrao. Usei em JWin.
// vale soh p arial tamanho padrao

	public static int larChar(char ca){ // tamChar
		// largura de caracteres
		// funcionava bem com a fonte do windows
		// acho q vai ter q fazer um fonte base de testes p calibrar isto no Linux
		if(J.tem(ca," \'\"!Ifijlt[]//\\||.,;:")) return 3; 
		if(ca==237) return 3; // "i" com acento agudo
		if(ca==205) return 3; // "I" com acento agudo
		if(J.tem(ca,"ry(){}-")) return 4;
		if(J.tem(ca,"xzv*^")) return 5;
		if(J.tem(ca,"Jc")) return 6;
		if(ca==231) return 6; // cedilha minuscula
		if(J.tem(ca,"BEKPS&")) return 8;
		if(ca==201) return 8; // "E" com acento agudo
		if(ca==202) return 8; // "E" com circunflexo
		if(J.tem(ca,"CDGHMNOQRUw")) return 9;
		if(ca==211) return 9; // "O" com acento agudo		
		if(ca==212) return 9; // "O" com circunflexo		
		if(ca==213) return 9; // "O" com til
		if(ca==199) return 9; // cedilha maiuscula
		if(J.tem(ca,"W%m")) return 11;
		if(J.tem(ca,"@")) return 12;
		return 7;
	}	
	public static int larText(String st){ // tamText larStr tamStr
		if(st==null) return 0;
		int v = 0;
		for(int q=0; q<st.length(); q++)
			v+= larChar(st.charAt(q));
		return v;
	}
	
	public static boolean charRec(int ca){
		// verifica se o caracter eh reconhecido por uma caixa de texto ou similar em JWin.java
		// apostrofos (  \'  ), usados na ordem de tabulacao, substituem tab e nao podem ser reconhecidos por enquanto
		// aspas estao dando problema na caixa de texto quando se usa apostrofo + shift p voltar a tabulacao
	
    // aaaaaaaaaaaaaaaaaaaaaaa
		//if(ca==129) return true; //seria a tecla acento
		//boolean opLeuAcentoAgudo=true;		
		//if(opLeuAcentoAgudo) { if(ca=='e') ca=(char)233; return true; }
		//if(opLeuAcentoAgudo) { if(ca=='e') ca=(char)233; return true; }
		//if(J.tem((char)ca,"??????????????????")) return true; // ?Mais algum aqui???
				if(ca==225) return true; // o trecho abaixo foi gerado gambiarro-automaticamente.
				if(ca==233) return true; // sao as vogais com acentos agudos, graves, circunflexos e til
				if(ca==237) return true; // mas nem todos estao aqui, jah q nao tem "i" com til
				if(ca==243) return true; // este fonte nao esta retendo acentos, deve ser algum bug no notepad++
				if(ca==250) return true;
				if(ca==193) return true;
				if(ca==201) return true;
				if(ca==205) return true;
				if(ca==211) return true;
				if(ca==218) return true;
				if(ca==227) return true;
				if(ca==245) return true;
				if(ca==195) return true;
				if(ca==213) return true;
				if(ca==226) return true;
				if(ca==194) return true;
				if(ca==234) return true;
				if(ca==202) return true;
				if(ca==244) return true; // "o" com circunflexo
				if(ca==212) return true; // "O" com circunflexo
				if(ca==224) return true; // "a" com crase
				if(ca==192) return true; // "A" com crase
				if(ca==231) return true; // cedilha minusculo				
				if(ca==199) return true; // cedilha maiusculo
		if(J.tem((char)ca,"ABCDEFGHIJKLMNOPQRSTUVWXYZ")) return true;
		if(J.tem((char)ca,"abcdefghijklmnopqrstuvwxyz")) return true;
		if(J.tem((char)ca," 0123456789")) return true;
		if(J.tem((char)ca,".,:;\""+(char)92)) return true; // 92=???
		if(J.tem((char)ca,"^~?!@#$%&*()<>[]{}-=_+//\\\'\"|")) return true;
		return false;
	}
	public static String remIni(int lim, String par){
		String st="";
		for(int q=lim; q<par.length(); q++)
			st+=par.charAt(q);
		return st;
	}
	public static String truncLar(int lim, String par){
		String ret="";
		int tam=0, lc=0;
		char ca=0;
		for(int q=0; q<par.length(); q++){
			ca = par.charAt(q);
			lc=larChar(ca);
			tam+=lc;
			if(tam>lim) return ret;
			else ret+=ca;
		}
		return ret;
	}
	public static String stEntre(char chIni, char chFim, Object p){
		// retorna o string entre os caracteres, sem inclui-los
		// usei p extrair o nome do string de objetos genericos
		if(p==null) return "null";
		String st="",pp=""+p; char ca=0;		
		
		if(pp.length()<=0) return "???111???";
		boolean vai=false;
		for(int q=0; q<pp.length(); q++){
			ca = pp.charAt(q);
			if(ca==chFim) return st;
			if(vai) st+=ca;
			if(ca==chIni) vai=true;
		}
		return "???222???";
	}
	public static String className(Object p){ // tag getClassName getName nomeClasse nomeDaClasse
		return stEntre('$','@',p);
	}
	public static boolean ehClass(Object p, String nm){
		return iguais(className(p), nm);
	}
	public static boolean mesmaClasse(Object p, Object pp){
		return J.iguais(className(p), className(pp));
	}
	public static boolean ehClass(Object p, String nm, String nmm){
		return iguais(className(p), nm) || iguais(className(p), nmm);
	}	
	
// === BASE PARA 3D ========================
// visao do editor, do novo p linux inclusive
		// static int tipCam=0; // 0=editor, 1=joeCraft // isso jah nao se usa
		static float angX=0, cosAngX=1, sinAngX=0;
		static float angY=0, cosAngY=1, sinAngY=0;
		static float angZ=0, cosAngZ=1, sinAngZ=0; 

		// deslocado DEPOIS de rotacionado; Bom p editor 3d
		static float incX=0, incY=0, incZ=0;


	public static class Coord2{
		int x=0, y=0;
		boolean fr=false, // fora da tela
			frh=false, frv=false; // fora Horizontalmente(x<margem ou maior q margem) ou Verticalmente
		boolean ao=false; // atras do olho, z; 
	}
		// abaixo somente quando usar "impPol2()"
		static boolean opPrecalc2d=true;
	public static class Coord3{
			float x=0, y=0, z=0;
			int X=0, Y=0; // 2d
			boolean ao = false;
		public Coord3(float i, float j, float k){///
			x=i; y=j; z=k;
			if(opPrecalc2d){
				Coord2 c = fxy(i,j,k);
				X = c.x;
				Y = c.y;
				ao = c.ao; // atraz do olho
			}
		}
		public Coord3 rel(float i, float j, float k){
			Coord3 q = new Coord3(x+i, y+j, z+k);
			return q;
		}

	}	
	
	public static boolean pntHitTri(float x, float y, float i, float j, float ii, float jj, float iii, float jjj){
		// verifica se o ponto x:y está dentro do tri i:j ii:jj iii:jjj
		float A =  (-jj * iii + j * (-ii + iii) + i * (jj - jjj) + ii * jjj) / 2f;
		int sinal = A < 0 ? -1 : 1;
		float s = (j * iii - i * jjj + (jjj - j) * x + (i - iii) * y) * sinal;
		float t = (i * jj - j * ii + (j - jj) * x + (ii - i) * y) * sinal;
		return s > 0 && t > 0 && s + t < 2f * A * sinal;
	}	
	
	public static boolean pntHitTri2(float x, float y, float x1, float y1, float x2, float y2, float x3, float y3) {
		// eu tinha esquecido q já existia um metodo destes, dai criei o 2 mesmo...
		// verifica se o pnt x:y está dentro do triangulo
		// da p adaptar um metodo destes juntando dois tris p formar um quadrilátero.
		// chatGPT ajudou aq
		
    // Calcula os valores dos determinantes
    double det1 = ((x - x2) * (y1 - y2)) - ((x1 - x2) * (y - y2));
    double det2 = ((x - x3) * (y2 - y3)) - ((x2 - x3) * (y - y3));
    double det3 = ((x - x1) * (y3 - y1)) - ((x3 - x1) * (y - y1));

    // Verifica se o ponto está dentro do triângulo
    if ((det1 >= 0 && det2 >= 0 && det3 >= 0) || (det1 <= 0 && det2 <= 0 && det3 <= 0)) {
        return true;
    } else {
        return false;
    }
}
	
	public static void impSpot(int cr, float tt, float i, float j, float ii, float jj){
		// imprime raio nano de MachineWars (mas acho q nao tá integrado naquele fonte)
		// daria p mudar cr p Color em vez de int, mas depois eu faço a compatibilidade
		// tag d impRaioTrat d impLaser
		
		
		//J.impLin(cr, i,j,ii,jj);
		//if(J.cont %10<5) return;
		J.impCirc(cr, tt, ii,jj);
		J.impTri(cr, 0, i,j, ii-tt,jj,ii+tt,jj); // hor
		J.impTri(cr, 0, i,j, ii,jj-tt,ii,jj+tt); // vert
		
		float ttt = tt*0.71f;
		J.impTri(cr, 0, i,j, ii-ttt,jj-ttt,ii+ttt,jj+ttt); // desce
		J.impTri(cr, 0, i,j, ii-ttt,jj+ttt,ii+ttt,jj-ttt); // sobe

	}	
	

	// esse eh 2d. O 3d tah mais p frente.
	public static boolean impTri(int cr, int crr, float x, float y, float xx, float yy, float xxx, float yyy){
		// ATENÇÃO!!! pntHitTri(x,y, i,j,ii,jj,iii,jjj) está disponivel!
		// acho q perde um pouco de clock com isso...
		return impTri(cr,crr, (int)x, (int)y, (int)xx, (int)yy,(int)xxx, (int)yyy);
	}
	public static boolean impTri(int cr, int crr, int x, int y, int xx, int yy, int xxx, int yyy){
		// ATENÇÃO!!! pntHitTri(x,y, i,j,ii,jj,iii,jjj) está disponivel!		
		cr+=opIncCr3d;
	
		if(cr<0)cr=0; 
		if(cr>512)cr=512;
		int m[]= {x,xx,xxx};
		int n[]= {y,yy,yyy};
		
		if(cr!=0){
			g.setColor(J.cor[cr]);
			g.fillPolygon(m,n,m.length);		
		}
		
		if(crr!=cr)
		if(crr!=0){
			if(crr<0)crr=0; 
			if(crr>512)crr=512;				
			g.setColor(J.cor[crr]);		
			g.drawPolygon(m,n,m.length);		
		}		

		int i = (int)menor(x, xx, xxx);
		int ii =(int) maior(x, xx, xxx);
		int j = (int)menor(y, yy, yyy);
		int jj = (int)maior(y, yy, yyy);
		// if(dentro)		
		if(opDebug){ // caixa de colisao
			g.setColor(J.cor[10]);
			g.drawRect(i,j, ii-i, jj-j);
		}
		// if(dentro)		
		if(noInt(xPonto, i,ii))// 2D
		if(noInt(yPonto, j,jj))
			return true;
		return false;		
//------------	
		

	}
	// 2D tambem
	public static boolean impTri(Color cr, Color crr, int x, int y, int xx, int yy, int xxx, int yyy){
		// ATENÇÃO!!! pntHitTri(x,y, i,j,ii,jj,iii,jjj) está disponivel!		
		int m[]= {x,xx,xxx};
		int n[]= {y,yy,yyy};
		if(cr!=null){
			g.setColor(cr);
			g.fillPolygon(m,n,m.length);		
		}
		if(crr!=null)
		if(cr!=crr){
			g.setColor(crr);		
			g.drawPolygon(m,n,m.length);		
		}
		
		if(!opVerHit) return false;
	
		int i = (int)menor(x, xx, xxx);
		int ii =(int) maior(x, xx, xxx);
		int j = (int)menor(y, yy, yyy);
		int jj = (int)maior(y, yy, yyy);
		// if(dentro)		
		if(opDebug){ // caixa de colisao
			g.setColor(J.cor[10]);
			g.drawRect(i,j, ii-i, jj-j);
		}
		// if(dentro)		
		if(noInt(xPonto, i,ii))// 2D
		if(noInt(yPonto, j,jj))
			return true;
	
		return false;
	}	
	// esse eh 2d. O 3d tah mais p frente.
	public static void impPol(int cr, int crr, int x, int y, int xx, int yy, int xxx, int yyy, int xxxx, int yyyy){
		// ATENÇÃO!!! pntHitTri(x,y, i,j,ii,jj,iii,jjj) está disponivel!		
		if(cr<0)cr=0; 
		if(cr>512)cr=512;
		int m[]= {x,xx,xxx,xxxx};
		int n[]= {y,yy,yyy,yyyy};
		if(cr!=0){
			g.setColor(J.cor[cr]);
			g.fillPolygon(m,n,m.length);		
		}
		if(crr!=cr)
		if(crr!=0){
			if(crr<0)crr=0; 
			if(crr>512)crr=512;				
			g.setColor(J.cor[crr]);		
			g.drawPolygon(m,n,m.length);		
		}
	}	
		static Color opCrPolSel=null;		
	public static boolean impPol(Color cr, Color crr, int x, int y, int xx, int yy, int xxx, int yyy, int xxxx, int yyyy){
		// ATENÇÃO!!! pntHitTri(x,y, i,j,ii,jj,iii,jjj) está disponivel!		
		// retorna true se deu hit em  J . x P o n t o
		// IMPORTANTE:  o p V e r H i t  precisa ser true, senão sempre retornará false;
		// tag abacaxi
		// ajustei melhor este metodo em fev de 2022
		// funciona bem, ma o problema é q destacará todos os pols q der hit, e no mine eu queria só o ultimo pol impresso com hit. O método então fará todos os pols na reta do "cursor" brilharem com "opCrPolSel".
		
		if(cr==null && crr==null) return false;
		numPolImp++;		
		
		boolean selPol=false;

		if(opVerHit){ // examinando cursor sobre o poligono, no caso corresponde ao meio da tela
			int i = (int)menor(x, xx, xxx, xxxx);
			int ii =(int) maior(x, xx, xxx, xxxx);
			int j = (int)menor(y,yy,yyy,yyyy);
			int jj = (int)maior(y,yy,yyy,yyyy);

			if(opDebug){ // caixa de colisao
				g.setColor(J.cor[J.rndCr9()]);
				g.drawRect(i,j, ii-i, jj-j);
			}
			
			selPol=false;
			if(noInt(xPonto, i,ii)) // 2D
			if(noInt(yPonto, j,jj)) 
				selPol=true;
			
		}
		
		if(selPol) 
			if(opCrPolSel!=null) 
				cr = opCrPolSel;

		int m[]= {x,xx,xxx,xxxx};
		int n[]= {y,yy,yyy,yyyy};
		if(cr!=null){
			g.setColor(cr);
			g.fillPolygon(m,n,m.length);		
		}
		if(crr!=cr)
		if(crr!=null){
			g.setColor(crr);		
			g.drawPolygon(m,n,m.length);		
		}		
		return selPol;
	}		
	public static void impMont(Color cr, int x, int y, int tx, int ty){
		// coordenadas RELATIVAS
		// int inc = (int)(tx/3f);
		int xm = (int)(x+tx/2f);
		// impTri(cre,0, xm,y, x,y+ty, (x+=inc),y+ty);
		// impTri(cr ,0, xm,y, x,y+ty, (x+=inc),y+ty);
		// impTri(crd,0, xm,y, x,y+ty, (x+=inc),y+ty);	
		// impTri(cr.darker(),null, xm,y, x,y+ty, (x+=inc),y+ty);
		// impTri(cr,null, xm,y, x,y+ty, (x+=inc),y+ty);	
		// impTri(cr.brighter(),null, xm,y, x,y+ty, (x+=inc),y+ty);	
		impTri(cr,null, xm,y, x,y+ty, x+tx,y+ty);	
	}
	public static void impLin(Color cr, int x, int y, int xx, int yy){
		if(cr==null) return;
		g.setColor(cr);
		g.drawLine(x, y, xx, yy);					
	}	
	public static void impLin(Color cr, float x, float y, float xx, float yy){
		if(cr==null) return;
		g.setColor(cr);
		g.drawLine((int)x, (int)y, (int)xx, (int)yy);					
	}		
	public static void impLinRel(Color cr, int x, int y, int xx, int yy){
		if(cr==null) return;
		g.setColor(cr);
		g.drawLine(x, y, xx+x, yy+y);					
	}	
	public static void impLinRel(int cr, int x, int y, int xx, int yy){
		impLin(cr,x,y,x+xx,y+yy);
	}
	public static void impLin(int cr, float x, float y, float xx, float yy){
		impLin(cr, (int)x,(int)y,(int)xx,(int)yy);
	}
	public static void impLin(int cr, int x, int y, int xx, int yy){
		if(cr>512)cr=512;
		if(cr<0)cr=0;
		if(cr==0)cr=16;
		g.setColor(J.cor[cr]);
		g.drawLine(x, y, xx, yy);					
	}

//=== LOSANGULOS ==============================
/* COMO IMPRIMIR UMA TABELA ISO (tipo SimCity)
		int v = 24; // zoom
	public void impTabISO(){
		int esp=v+v, espp=v, mm=0, nn=0;
		for(int m=-maxX; m<=maxX; m++)
		for(int n=-maxY; n<=maxY; n++){
			mm = J.xPonto+(m+n)*(esp>>1); 
			nn = J.yPonto+(n)*espp-((m*espp)>>1)-(n*esp>>2); // haaaaaaa!!! consegui
			nn-=espp>>1; // centralizando			
			J.impRetRel(15,0, mm, nn,2,2);
			J.impLosRel(J.abs(m*n),10, mm, nn,esp,espp);
		}
	}
	
	public void impTabISO2(){ // olhos mais baixos, mais horizontalmente perto do tabuleiro. Nao t? perfeito, mas jah ajuda.
		int esp=v+v+v, espp=v, mm=0, nn=0;
		float w=(v/8);
		if(v==24) w=3;
		if(v==48) w=6;
		for(int m=-maxX; m<=maxX; m++)
		for(int n=-maxY; n<=maxY; n++){
			mm = J.xPonto+(m+n)*(esp>>1); 
			//nn = J.yPonto+(n)*espp-((m*espp)>>1)-(n*esp>>2); // haaaaaaa!!! consegui
			nn = J.yPonto+(n)*espp-((m*espp)>>1)-(n*esp>>3)-(int)(n*w); // haaaaaaa!!! consegui

			
			//J.impRetRel(15,0, mm, nn,2,2);
			J.impLosRel(J.abs(m*n),10, mm, nn,esp,espp);
		}
	}
	
*/
	public static void impLos(int crp, int crc, int i, int j, int ii, int jj){
		impLos(J.cor[crp],J.cor[crc], i,j,i,j);
	}
	public static void impLosRel(int crp, int crc, int i, int j, int ii, int jj){
		impLos(J.cor[crp],J.cor[crc], i,j,i+ii,j+jj);
	}
	public static void impLosRel(Color crp, Color crc, int i, int j, int ii, int jj){
		impLos(crp,crc, i,j,i+ii,j+jj);
	}
	public static void impLos(Color crp, Color crc, int i, int j, int ii, int jj){
		int m=(ii-i)>>1;
		int n=(jj-j)>>1;		
		J.impPol(crp,crc, 
			i+m, j,
			ii, j+n,
			i+m-1, jj, // esse "-1" ? calibragem fina
			i, j+n);
	}



//=== RETANGULO2D ======================	


	public static void impMoldRel(Color crLuz, Color crSomb, int i, int j, int ii, int jj){
		// menos intermediarios deve acelerar o clock
		// ainda nao usa stroke
		if(crSomb!=null){
			g.setColor(crSomb);
			g.drawLine(i+ii,j,i+ii,j+jj); // dir
			g.drawLine(i,j+jj,i+ii,j+jj); // bxo
		}		
		if(crLuz!=null){
			g.setColor(crLuz);
			g.drawLine(i,j,i+ii,j); // cma
			g.drawLine(i,j,i,j+jj); // esq
		}
	}
	public static void impRet(int cr, int crr, float iii, float jjj, float iiii, float jjjj){
		if(g==null) return; // retirando bug 
		int i = (int)iii;
		int j = (int)jjj;
		int ii = (int)iiii;
		int jj = (int)jjjj;		
		if(i>ii){int w=i; i=ii; ii=w; }
		if(j>jj){int w=j; j=jj; jj=w; }		
		{ // correcoes de limite da tela // 999999999999999999999
			if(i<0) i = 0;
			if(j<0) j = 0;
			if(ii>J.maxXf) ii = J.maxXf;
			if(jj>J.maxYf) jj = J.maxYf;
		}
		if(cr!=0){
			g.setColor(J.cor[cr]);
			g.fillRect(i,j, ii-i, jj-j);			
		}
		if(crr!=0){
			g.setColor(J.cor[crr]);
			g.drawRect(i,j, ii-i-1, jj-j-1);			
		}		
	}
	public static void impRet(Color cr, Color crr, int i, int j, int ii, int jj){
		int w=0;
		if(g==null) return; // retirando bug de joeCraft
		if(i>ii){w=i; i=ii; ii=w; }
		if(j>jj){w=j; j=jj; jj=w; }
		if(cr!=null){
			g.setColor(cr);
			g.fillRect(i,j, ii-i, jj-j);			
		}
		if(crr!=null){
			g.setColor(crr);
			g.drawRect(i,j, ii-i-1, jj-j-1);			
		}
	}
	public static void impRetRel(Color cr, Color crr, int i, int j, int ii, int jj){		
		impRet(cr,crr, i,j,i+ii,j+jj);
	}
	public static void impRetRel(Color cr, Color crr, float i, float j, float ii, float jj){		
		impRet(cr,crr, (int)i,(int)j,(int)(i+ii),(int)(j+jj));
	}	

	public static void impRet(int cr, int crr, int i, int j, int ii, int jj){		
		if(cr<0) cr=0;
		if(cr>512) cr=512;
		Color c = (cr==0?null:J.cor[cr]);
		Color cc = (crr==0?null:J.cor[crr]);
		impRet(c, cc, i,j,ii,jj);
	}
	public static void impRetRel(int cr, int crr, float i, float j, float ii, float jj){
		impRetRel(cr,crr, (int)i, (int)j, (int)ii, (int)jj);
	}
	public static void impRetRel(int cr, int crr, int i, int j, int ii, int jj){
		if(cr<0)cr=0;
		if(cr>512)cr=512;
		if(crr<0)crr=0;
		if(crr>512)crr=512;		
		Color c = (cr==0?null:J.cor[cr]);
		Color cc = (crr==0?null:J.cor[crr]);	
		impRet(c, cc, i,j,i+ii,j+jj);
	}
		static int opTamPixel = 2;
	public static void putPixel(int cr, int i, int j){ // gambiarra nostalgica
		// impPixel
		// i--; j--;
		J.impRet(cr,0, i,j,i+opTamPixel,j+opTamPixel);
	}
		static Color crText = Color.WHITE;
		
	public static void impText(int x, int y, Color cr, Font f, String st){
		if(st==null) return;

		Font ant = null;
		if(f!=null){
			ant = g.getFont();
			g.setFont(f);
		}
		
		g.setColor(Color.BLACK); // acho q essa nao precisa ser configuravel
		g.drawString(st,x+1,y+1);
		
		g.setColor(cr);
		g.drawString(st,x,y);		
		if(ant!=null) g.setFont(ant);
	}
		
	public static void impText(int x, int y, String st){
		if(st==null) return;

		g.setColor(Color.BLACK); // acho q essa nao precisa ser configuravel
		g.drawString(st,x+1,y+1);
		
		g.setColor(crText);
		g.drawString(st,x,y);		
	}
	
	public static void impText(int x, int y, Color cr, String st){
		// usei na impressao de botoes de JWin
		// nao queria sombra direta
		if(st==null) return;

		g.setColor(cr);
		g.drawString(st,x,y);		
	}
	
		// p barras de life
		public static int opCrContBar=15;
	public static void impBar(float v, float max, int crOn, int crOff, float i, float j, float ii, float jj){ // impLife impBarr
		impBar(v,max,J.cor[crOn],J.cor[crOff],i,j,ii,jj);
	}
	public static void impBarr(float v, float max, int crOn, int crOff, float i, float j, float ii, float jj){ // impLife impBarr
		impBar(v,max,J.cor[crOn],J.cor[crOff],i,j,ii,jj);
	}	
	public static void impBar(float v, float max, Color crOn, Color crOff, float i, float j, float ii, float jj){ // impLife impBarr
		// coord absolutas
		if( J.abs(ii)-J.abs(i) > J.abs(jj)-J.abs(j)){ // horizontal
			int vv = (int)prop(v,max, ii-i);
			J.impRet(crOn,null, (int)i,(int)j,(int)i+vv,(int)jj);
			J.impRet(crOff,null, (int)i+vv,(int)j,(int)ii,(int)jj);
		} else {
			int vv = (int)prop(v,max, jj-j);			
			J.impRet(crOff,null, (int)i,(int)j,(int)ii,(int)jj-vv);
			J.impRet(crOn,null, (int)i,(int)jj-vv,(int)ii,(int)jj);			
		}
		J.impRet(0,opCrContBar, i,j,ii,jj);
	}		
	

	
//=== CIRCULOS e EL?PSES===================================	
	// orientado pelo centro e raio
	public static void impElipse(Color cr, float rh, float rv, float x, float y){
		// rh = raio horizontal
		// rv = adivinha :b
		g.setColor(cr);				
		g.fillArc((int)(x-rh), (int)(y-rv), (int)(rh+rh),   (int)(rv+rv),      1,      360);	
		// 			     x,    y, width,  height, angIni,   angFin
	}
	public static void impElipse(Color cr,Color crr, float rh, float rv, float x, float y){
		if(cr!=null){
			g.setColor(cr);				
			g.fillArc((int)(x-rh), (int)(y-rv), (int)(rh+rh),   (int)(rv+rv),      1,      360);	
			// 			     x,    y, width,  height, angIni,   angFin
		}
		if(crr!=null){
			g.setColor(crr);				
			g.drawArc((int)(x-rh), (int)(y-rv), (int)(rh+rh),   (int)(rv+rv),      1,      360);	
			// 			     x,    y, width,  height, angIni,   angFin
		}		
	}	
	public static void impElipse(int cr,int crr, float rh, float rv, float x, float y){
		// ?limites p cores???
		// ?raios negativos???
		// ?fora da tela???
		if(cr!=0){
			g.setColor(J.cor[cr]);
			g.fillArc((int)(x-rh), (int)(y-rv), (int)(rh+rh),   (int)(rv+rv),      1,      360);	
			// 			     x,    y, width,  height, angIni,   angFin
		}
		if(crr!=0){
			g.setColor(J.cor[crr]);
			g.drawArc((int)(x-rh), (int)(y-rv), (int)(rh+rh),   (int)(rv+rv),      1,      360);	
			// 			     x,    y, width,  height, angIni,   angFin
		}		
	}		
	public static void impCirc(int cr, int crr, float r, float x, float y){
		impCirc(cr,crr,(int)r,(int)x,(int)y);
	}
	public static void impCirc(int cr, int crr, int r, int x, int y){
		if(cr>512) cr=512;
		if(cr<0) cr=0;
		if(crr>512) crr=512;
		if(crr<0) crr=0;		
		if(cr!=0){
			g.setColor(J.cor[cr]);				
			g.fillArc(x-r,y-r, r+r,r+r, 1,360);	
			// 			  x, y, width, height, angIni, angFin
		}
		if(crr!=0)
		if(crr!=cr){
			g.setColor(J.cor[crr]);				
			g.drawArc(x-r,y-r, r+r,r+r, 1,360);	
		}
	}	
	public static void impCirc(Color cr, int x, int y, int lar, int alt){
		// tag elipse, oval, arco
		// veja q nao usa raio, x:y eh o ponto central
		// distorcer com lar e alt
		if(cr==null) return;
		g.setColor(cr);
		// 			  x, y, width, height, angIni, angFin
		g.fillArc(x-(lar>>1),y-(alt>>1), lar,alt, 1,360);			
		
	}
	public static void impCirc(Color cr, float x, float y, float lar, float alt){
		// !!!!!!
		// ATENCAO: esta impressao parece estar certa, as outras devem estar erradas
		// problema na largura e altura. Melhor conferir depois, mas vai alterar os fontes antigos.
		if(cr==null) return;
		g.setColor(cr);
		// 			  x, y, width, height, angIni, angFin
		//g.fillArc((int)(x-lar*0.5f),(int)(y-alt*0.5f), (int)lar,(int)alt, 1,360);			
		g.fillArc((int)(x-lar),(int)(y-alt), (int)(lar*2f),(int)(alt*2f), 1,360);			
	}	
	public static void impCirc(Color cr, int r, int x, int y){
		if(cr==null) return;
		g.setColor(cr);				
		// 			  x, y, width, height, angIni, angFin
		g.fillArc(x-r,y-r, r+r,r+r, 1,360);	
	}		
	public static void impCirc(Color cr, Color crr, int r, int x, int y){
		if(cr!=null){
			g.setColor(cr);				
			// 			  x, y, width, height, angIni, angFin
			g.fillArc(x-r,y-r, r+r,r+r, 1,360);			
		}
		if(crr!=null){
			g.setColor(crr);				
			// 			  x, y, width, height, angIni, angFin
			g.drawArc(x-r,y-r, r+r,r+r, 1,360);			
		}		
	}			
	public static void impCirc(Color cr, float r, float x, float y){
		impCirc(cr,(int)r,(int)x,(int)y);
	}
	public static void impCirc(int cr, float r, float x, float y){
		if(cr<0) cr=12;
		impCirc(J.cor[cr],(int)r,(int)x,(int)y);
	}	
	public static void impCircRel(int cr,int crr, int x, int y, int xx, int yy){
		if(cr>512) cr=512;
		if(cr<0) cr=0;
		if(cr!=0){
			g.setColor(J.cor[cr]);				
			// 			  x, y, width, height, angIni, angFin
			g.fillArc(x,y, xx,yy, 1,360);	
		}
		if(crr!=0)
		if(crr!=cr){
			g.setColor(J.cor[crr]);				
			g.drawArc(x,y, xx,yy, 1,360);	
		}
		
	}	
//=======================================

	
	
	// abaixo eh usado p impTri e impPol 3d
	public static int media(int p, int pp){
		//isso foi escrito no team wiewer
		return(int)((p+pp)/2f);
	}

		static int opEscurecer=0; 
/* NAO USA O VETOR DA PALETA!!! 
	Parametros: [-7..+7], 0 nao altera,  
	boa gambiarra p fora da paleta, mas funciona bem	
	bom p efeitos de noite e clarao de raio/trovao
*/	
		static int opCrAnt=-1, opCrNov=-1; 
// gambiarra p impressao de balde3d
		
	// agora 3d
	// true se desenhou sobre xPonto:yPonto = cursor de JoeCraft 3D	
		static int 
			opTingirEsp=0; // p efeitos de congelamento ou qq
	public static boolean impTri(int cr, int crc, Coord3 p, Coord3 pp, Coord3 ppp){
		// ATENÇÃO!!! pntHitTri(x,y, i,j,ii,jj,iii,jjj) está disponivel!		
		// J.esc("cuidado! Triangulo 3D com cor e contorno zero!");
		if(cr==0) if(cr==crc) return false;
		J.Coord2 c = fxy(p.x, p.y, p.z);
		J.Coord2 cc = fxy(pp.x, pp.y, pp.z);
		J.Coord2 ccc = fxy(ppp.x, ppp.y, ppp.z);
		boolean dentro = true;
		int u = maxXf, w = maxYf;
		// "c.ao" = atraz do olho
		// "c.fr" = fora da tela, 2d
		// if (c.fr && cc.fr && ccc.fr) dentro=false;
		// if (c.ao && cc.ao && ccc.ao) dentro=false;//????????????
		if(!dentro) return false; // com certeza nao pegou o mouse no centro
		if(dentro){
			int m[] = {c.x, cc.x, ccc.x};
			int n[] = {c.y, cc.y, ccc.y};
			if(cr!=0){
				
				if(cr==opCrAnt) cr=opCrNov; // gambiarra p impressao de balde3d
				
				if(opIncCr3d!=0)
				if(cr>15)
				if(cr<250)
					cr+=opIncCr3d;
				
				if(opTingirEsp!=0){ // p efeito de congelamento
					cr = cr%10;
					cr+=opTingirEsp-9; // "9" aqui. Cuidado!
				}				
				
				if(cr<0)cr=0;
				if(cr>512)cr=512;				

				if(opImpAlfa==0)
					g.setColor(cor[cr]);
				else 
					g.setColor(altAlfa(J.cor[cr],opImpAlfa));
				g.fillPolygon(m,n, m.length);				
			}
			
			// contorno			
			if(crZettaVision==null) // tratado abaixo
			if(crc!=0)
			if(crc!=cr)
			if(crc>=0)
			if(crc<=512){
				g.setColor(cor[crc]); // soh contorno
				g.drawPolygon(m,n, m.length);				
			}
			
			// aqui
			if(crZettaVision!=null){ 
				g.setColor(crZettaVision);
				g.drawPolygon(m,n, m.length);												
			}	

		}
		int i = (int)menor(c.x, cc.x, ccc.x);
		int ii =(int) maior(c.x, cc.x, ccc.x);
		int j = (int)menor(c.y, cc.y, ccc.y);
		int jj = (int)maior(c.y, cc.y, ccc.y);
		if(dentro)		
		if(opDebug){ // caixa de colisao
			g.setColor(J.cor[10]);
			g.drawRect(i,j, ii-i, jj-j);
		}
		if(dentro)		
		if(noInt(xPonto, i,ii))// 2D
		if(noInt(yPonto, j,jj))
			return true;
		return false;		
  }
	public static boolean impTri(int cr, int crc, float x,float y,float z, float xx,float yy,float zz, float xxx,float yyy,float zzz){
		// ATENÇÃO!!! pntHitTri(x,y, i,j,ii,jj,iii,jjj) está disponivel!		
		return impTri(cr, crc, 
							new J.Coord3(x,y,z), 
							new J.Coord3(xx,yy,zz), 
							new J.Coord3(xxx,yyy,zzz));
	}		

	public static Color altColor(Color p, int luz){
		// luz 0..255
		return altColor(p, luz,luz,luz);
	}
	public static Color altColor(Color p, int rr, int gg, int bb){
		// Veja q o deslocamento de rr, gg, bb é relativo.
		// dah p usar um getRGB() aqui
		if(p==null) return null;
		int r = p.getRed();
		int g = p.getGreen();	
		int b = p.getBlue();
		int a = p.getAlpha();
		
		// int w=10;// <><><><><><><><>
		// r+=rr*w;
		// g+=gg*w;
		// b+=bb*w;
		r+=rr;
		g+=gg;
		b+=bb;
		if(r<0)r=0; if(r>255)r=255;
		if(g<0)g=0; if(g>255)g=255;
		if(b<0)b=0; if(b>255)b=255;
		// nao meche em alpha
		//return new Color(r,g,b,a);
		return altAlfa(new Color(r,g,b), a);
	}
	public static Color altAlfa(int p, float q){
		return altAlfa(J.cor[p], q);
	}
	public static Color altAlfa(Color p, float q){
		// 0->0
		// 0.5->127
		// 1->255
		// x = 127*1/255;
		int qtd = (int)(q*255);
		
		// nao precisa disso
		// AUTOCORRECAO NO OUTRO METODO
		//qtd = J.corrInt(qtd,0,255); 
		return altAlfa(p, qtd );
	}
	public static Color altAlfa(Color p, int qtd){
		// dah p usar um getRGB() aqui
		// alfa 255 = opaco ao maximo, 0 eh transparente
		// ... soh q esse 255 tah invertido. 
		// 255 alfa seria totalmente transparente.
		// isso nao tah intuitivo pq o parametro tah invertido
		// o certo seria altOpacidade() p conservar estes parametros.
		if(p==null) return null;
		int r = p.getRed();
		int g = p.getGreen();	
		int b = p.getBlue();
		
		qtd = corrInt(qtd,0,255);		
		return new Color(r,g,b, qtd);
	}
	public static Color mixColor(int cr, int crr){
		// controle de limites depois
		return mixColor(J.cor[cr], J.cor[crr]);
	}
	public static Color mixColor(Color cr, Color crr){
		if(cr==null) cr = J.cor[16];
		if(crr==null) crr = J.cor[16];
		int r = cr.getRed();
		int g = cr.getGreen();	
		int b = cr.getBlue();		
		int a = cr.getAlpha();		
		int rr = crr.getRed();
		int gg = crr.getGreen();	
		int bb = crr.getBlue();				
		int aa = crr.getAlpha();				
		// r = (int)((r+rr)/2f);
		// g = (int)((g+gg)/2f);
		// b = (int)((b+bb)/2f);
		// a = (int)((a+aa)/2f);
		r = (r+rr)>>1; // assim eh mais rapido
		g = (g+gg)>>1;
		b = (b+bb)>>1;
		a = (a+aa)>>1;
		r = J.corrInt(r,0,255);
		g = J.corrInt(g,0,255);
		b = J.corrInt(b,0,255);
		a = J.corrInt(a,0,255);
		return new Color(r,g,b, a);
	}
	public static Color mixColor(Color cr1, Color cr2, Color cr3, Color cr4){
		if(cr1==null) cr1 = J.cor[16];
		if(cr2==null) cr2 = J.cor[16];
		if(cr3==null) cr3 = J.cor[16];
		if(cr4==null) cr4 = J.cor[16];
		int r = (cr1.getRed()+cr2.getRed()+cr3.getRed()+cr4.getRed())>>2; 
		int g = (cr1.getGreen()+cr2.getGreen()+cr3.getGreen()+cr4.getGreen())>>2; 
		int b = (cr1.getBlue()+cr2.getBlue()+cr3.getBlue()+cr4.getBlue())>>2; 
		int a = (cr1.getAlpha()+cr2.getAlpha()+cr3.getAlpha()+cr4.getAlpha())>>2; 
		r = J.corrInt(r,0,255);
		g = J.corrInt(g,0,255);
		b = J.corrInt(b,0,255);
		a = J.corrInt(a,0,255);
		return new Color(r,g,b, a);
	}
	public static Color mixColor(Color cr1, Color cr2, Color cr3, Color cr4, Color cr5){
		if(cr1==null) cr1 = J.cor[16];
		if(cr2==null) cr2 = J.cor[16];
		if(cr3==null) cr3 = J.cor[16];
		if(cr4==null) cr4 = J.cor[16];
		if(cr5==null) cr5 = J.cor[16];
		int r = (int)((cr1.getRed()+cr2.getRed()+cr3.getRed()+cr4.getRed()+cr5.getRed())/5f); 
		int g = (int)((cr1.getGreen()+cr2.getGreen()+cr3.getGreen()+cr4.getGreen()+cr5.getGreen())/5f); 
		int b = (int)((cr1.getBlue()+cr2.getBlue()+cr3.getBlue()+cr4.getBlue()+cr5.getBlue())/5f); 
		int a = (int)((cr1.getAlpha()+cr2.getAlpha()+cr3.getAlpha()+cr4.getAlpha()+cr5.getAlpha())/5f); 
		r = J.corrInt(r,0,255);
		g = J.corrInt(g,0,255);
		b = J.corrInt(b,0,255);
		a = J.corrInt(a,0,255);
		return new Color(r,g,b, a);
	}

	public static Color mixColor(float qtd, int cr, int crr){
		return mixColor(qtd, J.cor[cr], J.cor[crr]);
	}
	public static Color mixColor(float qtd, Color cr, Color crr){
		// qtd de 0f a 1f indicando a qtd da primeira cor
		if(cr==null) cr = J.cor[16];
		if(crr==null) crr = J.cor[16];
		int r = cr.getRed();
		int g = cr.getGreen();
		int b = cr.getBlue();
		int a = cr.getAlpha();
		
		// corrigindo a transparencia na gambiarra
		// nao sei sei isso atrapalhou em JoeCraft
		// adaptei este trecho p jogo de naves com png
		// abaixo tem mais deste, cuidado;
		if(r+g+b==0) return(new Color(0,0,0,0)); 
		
		int rr = crr.getRed();
		int gg = crr.getGreen();
		int bb = crr.getBlue();
		
		if(rr+gg+bb==0) return(new Color(0,0,0,0));		
		int aa = crr.getAlpha();
		//int aa = (int)(crr.getAlpha()*qtd);
		// ABAIXO NAO EH NECESSARIO AQUI
		// r = (r+rr)>>1; // assim eh mais rapido
		// g = (g+gg)>>1;
		// b = (b+bb)>>1;
		// a = (a+aa)>>1;
		if(qtd<0.001f) qtd=0;
		if(qtd>1f) qtd=1f;
		
		float q = qtd, qq = 1f-qtd;
		r = (int)(r*q+rr*qq);
		g = (int)(g*q+gg*qq);
		b = (int)(b*q+bb*qq);
		//a = (int)(a*q+aa*qq);

		a = (int)menor(a,aa);
		
		r = J.corrInt(r,0,255);
		g = J.corrInt(g,0,255);
		b = J.corrInt(b,0,255);
		a = J.corrInt(a,0,255);

		return new Color(r,g,b, a);
	}
	
// atualizando com parametros "Color". Nada de paleta engessada!
// esse eh 3d com "color"
	public static boolean impTri(Color crt, Color crc, Coord3 p, Coord3 pp, Coord3 ppp){
		// ATENÇÃO!!! pntHitTri(x,y, i,j,ii,jj,iii,jjj) está disponivel!		
		// nulos fora
		if(crt==null) if (crc==null) return false;
		
		// coordenadas 3d
		J.Coord2 c = fxy(p.x, p.y, p.z);
		J.Coord2 cc = fxy(pp.x, pp.y, pp.z);
		J.Coord2 ccc = fxy(ppp.x, ppp.y, ppp.z);
		
		// eh valido?
		boolean dentro = true;
		// "c.ao" = atraz do olho
		// "c.fr" = fora da tela, 2d
		// if (c.fr && cc.fr && ccc.fr) dentro=false;
		// if (c.ao && cc.ao && ccc.ao) dentro=false;
		// com certeza nao pegou o mouse no centro
		// saindo 
		if(!dentro) return false; 
		
		// desenhando
		if(dentro){
			int m[] = {c.x, cc.x, ccc.x};
			int n[] = {c.y, cc.y, ccc.y};
			if(crt!=null){
				// opCrAnt, opCrNov, o p I n c C r 3 d nao se aplicam aqui
				// mas dah p ajustar depois. 
				// Ateh um "tingir" nesse nivel seria legal;
			
				// DEVE SER OTIMIZADO!
				// deve consumir muito clock!
				Color acor = crt;				
				// if(opEscurecer>0){
					// if(opEscurecer>=1) acor=acor.darker();
					// if(opEscurecer>=2) acor=acor.darker();
					// if(opEscurecer>=3) acor=acor.darker();
					// if(opEscurecer>=4) acor=acor.darker();
					// if(opEscurecer>=5) acor=acor.darker();
					// if(opEscurecer>=6) acor=acor.darker();
					// if(opEscurecer>=7) acor=acor.darker();
				// }	
				// if(opEscurecer<0){
					// if(opEscurecer<=-1) acor=acor.brighter();
					// if(opEscurecer<=-2) acor=acor.brighter();
					// if(opEscurecer<=-3) acor=acor.brighter();
					// if(opEscurecer<=-4) acor=acor.brighter();
					// if(opEscurecer<=-5) acor=acor.brighter();
					// if(opEscurecer<=-6) acor=acor.brighter();
					// if(opEscurecer<=-7) acor=acor.brighter();
				// }					
				g.setColor(acor);
				g.fillPolygon(m,n, m.length);				
			}
			
			// contorno
			if(crc!=null)
			if(crc!=crt){				
				// OTIMIZAR ISSO! 
				// engole clock!
				Color acor = crc;				
				// if(opEscurecer>0){
					// if(opEscurecer>=1) acor=acor.darker();
					// if(opEscurecer>=2) acor=acor.darker();
					// if(opEscurecer>=3) acor=acor.darker();
					// if(opEscurecer>=4) acor=acor.darker();
					// if(opEscurecer>=5) acor=acor.darker();
					// if(opEscurecer>=6) acor=acor.darker();
					// if(opEscurecer>=7) acor=acor.darker();
				// }	
				// if(opEscurecer<0){
					// if(opEscurecer<=-1) acor=acor.brighter();
					// if(opEscurecer<=-2) acor=acor.brighter();
					// if(opEscurecer<=-3) acor=acor.brighter();
					// if(opEscurecer<=-4) acor=acor.brighter();
					// if(opEscurecer<=-5) acor=acor.brighter();
					// if(opEscurecer<=-6) acor=acor.brighter();
					// if(opEscurecer<=-7) acor=acor.brighter();
				// }			
				g.setColor(acor);
				g.drawPolygon(m,n, m.length);								
			}
		}
		
		// p reconhecimento de hit, a frente
		int i = (int)menor(c.x, cc.x, ccc.x);
		int ii =(int) maior(c.x, cc.x, ccc.x);
		int j = (int)menor(c.y, cc.y, ccc.y);
		int jj = (int)maior(c.y, cc.y, ccc.y);
		
		// p debug
		if(dentro)		
		if(opDebug){ // "caixa de colisao"
			g.setColor(Color.GREEN);
			g.drawRect(i,j, ii-i, jj-j);
		}
		
		//examinando hit do cursor
		if(dentro)		
		if(noInt(xPonto, i,ii))// 2D
		if(noInt(yPonto, j,jj))
			return true;
		return false;		
  }	
	public static boolean impTri(Color crt, Color crc, float i, float j, float k, float ii, float jj, float kk, float iii, float jjj, float kkk){
		// ATENÇÃO!!! pntHitTri(x,y, i,j,ii,jj,iii,jjj) está disponivel!		
		Coord3 c = new Coord3(i,j,k);
		Coord3 cc = new Coord3(ii,jj,kk);
		Coord3 ccc = new Coord3(iii,jjj,kkk);
		return impTri(crt,crc,c,cc,ccc);
	}

	

		static boolean op2Side=false; // force2side
		static int opIncCr3d=0; // efeitos p metal
		
		// p melhorar o efeito de cubos proximos
		// MAS NAO DAH TANTO EFEITO ASSIM...
		// static boolean opReforcaContorno=false; 

			// esse eh 3d
	public static boolean impPol(int cr, int crc, Coord3 p, Coord3 pp, Coord3 ppp, Coord3 pppp){
		// ATENÇÃO!!! pntHitTri(x,y, i,j,ii,jj,iii,jjj) está disponivel!		
		impPolOk=false;
		if( (cr==0)&& (crc==0) )return false;


			J.Coord2 c = J.fxy(p.x, p.y, p.z);
			J.Coord2 cc = J.fxy(pp.x, pp.y, pp.z);
					if(c.x>cc.x) 
						if(!op2Side) return false; // de costas; sair rapido
			J.Coord2 ccc = J.fxy(ppp.x, ppp.y, ppp.z);
			J.Coord2 cccc = J.fxy(pppp.x, pppp.y, pppp.z);
			boolean dentro = true;
			int u = J.maxXf, w = J.maxYf;
			if((c.fr) 
			&& (cc.fr) 
			&& (ccc.fr)
			&& (cccc.fr) ) 
				dentro=false;
			if((c.ao)
			&& (cc.ao) 
			&& (ccc.ao) 
			&& (cccc.ao) ) dentro=false;
			if(dentro){
				int m[] = {c.x, cc.x, ccc.x, cccc.x};
				int n[] = {c.y, cc.y, ccc.y, cccc.y};
				if(cr!=0){				
					if(opIncCr3d!=0)
					if(cr>15)
					if(cr<250){ // cores imunes
						cr+=opIncCr3d;
					}
					if(cr<0)cr=0;
					if(cr>512)cr=512;
				}	
				Color acor = cor[cr];
				if(opEscurecer>0){
					acor = cor[cr];				
					if(opEscurecer>=1) acor=acor.darker();
					if(opEscurecer>=2) acor=acor.darker();
					if(opEscurecer>=3) acor=acor.darker();
					if(opEscurecer>=4) acor=acor.darker();
					if(opEscurecer>=5) acor=acor.darker();
					if(opEscurecer>=6) acor=acor.darker();
					if(opEscurecer>=7) acor=acor.darker();
					g.setColor(acor);
				}	
				if(opEscurecer<0){
					acor = cor[cr];				
					if(opEscurecer<=-1) acor=acor.brighter();
					if(opEscurecer<=-2) acor=acor.brighter();
					if(opEscurecer<=-3) acor=acor.brighter();
					if(opEscurecer<=-4) acor=acor.brighter();
					if(opEscurecer<=-5) acor=acor.brighter();
					if(opEscurecer<=-6) acor=acor.brighter();
					if(opEscurecer<=-7) acor=acor.brighter();
					g.setColor(acor);
				}					
				
				if(opEscurecer==0)	g.setColor(acor);
				
				if(cr!=0){
					g.fillPolygon(m,n, m.length);
					impPolOk=true;
				}
				
				if(crc==-1)
				if(cr!=0){
					acor = acor.darker();
					g.setColor(acor.darker());
					g.drawPolygon(m,n, m.length);
					// NAO DAH TANTO EFEITO ASSIM...
					// if(opReforcaContorno){
						// int mss[] = {++c.x, ++cc.x, ++ccc.x, ++cccc.x};
						// int nss[] = {++c.y, ++cc.y, ++ccc.y, ++cccc.y};
						// g.setColor(acor.darker()); // mais escuro ainda
						// g.drawPolygon(mss,nss, mss.length);					
						// opReforcaContorno=false;
					// }
				}
				
				if(crc>0)
				if(crc<=512)
				if(crc!=cr){
					g.setColor(cor[crc]);					
					g.drawPolygon(m,n, m.length);								
				}
			}
		
		int i = (int)menor(c.x, cc.x, ccc.x, cccc.x);
		int ii =(int) maior(c.x, cc.x, ccc.x, cccc.x);
		int j = (int)menor(c.y, cc.y, ccc.y, cccc.y);
		int jj = (int)maior(c.y, cc.y, ccc.y, cccc.y);
		if(dentro)		
		if(opDebug){ // caixa de colisao
			g.setColor(J.cor[12]);
			g.drawRect(i,j, ii-i, jj-j);
		}
		if(dentro)
		if(noInt(xPonto, i,ii)) // 2D
		if(noInt(yPonto, j,jj))
			return true;
		return false;						
  }
	public static boolean impPol(int cr, int crc,boolean mancha, float x,float y,float z, float xx,float yy,float zz, float xxx,float yyy,float zzz,	float xxxx, float yyyy, float zzzz){
		// ATENÇÃO!!! pntHitTri(x,y, i,j,ii,jj,iii,jjj) está disponivel!		
		if(cr==0) return false;
		return impPol(cr, crc,
							new J.Coord3(x,y,z), 
							new J.Coord3(xx,yy,zz), 
							new J.Coord3(xxx,yyy,zzz), 
							new J.Coord3(xxxx,yyyy,zzzz));				
	}



	
// atualizando p parametros Color. Nada de paleta "engessante"	
// esse eh 3d
		static int opImpAlfa = 0; // 0..255
	public static boolean impPol(Color crp, Color crc, Coord3 p, Coord3 pp, Coord3 ppp, Coord3 pppp){
		// ATENÇÃO!!! pntHitTri(x,y, i,j,ii,jj,iii,jjj) está disponivel!		
		impPolOk=false;

		// invalidos fora
		if(crp==null)	if(crc==null) return false;

		// vendo de costas
		J.Coord2 c = J.fxy(p.x, p.y, p.z);
		J.Coord2 cc = J.fxy(pp.x, pp.y, pp.z);
		if(c.x>cc.x) 
		if(!op2Side) 
			return false; // sair rapido

		// continuando e aproveitando coords jah calculadas
		J.Coord2 ccc = J.fxy(ppp.x, ppp.y, ppp.z);
		J.Coord2 cccc = J.fxy(pppp.x, pppp.y, pppp.z);
		
		boolean dentro = true;

		// coords validas, seguindo
		if(dentro){
			int m[] = {c.x, cc.x, ccc.x, cccc.x};
			int n[] = {c.y, cc.y, ccc.y, cccc.y};
			
			Color acor = null;// tem q ser fora do bloco abaixo p reutilizar a cor p contorno depois
			if(crp!=null){
				// o p I n c C r 3 d nao se aplica aqui

				if(opImpAlfa==0) acor = crp;
				else acor = J.altAlfa(crp, opImpAlfa);
				
				g.setColor(acor);
				g.fillPolygon(m,n, m.length);
				impPolOk=true;				
			}	
			
			// CONTORNO ESPECIAL
			// escurecer contornos quando crp==crc
			// p deixar sem contornos, faca crc=null
			if(crc!=null)
			if(crc==crp){
				g.setColor(acor.darker());
				g.drawPolygon(m,n, m.length);
			}

			// CONTORNO COMUM
			// normais, com cor diferente
			if(crc!=null)
			if(crc!=crp){ // jah tratado acima
				acor = null; // eh outra cor agora
				acor = crc;
				g.setColor(acor);					
				g.drawPolygon(m,n, m.length);								
			}
		}
		
		int i = (int)menor(c.x, cc.x, ccc.x, cccc.x);
		int ii =(int) maior(c.x, cc.x, ccc.x, cccc.x);
		int j = (int)menor(c.y, cc.y, ccc.y, cccc.y);
		int jj = (int)maior(c.y, cc.y, ccc.y, cccc.y);
		if(dentro)		
		if(opDebug){ // caixa de colisao
			g.setColor(Color.RED);
			g.drawRect(i,j, ii-i, jj-j);
		}
		if(dentro)
		if(noInt(xPonto, i,ii)) // 2D
		if(noInt(yPonto, j,jj))
			return true;
		return false;						
  }

// Esse tem precalculo de 2d e 3d. Economiza calculos na impressao de cubos.
// deve ter habilitado "opPrecalc2d = true" p "ss C oo r d 3 d"
	public static boolean impPol2(Color crp, Color crc, Coord3 p, Coord3 pp, Coord3 ppp, Coord3 pppp){
		// ATENÇÃO!!! pntHitTri(x,y, i,j,ii,jj,iii,jjj) está disponivel!		
		impPolOk=false;


		
		// invalidos fora
		if(crp==null)	if(crc==null) return false;

		// vendo de costas
		// J.Coord2 c = J.fxy(p.x, p.y, p.z);
		// J.Coord2 cc = J.fxy(pp.x, pp.y, pp.z);
		if(p.X>pp.X) 
		if(!op2Side) 
			return false; // sair rapido

		// continuando e aproveitando coords jah calculadas
		// ISSO JAH FOI PRECALCULADO!
		// J.Coord2 ccc = J.fxy(ppp.x, ppp.y, ppp.z);
		// J.Coord2 cccc = J.fxy(pppp.x, pppp.y, pppp.z);
		
		boolean dentro = true;

		// coords validas, seguindo
		if(dentro){
			int m[] = {p.X, pp.X, ppp.X, pppp.X};
			int n[] = {p.Y, pp.Y, ppp.Y, pppp.Y};
			
			Color acor = null;// tem q ser fora do bloco abaixo p reutilizar a cor p contorno depois
			if(crp!=null){
				// o p I n c C r 3 d nao se aplica aqui

				if(opImpAlfa==0) acor = crp;
				else acor = J.altAlfa(crp, opImpAlfa);
			
				g.setColor(acor);
				g.fillPolygon(m,n, m.length);
				impPolOk=true;				
			}	
			
			// CONTORNO ESPECIAL
			// escurecer contornos quando crp==crc
			// p deixar sem contornos, faca crc=null
			if(crc!=null)
			if(crc==crp){
				g.setColor(acor.darker());
				g.drawPolygon(m,n, m.length);
			}

			// CONTORNO COMUM
			// normais, com cor diferente
			if(crc!=null)
			if(crc!=crp){ // jah tratado acima
				acor = null; // eh outra cor agora
				acor = crc;
		
				g.setColor(acor);					
				g.drawPolygon(m,n, m.length);								
			}
		}
		
		int i = (int)menor(p.X, pp.X, ppp.X, pppp.X);
		int ii =(int) maior(p.X, pp.X, ppp.X, pppp.X);
		int j = (int)menor(p.Y, pp.Y, ppp.Y, pppp.Y);
		int jj = (int)maior(p.Y, pp.Y, ppp.Y, pppp.Y);
		
		if(dentro)		
		if(opDebug){ // caixa de colisao
			g.setColor(Color.RED);
			g.drawRect(i,j, ii-i, jj-j);
		}
		if(dentro)
		if(noInt(xPonto, i,ii)) // 2D
		if(noInt(yPonto, j,jj))
			return true;
		return false;						
  }

// Idem acima, mas usa o buffer
	public static boolean bufPol(Color crp, Color crc, Coord3 p, Coord3 pp, Coord3 ppp, Coord3 pppp){
		if(bufImp==null) {
			// J.impErr("buffer de impressao nao inicializado");
			// o buffer demora um pouco p iniciar
			return false;
		}
		impPolOk=false;


		
		// invalidos fora
		if(crp==null)	if(crc==null) return false;

		// vendo de costas
		// J.Coord2 c = J.fxy(p.x, p.y, p.z);
		// J.Coord2 cc = J.fxy(pp.x, pp.y, pp.z);
		if(p.X>pp.X) 
		if(!op2Side) 
			return false; // sair rapido

		// continuando e aproveitando coords jah calculadas
		// ISSO JAH FOI PRECALCULADO!
		// J.Coord2 ccc = J.fxy(ppp.x, ppp.y, ppp.z);
		// J.Coord2 cccc = J.fxy(pppp.x, pppp.y, pppp.z);
		
		boolean dentro = true;

		// coords validas, seguindo
		if(dentro){
			int m[] = {p.X, pp.X, ppp.X, pppp.X};
			int n[] = {p.Y, pp.Y, ppp.Y, pppp.Y};
			
			Color acor = null;// tem q ser fora do bloco abaixo p reutilizar a cor p contorno depois
			if(crp!=null){
				// o p I n c C r 3 d nao se aplica aqui

				if(opImpAlfa==0) acor = crp;
				else acor = J.altAlfa(crp, opImpAlfa);
			
				grafBufImp.setColor(acor);
				grafBufImp.fillPolygon(m,n, m.length);
				impPolOk=true;				
			}	
			
			// CONTORNO ESPECIAL
			// escurecer contornos quando crp==crc
			// p deixar sem contornos, faca crc=null
			if(crc!=null)
			if(crc==crp){
				grafBufImp.setColor(acor.darker());
				grafBufImp.drawPolygon(m,n, m.length);
			}

			// CONTORNO COMUM
			// normais, com cor diferente
			if(crc!=null)
			if(crc!=crp){ // jah tratado acima
				acor = null; // eh outra cor agora
				acor = crc;
		
				grafBufImp.setColor(acor);					
				grafBufImp.drawPolygon(m,n, m.length);								
			}
		}

		// GARGALO!!!
		// daria p economizar clock habilitando isso somente quando necessario.
		// o mesmo p outras rotinas parecidas
		if(!opVerHit) return false;
		
		int i = (int)menor(p.X, pp.X, ppp.X, pppp.X);
		int ii =(int) maior(p.X, pp.X, ppp.X, pppp.X);
		int j = (int)menor(p.Y, pp.Y, ppp.Y, pppp.Y);
		int jj = (int)maior(p.Y, pp.Y, ppp.Y, pppp.Y);
		
		if(dentro)		
		if(opDebug){ // caixa de colisao
			grafBufImp.setColor(Color.RED);
			grafBufImp.drawRect(i,j, ii-i, jj-j);
		}
		if(dentro)
		if(noInt(xPonto, i,ii)) // 2D
		if(noInt(yPonto, j,jj))
			return true;
		return false;						
  }







		static int numCubImp=0;
		static boolean opDestCub=false;
		static boolean opEscurecerContorno=true;
	public static boolean impCub( float x, float y, float z, int crc, int crb, int crn, int crs, int crl, int cro, 	float c, float b, float n, float s, float l, float o){
		// impressao imperfeita mas suficiente

		// c=1 e b=1 fazem um cubo de 1 de altura, nao 2; mais intuitivo; nada de ficar colocando ponto decimal nos parametros
		c/=2f;			b/=2f;			n/=2f;
		s/=2f;			l/=2f;			o/=2f;
	
		J.Coord3 cen = new J.Coord3(x,y,z);
		
		if(opImpRefAgua){
			b=-b;
			c=-c;
		}
		
		J.Coord3 bno = cen.rel(-o,-b,+n);
		J.Coord3 bnl = cen.rel(+l,-b,+n); 
		J.Coord3 bso = cen.rel(-o,-b,-s);
		J.Coord3 bsl = cen.rel(+l,-b,-s);
		
		J.Coord3 cno = cen.rel(-o,+c,+n);
		J.Coord3 cnl = cen.rel(+l,+c,+n); 
		J.Coord3 cso = cen.rel(-o,+c,-s);
		J.Coord3 csl = cen.rel(+l,+c,-s);
		J.Coord3 cCen = cen.rel(0,+c,0);
		if(opLadeira!=0){
			int q=1;
			switch(opLadeira){
				case LES: cnl.y-=q; csl.y-=q; break;
				case OES: cno.y-=q; cso.y-=q; break;
				case NOR: cno.y-=q; cnl.y-=q; break;
				case SUL: cso.y-=q; csl.y-=q; break;
				default: esc("Ops! Parametro incorreto para J.opLadeira: "+opLadeira);
			}			
			opLadeira=0;
		}
		
		
		int crr=0;

		
	
		
		J.op2Side=true;

		int q=1;		
		float bb=0;
		if(opSombBaixaCanto) bb=b;		
		// nas laterais
		Coord3 cenN = cen.rel(0,-bb,+n);
		Coord3 cenS = cen.rel(0,-bb,-s);
		Coord3 cenL = cen.rel(+l,-bb,0);
		Coord3 cenO = cen.rel(-o,-bb,0);
		
		// em blocos vizinhos: sombra de rocha diferente de sombra de folhas
		opSombBaixaCanto=false; 		
		
		if(J.angX>=0) if(crb!=0) if(J.impPol(crb,crr, bno,bnl,bsl,bso)) dSel=BXO;
		// if(crc!=0) 

	
		if(opEscurecerContorno) crr=-1;
		
		if(J.impPol(crc,crr, cno,cnl,csl,cso)) dSel=CMA;
		
		if(crc!=0){
			if(J.opScn) impTri(crc-q,crr, cCen,cnl,cno);		
			if(J.opScs) impTri(crc-q,crr, cCen,csl,cso);		
			if(J.opScl) impTri(crc-q,crr, cCen,cnl,csl);		
			if(J.opSco) impTri(crc-q,crr, cCen,cno,cso);		
		}
		J.opScn=false;
		J.opScs=false;
		J.opScl=false;
		J.opSco=false;
		
		if(J.angX<0) if(crb!=0) if(J.impPol(crb,crr, bno,bnl,bsl,bso)) dSel=BXO;

		J.op2Side=false;
		
		impNorOk=false;// ficarah true se o pol estiver de frente e for imprimido por isso
		impSulOk=false;		
		impLesOk=false;		
		impOesOk=false;		

		
		
		if(J.impPol(crn,crr,  cnl, cno, bno, bnl)) dSel=NOR; //nor
		if(impPolOk) impNorOk=true;
		if(impPolOk)		
		if(crn!=0){
			if(J.opSnl) impTri(crn-q,crr, cnl,bnl, cenN);
			if(J.opSno) impTri(crn-q,crr, cno,bno, cenN);
		}
		opSnl=false;
		opSno=false;
		
		if(J.impPol(crs,crr,  cso, csl, bsl, bso)) dSel=SUL; //sul
		if(impPolOk) impSulOk=true;		
		if(impPolOk)				
		if(crs!=0){		
			if(J.opSsl) impTri(crs-q,crr, csl,bsl, cenS);
			if(J.opSso) impTri(crs-q,crr, cso,bso, cenS);			
		}
		opSsl=false;
		opSso=false;			
		
		if(J.impPol(crl,crr,  csl, cnl, bnl, bsl)) dSel=LES; //les
		if(impPolOk) impLesOk=true;				
		if(impPolOk)				
		if(crl!=0){
			if(J.opSln) impTri(crl-q,crr, cnl,bnl, cenL);
			if(J.opSls) impTri(crl-q,crr, csl,bsl, cenL);						
		}
		opSln=false;
		opSls=false;			

		if(J.impPol(cro,crr,  cno, cso, bso, bno)) dSel=OES; //oes			
		if(impPolOk) impOesOk=true;		
		if(impPolOk)				
		if(cro!=0){
			if(J.opSon) impTri(cro-q,crr, cno,bno, cenO);
			if(J.opSos) impTri(cro-q,crr, cso,bso, cenO);									
		}
		opSon=false;
		opSos=false;			
		
		
		
		numCubImp++;

		if(opDebug){
			Coord2 r = J.fxy(cen.x, cen.y, cen.z);
			g.setColor(J.cor[12]);
			g.drawRect(r.x-2,r.y-2,4,4);
		}
		
		
		if(opDestCub) {
			crr= 15+cont % 2;
			crr= 254;
			crn=0;			crs=0;
			crl=0;			cro=0;
			crb=0;			crc=0;
			switch(dSel){
				case 0:
				case NOR: crn=crr; break;
				case SUL: crs=crr; break;
				case LES: crl=crr; break;
				case OES: cro=crr; break;
				case CMA: crc=crr; break;
				case BXO: crb=crr; break;
			}
			opDestCub = false;
			if(J.C(3))
			impCub(x, y, z, 
				crc, 				crb,
				crn, 				crs, 
				crl, 				cro, 	
				2*c, 2*b, 2*n, 2*s, 2*l, 2*o);			
		}		
		
		if(dSel!=0) return true;
		return false;
	}
	
// ATUALIZANDO COM PARAMETROS "color". Fora a paleta "engessante"
		static boolean
			ocNor=false,
			ocSul=false,
			ocLes=false,
			ocOes=false,
			ocCma=false,
			ocBxo=false;
		static Color 
			crcEsp = null, // neve no topo, grama	
			crbEsp = null, // cores especiais p novo esquema de impressao
			crlEsp = null,
			croEsp = null,
			crnEsp = null,
			crsEsp = null;
		static int opContraste=1;

	public static void sai(){System.exit(0); }
	public static void sai(String st){
		// saindo com mensagem. Deve ajudar no debug...
		esc("================================");
		esc("");
		esc(st);
		esc("");
		esc("saindo...");
		sai();
	}


	static boolean opReciclaCoord = true;
	static J.Coord3  // p reciclar coords
		cen = null, cCen = null,
		cno = null, cnl = null, cso = null, csl=null,
		bno = null, bnl = null, bso = null, bsl=null;

	public static void zCoord(){
		// usado na reciclagem de coords
		// aponta q vai se iniciar a impressao de nova coluna de cubos em j o e C r a f t
		cen = null; cCen = null;
		cno = null; cnl = null; cso = null; csl=null;
		bno = null; bnl = null; bso = null; bsl=null;
	}
		
// ESQUEMA DE IMPRESSAO COM BUFFER		
		static float opPiramidal=0; // p impressao de pinheiros. Expandir isso p outras impressoes.
	public static boolean impCubBuf(Color cr, float x, float y, float z, float c, float b, float n, float s, float l, float o){
		// impressao imperfeita mas suficiente
	
		// melhor multiplicar 3 q dividir 6 abaixo
		// x*=2f;
		// y*=2f;
		// z*=2f;		
		x+=x; // somas mais rapidas que multiplicacoes
		y+=y;
		z+=z;
		
		// c=1 e b=1 fazem um cubo de 1 de altura, nao 2; mais intuitivo; nada de ficar colocando ponto decimal nos parametros
		// c/=2f;			b/=2f;			n/=2f;
		// s/=2f;			l/=2f;			o/=2f;
	
		if(opImpRefAgua){
			b=-b;
			c=-c;
		}	
	
		cen = new J.Coord3(x,y,z);
		
		if(opImpRefAgua){
			b=-b;
			c=-c;
		}
		
		boolean rec = opReciclaCoord;
		if(cno==null || cnl==null || cso == null || csl==null) rec = false;
		if(opPiramidal!=0) rec=false;
		
		if(rec){
			bno = cno;
			bnl = cnl;
			bso = cso;
			bsl = csl;
		} else {
			bno = cen.rel(-o,-b,+n);
			bnl = cen.rel(+l,-b,+n); 
			bso = cen.rel(-o,-b,-s);
			bsl = cen.rel(+l,-b,-s);
		}
		
		float u=0;
		if(opPiramidal!=0) u=opPiramidal*2;
		cno = cen.rel(-o-u,+c,+n+u);
		cnl = cen.rel(+l+u,+c,+n+u); 
		cso = cen.rel(-o-u,+c,-s-u);
		csl = cen.rel(+l+u,+c,-s-u);
		cCen = cen.rel(0,+c,0);
		if(opLadeira!=0){
			float q=1.6f;
			switch(opLadeira){ // deu merda aqui. hhhhhhhhhhh
				case LES: cnl.y-=q; csl.y-=q; break;
				case OES: cno.y-=q; cso.y-=q; break;
				case SUL: cno.y-=q; cnl.y-=q; break; // invertido mesmo ..
				case NOR: cso.y-=q; csl.y-=q; break;
				default: esc("Ops! Parametro incorreto para J.opLadeira: "+opLadeira);
			}			
			opLadeira=0;
		}
		
		// ajustar depois
		// int crr=0;
		// if(opDestCub) {
			// crn=crs=crl=cro=crb=crc=0;
			// crr=31-cont % (31-16);
		// }
		// "opFaceInterna" parece inocuo
		
		if(opDestCub){			
			int q = (cont %6)*60;
			q = J.corrInt(q,0,255);
			Color w = new Color(255,255,255, q);
			if(dSel==CMA) crcEsp= w;
			if(dSel==BXO) crbEsp= w;
			if(dSel==NOR) crnEsp= w;
			if(dSel==SUL) crsEsp= w;
			if(dSel==LES) crlEsp= w;
			if(dSel==OES) croEsp= w;
		}
		
		J.op2Side=true;

		int q=1;		
		// float bb=0;		
		// if(opSombBaixaCanto) bb=b; // sombra de rocha no chao eh triangulo reto, de folha da arvore eh isoceles (apenas 2 lados iguais(escaleno=tudo diferente; equilatero eh tudo igual))
		// // nas laterais
		// Coord3 cenN = cen.rel(0,-bb,+n);
		// Coord3 cenS = cen.rel(0,-bb,-s);
		// Coord3 cenL = cen.rel(+l,-bb,0);
		// Coord3 cenO = cen.rel(-o,-bb,0);
		
		// em blocos vizinhos: sombra de rocha diferente de sombra de folhas
		opSombBaixaCanto=false; 		
		
		// GARGALO!!!
		// p ajuste de claridade esta em impTri e impPol
		// se jah foi ajustado pela cor geral aqui, deve ser dispensado lah. 
		// deve ter um flag p isso
		
		Color acor=null, acorr=null;

		// BAIXO, serah repetido abaixo
		if(!ocBxo){ // ajuste especifico nos niveis inferiores. Poderia ser otimizado no geral aqui
				acor = null;
				acorr = null;
			if(crbEsp!=null) acor = crbEsp; // nao deve ser zerada
			else acor = altColor(cr,-4*opContraste);
				if(opEscurecerContorno) acorr = acor;	else acorr = null;	
				if(crZettaVision!=null) acorr = crZettaVision;
				if(opSemContorno) acorr=null;
				if(J.bufPol(acor,acorr, bno,bnl,bsl,bso)) 
					dSel=BXO;
			}
			
		// CIMA		
		if(!ocCma){
			acor = null;
			acorr = null;
		if(crcEsp!=null) acor = crcEsp; // nao deve ser zerada
		else acor = cr;
			if(J.sc) acor = altColor(acor, -opContraste-opContraste);
			if(opEscurecerContorno) acorr = acor;	else acorr = null;
			if(crZettaVision!=null) acorr = crZettaVision;			
			if(opSemContorno) acorr=null;			
			if(J.bufPol(acor,acorr, cno,cnl,csl,cso)) 
				dSel=CMA;
		}
		
		// SE TIVER SOMBRA PARCIAL ACIMA
		// veja q eh melhor desenhar esse detalhe depois
		// veja a frequencia desse detalhe nos blocos
		// nao compensa desmenbrar a impressao da face acima em 4 processos distintos
		// lembre dos cubos de solo plano q sao maioria
		// if(!ocCma){
			// int w = 0;
			// if(J.sc) w+=opContraste+opContraste;
			// acor = null;
		// if(crcEsp!=null) acor = altColor(crcEsp,-w-opContraste);
		// else acor = altColor(cr,-w);
			// // sem contorno aqui
			// if(J.opScn) impTri(acor, null, cCen,cnl,cno);		
			// if(J.opScs) impTri(acor, null, cCen,csl,cso);		
			// if(J.opScl) impTri(acor, null, cCen,cnl,csl);		
			// if(J.opSco) impTri(acor, null, cCen,cno,cso);		
		// }
		// J.opScn=false;
		// J.opScs=false;
		// J.opScl=false;
		// J.opSco=false;
		
		// BAIXO, acima tem outro igual a esse
		if(!ocBxo){ // ajuste especifico nos niveis inferiores. Poderia ser otimizado no geral aqui
				acor = null;
				acorr = null;
			if(crbEsp!=null) acor = crbEsp; // nao deve ser zerada
			else acor = altColor(cr,-4*opContraste);
				if(opEscurecerContorno) acorr = acor;	else acorr = null;	
				if(crZettaVision!=null) acorr = crZettaVision;				
				if(opSemContorno) acorr=null;				
				if(J.bufPol(acor,acorr, bno,bnl,bsl,bso)) 
					dSel=BXO; // esse tem q usar i m p P o l normal
			}

		J.op2Side=false;
		
		impNorOk=false;// ficarah true se o pol estiver de frente e for imprimido por isso
		impSulOk=false;		
		impLesOk=false;		
		impOesOk=false;		


		
		if(!ocNor){
			int w = opContraste*2;
			if(J.sn) w+=opContraste;					
			acor=null;
			acorr=null;
		if(crnEsp!=null) acor = crnEsp; // nao deve ser zerada
		else acor = altColor(cr,-w);			
			if(opEscurecerContorno) acorr = acor;
			if(crZettaVision!=null) acorr = crZettaVision;			
			if(opSemContorno) acorr=null;			
			if(J.bufPol(acor,acorr,  cnl, cno, bno, bnl)) 
				dSel=NOR; 				
			if(impPolOk) impNorOk=true;
			// if(impPolOk){ // sombras laterais parciais
				// acor = altColor(acor,-opContraste);
				// if(J.opSnl) impTri(acor,null, cnl,bnl, cenN);
				// if(J.opSno) impTri(acor,null, cno,bno, cenN);
			// }
		}
		opSnl=false;
		opSno=false;
		
		if(!ocSul){
			int w = opContraste*2;
			if(J.ss) w+=opContraste;							
			acor=null;
			acorr=null;
		if(crsEsp!=null) acor = crsEsp; // nao deve ser zerada
		else acor = altColor(cr,-w);						
			if(opEscurecerContorno) acorr = acor;			
			if(crZettaVision!=null) acorr = crZettaVision;			
			if(opSemContorno) acorr=null;			
			if(J.bufPol(acor,acorr,  cso, csl, bsl, bso)) 
				dSel=SUL; 
			if(impPolOk) impSulOk=true;		
			// if(impPolOk){ // sombras laterais parciais
				// acor = altColor(acor,-opContraste);			
				// if(J.opSsl) impTri(acor,null, csl,bsl, cenS);
				// if(J.opSso) impTri(acor,null, cso,bso, cenS);			
			// }
		}
		opSsl=false;
		opSso=false;			
		
		if(!ocLes){
			int w = opContraste;
			if(J.sl) w+=opContraste;							
			acor=null;
			acorr=null;
		if(crlEsp!=null) acor = crlEsp; // nao deve ser zerada
		else acor = altColor(cr,-w);
			if(opEscurecerContorno) acorr = acor;			
			if(crZettaVision!=null) acorr = crZettaVision;			
			if(opSemContorno) acorr=null;			
			if(J.bufPol(acor,acorr,  csl, cnl, bnl, bsl)) 
				dSel=LES; 
			if(impPolOk) impLesOk=true;				
			// if(impPolOk){ // sombras parciais laterais
				// acor = altColor(acor,-opContraste);			
				// if(J.opSln) impTri(acor,null, cnl,bnl, cenL);
				// if(J.opSls) impTri(acor,null, csl,bsl, cenL);						
			// }
		}	
		opSln=false;
		opSls=false;			

		if(!ocOes){
			int w = opContraste*3;
			if(J.so) w+=opContraste;							
			acor=null;
			acorr=null;
		if(croEsp!=null) acor = croEsp; // nao deve ser zerada
		else acor = altColor(cr,-w);
			if(opEscurecerContorno) acorr = acor;			
			if(crZettaVision!=null) acorr = crZettaVision;						
			if(opSemContorno) acorr=null;			
			if(J.bufPol(acor,acorr,  cno, cso, bso, bno)) 
				dSel=OES;
			if(impPolOk) impOesOk=true;		
			// if(impPolOk){  // sombras laterais parciais
				// acor = altColor(acor,-opContraste);			
				// if(J.opSon) impTri(acor,null, cno,bno, cenO);
				// if(J.opSos) impTri(acor,null, cso,bso, cenO);									
			// }
		}	
		opSon=false;
		opSos=false;			
		
		
		
		numCubImp++;

		if(opDebug){
			Coord2 r = J.fxy(cen.x, cen.y, cen.z);
			J.bufRetRel(null, J.cor[12], r.x-2,r.y-2,4,4);
		}
		
		// ocCma=false; // aqui no fim p nao bugar
		// ocBxo=false; // mas parece bugar mesmo assim
		// ocNor=false; // melhor ajustar no fonte original
		// ocSul=false;
		// ocLes=false;
		// ocOes=false;
		
		// sc=false; // nao deve ser alterado aqui
		// sn=false;
		// ss=false;
		// sl=false;
		// so=false;
		
		if(dSel!=0) return true;
		return false;
	}
		
		
// ESQUEMA DE IMPRESSAO com Color, FORA DO BUFFER
	public static boolean impCub(Color cr, float x, float y, float z, float c, float b, float n, float s, float l, float o){
		// impressao imperfeita mas suficiente
	
		// melhor multiplicar 3 q dividir 6 abaixo
		// x*=2f;
		// y*=2f;
		// z*=2f;		
		x+=x; // somas mais rapidas que multiplicacoes
		y+=y;
		z+=z;
		
		// c=1 e b=1 fazem um cubo de 1 de altura, nao 2; mais intuitivo; nada de ficar colocando ponto decimal nos parametros
		// c/=2f;			b/=2f;			n/=2f;
		// s/=2f;			l/=2f;			o/=2f;
	
		if(opImpRefAgua){
			b=-b;
			c=-c;
		}	
	
		cen = new J.Coord3(x,y,z);
		
		if(opImpRefAgua){
			b=-b;
			c=-c;
		}
		
		boolean rec = opReciclaCoord;
		if(cno==null || cnl==null || cso == null || csl==null) rec = false;
		if(opPiramidal!=0) rec=false;
		
		if(rec){
			bno = cno;
			bnl = cnl;
			bso = cso;
			bsl = csl;
		} else {
			bno = cen.rel(-o,-b,+n);
			bnl = cen.rel(+l,-b,+n); 
			bso = cen.rel(-o,-b,-s);
			bsl = cen.rel(+l,-b,-s);
		}
		
		float u=0;
		if(opPiramidal!=0) u=opPiramidal*2;
		cno = cen.rel(-o-u,+c,+n+u);
		cnl = cen.rel(+l+u,+c,+n+u); 
		cso = cen.rel(-o-u,+c,-s-u);
		csl = cen.rel(+l+u,+c,-s-u);
		cCen = cen.rel(0,+c,0);
		if(opLadeira!=0){
			float q=1.6f;
			switch(opLadeira){ // deu merda aqui. hhhhhhhhhhh
				case LES: cnl.y-=q; csl.y-=q; break;
				case OES: cno.y-=q; cso.y-=q; break;
				case SUL: cno.y-=q; cnl.y-=q; break; // invertido mesmo ..
				case NOR: cso.y-=q; csl.y-=q; break;
				default: esc("Ops! Parametro incorreto para J.opLadeira: "+opLadeira);
			}			
			opLadeira=0;
		}
		
		// ajustar depois
		// int crr=0;
		// if(opDestCub) {
			// crn=crs=crl=cro=crb=crc=0;
			// crr=31-cont % (31-16);
		// }
		// "opFaceInterna" parece inocuo
		
		if(opDestCub){			
			int q = (cont %6)*60;
			q = J.corrInt(q,0,255);
			Color w = new Color(255,255,255, q);
			if(dSel==CMA) crcEsp= w;
			if(dSel==BXO) crbEsp= w;
			if(dSel==NOR) crnEsp= w;
			if(dSel==SUL) crsEsp= w;
			if(dSel==LES) crlEsp= w;
			if(dSel==OES) croEsp= w;
		}
		
		J.op2Side=true;

		int q=1;		
		// float bb=0;		
		// if(opSombBaixaCanto) bb=b; // sombra de rocha no chao eh triangulo reto, de folha da arvore eh isoceles (apenas 2 lados iguais(escaleno=tudo diferente; equilatero eh tudo igual))
		// // nas laterais
		// Coord3 cenN = cen.rel(0,-bb,+n);
		// Coord3 cenS = cen.rel(0,-bb,-s);
		// Coord3 cenL = cen.rel(+l,-bb,0);
		// Coord3 cenO = cen.rel(-o,-bb,0);
		
		// em blocos vizinhos: sombra de rocha diferente de sombra de folhas
		opSombBaixaCanto=false; 		
		
		// GARGALO!!!
		// p ajuste de claridade esta em impTri e impPol
		// se jah foi ajustado pela cor geral aqui, deve ser dispensado lah. 
		// deve ter um flag p isso
		
		Color acor=null, acorr=null;

		// BAIXO, serah repetido abaixo
		if(!ocBxo){ // ajuste especifico nos niveis inferiores. Poderia ser otimizado no geral aqui
				acor = null;
				acorr = null;
			if(crbEsp!=null) acor = crbEsp; // nao deve ser zerada
			else acor = altColor(cr,-4*opContraste);
				if(opEscurecerContorno) acorr = acor;	else acorr = null;	
				if(crZettaVision!=null) acorr = crZettaVision;
				if(opSemContorno) acorr=null;
				if(J.impPol(acor,acorr, bno,bnl,bsl,bso)) 
					dSel=BXO;
			}
			
		// CIMA		
		if(!ocCma){
			acor = null;
			acorr = null;
		if(crcEsp!=null) acor = crcEsp; // nao deve ser zerada
		else acor = cr;
			if(J.sc) acor = altColor(acor, -opContraste-opContraste);
			if(opEscurecerContorno) acorr = acor;	else acorr = null;
			if(crZettaVision!=null) acorr = crZettaVision;			
			if(opSemContorno) acorr=null;			
			if(J.impPol(acor,acorr, cno,cnl,csl,cso)) 
				dSel=CMA;
		}
		
		// SE TIVER SOMBRA PARCIAL ACIMA
		// veja q eh melhor desenhar esse detalhe depois
		// veja a frequencia desse detalhe nos blocos
		// nao compensa desmenbrar a impressao da face acima em 4 processos distintos
		// lembre dos cubos de solo plano q sao maioria
		// if(!ocCma){
			// int w = 0;
			// if(J.sc) w+=opContraste+opContraste;
			// acor = null;
		// if(crcEsp!=null) acor = altColor(crcEsp,-w-opContraste);
		// else acor = altColor(cr,-w);
			// // sem contorno aqui
			// if(J.opScn) impTri(acor, null, cCen,cnl,cno);		
			// if(J.opScs) impTri(acor, null, cCen,csl,cso);		
			// if(J.opScl) impTri(acor, null, cCen,cnl,csl);		
			// if(J.opSco) impTri(acor, null, cCen,cno,cso);		
		// }
		// J.opScn=false;
		// J.opScs=false;
		// J.opScl=false;
		// J.opSco=false;
		
		// BAIXO, acima tem outro igual a esse
		if(!ocBxo){ // ajuste especifico nos niveis inferiores. Poderia ser otimizado no geral aqui
				acor = null;
				acorr = null;
			if(crbEsp!=null) acor = crbEsp; // nao deve ser zerada
			else acor = altColor(cr,-4*opContraste);
				if(opEscurecerContorno) acorr = acor;	else acorr = null;	
				if(crZettaVision!=null) acorr = crZettaVision;				
				if(opSemContorno) acorr=null;				
				if(J.impPol(acor,acorr, bno,bnl,bsl,bso)) 
					dSel=BXO; // esse tem q usar i m p P o l normal
			}

		J.op2Side=false;
		
		impNorOk=false;// ficarah true se o pol estiver de frente e for imprimido por isso
		impSulOk=false;		
		impLesOk=false;		
		impOesOk=false;		


		
		if(!ocNor){
			int w = opContraste*2;
			if(J.sn) w+=opContraste;					
			acor=null;
			acorr=null;
		if(crnEsp!=null) acor = crnEsp; // nao deve ser zerada
		else acor = altColor(cr,-w);			
			if(opEscurecerContorno) acorr = acor;
			if(crZettaVision!=null) acorr = crZettaVision;			
			if(opSemContorno) acorr=null;			
			if(J.impPol2(acor,acorr,  cnl, cno, bno, bnl)) 
				dSel=NOR; 				
			if(impPolOk) impNorOk=true;
			// if(impPolOk){ // sombras laterais parciais
				// acor = altColor(acor,-opContraste);
				// if(J.opSnl) impTri(acor,null, cnl,bnl, cenN);
				// if(J.opSno) impTri(acor,null, cno,bno, cenN);
			// }
		}
		opSnl=false;
		opSno=false;
		
		if(!ocSul){
			int w = opContraste*2;
			if(J.ss) w+=opContraste;							
			acor=null;
			acorr=null;
		if(crsEsp!=null) acor = crsEsp; // nao deve ser zerada
		else acor = altColor(cr,-w);						
			if(opEscurecerContorno) acorr = acor;			
			if(crZettaVision!=null) acorr = crZettaVision;			
			if(opSemContorno) acorr=null;			
			if(J.impPol2(acor,acorr,  cso, csl, bsl, bso)) 
				dSel=SUL; 
			if(impPolOk) impSulOk=true;		
			// if(impPolOk){ // sombras laterais parciais
				// acor = altColor(acor,-opContraste);			
				// if(J.opSsl) impTri(acor,null, csl,bsl, cenS);
				// if(J.opSso) impTri(acor,null, cso,bso, cenS);			
			// }
		}
		opSsl=false;
		opSso=false;			
		
		if(!ocLes){
			int w = opContraste;
			if(J.sl) w+=opContraste;							
			acor=null;
			acorr=null;
		if(crlEsp!=null) acor = crlEsp; // nao deve ser zerada
		else acor = altColor(cr,-w);
			if(opEscurecerContorno) acorr = acor;			
			if(crZettaVision!=null) acorr = crZettaVision;			
			if(opSemContorno) acorr=null;			
			if(J.impPol2(acor,acorr,  csl, cnl, bnl, bsl)) 
				dSel=LES; 
			if(impPolOk) impLesOk=true;				
			// if(impPolOk){ // sombras parciais laterais
				// acor = altColor(acor,-opContraste);			
				// if(J.opSln) impTri(acor,null, cnl,bnl, cenL);
				// if(J.opSls) impTri(acor,null, csl,bsl, cenL);						
			// }
		}	
		opSln=false;
		opSls=false;			

		if(!ocOes){
			int w = opContraste*3;
			if(J.so) w+=opContraste;							
			acor=null;
			acorr=null;
		if(croEsp!=null) acor = croEsp; // nao deve ser zerada
		else acor = altColor(cr,-w);
			if(opEscurecerContorno) acorr = acor;			
			if(crZettaVision!=null) acorr = crZettaVision;						
			if(opSemContorno) acorr=null;			
			if(J.impPol2(acor,acorr,  cno, cso, bso, bno)) 
				dSel=OES;
			if(impPolOk) impOesOk=true;		
			// if(impPolOk){  // sombras laterais parciais
				// acor = altColor(acor,-opContraste);			
				// if(J.opSon) impTri(acor,null, cno,bno, cenO);
				// if(J.opSos) impTri(acor,null, cso,bso, cenO);									
			// }
		}	
		opSon=false;
		opSos=false;			
		
		
		
		numCubImp++;

		if(opDebug){
			Coord2 r = J.fxy(cen.x, cen.y, cen.z);
			g.setColor(J.cor[12]);
			g.drawRect(r.x-2,r.y-2,4,4);
		}
		
		// ocCma=false; // aqui no fim p nao bugar
		// ocBxo=false; // mas parece bugar mesmo assim
		// ocNor=false; // melhor ajustar no fonte original
		// ocSul=false;
		// ocLes=false;
		// ocOes=false;
		
		// sc=false; // nao deve ser alterado aqui
		// sn=false;
		// ss=false;
		// sl=false;
		// so=false;
		
		if(dSel!=0) return true;
		return false;
	}
		static boolean opSemContorno=false; // cuidado, isso anula zettaVision
		static Color crZettaVision = null;
		static int opLadeira=0; // usado acima, em i.m.p.C.u.b. p distorcer face especifica nslo
		static boolean // sombras TOTAIS p cada face do cubo. Sombra causada por outro cubo acima
			sc=false, // bxo nao precisa
			sn=false,
			ss=false,
			sl=false,
			so=false; 
		static boolean // sombras parciais p cada face do cubo. Causada por outro cubo na lateral ou cmaLateral
				opSombBaixaCanto=false, // sombra de folha e de morro tem diferenca
				opSon=false,
				opSos=false,
				opSln=false,
				opSls=false,
				opSsl=false,
				opSso=false,
				opSnl=false,
				opSno=false,
				opScn=false, // aqui eh cima. baixo nao precisa
				opScs=false,
				opScl=false,
				opSco=false;
		static boolean
			impPolOk=false; // avisarah q o pol foi impresso e q nao estava de costas
		static boolean 
			impNorOk=false, // flag q avisa q a face tal do cubo foi imprimida normalmente jah q estava de frente
			impSulOk=false,
			impLesOk=false,
			impOesOk=false;
	
		static int dSel=0;
	
	public static String stDir(int p){
		switch(p){
			case NOR: return "NOR";  
			case NL: return "NL"; 
			case LES: return "LES";
			case SL: return "SL"; 
			case SUL: return "SUL";
			case SO: return "SO"; 
			case OES: return "OES";
			case NO: return "NO"; 
			case FRE: return "FRE";
			case DIR: return "DIR";
			case TRA: return "TRA";
			case ESQ: return "ESQ";
			case BXO: return "BXO";
			case CMA: return "CMA";
			case CEN: return "CEN";
		}
		return "???";
	}
	static final boolean SAVE=true, OPEN=false;
	static final int 
		NOR=1, 
		NL=2, 
		LES=3, 
		SL=4, 
		SUL=5, 
		SO=6, 
		OES=7, 
		NO=8,
		FRE=21,
		DIR=22,
		TRA=23,
		ESQ=24,
		BXO=31,
		CMA=32,
		CEN=33;
		
	public static void saveCam(String cam){
		J.escCfg("fov",J.fov,cam);
		J.escCfg("zoom",J.zoom,cam);
		J.escCfg("lmAO",J.lmAO,cam);
		J.escCfg("incZ",J.incZ,cam);
	}
	public static void openCam(String cam){
		J.fov = J.leCfgFloat("fov",cam);
		J.zoom = J.leCfgFloat("zoom",cam);
		J.lmAO = J.leCfgFloat("lmAO",cam);
		J.incZ = J.leCfgFloat("incZ",cam);
	}
	
	public static void iniCam(){ // ini3d
		J.rotCam(1,1,1);
		J.rotCam(-1,-1,-1);
		
		/* tente abaixo
		J.incZ=0;
		J.zoom=760;
		J.zCam=10;	
		J.opPiramidal = 0.00001f;
		*/
	}
	public static void iniCam(float x, float y, float z){
		J.xCam=x;
		J.yCam=y;
		J.zCam=z;
		iniCam();
	}
	public static void posCam(float qx, float qy, float qz){
		// posiciona camera
		// precisa ser unificado com rotCam, talves um rotCamRel()
		// veja: a unica diferenca entre este metodo e rotCam eh o incremento pelo sinal.
		// irma de rotCam (q incrementa o giro)
		// o metodo abaixo posiciona a camera sem incrementar o giro relativamente.
		float volta = (float)(Math.PI*2);
		
		if(qx!=0){// bxo/cma		
			angX=qx;
			float q=1.4f;
			if(angX>+q)angX=+q;
			if(angX<-q)angX=-q;
			// if(angX>volta)angX-=volta;
			// if(angX<    0)angX+=volta;			
			sinAngX= (float)Math.sin(angX);
			cosAngX= (float)Math.cos(angX);
		}
	
		if(qy!=0){// dir/esq
			angY=qy;
			if(angY>volta)angY-=volta;
			if(angY<    0)angY+=volta;
			sinAngY= (float)Math.sin(angY);
			cosAngY= (float)Math.cos(angY);
		}
		
		if(qz!=0){// torcao
			angZ=qz;
			if(angZ>volta)angZ-=volta;
			if(angZ<    0)angZ+=volta;			
			sinAngZ= (float)Math.sin(angZ);
			cosAngZ= (float)Math.cos(angZ);
		}		
	}
		final static float VOLTA = (float)(Math.PI*2);
	public static void rotCam(float qx, float qy, float qz){

		if(J.opInvHorBuf) qy=-qy; // nao ? o qx!
		
		if(qx!=0){// bxo/cma		
			angX+=qx;
			float q=1.4f;
			if(angX>+q)angX=+q;
			if(angX<-q)angX=-q;
			// if(angX>volta)angX-=volta;
			// if(angX<    0)angX+=volta;			
			sinAngX= (float)Math.sin(angX);
			cosAngX= (float)Math.cos(angX);
		}
	
		if(qy!=0){// dir/esq
			angY+=qy;
			if(angY>VOLTA)angY-=VOLTA;
			if(angY<    0)angY+=VOLTA;
			sinAngY= (float)Math.sin(angY);
			cosAngY= (float)Math.cos(angY);
		}
		
		if(qz!=0){// torcao
			angZ+=qz;
			if(angZ>VOLTA)angZ-=VOLTA;
			if(angZ<    0)angZ+=VOLTA;			
			sinAngZ= (float)Math.sin(angZ);
			cosAngZ= (float)Math.cos(angZ);
		}		
	}
	
		static float zoom = 1600f; // CUIDADO!! Este "zoom global" n?o foi usado s? nos fontes de JoeCraft. Teve um fonte sobre diep.io (jun/2021) q tb usou esta var. Tb tem referencias dela em "JMob.java"

		static int contRefAgua=0; // ondulacao sobre coordenadas funcionando independentemente de "cont". Tava dando problema de tempo irregular.
		static float 
			pTont=0, // efeitos de tontura: [0..1]; 0=sobrio
			vTont=2f; // velocidade de tontura: 2 = embriagado, 8 = efeito amarantos
		static float fov=2f, opLinhaDeCorte=1f;
			
		static float 
			lmAO=0f; // limite Atras do Olho, quando se omite a impressao por estar perto demais do observador.
		static int opQtdNoise=0;

		static Coord2 c=null; 

	public static float dist3d(float x, float y, float z, float xx, float yy, float zz){
		// mede a distancia entre dois pontos no espaco 3d
		// isto ficou excelente... claro, jah sao 2:40 da manha! Se for fora de hora, funciona muito bem. >:(		
		// p joeCraft deve ser usado:
		// 		return J.dist3d( -J.xCam,J.yCam,-J.zCam, xImp, yImp,zImp);
		// sendo q xImp:yImp:zImp eh a posicao absoluta final do cubo na "tela3d"
		float dx = xx-x, dy = yy-y, dz = zz-z;
		dx*=dx;
		dy*=dy;
		dz*=dz;
		return (float)(Math.sqrt(dx+dy+dz));
	}
	public static float dist2d(float x, float y, float xx, float yy){
		float dx = xx-x, dy = yy-y;
		dx*=dx;
		dy*=dy;
		return (float)(Math.sqrt(dx+dy));
	}
	public static float dist(float p, float pp){
		// distancia numa reta
		if(pp>p) return pp-p;
		return p-pp;
	}
	
		
	// este eh o coracao da programacao 3d	
	public static Coord2 fxy(float x, float y, float z){
		float xx=0, yy=0, zz=0;	 //fffffffffffff
		// if(opImpRefAgua) y+=0.5f; // problema com underWater

		if(fov!=1f)
			z*=fov;
		
		if(opQtdNoise!=0){
			// x+=RS(opQtdNoise)/100f; 
			// y+=RS(opQtdNoise)/100f; 
			// z+=RS(opQtdNoise)/100f; 	
			x+=(float)(RS(opQtdNoise)/100f); // 2^7=128, 2^6=64
			y+=(float)(RS(opQtdNoise)/100f);
			z+=(float)(RS(opQtdNoise)/100f);
		}
		
		y=-y;

	
		y+=yCam;
		x+=xCam;
		z+=zCam;

		// pode e deve ser otimizado
		
		// dir/esq
		xx = x*cosAngY-z*sinAngY;
		yy = y;
		zz = x*sinAngY+z*cosAngY;		

		// cma/bxo
		x = xx;
		y = yy*cosAngX-zz*sinAngX;
		z = yy*sinAngX+zz*cosAngX; 
		
		x+= incX;
		y+= incY;
		z+= incZ;		


		
		// torcao, inclinar; fica bom aqui p efeito de hurtJoe
		// bom, mas impede o uso de texturas em blocos, jah q ainda nao foi implementado. Dificil.
		// tambem deixa um pouco mais lento por causa das multiplicacoes
		// ??? nao teria como reter essa(s) multiplicacao(coes)?
		// seria bem impactante pq essa funcao eh muito frequente
		
		// xx = x*cosAngZ-y*sinAngZ;
		// yy = x*sinAngZ+y*cosAngZ;
		// zz = z;
		
		// x = xx+incX;
		// y = yy+incY;
		// z = zz+incZ;

		

		if(pTont>0){
			if(pTont>1)pTont=1;
			x*= (float)(1+pTont*0.4f*Math.sin(cont/(vTont)));
			y*= (float)(1+pTont*0.4f*Math.sin(cont/(vTont+1)));
			// abaixo nao precisa
			// z+= (float)(1+pTont*Math.sin(cont/7f));
		}	

		
		c = new Coord2();

		// if(opProfund)
		if(z<=0) z=0.0001f;

		// x+x melhor q x*2
		// c.x=J.xPonto+(int)(((x)/z)*zoom);
		// c.y=J.yPonto+(int)(((y)/z)*zoom);
		c.x=J.xPonto+(int)((x*zoom/z));
		c.y=J.yPonto+(int)((y*zoom/z));
		// c.y+=(int)(J.angX*-100f);
		

		// if(c.x<-100) c.x=-100;
		// if(c.x>J.maxXf+100) c.x=J.maxXf+100;
		// if(c.y<-100) c.y=-100;
		// if(c.y>J.maxYf+100) c.y=J.maxYf+100;
		
		if(opImpRefAgua){
			float u = (float)(Math.cos((int)(contRefAgua+x+y+z))*3);
			c.y-= u;
			// c.y+= Math.cos(cont);
		}
		
		c.ao= (z<opLinhaDeCorte);		
		int margemH = 300;
		int margemV = 600;
		// fr = fora da tela
		if (c.x<-margemH) c.frh=true; 
		if (c.y<-margemV) c.frv=true; 
		if (c.x>J.maxXf+margemH) c.frh=true;  
		if (c.y>J.maxYf+margemV) c.frv=true;
		c.fr = (c.frh || c.frv || c.ao);
		return c;
	}	
	
		static boolean opImpRefAgua=false;
		
		static int tmpCrCint=1; // usado por J3d.java. p impressao de cores cintilantes.
		static boolean polFora = false;	
		static float xCam=0, zCam=0; // tb foram usadas estas tres vars xCam em JMob.java.
		static float yCam=0, V=0;	// v p testes
/* ATENCAO: VEJA A DIFERENCA ENTRE "xCam" e "incX":
	incX: incremento DEPOIS de rotacionado
	xCam: incremento ANTES de rotacionar; Use p deslocamento fino do passo em JoeCraft

*/		

		
	public static int fx(float ii, float jj, float kk){
		Coord2 c = fxy(ii,jj,kk);
		return c.x;
	}	
	public static int fy(float ii, float jj, float kk){
		Coord2 c = fxy(ii,jj,kk);
		return c.y;
	}		
		
	// static int // p reter contexto 3d; impressao da mao do joe
			// c_xPonto=J.xPonto,
			// c_yPonto=J.yPonto;
	// static float 
			// c_zoom=J.zoom,
			// c_fov=J.fov,
			// c_incX=J.incX,
			// c_incY=J.incY,
			// c_incZ=J.incZ,
			// c_sinAngX = J.sinAngX,
			// c_cosAngX = J.cosAngX,
			// c_sinAngY = J.sinAngY,
			// c_cosAngY = J.cosAngY,
			// c_sinAngZ = J.sinAngZ,
			// c_cosAngZ = J.cosAngZ;			

			
	// public static void getSetContex3d(boolean grd){ // p 3d; mao do joe
		// if(grd){ // guardar contexto 3d
			// c_xPonto = J.xPonto;
			// c_yPonto = J.yPonto;
			// c_zoom = J.zoom;
			// c_fov = J.fov;
			// c_incX = J.incX;
			// c_incY = J.incY;
			// c_incZ = J.incZ;
			// c_sinAngX = J.sinAngX;
			// c_cosAngX = J.cosAngX;
			// c_sinAngY = J.sinAngY;
			// c_cosAngY = J.cosAngY;
			// c_sinAngZ = J.sinAngZ;
			// c_cosAngZ = J.cosAngZ;			
		// } else { // restaurar contexto 3d
			// J.xPonto = c_xPonto;
			// J.yPonto = c_yPonto;
			// J.zoom = c_zoom;
			// J.fov = c_fov;
			// J.incX = c_incX;
			// J.incY = c_incY;
			// J.incZ = c_incZ;
			// J.sinAngX = c_sinAngX;
			// J.cosAngX = c_cosAngX;
			// J.sinAngY = c_sinAngY;
			// J.cosAngY = c_cosAngY;
			// J.sinAngZ = c_sinAngZ;
			// J.cosAngZ = c_cosAngZ;			
		// }	
	// }	
	
// ========================================	
	
	public static void iniPlt16(){
		int c=0;
		for(int k=16; k<32; k++){
			J.cor[k]= new Color(c,c,c);
			c+=17;
		}
		// c*16=255; c=16/255;
		J.cor[ 0]= new Color(000,000,000);
		J.cor[ 1]= new Color(000,000,127);
		J.cor[ 2]= new Color(000,127,000);
		J.cor[ 3]= new Color(000,127,127);
		J.cor[ 4]= new Color(127,000,000);
		J.cor[ 5]= new Color(127,000,127);
		J.cor[ 6]= new Color(127,127,000);
		J.cor[ 7]= new Color(127,127,127);
		
		J.cor[ 8]= new Color(064,064,064);
		J.cor[ 9]= new Color(000,000,255);
		J.cor[10]= new Color(000,255,000);
		J.cor[11]= new Color(064,255,255);
		J.cor[12]= new Color(255,000,000);
		J.cor[13]= new Color(255,000,255);
		J.cor[14]= new Color(255,255,000);
		J.cor[15]= new Color(255,255,255);
		
		
	}
    
	public static int iniPlt(){
		// o caminho padrao p paletas antigas
		//return iniPltAnt("c:\\java\\jf1\\joe3d_08.plt");    
      
		return iniPltAnt("jf1/joe3d_08.plt");
	}	
	public static int iniPlt(String cam){
    cam = emMinusc(cam);
		if(assArq(cam).equals("PL2")) saveOpenPlt2(false, cam);
		else iniPltAnt(cam);
		return 0; // gambiarra p cortar fila no carregamento
	}	
	public static int iniPltAnt(String cam){
    if(!J.arqExist(cam))
      J.impErr("!arquivo perdido: !"+cam+"!");
    if(tem(':',cam)) J.impErr("!(j) erro de caminho de sistema de arquivos. Nao estamos mais no windows!!!... (j)");
		// paleta no formato antigo
		J.cor = new Color[512+1];
		FileInputStream ler=null;
		iniPlt16();
		try {
			int r=0, g=0, b=0;
			ler = new FileInputStream(cam);
			// a assinatura antiga = "PL1"
			r=ler.read();	r=ler.read();	r=ler.read();
			// 63*c=255 -> c=255/63
			
			
			float q=4f; // contraste aqui
			int qq=0; // brilho aqui
					
			
			for(int k=32; k<256; k++){
					r=ler.read();
					g=ler.read();
					b=ler.read();
				r = (int)(r*q)+qq;
				g = (int)(g*q)+qq;
				b = (int)(b*q)+qq;
				r = corrInt(r, 0,255);
				g = corrInt(g, 0,255);
				b = corrInt(b, 0,255);
				J.cor[k]= new Color(r,g,b);
			}	
			
			// provisorio p paleta extendida
			// tttttttttttt
			for(int k=256; k<=512; k++)
				J.cor[k]= new Color(J.R(255),J.R(255),J.R(255));
			
			ler.close();
		} catch(IOException e){
			J.esc("Ops! Erro abrindo a paleta. (J.iniPlt())");
      e.printStackTrace();      
		} finally {
			try{ ler.close(); } catch(IOException e){}
		}		
		return 0;		
	}	

		static String camBkpPlt = "bkp.plt";
	public static void saveOpenPlt2(boolean s, String cam){
		if(!s)
		if(!arqExist(cam)){
			impErr("!arquivo perdido: "+cam);
			return;
		}
		
		if(s)
		if(arqExist(cam)){
			// serah q isso funciona???
			delArq(camBkpPlt);
			renameArq(cam,camBkpPlt);
			saveOpenPlt2(true, cam);
		}
			
			
		RandomAccessFile arq = null;
		try{
			arq = new RandomAccessFile(cam, "rw");
			// assinatura
			if(s){
				arq.writeChar('P');
				arq.writeChar('L');
				arq.writeChar('2');
			} else {
				char c = arq.readChar(); 
				char cc = arq.readChar();
				char ccc = arq.readChar();
				if(c!='P' || cc!='L' || ccc!='2') {
					J.esc("falha abrindo arquivo de paleta. A assinatura nao confere: "+cam);
					return;
				}
			}


			int r=0, g=0, b=0;
			for(int q=0; q<=512; q++){
				if(s){
					arq.writeChar((char)J.cor[q].getRed());
					arq.writeChar((char)J.cor[q].getGreen());
					arq.writeChar((char)J.cor[q].getBlue());
				} else {
					r=(int)arq.readChar();
					g=(int)arq.readChar();
					b=(int)arq.readChar();					
					J.cor[q] = new Color(r,g,b);
				}
			}
			arq.close();
			J.esc("paleta ok: "+cam);			
		} catch(IOException e){
			try{ arq.close(); } catch(IOException ee){}
		}
	}		
	

	public static int rndCr9(){
		return (r.nextInt(21)+4)*10+9; // 4..24 * 10+9
	}
	
	public static Color rndCor(){													
		return cor[r.nextInt(256)];
	}

  public static int graf(Graphics gg){
		Graphics2D g2=(Graphics2D)gg;
	  RenderingHints rh = new RenderingHints(
		  RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);			
		rh.put(
		  RenderingHints.KEY_RENDERING,
			RenderingHints.VALUE_RENDER_QUALITY);
		g2.setRenderingHints(rh);
		return 0;
	}
	
	public static void impErr(String st, String stm){
		String c="<><><><><><><><><><><><><><><><>";
		esc(" ");	esc(c);	esc(" ");
		esc("Ops!");
		esc(" ");
		esc(st);
		esc(stm);
		esc(" ");	esc(c);	esc(" ");
		if(st.charAt(0)=='!') J.sai();
	}
	public static void impErr(String st){
		impErr(st,"");
	}
	
	public static int esc(String st){ // void esc(
    System.out.println(st);
		return 0; // pequena gambiarra
		// poderia ser tb sem pular linhas:
    // System.out.print(p);		
  }  
	public static int escl(String st){
    System.out.print(st);
		return 0; 
  }  	
  
  public static void delay(int p){
	  try {Thread.sleep(p);} catch(Exception e){ }  
  }
/* DELIMITADORES P SCANNER
https://www.javatpoint.com/post/java-scanner-usedelimiter-method#:~:text=The%20useDelimiter()%20is%20a,Scanner%20useDelimiter(Pattern%20pattern)%20Method


import java.util.Scanner;    
public class ScannerUseDelimiterExample2 {    
     public static void main(String args[]){   
            // Initialize Scanner object  
            Scanner scan = new Scanner("JavaTpoint/Abhishek/Male/22");  
            //Initialize the string delimiter  
            scan.useDelimiter("/");  
            //Printing the tokenized Strings  
            while(scan.hasNext()){  
                System.out.println(scan.next());  
            }  
            scan.close();   
          }    
}     

Output:

JavaTpoint
Abhishek
Male
22

.......................
OUTRO EXEMPLO:

import java.util.Scanner;    
public class ScannerUseDelimiterExample3 {    
     public static void main(String args[]){       
           String input = "1 fish 2 fish red fish blue fish";          
           // \\s* means 0 or more repetitions of any whitespace character   
           // fish is the pattern to find  
           @SuppressWarnings("resource")  
       Scanner sc = new Scanner(input).useDelimiter("\\s*fish\\s*");       
           System.out.println(sc.nextInt());   // prints: 1  
           System.out.println(sc.nextInt());   // prints: 2  
           System.out.println(sc.next());      // prints: red  
           System.out.println(sc.next());      // prints: blue         
           //close the scanner  
           sc.close();       
         }    
}     
Output:

1
2
red
blue

*/	
  public static int leInt(){
    Scanner s = new Scanner(System.in);
	return s.nextInt();
  }
  public static int leInt(String st){
    System.out.print(st);
    Scanner s = new Scanner(System.in);
		return s.nextInt();
  }
	public static String leStr(String p){
	  esc(p);
	  Scanner s = new Scanner(System.in);
		return s.nextLine();
		// existe tb nextInt e similares
		// hasNextLine()
	}
	
	public static int charEmInt(char p){
	  int q=0;
	  switch(p){
		  case '1': q=1; break;
		  case '2': q=2; break;
		  case '3': q=3; break;
		  case '4': q=4; break;
		  case '5': q=5; break;
		  case '6': q=6; break;
		  case '7': q=7; break;
		  case '8': q=8; break;
		  case '9': q=9; break;
		}
		return q;
	}	
	public static boolean ehDig(char p){
		boolean q=false;
		switch(p){
			case '0': case '1': case '2':
			case '3': case '4': case '5':
			case '6': case '7': case '8':
			case '9': q=true; break;
		}
		return q;
	}
	public static boolean ehLetra(char p){		
		boolean eh=false;
		switch(emMaiusc(p)){
			case 'A':
			case 'B':
			case 'C':
			case 'D':
			case 'E':
			case 'F':
			case 'G':
			case 'H':
			case 'I':
			case 'J':
			case 'K':
			case 'L':
			case 'M':
			case 'N':
			case 'O':
			case 'P':
			case 'Q':
			case 'R':
			case 'S':
			case 'T':
			case 'U':
			case 'V':
			case 'W':
			case 'X':
			case 'Y':
			case 'Z': eh=true; break;
		}
		return eh;
	}
	public static char emMinusc(char p){ 
		switch(p){
			case 'A': return 'a';
			case 'B': return 'b'; 
			case 'C': return 'c'; 
			case 'Ç': return 'ç'; 			
			case 'D': return 'd'; 
			case 'E': return 'e'; 
			case 'F': return 'f';  
			case 'G': return 'g';  
			case 'H': return 'h';  
			case 'I': return 'i';  
			case 'J': return 'j';  
			case 'K': return 'k';  
			case 'L': return 'l';  
			case 'M': return 'm';  
			case 'N': return 'n';  
			case 'O': return 'o';  
			case 'P': return 'p';  
			case 'Q': return 'q';  
			case 'R': return 'r';  
			case 'S': return 's';  
			case 'T': return 't';  
			case 'U': return 'u';  
			case 'V': return 'v';  
			case 'W': return 'w';  
			case 'X': return 'x';  
			case 'Y': return 'y';  
			case 'Z': return 'z';  
		}
		return p;
	}	
	public static char emMaiusc(char p){ 
		switch(p){
			case 'á': p='Á'; break;
			case 'à': p='À'; break;
			case 'ã': p='Ã'; break;
			case 'â': p='Â'; break;
			case 'é': p='É'; break; // mais depois
			
			case 'a': p='A'; break;
			case 'b': p='B'; break;
			case 'c': p='C'; break;
			case 'ç': p='Ç'; break;
			case 'd': p='D'; break;
			case 'e': p='E'; break;
			case 'f': p='F'; break;
			case 'g': p='G'; break;
			case 'h': p='H'; break;
			case 'i': p='I'; break;
			case 'j': p='J'; break;
			case 'k': p='K'; break;
			case 'l': p='L'; break;
			case 'm': p='M'; break;
			case 'n': p='N'; break;
			case 'o': p='O'; break;
			case 'p': p='P'; break;
			case 'q': p='Q'; break;
			case 'r': p='R'; break;
			case 's': p='S'; break;
			case 't': p='T'; break;
			case 'u': p='U'; break;
			case 'v': p='V'; break;
			case 'w': p='W'; break;
			case 'x': p='X'; break;
			case 'y': p='Y'; break;
			case 'z': p='Z'; break;
		}
		return p;
	}
	public static String emMaiusc(String p){
		// deve ser ajustado aqui:
		// st.toUpperCase() e st.toLowerCase()
		if(p==null) return null;
		String ret="";
		for(int k=0; k<p.length(); k++){
			ret+=J.emMaiusc(p.charAt(k));
		}
		return ret;
	}
	public static String primMaiusc(String p){
		char ca=' ', ch=' ';
		String st = "";
		for(int q=0; q<p.length(); q++){
			ch = p.charAt(q);
			if(ca==' ') st+= J.emMaiusc(ch);
			else st+= J.emMinusc(ch);
			ca = ch;
		}
		return st;
	}
	public static String emMinusc(String p){
		if(p==null) return null;
		String ret="";
		for(int k=0; k<p.length(); k++){
			ret+=J.emMinusc(p.charAt(k));
		}
		return ret;		
	}
	
	public static int abs(int p){
		if (p<0)p=-p;
		return p;
	}
	public static long abs(long p){
		if (p<0)p=-p;
		return p;
	}
	public static float abs(float p){
		if (p<0)p=-p;
		return p;
	}	

	public static boolean ehNum(char ca){
		// mas nao contei ponto/virgula decimal aqui. Nao sei se isso eh bom.
		if(tem(ca,"0123456789")) 
			return true;
		return false;		
	}
	public static boolean ehNum(String st){
		for(int q=0; q<st.length(); q++)
			if(!tem(st.charAt(q),"0123456789.,")) 
				return false;
		return true;		
	}
	public static String remInicio(int n, String p){
		if(n<=0) return p;
		if(p==null) return p;
		if(n>=p.length()) return null;		
		String st="";
		for(int q=n; q<p.length(); q++)
			st+=p.charAt(q);
		return st;
		
	}

// === CORES =========================================
	public static int colorInt(int r, int g, int b, int a){
		// cria uma cor ***COM ALFA*** a partir dos 4 parametros acima
		// todos os parametros: 0..255
		return (a & 0xff) << 24 | (r & 0xff) << 16 | (g & 0xff) << 8 | (b & 0xff);
	}
	public static Color stEmColor(String st){
		if(st==null) return null;
		if(st.equals("")) return null;
		J.guardaTokens();
		J.extTokens(st);
//J.esc("===========RGB: "+J.tk1+" "+J.tk2+" "+J.tk3+" "+J.tk4);		
		Color c = new Color(
				J.stEmInt(J.tk1), 
				J.stEmInt(J.tk2), 
				J.stEmInt(J.tk3));
		if(!J.tk4.equals("")) 
			c = J.altAlfa(c, J.stEmInt(J.tk4));
		J.restauraTokens();
		return c;
	}
	public static String colorEmSt(Color cr){
		if(cr==null) return "null";
		// if(cr==null) J.impErr("!cor nula!!!", "J.colorEmStc()");
		String st="";
		st+= J.intEmSt000(cr.getRed())+" ";
		st+= J.intEmSt000(cr.getGreen())+" ";
		st+= J.intEmSt000(cr.getBlue())+" ";
		st+= J.intEmSt000(cr.getAlpha());
		return st;
	}
	public static String colorEmStc(Color cr){
		// CUIDADO, ESSE EH COMPACTO: 000000000000 E NAO "000 000 000 000"
		if(cr==null) return "null";		
		// if(cr==null) J.impErr("!cor nula!!!", "J.colorEmStc()");
		String st="";
		st+= J.intEmSt000(cr.getRed());
		st+= J.intEmSt000(cr.getGreen());
		st+= J.intEmSt000(cr.getBlue());
		st+= J.intEmSt000(cr.getAlpha());
		return st;
	}
	public static Color stcEmColor(String st){
		// essa cor eh compactada. Ex: 000255000127
		if(st==null) return null;
		if(st.equals("")) return null;
		// Color c = new Color(
				// J.stEmInt(J.tk1), 
				// J.stEmInt(J.tk2), 
				// J.stEmInt(J.tk3));
		// if(!J.tk4.equals("")) 
			// c = J.altAlfa(c, J.stEmInt(J.tk4));
		// J.restauraTokens();
		if(st.length()<8) {
			J.impErr("string de cor fora do formato: '"+st+"'","J.stcEmColor()");
			return J.cor[12];
		}
		int r = stEmInt(""+st.charAt(0)+st.charAt(1)+st.charAt(2));
		int g = stEmInt(""+st.charAt(3)+st.charAt(4)+st.charAt(5));
		int b = stEmInt(""+st.charAt(6)+st.charAt(7)+st.charAt(8));
		int a = 255; // 255 = opaco ao maximo, 0 eh transparente
		if(st.length()>=11)
			a = stEmInt(""+st.charAt(9)+st.charAt(10)+st.charAt(11));
		Color c = new Color(r,g,b,a);
		return c;
	}
	// public static String colorEmHexa(Color cr) // deve aceitar tb "FFF"(nota??o resumida) e "FFFA"(resumida com alfa)
	// public static Color hexaEmColor(String st)

	public static String compStChar(String p, char ca, int len){
		// completa o string com o char ca ateh alcancar o tamanho len
		if(p.length()>=len) return p;
		for(int q=0; q<9000; q++){
			p+=""+ca;			
			if(p.length()>=len) return p;
		}
		return p;			
	}
	public static String compStCharE(String p, char ca, int len){
		// insere o char ca a esq do string ateh q atinja o tamanho len
		if(p.length()>=len) return p;
		for(int q=0; q<9000; q++){
			p=""+ca+p;
			if(p.length()>=len) return p;
		}
		return p;			
	}
	
	public static float stEmFloat(String p){
		// depois
		// fazer igual a stEmInt()
		// nao quero q interrompa o programa
		if(p==null) return 0;
		if(J.iguais(p,"")) return 0;
		if(J.iguais(p,"NAN")) return 0;
		p = trocaChar(',','.',p);
		return Float.parseFloat(p);
	}
	

	public static int stEmInt(String p){
		// essa acaba sendo melhor q com parseInt pq o programa nao trava quando dah erro de formato.
		// com p="-12 34", o retorno serah "-12"; O segundo numero sempre eh ignorado.
		// repare q o string eh invertido no calculo.
		// bug: se existir dois "-" no string o numero ficarah positivo. Isso nao eh bom, mas acho q nao trarah problemas.		
		/* TESTES: 
		J.esc(""+stEmInt("1"));
		J.esc(""+stEmInt("12"));
		J.esc(""+stEmInt("1234"));
		J.esc(""+stEmInt("+12"));
		J.esc(""+stEmInt("-15"));
		J.esc(""+stEmInt("-16 12"));
		J.esc(""+stEmInt("-17,12"));
		J.esc(""+stEmInt("-18,456"));
		J.esc(""+stEmInt("+19,94"));		
		*/
		p = J.truncarAte(' ',p);
		p = J.truncarAte(',',p);
		p = J.truncarAte('.',p);
		p = J.invStr(p);
		p = J.limpaSt(p);
		int md=1, num=0;
		char ca=0;
		for(int q=0; q<p.length(); q++){
			ca = p.charAt(q);
			switch(ca){
				case '.':
				case ';':
				case ',':
				case ' ': return num;
				case '+': break;
				case '-': num*=-1; break;
				case '0': md*=10; break;
				case '1': num+= md*1; md*=10; break;
				case '2': num+= md*2; md*=10; break;
				case '3': num+= md*3; md*=10; break;
				case '4': num+= md*4; md*=10; break;
				case '5': num+= md*5; md*=10; break;
				case '6': num+= md*6; md*=10; break;
				case '7': num+= md*7; md*=10; break;
				case '8': num+= md*8; md*=10; break;
				case '9': num+= md*9; md*=10; break;
			}			
		}
		return num;
	}
	public static String intEmSt(int p){
		return Integer.toString(p);
	}
	public static String floatEmSt(float p){
		return Float.toString(p);
	}	
	public static String floatEmSt_000_00(float p){
		// 1.23456f ficaria "+001.23"
		// parece funcionar bem, mas precisa testar mais

		int inte=(int)p;
		float dec=p-inte;
		String st = J.intEmSt_000(inte);
		
		if(dec==0) return st+".00";
		
		String std=""+dec+"00000000";
		st+="."+std.charAt(2)+std.charAt(3);		
		//J.sai("<<<<<<<<"+st);
		return st;
	}		
/*	TAH MAIS P BAIXO
	public static float stEmFloat(String p){
		return Float.parseFloat(p);
	}	
*/
	public static String intEmSt00(int p){
		return intEmSt00(p,'0');
	}
	public static String intEmSt00(int p, char ca){
		String st = Integer.toString(p);
		if (st.length()<2)st=ca+st;
		if (st.length()<2)st=ca+st;
		return st;
	}	
	public static String intEmSt000(int p){
		return intEmSt000(p,'0');
	}
	public static String intEmSt000(int p, char ca){
		String st = Integer.toString(p);
		if (st.length()<3)st=ca+st;
		if (st.length()<3)st=ca+st;
		if (st.length()<3)st=ca+st;
		return st;
	}
	public static String intEmSt0000(int p){
		boolean ng = false;
		
		if(p<0){
			p = -p;
			ng = true;
		}		
		
		String st = Integer.toString(p);
		char ca = '0';
		if (st.length()<4)st=ca+st;
		if (st.length()<4)st=ca+st;
		if (st.length()<4)st=ca+st;
		if (st.length()<4)st=ca+st;
		
		if(ng)st="-"+st;
		
		return st;
	}	
	public static String intEmSt_000(int p){
		// sempre com sinal a esq
		return intEmSt_000(p,'0');		
	}
	public static String intEmSt_000(int p, char ca){
		// sempre com sinal a esq	
		String st = Integer.toString(J.abs(p));
		if (st.length()<3)st=ca+st;
		if (st.length()<3)st=ca+st;
		if (st.length()<3)st=ca+st;
		if(p<0)st="-"+st; else st="+"+st;
		return st;
	}		
	public static String intEmSt_0000(int p){
		// sempre com sinal a esq
		// usado nos nomes de arq de area de joeCraft
		return intEmSt_0000(p,'0');
	}
	public static String intEmSt_0000(int p, char ca){
		// sempre com sinal a esq	
		String st = Integer.toString(J.abs(p));
		if (st.length()<4)st=ca+st;
		if (st.length()<4)st=ca+st;
		if (st.length()<4)st=ca+st;
		if (st.length()<4)st=ca+st;
		if(p<0)st="-"+st; else st="+"+st;
		return st;
	}	
	public static String intEmSt_000000(int p){
		// sempre com sinal a esq
		// usado nos nomes de arq de area de joeCraft
		return intEmSt_000000(p,'0');
	}
	public static String intEmSt_000000(int p, char ca){
		// sempre com sinal a esq	
		String st = Integer.toString(J.abs(p));
		if (st.length()<6)st=ca+st;
		if (st.length()<6)st=ca+st;
		if (st.length()<6)st=ca+st;
		if (st.length()<6)st=ca+st;
		if (st.length()<6)st=ca+st;
		if (st.length()<6)st=ca+st;
		if(p<0)st="-"+st; else st="+"+st;
		return st;
	}		
	public static String stInd(int i, String st0, String st1, String st2, String st3){
		if(i==0) return st0;
		if(i==1) return st1;
		if(i==2) return st2;
		return st3;
	}

// === MANIPULACAO DE ARQUIVOS BINARIOS ====
	
	public static boolean copArq(String fonte, String dest){
		try{
			Path FROM = Paths.get(fonte);
			Path TO = Paths.get(dest);
			//sobrescreve dest caso já exista
			CopyOption[] options = new CopyOption[]{
				StandardCopyOption.REPLACE_EXISTING,
				StandardCopyOption.COPY_ATTRIBUTES
			}; 
			Files.copy(FROM, TO, options);	
			return true;
		} catch(IOException e){
			J.impErr("!falha copiando arquivo: |"
				+fonte+"|  para   |"
				+dest+"|","J.copArq()");;
			return false;
		}
	}	
	public static boolean ehArq(String cam){
		// melhor testar isso. Nao sei se tah funcionando.
		if(cam==null) return false;
		File arq = new File(cam);
		// if(!arq.exists()) return false;
		if(arq.isDirectory()) return false;		
		if(arq.isFile()) return true;
		return false;
	}
	public static boolean ehDiretorio(String cam){
		// melhor testar isso. Nao sei se tah funcionando.		
		if(cam==null) return false;
		File arq = new File(cam);
		// if(!arq.exists()) return false;
		if(arq.isFile()) return false;		
		if(arq.isDirectory()) return true;
		return false;
	}	
	public static boolean mkDir(String p){
		// cria o diretorio informado em "p"
		// lembre: "maps//mundo1//*.frg";
		// retorna true se o diretorio foi criado
		return new File(p).mkdir();
	}
	public static boolean arqExist(String cam){
		if(cam==null) return false;
		File arq = new File(cam);
		return arq.exists();
	}
	public static void delArq(String cam){
		if(!J.arqExist(cam)) J.impErr("!impossivel deletar: arquivo perdido: ["+cam+"]","J.delArq()");
		File arq = new File(cam);
		arq.delete();
	}
	public static void renameArq(String ant, String nov){
		// precisa ser melhor testada
		if(!J.arqExist(ant)){
			J.esc("arquivo nao encontrado: "+ant);
			return;
		}
		try{
			File aa = new File(ant);
			File an = new File(nov);
			aa.renameTo(an);
			J.esc("renomeado: ");
			J.esc("      "+aa);
			J.esc("  para:");
			J.esc("      "+an);
		} catch(Exception e){
			J.impErr("falha renomeando arquivo: '"+ant+"'  para  '"+nov+"'","J.renameArq()" );
		}
	}
	public static String assArq(String cam){
		// retorna a assinatura do arq
		// ass = 3 primeiros chars do mesmo
		// usei na abertura de paleta nova no autoreconhecimento da versao do arq
		// passo o caminho do arq p comp q se encarrega de ver como abri-lo examinando primeiro sua versao
		RandomAccessFile arq = null;
		String ret = "";
		try{
			arq = new RandomAccessFile(cam, "rw");
			ret+= arq.readChar();
			ret+= arq.readChar();
			ret+= arq.readChar();
			ret = emMaiusc(ret);
			arq.close();			
		}catch(IOException e){
			impErr("!falha lendo assinatura de arquivo: "+cam,"J.assArq()");
			ret = "???";
			try{arq.close(); }catch(IOException ee){}
		}
		return ret;
	}

	
// === ARQUIVOS ASCII ======================
		static boolean opEmMaiusc=false;	
	public static int numLinKarq(String cam){
		// redirecionado por compatibilidade
		return numLinArq(cam);
	}
	public static int numLinArq(String cam){
		// FUNCAO: conta o numero total de linhas num arq ASCII
		// use com "leLinKarq()" e "escLinKarq()"
		// o arquivo comeca na linha zero
		// retorna -1 se erro
		// (tag de busca: saveStringArq saveStArq )
		if(!J.arqExist(cam)) {
			J.esc("arquivo nao encontrado: "+cam);
			return -1;			
		}	
		FileReader fr = null;
		BufferedReader br = null;
		String lido=null;
		boolean ok=false;
		int c=-1; // o arq comecarah em zero entao
		try{
			fr = new FileReader(cam);
			br = new BufferedReader(fr);
			do{
				lido = br.readLine();
				c++;
			} while(lido!=null);
			br.close();
			fr.close();
			ok=true;
		} catch(IOException e){
			ok=false;
			J.impErr("falha contando linhas do arq: "+cam+"  ","numLinKarq()");
			try{
				br.close();
				fr.close();
			}catch(IOException ee){}
		}
		if(ok) return c;
		return -1;
	}	

	public static void criaArqVazio(String cam){
		RandomAccessFile arq = null;
		if(cam==null) J.impErr("!caminho nulo","J.criaArqVazio()");
		try{
			arq = new RandomAccessFile(cam,"rw");
			// arq.writeInt(0);
			arq.close();
			// J.esc("arquivo vazio criado com sucesso: "+cam);
		}catch(IOException e){
			if(arq!=null) try{arq.close();}catch(IOException ee){}
			J.impErr("falha criando arquivo vazio: "+cam);
			e.printStackTrace();
		}
	}
	
	public static ArrayList<String> remLinVazia(ArrayList<String> ls){ 
		// remove linhas vazia do array list
		// util p carregar registros em banco de dados de JWin.java
		// nao deixei embutido em carrStrings() pq linhas em branco sao uteis na organizacao de scripts
		ArrayList<String> ret = new ArrayList<>();
		for(String s:ls)
			if(!J.iguais("",s))
				ret.add(s);
		return ret;		
	}
		static char opCharStopOpenStrings=0; // pára de carregar o arq se encontrar o char na primeira posição da linha. Veja q o char deve ser <> #0
	public static ArrayList<String> openStrings(String cam){ // tag carrStrings, carrStr
		// usa UTF-8, reconhece acentos
		if(!J.arqExist(cam)){
			J.impErr("!arquivo perdido: "+cam,"J.openStrings()");			
			return null;
		}

		ArrayList<String> arr = new ArrayList<>();
		File ff = null;
		BufferedReader br = null;
		String st=null;
		try{
			ff = new File(cam); // precisa ser com barras duplas descendo
			br = new BufferedReader(new InputStreamReader(new FileInputStream(ff), "UTF8"));
			
			do{
				st = br.readLine();
				esc(">>"+st);
				if(opEmMaiusc) st = J.emMaiusc(st);
				
				// pára de carregar o arq se encontrar o char flag na começo da linha
				if(opCharStopOpenStrings!=0) 
				if(st!=null)
				if(st.length()>0)
					if(st.charAt(0)==opCharStopOpenStrings)
					 st=null;
					 
				
				if(st!=null) arr.add(st);
			}while(st!=null);
			
			br.close();
			//ff.close(); // nao existe este metodo p File
			J.esc("ok: "+cam);
			return arr;
		}catch(IOException e){
			J.impErr("falha lendo lista de strings: "+cam,"J.openStrings()");
			J.sai();
			return null;
		}
		// return null;				
	}
	public static ArrayList<String> openStringsUTF8(String cam){ // ESSA EH MELHOR tag carrStrings, carrStr
		return openStrings(cam); // conservei por compatibilidade
	}
	public static ArrayList<String> openStringsUTF8(String cam, int min, int max){ 
		return openStrings(cam, min, max); // por conpatibilidade
	}
	public static ArrayList<String> openStrings(String cam, int min, int max){ 
		// precisa testar melhor esta, mas parece boa
		// usa UTF-8, reconhece acentos
		// pega somente o intervalo de linhas entre min e max, incluido ambos
		// a primeira linha do arq eh 0
		// seria bom usar em JWin.DataBase, p carregar arquivos grandes (mas cuidado com o cabecalho de colunas)
		
		if(min<0)min=0;
		
		if(!J.arqExist(cam)){
			J.impErr("!arquivo perdido: "+cam,"J.openStringsUTF8()");			
			return null;
		}

		ArrayList<String> arr = new ArrayList<>();
		File ff = null;
		BufferedReader br = null;
		String st=null;
		int l=-1;
		try{
			ff = new File(cam); // precisa ser com barras duplas descendo
			br = new BufferedReader(new InputStreamReader(new FileInputStream(ff), "UTF8"));
			
			do{
				st = br.readLine();
				l++;
				// esc(">>"+st);
				if(J.noInt(l,min,max)){
					if(opEmMaiusc) st = J.emMaiusc(st);
					if(st!=null) arr.add(st);
				}
				if(l>max) st=null;
			}while(st!=null);
			
			br.close();
			//ff.close(); // nao existe este metodo p File
			J.esc("ok: "+cam);
			return arr;
		}catch(IOException e){
			J.impErr("falha lendo lista de strings: "+cam,"J.openStrings()");
			J.sai();
			return null;
		}
		// return null;		
	}
	public static boolean appendStringArq(String st, String cam){
		// insere uma nova linha no arquivo ASCII, no final deste
		return J.escLinKarq(st, -2, cam); // -2 vai p final; -1 na primeirissima linha deslocando todo o resto do arq p frente; se for um numero especifico, substitui a linha em questao
	}
	public static boolean saveStrings(ArrayList<String> arr, String cam){
		// parece nao ser necessario UTF8 aqui, mas ? melhor testar depois
		// lembre q p inserir um item no comeco da lista deslocando todos os outros p baixo, basta fazer
		// 			st.add(0,itemNovoNaPrimeirissimaPosicao);			
		if(arr==null) return false;
		if(arr.size()==0) return false;
		
		// bug corrigido abaixo. Dava erro quando o arq nao existia previamente.
		if(!J.arqExist(cam)) J.criaArqVazio(cam);
		
		FileWriter fw = null;
		PrintWriter pw = null;
		try{
			fw = new FileWriter(cam);
			pw = new PrintWriter(fw);
			for(String st: arr){
				if(st!=null)
				  pw.println(st);
				// o problema eh q o arquivo sempre acaba com uma linha em branco. Corrigir.
				// J.delay(3); 
			}
			pw.close();
			fw.close();
			J.esc("ok: "+cam);
			return true;
		} catch(IOException e){
			try{ pw.close(); fw.close(); } catch(IOException ee){}
			J.impErr("!falha salvando strings: "+cam,"J.saveStrings()");
			return false;			
		}
		// return false;		
	}
/*	
Aparentemente, saveStrings() estah salvando normalmente com acentos depois de abrir um arquivo acentuado. Caso der problema, uma pista seria o seguinte:
				OutputStreamWriter bufferOut = new OutputStreamWriter(new FileOutputStream("c:\\temp\\acentos.txt"),"UTF-8");
       bufferOut.write("acento agudo: ?????\n");
       bufferOut.write("acento circunflexo: ?????\n");
       bufferOut.write("fim");
       bufferOut.close();
*/

/* ATENCAO!!! 
Estes dois metodos abaixo foram p video. 
Examine quando se salva um arquivo novo. Criar arq vazio primeiro p nao bugar.
Veja tamb?m arquivo ausente e mensagens de erro.
Adapt?-los e deix?-los como padrao. 
N?o precisarei trabalhar com textos sem UTF8. Deixar UTF8 como padrao.

	public static ArrayList<String> openStrings(String cam){ 
		// este usa UTF-8, reconhece acentos
		ArrayList<String> arr = new ArrayList<>();
		File ff = null;
		BufferedReader br = null;
		String st=null;
		try{
			ff = new File(cam); 
			br = new BufferedReader(new InputStreamReader(new FileInputStream(ff), "UTF8"));
			do{
				st = br.readLine();
				if(st!=null) arr.add(st);
			}while(st!=null);
			
			br.close();
			//ff.close(); // nao existe este metodo p File
			System.out.println("open ok: "+cam);
			return arr;
		}catch(IOException e){
			System.out.println("falha lendo lista de strings: "+cam);
			e.printStackTrace();
			try{ br.close(); } catch(IOException ee){}
			System.exit(0);			
			return null;
		}
	}
  public static boolean saveStrings(ArrayList<String> arr, String cam){
		PrintWriter pw = null;
		try{
			pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(cam), "UTF-8"));
			for(String st: arr){
				pw.println(st);
			}
			pw.close();
			System.out.println("save ok: "+cam);
			return true;
		} catch(IOException e){
			System.out.println("Falha salvando strings: "+cam);
			return false;			
		}
	}

*/

	public static boolean escLinKarq(String st, int ln, String cam){
		// FUNCAO: substitui a linha ln do arq cam por st
		// parece funcionar muito bem
		// use com "leLinKarq()" abaixo
		// se "ln==-1", empurra todo o arq p baixo e escreve ln na primeirissima linha sem substituir
		// se "ln==-2", coloca a nova linha no final do arquivo incrementando-o
		// o arquivo comeca na linha zero
		// (tag de busca: saveStringArq saveStArq )
		
		if(!J.arqExist(cam)) criaArqVazio(cam);// bug corrigido. Dava erro quando o arq nao existia previamente.
		
		FileReader fr = null;
		BufferedReader br = null;
		FileWriter fw = null;
		PrintWriter pw = null;
		String camT="ASCII_temp.txt";
		String lido=null;
		boolean ok=false;
		int c=-1; // o arq comecarah em zero entao
		try{
			fr = new FileReader(cam);
			br = new BufferedReader(fr);
			fw = new FileWriter(camT);
			pw = new PrintWriter(fw);
			do{
				if(c==-1)
					if(ln==-1)
						pw.println(st); // insere na primeira linha empurrando tudo p baixo
				lido = br.readLine();
				c++;
				if(c==ln) pw.println(st);
				else { if(lido!=null) pw.println(lido); }
			} while(lido!=null);
			if(ln==-2) 
				pw.println(st); // eh so colocar -2 p aumentar o arquivo
			br.close();
			fr.close();
			pw.close();
			fw.close();
			ok=true;
		} catch(IOException e){
			ok=false;
			J.impErr("!falha salvando string: "+cam+"  "+st,"escLinKarq()");
			e.printStackTrace();
			try{
				br.close();
				fr.close();
				pw.close();
				fw.close();				
			}catch(IOException ee){}
		}
		if(ok){
			J.delArq(cam);
			J.renameArq(camT,cam);
			J.esc("string ok: "+cam);		
		}	
		return ok;
	}
	public static String leLinKarq(int ln, String cam){
	// arq comeca na linha zero; ler alem do arq retorna "null"
	// quanto menor o arquivo, mais rapida eh a leitura
	// retorna null se fora do tamanho do arquivo


		
		// removendo bug
		if(cam.charAt(0)=='/') cam = remPrimChar(cam);
		if(cam.charAt(0)=='/') cam = remPrimChar(cam);
		
		// removendo mais bugs
		if(cam.startsWith("null//"))
		  cam = remTrecho(0,5,cam);
		// "null//Fonte113.java"
		// ?foi??? R: parece q funcionou. O  E d i t o r W  i n  tava travando.
			

		
		if(!J.arqExist(cam)){
			J.impErr("!arquivo perdido: "+cam,"J.leLinKarq()");
			return "ERRO";
		}
		String ret = null;
		FileReader fr = null;
		BufferedReader br = null;
		try{
			fr = new FileReader(cam);
			br = new BufferedReader(fr);
			for(int k=0; k<=ln; k++) 
				if(k==ln) ret = br.readLine();
				else br.readLine();
			br.close();
			fr.close();
		}catch(IOException e){
			e.printStackTrace();
			J.impErr("Erro lendo arquivo ASCII: "+cam,"linhaKarq()");
			J.sai();
		}
		// if (ret==null) ret = "???"; // melhor retornar nulo mesmo
		return ret;
	}	
	public static String linhaKarq(int ln, String cam){
		// obsoleto, mas redirecionado
		return leLinKarq(ln,cam);
	}	
	public static int procLinArq(String psq, String cam){
		// FUNCAO: procura um linha no arq ascii e retorna o indice dela. 
		// nao faz distincao de minusculas/maiusculas
		// O arq comeca em zero
		// retorna -1 se nao encontrou 
		// retorna -2 se arquivo inexistente
		// O loop termina na primeira ocorrencia
	
		if(!arqExist(cam)) {
			impErr("arquivo nao encontrado: "+cam,"procLinArq()");
			return -2;
		}
		
		psq = J.emMaiusc(psq);
		int num = -1;
		FileReader fr = null;
		BufferedReader br = null;
		try{
			fr = new FileReader(cam);
			br = new BufferedReader(fr);
			String lin = null;
			int c=-1;
			do{
				lin = br.readLine();				
				c++;
				if (lin!=null) {
					lin = truncarAte(';',lin); // ignora tudo depois de ";", inclusive
					lin = emMaiusc(lin);
          lin = semSpcIni(lin);
					if (lin.equals(psq)) num = c;
				}	
			} while( (lin!=null) && (num==-1) );
			br.close();
			fr.close();
		} catch(IOException e){
			impErr("falha lendo arq ASCII: "+cam,"procLinArq()");
		}	
		return num;
	}	
	public static boolean iguais(String st, String stt){
		// compara dois strings
		// nao eh case-sensitive: "NOME" == "nome"
		// nao trata espacos iniciais nem tabs
		
		if(st==null) if(stt==null) return true;
		if(st==null) if(stt!=null) return false;
		if(st!=null) if(stt==null) return false;
		
		st = J.emMaiusc(st);
		stt = J.emMaiusc(stt);
		if(st.equals(stt)) return true;
		return false;
	}
	public static int getIndFrase(String trecho, String par){
		// frase comeca em zero
		// frase seria o trecho do string q esta entre virgulas ou separado no comeco/final do string por uma virgula.
		if(trecho==null) return -2;
		if(par==null) return -2;
		trecho = limpaSt(trecho);
		par = limpaSt(par);
		
		int f=0;
		char ca=0;
		String st="";
		for(int q=0; q<par.length(); q++){
			ca = par.charAt(q);
			if(ca==','){
				st = limpaSt(st);
				if(iguais(st,trecho)) return f;
				// esc(st);
				st="";
				f++;
			} else st+=ca;
		}
		return -1;// nao achou
	}
	public static String getFraseK(int k, String par){
		// frase comeca em zero
		// frase seria o trecho do string q esta entre virgulas ou separado no comeco/final do string por uma virgula.
		if(par==null) return "???";
		int f=0;
		char ca=0;
		String st="";
		for(int q=0; q<par.length(); q++){
			ca = par.charAt(q);
			if(ca==','){
				if(f==k) return limpaSt(st);
				st="";
				f++;
			} else st+=ca;
		}
		return "???";
	}
	public static String limpaSt(String par){
		// remove tab, espaco inicial e final
		// pode ser otimizada, mas jah funciona bem		
		
		par = J.trocaChar((char)13,' ',par);
		par = J.trocaChar((char)10,' ',par);
		par = J.trocaChar('\t',' ',par);// tab
		par = J.semSpcIni(par);
		par = J.semSpcFinal(par);
		if(opEmMaiusc) 
			par = J.emMaiusc(par);
		return par;
	}
	public static void geraArqAjuda(String camFont, String camAj){ // tag geraHelp() , criaHelp()
		// gera arq ascii no formato:
		// ?Jah permite multiline???
		//   //!T+shift    (tecla ou sua combinacao)
		//   //@Dica			 (string explicativo)
		// os sinais "!" e "@" sao indispensaveis
		if(!J.arqExist(camFont)) {
			J.impErr("!arquivo fonte nao encontrado: "+camFont,"J.geraArqAjuda()");
			return;			
		}	
		FileReader fr = null;
		BufferedReader br = null;
		String lido=null, st=null;
		char ca=0;
		boolean ok=false;
		ArrayList<String> arr = new ArrayList<>();
		int c=-1;
		try{
			fr = new FileReader(camFont);
			br = new BufferedReader(fr);
			do{
				c++;
				lido = br.readLine();
				if(lido!=null)
				if(lido.length()>2){
					lido = J.semSpcIni(lido);
					lido = J.semSpcFinal(lido);
					ca=' ';
					if(lido.length()>2)
						ca = lido.charAt(2);
					if(J.vou(ca,'!','@')){
						st = J.removeIni(lido,3);
						if(ca=='!') arr.add(" "); // uma linha em branco
						if(ca=='@') st = "      "+st;
						arr.add(st);
					}	
					// o formato eh:
					//   //!tecla + combinacao
					//	 //@dica					
				}
			} while(lido!=null);
			J.esc("arquivo de ajuda:");
			J.esc("   "+c+" num linhas em "+camFont);
			J.esc("   "+(arr.size())+" linhas colhidas");
			br.close();
			fr.close();
			arr.remove(0); // a primeira linha em branco
			J.saveStrings(arr, camAj);
		} catch(IOException e){
			J.impErr("falha gerando arquivo de ajuda: "+camAj+"  pelo fonte "+camFont,"J.geraArqAjuda()");
			try{
				br.close();
				fr.close();
			}catch(IOException ee){}
		}
	}			
	public static boolean remLinArq(int ln, String cam){
		if(!J.arqExist(cam))
			J.impErr("!arquivo perdido: |"+cam+"|", "J.remLinArq()");
		
		FileReader fr = null;
		BufferedReader br = null;
		FileWriter fw = null;
		PrintWriter pw = null;
		String camTmp = "TMP - "+cam;
		try{
			fr = new FileReader(cam);
			br = new BufferedReader(fr);
			fw = new FileWriter(camTmp);
			pw = new PrintWriter(fw);
			String st="";
			int c=-1;
			do{
				st = br.readLine();
				c++;
				if(c!=ln)	
					if(st!=null)
						pw.println(st);				
			} while(st!=null);
			br.close();
			fr.close();
			pw.close();
			fw.close();
			J.delArq(cam);
			J.renameArq(camTmp,cam);
			J.esc("ok: "+cam);
		}catch(IOException e){
			J.impErr("falha removendo linha "+ln+" do arq: |"+cam+"|", "J.remLinArq()");
			try{ pw.close(); fw.close(); }catch(IOException e1){}
			try{ br.close(); fr.close(); }catch(IOException e2){}
			return false;
		}
		return true;
			
	}
// =========================================
	
		static String tk1,tk2, tk3,tk4,tk5, tk6,tk7,tk8,tk9,tk10,tk11,tk12,tk13,tk14,tk15,tk16;
		static String tk1_,tk2_, tk3_,tk4_,tk5_, tk6_,tk7_,tk8_,tk9_,tk10_,tk11_,tk12_,tk13_,tk14_,tk15_,tk16_;
		static boolean opTokenMaiusc=true; // p linux é melhor p achar arquivos por script
	public static void guardaTokens(){
		tk1_ = "";	tk1_ = tk1;
		tk2_ = "";	tk2_ = tk2;
		tk3_ = "";	tk3_ = tk3;
		tk4_ = "";	tk4_ = tk4;
		tk5_ = "";	tk5_ = tk5;
		tk6_ = "";	tk6_ = tk6;
		tk7_ = "";	tk7_ = tk7;
		tk8_ = "";	tk8_ = tk8;
		tk9_ = "";	tk9_ = tk9;
		tk10_ = "";	tk10_ = tk10;
		tk11_ = "";	tk11_ = tk11;
		tk12_ = "";	tk12_ = tk12;
		tk13_ = "";	tk13_ = tk13;
		tk14_ = "";	tk14_ = tk14;
		tk15_ = "";	tk15_ = tk15;
		tk16_ = "";	tk16_ = tk16;
	}
	public static String getToken(int t){
		switch(t){
			case 1: return tk1;
			case 2: return tk2;
			case 3: return tk3;
			case 4: return tk4;
			case 5: return tk5;
			case 6: return tk6;
			case 7: return tk7;
			case 8: return tk8;
			case 9: return tk9;
			case 10: return tk10;
			case 11: return tk11;
			case 12: return tk12;
			case 13: return tk13;
			case 14: return tk14;
			case 15: return tk15;
			case 16: return tk16;
		}
		impErr("!numero invalido para o token: "+t,"J.getToken()");
		return "???";
	}
	public static void restauraTokens(){
		tk1 = tk1_;
		tk2 = tk2_;
		tk3 = tk3_;
		tk4 = tk4_;
		tk5 = tk5_;
		tk6 = tk6_;
		tk7 = tk7_;
		tk8 = tk8_;
		tk9 = tk9_;
		tk10 = tk10_;
		tk11 = tk11_;
		tk12 = tk12_;
		tk13 = tk13_;
		tk14 = tk14_;
		tk15 = tk15_;
		tk16 = tk16_;
	}	
	public static void zTokens(){
		tk1=tk2=tk3=tk4=tk5=tk6=tk7=tk8=tk9=tk10=tk11=tk12=tk13=tk14=tk15=tk16="";
	}
	public static String limpaComs(String p){
		// chamada automatica em "extTokens"
		p = truncarAte(';',p);
		p = trocaChar('?',' ',p);
		p = trocaChar(':',' ',p);
		p = trocaChar('\t',' ',p);// TAB
		if(opTokenMaiusc)	p = emMaiusc(p);
		return p;
	}

	public static void extTokens(String p){
		zTokens();
		p = limpaComs(p);
		Scanner sc = new Scanner(p);
		if (sc.hasNext()) tk1= sc.next();
		if (sc.hasNext()) tk2= sc.next();
		if (sc.hasNext()) tk3= sc.next();
		if (sc.hasNext()) tk4= sc.next();
		if (sc.hasNext()) tk5= sc.next();
		if (sc.hasNext()) tk6= sc.next();
		if (sc.hasNext()) tk7= sc.next();
		if (sc.hasNext()) tk8= sc.next();
		if (sc.hasNext()) tk9= sc.next();
		if (sc.hasNext()) tk10= sc.next();
		if (sc.hasNext()) tk11= sc.next();
		if (sc.hasNext()) tk12= sc.next();
		if (sc.hasNext()) tk13= sc.next();
		if (sc.hasNext()) tk14= sc.next();
		if (sc.hasNext()) tk15= sc.next();
		if (sc.hasNext()) tk16= sc.next();
	}
	public static int numTokens(){
		return nTokens();
	}
	public static int nTokens(){
		// usar depois de extTokens()
		if(tk1.equals("")) return 0;
		if(tk2.equals("")) return 1;
		if(tk3.equals("")) return 2;
		if(tk4.equals("")) return 3;
		if(tk5.equals("")) return 4;
		if(tk6.equals("")) return 5;
		if(tk7.equals("")) return 6;
		if(tk8.equals("")) return 7;
		if(tk9.equals("")) return 8;
		if(tk10.equals("")) return 9;
		if(tk11.equals("")) return 10;
		if(tk12.equals("")) return 11;
		if(tk13.equals("")) return 12;
		if(tk14.equals("")) return 13;
		if(tk15.equals("")) return 14;
		if(tk16.equals("")) return 15;
		return 16;
	}
	public static int larTextMaiorSplit(String s, char div){
		int nl = 1+J.contChar(div,s);
		int maior = 0, lar = 0;
		String st;
		for(int q = 0; q<nl; q++){
			st = J.split(s,div,q);
			lar = J.larText(st);
			if(lar>maior) maior = lar;
		}
		return maior;
	}

	public static String split(String s, char div, int n){
		// LEMBRETE:
/*
			String st = "separar este string por espacos";
			String lis[] = st.split(" ");
			J.sai(lis[0]); // retorna "separar"
*/		
		// divide o string usando o caracterer referencia
		// retorna o trecho especifico deste string parametro
		// o primeiro trecho tem ordem zero e corresponde ao trecho: inicio ateh a primeira ocorrencia do caracter, sem inclui-lo
		//    	MENTIRA!!! O PRIMEIRO TEM ORDEM "1", testei com div=' ' e nao tinha nada com n=zero
		// o caracter-referencia sempre serah excluido do retorno
		// um trecho q nao existe voltarah como string vazio "", mas nao nulo
		// saiba: s="c://java//wav//!toin.wav", div="/", n="1" retornarah "", jah q o prim trecho estah entre os dois caracteres divisores q estao COLADOS
		// n=-3 retornarah um string vazio, assim como um trecho qq positivo q nao existe
		// se o caracter divisor nao existir no string, todo o parametro "s" eh considerado como trecho zero, e os restantes como inexistentes
		// QUESTAO: ?ateh q ponto este metodo substitui "e x t T o k e n s ()"???
		// daria p usar este metodo tb p extrair letras de drives e extencoes de caminhos de arquivo; 
		// Veja q ateh uma pasta numa profundidade especifica de um caminho poderia ser extraida, mas precisaria tratar o caminho primeiro p q nao tivesse barras repetidas (jah existe uma funcao q trata disso)
		
		// tratar parametros nulos e estranhos
		if(s==null) return "";
		
		// setup
		int l = s.length(), i=0;
		String st="";
		char ca = 0;
		
		// retornando st devido
		for(int q=0; q<l; q++){
			ca = s.charAt(q);
			if(ca==div) i++;
			if(i==n) if(ca!=div) st+=""+ca;
			if(ca==div) if(i>n) return st;
		}	
		return st; // se chegou aqui eh pq nao existe o intervalo;
	}
	public static boolean comecaCom(String st, String com){
		// pressupoe q "com" eh menor q "st", mas corrige se nao for
		// retorna true se o comeco de "st" for igual ao comeco de "com"

/*	Eu nao conhecia "String.startsWith()" ainda...
		String stt = "aqui este eh um exemplo de string";
			if(stt.startsWith("aqui")) esc("xxxxxxxxxxx");
*/			
			
		if(st==null) return false;
		if(com==null) return false;
		
		st = J.emMaiusc(st);
		com = J.emMaiusc(com);
		boolean eh=true;
		int lm = com.length();
		if(lm>st.length()) lm = st.length(); // p nao bugar quando "st" for menor q "com"
		for(int q=0; q<lm; q++)
			if(com.charAt(q)!=st.charAt(q)) eh=false;
		return eh;		
	}	
	public static String truncar(char w, String p){
		return truncarAte(w,p); // mais intuitivo
	}
	
	public static String truncarAte(char w, String p){
		//tudo q vier apos o char w(inclusive) sera ignorado
		//usado p comentarios em scripts
		if(p==null) return "";
		
		String st="";
		char ca=0;
		for(int k=0; k<p.length(); k++){
			ca=p.charAt(k);
			if(ca==w) break;
			st+=ca;
		}
		return st;
	}	
	public static String truncarAntes(char w, String p){
		//tudo q estiver antes da primeira ocorrencia do char w(inclusive) sera ignorado
		//usado p scripts de JWinn
		// tag removeAntes, remAntes, remChar
		if(p==null) return "";
		
		String st="";
		char ca=0;
		boolean vai=false;
		for(int k=0; k<p.length(); k++){
			ca=p.charAt(k);
			if(vai)st+=ca;
			if(ca==w) vai=true;
		}
		return st;
	}	
	public static String stAntesDe(char w, String p){ // mais intuitivo
		return truncarAte(w,p); 
	}
	public static String stDepoisDe(char w, String p){ // mais intuitivo
		return truncarAntes(w,p);
	}	
	
	public static String trocaCharPos(int pos,char ca,String p){
		// troca o caracter da posicao especifica do string, q sempre comeca em zero
		// tem q testar isso melhor
		
		if(p==null) return null;
		if(pos>p.length()) return p;
		if(pos<0) return p;
		
		String st = "";
		for(int q=0; q<p.length(); q++){
			if(q==pos) st+=""+ca;
			else st+=p.charAt(q);
		}
		return st;
	}
	public static int numPalavras(String p){
		// qq coisa entre espacos e tabs eh uma palavra
		// cuidado com "," e ";" q sao considerados letras
		// assim: 
		//		"eu tenho, moedas"(3 palavras) 
		// eh diferente de 
		//    "eu tenho , moedas" (4 palavras)
		// reparou a virgula entre espacos acima?
		
		if(p==null) return 0;
		p = trocaChar('\t',' ',p); // trocando tabs por espacos
		if(J.iguais(p,"")) return 0;
		
		p = J.semSpcIni(p);
		p = J.semSpcFinal(p);
		
		char ca=0;
		int 
			e=0, // estado da maq de estados
			w=1; //numero da palavra atual
		for(int q=0; q<p.length(); q++){
			ca = p.charAt(q);
			if(e==0){
				if(ca!=' ') e=1;
			} else if(e==1){
				if(ca==' ') { e=0; w++; }
			}
		}		
		return w;		
	}
	public static Integer intKst(int k, String p){
		return J.stEmInt( palavraK(k,p));
	}
	public static String palavraK(int k, String p){ // tag getPalavraK palavraKst
		// retorna a k-esima palavra do string p
		// palavras separadas por espaco
		// a primeira palavra tem k=0
		// espacos no inicio/fim da palavra sao ignorados
		// cuidado com "," e ";" jah q sao consideradas letras (senao fica dificil pegar numeros float no meio do string de um arq)
		// use J.remChar(ca,st) p remover
		// k fora do string retorna "". ex: palavra -10 ou palavra 32000
		if(p==null) return "";
		p = trocaChar('\t',' ',p); // "(char)9" tb funcionaria aqui
		
		p = J.semSpcIni(p);
		p = J.semSpcFinal(p);		
		
		String st="";
		char ca=0;
		int 
			e=0, // estado da maq de estados
			w=0; //numero da palavra atual
		for(int q=0; q<p.length(); q++){
			ca = p.charAt(q);
			if(e==0){
				if(ca!=' ') e=1;
			} else if(e==1){
				if(ca==' ' || ca=='\t') {  // ca==(char)9 eh o mesmo q '\t'
					e=0; w++; 
				}
			}
			if(w==k){
				if(ca!=' ') 
					st+=""+ca;
			}
		}		
		return st;
	}
	public static String palavraKaKK(int k,int kk, String p){ // tag getPalavraK palavraKst
		// retorna o conjunto de palavras k..kk
		// palavras separadas por espaco
		// a primeira palavra tem k=0
		// se tem 4 palavras e kk vale 1000, ent?o retorna apenas at? 4
		// espacos no inicio/fim da palavra sao ignorados
		// cuidado com "," e ";" jah q sao consideradas letras (senao fica dificil pegar numeros float no meio do string de um arq)
		// use J.remChar(ca,st) p remover, ou J.trocaChar(a,n,st);
		// k fora do string retorna "". ex: palavra -10 ou palavra 32000
		if(p==null) return "";
		p = trocaChar('\t',' ',p); // "(char)9" tb funcionaria aqui
		
		p = J.semSpcIni(p);
		p = J.semSpcFinal(p);		
		
		String st="";
		char ca=0;
		int 
			e=0, // estado da maq de estados
			w=0; //numero da palavra atual
		for(int q=0; q<p.length(); q++){
			ca = p.charAt(q);
			if(e==0){
				if(ca!=' ') e=1;
			} else if(e==1){
				if(ca==' ' || ca=='\t') {  // ca==(char)9 eh o mesmo q '\t'
					e=0; w++; 
				}
			}
			if(w>=k && w<=kk){
				//if(ca!=' ') 
					st+=""+ca;
			}
		}		
		st = J.semSpcIni(st);
		st = J.semSpcFinal(st);
		return st;
	}

	public static String trocaChar(char a,char n,String p){
		if(p==null) return "";
		char ca=0;
		String st="";
		for(int k=0; k<p.length(); k++){
			ca = p.charAt(k);
			if(ca==a)	st+=n;
			else st+=ca;	
		}
		return st;
	}
	public static String remChar(char ca, String st){
		String w = "";
		for(int q=0; q<st.length(); q++){
			if(st.charAt(q)!=ca)
				w+=st.charAt(q);
		}
		return w;
	}
	public static String remChar(int pos, String st){
		// remove um unico char da posicao informada
		if(st==null) return null;
		String w = "";
		for(int q=0; q<st.length(); q++){
			if(q!=pos)
				w+=st.charAt(q);
		}
		return w;		
	}
	public static String remChars(String ls, String st){
		// remove todos os char "ls" do string "st"
		String ret="";
		char ca = 0;
		for(int q=0; q<st.length(); q++){
			ca = st.charAt(q);
			if(!tem(ca,ls))
				ret+=ca;
		}
		return ret;
	}
	public static String remChars(int min, int max, String st){
		// remove os chars do meio da string
		// mas acho q serve p extremos tb, mas precisa testar
		if(st==null) return null;
		if(min>max) {int q=min; min=max; max=q; }
		String ret = "";
		for(int q=0; q<st.length(); q++)
			if(q<min || q>max)
				ret+= st.charAt(q);
		return ret;
	}
	public static String getChars(int min, int max, String st){
		// retorna os chars do meio da string
		if(st==null) return null;
		if(min>max) {int q=min; min=max; max=q; }
		String ret = "";
		for(int q=0; q<st.length(); q++)
			if(q>=min && q<=max)
				ret+= st.charAt(q);
		return ret;
	}
	

	// acabei programando duas vezes aa toa, mas deixei por compatibilidade
	public static String insChar(char ca, String st, int pos){
		// essa tah boa
		if(st==null) return ""+ca;
		if(pos>=st.length()) return st+=ca;
		
		String w = "";
		for(int q=0; q<st.length(); q++){
			if(q==pos) w+=ca;
			w+=st.charAt(q);
		}
		return w;
	}		
	public static String insChar(char ca, int pos, String p){ // essa acho mais intuitiva
		String st="";
		for(int q=0; q<p.length(); q++){
			if(q==pos) st+=ca;
			st+= ""+p.charAt(q);
		}
		return st;
	}

	public static String insText(String ins, String st, int pos){ // insTrecho
		// ins: trecho a ser inserido
		// st: string original q serah aumentada com o trecho
		// pos... adivinha
		if(ins==null) return st;
		if(st==null) return ins;
		if(pos<0) return ins+st;
		if(pos>st.length()) return st+ins;
		
		String ret="";
		for(int q=0; q<st.length(); q++){
			if(pos==q) ret+=ins;
			ret+=st.charAt(q);
		}
		return ret;
	}		
	public static String pegaTrecho(char e, String st, char d){
		// pega o trecho do string delimitado pelos caracteres informados
		// ambos caracteres ficam fora
		boolean on=false;
		int l = st.length();
		String ret="";
		char ch=0;
		for(int q=0; q<l; q++){
			ch=st.charAt(q);
			if(on) if(ch!=d) ret+=""+ch;
			if(ch==e) on=true;
			if(ch==d) if(on)break;
		}
		return ret;
	}
	public static String pegaTrecho(int min, int max, String st){
		if(st==null) return "";
		if(min>st.length()) return "";
		if(max<0) return "";
		
		if(max>st.length()) max = st.length();
		if(min<0) min=0;
		if(min>max) return "";
		
		String w = "";
		for(int q=0; q<st.length(); q++){			
			if(J.noInt(q, min,max))
				w+=st.charAt(q);
		}
		return w;					
	}
	public static String remTrecho(int min, int max, String st){
		if(st==null) return "";
		if(min<0) min=0;
		if(max<0) return st;
		if(min>max) return "";// ?assim mesmo???
		String w = "";
		for(int q=0; q<st.length(); q++){			
			if(!J.noInt(q, min,max))
				w+=st.charAt(q);
		}
		return w;			
	}	
	public static String semSpcFinal(String st){
		if(st==null) return null;
		boolean vai=false;
		String ret = "";
		char c = ' ';
		for(int p=st.length()-1; p>=0; p--){
			c = st.charAt(p);
			if(c!=' ') vai=true;
			if(vai) ret = c + ret;
		}
		return ret;
	}
	
	
	// procLinK estah na parte de arquivos ASCII
	public static String remPrimChar(String p){
		if(p==null) return null;
		if(p.length()==0) return "";
		
		String st="";
		for(int q=1; q<p.length(); q++)
			st+=p.charAt(q);
		return st;		
	}
	public static String remUltChar(String p){
		if(p==null) return null;
		if(p.length()==0) return "";
		
		String st="";
		for(int q=0; q<p.length()-1; q++)
			st+=p.charAt(q);
		return st;
	}
  public static String semSpcIni(String st){ // remSpcIni
		if(st==null) return null;
		boolean vai = false;
		String ret="";
		char ca=0;
		for(int q=0; q<st.length(); q++){
			ca = st.charAt(q);
			if(ca!=' ') vai=true;
			if(vai) ret+=""+ca;
		}
		return ret;
	}
	public static String removeIni(String p, int lm){
		// remove tudo antes de lm
		
		if(p==null) return "";
		if(lm>p.length()) return "";
		if(lm<0)lm=0;		
		
		String st="";
		for(int q=lm; q<p.length(); q++)
			st+=p.charAt(q);
		return st;	
	}
	public static String truncUltimos(int n, String p){
		// tem q testar
		return trunc(p, p.length()-n);
	}
	public static String trunc(String p, int lm){
		// trunca o final do string ateh lm removendo o final dele
		// p truncar o inicio, use removeIni
		if(p==null) return "";
		if(lm>p.length()) lm=p.length();
		
		String st = "";		
		for(int q=0; q<lm; q++)
			st+=p.charAt(q);
		return st;
	}
	
	public static int procChar(char ca, String st){
		// parece funcionar bem
		// procura o char ca no string st
		// retorna -1 se nao achou
		if(st==null) return -1;
		if(st.equals("")) return -1;
		for(int q=0; q<st.length(); q++)
			if(st.charAt(q)==ca)
				return q;
		return -1;	
	}

	public static char incChar(char p){
		// na marra
		switch(p){
			case '9': return '0';
			case '8': return '9'; 
			case '7': return '8'; 
			case '6': return '7'; 
			case '5': return '6'; 
			case '4': return '5'; 
			case '3': return '4'; 
			case '2':	return '3'; 
			case '1': return '2'; 
			case '0': return '1'; 
		}		
		return p;
	}
	public static char decChar(char p){
		switch(p){
			case '9': return '8';
			case '8': return '7'; 
			case '7': return '6'; 
			case '6': return '5'; 
			case '5': return '4'; 
			case '4': return '3'; 
			case '3': return '2'; 
			case '2':	return '1'; 
			case '1': return '0'; 
			case '0': return '9'; 
		}		
		return p;
	}	
	public static String incChar(int pos, String p){
		String st="";
		for(int q=0; q<p.length(); q++)
			if(q!=pos) 
				st+=p.charAt(q);
			else
				st+=incChar(p.charAt(q));				
		return st;			
	}
	public static String decChar(int pos, String p){
		String st="";
		for(int q=0; q<p.length(); q++)
			if(q!=pos) 
				st+=p.charAt(q);
			else
				st+=decChar(p.charAt(q));				
		return st;			
	}	
	public static String incSt(String p){
		// reconhece a sequencia "apagar01.ext" e incrementa p "apagar02.ext";
		// ve^ apenas o segundo algarismo seguido do zero, mas jah ajuda bastante na abertura de arquivos
		if(p==null) return null;
		int w=-1;
		for(int q = 0; q<p.length(); q++)
			if(p.charAt(q)=='0')
			 if(w==-1) w=q;
		if(w!=-1)
			p = incChar(w+1, p);
		return p;
	}
	public static String decSt(String p){
		// reconhece a sequencia "apagar02.ext" e decrementa p "apagar01.ext";		
		if(p==null) return null;
		int w=-1;
		for(int q = 0; q<p.length(); q++)
			if(p.charAt(q)=='0')
			 if(w==-1) w=q;
		if(w!=-1)
			p = decChar(w+1, p);
		return p;
	}	

	public static String incSt000(String pp){
		// ?jah tem a decSt000()???
		// reconhece a sequencia "apagar001.ext" e incrementa p "apagar002.ext";
		// o problema eh q soh pega os 3 primeiros digitos, mas jah ajuda.
		// se estiver "aaa001sss222fff", soh modificarah p "002" na parte referente
		// nao pega "ww01rr" :(  depois
		// tah meia-boca mais jah ajuda
		String p = pp+"   ";
		char ca=0, caa=0, caaa=0;
		int num=0;
		for(int q=0; q<p.length(); q++){
			ca  = p.charAt(q);
			caa = p.charAt(q+1);
			caaa= p.charAt(q+2);
			if(J.ehNum(ca))
			if(J.ehNum(caa))
			if(J.ehNum(caaa)){
				num = J.stEmInt(""+ca+caa+caaa);
				if(num==999) num=0;
				else num++;				
				String st = J.intEmSt000(num);
				pp = trocaCharPos(q+0,st.charAt(0), pp);
				pp = trocaCharPos(q+1,st.charAt(1), pp);
				pp = trocaCharPos(q+2,st.charAt(2), pp);
				return pp;
			}
		}
		return pp;
	}	
	
	

	
/*
		- camCfg = "conf.ini";
		- cam = leStCfg("ARQ");
		- num = leIntCfg("valor");
		- fl = leFloatCfg("sigma");
		- st = LeStCfg("frase");
		
		getVar(st)
		getVal(st)
*/	

	public static void escCfg(String var, int val, String cam){
		escCfg(var,""+val,cam);
	}
	public static void escCfg(String var, float val, String cam){
		escCfg(var,""+val,cam);
	}	
	public static void escCfg(String var, boolean val, String cam){
		escCfg(var,""+(val?"TRUE":"FALSE"),cam);
	}		
	public static void escCfg(String var, String val, String cam){
		// bolar uma maneira de inserir comentarios dentro dos arquivos cfg:
		//    - como ficaria complicado identificar a linha do comentario, parece ser melhor dar um nome ao comentario como se fosse uma variavel.
		// 		- assim nao tem problema de duplicacao desta linha do comentario a cada gravacao
		// 		- basta dar um nome do tipo "REM1", "= = = = =", "COMENTARIO1", etc
		// 		- se nao fizer isto, corre-se o risco de inflar o arq a cada gravacao
		//  ?Seria possivel isto???: um J.escComCfg("var1", "isto é um comentário acima desta variável var1"); ... ou ao lado dela... ?Seria possivel???
		// insere um novo valor ou troca o valor da variavel dentro do arq de configuracao indicado
		// jah funciona rasoavelmente bem
		// BUG: deve-se evitar entradas multiplas da mesma var, pois este metodo ainda nao corrige essa duplicidade; Se uma var estiver repetida dentro do arq, somente a primeira ocorrencia eh modificada
		// AVISO: use apenas p gravar variaveis isoladas, nao tente salvar um montao delas ao mesmo tempo pq o sistema nao deve dar conta
		// POSSIVEL BUG: o sistema nao darah conta se muitas variaveis forem gravadas rapidamente, este metodo serve apenas p escrita isolada, nao p muitas variaveis dentro de um loop, por exemplo.
		
// corrigir parametros
		if(!J.arqExist(cam)){
			J.criaArqVazio(cam);
			J.esc("novo arq de configuracao criado: |"+cam+"|");
		}
		var = J.emMaiusc(J.limpaSt(var));
		val = J.limpaSt(val);
// carregar lista
		ArrayList<String> lst = J.openStrings(cam);
// comparar valores, um por um
//		se iguais, substituir valor, salvar arq e sair do metodo
//		se diferentes, continuar loop
		int lm = lst.size();
		if(lm>1000) lm=1000; 
		if(lm>=1000) {
			J.esc("====== AVISO ====== |"+cam+"|");
			J.esc("     - o arq de configuracao estah gigante(mais de 1000 linhas)");
			J.esc("     - foi truncado em |"+lst.get(lst.size()-1)+"|");
			J.esc("     - melhor verificar algum bug de gravacao");
		}
		for(int q=0; q<lm; q++){
			J.extTokens(lst.get(q));
			if(J.iguais(var,J.tk1)){				
				lst.set(q,var+" = "+val);
				//lst.add(var+" = "+val);
				J.saveStrings(lst,cam);
				J.esc("valor atualizado: "+var+" = "+val+"    em |"+cam+"|");
				return;
			}			
		}
// se acabou o loop, entao
// se nao achou a var, entao adicionar a lista
		lst.add(var+" = "+val);		
// salvar arq
		J.saveStrings(lst,cam);
		J.esc("nova variavel adicionada: |"+var+" = "+val+"|");
		if(opDelayCfg) delay(100); // ?serah q isso ajuda p salvar mais variaveis de uma vez soh??? Precisei p joeCraft
	}
		static boolean opDelayCfg=false;
	
	public static float leCfgFloat(String var, String cam){
		String st = leCfgSt(var,cam);
		float v = J.stEmFloat(st); // essa stEmFloat() jah foi ajustada???
		return v;		
	}
	public static int leCfgInt(String var, String cam){
		String st = leCfgSt(var,cam);
		int v = J.stEmInt(st);
		return v;
	}
	public static boolean leCfgBool(String var, String cam){
		String st = leCfgSt(var,cam);
		if(iguais(st,"TRUE")) return true;
		if(iguais(st,"FALSE")) return false;
		J.impErr("AVISO: valor estranho: |"+st+"|","J.leCfgBool()"); // melhor avisar isso
		return false;
	}
	public static String leCfgSt(String var, String cam){
// corrigir parametros
		if(!J.arqExist(cam)){
			J.impErr("!arquivo perdido: |"+cam+"|","J.leCfgSt()");
		}
		var = J.emMaiusc(J.limpaSt(var));
// carregar arq
		ArrayList<String> lst = J.openStrings(cam);
// correr linha por linha
		for(String st:lst){
// 		se encontrou o valor, retorna-lo e sair do metodo
			J.extTokens(st);
			if(J.iguais(J.tk1,var)){
				String ret = J.truncarAntes('=',st);
				ret = J.limpaSt(ret); // remove espacos iniciais
				J.esc("valor encontrado: "+var+" = "+ret);
				return ret;
			}
//		senao, continuar o loop			
		}
// se o loop acabou entao
// 	ou reportar valor nao encontrado e retornar um valor neutro		
//  ou avisar e interromper o programa
		J.impErr("valor nao encontrado: |"+var+"|  em  |"+cam+"|","J.leCfgSt()");
		return "???";
	}	


	
	public static String aUltimaPasta(String p){
		// em "c://pasta1//pasta2//pasta3//" retorna "pasta3"
		
/* selecionar a ultima(aa direita) pasta de arq em dir
				c://java//jf1//teste//
	 1 remove o ultimo char se for '/'
				c://java//jf1//teste/
	 2 repete a ultima operacao
				c://java//jf1//teste
	 3 inverte o string
				etset//1fj//avaj//:c
	 4 truncar ateh '/'
				etset
	 5 inverter de novo
				teste		
*/		
		//1 remove o ultimo char se for '/'
		if(p.charAt(p.length()-1)=='/') p = remUltChar(p);
		
		// 2 repete a ultima operacao
		if(p.charAt(p.length()-1)=='/') p = remUltChar(p);
		
		// 3 inverte o string
		p = J.invStr(p);
		
		// 4 truncar ateh '/'
		p = truncarAte('/',p);

		// 5 inverter de novo
		p = J.invStr(p);
		
		return p;
	}
	public static String dirAcima(String p){ 
		return umNivelAcima(p); // mais intuitivo este.
	}
	public static String umNivelAcima(String p){ // tah dirAcima()
		// p manipulacao de caminhos de arq
		// se for "c://", retorna o mesmo valor
		// ?e se for null ou vazio??? q valor seria melhor retornar???
		p = J.invStr(p);
		p = J.truncarAntes('/',p);
		p = J.invStr(p);
		return p+"/";
	}
	
	
	

	public static String camArq(String p){
		File f = new File(p);
    //String cam = f.getAbsolutePath();
    String cam = f.getParent();
		if(iguais(cam,".")) return "";
		return cam;
	}
	/* isto é so p windows
	public static String camArq(String p){
		//p = "c:\\java\\wavs\\text.txt";
		p = J.invStr(p); // "txt.txet\\svaw\\avaj\\:c"
		p = truncarAntes('\\',p); // "svaw\\avaj\\:c"
		//J.sai("[[["+p.length()+"]]]");
		if(p.length()==0) return ".";
		p = '\\'+p; // "\\svaw\\avaj\\:c"
		p = J.invStr(p); // "c:\\java\\wavs\\"		
		//J.sai(p);
		return p;
	}
	public static String camArq_(String p){
		// precisa testar melhor essa aqui
		if(p==null) return ".";
		if(!tem('/',p)) return "."; // diretorio atual. Tah certo isso?
		
		p = corrCam(p,"jf1"); // "c:\java\jf1\aqui.jf1"
		p = J.invStr(p); // "1fj.iuqa\1fj\avaj\:c"
		p = truncarAntes('\\',p); // "1fj\avaj\:c"
		p = '/'+p; // "\1fj\avaj\:c"
		p = J.invStr(p); // "c:\java\jf1\"		
		return p;
	}
	*/
	public static String nomeExtArq(String p){
		if(p==null) return null;
		if(!J.tem('/',p)) return p;
		
		//  c:\\win\\arqs\\text.txt		
		p = J.invStr(p); // txt.txet\\sqra\\niw\\:c
		p = J.truncar('/',p);		
		p = J.invStr(p);
		return p;		
	}
	public static String nomeExtArq_(String p){
		// ESTE ERA P WIN
		if(p==null) return null;
		if(!J.tem('\\',p)) return p;
		
		//  c:\\win\\arqs\\text.txt		
		p = J.invStr(p); // txt.txet\\sqra\\niw\\:c
		p = J.truncar('\\',p);		
		p = J.invStr(p);
		return p;		
	}
	public static String nomeExtArq__(String p){
		return nomeArq(p)+"."+extArq(p);
	}
	public static String nomeArq(String p){
		// retorna apenas o nome do arq, sem ext ou caminho
		// casos:
		// x		null
		// x		""
		// x		nome.ext
		// x		nome.extencaoGrande
		// x    nome.separado.com.ponto.ext
		// x		nome.e
		// x 		nome.
		// x		pasta\nome.ext			
		// x		pasta\nome.extGrande
		// x 		pasta\nome.com.ponto.ext
		// x		c:\nome.ext
		// x		c:\pasta\nome.ext
		// x		c:\pasta\nome.extencaoGrande
		// x		c:\pasta\nome.duas.extencoes
		// x		c:\pasta1\pasta2\nome.ext
		// x		c:\pasta\...\pastaX\nome.ext
		if(p==null) return null;
		p = invertString(p);
		p = truncarAntes('.',p);
		p = truncarAte('/',p);
		p = invertString(p);
		return p;
	}
	public static String invertString(String p){
		if(p==null) return null;
		if(p=="") return "";
		char ca = 0;
		String st="";
		for(int q=0; q<p.length(); q++){
			ca = p.charAt(q);
			st= ca + st;
		}	
		return st;
	}
	public static String extArq(String p){
// retorna apenas a extencao do arquivo
/* CASOS
ok	apagar01.jf1
ok	apagar01.jf
ok	apagar01.
ok	apagar01
ok	apagar01.extencaoGrande
ok	apagar01.duas.extencoes
ok  c:\\universo\\julico.o.mestre
ok	null
*/	
		if(p==null)	return "";		
		boolean para=false;		
		String st="";
		char ca=0;
		if(J.tem('.',p))
		for(int k=p.length()-1; k>=0; k--){
			ca=p.charAt(k);
			if(ca=='.') break;
			if(!para)st=ca+st;
		}
		st = J.emMaiusc(st);
		return st;
	}
	public static String formatFloat(float v, String fr){
		//fr = "R$ #,##0.00";
		DecimalFormat d = new DecimalFormat(fr);
		return d.format(v);		
/*
This will format the floating point number 1.23456 up-to 2 decimal places, because we have used two after decimal point in formatting instruction %.2f, f is for floating point number, which includes both double and float data type in Java. Don't try to use "d" for double here, because that is used to format integer and stands for decimal in formatting instruction. 
Read more: https://www.java67.com/2014/06/how-to-format-float-or-double-number-java-example.html#ixzz7QY3N6Csq
*/		
	}
	public static int tamArq(String p){
		// tamanho do arquivo em bytes
		// 1024 eh um numero importante aqui
		File arq = new File(p);
		if(!arqExist(p)) return 0;
		return (int)arq.length();		
	}
	public static int intKarq(int k, String cam){
		// FUNCAO: retorna o kaesimo integer do arq cam
		// nao eh arquivo ASCII, eh arquivo binario
		if(!arqExist(cam)) {
			impErr("O arquivo nao existe: "+cam,"J.intKarq()");
			return 0;		
		}	
		RandomAccessFile arq=null;
		try{
			arq = new RandomAccessFile(cam,"rw");
			arq.seek(k);
			int w = (int)arq.readInt(); 
			arq.close();
			return w;
		}catch(IOException e){
			try { arq.close(); } catch(IOException ee) {};
			impErr("falha consultando arquivo: "+cam,"J.intKarq()");
			return 0;
		}
	}		
	
// === CAMINHOS DE ARQUIVOS ================
	public static String remCharDuplo(char ch, String cam){
		// remove qq caracter quando vizinhos identicos, deixando apenas um desses caracteres
		// exemplo, com ch='.': "apagar..jf1" -> "apagar.jf1"
		// usei p tratar caminhos de arquivo
		// uso interno
		char ca=0, ca_=0;
		String st ="";
		for(int q=0; q<cam.length(); q++){
			ca = cam.charAt(q);
			if(ca==ch && ca_==ch)
				;
			else 
				st+=ca;
			ca_=ca;
		}
		return st;
	}
	public static String duplicaBarra(String cam){
		// usei p tratar caminhos de arquivo
		// duplica a barra subindo '/', mas a descendo nao
		// uso interno
		char ca=0, caa=0;
		String st="";
		for(int q=0; q<cam.length(); q++){
			ca = cam.charAt(q);
			caa = 0;
			if(q<cam.length()-1)
				caa = cam.charAt(q+1);
			st+=ca;
			if(ca=='/')
			if(caa!='/')	
				st+='/';
		}
		return st;
	}
	public static String remExtArq(String cam){
		// remove a extencao do caminho
		// se tiver varias extencoes, remove soh a ultima
		if(J.tem('.',cam)){
			boolean vai=false;
			char ca=0;
			String st="";
			for(int q=cam.length()-1; q>=0; q--){
				if(vai) st=ca+st;
				ca = cam.charAt(q);
				if(ca=='.') vai=true;
			}
			if(vai) st=ca+st;
			return st;
		} 
		return cam; // quando nao tem extencao p extrair
		
	}	

	public static String corrCam_(String cam, String ext){
		// do windowns
		// essa tah fraquinha
		cam = remExtArq(cam);
		return corrCam(cam+'.'+ext); // mas essa tah boa
	}
	public static String corrCam(String cam, String ext){
		// do linux // 111111111111111111111111111
		ext = J.remChar('.',ext);
		ext = J.emMinusc(ext);		
		if(!J.tem('/',cam)){
			if(J.iguais(ext,"wav")){
				cam = "wav/"+cam;
				if(!J.tem(".wav",cam)) cam+=".wav";
			}
			if(J.iguais(ext,"jf1")){
				cam = "jf1/"+cam;
				if(!J.tem(".jf1",cam)) cam+=".jf1";
			}			
			if(J.iguais(ext,"jf2")){
				cam = "jf2/"+cam;
				if(!J.tem(".jf2",cam)) cam+=".jf2";
			}						
			if(J.iguais(ext,"j3d")){
				cam = "j3d/"+cam;
				if(!J.tem(".j3d",cam)) cam+=".j3d";
			}						
			if(J.iguais(ext,"win")){
				cam = "win/"+cam;
				if(!J.tem(".win",cam)) cam+=".win";
			}						
		}
		return cam;
	}	
	
	public static String corrCam(String cam){
		// essa ficou excelente, MAS A VARIACAO Q A PRECEDE NAO AJUDOU MUITO (aquela com dois parametros)
		
		// cam = J.emMinusc(cam); // isso atrapalha p associacao de arquivos no editor jf1
		if(cam==null)cam="";
		cam = J.semSpcIni(cam);
		cam = J.semSpcFinal(cam);
		cam = J.trocaChar('\\','/', cam);
		//cam = remCharDuplo('/',cam);
		if(cam.length()<=0) cam+=" ";
		if(cam.charAt(0)=='/') cam=J.remPrimChar(cam);
		if(cam.charAt(0)=='/') cam=J.remPrimChar(cam);
		
		cam = duplicaBarra(cam); // soh p barra subindo
		
		boolean ehPasta = (J.ultChar(cam)=='/');
		
		if(!J.tem('.',cam)) // mas poderia tb ser um arq sem extencao
		//if(!J.tem('/',cam)) //??? isso nao faz sentido, jah q poderia ser o caminho de um arquivo numa subpasta
			ehPasta=true;
		
		
		// extraindo a extencao, caso nao seja uma pasta
		if(!ehPasta){
			
			if(J.ultChar(cam)=='.')
				cam = J.remUltChar(cam);
			
			String ext = "";
			if(J.tem('.',cam)){
				ext = J.extArq(cam);
			} else if(!tem('/',cam)) {// veja q pode ser um arquivo numa subPasta
				// tentar deduzir extencao caso exista uma pasta
				
				// ERRO DE LOGICA AQUI!!!!
				
				if(J.tem("j3d//",cam)) ext = "j3d"; 
				if(J.tem("jpg//",cam)) ext = "png";
				if(J.tem("win//",cam)) ext = "win";
				if(J.tem("jf1//",cam)) ext = "jf1"; 
				if(J.tem("jf2//",cam)) ext = "jf2"; 
				if(J.tem("wav//",cam)) ext = "wav";				
			}
			
			// acoplando extencao
			if(!J.tem('.',cam)){
				cam+='.'+ext;
			}
			
			// acoplando comeco do caminho em caminhos relativos tipo "jf1//apagar01.jf1"
			if(!tem('/',cam)){
				// extencoes mais comuns devem ficar primeiro
						 if(iguais("jf1",ext)) cam="jf1//"+cam;
				else if(iguais("jf2",ext)) cam="jf2//"+cam;
				else if(iguais("j3d",ext)) cam="j3d//"+cam;
				else if(iguais("win",ext)) cam="win//"+cam;
				else if(iguais("wav",ext)) cam="wav//"+cam;
				else if(iguais("jpg",ext)) cam="jpg//"+cam;
				else if(iguais("jpeg",ext)) cam="jpg//"+cam;
				else if(iguais("png",ext)) cam="jpg//"+cam;
				else if(iguais("jpeg",ext)) cam="jpg//"+cam;
				else if(iguais("ttf",ext)) cam="c://windows//fonts//"+cam;
			}
				
			cam = remCharDuplo('.',cam);
			
			// ultima gambiarra
			if(J.iguais(ext,"png"))
			if(!J.arqExist(cam)) 
				cam = J.camArq(cam)+J.nomeArq(cam)+".jpg";
				
			
		}
		

		return cam;
	}	
/*	TODAS ESSAS ABAIXO PASSARAM NESSE corrCam() (serah mesmo, andei modificando o metodo e nao testei)
		String lis[] = new String[]{ //fffffffffff
		"c:\\java\\win", 
		"c:/java//apagar01.wav.jpg",
		"c:/java//win",
		"c:\\java\\win\\",
		"c:\\java//jf1\\apagar02",
		"win\\",
		"\\win\\",
		"win//",
		"apagar01.jpg",
		"apagar01.jpeg",
		"apagar01.wav",
		"apagar01.win",
		"apagar01.jf1",
		"apagar01",
		"jf1//apagar01",
		"jf1//apagar01.jf1",
		"//jf1//apagar01.jf1",
		"\\jf1\\apagar01.jf1",
		"c:\\java\\jf1\\apagar01.jf1",
		"c://java\\jf1\\apagar01.jf1"};

*/  
	
	
/*
CLASSE FILE:
boolean canRead() -> retorna true se for possível ler o arquivo, falso o contrário
boolean canWrite() -> retorna true se for possível escrever no arquivo, falso o contrário
boolean exists() -> retorna true se o diretório ou arquivo se o objeto File existe, falso o contrário
boolean isFile() -> retorna true se o argumento passado ao construtor da File é um arquivo, falso o contrário
boolean isDirectory() -> retorna true se o argumento passado ao construtor da File é um diretório, falso o contrário
boolean isAbsolute() -> retorna true para caso o argumento seja de um caminho absoluto, falso o contrário
String getAbsolutePath() -> retorna uma String com o caminho absoluto do diretório ou arquivo
String getName() -> retorna uma String com o nome do arquivo ou do diretório
String getPath() -> retorna uma Sting com o caminho do arquivo ou diretório
String getParent() -> retorna uma String com o caminho do diretório pai (acima;anterior) ao do arquivo ou diretório atual
long length() -> retorna um tamanho, em bytes, do arquivo ou inexistente, caso seja diretório
long lastModified() -> retorna o tempo em que o arquivo ou diretório foi modificado pela última vez; varia de acordo com o sistema
String[] list() -> retorna um array de Strings com o conteúdo do diretório, ou null se for arquivo
OBS: 
	File arq = new File(""); 
	String[] lst = arq.list(); // isso retorna a listagem inteira do diretorio atual
	// nao dah p usar caracteres coringa!!! Tem q implementar na unha!
*/	

	public static char primChar(String st){
		if(st==null) return 0;
		if(st.length()<0) return 0;
		return st.charAt(0);
	}
	public static char ultChar(String st){
		if(st==null) return 0;
		if(st.length()<0) return 0;
		if(st.length()==1) return st.charAt(0);
		return st.charAt(st.length()-1);
	}	
	public static int contChar(char ca, String p){
		// conta o numero de caracteres "ca" do string
		if(p==null) return 0;
		int c=0;
		for(int k=0; k<p.length(); k++)
			if(p.charAt(k)==ca) c++;
		return c;	
	}
	
	public static String invStr(String p){
		String st="";
		if(p!=null)
		for(int k=0; k<p.length(); k++)
			st=p.charAt(k)+st;
		return st;	
	}


	public static ArrayList<String> listarArqs(String cam, String fil, String ext){
		return listArqs(cam,fil,ext); // por compatibilidade
	}
		
	public static ArrayList<String> listArqs(String cam, String fil, String ext){
// OBS: daria p usar File.isDirectory() e File.isFile()		
// na gambiarra dah p listar soh diretorios fazendo ext=" ", mas vai listar tb arquivos sem extencao (raro existir)
/*	INSTRUCOES
			[P LINUX, PRECISA CONFIRMAR TUDO ISTO ABAIXO]
			- cam nulo ajusta p pasta atual 
			- cam "win//" seria a subpasta dentro da pasta atual
			- fil precisa ser lapidado, mas jah funciona rasoavelmente
				> "*Fonte*", "Fonte*", "*Fonte" e "fonte" dao o mesmo resultado.
				> seria melhor se reconhecesse "apenas o comeco" e o caracter coringa "?" p um char individual
				> depois, talves
			- ext ".bat" ou "bat" dah na mesma
			- permite ateh 6 extencoes diferentes separadas por ";". Ex: "png;jpg;jpeg..." (acho q falta testar com o numero maximo de extencoes, mas jah tah boa)
*/

		{ // corrigindo parametro cam
			// "win//" seria a subpasta dentro da pasta atual
			if(cam==null) cam = "."; // a pasta atual
			if(cam.equals("")) cam = "."; 
			if(cam.equals(" ")) cam = "."; 
		}
		
		{ // corrigindo parametro fil
			// pode ser lapidado
			// "*abacate*" e "abacate" dao o mesmo efeito e isso nao eh bom
			if(fil!=null){
				if(fil.equals("*")) fil=null;
				if(fil.equals("")) fil=null;
			}
		}		
		
		
		String // multiplas extencoes com ";" no par ext
			ext1="",
			ext2="",
			ext3="",
			ext4="",
			ext5="",
			ext6="";
		{// corrigindo parametro ext
			// ext null inclui qq extencao
		if(ext!=null){
			if(ext.equals(".*")) ext = null;
			if(ext.equals("*"))  ext = null;
			if(ext.equals(""))  ext = null;
			ext = J.emMaiusc(ext);
			if(ext!=null)
			if(ext.charAt(0)=='.') 
				ext = J.remPrimChar(ext);
			}	
			if(J.tem(";",ext)){
				// desmembrar em varias vars
				int ne = J.contChar(';',ext)+1; // numero de extencoes: 2";" = 3 extencoes
				String sts[] = ext.split(";");
				
				if(ne>=2){
					ext1=sts[0];
					ext2=sts[1];					
				}
				if(ne>=3) ext3=sts[2];
				if(ne>=4) ext4=sts[3];
				if(ne>=5) ext5=sts[4];
				if(ne>=6) ext6=sts[5];
				

			}
		}	
		
		File arq = new File(cam);
		String[] ls = arq.list(); // arqs e diretorios
		ArrayList<String> ret = new ArrayList<>();

		if(ls!=null)
		for(String s:ls){
			if(ext==null 
				|| J.iguais(ext,J.extArq(s))
				|| J.iguais(ext1,J.extArq(s))
				|| J.iguais(ext2,J.extArq(s))
				|| J.iguais(ext3,J.extArq(s))
				)
			if(fil==null || J.tem(fil,s))
			// if(ehArq(s)) // ISSO TAH DANDO PROBLEMA
				ret.add(s);
		}	
		
		ret = diretoriosPrimeiro(ret);
		
		if(ls==null || ret.size()<=0) {
			J.esc("ATENCAO: lista de arquivos vazia.");
			J.esc("	cam |"+cam+"|");
			J.esc("	fil |"+fil+"|");
			J.esc("	ext |"+ext+"|");
		}			
		if(ls!=null)
		J.esc("listando "+ls.length+" arquivos por: ");
			J.esc("	cam |"+cam+"|");
			J.esc("	fil |"+fil+"|");
			J.esc("	ext |"+ext+"|");
		
		Collections.reverse(ret);// arrumar opcoes p isso depois
		
		return ret;
	}	
	public static ArrayList<String> diretoriosPrimeiro(ArrayList<String> p){
		ArrayList<String> ls = new ArrayList<>();
		for(String s:p)
			if(J.iguais(extArq(s),"")) // diretorio
				ls.add(s);
		for(String s:p)
			if(!J.iguais(extArq(s),"")) // diretorios
				ls.add(s);
		return ls;
	}

	
	public static String[] filtraExt(String ls[], String ext){
		if(ls==null) J.impErr("!a lista estah vazia!","J.filtraExt()");
		
		// funcao: filtra listagem de arquivos. Extencao diferente de ext fica fora.
		// autocorrecao:
		// - extencao maiusc
		// - extencao sem ponto
		// - ext "ignoreIssoEpegueApenasAextencao.ext" usa apenas "ext"; se passar o nome do arquivo inteiro como parametro de pesquisa, ignora-se o nome e pega-se apenas a extencao.
		ext = extArq("_."+ext); // maiusculo e sem ponto
		int val=0;
		for(int k=0; k<ls.length; k++){
			if(!extArq(ls[k]).equals(ext)) 
				ls[k]="-";
			else val++;
		}
		String bom[] = new String[val];
		int cb=0;
		for(int k=0; k<ls.length; k++){
			if(!ls[k].equals("-")) bom[cb++]=ls[k];
		}		
		return bom;
	}
	public static String[] filtraIniExt(String ls[], String ini, String ext){
		// funcao: filtra listagem de arquivos. 
		// 		Extencao diferente de ext fica fora.
		//		iniciais com char diferente de ini fica fora
		// autocorrecao:
		// - extencao maiusc
		// - extencao sem ponto
		// - ext "ignoreIssoEpegueApenasAextencao.ext" usa apenas "ext"; se passar o nome do arquivo inteiro como parametro de pesquisa, ignora-se o nome e pega-se apenas a extencao.
		for(int k=0; k<ls.length; k++){
			ls[k] = J.emMaiusc(ls[k]);
		}

		ext = extArq("_."+ext); // maiusculo e sem ponto
		int val=0;
		char ca = ini.charAt(0);
		for(int k=0; k<ls.length; k++){
			if(	 (!extArq(ls[k]).equals(ext)) 
				|| (ls[k].charAt(0)!=ca)) 
				ls[k]="-";
			else val++;
		}
		String bom[] = new String[val];
		int cb=0;
		for(int k=0; k<ls.length; k++){
			if(!ls[k].equals("-")) bom[cb++]=ls[k];
		}		
		return bom;
	}	

// Obsoletas, mas deixei aqui por compatibilidade	
	public static void dialMsg(String st){
		if(st==null) st="nulo";
		JOptionPane.showMessageDialog(null, st);	
	}
	public static void aviso(String st){
		// redirecionado
		dialMsg(st);
	}
	public static String prompt(String stt, String st){
		// retorna "null" se clicar em cancel
		return JOptionPane.showInputDialog(stt, st);
	}	
	public static String dialPrompt(String st){
		// preferir apenas "prompt" q eh mais intuitivo
    return prompt(st);
	}
	public static String prompt(String st){
		return prompt(null, st);
	}
	public static boolean confirm(String st){
		// retorna false se fechar a janela no "X"
		int ret = JOptionPane.showConfirmDialog(null, st, "confirme", JOptionPane.YES_NO_OPTION);
		return ret == JOptionPane.YES_OPTION;	
	}
	public static boolean dialYesNo(String st){
		return confirm(st);
	}	
	
// horizonte e paleta	
	public static void defHor(int crChao, int crCeu){
		// substituir o cod abaixo centralizando no metodo q usa Color como parametros. Aquele deve ser o metodo-mestre.
		J.cor[210]=J.cor[crChao];
		J.cor[219]=J.cor[crChao].brighter().brighter().brighter();
		J.cor[220]=J.cor[crCeu];
		J.cor[229]=J.cor[crCeu].brighter().brighter().brighter();
		degrade(210,219);
		degrade(220,229);
	}
	public static void defHor(int crChao, int crCeu, int crAtm){
		defHor(J.cor[crChao], J.cor[crCeu], J.cor[crAtm]);
	}
	public static void degrade(int cr, int crr){
		if(cr>crr){int k=cr; cr=crr; crr=k; }
		int lar = crr-cr;
		float r,g,b,rr,gg,bb;
		r = J.cor[cr].getRed();
		g = J.cor[cr].getGreen();
		b = J.cor[cr].getBlue();
		rr = J.cor[crr].getRed();
		gg = J.cor[crr].getGreen();
		bb = J.cor[crr].getBlue();		
		float pr,pg,pb;
		pr=(rr-r)/lar;
		pg=(gg-g)/lar;
		pb=(bb-b)/lar;
		Color w=null;
		r-=pr;
		g-=pg;
		b-=pb;
		for(int k=cr; k<=crr; k++){			
			r+=pr;
			g+=pg;
			b+=pb;
			w = new Color((int)r,(int)g,(int)b);
			J.cor[k]=w;
		}
	}	
	
	public static void degradeAlfa(int cr, int crr){
		// parece q nao t? funcionando. Testar melhor.
		if(cr>crr){int k=cr; cr=crr; crr=k; }
		int lar = crr-cr;
		float r,g,b,a,rr,gg,bb,aa;
		r = J.cor[cr].getRed();
		g = J.cor[cr].getGreen();
		b = J.cor[cr].getBlue();
		a = J.cor[cr].getAlpha();
		rr = J.cor[crr].getRed();
		gg = J.cor[crr].getGreen();
		bb = J.cor[crr].getBlue();		
		aa = J.cor[crr].getAlpha();		
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
		for(int k=cr; k<=crr; k++){			
			r+=pr;
			g+=pg;
			b+=pb;
			a+=pa;
			w = new Color((int)r,(int)g,(int)b, (int)a);
			J.cor[k]=w;
		}
	}	
	
	public static void defHor(Color crBxo, Color crCma, Color crAtm){
		if(crBxo!=null)J.cor[210]=crBxo;
		if(crAtm!=null)J.cor[219]=crAtm;
		if(crCma!=null)J.cor[220]=crCma;
		if(crAtm!=null)J.cor[229]=crAtm.brighter();
		degrade(210,219);
		degrade(220,229);
	}

	public static double getDuration(String cam){
		// retorna a duracao do arq wav em segundos
		// acho q funciona p varios formatos de audio, mas tem q testar p outros q nao sejam wav
		// seria bom testar isto melhor e incorporar a "JWav.java" (?j? fiz???)
		
		if(!J.tem('.',cam)) cam = cam+".wav";
		if(!J.tem('/',cam)) cam = "wav//"+cam;
			
		if(!J.arqExist(cam)){
			J.impErr("!arquivo perdido: |"+cam+"|", "J.getDuration()");
			return 0;
		}
		File file = new File(cam);
		try{
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
			AudioFormat format = audioInputStream.getFormat();
			long frames = audioInputStream.getFrameLength();
			//double durationInSeconds = (frames+0.0) / format.getFrameRate();					
			return (frames+0.0) / format.getFrameRate();				
		} catch(Exception e){
			impErr("!erro obtendo duracao de audio","J.getDuration()");
			e.printStackTrace();
			return 0d;
		}
	}

		static boolean opPltImpNum=true;
	public static void impPlt(int i, int j){ // impPal
		int lar = 3, alt = 14, w=3;				
		for(int q=0; q<=255; q++){
			J.impRetRel(q    ,0, i+q*w, j, lar, alt);
			J.impRetRel(q+256,0, i+q*w, j+alt, lar, alt);
			if(opPltImpNum){
				if(q>31)
				if(q<255)
				if(q%10==9)
				impText(i+q*w-22, j+12, J.intEmSt000(q));
			}		
		}
	}

/* COMO USAR O ITERATOR
			Iterator<Triang> it = tri.iterator();
			while(it.hasNext()){
				if (it.next().id==-1) it.remove();
			}
*/	
/* COMO USAR COMPARATOR/SORT
-1- escrever os imports devidos
		import java.util.Collections;
		import java.util.Comparator;
		import java.util.ArrayList;
		
-2- criar a classe que se quer tratar com seu respectivo arrayList
		ArrayList<Abob> abob = new ArrayList<>();
	public class Abob{
			int tam=10;
		public Abob(int tm){
			tam = tm;			
		}
	}
-3- adicionar elementos	
	abob.add(new Abob(10));
	abob.add(new Abob(20));
	abob.add(new Abob(30));
-4- criar metodo comparador como var global (mas poderia ser local tb)
	Comparator cmp = new Comparator(){
		public int compare(Object o, Object oo){
			Abob a = (Abob)o; // repare este cast
			Abob aa = (Abob)oo;
			if(a.tam>aa.tam) return -1; // este sinal define se ? ordem crescente ou decrescente
			return +1;
		}
	};

-5- evocar Collections.sort() com a lista e o comparador
		Collections.sort(abob,cmp);

*/
	
	
	/*
	DEU WARNING neste metodo. Isolei pq não uso tanto este metodo.
	public static ArrayList concatenarArrLst(ArrayList p, ArrayList pp){ // tags: unirArrLst unirArrayList juntarArrLst juntarArrayList somarArrLst somarArrayList
		if(p==null) J.impErr("!ERRO: arrayList nulo","J.concatenarArrLst() p ");
		if(p.size()<=0) J.impErr("!ERRO: arrayList vazio","J.concatenarArrLst() p");
		if(pp==null) J.impErr("!ERRO: arrayList nulo","J.concatenarArrLst() pp");
		if(pp.size()<=0) J.impErr("!ERRO: arrayList vazio","J.concatenarArrLst() pp");		
		ArrayList w = new ArrayList<>();
		int tam = p.size();
		for(int q=0; q<tam; q++)
			w.add(p.get(q));
		tam = pp.size();
		for(int q=0; q<tam; q++)
			w.add(pp.get(q));			
		return w;		
	}
	
	DEU WARNING neste metodo tb. Isolei pq não uso tanto este metodo.	
	public static ArrayList reverseArrLst(ArrayList p){
		// tá funcionando bem.
		if(p==null) J.impErr("!ERRO: arrayList nulo","J.reverseArrLst()");
		if(p.size()<=0) J.impErr("!ERRO: arrayList vazio","J.reverseArrLst()");
		ArrayList w = new ArrayList<>();
		int tam = p.size();
		for(int q=0; q<tam; q++)
			w.add(p.get(tam-q-1));
		return w;
	}
*/	
	public static boolean remIntArrLst(int min, int max, ArrayList arr){
		// remove um intervalo, cuidado com esse "n" no nome do metodo, p nao confundir com o outro metodo abaixo.
		if(min<0) return false;
		if(max<0) return false;
		boolean foi=false;
		Iterator i = arr.iterator();		
		for(int c=0; c<=arr.size(); c++){
			if(i.hasNext()) {
				i.next();
				if(J.noInt(c,min,max)) {i.remove(); foi=true; }
			} 		
		}
		return foi;
	}
	public static boolean remItArrLst(int p, ArrayList arr){
		if(p<0) return false; // frequentemente acontece: "p==-1"
		if(p>arr.size()) return false;
		if(arr==null) return false;
		Iterator i = arr.iterator();
		for(int k=0; k<=arr.size(); k++){
			if(i.hasNext()) {
				i.next();
				if(k==p) {i.remove(); return true; }
			} else return false;	
		}
		return false;
	}	
	public static boolean remItArrLst(Object p, ArrayList arr){
		// nao funciona p arr de strings
		if(p==null) return false;
		if(arr.size()==0) return false;
		Iterator i = arr.iterator();
		while(i.hasNext()){
		  if(i.next()==p) {
				i.remove(); 
				return true;
			}
		}	
		return false;
	}
	public static boolean remStArrLst(String p, ArrayList arr){
		if(p==null) return false;
		if(arr.size()==0) return false;
		p = J.emMaiusc(p);
		Iterator i = arr.iterator();
		while(i.hasNext()){
		  if(J.emMaiusc((String)i.next()).equals(p)) {
				i.remove(); 
				return true;
			}
		}	
		return false;
	}	
	public static boolean remUltArrLst(ArrayList arr){
		// remove o ultimo apenas
		if(arr.size()<=0) return false;
		Iterator i = arr.iterator();
		while(i.hasNext()) 
			i.next();
		i.remove();		
		return true;
	}
	public static boolean remPrimArrLst(ArrayList arr){
		// remove o primeiro apenas
		if(arr.size()<=0) return false;
		Iterator i = arr.iterator();
		i.next();
		i.remove();		
		return true;
	}	
	public static int indexOf(Object o, ArrayList lis){
		// retorna a ordem do objeto o dentro do arr lis
		// retorna -1 se não encontrou
		if(lis.size()==0) return -2;
		if(lis==null) return -3;
		if(o==null) return -4;
		
		int c=-1;
		for(Object w:lis){
			c++;
			if(w==o) return c;
		}
		return -1;
	}
/* COMO FAZER UM SORT NUM ARRAYLIST
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
...
	public void ordenar(){
		Collections.sort(arr, new Comparator<String>() {
        @Override
        public int compare(String s, String ss){
            return  s.compareTo(ss);
        }
    });		
	}
*/
/* ARRAYLIST SERIALIZATION, EXEMPLO BOM; tag serializacao
import java.io.*;  
import java.util.*;  
 class Fonte126 {  
  
        public static void main(String [] args)  
        {  
          ArrayList<String> al=new ArrayList<String>();  
          al.add("Ravi");    
          al.add("Vijay");    
         al.add("Ajay");    
           
         try  
         {  
             //Serialization  
             FileOutputStream fos=new FileOutputStream("file");  
             ObjectOutputStream oos=new ObjectOutputStream(fos);  
             oos.writeObject(al);  
             fos.close();  
             oos.close();  
             //Deserialization  
             FileInputStream fis=new FileInputStream("deletar.tmp");  
             ObjectInputStream ois=new ObjectInputStream(fis);  
           ArrayList  list=(ArrayList)ois.readObject();  
           System.out.println(list);    
         }catch(Exception e)  
         {  
             System.out.println(e);  
         }  
      }
 }


*/


	

		static JWav wav = null; // precisa ser externo p não bugar
	public static void playArqWav(String cam){
		if(wav!=null) wav.stop();
		wav = new JWav(cam);
		wav.play();
	}
	public static void playArqWav(int p){
		// bom p debug
		String cam="zero";
		switch(p){
			case 0: cam="zero"; break;
			case 1: cam="um"; break;
			case 2: cam="dois"; break;
			case 3: cam="tres"; break;
			case 4: cam="quatro"; break;
			case 5: cam="cinco"; break;
			case 6: cam="seis"; break;
			case 7: cam="sete"; break;
			case 8: cam="oito"; break;
			case 9: cam="nove"; break;
		}		
		playArqWav('_'+cam);
	}	

	public static float maior(float p, float pp){
		if(p>pp) return p;
		return pp;
	}
	public static float menor(float p, float pp){
		if(p<pp) return p;
		return pp;
	}	
	public static int menor(int p, int pp){
		if(p<pp) return p;
		return pp;
	}	  
	public static int maior(int p, int pp){
		if(p>pp) return p;
		return pp;
	}
	public static int maior(int p, int pp, int ppp){
		int r = p;
		if(pp>r)r=pp;
		if(ppp>r)r=ppp;
		return r;
	}
	public static int maior(int p, int pp, int ppp, int pppp){
		int r = p;
		if(pp>r)r=pp;
		if(ppp>r)r=ppp;
		if(pppp>r)r=pppp;
		return r;
	}
	
	public static float maior(float p, float pp, float ppp){
		float r = p;
		if(pp>r)r=pp;
		if(ppp>r)r=ppp;
		return r;
	}
	
	public static int menor(int p, int pp, int ppp){
		int r = p;
		if(pp<r)r=pp;
		if(ppp<r)r=ppp;
		return r;	
	}	
	public static int menor(int p, int pp, int ppp, int pppp){
		int r = p;
		if(pp<r)r=pp;
		if(ppp<r)r=ppp;
		if(pppp<r)r=pppp;
		return r;	
	}	
	
	public static float menor(float p, float pp, float ppp){
		float r = p;
		if(pp<r)r=pp;
		if(ppp<r)r=ppp;
		return r;	
	}	
	

		static int stroke = 4;

	public static void impBrick(Color cor, int i, int j, int ii, int jj){ // tag impBloco, impCubo, impTijolo
		int s=stroke; // stroke
		int iii=ii-i;
		int jjj=jj-j;
		// cores
		Color crl= altColor(cor, +60);
		Color crs= altColor(cor, -60);
		
		// corpo
		g.setColor(cor);
		g.fillRect(i,j,iii,jjj);
		
		// sombra
		g.setColor(crs);
		g.fillRect(i+s,jj-s,iii-s,s);	// baixo
		g.fillRect(ii-s,j+s,s,jjj-s); // dir
		
		// luz
		g.setColor(crl);
		g.fillRect(i,j,s,jjj-s);	// esq
		g.fillRect(i,j,iii-s,s);	// cima		
	}	
	public static void impBrickRel(Color cr, int i, int j, int ii, int jj){
		impBrick(cr, i,j,i+ii,j+jj);
	}
	public static void impBrickRel(int cr, int i, int j, int ii, int jj){
		impBrick(J.cor[cr], i,j,i+ii,j+jj);
	}
	public static void impBrick(int cr, int i, int j, int ii, int jj){
		impBrick(J.cor[cr], i,j,ii,jj);
	}	

	
	public static int unidade(int p){
		return p % 10;
	}
	public static int dezena(int p){
		return ((int)(p/10f)) % 10;
	}	
	public static int centena(int p){
		return ((int)(p/100f)) % 10;
	}		
	public static int milhar(int p){
		return ((int)(p/1000f)) % 10;
	}
	

	public static int rndInt(int n100, int n120){
		// retorna qq valor do intervalo aleatoriamente
		// inclui os dois numeros parametros nesse retorno
		return n100+R(n120-n100+1);
	}
	public static int retInt(int i, int p1, int p2, int p3, int p4){
		switch(i){
			case 0: return p1;
			case 1: return p2;
			case 2: return p3;
			case 3: return p4;
		}
		J.impErr("!fora do indice de retorno: "+i,"J.retInt()");
		return 0;
	}
	public static int incR(int p, int min, int max){
		p++;
		if(p>max) p=min;
		return p;
	}
	public static int decR(int p, int min, int max){
		p--;
		if(p<min) p=max;
		return p;
	}	
	public static String rndSt(String st1, String st2){
		switch(J.R(2)){
			case 1: return st1;
			default: return st2;
		}		
	}
	public static String rndSt(String st1, String st2, String st3){
		switch(J.R(3)){
			case 1: return st1;
			case 2: return st2;
			default: return st3;
		}
	}
	public static String rndSt(String st1, String st2, String st3, String st4){
		switch(J.R(4)){
			case 1: return st1;
			case 2: return st2;
			case 3: return st3;
			default: return st4;
		}
	}	
	public static String rndSt(String st1, String st2, String st3, String st4, String st5){
		switch(J.R(5)){
			case 1: return st1;
			case 2: return st2;
			case 3: return st3;
			case 4: return st4;
			default: return st5;
		}
	}		
	public static String rndSt(String st1, String st2, String st3, String st4, String st5, String st6){
		switch(J.R(6)){
			case 1: return st1;
			case 2: return st2;
			case 3: return st3;
			case 4: return st4;
			case 5: return st5;
			default: return st6;
		}
	}			
	public static String rndSt(String p[]){
		return p[J.R(p.length)];
	}

	public static int rnd(int[] vet){
		return vet[J.R(vet.length)];
	}

	public static float rnd(float p1, float p2){
		if(B(2)) return p1; 
		else return p2;
	}
	
	public static int rnd(int p1, int p2){
		if(B(2)) return p1; 
		else return p2;
	}
	public static int rnd(int p1, int p2, int p3){
		switch(R(3)){
			case 1:  return p1;
			case 2:  return p2;
			default: return p3;
		}
	}	
	public static int rnd(int p1, int p2, int p3, int p4){
		switch(R(4)){
			case 1:  return p1;
			case 2:  return p2;
			case 3:  return p3;
			default: return p4;
		}
	}		
	public static int rnd(int p1, int p2, int p3, int p4, int p5){
		switch(R(5)){
			case 1:  return p1;
			case 2:  return p2;
			case 3:  return p3;
			case 4:  return p4;
			default: return p5;
		}
	}
	public static int rnd(int p1, int p2, int p3, int p4, int p5, int p6){
		switch(R(6)){
			case 1:  return p1;
			case 2:  return p2;
			case 3:  return p3;
			case 4:  return p4;
			case 5:  return p5;
			default: return p6;
		}
	}
	public static int rnd(int p1, int p2, int p3, int p4, int p5, int p6, int p7){
		switch(R(7)){
			case 1:  return p1;
			case 2:  return p2;
			case 3:  return p3;
			case 4:  return p4;
			case 5:  return p5;
			case 6:  return p6;
			default: return p7;
		}
	}	
	public static int rnd(int p1, int p2, int p3, int p4, int p5, int p6, int p7, int p8){
		switch(R(8)){
			case 1:  return p1;
			case 2:  return p2;
			case 3:  return p3;
			case 4:  return p4;
			case 5:  return p5;
			case 6:  return p6;
			case 7:  return p7;
			default: return p8;
		}
	}	
	public static int rnd(int p1, int p2, int p3, int p4, int p5, int p6, int p7, int p8, int p9){
		switch(R(9)){
			case 1:  return p1;
			case 2:  return p2;
			case 3:  return p3;
			case 4:  return p4;
			case 5:  return p5;
			case 6:  return p6;
			case 7:  return p7;
			case 8:  return p8;
			default: return p9;
		}		
	}
	public static int rnd(int p1, int p2, int p3, int p4, int p5, int p6, int p7, int p8, int p9, int p10){
		switch(R(9)){
			case 1:  return p1;
			case 2:  return p2;
			case 3:  return p3;
			case 4:  return p4;
			case 5:  return p5;
			case 6:  return p6;
			case 7:  return p7;
			case 8:  return p8;
			case 9:  return p9;
			default: return p10;
		}		
	}

	public static char rndChar(String p){
		return p.charAt(J.R(p.length()));
	}

/*   	isso tah perfeito
			adotar como modelo
			dois objetos quadrados cujo "raio" eh dado por "tam"
			
		public boolean colidiu(Mob p){
			if(x-tam>p.x+p.tam) return false;
			if(y-tam>p.y+p.tam) return false;
			if(x+tam<p.x-p.tam) return false;
			if(y+tam<p.y-p.tam) return false;
			return true;
		}
*/	
/*	COLISAO ENTRE DOIS CIRCULOS
			se a distancia entre eles (a partir do centro de cada um) for menor q a soma dos dois raios, entao eles colidiram
*/
	
	public static boolean colidiu(float tam, float i, float j, float ii, float jj){
		tam*=0.5f;
		if(i>ii-tam)
		if(i<ii+tam)
			if(j>jj-tam)
			if(j<jj+tam)
				return true;
		return false;		
	}
	
	public static boolean taAcima(int tam, float i, float j, float ii, float jj){ // emCima praCima pCima tahEmCima
		// p jogo de nave 2D
		// alvo i:j na reta do canhao ii:jj
		// tam eh a margem de alcance
		tam = (int)(tam/2f);
		if(i>ii-tam)
		if(i<ii+tam)
			if(j<jj)				
				return true;
		return false;				
	}	
/*
		int altHor = (int)(J.maxYf/2f);
		int crChao = 69, crTeto=109;
	public void impChao(){		
		int c = altHor, t = altHor, 
			cc=c, tt=t, dif=0, difc=0;
		for(int q=0; q<=30; q++){
			dif = (int)(1+q*q*q*q*0.0001f);
			difc = 4+(int)(-40+q-J.cont/20f)%5;
			c+=dif;
			t-=dif;
			// chao
			if(crChao!=0)			
			J.impRet(crChao-difc,0,
				0,c,
				J.maxXf,
				cc);
			// teto
			if(crTeto!=0)
			J.impRet(crTeto-difc,0,
				0,t,
				J.maxXf,
				tt);				
			cc = c;	
			tt = t;
		}
	}
*/
// === BUFFER DE IMPRESSAO ====================	
		static BufferedImage bufImp = null;
		static Graphics grafBufImp = null;
		static int maxXbuf = 100, maxYbuf=100;

	public static void iniBuf(int mod){
		// função obsoleta, mas adaptei por compatibilidade
		JFig f = null;
		switch(mod){
			case 1: f = new JFig(J.cor[1], 320,200); break;
			case 2: f = new JFig(J.cor[2], 400,280); break;
			case 3: f = new JFig(J.cor[3], 640,400); break;
			case 4: f = new JFig(J.cor[4], 800,560); break;
			case 5: f = new JFig(J.cor[5], 800,600); break;
			case 6: f = new JFig(J.cor[6], 760,480); break;
				case 7: f = new JFig(J.cor[7], 1000,800); break;
			default:f = new JFig(J.cor[12], 100,100); break;
		}		
		bufImp = f.fig;
		grafBufImp = bufImp.getGraphics();
		maxXbuf = bufImp.getWidth();			
		maxYbuf = bufImp.getHeight();		
	}

	public static void iniBuf_(int mod){
		// abaixo pode variar. Fazer testes de frame-rate depois.
		// ?seria legal um "iniBuf(maxX,maxY)"??? Se sim, deveria ter um esquema p gerar o arquivo png automaticamente, coisa q nao estudei ainda. Depois, talvez.
		String cam = null;

		switch(mod){
			case 1: cam = "nulo320x200.png"; break;
			case 2: cam = "nulo400x280.png"; break;
			case 3: cam = "nulo640x400.png"; break;
			case 4: cam = "nulo800x560.png"; break;
			case 5: cam = "nulo800x600.png"; break;			
			case 6: cam = "nulo760x480.png"; break;			
			default:cam = "nulo100x100.png"; break; 
		}
			
			
		
		cam = J.corrCam(cam,"png");
		if(!J.arqExist(cam)){
			J.impErr("!arquivo-base do buffer de impressao nao foi encontrado: "+cam);
			return;
		}
		bufImp = null;
		maxXbuf = 100;
		maxYbuf = 100;
		try {
			bufImp = ImageIO.read(new File(cam));
			grafBufImp = bufImp.getGraphics();
			maxXbuf = bufImp.getWidth();			
			maxYbuf = bufImp.getHeight();
			J.esc("ok: buffer de impressao inicializado");
			// g = fig.getGraphics();
		} catch (IOException e) {	
			J.impErr("!falha inicializando buffer de impressao","J.iniBuf()");
		}		
		
	}
	public static void impBufx2(){
		impBuf(0,0, maxXbuf<<1, maxYbuf<<1);		
		
		/*	!!! COMECO DE OCULOS 3D !!!
			// buffer impresso duas vezes, um p cada olho, mas falta um deslocamento fino p dar profundidade.
			// qq dia desses eu testo isso
		impBuf(0,0, maxXbuf, maxYbuf<<1);		
		impBuf(maxXbuf,0, maxXbuf<<1, maxYbuf<<1);		
		*/
		/*	buffer simetrico espelhado, um p cada olho
			// efeito interessante tb
			// dah p pensar em monitores laterais
			// se eu tivesse como ligar 3 monitores, daria um efeito legal, bastaria empregar os proprios recursos de monitor do windows.
		impBuf(0,0, maxXbuf, maxYbuf<<1);		
		impBuf(maxXbuf<<1,0, maxXbuf, maxYbuf<<1);		
		*/
		/*	ateh daria p fazer um esquema de expansao de visao lateral num monitor soh, mas outra hora eu vejo isso
		*/
		
	}
	public static void impBuf(){
		impBuf(0,0, maxXbuf, maxYbuf);
	}
	public static void impBufRel(int i, int j, int ii, int jj){
		impBuf(i,j,i+ii,j+jj);
	}
	public static void impBuf(int i, int j, int ii, int jj){
		// coordenadas absolutas, nao relativas
		if(bufImp==null){
			J.esc("AVISO: buffer de impressao nao inicializado");
			// J.impErr("!buffer de impressao nao inicializado");
			// demora um pouco ateh abrir o arq base
			return;
		}
		if(opInvHorBuf) // efeitos legais com isso
			J.g.drawImage(bufImp, ii,j,i,jj, 0,0, maxXbuf,maxYbuf, null);
		else
			J.g.drawImage(bufImp, i,j,ii,jj, 0,0, maxXbuf,maxYbuf, null);
	}
		static boolean opInvHorBuf=false;
	public static void bufRet(Color cr, Color crr, int i, int j, int ii, int jj){
		if(bufImp==null){
			// J.impErr("!buffer de impressao ainda nao inicializado");
			return;
		}
		if(cr!=null){
			grafBufImp.setColor(cr);
			grafBufImp.fillRect(i,j, ii-i,jj-j);			
		}
		if(crr!=null){
			grafBufImp.setColor(crr);
			grafBufImp.drawRect(i,j, ii-i,jj-j);			
		}		
	}			
	public static void bufRet(int cr, int crr, int i, int j, int ii, int jj){
		if(bufImp==null){
			// J.impErr("!buffer de impressao ainda nao inicializado");
			return;
		}
		if(cr!=0){
			grafBufImp.setColor(J.cor[cr]);
			grafBufImp.fillRect(i,j, ii-i,jj-j);			
		}
		if(crr!=0){
			grafBufImp.setColor(J.cor[crr]);
			grafBufImp.drawRect(i,j, ii-i,jj-j);			
		}		
	}			
	public static void bufRetRel(Color cr, Color crr, int i, int j, int ii, int jj){
		if(bufImp==null){
			// J.impErr("!buffer de impressao ainda nao inicializado");
			return;
		}
		if(cr!=null){
			grafBufImp.setColor(cr);
			grafBufImp.fillRect(i,j, ii,jj);			
		}
		if(crr!=null){
			grafBufImp.setColor(crr);
			grafBufImp.drawRect(i,j, ii,jj);			
		}		
	}		
	public static void bufLin(Color cr, int i, int j, int ii, int jj){
		if(bufImp==null){
			// J.impErr("!buffer de impressao ainda nao inicializado");
			return;
		}		
		grafBufImp.setColor(cr);
		grafBufImp.drawLine(i,j,ii,jj);					
	}		
	public static void bufCirc(Color cr, Color crr, int r, int x, int y){
		if(bufImp==null){
			// J.impErr("!buffer de impressao ainda nao inicializado");
			return;
		}		
		if(cr!=null){
			grafBufImp.setColor(cr);
			grafBufImp.fillArc(x-r,y-r, r+r,r+r, 1,360);	
			// 			  x, y, width, height, angIni, angFin
		}
		if(crr!=null)
		if(crr!=cr){
			grafBufImp.setColor(crr);
			grafBufImp.drawArc(x-r,y-r, r+r,r+r, 1,360);	
		}
	}	
		static boolean opTriParaHit=false; // o triangulo nao serah impresso, ele funcionarah apenas como caixa de colisao pro mouse.
	public static boolean bufTri(Color cr, Color crr, int i, int j, int ii, int jj, int iii, int jjj){
		if(bufImp==null){
			J.impErr("!buffer de impressao ainda nao inicializado");
			return false;
		}		
		
		int m[]= {i,ii,iii};
		int n[]= {j,jj,jjj};
		
		if(!opTriParaHit){ // isso deve ser implementado nos outros metodos
			if(cr!=null){
				grafBufImp.setColor(cr);
				grafBufImp.fillPolygon(m,n,m.length);		
			}	
			
			if(crr!=null){
				grafBufImp.setColor(crr);
				grafBufImp.drawPolygon(m,n,m.length);		
			}	
		}		
		opTriParaHit=false; // por seguranca

		
		// habilitar isso somente quando necessario		
		if(opVerHit){
			int menorX = J.menor(i,ii,iii);
			int menorY = J.menor(j,jj,jjj);
			int maiorX = J.maior(i,ii,iii);
			int maiorY = J.maior(j,jj,jjj);		
			
			menorX-=ajHit;
			menorY-=ajHit;
			maiorX+=ajHit;
			maiorY+=ajHit;
			
			if(opDebug) // caixa de colisao
				J.bufRet(0,14, menorX,menorY,maiorX,maiorY);
			
			
			if(noInt(xPonto, menorX,maiorX)) // 2D
			if(noInt(yPonto, menorY,maiorY))
				return true;
		}
		return false;	
	}		
		static boolean opVerHit = true; // expandir isso p outros metodos parecidos. Seria interessante deixar a verificacao de hit somente quando o mouse estivesse mais ou menos no centro da tela. Nao precisa verificar isso p todo q qq poligono imprimido, limitar apenas ao centro da tela com um certo intervalo jah ajuda p ganhar clock.
		static int ajHit=3; // ajuste para o hit
		static int numPolImp=0; // deve ser expandido p outros metodos (mas existem metodos obsoletos q nao precisam ser mechidos)
	public static boolean bufPol(Color cr, Color crr, int i, int j, int ii, int jj, int iii, int jjj, int iiii, int jjjj){
		if(bufImp==null){
			// J.impErr("!buffer de impressao ainda nao inicializado");
			return false;
		}		

		
		// poligono de costas, nao imprimir. A impressao precisa ser sempre com x crescente, do contrario, deduzir-se-ah q o poligono estah de costas
		// if(ii<i) return; 
		
		int m[]= {i,ii,iii,iiii};
		int n[]= {j,jj,jjj,jjjj};
		
		if(cr!=null){
			grafBufImp.setColor(cr);
			grafBufImp.fillPolygon(m,n,m.length);		
		}	
		if(crr!=null){
			grafBufImp.setColor(crr);
			grafBufImp.drawPolygon(m,n,m.length);		
		}	

		numPolImp++;

		if(opVerHit){
			int x = J.menor(i,ii,iii,iiii);
			int xx = J.maior(i,ii,iii,iiii);
			int y = J.menor(j,jj,jjj,jjjj);
			int yy = J.maior(j,jj,jjj,jjjj);
			
			x-=ajHit;
			y-=ajHit;
			xx+=ajHit;
			yy+=ajHit;

			if(opDebug) // caixa de colisao
				J.bufRet(0,11, x,y,xx,yy);
						
			if(noInt(xPonto, x,xx)) // 2D
			if(noInt(yPonto, y,yy))		
				return true;
		}
		return false;
	}		
	public static void bufText(int x, int y, Color cr, String st){
		if(st==null) return;
		if(bufImp==null){
			// J.impErr("!buffer de impressao ainda nao inicializado");
			return;
		}				

		grafBufImp.setColor(cr);
		grafBufImp.drawString(st,x,y);		
	}
	public static void bufClear(){
		bufClear(J.cor[0]);
	}
	public static void bufClear(Color cr){
		bufRetRel(cr,null, 0,0,maxXbuf, maxYbuf);
	}

// === CHAO e CEU ============================
// usei na nave 3d
// cansei de refazer toda hora. Tah pronto aqui.

		static int altHor = 0, crTeto = 59,	crChao = 69;
	public static void regChaoTeto(){
		J.Coord2 c = J.fxy(0,0,10000);
		altHor = c.y;		
		int w=40, e=0, max=12;
		J.Coord2 cc = null;		
		int v=1; // lentidao ajustada aqui
		// int altChao=0, altTeto=40+(int)(J.cont/10f)%10;		
		int altChao=0, altTeto=12;
		
		
		// crTeto=0;
		if(crChao!=0 || crTeto!=0)
		for(int q=0; q<=max; q++){
			e = (int)(J.cont/v)%w;
			if(crChao!=0){
				c = J.fxy(0,altChao,q*20-e);
				cc = J.fxy(0,altChao,q*20+w-e);
				J.impRet(crChao-q%2,0, 0,c.y, J.maxXf,cc.y);						
			}	
			if(crTeto!=0){
				c = J.fxy(0,altTeto,q*20-e);
				cc = J.fxy(0,altTeto,q*20+w-e);			
				J.impRet(crTeto-q%2,0, 0,c.y, J.maxXf,cc.y);						
			}	
		}
		if(crTeto!=0) J.impRet(crTeto-1,0, 0,altHor-10, J.maxXf,altHor-20);
		if(crChao!=0) J.impRet(crChao-1,0, 0,altHor+10, J.maxXf,altHor+20);

	}	

// === EFEITO OCULOS ==========================
/* 
 ficou bom
 se cr16, escurece gradualmente at? preto m?ximo, depois vai clareando de novo
 no ponto cOculos=255 a cor est? mais opaca, ? exatamente o meio do processo (transicao de telas aqui)
INSTRU??ES:
-1- coloque um "J.regOculos();" dentro do loop principal
-2- acione por "J.iniOculos(qqCrLegal);
-3- monitore o processo vendo o valor de "cOculos"
OBS1: lembre-se q este m?todo introduz uma camada de transparencia, logo, sobrepor? qq uma q venha antes deste metodo. Altere a ordem de "J.regOculos()" p ajustar isso.
OBS2: existe consumo significativo de clock enquanto "cOculos>0". Cuidado.
OBS3: d? p alterar o efeito monitorando e alterando o valor de cOculos.
 */

		static int cOculos=0, maxOculos=255<<1, velOculos=3; 
		static Color crOculos=null;
	public static void regOculos(){
		if(cOculos!=0){
			int q = J.pulsar(cOculos, 255);
			J.impRetRel(J.altAlfa(crOculos, q),null, 0,0, J.maxXf, J.maxYf);
			cOculos-=velOculos;
			if(cOculos<0)cOculos=0;
		}
	} 
	public static void iniOculos(Color cr, char op){
		crOculos=cr;
		op = emMinusc(op);
		// considere J.cor[16] p explica??o abaixo:
		
		// 'p': pulsar; escurece gradual, chega ao ponto m?ximo e vai clareando
		// 'c': clarear; come?a escuro no m?ximo e vai clareando aos poucos
		// 'e': escurecer... a? tem q interromper o processo no fonte-destino quando cOculos==maxOculos>>1;
		switch(op){
			case 'p': cOculos=maxOculos; break;
			case 'c': cOculos=maxOculos>>1; break;
			//case 'e': escurecer... veja obs acima
		}		
	}
	public static void iniOculos(int cr, char op){
		iniOculos(J.cor[cr],op); // ?limites???
	}

// === CAPTURANDO A TELA INTEIRA =================
 public static BufferedImage screenCapture() {
	 // JOGUE P UM JFig(retornoDesteMetodo) q ajuda!
	 // daria até p criar slides p vids de tutorial do krita
    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    //Dimension dimension = new Dimension(400,400); 
		// até daria p diminuir a area capturada, mas sempre iniciará em 0:0
		// daí, é melhor jogar p um JFig a alterar por ele
    Rectangle rectangle = new Rectangle(dimension);
		try{
			if(robot==null) robot = new Robot();
		} catch(Exception e){
			e.printStackTrace();
			J.impErr("!erro criando robot para captura de tela","J.screenCapture()");
		}
    BufferedImage screen = robot.createScreenCapture(rectangle);
    return screen;
 }
 
}