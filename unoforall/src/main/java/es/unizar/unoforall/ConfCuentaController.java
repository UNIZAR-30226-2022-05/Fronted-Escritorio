package es.unizar.unoforall;

import java.net.URL;
import java.util.ResourceBundle;

import es.unizar.unoforall.api.RestAPI;
import es.unizar.unoforall.model.UsuarioVO;
import es.unizar.unoforall.utils.HashUtils;
import es.unizar.unoforall.utils.ImageManager;
import es.unizar.unoforall.utils.StringUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class ConfCuentaController implements Initializable {
	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	private static final boolean DEBUG = App.DEBUG;

	@FXML private VBox fondo;
	
	@FXML private ImageView imgMenu;
	
	@FXML private Button btnEliminar;
	
	@FXML private Label labelError;
	@FXML private TextField cajaNombre;	
	@FXML private TextField cajaCorreo;	
	@FXML private PasswordField cajaContrasenya;
	@FXML private PasswordField cajaContrasenya2;
	
	@FXML private VBox contenedorOculto;
	@FXML private TextField cajaCodigo;
	
	private UsuarioVO usuario;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//PONER EL FONDO CORRESPONDIENTE
		fondo.setBackground(ImageManager.getBackgroundImage(App.getPersonalizacion().get("tableroSelec")));
		
		//CONFIGURACION DE EFECTO DE HOVER
		imgMenu.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				imgMenu.setFitWidth(210);
				imgMenu.setFitHeight(160);
				imgMenu.setEffect(new Glow(0.3));
			}
		});
		imgMenu.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				imgMenu.setFitWidth(200);
				imgMenu.setFitHeight(150);
				imgMenu.setEffect(null);
			}
		});
		

		btnEliminar.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				btnEliminar.setEffect(new Glow(0.5));
			}
		});
		btnEliminar.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				btnEliminar.setEffect(null);
			}
		});
		
		autoCompletar();
	}
	
	public void autoCompletar() {	
		RestAPI apirest = App.apiweb.getRestAPI();
		apirest.setOnError(e -> {if (DEBUG) System.out.println(e);});
		
		apirest.openConnection("/api/sacarUsuarioVO");
    	apirest.receiveObject(UsuarioVO.class, retorno -> {
    		if (retorno.isExito()) {
	    		usuario = retorno;
	    		cajaNombre.setText(StringUtils.parseString(retorno.getNombre()));
	    		cajaCorreo.setText(StringUtils.parseString(retorno.getCorreo()));
	    	} else {
	    		labelError.setText("No se han podido autocompletar los datos de la cuenta");
	    	}
    	});
	}
	
	@FXML
    private void goBack(ActionEvent event) {
		labelError.setText("");
		if (contenedorOculto.isVisible()) {
			RestAPI apirest = App.apiweb.getRestAPI();
			
			apirest.openConnection("/api/actualizarCancel");
	    	apirest.receiveObject(String.class, retorno -> {
	    		if (retorno != null) {
		    		labelError.setText(StringUtils.parseString(retorno));
		    		if (DEBUG) System.out.println(StringUtils.parseString(retorno));
		    	} else {
					App.setRoot("principal");
				}
	    	});
		} else {
			App.setRoot("principal");
		}
	}

	@FXML
    private void goToMain(Event event) {
		labelError.setText("");
		if (contenedorOculto.isVisible()) {
			RestAPI apirest = App.apiweb.getRestAPI();
			
			apirest.openConnection("/api/actualizarCancel");
	    	apirest.receiveObject(String.class, retorno -> {
	    		if (retorno != null) {
		    		labelError.setText(StringUtils.parseString(retorno));
		    		if (DEBUG) System.out.println(retorno);
		    	}else {
		    		App.setRoot("principal");
		    	}
	    	});
		}
	}

	@FXML
	private void actualizarCuenta(ActionEvent event) {
		labelError.setText("");
		String nuevoCorreo = cajaCorreo.getText();
		String nuevoNombre = cajaNombre.getText();
		String nuevaContrasenna = cajaContrasenya.getText();
		String confirmarContrasenna = cajaContrasenya2.getText();
    	
		if (nuevoCorreo.equals("") || nuevoNombre.equals("")) {
			labelError.setText("Faltan parámetros");
			if (DEBUG) System.out.println("Faltan parámetros");
		} else if (!nuevaContrasenna.equals(confirmarContrasenna)) {
			labelError.setText("Las contraseñas no coinciden");
			if (DEBUG) System.out.println("Las contraseñas no coinciden");
		} else {
			if (nuevaContrasenna.equals("")) {
				nuevaContrasenna = usuario.getContrasenna();
			} else {
				HashUtils.cifrarContrasenna(nuevaContrasenna);
			}
			RestAPI apirest = App.apiweb.getRestAPI();
			apirest.addParameter("correoNuevo",nuevoCorreo);
			apirest.addParameter("nombre",nuevoNombre);
			apirest.addParameter("contrasenna",nuevaContrasenna);
			apirest.setOnError(e -> {if (DEBUG) System.out.println(e);});
			
			apirest.openConnection("/api/actualizarCuentaStepOne");
	    	apirest.receiveObject(String.class, retorno -> {
	    		if (retorno == null) {
		    		desocultarContenedorOculto();
		    	} else {
		    		labelError.setText(StringUtils.parseString(retorno));
		    		if (DEBUG) System.out.println(retorno);
		    	}
	    	});
		}
	}
	
	private void desocultarContenedorOculto() {
		contenedorOculto.setDisable(false);
		contenedorOculto.setVisible(true);
	}
	
	@FXML
	private void confirmarCodigo (ActionEvent event) {
		labelError.setText("");
		Integer codigo = 0;
		//Si el código no es un entero, no es válido
		try {
			codigo = Integer.parseInt(cajaCodigo.getText());
		} catch (Exception e) {
			labelError.setText("Por favor introduzca un código válido");
			return;
		}
		RestAPI apirest = App.apiweb.getRestAPI();
		apirest.addParameter("codigo",codigo);
		apirest.setOnError(e -> {if (DEBUG) System.out.println(e);});
		
		apirest.openConnection("/api/actualizarCuentaStepTwo");
		apirest.receiveObject(String.class, retorno -> {
			if (retorno == null) {
	    		if (DEBUG) System.out.println("Exito.");
	    		App.setRoot("principal");
	    	} else {
	    		labelError.setText(StringUtils.parseString(retorno));
	    		if (DEBUG) System.out.println(retorno);
	    	}
		});
	}
	
	@FXML
    private void deleteAccount(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Eliminar cuenta");
    	alert.setHeaderText("¿Quieres eliminar tu cuenta?");
    	
    	ButtonType respuesta = alert.showAndWait().get();
    	if (respuesta == ButtonType.OK) {
    		if (DEBUG) System.out.println("Primera confirmación eliminación cuenta");
    		
    		alert = new Alert(AlertType.CONFIRMATION);
        	alert.setTitle("Eliminar cuenta");
        	alert.setHeaderText("¿Estás seguro?");
        	alert.setContentText("Esta acción no es reversible y eliminará \n"
        						+ "TODOS los datos de tu cuenta de forma permanente");
        	
        	respuesta = alert.showAndWait().get();
        	if (respuesta == ButtonType.OK) {
        		if (DEBUG) System.out.println("Segunda confirmación eliminación cuenta.");
        		
        		//DESTRUCCION CUENTA
        		RestAPI apirest = App.apiweb.getRestAPI();
        		apirest.setOnError(e -> {if (DEBUG) System.out.println(e);});
        		
        		apirest.openConnection("/api/borrarCuenta");
            	apirest.receiveObject(String.class, retorno -> {
            		if (retorno.equals("BORRADA")) {
	        	    	App.setRoot("login");
	        	        App.cerrarConexion();
	        	    	if (DEBUG) System.out.println("Cuenta eliminada");
	            	} else {
	            		labelError.setText(StringUtils.parseString(retorno));
	            		if (DEBUG) System.out.println(retorno);
	            	}
            	});
        	}
    	}
	}

}
