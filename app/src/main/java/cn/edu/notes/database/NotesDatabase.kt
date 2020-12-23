package cn.edu.notes.database

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteOpenHelper
import cn.edu.notes.Entities.Notes
import cn.edu.notes.dao.NoteDao

@Database(entities = [Notes::class],version = 1,exportSchema = false)
abstract class NotesDatabase:RoomDatabase(){
    companion object{
        var notesDatabase:NotesDatabase?=null
        fun getDatabase(context: Context):NotesDatabase{
            if(notesDatabase!=null){
                notesDatabase=Room.databaseBuilder(
                    context
                    , NotesDatabase::class.java
                    , "notes.db"
                ).build()

            }
            return notesDatabase!!
        }
    }
    abstract fun noteDao():NoteDao
}