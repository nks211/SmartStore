package com.ssafy.smartstore_jetpack.src.main.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.config.BaseFragment
import com.ssafy.smartstore_jetpack.databinding.FragmentNoteBinding
import com.ssafy.smartstore_jetpack.dto.Note
import com.ssafy.smartstore_jetpack.src.main.MainActivityViewModel
import com.ssafy.smartstore_jetpack.util.CommonUtils
import com.ssafy.smartstore_jetpack.util.RetrofitUtil
import kotlinx.coroutines.launch

class NoteFragment: BaseFragment<FragmentNoteBinding>(FragmentNoteBinding::bind, R.layout.fragment_note) {
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var note: Note

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        note = activityViewModel.selectedNote.value!!

        binding.noteTitle.text = note.title
        binding.noteDate.text = CommonUtils.getFormattedString(note.orderTime)
        binding.noteSender.text = note.senderId
        binding.noteContent.text = note.content

        binding.confirm.setOnClickListener{
            Navigation.findNavController(requireView()).popBackStack()
        }
        binding.delete.setOnClickListener {
            lifecycleScope.launch{
                val bool = RetrofitUtil.noteService.delete(note.id)
                if(bool) activityViewModel.getNotes(note.receiverId)
            }
            Navigation.findNavController(requireView()).popBackStack()
        }
    }


}