package es.unizar.unoforall.model.partidas;

import es.unizar.unoforall.model.UsuarioVO;

public class Participante {
	private UsuarioVO usuario;
	private HaJugadoVO datosPartida;
	private int puesto;
	private int puntos;
	
	public Participante (UsuarioVO usuario, HaJugadoVO datosPartida) {
		this.usuario=usuario;
		this.datosPartida=datosPartida;
		puesto = 0;
		switch(datosPartida.getUsrsDebajo()) {
		case 3:
			puntos = 20;
			break;
		case 2:
			puntos = 10;
			break;
		case 1: 
			puntos = 5;
			break;
		case 0:
			puntos = 0;
			break;
		}
	}

	
	public int getPuesto() {
		return puesto;
	}


	public void setPuesto(int numParticipantes) {
		puesto = numParticipantes-datosPartida.getUsrsDebajo();
	}



	public int getPuntos() {
		return puntos;
	}



	public void setPuntos(int puntos) {
		this.puntos = puntos;
	}



	public UsuarioVO getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioVO usuario) {
		this.usuario = usuario;
	}

	public HaJugadoVO getDatosPartida() {
		return datosPartida;
	}

	public void setDatosPartida(HaJugadoVO datosPartida) {
		this.datosPartida = datosPartida;
	}
	
	
}
