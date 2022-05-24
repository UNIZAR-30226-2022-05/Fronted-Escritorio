package es.unizar.unoforall.model.partidas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

import es.unizar.unoforall.model.salas.ConfigSala;

public class Partida {
	public final static int TIMEOUT_TURNO = 30*1000;  // 30 segundos
	
	private boolean hayError = false;
	private String error = "";
	
	private List<Carta> mazo = null;
	private List<Carta> cartasJugadas = null;
	
	private Jugada ultimaJugada = null;
	private int turnoUltimaJugada;


	private List<Jugador> jugadores = null;
	private int turno = 0;
	private boolean sentidoHorario = true;
	
	private ConfigSala configuracion = null;
	private boolean terminada = false;	
	
	//Fecha de inicio de la partida (Ya en formato sql porque no la necesita el frontend en este punto). 
	private Long fechaInicio = null; 
	
	//Variables para extraer resultados de efectos
	private boolean modoAcumulandoRobo = false;
	private int roboAcumulado = 0;
	private boolean modoJugarCartaRobada = false;
	private Carta cartaRobada = null;
	private boolean repeticionTurno = false;
	
	private final Object LOCK = new Object();
	private static final int MAX_ROBO_ATTACK = 5;
	
	private UUID salaID = null;	
	
	private class PosiblesTiposJugadas {
		public boolean esEscalera;
		public boolean esIguales;
		public boolean valida;
		 
		public PosiblesTiposJugadas(boolean esEscalera, boolean esIguales, boolean valida) {
			this.esEscalera = esEscalera;
			this.esIguales = esIguales;
			this.valida = valida;
		}
	}
	
	public Partida(String error) {	//Para construir una partida con error = true
		this.setHayError(true);
		this.setError(error);
	}
	
	private Partida() {
		
	}
	
	
		
	public Partida(List<UUID> jugadoresID, ConfigSala configuracion, UUID salaID) {
		this.setHayError(false);
		this.setError("");
		this.turno = 0;
		this.sentidoHorario = true;
		this.configuracion = configuracion;
		this.terminada = false;
		this.salaID = salaID;
		ultimaJugada = null;
		turnoUltimaJugada = 0;
				
				
		//Marcamos fecha de inicio
		fechaInicio = System.currentTimeMillis(); //Fecha actual.
		
		//Mazo
		this.mazo = new LinkedList<>();
		
		for(Carta.Color color : Carta.Color.values()) {
			if (color != Carta.Color.comodin) {
				for(Carta.Tipo tipo : Carta.Tipo.values()) {
					if (tipo.equals(Carta.Tipo.n0)) {
						this.mazo.add(new Carta(tipo,color));
					} else if (compruebaIncluirMazo(tipo)) {	//dos veces
						this.mazo.add(new Carta(tipo,color));
						this.mazo.add(new Carta(tipo,color));
					}
				}
			} else {
				for(int i = 0; i < 4; i++) {
					this.mazo.add(new Carta(Carta.Tipo.cambioColor,Carta.Color.comodin));
					this.mazo.add(new Carta(Carta.Tipo.mas4,Carta.Color.comodin));
				}
			}
		}
		
//		List<Carta> listaCartasBaraja = new ArrayList<>();
//		listaCartasBaraja.add(new Carta(Carta.Tipo.cambioColor, Carta.Color.comodin));
//		listaCartasBaraja.add(new Carta(Carta.Tipo.mas4, Carta.Color.comodin));
//		listaCartasBaraja.add(new Carta(Carta.Tipo.n1, Carta.Color.rojo));
//		listaCartasBaraja.add(new Carta(Carta.Tipo.n2, Carta.Color.rojo));
//		listaCartasBaraja.add(new Carta(Carta.Tipo.n3, Carta.Color.rojo));
//		listaCartasBaraja.add(new Carta(Carta.Tipo.mas2, Carta.Color.rojo));
//		listaCartasBaraja.add(new Carta(Carta.Tipo.n4, Carta.Color.rojo));
//		
//		for (Carta c : listaCartasBaraja) {
//			for(int i = 0; i < 20; i++) {
//				this.mazo.add(c.clone());
//			}
//		}
		
		
		Collections.shuffle(this.mazo); 
		
		
		// Cartas jugadas
		this.cartasJugadas = new LinkedList<>();
		this.cartasJugadas.add(getCartaInicial());
		
		// Jugadores
		this.jugadores = new LinkedList<>();
		for(UUID jID : jugadoresID) {
			this.jugadores.add(new Jugador(jID));
		}
			// Se crean las IA
		int numIAs = configuracion.getMaxParticipantes() - jugadoresID.size();
		for(int i = 0; i < numIAs; i++) {
			this.jugadores.add(new Jugador());
		}
			// Se crean las manos de todos los jugadores
		for(Jugador j : this.jugadores) {
			for (int i = 0; i < 7; i++) {
				robarCartaJugador(j, 1);
			}
		}
	}


