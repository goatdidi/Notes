package cn.edu.notes.dao

import androidx.room.*
import androidx.room.Dao
import androidx.room.Query
import cn.edu.notes.Entities.Notes

@Dao
interface NoteDao {
    //suspend该函数需要在协程中执行
    @Query("SELECT * FROM notes ORDER BY id DESC")
    suspend fun getallNotes() : List<Notes>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(note:Notes)

    @Delete
    suspend fun deleteNote(note:Notes)
}