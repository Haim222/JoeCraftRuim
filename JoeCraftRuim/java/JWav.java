// para sons
// ATENÇÃO: TODOS OS SONS FORAM DESABILITADOS!
// Este fonte permite agendar o carregamento de sons e soh carrega-los quando forem acionados com play.
// util em JoeCraft pq evita carregar todos aqueles arq de som sem q sejam usados

// DEDUZIR A DURACAO DE ARQ WAV(p "isPlaying()) COM "getDuration()"


import java.applet.AudioClip;
import java.applet.Applet;

// p pegar a duracao de wavs ( ?"getDuration()" abaixo já funciona bem???)
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioFormat;

public class JWav{
	
	
		// <><><> ATENÇÃO AQUI <><><><><><><><><><><><><><>
		boolean opMudo = true;
		// <><><><><><><><><><><><><><><><><>
		
	
		String cam = null, cam2=null, cam3=null;
		AudioClip wav = null, wav2 = null, wav3 = null;
		boolean 
			opEco = true, // antigo "opAvisarAoTocar" (nao substituí nos outros fontes ainda)
			opCarrOnPlay = true; // CUIDADO COM ESTA VAR !!!
		static	boolean 
			opNaoAvisarNada = false, // nao imprimir caminhos de arquivo tocando, confuso, mas funciona		
			opCarrTudoNormal = false;
		int cLoop=0, cPlay=0; // agendadores, tocar depois do contador chegar a zero
		Long 
			nanoTimeInicio=0L,
			nanoTimeLength=0L; // veja setLengh(); precisa definir esse somente se for usar um "isPlaying()"
	public void setLength(Long p){ 
		// define a duracao do wav 
		// precisa disso se for usar		"isPlaying()" 
		// demanda calibragem fina do tempo. 
		// Ex: ambiente33.wav tem 4.2 no goldWav mas entra aqui como 4.2 * 1bilhao
		nanoTimeLength = p;
		// veja o tempo no goldWave e acione este metodo
		// lembrete:
		//    nanoTime eh dado em nanosegundos, 
		//   ou seja: eh preciso 1 bilhao de 
		//   nanosegundos p fazer um segundo:
	  //   1.000.000.000
	}	
	public boolean isPlaying(){
		if(cam2!=null)
			J.impErr("! isPlaying() para slot triplo ainda nao foi implementado. Use-o apenas p slot singular.","JWav.isPlaying()");
		//if(nanoTimeLength==0) 
		//	J.impErr("!impossivel determinar se o JWav estah tocando. Nao foi determinada a duracao aproximada do mesmo: |"+cam+"|","JWav.isPlaying()");

		//if(nanoTimeLength==0) nanoTimeLength = (Long)(getDuration(cam)*1000000000.0);

		if(nanoTimeLength==0) {
			Double d = getDuration(cam);
			int i = (int)(d*1000000000.0);
			nanoTimeLength = (long)(i);
		}

		
		if(J.nanoTime()-nanoTimeInicio>nanoTimeLength)
			return false;
		return true;		
	}	


	public double getDuration(String cam){ 
		// retorna a duracao do arq wav em segundos. (fracionados)
		// acho q funciona p varios formatos de audio, mas tem q testar p outros q nao sejam wav
		// Veja q double tem ponto flutuante e nao eh como a contagem de nanosegundos. Precisa converter o usar "isPlaying()" automatizado.
		// setLength() quase ficou obsoleta. 
		
		
		if(!J.tem('.',cam)) cam = cam+".wav";
		if(!J.tem('/',cam)) cam = "wav//"+cam;		
		
		if(!J.arqExist(cam)){
			J.impErr("!arquivo perdido: ["+cam+"]", "JWav.getDuration()");
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
			J.impErr("!erro obtendo duracao de audio","J.getDuration()");
			e.printStackTrace();
			return 0d;
		}
	}


	public JWav(AudioClip p){
		wav = p;
	}
	public JWav(String st, Long len){ // obsoleto
		// jah define o tempo do wav p usar com "isPlaying()"
		// nem todo wav precisa ter a duracao definida, mas se for usar "isPlaying()", precisa definir o tempo.
		// veja o length no goldWav
		// 2.5segundos devem ficar assim:
		// 2.5*1bilhao = 1000000000
		// dah p ajustar melhor esse metodo, mas jah funciona
		cam = J.corrCam(st,"wav");
		if(!J.arqExist(cam)) J.impErr("arquivo perdido: ["+cam+"]", "construtor da classe JWav");
		if((!opCarrOnPlay)||(opCarrTudoNormal)) carr();		
		setLength(len);
	}


