package com.mheredia.petproject.ui.petInfo.medical

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mheredia.petproject.R
import com.mheredia.petproject.data.model.MedicalInfo
import kotlin.reflect.KFunction1

class MedicalAdapter(
    var result: MutableList<MedicalInfo>,
    var openDialog: (medicalInfo: MedicalInfo) -> Unit,
    var noItemMessage: KFunction1<MutableList<MedicalInfo>, Unit>
) :
    RecyclerView.Adapter<MedicalAdapter.ViewHolder>() {


    fun addMedicalInfo(medicalInfo: MedicalInfo) {
        result.add(medicalInfo)
        noItemMessage(result)
        notifyDataSetChanged()
    }

    fun editMedicalInfo(medicalInfo: MedicalInfo) {
        var position=0
        for (item in result) {
            if(item.petId==medicalInfo.petId){
                result[position]=medicalInfo
                break
            }
            position++
        }
        notifyDataSetChanged()
    }

    fun deleteMedicalInfo(index: Int) {
        result.removeAt(index)
        noItemMessage(result)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.medical_card, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return result.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = result[position].condition
        holder.subTitle.text = result[position].medicines
        holder.subTitle2.text = result[position].careInstructions
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title: TextView
        var subTitle: TextView
        var subTitle2: TextView
        var petImage: ImageView


        init {

            title = itemView.findViewById(R.id.card_title)
            subTitle = itemView.findViewById(R.id.card_subtitle)
            subTitle2 = itemView.findViewById(R.id.card_subtitle_2)
            petImage = itemView.findViewById(R.id.pet_image)


            itemView.setOnClickListener {
                val position: Int = getAdapterPosition()
                val medicalInfo = MedicalInfo(
                    result[position].medicalId,
                    result[position].condition,
                    result[position].medicines,
                    result[position].careInstructions,
                    result[position].petId
                )
                openDialog(medicalInfo)
            }
        }
    }


}



