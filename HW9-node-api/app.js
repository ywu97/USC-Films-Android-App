//route file for express api
const express = require('express');
const app = express();
var cors = require('cors');
var request = require('request');
const util = require('util');
const { response } = require('express');
const { get } = require('request');
const API_KEY = "Your_TMDB_API_KEY";
const PORT = process.env.port || 8080;
const getPromise = util.promisify(request);

app.use(cors());

//Homepage
var getCurrentMovies = {
    'url' :'https://api.themoviedb.org/3/movie/now_playing?api_key=' + API_KEY + '&language=en-US&page=1'
};
app.get('/currentMovies',async(req, res) =>{
    request(getCurrentMovies, function(error, response, body){
        if(!error && response.statusCode == 200){
            res.send(body);
        }
    })
});

var getPopularMovies = {
    'url': 'https://api.themoviedb.org/3/movie/popular?api_key=' + API_KEY + '&language=en-US&page=1'
}
app.get('/popularMovies',async(req, res) =>{
    request(getPopularMovies, function(error, response, body){
        if(!error && response.statusCode == 200){
            res.send(body);
        }
    })
});

var getTopRatedMovies = {
    'url': 'https://api.themoviedb.org/3/movie/top_rated?api_key=' + API_KEY + '&language=en-US&page=1'
};
app.get('/topRatedMovies',async(req, res) =>{
    request(getTopRatedMovies, function(error, response, body){
        if(!error && response.statusCode == 200){
            res.send(body);
        }
    })
});



var getPopularTvShows = {
    'url': 'https://api.themoviedb.org/3/tv/popular?api_key=' + API_KEY + '&language=en-US&page=1'
};
app.get('/popularTvshows', async(req, res) =>{
    request(getPopularTvShows, function(error, response, body){
        if(!error && response.statusCode == 200){
            res.send(body);
        }
    })
});

var getTopRatedTvShows = {
    'url': 'https://api.themoviedb.org/3/tv/top_rated?api_key=' + API_KEY + '&language=en-US&page=1',
};
app.get('/topRatedTvShows',async(req, res) => {
    request(getTopRatedTvShows, function(error, response, body){
        if(!error && response.statusCode == 200){
            res.send(body);
        }
    })

});

var getTrendingTvShows = {
    'url': 'https://api.themoviedb.org/3/trending/tv/day?api_key=' + API_KEY,
}
app.get('/trendingTvShows', async(req, res) => {
    request(getTrendingTvShows, function(error, response, body){
        if(!error && response.statusCode == 200){
            res.send(body);
        }
    })
});


app.get('/Video', async(req, res) => {
    id = req.query.id;
    category = req.query.category;
    var getVideo = 'https://api.themoviedb.org/3/'+category+'/'+ id +'/videos?api_key=' + API_KEY + '&language=en-US&page=1';
    request(getVideo, function(error, response, body){
        if(!error && response.statusCode == 200){
            res.send(body);
        }
    })
});


app.get('/Details', async(req, res) =>{
    id = req.query.id;
    category = req.query.category;
    var getDetails = 'https://api.themoviedb.org/3/'+category+'/' + id + '?api_key='+ API_KEY + '&language=en-US&page=1';
    request(getDetails, function(error, response, body){
        if(!error && response.statusCode == 200){
            res.send(body);
        }
    })
});

app.get('/Cast',async(req, res) =>{
    id = req.query.id;
    category = req.query.category;
    var getCast = 'https://api.themoviedb.org/3/'+category+'/' + id + '/credits?api_key='+ API_KEY +'&language=en-US&page=1';
    request(getCast, function(error, response, body){
        if(!error && response.statusCode == 200){
            res.send(body);
        }
    })
});

app.get('/Reviews',async(req, res) =>{
    id = req.query.id;
    category = req.query.category;
    var getReviews = 'https://api.themoviedb.org/3/'+category+'/' + id + '/reviews?api_key='+ API_KEY +'&language=en-US&page=1';
    request(getReviews, function(error, response, body){
        if(!error && response.statusCode == 200){
            res.send(body);
        }
    })
})


app.get('/Recommended', async(req, res) => {
    id = req.query.id;
    category = req.query.category;
    var getRecommended = 'https://api.themoviedb.org/3/'+category+'/' + id + '/recommendations?api_key='+ API_KEY +'&language=en-US&page=1';
    request(getRecommended, function(error, response, body){
        if(!error && response.statusCode == 200){
            res.send(body);
        }
    })
})


app.get('/getSearchResult', async(req, res) => {
    var queryInput = req.query.queryInput;
    var url = 'https://api.themoviedb.org/3/search/multi?api_key='+API_KEY+'&language=en-US&query=' + queryInput;
    request(url, function (error, response, body) {
        if (!error && response.statusCode == 200) {
            res.send(body);
        }
    })
})
 

app.listen(PORT, (req, res) =>{
    console.log('Express app.js is running at port 8080');
});
module.exports = app;