	public JWav(String st){
		cam = J.corrCam(st,"wav");
		if(!J.arqExist(cam)) J.impErr("arquivo perdido: ["+cam+"]", "construtor da classe JWav");
		if((!opCarrOnPlay)||(opCarrTudoNormal)) carr();		
	}
	public JWav(String st, String stt, String sttt){
		// mecanismo de slot triplo abaixo
		// basta informar os tres caminhos acima p rodar 1 dos 3 randomicamente a cada chamada  "p l a y ()". Ajudou no JoeCraft (dez 2017)
		cam = J.corrCam(st,"wav");
		if(!J.arqExist(cam)) J.impErr("arquivo perdido: ["+cam+"]", "construtor da classe JWav");
		
		cam2 = J.corrCam(stt,"wav");
		if(!J.arqExist(cam2)) J.impErr("arquivo perdido: ["+cam2+"]", "construtor da classe JWav");

		cam3 = J.corrCam(sttt,"wav");
		if(!J.arqExist(cam3)) J.impErr("arquivo perdido: ["+cam3+"]", "construtor da classe JWav");
		
		if((!opCarrOnPlay)||(opCarrTudoNormal)) carr();		
	}
	public boolean playSeAg(){
		// play se agendado
		// Ficou pratico no fonte de jogo de xadres... e em vários outros tb. É muito util;
		

		
		if(cPlay>0) {
			stop();
			play();
			cPlay=0;
			return true;
		}	
		return false;
	}
	public void agPlay(int tempo){
		// ?seria bom jah carrega-lo aqui???
		// parece que sim
		
		
		
		if(wav==null) carr(); // wav2 e 3 embutidos
		
		cPlay=tempo; // agendamento
	}
	public void agLoop(int tempo){
		if(wav==null) carr(); // wav2 e 3 embutidos
		cLoop=tempo; // agendamento
	}
	public boolean reg(){ // esse precisa estar no loop principal do fonte de alguma forma p agendamento funcionar
		// retorna true se tocou o wav agendado
		// funciona normal com o mecanismo de 3
	
		if(cPlay>0) {
			cPlay--;
			if(cPlay==0) {
				play();
				return true;
			}
		}	
		if(cLoop>0) {
			cLoop--;
			if(cLoop==0) {
				loop();
				return true;
			}
		}			
		return false;
	}
	public void unload(){
		// p liberar a memoria
		stop();
		wav = null;
		wav2 = null;
		wav3 = null;
		// se tentar tocar de novo, vai carregar automaticamente.
		// usei no som de pinguins do mine. Eh q ia usar um arq de 400k e achei q ele poderia ser descartado quando saisse do ambiente de gelo. Parece bom.
	}
	public void carr(){
		// dah p acionar manualmente
		if(J.arqExist(cam))
			wav = J.carrSom2(cam);
		else wav = J.carrSom2("bip01");
		
		if(cam2!=null)
		if(J.arqExist(cam2))
			wav2 = J.carrSom2(cam2);
		else wav2 = J.carrSom2("bip01");		
		
		if(cam3!=null)
		if(J.arqExist(cam3))
			wav3 = J.carrSom2(cam3);
		else wav3 = J.carrSom2("bip01");
	}
	public void stop(){
		if(wav!=null)	wav.stop();
		if(wav2!=null)wav2.stop();		
		if(wav3!=null)wav3.stop();		
	}
	public void play(){		
		if(opMudo) return;		
		
		if(cam2==null){
			// caso nao tenha sido usado o mecanismo de slot triplo, entao...
			if(wav==null) carr();
			cPlay=0; // removendo o agendamento caso programado
			if(!opNaoAvisarNada)
			if(opEco)
				J.esc("tocando: "+cam);
			wav.play(); 

			// soh pegar o tempo inicial se for usar "isPlaying()". P usar esta ultima funcao, precisa acionar primeiro "setLength()" p definir o tempo do wav em questao.
			if(nanoTimeLength!=0L)
				nanoTimeInicio=J.nanoTime();

		} else {
			cPlay=0; // removendo o agendamento caso programado
			switch(J.R(3)){
				case 0: 
					if(wav==null) carr();
					if(!opNaoAvisarNada)					
					if(opEco)
						J.esc("tocando: "+cam);
					wav.play(); 				
					break;
				case 1: 
					if(wav2==null) carr();
					if(!opNaoAvisarNada)					
					if(opEco)					
						J.esc("tocando: "+cam2);
					wav2.play(); 				
					break;					
				case 2: 
					if(wav3==null) carr();
					if(!opNaoAvisarNada)					
					if(opEco)					
						J.esc("tocando: "+cam3);
					wav3.play();
					break;										
			}
		}
	}
	public void loop(){ // loop apenas p 1o slot. Se precisar dos outros eu implemento depois.
		if(opMudo) return;

		if(wav==null) carr();
		wav.loop();
	}
}