	/**************************************************************************/
	// Funciones privadas
	/**************************************************************************/
	
	// Devuelve la primera carta de la partida
	//FUNCIONA
	private Carta getCartaInicial() {
		int indice = 0;
		Carta carta = this.mazo.get(indice);
		while(carta.getTipo()==Carta.Tipo.cambioColor 
				|| carta.getTipo()==Carta.Tipo.mas2 
				|| carta.getTipo()==Carta.Tipo.mas4 
				|| carta.getTipo()==Carta.Tipo.x2 
				|| carta.getTipo()==Carta.Tipo.rayosX 
				|| carta.getTipo()==Carta.Tipo.salta 
				|| carta.getTipo()==Carta.Tipo.intercambio 
				|| carta.getTipo()==Carta.Tipo.reversa) {
			indice++;
			carta = this.mazo.get(indice);
		}
		this.mazo.remove(indice);
		return carta;
	}
	
	//FUNCIONA
	// Comprueba si una carta especial debe incluirse en el mazo o no según las
	// reglas
	private boolean compruebaIncluirMazo(Carta.Tipo tipo) {
		if (tipo == Carta.Tipo.rayosX && !configuracion.getReglas().isCartaRayosX()) {
			return false;
		} else if (tipo == Carta.Tipo.intercambio && !configuracion.getReglas().isCartaIntercambio()) {
			return false;
		} else if (tipo == Carta.Tipo.x2 && !configuracion.getReglas().isCartaX2()){
			return false;
		} else if (!tipo.equals(Carta.Tipo.cambioColor) && !tipo.equals(Carta.Tipo.mas4)){
			return true;
		} else {
			return false;
		}
	}
	
	//FUNCIONA
	private void avanzarTurno() {
		if (this.sentidoHorario) {
			this.turno++;
			if (this.turno >= this.jugadores.size()) {
				this.turno = 0;
			}
		} else {
			this.turno--;
			if (this.turno < 0) {
				this.turno = this.jugadores.size() - 1;
			}
		}
	}
	
	//FUNCIONA
	private Jugador siguienteJugador() {
		if (this.sentidoHorario) {
			if (this.turno == this.jugadores.size() - 1) {
				return this.jugadores.get(0);
			} else {
				return this.jugadores.get(this.turno + 1);
			}
		} else {
			if (this.turno == 0) {
				return this.jugadores.get(this.jugadores.size() - 1);
			} else {
				return this.jugadores.get(this.turno - 1);
			}
		}
	}
	
	private boolean compatibleAcumulador(Carta c) {
		if ( (configuracion.getReglas().isEncadenarRoboCartas() 
				&& (c.esDelTipo(Carta.Tipo.mas4) || c.esDelTipo(Carta.Tipo.mas2))) 
			|| 
			 (configuracion.getReglas().isRedirigirRoboCartas() && c.esDelTipo(Carta.Tipo.reversa)) ) {
			return true;
		} else {
			return false;
		}
	}
	
