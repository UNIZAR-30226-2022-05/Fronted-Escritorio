package es.unizar.unoforall.utils;

public enum Pantalla{
	CAMBIAR_IP("cambiarIP"),
	LOGIN("login"),
	REGISTER("registro"),
	CONFIRMACION_CORREO("confirmacionCorreo"),
	
	REESTABLECER_PASSWORD("recuperacionContrasenya"),
	ESPECIFICACION_CORREO("especificacionCorreo"),
	
	PRINCIPAL("principal"),
	
	CREAR_SALA("crearSala"),
	BUSQUEDA_SALA("buscarSala"),
	BUSQUEDA_AVANZADA_SALA("busquedaAvanzadaSala"),
	SALA("vistaSala"),
	SALA_PAUSADA("vistaSalaPausada"),
	PARTIDA("partida"),
	
	CONFIGURAR_ASPECTO("confAspecto"),
	CONFIGURAR_CUENTA("confCuenta"),
	NOTIFICACIONES("notificaciones"),
	AMIGOS("amigos"),
	HISTORIAL("historial");
	
	private String fxml;
	
	private Pantalla(String fxml){
		this.fxml = fxml;
	}
	
	public String getFXML(){
		return this.fxml;
	}
}
