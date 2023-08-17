// texto com char-pixels, versao 2.0
// nao usa jf1, pixels por codigo in-line
// não tá elegante nem otimizada, precisa ser reescrita, mas funciona
/* COMO USAR:
Escreva fora e acima do loop principal:
		JText txt = new JText();
Digite as instrucoes de acordo com sua necessidade:
		txt.imp(J.cor[9], 100,200, "texto de exemplo"); // x:y = 100:200 
		txt.imp("isto aparece na linha logo abaixo"); // impressao com a cor azul, definida acima
		txt.setSize(larguraDoChar,altura); // nao altere larChar altChar diretamente, faça por este metodo.
		txt.crSomb = null; // sem sombra
Mais um exemplo:
		txt.imp(119-J.pulsar(J.cont>>5,4), 100,40,"Caracteres com cores pulsantes");
		txt.imp("abcdefghijklmnopqrstuvwxyz _ __________");
		txt.imp("0123456789");
		txt.imp("!?@#$%&*_-+='\"()[]{}<>~^/\\|:;.,´`¨"); // demonstracao de caracteres
Pode-se criar duas instancias de JText, cada uma com suas cores e tamanhos		
Repare que imp("texto") parece com esc("blabla"), jah q a impressao continua na linha debaixo.


*/
import java.awt.Color;

public class JText{
		int lin=0, col=0, 
			altChar=20, larChar=altChar>>1, esp=larChar+1, 
			altLin=altChar-2;
		Color crEsc = J.cor[15], crSomb=J.cor[8];		
		

		
	public void 
	ze(int l, int a){
		altChar = a;
		larChar = l;
		esp = larChar+1;
		altLin=altChar-5;		
	}	
	public void imp(int i, int j, String st){
		col = i;
		lin = j;
		for(int q=0; q<st.length(); q++)
			impChar(crEsc,null, col+q*esp,lin, st.charAt(q));
		lin+=altLin;		
	}		
	public void imp(int cr, int i, int j, String st){
		imp(J.cor[cr],i,j,st);
	}
	public void imp(Color cr, int i, int j, String st){
		crEsc = cr;
		col = i;
		lin = j;
		for(int q=0; q<st.length(); q++)
			impChar(crEsc, null, col+q*esp,lin, st.charAt(q));
		lin+=altLin;		
	}
	public void imp(String st){
		for(int q=0; q<st.length(); q++)
			impChar(crEsc, null, col+q*esp,lin, st.charAt(q));
		lin+=altLin;
	}

