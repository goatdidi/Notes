package cn.edu.notes

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import cn.edu.notes.Entities.Notes
import cn.edu.notes.database.NotesDatabase
import cn.edu.notes.util.NoteBottomSheetFragment
import kotlinx.android.synthetic.main.fragment_create_note.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class CreateNoteFragment : BaseFragment() {
    var currencDate:String?=null
    var selectedColor = "#171C26"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_note, container, false)
    }


    companion object {

        fun newInstance() =
            CreateNoteFragment().apply {
                arguments = Bundle().apply {

                }
            }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            BroadcastReceiver, IntentFilter("bottom_sheet_action")
        )
        val sdformat = SimpleDateFormat("dd/m/yyyy hh:mm:ss")
        val currentData = sdformat.format(Date())
        colorView.setBackgroundColor(Color.parseColor(selectedColor))
        tvDateTime.text=currentData
        imgDone.setOnClickListener {
            saveNote()
        }
        imgBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        imgMore.setOnClickListener {
            var noteBottomSheetFragment = NoteBottomSheetFragment.newInstance()
            noteBottomSheetFragment.show(requireActivity().supportFragmentManager,"Note Bottom Sheet Fragment")
        }
    }
    fun replaceFragment(fragement:Fragment,istranstion:Boolean){
        val fragementTransition = activity!!.supportFragmentManager.beginTransaction()
        if (istranstion){
            fragementTransition.setCustomAnimations(android.R.anim.slide_out_right,android.R.anim.slide_in_left)
        }
        fragementTransition.replace(R.id.frame_layout,fragement).addToBackStack(fragement.javaClass.simpleName).commit()
    }
    private fun saveNote(){
        if (etNoteTitle.text.isNullOrEmpty()){
            Toast.makeText(context,"需要标题",Toast.LENGTH_SHORT).show()
        }
        if (etNoteSubTitle.text.isNullOrEmpty()){
            Toast.makeText(context,"需要副标题",Toast.LENGTH_SHORT).show()
        }
        if(etNoteDesc.text.isNullOrEmpty()){
            Toast.makeText(context,"需要正文",Toast.LENGTH_SHORT).show()
        }
        launch {
            var notes =Notes()
            notes.title=etNoteTitle.text.toString()
            notes.subTitle=etNoteSubTitle.text.toString()
            notes.noteText=etNoteDesc.text.toString()
            notes.dateTime=currencDate
            notes.color=selectedColor
            context?.let {
                NotesDatabase.getDatabase(it).noteDao().insertNotes(notes)
                etNoteTitle.setText("")
                etNoteSubTitle.setText("")
                etNoteDesc.setText("")
            }
        }

    }
    private val BroadcastReceiver:BroadcastReceiver = object :BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            var actionColor = intent!!.getStringExtra("actionColor")
            when(actionColor!!){
                "Blue" ->{
                    selectedColor = intent.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }
                "Yellow" ->{
                    selectedColor = intent.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }
                "Pink" ->{
                    selectedColor = intent.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }
                "Green" ->{
                    selectedColor = intent.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }
                "Orange" ->{
                    selectedColor = intent.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }
                "Black" ->{
                    selectedColor = intent.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }
                else ->{
                    selectedColor = intent.getStringExtra("selectedColor")!!
                }
            }
        }
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(BroadcastReceiver)
        super.onDestroy()
    }
}
