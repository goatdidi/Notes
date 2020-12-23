package cn.edu.notes

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import cn.edu.notes.adapter.NotesAdapter
import cn.edu.notes.database.NotesDatabase
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.launch


class HomeFragment : BaseFragment() {


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
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    companion object {

        fun newInstance() =
            HomeFragment().apply {
                arguments = Bundle().apply {

                }
            }


}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager=StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        launch {
            context?.let {
                var notes=NotesDatabase.getDatabase(it).noteDao().getallNotes()
                recycler_view.adapter=NotesAdapter(notes)
            }
        }
        create_notes.setOnClickListener {
            replaceFragment(CreateNoteFragment.newInstance(),true)
        }
    }
    fun replaceFragment(fragement:Fragment,istranstion:Boolean){
        val fragementTransition = activity!!.supportFragmentManager.beginTransaction()
        if (istranstion){
            fragementTransition.setCustomAnimations(android.R.anim.slide_out_right,android.R.anim.slide_in_left)
        }
        fragementTransition.replace(R.id.frame_layout,fragement).addToBackStack(fragement.javaClass.simpleName).commit()
    }
    }

