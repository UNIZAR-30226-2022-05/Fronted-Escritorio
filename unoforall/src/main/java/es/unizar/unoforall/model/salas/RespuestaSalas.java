package es.unizar.unoforall.model.salas;

import java.util.HashMap;
import java.util.UUID;

public class RespuestaSalas {
	private boolean exito;
	private HashMap<UUID,Sala> salas;
	
	public RespuestaSalas() {
		salas = new HashMap<>();
		exito = false;
	}
	
	public RespuestaSalas(HashMap<UUID,Sala> salas) {
		this();
		this.setSalas(salas);
		this.exito = true;
	}

	public HashMap<UUID,Sala> getSalas() {
		return salas;
	}

	public void setSalas(HashMap<UUID,Sala> salas) {
		this.salas = salas;
	}

	public boolean isExito() {
		return exito;
	}
	
	public RespuestaSalas getRespuestaAEnviar() {
		RespuestaSalas respuestaResumida = new RespuestaSalas();
		
		respuestaResumida.exito = this.exito;

		respuestaResumida.salas = new HashMap<>();
		this.salas.forEach((uuid, sala) -> 
			respuestaResumida.salas.put(uuid, sala.getSalaAEnviar()));
		
		return respuestaResumida;
	}
}
