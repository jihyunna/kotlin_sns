package com.example.snsproject.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.snsproject.R
import com.example.snsproject.navigation.model.AlarmDTO
import kotlinx.android.synthetic.main.item_comment.view.*
import kotlinx.android.synthetic.main.fragment_alarm.view.*

class AlarmFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_alarm,container,false)
        view.alarmfragment_recyclerview.adapter = AlarmRecyclerviewAdapter()
        view.alarmfragment_recyclerview.layoutManager = LinearLayoutManager(activity)
        return view
    }
    inner class AlarmRecyclerviewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> () {
        var alarmDTOList : ArrayList<AlarmDTO> = arrayListOf() // arraylist 초기화

        init {
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            FirebaseFirestore.getInstance().collection("alarms").whereEqualTo("destinationUid", uid).addSnapshotListener {
                    querySnapshot, firebaseFirestoreException -> alarmDTOList.clear() //array값 지운 다음 다시 담아주기

                if(querySnapshot == null) return@addSnapshotListener
                for(snapshot in querySnapshot.documents) {
                    alarmDTOList.add(snapshot.toObject(AlarmDTO::class.java)!!)
                    }//ararlmlist에 snapshot 값 넣어줌
                    notifyDataSetChanged()
            }
            }

            // 알람 저장하는 리스트 변수
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                var view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
                return CustomViewHolder(view)
            }

            inner class CustomViewHolder(view:View):RecyclerView.ViewHolder(view)

            override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
                var view = p0.itemView
                when(alarmDTOList[p1].kind) {
                    0 -> {
                        val str_0 = alarmDTOList[p1].userId + getString(R.string.alarm_favorite)
                        view.commentviewitem_textview_profile.text = str_0
                    }
                    1 -> {
                        val str_0 = alarmDTOList[p1].userId + " " + getString(R.string.alarm_comment) + " : " + alarmDTOList[p1].message
                        view.commentviewitem_textview_profile.text = str_0
                    }
                    2 -> {
                        val str_0 = alarmDTOList[p1].userId + " "+ getString(R.string.alarm_follow)
                        view.commentviewitem_textview_profile.text = str_0
                    }
                }

                FirebaseFirestore.getInstance().collection("profileImages").document(alarmDTOList[p1].uid!!).get().addOnCompleteListener {
                    task ->
                    if(task.isSuccessful) {
                        val url = task.result!!["image"]
                        Glide.with(view.context).load(url).apply(RequestOptions().circleCrop()).into(view.commentviewitem_imageview_profile)
                    }
                }
                view.commentviewitem_textview_comment.visibility = View.INVISIBLE
            }

            override fun getItemCount(): Int {
                return alarmDTOList.size
            }

        }

    }