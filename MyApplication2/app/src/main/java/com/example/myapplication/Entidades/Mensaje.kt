package com.example.myapplication.Entidades

open class Mensaje {
    var mensaje: String? = null
    var nombre: String? = null
    var fotoPerfil: String? = null
    var type_mensaje: String? = null
    var hora: String? = null

    var urlFoto: String? = null

    constructor() {}
    constructor(mensaje: String?, nombre: String?, fotoPerfil: String?, type_mensaje: String?, hora: String?) {
        this.mensaje = mensaje
        this.nombre = nombre
        this.fotoPerfil = fotoPerfil
        this.type_mensaje = type_mensaje
        this.hora = hora
    }

    constructor(mensaje: String?,  urlFoto: String?,nombre: String?, fotoPerfil: String?, type_mensaje: String?, hora: String?) {
        this.mensaje = mensaje
        this.nombre = nombre
        this.fotoPerfil = fotoPerfil
        this.type_mensaje = type_mensaje
        this.hora = hora
        this.urlFoto = urlFoto
    }


}