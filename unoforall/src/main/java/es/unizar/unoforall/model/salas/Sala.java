package es.unizar.unoforall.model.salas;


import java.util.HashMap;
import java.util.UUID;

import es.unizar.unoforall.model.UsuarioVO;

public class Sala {	
	//Para devolver una sala que no existe
	private boolean noExiste;
	
	private ConfigSala configuracion;
	
	private boolean enPartida;
	
	//Identificador de cada usuario con su VO
	private HashMap<UUID, UsuarioVO> participantes;
	//Conjunto de participantes con el indicador de si están listos o no
	private HashMap<UUID, Boolean> participantes_listos;
	
	public Sala() {
		participantes = new HashMap<>();
		participantes_listos = new HashMap<>();
		noExiste = true;
	}
	
	public Sala(ConfigSala configuracion) {
		this();
		this.configuracion = configuracion;
		this.setEnPartida(false);
		this.noExiste = false;
	}

	public ConfigSala getConfiguracion() {
		return configuracion;
	}

	public boolean isEnPartida() {
		return enPartida;
	}

	public void setEnPartida(boolean enPartida) {
		this.enPartida = enPartida;
	}
	
		// Devuelve false si no es posible añadir un nuevo participante
	public boolean nuevoParticipante(UsuarioVO participante) {
		if(participantes.size() < configuracion.getMaxParticipantes()) {
			participantes.putIfAbsent(participante.getId(), participante);
			participantes_listos.putIfAbsent(participante.getId(), false);
			return true;
		} else {
			return false;
		}
	}
	
	// Devuelve false si no es posible añadir un nuevo participante
	public void eliminarParticipante(UUID participanteID) {
		participantes.remove(participanteID);
		participantes_listos.remove(participanteID);
	}
	
	public void nuevoParticipanteListo(UUID participanteID) {
		if(participantes.containsKey(participanteID)) {
			participantes_listos.put(participanteID, true);
		}
	}
	
	// Devuelve un hashmap con el  usuarioID - UsuarioVO
	public boolean hayParticipante(UUID usuarioID) {
		if (participantes.containsKey(usuarioID)) {
			return true;
		} else {
			return false;
		}
	}
	
	// Devuelve un hashmap con el VO de cada usuario relacionado con si está o no preparado
	public HashMap<UsuarioVO, Boolean> getParticipantes() {
		HashMap<UsuarioVO, Boolean> result = new HashMap<>();
		participantes.forEach((k,v) -> result.put(v, participantes_listos.get(k)));
		return result;
	}
	
	public int numParticipantes() {
		return participantes.size();
	}
	
	public boolean puedeUnirse() {
		if (getConfiguracion().isEsPublica()
				&& numParticipantes() < getConfiguracion().getMaxParticipantes() 
				&& !isEnPartida()) {
			return true;
		} else {
			return false;
		}
	}
	

	@Override
	public String toString() {
		return "Sala [configuracion=" + configuracion + ", enPartida=" + enPartida + ", participantes=" + participantes
				+ "]";
	}

	public boolean isNoExiste() {
		return noExiste;
	}
}