	private PosiblesTiposJugadas evaluaJugada(Carta c1, Carta c2) {
		PosiblesTiposJugadas pj = null;
		if (Carta.compartenTipo(c1, c2)) {
			pj = new PosiblesTiposJugadas(false,true,true);
		} else if(Carta.sonConsecutivas(c1, c2)) {
			pj = new PosiblesTiposJugadas(true,false,true);
		} else {
			pj = new PosiblesTiposJugadas(false,false,false);
		}
		return pj;
	}
	
	private Carta robarCarta() {
		if (this.mazo.isEmpty()) {
			System.err.println("Intento de robo de mazo vacío.");
			Carta auxiliar = cartasJugadas.get(cartasJugadas.size()-1);
			cartasJugadas.remove(cartasJugadas.size()-1);
			this.mazo.addAll(cartasJugadas);
			cartasJugadas.removeAll(cartasJugadas);
			cartasJugadas.add(auxiliar);
			/*while(this.cartasJugadas.size()!=1) {
				this.mazo.add(this.cartasJugadas.get(0));
				this.cartasJugadas.remove(0);
			}*/
			Collections.shuffle(this.mazo);
		}
		Carta c = this.mazo.get(0);
		c.setDefault();
		
		this.mazo.remove(0);
		if (this.mazo.isEmpty()) {
			Carta auxiliar = cartasJugadas.get(cartasJugadas.size()-1);
			cartasJugadas.remove(cartasJugadas.size()-1);
			this.mazo.addAll(cartasJugadas);
			cartasJugadas.removeAll(cartasJugadas);
			cartasJugadas.add(auxiliar);
			/*while(this.cartasJugadas.size()!=1) {
				this.mazo.add(this.cartasJugadas.get(0));
				this.cartasJugadas.remove(0);
			}*/
			Collections.shuffle(this.mazo);
		}
		return c;
	}
	
	private void robarCartaJugador(Jugador j, int numCartas) {
		for (int i = 0; i < numCartas; i++) {
			if(j.getMano().size()==20) {
				break;
			}
			j.getMano().add(robarCarta());
		}
	}

	private void juegaCarta(Carta c, Jugada jugada) {
		boolean esSalto = false;
		boolean hayIntercambio = false;
		
		switch (c.getTipo()) {
			case intercambio:
				hayIntercambio = true;
				break;
				
			case mas2:
				if(configuracion.getReglas().isEncadenarRoboCartas() || configuracion.getReglas().isRedirigirRoboCartas()) {
					if(!modoAcumulandoRobo) {
						modoAcumulandoRobo = true;
						roboAcumulado = 2;
					} else {
						roboAcumulado+=2;
					}
				} else {
					robarCartaJugador(siguienteJugador(), 2);
					esSalto=true;
				}
				break;
				
			case mas4:
				if(configuracion.getReglas().isEncadenarRoboCartas() || configuracion.getReglas().isRedirigirRoboCartas()) {
					if(!modoAcumulandoRobo) {
						modoAcumulandoRobo = true;
						roboAcumulado = 4;
					} else {
						roboAcumulado+=4;
					}
				} else {
					robarCartaJugador(siguienteJugador(), 4);
					esSalto=true;
				}
				break;
				
			case x2:
				int numCartas = siguienteJugador().getMano().size();
				robarCartaJugador(siguienteJugador(), numCartas);
				esSalto=true;
				break;
				
			case rayosX:
				for (int i = 0 ; i < configuracion.getMaxParticipantes() ; i++) {
					if( i != turno ) {
						Jugador j = jugadores.get(i);
						List<Carta> mano = j.getMano();
						Collections.shuffle(mano);
						boolean hecho = false;
						for (int carta = 0; carta < mano.size() && !hecho; carta++) {
							if(!mano.get(carta).isVisiblePor(turno)) {
								mano.get(carta).marcarVisible(turno);
								hecho = true;
							}
						}
						System.out.println("Termina el computo del rayos X");
					}
				}
				break;
				
			case reversa:
				this.sentidoHorario = ! this.sentidoHorario;
				break;
				
			case salta:
				esSalto = true;//avanzarTurno();
				break;
				
			case cambioColor:
				break;

			default:
				break;
		}
		
		this.cartasJugadas.add(c); //La añade al final (por implementaciones de rellenar y robar del mazo);
		if (c.esDelTipo(Carta.Tipo.cambioColor) || c.esDelTipo(Carta.Tipo.mas4)) {
			for(int i = 0; i < this.jugadores.get(turno).getMano().size(); i++) {
				if (this.jugadores.get(turno).getMano().get(i).esDelTipo(c.getTipo())) {
					this.jugadores.get(turno).getMano().remove(i);
					break;
				}
			}
			
		} else {
			this.jugadores.get(turno).getMano().remove(c);
		}
		
		
		
		if(	configuracion.getReglas().isEvitarEspecialFinal() && 
				this.jugadores.get(turno).getMano().size()==1 &&
				this.jugadores.get(turno).getMano().get(0).esDelColor(Carta.Color.comodin)) {
			robarCartaJugador(this.jugadores.get(turno), 2);
		}
		if(hayIntercambio) {
			List<Carta> nuevaMano = new ArrayList<>(jugadores.get(jugada.getJugadorObjetivo()).getMano());
			jugadores.get(jugada.getJugadorObjetivo()).getMano().clear();
			jugadores.get(jugada.getJugadorObjetivo()).getMano().addAll(jugadores.get(turno).getMano());
			jugadores.get(turno).getMano().clear();
			jugadores.get(turno).getMano().addAll(nuevaMano);
		}
		if (this.jugadores.get(turno).getMano().size()!=1) {
			this.jugadores.get(turno).setProtegido_UNO(false);
		}
		if (esSalto) {
			avanzarTurno();
			repeticionTurno = true;
		}
	}
	