		String st1="", st2="", st3="", st4="", st5="", st6="", st7="", st8="";
	public void defChar(String s1, String s2, String s3, String s4, String s5, String s6, String s7, String s8){
		st1 = s1;
		st2 = s2;
		st3 = s3;
		st4 = s4;
		st5 = s5;
		st6 = s6;
		st7 = s7;
		st8 = s8;
	}
	public void impCharRel(Color cr, int i, int j, int ii, int jj, String s1, String s2, String s3, String s4, String s5, String s6, String s7, String s8){
		int tmx = (int)((ii) / 5);
		int tmy = (int)((jj) / 8);		
		crEsc = cr;
		for(int q=0; q<s1.length(); q++){
			// nao pode usar a mesma cor p definir a sombra e o degradê!
			if(cr!=crSomb) if(cr1!=null) cr = cr1; if(s1.charAt(q)!=' ') J.impRetRel(cr,null, i, j      , tmx, tmy);
			if(cr!=crSomb) if(cr1!=null) cr = cr2; if(s2.charAt(q)!=' ') J.impRetRel(cr,null, i, j+tmy  , tmx, tmy);
			if(cr!=crSomb) if(cr1!=null) cr = cr3; if(s3.charAt(q)!=' ') J.impRetRel(cr,null, i, j+tmy*2, tmx, tmy);
			if(cr!=crSomb) if(cr1!=null) cr = cr4; if(s4.charAt(q)!=' ') J.impRetRel(cr,null, i, j+tmy*3, tmx, tmy);
			if(cr!=crSomb) if(cr1!=null) cr = cr5; if(s5.charAt(q)!=' ') J.impRetRel(cr,null, i, j+tmy*4, tmx, tmy);
			if(cr!=crSomb) if(cr1!=null) cr = cr6; if(s6.charAt(q)!=' ') J.impRetRel(cr,null, i, j+tmy*5, tmx, tmy);
			if(cr!=crSomb) if(cr1!=null) cr = cr7; if(s7.charAt(q)!=' ') J.impRetRel(cr,null, i, j+tmy*6, tmx, tmy);
			if(cr!=crSomb) if(cr1!=null) cr = cr8; if(s8.charAt(q)!=' ') J.impRetRel(cr,null, i, j+tmy*7, tmx, tmy);
			if(cr1==null) cr = J.altColor(cr,-20); // leve efeito 11111111111111111
			i+=tmx;
		}						
	}
	public void impChar(int cr, int crf, int i, int j, char ca){
		// ?limites p cor???
		impChar(J.cor[cr],J.cor[crf],i,j,ca);
	}
	public boolean ehMinuscula(char ca){
		if(J.tem(ca,"abcdefghijklmnopqrstuvwxyz")) return true;
		if(J.tem(ca,"áàãâéèêíóôúç")) return true; // ?mais???
		return false;
	}
	public void impChar(Color cr, Color crf, int i, int j, char ca){
		if(crf!=null) J.impRetRel(crf,null, i,j,larChar,altChar);
		switch(J.emMaiusc(ca)){// rrrrrrrrrrrrrr
			case 0: break;
			case 64000: {defChar( // pensar melhor nisso
				"XXXXX",
				"XXXXX",
				"XXXXX",
				"XXXXX",
				"XXXXX",
				"XXXXX",
				"XXXXX",
				"XXXXX");				
			} break;
			case 64001: {defChar( 
				"XXXXX",
				"X   X",
				"X X X",
				"XXX X",
				"XX XX",
				"XXXXX",
				"XX XX",
				"XXXXX");				
			} break;			
			case 64002: {defChar( 
				"     ",
				" X X ",
				"XXXXX",
				"XXXXX",
				"XXXXX",
				" XXX ",
				"  X  ",
				"     ");
				} break;									
			case 64003: {defChar( 
				"  X  ",
				" X X ",
				"  X  ",
				" XXX ",
				"X X X",
				"  X  ",
				" X X ",
				"XX XX");
				} break;									
			case 64004: {defChar( 
				"  X  ",
				" X X ",
				"X X X",
				" XXX ",
				"X X X",
				"XXXXX",
				"     ",
				"     ");
				} break;									
			case 64005: {defChar( 
				"  X  ",
				" X X ",
				"  X  ",
				"  X  ",
				"  X  ",
				"  X  ",
				"  X  ",
				"  XX ");
				} break;													
			case 64006: {defChar( 
				"  X  ",
				" X X ",
				"  X  ",
				" XXX ",
				"X X X",
				"  X  ",
				" X X ",
				" XXXX");
				} break;					
			case 64007: {defChar( 
				"  X  ",
				" X X ",
				"  X  ",
				" XXXX",
				"X X  ",
				" XX X",
				"X  XX",
				"XX   ");
				} break;									
			case ' ': {defChar(
				"     ",
				"     ",
				"     ",
				"     ",
				"     ",
				"     ",
				"     ",
				"     ");
			} break;

			case '!': {defChar(
				"     ",
				" X   ",
				" X   ",
				" X   ",
				" X   ",
				" X   ",
				"     ",
				" X   ");
			} break;			
			case '?': {defChar(
				"     ",
				" XX  ",
				"X  X ",
				"   X ",
				"  X  ",
				" X   ",
				"     ",
				" X   ");
			} break;		
			case '@': {defChar(
				"     ",
				" XXX ",
				"X   X",
				"X XXX",
				"X X X",
				"X XXX",
				" XXX ",
				"     ");
			} break;		
			case '#': {defChar(
				"     ",
				" X X ",
				"XXXXX",
				" X X ",
				" X X ",
				"XXXXX",
				" X X ",
				"     ");
			} break;
			case '$': {defChar(
				"  X  ",
				" XXX ",
				"X X X",
				" XX  ",
				"  XX ",
				"X X X",
				" XXX ",
				"  X  ");
			} break;		
			case '%': {defChar(
				"     ",
				"XX X ",
				"XX X ",
				"  X  ",
				" X   ",
				"X  XX",
				"X  XX",
				"     ");
			} break;		
			case '&': {defChar(
				"     ",
				" X   ",
				"X X  ",
				" X  X",
				"X XX ",
				"X  X ",
				" XX X",
				"     ");
			} break;		
			case '*': {defChar(
				"     ",
				"  X  ",
				"X X X",
				" XXX ",
				" XXX ",
				"X X X",
				"  X  ",
				"     ");
			} break;		
			case '_': {defChar(
				"     ",
				"     ",
				"     ",
				"     ",
				"     ",
				"     ",
				"     ",
				"XXXXX");
			} break;					
			case '-': {defChar(
				"     ",
				"     ",
				"     ",
				"     ",
				"XXXX ",
				"     ",
				"     ",
				"     ");
			} break;			
			case '+': {defChar(
				"     ",
				"     ",
				"  X  ",
				"  X  ",
				"XXXXX",
				"  X  ",
				"  X  ",
				"     ");
			} break;					
			case '=': {defChar(
				"     ",
				"     ",
				"     ",
				"XXXX ",
				"     ",
				"XXXX ",
				"     ",
				"     ");
			} break;					
			case '\'': {defChar(
				"     ",
				" XX  ",
				" XX  ",
				" X   ",
				"     ",
				"     ",
				"     ",
				"     ");
			} break;					
			case '"': {defChar(
				"     ",
				" X X ",
				" X X ",
				" X X ",
				"     ",
				"     ",
				"     ",
				"     ");
			} break;								
			case '(': {defChar(
				"  X  ",
				" X   ",
				" X   ",
				" X   ",
				" X   ",
				" X   ",
				" X   ",
				"  X  ");
			} break;								
			case ')': {defChar(
				"  X  ",
				"   X ",
				"   X ",
				"   X ",
				"   X ",
				"   X ",
				"   X ",
				"  X  ");
			} break;					
			case '{': {defChar(
				"  XX ",
				" X   ",
				" X   ",
				"XX   ",
				" X   ",
				" X   ",
				" X   ",
				"  XX ");
			} break;								
			case '}': {defChar(
				" XX  ",
				"   X ",
				"   X ",
				"   XX",
				"   X ",
				"   X ",
				"   X ",
				" XX  ");
				} break;					
			case '[': {defChar(
				" XXX ",
				" X   ",
				" X   ",
				" X   ",
				" X   ",
				" X   ",
				" X   ",
				" XXX ");
			} break;								
			case ']': {defChar(
				" XXX ",
				"   X ",
				"   X ",
				"   X ",
				"   X ",
				"   X ",
				"   X ",
				" XXX ");
				} break;					
			case '<': {defChar(
				"     ",
				"     ",
				"  X  ",
				" X   ",
				"X    ",
				" X   ",
				"  X  ",
				"     ");
				} break;					
			case '>': {defChar(
				"     ",
				"     ",
				" X   ",
				"  X  ",
				"   X ",
				"  X  ",
				" X   ",
				"     ");
				} break;					
			case '^': {defChar(
				"     ",
				"     ",
				"  X  ",
				" X X ",
				"X   X",
				"     ",
				"     ",
				"     ");
				} break;
			case '~': {defChar(
				"     ",
				"     ",
				"     ",
				" X X ",
				"X X  ",
				"     ",
				"     ",
				"     ");
				} break;					
			case '/': {defChar(
				"   X ",
				"   X ",
				"  X  ",
				"  X  ",
				" X   ",
				" X   ",
				"X    ",
				"X    ");
				} break;				
			case '\\': {defChar(
				"X    ",
				"X    ",
				" X   ",
				" X   ",
				"  X  ",
				"  X  ",
				"   X ",
				"   X ");
				} break;								
			case '|': {defChar(
				"  X  ",
				"  X  ",
				"  X  ",
				"  X  ",
				"  X  ",
				"  X  ",
				"  X  ",
				"  X  ");
				} break;												
			case ':': {defChar(
				"     ",
				"     ",
				" XX  ",
				" XX  ",
				"     ",
				" XX  ",
				" XX  ",
				"     ");
				} break;												
			case ';': {defChar(
				"     ",
				"     ",
				" XX  ",
				" XX  ",
				"     ",
				" XX  ",
				" XX  ",
				"  X  ");
				} break;					
			case '.': {defChar(
				"     ",
				"     ",
				"     ",
				"     ",
				"     ",
				" XX  ",
				" XX  ",
				"     ");
			} break;
			case ',': {defChar(
				"     ",
				"     ",
				"     ",
				"     ",
				"     ",
				" XX  ",
				" XX  ",
				"  X  ");
				} break;									
			case '`': {defChar( // "´" nao tah pegando, nem trema
				"       ",
				" XX XX ",
				"XXXXXXX",
				"XXXXXXX",
				"XXXXXXX",
				" XXXXX ",
				"  XXX  ",
				"   X   ");
				} break;
			
			case '0': {defChar(
				"     ",
				" XX  ",
				"X  X ",
				"XX X ",
				"X XX ",
				"X  X ",
				" XX  ",
				"     ");
			} break;		
			case '1': {defChar(
				"     ",
				" X   ",
				"XX   ",
				" X   ",
				" X   ",
				" X   ",
				"XXX  ",
				"     ");
			} break;			
			case '2': {defChar(
				"     ",
				" XX  ",
				"X  X ",
				"   X ",
				"  X  ",
				" X   ",
				"XXXX ",
				"     ");
			} break;		
			case '3': {defChar(
				"     ",
				"XXXX ",
				"   X ",
				"  X  ",
				"   X ",
				"X  X ",
				" XX  ",
				"     ");
			} break;		
			case '4': {defChar(
				"     ",
				"   X ",
				"  XX ",
				" X X ",
				"X  X ",
				"XXXXX",
				"   X ",
				"     ");
			} break;		
			case '5': {defChar(
				"     ",
				"XXXX ",
				"X    ",
				"XXX  ",
				"   X ",
				"X  X ",
				" XX  ",
				"     ");
			} break;		
			case '6': {defChar(
				"     ",
				" XXX ",
				"X    ",
				"XXX  ",
				"X  X ",
				"X  X ",
				" XX  ",
				"     ");
			} break;		
			case '7': {defChar(
				"     ",
				"XXXX ",
				"   X ",
				"   X ",
				"  X  ",
				"  X  ",
				"  X  ",
				"     ");
			} break;		
			case '8': {defChar(
				"     ",
				" XX  ",
				"X  X ",
				" XX  ",
				"X  X ",
				"X  X ",
				" XX  ",
				"     ");
			} break;		
			case '9': {defChar(
				"     ",
				" XX  ",
				"X  X ",
				" XXX ",
				"   X ",
				"X  X ",
				" XX  ",
				"     ");

			} break;		

			case 'A': {defChar(
				"     ",
				" XX  ",
				"X  X ",
				"X  X ",
				"XXXX ",
				"X  X ",
				"X  X ",
				"     ");
			} break;
			case 'B': {defChar(
				"     ",
				"XXX  ",
				"X  X ",
				"XXX  ",
				"X  X ",
				"X  X ",
				"XXX  ",
				"     ");
			} break;			
			case 'C': {defChar(
				"     ",
				" XX  ",
				"X  X ",
				"X    ",
				"X    ",
				"X  X ",
				" XX  ",
				"     ");
			} break;		
			case 231: {defChar( // 231, "Ç"
				"     ",
				" XX  ",
				"X  X ",
				"X    ",
				"X    ",
				"X XX ",
				" XX  ",
				"  X  ");
			} break;					

			case 'D': {defChar(
				"     ",
				"XXX  ",
				"X  X ",
				"X  X ",
				"X  X ",
				"X  X ",
				"XXX  ",
				"     ");
			} break;			
			case 'E': {defChar(
				"     ",
				"XXXX ",
				"X    ",
				"XXX  ",
				"X    ",
				"X    ",
				"XXXX ",
				"     ");
			} break;			
					case 'É': {defChar(
						"  XXX",
						"XXXX ",
						"X    ",
						"XXX  ",
						"X    ",
						"X    ",
						"XXXX ",
						"     ");
					} break;					
			case 'F': {defChar(
				"     ",
				"XXXX ",
				"X    ",
				"XXX  ",
				"X    ",
				"X    ",
				"X    ",
				"     ");
			} break;			
			case 'G': {defChar(
				"     ",
				" XX  ",
				"X  X ",
				"X    ",
				"X XX ",
				"X  X ",
				" XX  ",
				"     ");
			} break;		
			case 'H': {defChar(
				"     ",
				"X  X ",
				"X  X ",
				"XXXX ",
				"X  X ",
				"X  X ",
				"X  X ",
				"     ");
			} break;			
			case 'I': {defChar(
				"     ",
				"XXX  ",
				" X   ",
				" X   ",
				" X   ",
				" X   ",
				"XXX  ",
				"     ");
			} break;			
			case 'J': {defChar(
				"     ",
				"XXXXX",
				"   X ",
				"   X ",
				"X  X ",
				"X  X ",
				" XX  ",
				"     ");
			} break;		
			case 'K': {defChar(
				"     ",
				"X  X ",
				"X X  ",
				"XX   ",
				"XX   ",
				"X X  ",
				"X  X ",
				"     ");
			} break;			
			case 'L': {defChar(
				"     ",
				"X    ",
				"X    ",
				"X    ",
				"X    ",
				"X    ",
				"XXXX ",
				"     ");
			} break;			
			case 'M': {defChar(
				"     ",
				"X   X",
				"XX XX",
				"X X X",
				"X   X",
				"X   X",
				"X   X",
				"     ");
			} break;					
			case 'N': {defChar(
				"     ",
				"X  X ",
				"XX X ",
				"XX X ",
				"X XX ",
				"X XX ",
				"X  X ",
				"     ");
			} break;					
			case 'O': {defChar(
				"     ",
				" XX  ",
				"X  X ",
				"X  X ",
				"X  X ",
				"X  X ",
				" XX  ",
				"     ");
			} break;		
			case 'P': {defChar(
				"     ",
				"XXX  ",
				"X  X ",
				"X  X ",
				"XXX  ",
				"X    ",
				"X    ",
				"     ");
			} break;		
			case 'Q': {defChar(
				"     ",
				" XX  ",
				"X  X ",
				"X  X ",
				"X  X ",
				"X XX ",
				" XXX ",
				"     ");
			} break;		
			case 'R': {defChar(
				"     ",
				"XXX  ",
				"X  X ",
				"X  X ",
				"XXX  ",
				"X X  ",
				"X  X ",
				"     ");
			} break;		
			case 'S': {defChar(
				"     ",
				" XX  ",
				"X  X ",
				" X   ",
				"  X  ",
				"X  X ",
				" XX  ",
				"     ");
			} break;		
			case 'T': {defChar(
				"     ",
				"XXXXX",
				"  X  ",
				"  X  ",
				"  X  ",
				"  X  ",
				"  X  ",
				"     ");
			} break;			
			case 'U': {defChar(
				"     ",
				"X  X ",
				"X  X ",
				"X  X ",
				"X  X ",
				"X  X ",
				" XX  ",
				"     ");
			} break;		
			case 'V': {defChar(
				"     ",
				"X  X ",
				"X  X ",
				"X  X ",
				"X  X ",
				" XX  ",
				" XX  ",
				"     ");
			} break;		
			case 'W': {defChar(
				"     ",
				"X   X",
				"X   X",
				"X   X",
				"X X X",
				"XX XX",
				"X   X",
				"     ");
			} break;					
			case 'X': {defChar(
				"     ",
				"X  X ",
				"X  X ",
				" XX  ",
				" XX  ",
				"X  X ",
				"X  X ",
				"     ");
			} break;		
			case 'Y': {defChar(
				"     ",
				"X X  ",
				"X X  ",
				" X   ",
				" X   ",
				" X   ",
				" X   ",
				"     ");
			} break;		
			case 'Z': {defChar(
				"     ",
				"XXXX ",
				"   X ",
				"  X  ",
				" X   ",
				"X    ",
				"XXXX ",
				"     ");
			} break;		

			default: {defChar(
				"XXXXX",
				"X   X",
				"X   X",
				"X   X",
				"X   X",
				"X   X",
				"X   X",
				"XXXXX");
			} break;
		} 


		
		crEsc = cr; //???					

		int tmx = (int)(larChar / 5);
		int tmy = (int)(altChar / 8);
		
		/* DEPOIS, pensei em deixar minusculas um pouquinho mais baixas usando a mesma "matriz de pixels"
		if(ehMinuscula(ca)) {
			tmy=(int)(altChar / 11);
		} else {			
			tmy = 
		}
		*/
		
		if(crSomb!=null) impCh(crSomb,i+tmx, j+tmy, tmx, tmy);
		impCh(crEsc,i, j, tmx, tmy);					
			


	}
		Color 
			cr1=null, cr2=null,
			cr3=null, cr4=null,
			cr5=null, cr6=null,
			cr7=null, cr8=null;

