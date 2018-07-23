import requests, json, csv
from bs4 import BeautifulSoup

with open('../support_files/county_data.csv') as csv_county:
    county_reader = csv.DictReader(csv_county)
    precinct_data = []
    for row in county_reader:
        session = requests.Session()
        payload = {'CountyId': row['id']}
        responce = session.post('https://vt.ncsbe.gov/PPLkup/LoadPrecincts/', data=payload)
        json_responce = json.loads(responce.text)
        for precinct in json_responce:
            if precinct['Label'] != '':
                polling_info_responce = session.get('https://vt.ncsbe.gov/PPLkup/PollingPlaceResult/?CountyID={}&PollingPlaceID={}'.format(precinct['CountyID'], precinct['PollingPlaceID']))
                soup = BeautifulSoup(polling_info_responce.text, 'html.parser')
                link = soup.find('div', {'id': 'divPollingPlace'}).find('a').find('br')
                zipcode = str(link.nextSibling).strip()[-5:]
                precinct_data.append({'county': row['county'] , 'county_id' :  precinct['CountyID'], 'label': precinct['Label'], 'polling_place_id': precinct['PollingPlaceID'] , 'name': precinct['Description'], 'zip_code': zipcode})
        print 'finished county {}'.format(row['county'])


with open('../support_files/nc_precinct_with_zip_name.csv', 'w') as output_file:
    fieldnames = ['county', 'county_id', 'label', 'polling_place_id', 'name', 'zip_code']
    writer = csv.DictWriter(output_file, fieldnames=fieldnames)
    writer.writeheader()
    for entry in precinct_data:
        writer.writerow(entry)






                
