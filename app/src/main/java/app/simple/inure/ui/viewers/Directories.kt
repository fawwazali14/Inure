package app.simple.inure.ui.viewers

import android.content.pm.ApplicationInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.simple.inure.R
import app.simple.inure.decorations.views.TypeFaceTextView
import app.simple.inure.extension.fragments.ScopedFragment

class Directories : ScopedFragment() {

    private lateinit var apkDir: TypeFaceTextView
    private lateinit var dataDir: TypeFaceTextView

    private lateinit var applicationInfo: ApplicationInfo

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_directories, container, false)

        apkDir = view.findViewById(R.id.sub_directory_base_package)
        dataDir = view.findViewById(R.id.sub_directory_data)

        applicationInfo = requireArguments().getParcelable("application_info")!!

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startPostponedEnterTransition()

        apkDir.text = applicationInfo.sourceDir
        dataDir.text = applicationInfo.dataDir
    }

    companion object {
        fun newInstance(applicationInfo: ApplicationInfo): Directories {
            val args = Bundle()
            args.putParcelable("application_info", applicationInfo)
            val fragment = Directories()
            fragment.arguments = args
            return fragment
        }
    }
}