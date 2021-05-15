package com.fcfm.poi_proyect_003.Adaptadores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.fcfm.poi_proyect_003.Clases.SubGrupos
import com.fcfm.poi_proyect_003.Fragment.FragmentoGrupo
import kotlinx.android.synthetic.main.group_list.view.*

interface SubGruposClickListener{
    fun subGrupoListener(grupos: SubGrupos)
}

class subGruposAdapter(private val context: FragmentoGrupo,
    private val subGruposlist: MutableList<SubGrupos>,
    private val subGrupoClickListener: SubGruposClickListener):
        RecyclerView.Adapter<subGruposAdapter.subGrupoViewHolder>() {

    inner class subGrupoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindData(currenGrup: SubGrupos) {
            //Se manda lo que es el nombre del grupo a la lista
            itemView.txtNombreGrupoLista.text = currenGrup.nombreGrupo

            itemView.txtNombreGrupoLista.setOnClickListener(null)
            itemView.txtNombreGrupoLista.setOnClickListener{
                subGrupoClickListener.subGrupoListener(currenGrup)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): subGrupoViewHolder {
        val itemView = LayoutInflater.from(parent.context).
        inflate(com.fcfm.poi_proyect_003.R.layout.group_list, parent, false)
        return subGrupoViewHolder(itemView)
    }

    override fun getItemCount(): Int = subGruposlist.size

    override fun onBindViewHolder(holder: subGrupoViewHolder, position: Int) {
        holder.bindData(subGruposlist[position])
    }

}