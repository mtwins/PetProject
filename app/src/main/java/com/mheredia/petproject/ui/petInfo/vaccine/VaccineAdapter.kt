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
            .inflate(R.layout.pet_card, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return result.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.title.text = result[position].petName
//        holder.subTitle.text = result[position].petBreed
//        holder.subTitle2.text = "${result[position].petAge} years old"

//        holder.petImage.setOnClickListener {
//            selectPetProfile( result[position].petId ,position)
//        }
//        if(!result[position].profileUrl.isNullOrEmpty()) {
//            GlideApp.with(holder.itemView.context)
//                .load(MainActivity.storage.reference.child(result[position].profileUrl))
//                .centerCrop()
//                .circleCrop()
//                .error(
//                    Glide.with(holder.itemView.context)
//                        .load(getDrawable(holder.itemView.context, R.drawable.ic_menu_camera))
//                )
//                .into(holder.petImage)
//        }



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


//            itemView.setOnClickListener {
//                var position: Int = getAdapterPosition()
//                val vaccineInfo = Vaccine(
//                    result[position].petId,
//                    result[position].petName,
//                    result[position].petType,
//                    result[position].petBreed,
//                    result[position].petAge,
//                    Firebase.auth.uid.toString()
//                )
//                openDialog(vaccineInfo)
//            }
        }
    }


}



