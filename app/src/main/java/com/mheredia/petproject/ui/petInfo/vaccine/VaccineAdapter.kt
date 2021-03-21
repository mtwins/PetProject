package com.mheredia.petproject.ui.petInfo.vaccine

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mheredia.petproject.GlideApp
import com.mheredia.petproject.MainActivity
import com.mheredia.petproject.R
import com.mheredia.petproject.data.model.PetInfo
import com.mheredia.petproject.data.model.Vaccine
import kotlinx.coroutines.tasks.await

class VaccineAdapter(
    var result: MutableList<Vaccine>,
    var openDialog: (vaccine: Vaccine) -> Unit
) :
    RecyclerView.Adapter<VaccineAdapter.ViewHolder>() {


    fun addVaccine(vaccineInfo: Vaccine) {
        result.add(vaccineInfo)
        notifyDataSetChanged()
    }

    fun editVaccine(vaccineInfo: Vaccine) {
        var position=0
        for (item in result) {
            if(item.petId==vaccineInfo.petId){
                result[position]=vaccineInfo
                break
            }
            position++
        }
        notifyDataSetChanged()
    }

    fun deleteContact(index: Int) {
        result.removeAt(index)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.vaccine_card, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return result.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = "${result[position].vaccineName}"
        holder.subTitle.text = "Given vaccine on ${result[position].vaccineDate}"
        holder.subTitle2.text = "Renew vaccine on ${result[position].renewVaccineDate} "
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
                val vaccineInfo = Vaccine(
                    result[position].vaccineId,
                    result[position].vaccineName,
                    result[position].vaccineDate,
                    result[position].renewVaccineDate,
                    result[position].petId
                )
                openDialog(vaccineInfo)
            }
        }
    }


}



