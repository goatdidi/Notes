package cn.edu.notes

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import cn.edu.notes.Entities.Notes
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
                var adapter=NotesAdapter(notes)
                recycler_view.adapter=adapter
                search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(p0: String?): Boolean {
                        //adapter.filter.filter(p0)
                        return true
                    }

                    override fun onQueryTextChange(p0: String?): Boolean {
                        adapter.filter.filter(p0)
                        return true
                    }
                })
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

//    override fun onItemClick(note: Notes) {
//        TODO("Not yet implemented")
//    }
}