	public void setDegrade(Color crr, Color cr){
	 // nao pode usar a mesma cor p definir a sombra e o degradê!		
		cr1 = J.mixColor(J.passos(0f,1f, 8, 1), cr,crr);
		cr2 = J.mixColor(J.passos(0f,1f, 8, 2), cr,crr);
		cr3 = J.mixColor(J.passos(0f,1f, 8, 3), cr,crr);
		cr4 = J.mixColor(J.passos(0f,1f, 8, 4), cr,crr);
		cr5 = J.mixColor(J.passos(0f,1f, 8, 5), cr,crr);
		cr6 = J.mixColor(J.passos(0f,1f, 8, 6), cr,crr);
		cr7 = J.mixColor(J.passos(0f,1f, 8, 7), cr,crr);
		cr8 = J.mixColor(J.passos(0f,1f, 8, 8), cr,crr);
	}		
	public void impCh(Color cr, int i, int j, int tmx, int tmy){		
		// este metodo eh mais interno
		// ele soh imprime o que foi carregado nas 8 linhas de pixels
		// quiz deixar disponivel p caso precisar definir caracteres personalizados
		// pensar melhor no mecanismo de personalizacao (um sheen ia ser legal, ou um console)
		for(int q=0; q<st1.length(); q++){
			// nao pode usar a mesma cor p definir a sombra e o degradê!
			if(cr!=crSomb) if(cr1!=null) cr = cr1; if(st1.charAt(q)!=' ') J.impRetRel(cr,null, i, j      , tmx, tmy);
			if(cr!=crSomb) if(cr1!=null) cr = cr3; if(st3.charAt(q)!=' ') J.impRetRel(cr,null, i, j+tmy*2, tmx, tmy);
			if(cr!=crSomb) if(cr1!=null) cr = cr2; if(st2.charAt(q)!=' ') J.impRetRel(cr,null, i, j+tmy  , tmx, tmy);
			if(cr!=crSomb) if(cr1!=null) cr = cr4; if(st4.charAt(q)!=' ') J.impRetRel(cr,null, i, j+tmy*3, tmx, tmy);
			if(cr!=crSomb) if(cr1!=null) cr = cr5; if(st5.charAt(q)!=' ') J.impRetRel(cr,null, i, j+tmy*4, tmx, tmy);
			if(cr!=crSomb) if(cr1!=null) cr = cr6; if(st6.charAt(q)!=' ') J.impRetRel(cr,null, i, j+tmy*5, tmx, tmy);
			if(cr!=crSomb) if(cr1!=null) cr = cr7; if(st7.charAt(q)!=' ') J.impRetRel(cr,null, i, j+tmy*6, tmx, tmy);
			if(cr!=crSomb) if(cr1!=null) cr = cr8; if(st8.charAt(q)!=' ') J.impRetRel(cr,null, i, j+tmy*7, tmx, tmy);
			if(cr1==null) cr = J.altColor(cr,-20); // leve efeito 11111111111111111
			i+=tmx;
		}			
	}	
}
/* A FAZER ZZZZZZZZZZZZZZZZZZZZZZZ
			TODA ESTA SS PRECISA SER REESCRITA, cada pixel dos char definido por strings não é algo elegante e eficiente. Melhor seria uma imagem com impressao de apenas parte dela na coord especifica.
			pensar melhor em personalizacao de caracteres
			?seria melhor trocar "imp()" por "esc()"??? Daria tb p redirecionar um metodo p outro.
			daria p sombrear melhor os caracteres em 11111111
			seria bom deixar definido o char de um cubo em 2d
*/