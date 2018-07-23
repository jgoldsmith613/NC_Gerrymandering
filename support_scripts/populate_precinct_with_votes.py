import requests, json, csv

with open('../support_files/results_sort_20161108.txt') as txt_voting_data:
    voting_data = csv.DictReader(txt_voting_data, delimiter='\t')
    precinct_voting_result = {}
    for row in voting_data:
        if row['contest_name'] == 'US PRESIDENT':
            if precinct_voting_result.get(row['precinct_code']) is None:
                precinct_voting_result[row['precinct_code']] = {'label': row['precinct_code']}
            if row['candidate_name'] == 'Hillary Clinton':
                DEM_COUNT = int(row['votes'])
                precinct_voting_result[row['precinct_code']]['DEM_COUNT'] = DEM_COUNT
            if row['candidate_name'] == 'Donald J. Trump':
                REP_COUNT = int(row['votes'])
                precinct_voting_result[row['precinct_code']]['REP_COUNT'] = REP_COUNT


with open('../support_files/nc_precinct_with_zip.csv') as precinct_data_csv:
    precinct_data = csv.DictReader(precinct_data_csv)
    map_precinct_data = {}
    for row in precinct_data:
        map_precinct_data[row['label']] = row



i = 0
for key in precinct_voting_result:
    print key
    entry = precinct_voting_result[key]
    if entry.get('REP_COUNT') is not None and key != 'PROVISIONAL' and key != 'MISC':
       data = map_precinct_data[key]

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
