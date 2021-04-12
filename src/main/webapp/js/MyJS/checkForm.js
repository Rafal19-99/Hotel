function checkForm(form) {

    var inputs = form.getElementsByTagName('input');
    for (var i = 0; i < inputs.length; i++) {
        if(inputs[i].hasAttribute("required")){
            if(inputs[i].value == ""){
                return false;
            }
        }
    }
    return true;
};



function addTable(list, appendObj) {
       
    var columns = addAllColumnHeaders(list, appendObj);

    for (var i = 0; i < list.length; i++) {
        var row$ = $('<tr/>');
        for (var colIndex = 0; colIndex < columns.length; colIndex++) {
            var cellValue = list[i][columns[colIndex]];

            if (cellValue == null) {
                cellValue = "";
            }

            if (cellValue.constructor === Array)
            {
                $a = $('<td/>');
                row$.append($a);
                addTable(cellValue, $a);

            } else if (cellValue.constructor === Object)
            {

                var array = $.map(cellValue, function (value, index) {
                    return [value];
                });

                $a = $('<td/>');
                row$.append($a);
                addObject(array, $a);

            } else {
                row$.append($('<td/>').html(cellValue));
            }
        }
        appendObj.append(row$);
    }
}


function addObject(list, appendObj) {
    for (var i = 0; i < list.length; i++) {
        var row$ = $('<tr/>');

        var cellValue = list[i];

        if (cellValue == null) {
            cellValue = "";
        }

        if (cellValue.constructor === Array)
        {
            $a = $('<td/>');
            row$.append($a);
            addTable(cellValue, $a);

        } else if (cellValue.constructor === Object)
        {

            var array = $.map(cellValue, function (value, index) {
                return [value];
            });

            $a = $('<td/>');
            row$.append($a);
            addObject(array, $a);

        } else {
            row$.append($('<td/>').html(cellValue));
        }
        appendObj.append(row$);
    }
}

// Adds a header row to the table and returns the set of columns.
// Need to do union of keys from all records as some records may not contain
// all records
function addAllColumnHeaders(list, appendObj)
{
    var columnSet = [];
    var headerTr$ = $('<tr>');

    for (var i = 0; i < list.length; i++) {
        var rowHash = list[i];
        for (var key in rowHash) {
            if ($.inArray(key, columnSet) == -1) {
                columnSet.push(key);
                headerTr$.append($('<th/>').html(key));
            }
        }
    }
    
    appendObj.append(headerTr$);

    return columnSet;
}