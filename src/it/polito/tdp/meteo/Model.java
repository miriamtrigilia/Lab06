package it.polito.tdp.meteo;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.CittaUmidita;
import it.polito.tdp.meteo.bean.SimpleCity;
import it.polito.tdp.meteo.db.MeteoDAO;

public class Model {

	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	
	private List<Citta> leCitta ;
	
	private List<Citta> best ;
	
	

	public List<Citta> getLeCitta() {
		return leCitta;
	}

	public Model() {

		MeteoDAO dao = new MeteoDAO() ;
		this.leCitta = dao.getAllCitta() ;
		
	}

	public Double getUmiditaMedia(Month mese, Citta citta) {
		MeteoDAO dao = new MeteoDAO() ;
		return dao.getUmiditaMedia(mese, citta);
	}

	public List<CittaUmidita> getUmiditaMedia(int mese) {
		return null ;
	}
	
	
	public List<Citta> calcolaSequenza(Month mese) {
		List<Citta> parziale = new ArrayList<>() ;
		this.best = null ;
		
		// carica dentro ciascuna delle leCitta la lista dei rilevamenti nel mese considerato (e solo quello)
		// citta.setRilevamenti(dao.getRilevamentiCittaMese(...))
		
		cerca(parziale, 0) ;
		return best ;
	}
	
	private void cerca(List<Citta> parziale, int livello) {
		
		if( livello == NUMERO_GIORNI_TOTALI ) {
			// caso terminale
			Double costo = calcolaCosto(parziale) ;
			if( best==null || costo < calcolaCosto(best)) {
				best = new ArrayList<>(parziale) ;
			}
			
			System.out.println(parziale);
		} else {
			
			// caso intermedio
			for( Citta prova: leCitta ) {
				
				if( aggiuntaValida(prova, parziale) ) {
					
					parziale.add(prova) ;
					cerca(parziale, livello+1) ;
					parziale.remove(parziale.size()-1) ;
					
				}
			}
			
		}
		
	}
	
	
	
	private Double calcolaCosto(List<Citta> parziale) {
		
		// sommatoria delle umidità in ciascuna città, considerando il rilevamendo del giorno giusto
		// SOMMA parziale.get(giorno-1).getRilevamenti().get(giorno-1)
		
		// a cui sommo 100 * numero di volte in cui cambio città

		
		return null;
	}

	private boolean aggiuntaValida(Citta prova, List<Citta> parziale) {

		// verifica giorni massimi
		int conta = 0 ;
		for(Citta precedente: parziale)
			if(precedente.equals(prova))
				conta++ ;
		if(conta>=NUMERO_GIORNI_CITTA_MAX)
			return false ;
		
		// verifica giorni minimi
		if(parziale.size()==0)  // primo giorno
			return true ;
		if(parziale.size()==1 || parziale.size()==2) { // secondo o terzo giorno: non posso cambiare
			return parziale.get(parziale.size()-1).equals(prova) ;
		}
		if(parziale.get(parziale.size()-1).equals(prova)) // giorni successivi, posso SEMPRE rimanere
			return true ;
		// sto cambiando citta
		if(parziale.get(parziale.size()-1).equals(parziale.get(parziale.size()-2)) &&
				parziale.get(parziale.size()-2).equals(parziale.get(parziale.size()-3)) )
			return true ;
		
		return false;
	}

	public String trovaSequenza(int mese) {

		return "TODO!";
	}

	private Double punteggioSoluzione(List<SimpleCity> soluzioneCandidata) {

		double score = 0.0;
		return score;
	}

	private boolean controllaParziale(List<SimpleCity> parziale) {

		return true;
	}

}
