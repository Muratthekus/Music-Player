package com.malikane.mussic.UI

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.malikane.mussic.R

class ShareBottomSheetDialog : BottomSheetDialogFragment(){

	lateinit var clickListener:ClickListener
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		val v:View=inflater.inflate(R.layout.share_bottom_sheet_dialog,container,false)
		val share: Button =v.findViewById(R.id.sharebutton)
		share.setOnClickListener {
			clickListener.onButtonClicked()
			dismiss()
		}
		return v
	}
	interface ClickListener{
		fun onButtonClicked()
	}
	fun setShareButtonClickListener(clickListener:ClickListener){
		this.clickListener=clickListener
	}

}