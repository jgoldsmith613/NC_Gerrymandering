import requests, json, csv

with open('../support_files/precinct_sort_statewide_at_large_contests_no_admin_precincts_20161108_no_BOM.txt') as txt_voting_data:
    voting_data = csv.DictReader(txt_voting_data, delimiter='\t')
    precinct_voting_result = {}
    for row in voting_data:
        row_tuple = (row['Precinct'], row['County'])
        if row['Contest Name'] == 'US PRESIDENT':
            if precinct_voting_result.get(row_tuple) is None:
                precinct_voting_result[row_tuple] = {}
            if row['Choice'] == 'Hillary Clinton':
                DEM_COUNT = int(row['Total Votes'])
                precinct_voting_result[row_tuple]['DEM_COUNT'] = DEM_COUNT
            if row['Choice'] == 'Donald J. Trump':
                REP_COUNT = int(row['Total Votes'])
                precinct_voting_result[row_tuple]['REP_COUNT'] = REP_COUNT


with open('../support_files/nc_precinct_with_zip_name_modified.csv') as precinct_data_csv:
    precinct_data = csv.DictReader(precinct_data_csv)
    map_precinct_data = {}
    for row in precinct_data:
        row_tuple = (row['name'], row['county'])
        map_precinct_data[row_tuple] = row

with open('../support_files/best_30.csv') as district_results_csv:
    district_data = csv.DictReader(district_results_csv)
    map_district_results = {}
    for row in district_data:
        map_district_results[row['zip_code']] = row['district']


district_results = {}
for key in precinct_voting_result:
    votes = precinct_voting_result[key]
    precinct_zip = map_precinct_data[key]['zip_code']
    district = map_district_results[precinct_zip]
    if district_results.get(district) is None:
        district_results[district] = {'DEM_COUNT': 0, 'REP_COUNT': 0}
    district_result = district_results[district]
    district_result['DEM_COUNT'] = district_result['DEM_COUNT'] + votes['DEM_COUNT']
    district_result['REP_COUNT'] = district_result['REP_COUNT'] + votes['REP_COUNT']


dem = 0
rep = 0
for district in district_results:
    result = district_results[district]
    if result['DEM_COUNT'] == result['REP_COUNT']:
        print 'this is basically impossible'
    if result['DEM_COUNT'] > result['REP_COUNT']:
        print 'Democrats win district {}: {} to {}'.format(district, result['DEM_COUNT'], result['REP_COUNT'])
        dem+=1
    else:
        print 'Republicans win district {}:  {} to {}'.format(district, result['REP_COUNT'], result['DEM_COUNT'])
        rep+=1

print 'Democrats win {} seats'.format(dem)
print 'Republicans win {} seats'.format(rep)


# votes = precinct_voting_result[row['label']]
# row['dem_votes'] = votes['DEM_COUNT']
# row['rep_votes'] = votes['REP_COUNT']
# writable_precinct_data.append(row)


#with open('../support_files/nc_precinct_with_zip_and_votes.csv', 'w') as output_file:
#    fieldnames = ['county', 'county_id', 'label', 'polling_place_id', 'zip_code', 'dem_votes', 'rep_votes']
#    writer = csv.DictWriter(output_file, fieldnames=fieldnames)
#    writer.writeheader()
#    for entry in writable_precinct_data:
#        writer.writerow(entry) 