	//FUNCIONA
	private boolean compruebaPuedeJugar() {
		Carta anterior = getUltimaCartaJugada();
		for(Carta c : jugadores.get(turno).getMano()) {
			if (Carta.compartenColor(c, anterior) ||
			    c.esDelColor(Carta.Color.comodin) ||
			    Carta.compartenTipo(c, anterior)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean compruebaPuedeJugar(int jugador) {
		Carta anterior = getUltimaCartaJugada();
		for(Carta c : jugadores.get(jugador).getMano()) {
			if (Carta.compartenColor(c, anterior) ||
			    c.esDelColor(Carta.Color.comodin) ||
			    Carta.compartenTipo(c, anterior)) {
				return true;
			}
		}
		return false;
	}
	
		
	private void ejecutarJugada(Jugada jugada) {
		ultimaJugada = jugada;
		turnoUltimaJugada = turno;
		repeticionTurno = false;
		if(modoJugarCartaRobada) { //FUNCIONA
			if(jugada.getCartas()!=null && jugada.getCartas().size()==1) {
				juegaCarta(jugada.getCartas().get(0), jugada);
			}
			cartaRobada=null;
			modoJugarCartaRobada=false;
			
		} else if(jugada.isRobar()) {
			if(modoAcumulandoRobo) {
				modoAcumulandoRobo=false;
				robarCartaJugador(this.jugadores.get(turno), roboAcumulado);
				roboAcumulado=0;
				
			} else if (configuracion.getModoJuego().equals(ConfigSala.ModoJuego.Attack)) {
				int random_robo = new Random().nextInt(MAX_ROBO_ATTACK)+1;
				robarCartaJugador(this.jugadores.get(turno), random_robo);
				
			} else { //FUNCIONA
				if(this.jugadores.get(turno).getMano().size() < 20) {
					this.cartaRobada = robarCarta(); 	//No se usaba la variable global, se usaba una local
					this.jugadores.get(turno).getMano().add(cartaRobada);
					if (Carta.compartenColor(cartaRobada, getUltimaCartaJugada()) || cartaRobada.esDelColor(Carta.Color.comodin) 
							|| Carta.compartenTipo(cartaRobada,getUltimaCartaJugada())) {
						modoJugarCartaRobada=true;
					}
				}
			}
			
		} else {
			for (Carta c : jugada.getCartas()) {
				juegaCarta(c, jugada);
			}
			
		}
		
		//repite turno por ser dos jugadores
		boolean reversaRepiteTurno = getJugadores().size() == 2 
									&& !jugada.isRobar() 
									&& !modoAcumulandoRobo
									&& jugada.getCartas().get(0).esDelTipo(Carta.Tipo.reversa);
		if (reversaRepiteTurno) {
			repeticionTurno = true;
		}
		
		if(!modoJugarCartaRobada && !reversaRepiteTurno) {
			avanzarTurno();
		}
		
		// Se comprueba si se ha acabado la partida
		for (Jugador j : this.jugadores) {
			j.setPenalizado_UNO(false);
			if (j.getMano().size() == 0) {
				this.terminada = true;
			}
		}
		
	}
	

	private void cambiarColorAleatorioIA(Carta c) {
		int random_color = new Random().nextInt(4);
		switch(random_color) {
			case 0:
				c.setColor(Carta.Color.amarillo);
				break;
			case 1:
				c.setColor(Carta.Color.rojo);
				break;
			case 2:
				c.setColor(Carta.Color.azul);
				break;
			case 3:
				c.setColor(Carta.Color.verde);
				break;
		}
	}
	
	
	/**************************************************************************/
	// Funciones públicas
	/**************************************************************************/
	
	public boolean ejecutarJugadaJugador(Jugada jugada, UUID jugadorID) {
		synchronized (LOCK) {
			if (validarJugada(jugada) && 
					!this.jugadores.get(turno).isEsIA() &&
					this.jugadores.get(turno).getJugadorID().equals(jugadorID)) {
				ejecutarJugada(jugada);
				return true;
			} else {
				return false;
			}
		}
	}
	
	
	public void ejecutarJugadaIA() {
		synchronized (LOCK) {
			if (this.jugadores.get(turno).isEsIA()) {
				Jugada jugadaIA = new Jugada();	// por defecto, robar
				
				if (compruebaPuedeJugar()) {	
					Carta cartaCentral = getUltimaCartaJugada();
					
					if (modoAcumulandoRobo) {
						for (Carta c : this.jugadores.get(turno).getMano()) {
							if(compatibleAcumulador(c) && 
									((Carta.compartenTipo(c, cartaCentral)) 	//Si la carta es usable según las reglas
											|| Carta.compartenColor(getUltimaCartaJugada(),c)  
											|| c.esDelTipo(Carta.Tipo.mas4))) {
								
								List<Carta> listaCartas = new ArrayList<>();
								listaCartas.add(c);
								jugadaIA.setCartas(listaCartas);
								jugadaIA.setRobar(false);
								
								if (c.esDelColor(Carta.Color.comodin)) {
									cambiarColorAleatorioIA(c);
								}
								break;
							}
						}
						
					} else if (modoJugarCartaRobada) {		
						List<Carta> listaCartas = new ArrayList<>();
						listaCartas.add(cartaRobada);
						jugadaIA.setCartas(listaCartas);
						jugadaIA.setRobar(false);
						
						if (cartaRobada.esDelColor(Carta.Color.comodin)) {
							cambiarColorAleatorioIA(cartaRobada);
						}
						
					} else {
						for (Carta c : this.jugadores.get(turno).getMano()) {
							if (c.esCompatible(cartaCentral)) {
								List<Carta> listaCartas = new ArrayList<>();
								listaCartas.add(c);
								jugadaIA.setCartas(listaCartas);
								jugadaIA.setRobar(false);
								
								if (c.esDelColor(Carta.Color.comodin)) {
									cambiarColorAleatorioIA(c);
								}
								break;
							}
						}
					}	
					
					if (!validarJugada(jugadaIA)) {
						System.err.println("ERROR: la IA ha elegido una jugada no válida");
					}
					
					if (!jugadaIA.isRobar() && 
							this.jugadores.get(turno).getMano().size() - jugadaIA.getCartas().size() == 1) {
						pulsarBotonUNOInterno(turno);		// Se protege
					}
					
					if (!jugadaIA.isRobar() && 
							jugadaIA.getCartas().get(0).esDelTipo(Carta.Tipo.intercambio)) {
						int mejorJugador = 0;
						int menorNumCartas = 300;
							
						//busca el jugador con menos cartas
						for (int indice = 0; indice < jugadores.size(); indice++) { 	
							if (jugadores.get(indice).getMano().size() < menorNumCartas) {
								mejorJugador = indice;
								menorNumCartas = jugadores.get(indice).getMano().size();
							}
						}
						
						jugadaIA.setJugadorObjetivo(mejorJugador);
					}
				}
				
				System.out.println("* * * Jugada elegida por la IA: " + jugadaIA);
				ejecutarJugada(jugadaIA);
				
				
				// Comprueba si puede denunciar a alguien (por simplicidad solo lo hace en su turno)
				for (Jugador j : this.jugadores) {
					if(!j.isProtegido_UNO() && j.getMano().size()==1) { //Pillado
						pulsarBotonUNOInterno(turno);		// Se protege
						System.out.println("* * * La IA pulsa el botón de 1");
					}	
				}
			}
		}
	}
	
	//Cuando un jugador se pasa del tiempo de turno
	public void saltarTurno() {
		synchronized (LOCK) {
			ejecutarJugada(new Jugada());
			if (modoJugarCartaRobada) {
				modoJugarCartaRobada = false;
				avanzarTurno();
			}
		}
		
	}
	
	
	public boolean turnoDeIA() {
		synchronized (LOCK) {
			return this.jugadores.get(turno).isEsIA();
		}
	}
	
	public void expulsarJugador(UUID jugadorID) {
		synchronized (LOCK) {
			//se sustituye por IA
			for (Jugador j : jugadores) {
				if(!j.isEsIA() && j.getJugadorID().equals(jugadorID)) {
					j.setEsIA(true);
					j.setJugadorID(null);
					break;
				}
			}
		}
	}
	
	
	public void pulsarBotonUNO(UUID jugadorID) { 
		synchronized (LOCK) {
			repeticionTurno = false;
			ultimaJugada = null;
			for (int indice = 0; indice < jugadores.size(); indice++) {
				if (jugadores.get(indice).getJugadorID() != null && 
						jugadores.get(indice).getJugadorID().equals(jugadorID)) {
					pulsarBotonUNOInterno(indice);
					break;
				}
			}
		}
	}
	
	private void pulsarBotonUNOInterno(int jugador) { 
		Jugador j = jugadores.get(jugador);
		if ((jugador == turno 
			 && compruebaPuedeJugar(jugador))
				|| j.getMano().size()==1) { 
			//Si es su turno y puede jugar la penultima carta, o solo tiene una, se protege
			j.setProtegido_UNO(true);
		}
		
		for (Jugador j2 : this.jugadores) {
			if(!j2.isProtegido_UNO() && j2.getMano().size()==1) { //Pillado, roba dos cartas.
				robarCartaJugador(j2, 2);
				j2.setPenalizado_UNO(true);
			}	
		}
	}
	
	public int getNumIAs() {
		synchronized (LOCK) {
			int numIAs = 0;
			for (Jugador j : this.jugadores) {
				if (j.isEsIA()) {
					numIAs++;
				}
			}
			return numIAs;
		}
	}
	
	
	/**************************************************************************/
	// Para los FRONTENDs
	/**************************************************************************/
	
	// Se devuelven ordenados en sentido horario
	public List<Jugador> getJugadores() {
		return jugadores;
	}
	
	public int getTurno() {
		return turno;
	}
	
	// Devuelve -1 si no se ha encontrado
	public int getIndiceJugador(UUID jugadorID) {
		for (int i = 0; i < this.jugadores.size() ; i++) {
			if (Objects.equals(jugadores.get(i).getJugadorID(), jugadorID)) {
				return i;
			}
		}
		return -1;
	}
	
	public Carta getUltimaCartaJugada() {
		return this.cartasJugadas.get(this.cartasJugadas.size()-1);
	}
	
	public boolean isSentidoHorario() {
		return sentidoHorario;
	}

	public void setSentidoHorario(boolean sentidoHorario) {
		synchronized (LOCK) {
			this.sentidoHorario = sentidoHorario;
		}
	}

	
	public boolean validarJugada(Jugada jugada) {
		if (jugada.isRobar()) { //FUNCINA
			if(jugadores.get(turno).getMano().size()>=20 && compruebaPuedeJugar()) { //No puede robar si puede jugar (sobraba el negado)
				return false;
			} else {
				return true;
			}
		} else if(modoJugarCartaRobada) {
			if(jugada.getCartas().isEmpty() 
					|| jugada.getCartas().get(0).equals(this.cartaRobada) 
					|| this.cartaRobada.esDelColor(Carta.Color.comodin)) { //Si se juega la carta robada o ninguna
				return true;
			} else {
				return false;
			}
		}else if (jugada.getCartas() == null || jugada.getCartas().isEmpty()) {
			return false;
		} else if(modoAcumulandoRobo) {
			Carta anterior = getUltimaCartaJugada();
			if(jugada.getCartas().size()!=1) { //Solo se puede jugar una
				return false;
			} else {
				Carta c = jugada.getCartas().get(0);
				if(compatibleAcumulador(c) && (Carta.compartenTipo(c, anterior) //Si la carta es usable según las reglas
								|| Carta.compartenColor(getUltimaCartaJugada(),c)  || c.esDelTipo(Carta.Tipo.mas4))) {
					return true;
				}
			}
	    } else { //FUNCIONA
			Carta anterior = getUltimaCartaJugada();
			boolean valida = false;
			Carta.Tipo tipo = jugada.getCartas().get(0).getTipo();
			
			//Las únicas cartas que hacen "jugadas" son los números, para el resto de cartas solo se puede jugar una.
			if(configuracion.getReglas().isJugarVariasCartas() && Carta.esNumero(tipo)) { //FUNCIONA
				int numCartas = 0; //Se necesitan dos para definir si son escaleras o iguales
				PosiblesTiposJugadas pj = new PosiblesTiposJugadas(false,false,false);
				for (Carta c : jugada.getCartas()) {
					if (numCartas<=1) {
						if(numCartas==0) {
							valida = Carta.compartenTipo(c, anterior) || Carta.compartenColor(anterior,c);
						} else {
							pj = evaluaJugada(anterior,c);
							valida = pj.valida;
						}
						numCartas++;
					} else {
						if(pj.esEscalera) {
							valida = Carta.sonConsecutivas(anterior,c);
						} else if(pj.esIguales){
							valida = Carta.compartenTipo(c, anterior);
						} else {
							valida = false;
						}
					}
					anterior = c;
					if(!valida) {
						break;
					}
				}
				//FUNCIONA
			} else { //Cartas con efecto o en general sin poder jugar varias cartas
				if (jugada.getCartas().size()>1) {
					valida = false; //Solo se puede jugar una si no son números. (o si no se permite jugar más de una).
				}else {
					valida = Carta.compartenTipo(jugada.getCartas().get(0),anterior) 
							|| Carta.compartenColor(anterior,jugada.getCartas().get(0))
							|| jugada.getCartas().get(0).esDelTipo(Carta.Tipo.mas4)
							|| jugada.getCartas().get(0).esDelTipo(Carta.Tipo.cambioColor);
				}
			}
			
			return valida;
		}
		return false;
	}
	
	// Se debe mirar en cada turno, y cuando devuelva true ya se puede desconectar
	// del buzón de la partida con websockets
	public boolean estaTerminada() {
		return this.terminada;
	}
	
	public boolean isHayError() {
		return hayError;
	}

	private void setHayError(boolean hayError) {
		this.hayError = hayError;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Long getFechaInicio() {
		return fechaInicio;
	}

	public ConfigSala getConfiguracion() {
		return configuracion;
	}

	public Jugador getJugadorActual() {
		return jugadores.get(turno);
	}

	
	

	@Override
	public String toString() {
		final int maxLen = 5;
		return "Partida [hayError=" + hayError + ", error=" + error + ", mazo="
				+ (mazo != null ? mazo.subList(0, Math.min(mazo.size(), maxLen)) : null) + ", cartasJugadas="
				+ (cartasJugadas != null ? cartasJugadas.subList(0, Math.min(cartasJugadas.size(), maxLen)) : null)
				+ ", jugadores=" + (jugadores != null ? jugadores.subList(0, Math.min(jugadores.size(), maxLen)) : null)
				+ ", turno=" + turno + ", sentidoHorario=" + sentidoHorario + ", configuracion=" + configuracion
				+ ", terminada=" + terminada + ", fechaInicio=" + fechaInicio + ", modoAcumulandoRobo="
				+ modoAcumulandoRobo + ", roboAcumulado=" + roboAcumulado + ", modoJugarCartaRobada="
				+ modoJugarCartaRobada + ", cartaRobada=" + cartaRobada + "]";
	}

	public Carta.Color getColorActual() {
		return getUltimaCartaJugada().getColor();
	}

	public boolean isModoAcumulandoRobo() {
		return modoAcumulandoRobo;
	}

	public void setModoAcumulandoRobo(boolean modoAcumulandoRobo) {
		synchronized (LOCK) {
			this.modoAcumulandoRobo = modoAcumulandoRobo;
		}
	}

	public boolean isModoJugarCartaRobada() {
		return modoJugarCartaRobada;
	}

	public void setModoJugarCartaRobada(boolean modoJugarCartaRobada) {
		synchronized (LOCK) {
			this.modoJugarCartaRobada = modoJugarCartaRobada;
		}
	}

	
	public Carta getCartaRobada() {
		return cartaRobada;
	}

	public void setCartaRobada(Carta cartaRobada) {
		synchronized (LOCK) {
			this.cartaRobada = cartaRobada;
		}
	}

	public Partida getPartidaAEnviar() {
		synchronized (LOCK) {
			Partida partidaResumida = new Partida();
			
			partidaResumida.hayError = hayError;
			partidaResumida.error = error;
			
			partidaResumida.mazo = null;
			
			if (cartasJugadas != null && !cartasJugadas.isEmpty()) {
				partidaResumida.cartasJugadas = this.cartasJugadas.subList(this.cartasJugadas.size()-1, this.cartasJugadas.size());
			} else {
				partidaResumida.cartasJugadas = this.cartasJugadas;
			}
			
			partidaResumida.ultimaJugada = this.ultimaJugada;
			partidaResumida.turnoUltimaJugada = this.turnoUltimaJugada;
			
			
			partidaResumida.jugadores = jugadores;
			partidaResumida.turno = turno;
			partidaResumida.sentidoHorario = sentidoHorario;
			
			partidaResumida.configuracion = configuracion;
			partidaResumida.terminada = terminada;	
			
			//Fecha de inicio de la partida (Ya en formato sql porque no la necesita el frontend en este punto). 
			partidaResumida.fechaInicio = fechaInicio; 
			
			//Variables para extraer resultados de efectos
			partidaResumida.modoAcumulandoRobo = modoAcumulandoRobo;
			partidaResumida.roboAcumulado = roboAcumulado;
			partidaResumida.modoJugarCartaRobada = modoJugarCartaRobada;
			partidaResumida.cartaRobada = cartaRobada;
			
			partidaResumida.repeticionTurno = repeticionTurno;
			
			partidaResumida.salaID = null;
			
			return partidaResumida;
		}
	}

	public UUID getSalaID() {
		return salaID;
	}

	public void setSalaID(UUID salaID) {
		synchronized (LOCK) {
			this.salaID = salaID;
		}
	}
	
	
	public boolean isRepeticionTurno() {
		return repeticionTurno;
	}

	public void setRepeticionTurno(boolean repeticionTurno) {
		synchronized (LOCK) {
			this.repeticionTurno = repeticionTurno;
		}
	}

	public int getRoboAcumulado() {
		return roboAcumulado;
	}
	
	public Jugada getUltimaJugada(){
		return this.ultimaJugada;
	}
	
	public int getTurnoUltimaJugada() {
		return turnoUltimaJugada;
	}
	
	public void resetUltimaJugada() {
		synchronized (LOCK) {
			this.ultimaJugada = null;
		}
	}
}
