package com.fcfm.poi_proyect_003.Adaptadores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fcfm.poi_proyect_003.Clases.Tareas
import com.fcfm.poi_proyect_003.Fragment.FragmentoTareas
import kotlinx.android.synthetic.main.item_tareas.view.*

interface TareasClickListener{
    fun detalle(Dtarea: Tareas)
}


class TareasAdapter(
    private val context: FragmentoTareas,
    private val tarea:MutableList<Tareas>,
    private val tareaClick: TareasClickListener): RecyclerView.Adapter<TareasAdapter.TareaViewHolder>() {

    inner class TareaViewHolder (itemView: View):RecyclerView.ViewHolder(itemView){
            fun DataInfo(tarea: Tareas){
                itemView.txtNombreTarea.text = tarea.Titulo
                itemView.txtDescTarea.text = tarea.Desc
                itemView.chConcluir.isChecked = tarea.check
                itemView.imgTarea.setOnClickListener(null)
                itemView.imgTarea.setOnClickListener{
                    tareaClick.detalle(tarea)
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareasAdapter.TareaViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(com.fcfm.poi_proyect_003.R.layout.item_tareas, parent, false)
        return TareaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TareasAdapter.TareaViewHolder, position: Int) {
        holder.DataInfo(tarea[position])
    }

    override fun getItemCount(): Int {
        return tarea.size
    }
}