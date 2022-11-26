package com.example.snsproject.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.snsproject.R
import com.example.snsproject.navigation.model.ContentDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.snsproject.databinding.FragmentDetailBinding
import kotlinx.android.synthetic.main.fragment_detail.view.*
import kotlinx.android.synthetic.main.item_detail.view.*


class DetailViewFragment : Fragment() {
    var firestore : FirebaseFirestore? = null
    var uid : String? = null
    lateinit var binding: FragmentDetailBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentDetailBinding.inflate(inflater,container,false)

        firestore = FirebaseFirestore.getInstance()
        uid = FirebaseAuth.getInstance().currentUser?.uid

        binding.detailviewfragmentRecyclerview.adapter = DetailViewRecyclerViewAdapter()
        binding.detailviewfragmentRecyclerview.layoutManager = LinearLayoutManager(activity)

        return binding.root
    }
    inner class DetailViewRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        var contentDTOs : ArrayList<ContentDTO> = arrayListOf()
        var contentUidList : ArrayList<String> = arrayListOf()

        init {
            firestore?.collection("images")?.orderBy("timestamp")?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                contentDTOs.clear()
                contentUidList.clear()
                //Sometimes, This code return null of querySnapshot when it signout
                if(querySnapshot == null) return@addSnapshotListener

                for(snapshot in querySnapshot!!.documents){
                    var item = snapshot.toObject(ContentDTO::class.java)
                    contentDTOs.add(item!!)
                    contentUidList.add(snapshot.id)
                }
                notifyDataSetChanged()
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val binding = LayoutInflater.from(parent.context).inflate(R.layout.item_detail,parent,false)
            return CustomViewHolder(binding)
        }

        inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//            val postUserText = itemView.findViewById<TextView>(R.id.detailviewitem_profile_textview)
//            val postMainImage = itemView.findViewById<ImageView>(R.id.detailviewitem_imageview_content)
//            val postExplain = itemView.findViewById<TextView>(R.id.detailviewitem_explain_textview)
//            val likeCount = itemView.findViewById<TextView>(R.id.detailviewitem_favoritecounter_textview)
//
//            fun bind(){
//                val pos = adapterPosition
//
//                if(pos!=RecyclerView.NO_POSITION){
//                    itemView.setOnClickListener{
//                        itemClick?.onItemClick(itemView,contentDTOs[pos],pos)
//                    }
//                }
//            }
        }

        override fun getItemCount(): Int {
            return contentDTOs.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            var viewholder = (holder as CustomViewHolder).itemView
            //UserId
            viewholder.detailviewitem_profile_textview.text = contentDTOs!![position].userId
            //Image
            Glide.with(holder.itemView.context).load(contentDTOs!![position].imageUrl).into(viewholder.detailviewitem_imageview_content)
            //Explain of content
            viewholder.detailviewitem_explain_textview.text = contentDTOs!![position].explain
            //likes
            viewholder.detailviewitem_favoritecounter_textview.text = "Likes " + contentDTOs!![position].favoriteCount

            Glide.with(holder.itemView.context).load(contentDTOs!![position].imageUrl).into(viewholder.detailviewitem_imageview_content)

        }


    }
}