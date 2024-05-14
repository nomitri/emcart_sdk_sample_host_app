package com.nomitri.nomitrisampleapp.permissions

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nomitri.nomitrisampleapp.MainActivity
import com.nomitri.nomitrisampleapp.R
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog

class PermissionsActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    private val REQUEST_CODE_REQUIRED_PERMISSION = 101
    private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permissions)
        requestPermissions()
    }

    private fun requestPermissions() {
        EasyPermissions.requestPermissions(
            this, getString(R.string.permissions_missing_body),
            REQUEST_CODE_REQUIRED_PERMISSION, *REQUIRED_PERMISSIONS
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(this).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        if (hasAllPermissions()) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun hasAllPermissions(): Boolean {
        return EasyPermissions.hasPermissions(this, *REQUIRED_PERMISSIONS)
    }
}
