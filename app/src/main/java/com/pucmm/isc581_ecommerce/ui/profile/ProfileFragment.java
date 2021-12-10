package com.pucmm.isc581_ecommerce.ui.profile;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.fragment.app.Fragment;

        import android.content.Intent;
        import android.os.Bundle;
        import android.text.TextUtils;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.bumptech.glide.Glide;
        import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
        import com.google.firebase.auth.FirebaseAuth;
        import com.pucmm.isc581_ecommerce.R;
        import com.pucmm.isc581_ecommerce.activities.LoginActivity;
        import com.pucmm.isc581_ecommerce.activities.MainActivity;
        import com.pucmm.isc581_ecommerce.firebaseHandlers.dbHelpers.UsersDB;
        import com.pucmm.isc581_ecommerce.models.User;
        import com.pucmm.isc581_ecommerce.sessions.UserSession;

        import java.util.Objects;

public class ProfileFragment extends Fragment {

    private ImageView profilePic;
    private TextView name;
    private TextView username;
    private TextView email;
    private TextView phone;
    private TextView role;
    private ExtendedFloatingActionButton logout;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_profile_view, container, false);

        UserSession session = new UserSession(requireActivity().getApplicationContext());

        profilePic = root.findViewById(R.id.profile_image);
        name = root.findViewById(R.id.tv_profile_user);
        username = root.findViewById(R.id.tv_profile_username);
        email = root.findViewById(R.id.tv_profile_email);
        phone = root.findViewById(R.id.tv_profile_phone);
        role = root.findViewById(R.id.tv_profile_role);
        logout = root.findViewById(R.id.logout_btn);


        String imageUrl = session.getImage();

        if ( !TextUtils.isEmpty(imageUrl) ) {
            Glide.with(this).load(imageUrl).into(profilePic);
        }

        name.setText( session.getName() );
        username.setText( session.getLastName() );
        email.setText( session.getEmail() );
        phone.setText( session.getContact() );
        role.setText( session.getRol() );

        logout.setOnClickListener(v-> {
            session.logoutUser();
        });
        return root;
    }
}