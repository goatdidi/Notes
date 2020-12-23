package cn.edu.notes.dao

import androidx.room.*
import androidx.room.Dao
import androidx.room.Query
import cn.edu.notes.Entities.Notes

@Dao
interface NoteDao {
    //suspend该函数需要在协程中执行
    @get:Query("SELECT * FROM notes ORDER BY id DESC")
    val allNotes:List<Notes>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(note:Notes)

    @Delete
    suspend fun deleteNote(note:Notes)
}