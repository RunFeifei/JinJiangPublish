package com.uestc.jinjiang.publish.tab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.uestc.jinjiang.publish.R

/**
 * @author PengFeifei
 * @Description TODO
 * @date 2021/5/30
 */
class DemoObjectFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.activity_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (view.findViewById(R.id.textTitle) as TextView).text = System.currentTimeMillis().toString()
    }
}