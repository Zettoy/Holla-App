/*
*
*  Run this script on a Facebook page.
*  E.g. https://www.facebook.com/UNSWRoundhouse/
*  Make sure the page has finished loading first.
*
* */
(function scrape() {
    function getRandomInt(min, max) {
        return Math.floor(Math.random() * (max - min + 1)) + min;
    }
    var NUM_POSTS_TO_SCRAPE = 3;

    var posts = document.querySelectorAll(".userContentWrapper");
    var to_exfil = [];
    posts.forEach(function (post, idx) {
        if (idx < NUM_POSTS_TO_SCRAPE) {
            // if(idx === 1){
            // console.log(post)
            var see_more = post.querySelector(".see_more_link_inner");
            if (see_more !== null) {
                see_more.click();
            }
            var timestamp = post.querySelector("abbr");

            var unix_ts = timestamp.getAttribute("data-utime");
            // console.log(timestamp, unix_ts);
            setTimeout(
                function () {
                    var content = post.querySelector(".userContent");
                    var post_exfil_obj = {
                        "id": getRandomInt(10**6,10**10),
                        "location_name": "nowhere",
                        "coordinates": {
                            "latitude": 0,
                            "longitude": 0
                        },
                        "author": "UNSWRoundhouse",
                        "content": content.innerText,
                        "created_at": unix_ts
                    }
                    // console.log(JSON.stringify(post_exfil_obj));
                    // console.log(post_exfil_obj);
                    to_exfil.push(post_exfil_obj);
                }, 1000
            );
        }
    })
    setTimeout(function () {
        // console.log(to_exfil);
        console.log(JSON.stringify({"posts":to_exfil}));
    }, 5000);
    /*
    - timestamp
    - username
    - content (incl emoji)
    - location_name
    - coordinates
     */
})();