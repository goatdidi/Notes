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
import pub.devrel.easypermissions.EasyPermissions
import java.text.SimpleDateFormat
import java.util.*
import android.Manifest
import android.app.Activity.RESULT_OK
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.util.Patterns
import pub.devrel.easypermissions.AppSettingsDialog
import java.lang.Exception


class CreateNoteFragment : BaseFragment() ,EasyPermissions.PermissionCallbacks,EasyPermissions.RationaleCallbacks{
    var currencDate:String?=null
    private var WRITE_STORAGE_PERM=123
    private var READ_STORAGE_PERM=123
    private var REQUEST_CODE_IMAGE=456
    private var selectedImagePath = ""
    private var webLink = ""
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
        btnOk.setOnClickListener {
            if (etWebLink.text.toString().trim().isNotEmpty()){
                checkWebUrl()
            }else{
                Toast.makeText(requireContext(),"需要外部链接",Toast.LENGTH_SHORT).show()
            }
        }
        btnCancel.setOnClickListener {
            layoutWebUrl.visibility=View.GONE // 设置隐藏链接
        }
        tvWebLink.setOnClickListener {
            var intent = Intent(Intent.ACTION_VIEW,Uri.parse(etWebLink.text.toString()))
            startActivity(intent)
        }
    }
//    fun replaceFragment(fragement:Fragment,istranstion:Boolean){
//        val fragementTransition = activity!!.supportFragmentManager.beginTransaction()
//        if (istranstion){
//            fragementTransition.setCustomAnimations(android.R.anim.slide_out_right,android.R.anim.slide_in_left)
//        }
//        fragementTransition.replace(R.id.frame_layout,fragement).addToBackStack(fragement.javaClass.simpleName).commit()
//    }
    private fun saveNote(){
        if (etNoteTitle.text.isNullOrEmpty()){
            Toast.makeText(context,"需要标题",Toast.LENGTH_SHORT).show()
        }
        if (etNoteSubTitle.text.isNullOrEmpty()){
            Toast.makeText(context,"需要副标题",Toast.LENGTH_SHORT).show()
        }
        if(etNoteDesc.text.isNullOrEmpty()){
            Toast.makeText(context,"需要正文",Toast.LENGTH_SHORT).show()
        } else{
        launch {
            var notes =Notes()
            notes.title=etNoteTitle.text.toString()
            notes.subTitle=etNoteSubTitle.text.toString()
            notes.noteText=etNoteDesc.text.toString()
            notes.dateTime=currencDate
            notes.color=selectedColor
            notes.imgPath=selectedImagePath
            notes.webLink=webLink
            context?.let {
                NotesDatabase.getDatabase(it).noteDao().insertNotes(notes)
                etNoteTitle.setText("")
                etNoteSubTitle.setText("")
                etNoteDesc.setText("")
                imgNote.visibility=View.GONE
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
        }

    }
    private val BroadcastReceiver:BroadcastReceiver = object :BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            var action = intent!!.getStringExtra("action")
            when(action!!){
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
                "Image" ->{
                    readStorageTask()
                    layoutWebUrl.visibility = View.GONE
                }
                "WebUrl" ->{
                    //展示外部链接
                    layoutWebUrl.visibility = View.VISIBLE
                }
                else ->{
                    imgNote.visibility = View.GONE
                    layoutWebUrl.visibility = View.GONE
                    selectedColor = intent.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }
            }
        }
    }
    override fun onDestroy() {
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(BroadcastReceiver)
        super.onDestroy()
    }
    private fun hasReadStoragePerm():Boolean{
        return EasyPermissions.hasPermissions(requireContext(),Manifest.permission.READ_EXTERNAL_STORAGE)
    }
    private fun hasWriteStoragePerm():Boolean{
        return EasyPermissions.hasPermissions(requireContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }
    private fun readStorageTask(){
        if (hasReadStoragePerm()){
            pickImageFromGallery()
        }else{
            EasyPermissions.requestPermissions(
                requireActivity(),
                getString(R.string.storage_permission_text),
                READ_STORAGE_PERM,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }
    private fun pickImageFromGallery() {
        var intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent,REQUEST_CODE_IMAGE)
        //调用if 发现打开不了相册QAQ
//        if (intent.resolveActivity(requireActivity().packageManager) != null){
//            startActivityForResult(intent,REQUEST_CODE_IMAGE)
//        }
    }

    private fun getPathFromUri(contentUri: Uri): String? {
        var filePath:String? = null
        var cursor = requireActivity().contentResolver.query(contentUri,null,null,null,null)
        if (cursor == null){
            filePath = contentUri.path
        }else{
            cursor.moveToFirst()
            var index = cursor.getColumnIndex("_data")
            filePath = cursor.getString(index)
            cursor.close()
        }
        return filePath
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==REQUEST_CODE_IMAGE&&resultCode ==RESULT_OK){
            if(data!=null){
                var selectedImageUrl=data.data
                if(selectedImageUrl!=null){
                    try {
                        var inputStream = requireActivity().contentResolver.openInputStream(selectedImageUrl)
                        var bitmap=BitmapFactory.decodeStream(inputStream)
                        imgNote.setImageBitmap(bitmap)
                        imgNote.visibility=View.VISIBLE
                        selectedImagePath=getPathFromUri(selectedImageUrl)!!
                    }catch (e:Exception){
                        Toast.makeText(requireContext(),e.message,Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun checkWebUrl(){
        if (Patterns.WEB_URL.matcher(etWebLink.text.toString()).matches()){
            layoutWebUrl.visibility = View.GONE
            etWebLink.isEnabled = false
            webLink = etWebLink.text.toString()
            tvWebLink.visibility = View.VISIBLE //设置可见
            tvWebLink.text = etWebLink.text.toString()
        }else{
            Toast.makeText(requireContext(),"Url is not valid",Toast.LENGTH_SHORT).show()
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,requireActivity())
    }

    override fun onPermissionsGranted(p0: Int, p1: MutableList<String>) {

    }

    override fun onPermissionsDenied(p0: Int, p1: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(requireActivity(),p1)){
            AppSettingsDialog.Builder(requireActivity()).build().show()
        }
    }

    override fun onRationaleAccepted(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onRationaleDenied(p0: Int) {
        TODO("Not yet implemented")
    }
}
