package com.doncaruso.doncaruso;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FragmentInicio extends Fragment implements AdapterInicio.EscuchaEventosClick{

    private List<Menu> menuList;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private RequestQueue requestQueue;
    private int requestCount = 1;

    public FragmentInicio() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_inicio, container, false);

        recyclerView = (RecyclerView)v.findViewById(R.id.reciclador_inicio);

        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(layoutManager);

        //Initializing our superheroes list
        menuList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getActivity());

        //Calling method to get data to fetch data
        ObtenerDatos();
        //initializing our adapter
        adapter = new AdapterInicio(menuList, getActivity(), this);

        recyclerView.setAdapter(adapter);

        return v;
    }

    private JsonArrayRequest ObtenerDatosServidor(int requestCount) {


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL.Menus,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        AnalaizarDatos(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getActivity(), "No se ha podido entablar conexión", Toast.LENGTH_SHORT).show();
                    }
                });
        return jsonArrayRequest;
    }
    private void ObtenerDatos() {
        requestQueue.add(ObtenerDatosServidor(requestCount));
        requestCount++;
    }
    private void AnalaizarDatos(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            Menu prom = new Menu();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                prom.setId(json.getString(URL.TAG_ID));
                prom.setNombre(json.getString(URL.TAG_NOMBRE));
                prom.setImagen(json.getString(URL.TAG_IMG));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            menuList.add(prom);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterInicio.ViewHolder holder, int posicion) {

    }
}