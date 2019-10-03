package co.unicauca.clientes.client;

import co.unicauca.clientes.controller.GUIBusquedaClientesController;
import co.unicauca.clientes.modelo.ClientesDB;
import co.unicauca.clientes.vista.*;

/**
 *
 * @author Libardo Pantoja, Ricardo Zambrano
 */
public class RunMVC {

    public RunMVC() {
        //create Model and View

        ClientesDB myModel = new ClientesDB();
        GUIBusquedaClientes myView = new GUIBusquedaClientes(400, 50);
        
        GUIClientesProfesion myView2 = new GUIClientesProfesion(400,230);
        
        GUIEstadisticaPorSexo myView3 = new GUIEstadisticaPorSexo(400,430);
        
        GUIEstadisticaPorSexoGrafica myView4 = new GUIEstadisticaPorSexoGrafica(800,400);
        
        myModel.addObserver(myView);
        myModel.addObserver(myView2);
        myModel.addObserver(myView3);
        myModel.addObserver(myView4);

        GUIBusquedaClientesController myController = new GUIBusquedaClientesController();
        myController.addModel(myModel);
        myController.addView(myView);
        myView.addController(myController);
    }

}
