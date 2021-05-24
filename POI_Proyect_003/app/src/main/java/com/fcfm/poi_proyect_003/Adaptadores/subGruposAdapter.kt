package com.fcfm.poi_proyect_003.Adaptadores

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.fcfm.poi_proyect_003.Clases.SubGrupos
import com.fcfm.poi_proyect_003.Fragment.FragmentoGrupo
import com.fcfm.poi_proyect_003.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.group_list.view.*

interface SubGruposClickListener{
    fun subGrupoListener(grupos: SubGrupos)
}

class subGruposAdapter(private val context: FragmentoGrupo,
    private val subGruposlist: MutableList<SubGrupos>,
    private val subGrupoClickListener: SubGruposClickListener):
        RecyclerView.Adapter<subGruposAdapter.subGrupoViewHolder>() {

    inner class subGrupoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val txtUserName: TextView = itemView.findViewById(R.id.txtNombreGrupoLista)
        fun bindData(currentGroup: SubGrupos) {
            //Se manda lo que es el nombre del grupo a la lista
            //itemView.txtNombreGrupoLista.text = currentGrup.nombreGrupo
            itemView.txtNombreGrupoLista.text = currentGroup.nombreGrupo
            val imagenRec = itemView.ivSubGrupoRec
            val imagenUri = Uri.parse(currentGroup.imagen)
            Picasso.get().load(imagenUri).into(imagenRec)
            itemView.txtNombreGrupoLista.setOnClickListener(null)
            itemView.txtNombreGrupoLista.setOnClickListener{
                subGrupoClickListener.subGrupoListener(currentGroup)
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