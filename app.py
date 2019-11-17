from flask import Flask, request, render_template
import pickle
import numpy as np
from flask_restplus import Api,Resource
from flask_cors import CORS
import seaborn as se
import pandas as pd

app = Flask(__name__)   
CORS(app)
API_NAME = Api(app)
app.config['SECRET_KEY'] = 'disable the web security'
app.config['CORS_HEADERS'] = 'Content-Type'

class Predictheart(Resource):
    def get(self, vizdata):
        info = vizdata.split(",")
        age = int(info[0])
        sex = int(info[1])
        chest_pain_type = float(info[2])
        resting_blood_pressure = float(info[3])
        cholesterol = float(info[4])
        fasting_blood_sugar = float(info[5])
        rest_ecg = float(info[6])
        max_heart_rate_achieved = float(info[7])
        exercise_induced_angina = float(info[8])
        st_depression = float(info[9])
        st_slope = float(info[10])
        num_major_vessels = float(info[11])
        thalassemia = float(info[12])
        model = load_model()
        data = ([[age, sex, chest_pain_type, resting_blood_pressure,cholesterol,fasting_blood_sugar,rest_ecg,max_heart_rate_achieved,exercise_induced_angina,st_depression,st_slope, num_major_vessels,thalassemia]])
        col = ['age', 'sex', 'chest_pain_type', 'resting_blood_pressure','cholesterol','fasting_blood_sugar','rest_ecg','max_heart_rate_achieved','exercise_induced_angina','st_depression','st_slope', 'num_major_vessels','thalassemia']
        #data = np.array(data)
        data = pd.DataFrame(data,columns = col)
        data = data_scaling(data)
        data = model.predict(data)
        if data == 0:
            result = "No"
        else:
            result = "Yes"
        # return render_template('result.html', glycohemoglobin=abs(round(w_glyco)*10), glucose_serum=abs(round(w_gluco)*10), albumin_urine=abs(round(w_albumin)*10), pred=result)
        return {'isHeart': result}




API_NAME.add_resource(Predictheart, '/heartpredict/viz/<vizdata>', methods=['POST', 'GET'])

def load_model():
    """This function load the pickled model as API for flask
         :returns: loaded_model
         :rtype: float64
         method's invoked:  None"""
    loaded_model = pickle.load(open('rf_model.pkl', 'rb'))
    return loaded_model


def data_scaling(df):

    cont_data = df.values
    m = open('scaled_model.pkl', 'rb')
    model = pickle.load(m)
    cont_data = model.transform(cont_data)
    return cont_data


if __name__ == '__main__':
        app.run(host='localhost', port=5000, debug=True, threaded=True)
