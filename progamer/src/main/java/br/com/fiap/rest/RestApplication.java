package br.com.fiap.rest;

import javax.ws.rs.core.Application;
import javax.ws.rs.ApplicationPath;

//Esta é a classe que responde/cuida das requisições em /api

@ApplicationPath("/api")
public class RestApplication extends Application {
	
}