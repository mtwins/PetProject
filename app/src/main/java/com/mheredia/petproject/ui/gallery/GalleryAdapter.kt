package com.mheredia.petproject.ui.gallery

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mheredia.petproject.GlideApp
import com.mheredia.petproject.MainActivity
import com.mheredia.petproject.R
import com.mheredia.petproject.data.model.PetPicture
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class GalleryAdapter(
    var result: MutableList<PetPicture>,
    var context: Context,
    var viewModel: GalleryViewModel,
    var galleryFragment: FragmentActivity,
    var openDialog: (petPicture: PetPicture) -> Unit,
    var showNoItemMessage: (root: View, result: List<PetPicture>) -> Unit,
    var root: View
) :
    RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {
    fun addPicture(petPicture: PetPicture) {
        result.add(petPicture)
        showNoItemMessage(root,result )
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

    fun deletePicture(index: Int) {
        result.removeAt(index)
        showNoItemMessage(root,result )
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
        val picture = Firebase.storage.reference.child(result[position].pictureUrl)
        GlideApp.with(holder.itemView.context)
            .load(MainActivity.storage.reference.child(result[position].pictureUrl))
            .error(
                Glide.with(holder.itemView.context)
                    .load(
                        AppCompatResources.getDrawable(
                            holder.itemView.context,
                            R.drawable.ic_menu_camera
                        )
                    )
            )
            .into(holder.petPicture)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val bitmap = Glide.with(context)
                    .asBitmap()
                    .load(picture)
                    .submit(100, 100).get()

                Palette.from(bitmap).generate { palette ->
                    palette?.getDarkMutedColor(ContextCompat.getColor(context, android.R.color.black))
                        .let { bgColor ->
                            if (bgColor != null) {
                                holder.tagHolder.setBackgroundColor(bgColor)
                            }
                        }
                }
            }catch (e:Exception) {

            }


        }
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
                openDialog( petPicture(adapterPosition))

            }
            itemView.setOnLongClickListener {
            viewModel.shareGalleryImage(galleryFragment,context,petPicture(adapterPosition))
                true
            }
        }

        private fun petPicture(position:Int): PetPicture {
            val picture = PetPicture(
                result[position].petId,
                result[position].pictureId,
                Firebase.auth.currentUser?.uid!!,
                result[position].pictureTag,
                result[position].pictureUrl
            )
            return picture
        }
    }


}
