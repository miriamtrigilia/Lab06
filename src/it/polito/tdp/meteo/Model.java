package it.polito.tdp.meteo;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.Rilevamento;
import it.polito.tdp.meteo.bean.SimpleCity;
import it.polito.tdp.meteo.db.MeteoDAO;

public class Model {

	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	
	
	private MeteoDAO dao;
	private List<Citta> citta= new ArrayList<Citta>();
	private List<Citta> best;
	private List<SimpleCity> cittaCosti = new ArrayList<SimpleCity>();
	int mese=0;
	
	public Model() {
		dao = new MeteoDAO();
		citta = dao.getCitta();
	}

	public String getUmiditaMedia(int mese) {
		String risultato = "";
		
		for(Citta c: this.citta) 
			risultato += c.getNome()+" "+this.dao.getAvgRilevamentiLocalitaMese(mese, c.getNome())+"\n";
		
		return risultato;
	}

	private Double punteggioSoluzione(List<SimpleCity> soluzioneCandidata) {

		double score = 0.0;
		return score;
	}

	private boolean controllaParziale(List<SimpleCity> parziale) {

		return true;
	}
	
	public List<Citta> calcolaSequenza(int mese) {
		List<Citta> parziale = new ArrayList<>() ;
		this.best = null ;
		
		// carica dentro ciascuna delle leCitta la lista dei rilevamenti (localita, data, umidita) nel mese considerato 
		for(Citta c : citta) {
			c.setRilevamenti(dao.getAllRilevamentiLocalitaMese(mese, c.getNome()));
		}
		// imposto il mese
		this.mese = mese;
		ricorri(parziale, 0) ;
		return best ;
	} 
	
	private void ricorri(List<Citta> parziale, int livello ) {
		// TERMINA
		if(livello == this.NUMERO_GIORNI_TOTALI) {
			//calcola costo
			Double costo = calcolaCosto(parziale);
			if(best == null || costo < calcolaCosto(best)) // controlla se è il minimo ed eventualmente aggiorna
				best = new ArrayList<>(parziale);
		}
		else {
			// caso intermedio
			for(Citta prova : this.citta) {
				if(aggiuntaValida(prova, parziale)) {
					parziale.add(prova);
					ricorri(parziale,livello+1);
					parziale.remove(parziale.size()-1);
				}
			}
		}
	}

	private Double calcolaCosto(List<Citta> parziale) {
		double costo = 0;
		
		for(int giorno = 1; giorno <= this.NUMERO_GIORNI_TOTALI; giorno++) {
			// dove mi trovo
			Citta c = parziale.get(giorno-1);
			// calcolo umidita in quel giorno
			double umidita = c.getRilevamenti().get(giorno-1).getUmidita();
			costo += umidita;
		}
		
		// controllo se ho cambiato citta
		for(int giorno = 2; giorno <= this.NUMERO_GIORNI_TOTALI; giorno++) {
			if(!parziale.get(giorno-1).equals(parziale.get(giorno-2)))
				costo += this.COST;
		}
		
		return costo;
	}

	private boolean aggiuntaValida(Citta prova, List<Citta> parziale) {
		// VERIFICA GIORNI MAX
		int cont = 0;
		for(Citta precedente : parziale) {
			if(prova.equals(precedente))
				cont++;
		}
		
		if(cont>=this.NUMERO_GIORNI_CITTA_MAX)
			return false;
		
		// VERIFICA GIORNI MIN
		
		// PRIMO GIORNO
		if(parziale.size()==0) 
			return true;
		
		// SECONDO E TERZO GIORNO
		if(parziale.size()==1 || parziale.size()==2)
			return parziale.get(parziale.size()-1).equals(prova);  // non posso cambiare, quindi aggiungo solo se è uguale a quello prima
		
		// ALTRI GIORNI
		if(parziale.get(parziale.size()-1).equals(prova)) 		
			return true; // posso sempre rimanere (tanto c'è la verifica di giorni max)
		
		// CAMBIO CITTA
		if(parziale.get(parziale.size()-1).equals(parziale.get(parziale.size()-2)) && parziale.get(parziale.size()-2).equals(parziale.get(parziale.size()-3)))
			return true;
		
		return false;
		
	}
 

}
