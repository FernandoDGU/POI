package com.fcfm.poi_proyect_003.Clases

import com.google.firebase.database.Exclude
import java.sql.Timestamp

class ChatGrupal(
    var id: String = "",
    var contenido: String = "",
    var de: String = "",
    var deNombre: String ="",
    var timestamp: Any? = null,
    var encrypt: Boolean = false
) {
    @Exclude
    var esMio: Boolean = false
}