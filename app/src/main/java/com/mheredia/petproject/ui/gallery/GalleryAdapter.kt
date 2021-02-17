package com.mheredia.petproject.ui.gallery

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mheredia.petproject.R
import com.mheredia.petproject.data.model.PetPicture
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class GalleryAdapter(var result: MutableList<PetPicture>, var context: Context) :
    RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {
    fun addPicture(petPicture: PetPicture) {
        result.add(petPicture)
        notifyDataSetChanged()
    }

    fun editPicture(petPicture: PetPicture) {
        var position = 0
        for (item in result) {
            if (item.pictureId == petPicture.pictureId) {
                result[position] = petPicture
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
            .inflate(R.layout.picture_cards, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return result.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tag.text = result[position].pictureTag
        val picture ="https://ichef.bbci.co.uk/news/1024/cpsprodpb/151AB/production/_111434468_gettyimages-1143489763.jpg"
//            Firebase.storage.reference.child("profile/${Firebase.auth.currentUser?.uid.toString()}")
         Glide.with(context)
            .asBitmap()
            .load(picture)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(holder.petPicture)

        GlobalScope.launch(Dispatchers.IO) {
            val bitmap = Glide.with(context)
                .asBitmap()
                .load(picture)
                .centerCrop()
                .submit(100, 100).get()
            Palette.from(bitmap).generate { palette ->
                palette?.getDarkVibrantColor(ContextCompat.getColor(context, android.R.color.black)).let {bgColor->
                    if (bgColor != null) {
                        holder.tagHolder.setBackgroundColor(bgColor)
                    }
                }
            }

        }


//        val photo = BitmapFactory.decodeResource(context.resources,
//            pictureGlide.getImageResourceId(context))
//        Palette.from(photo).generate { palette ->
//            val bgColor = palette?.getMutedColor(
//                ContextCompat.getColor(context,
//                android.R.color.black))
//            holder.tagHolder.setBackgroundColor(bgColor)
//        }
//        holder.subTitle.text = result[position].notes
//
//        holder.emailImage.setOnClickListener {
//            openEmail(result[position].email )
//        }
//        holder.phoneImage.setOnClickListener {
//            openPhone(result[position].phone )
//        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tag: TextView
        var petPicture: ImageView
        var tagHolder: LinearLayout


        init {

            tag = itemView.findViewById(R.id.tag)
            tagHolder = itemView.findViewById(R.id.tagHolder)
            petPicture = itemView.findViewById(R.id.petImage)


            itemView.setOnClickListener {
//                var position: Int = getAdapterPosition()
//                val picture = Picture(
//                    Firebase.auth.currentUser?.uid.toString(),
//                    result[position].name,
//                    result[position].phone,
//                    result[position].email,
//                    result[position].notes,
//                    result[position].contactId
//                )
//                openDialog(picture)
            }
        }
    }


}
