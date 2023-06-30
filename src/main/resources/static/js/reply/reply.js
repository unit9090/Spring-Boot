/**
 * 댓글 영역 보이기/숨기기 토글
 * 댓글 검색, 등록, 수정, 삭제
 */

document.addEventListener('DOMContentLoaded', () => {
	// 부트스트랩 Collapse 객체를 생성. 초기 상태는 화면에 보이지 않는 상태(옵션)
	const bsCollapse = new bootstrap.Collapse('div#replyToggleDiv', {toggle : false});
	
	// 토글 버튼을 찾고, 이벤트 리스너를 등록.
	const btnToggleReply = document.querySelector('#btnToggleReply');
	btnToggleReply.addEventListener('click', (e) => {
		bsCollapse.toggle();
		// console.log(e.target.innerText);
		if(e.target.innerText === '보이기') {
			e.target.innerText = '숨기기';
			
			// 댓글 목록 불러오기:
			getRepliesWithPostId();
			
		} else {
			e.target.innerText = '보이기'
		}
	});
	
	const deleteReply = (e) => {
		// console.log(e.target);
		
		const result = confirm('정말 삭제할까요?');
		if(!result) {
			return;
		}
		
		// 삭제할 댓글 아이디
		const id = e.target.getAttribute('data-id');
		
		// Ajax DELETE 방식 요청 주소
		const reqUrl = `/api/reply/${id}`;
		
		axios.delete(reqUrl)					// Ajax DELETE 요청을 보냄
		.then((res) => {						// 성공 응답일 때 실행할 콜백 등록
			console.log(res);
			
			// 댓글 목록 새로 고침
			getRepliesWithPostId();
		})
		.catch((err) => console.log(err));		// 실패 응답일 때 실행할 콜백 등록
		
	}
	
	const updateReply = (e) => {
		// console.log(e.target);
		const replyId = e.target.getAttribute('data-id');
		const textAreaId = `textarea#replyText_${replyId}`;
		// console.log(document.querySelector(textAreaId));
		
		// TODO: Ajax PUT 요청
		const result = confirm('수정할까요?');
		if(!result) {
			return;
		}
		
		const replyText = document.querySelector(textAreaId).value;
		if(replyText === '') {
			alert('수정할 댓글 내용을 입력하세요.');
			return;
		}
		const data = {replyId, replyText};	// 요청 데이터(수정할 댓글 내용)
		console.log(data);
		
		// Ajax PUT 방식 요청 주소
		const reqUrl = '/api/reply';
		
		axios.put(reqUrl, data)					// Ajax UPDATE 요청을 보냄
		.then((res) => {						// 성공 응답일 때 실행할 콜백 등록
			console.log(res);
			
			// 댓글 목록 새로 고침
			getRepliesWithPostId();
		})
		.catch((err) => console.log(err));		// 실패 응답일 때 실행할 콜백 등록
		
	}
	
	// 댓글 목록 업데이트
	const makeReplyElements = (data) => {
		// 댓글 개수를 배열(data)의 원소 개수로 댓글 개수 업데이트
		document.querySelector('span#replyCount').innerText = data.length;
		
		// 댓글 목록을 삽입할 div 요소를 찾음
		const replies = document.querySelector('div#replies');
		
		// div 안에 작성된 기존 내용은 삭제.
		replies.innerHTML = '';
		
		// div 안에 삽입할 HTML 코드 초기화.
		let htmlStr = '';
		for(let reply of data) {
			htmlStr += `
				<div class="col-12">
					<div class="card my-2 p-2">
						<div>
							<span class="d-none">${reply.id}</span>
							<span class="fw-bold">${reply.writer}</span>
						</div>
						<textarea id="replyText_${reply.id}">${reply.replyText}</textarea>
						<div class="mt-2">
							<button class="btn btnDelete btn-outline-danger" data-id="${reply.id}">삭제</button>
							<button class="btn btnModify btn-outline-primary" data-id="${reply.id}">수정</button>
						</div>
					</div>
				</div>
			`;
		}
		
		// 작성된 HTML 문자열을 div 요소의 innerHTML로 설정.
		replies.innerHTML = htmlStr;
		
		// 모든 댓글 삭제 버튼들에게 이벤트 리스너를 등록.
		const btnDeletes = document.querySelectorAll('button.btnDelete');
		for(let btn of btnDeletes) {
			btn.addEventListener('click', (e) => deleteReply(e));
		}
		
		// 모든 댓글 수정 버튼들에게 이벤트 리스너를 등록.
		const btnModifies = document.querySelectorAll('button.btnModify');
		for(let btn of btnModifies) {
			btn.addEventListener('click', updateReply);
		}
		
	}
	
	// 포스트 번호에 달려 있는 댓글 목록을 (Ajax 요청으로) 가져오는 함수:
	const getRepliesWithPostId = async () => {
		const postId = document.querySelector('input#id').value;	// 포스트 아이디(번호)
		const reqUrl = `/api/reply/all/${postId}`;	// Ajax 요청 주소
		
		// Ajax 요청을 보내고 응답을 기다림.
		// get -> select
		// post -> insert
		// put -> update
		// delete -> delete
		try {
			const response = await axios.get(reqUrl);
			// console.log(response);
			makeReplyElements(response.data);
			
		} catch(error) {
			console.log(error);
		}
		
	}
	
	// 댓글 등록 버튼을 찾고, 이벤트 리스너 등록.
	const btnReplyCreate = document.querySelector('button#btnReplyCreate');
	btnReplyCreate.addEventListener('click', () => {
		// 포스트 아이디 찾음.
		const postId = document.querySelector('input#id').value;
		// 댓글 내용 찾음.
		const replyText = document.querySelector('textarea#replyText').value;
		// TODO: 댓글 작성자는 admin. 나중에 로그인한 사용자 아이디
		const writer = 'admin';
		
		// alert(`postId : ${postId}, replyText : ${ replyText } , writer : ${ writer }`);
		
		// 댓글 내용이 비어 있으면 경고창을 보여주고 종료.
		if(replyText === '') {
			alert('댓글 내용을 입력하세요.');
			return;
		}
		
		// Ajax 요청에서 보낼 데이터.
		const data = {postId, replyText, writer};
		// Ajax 요청을 보낼 URL
		const reqUrl = '/api/reply';
		
		
		axios.post(reqUrl, data)	// Ajax POST 요청을 보냄.
		.then((res) => {			// 성공 응답(response)일 때 실행할 콜백 등록
			console.log(res);	
			
			// 댓글 목록 새로고침
			getRepliesWithPostId();
			// 댓글 입력한 내용을 지움.
			document.querySelector('textarea#replyText').value = '';
			
		})
		.catch((err) => 			// 실패(error)일 때 실행할 콜백 등록
			console.log(err)
		);
		
	});
	
	
});

/*

// js 설명

test1();	// 실행문

// 선언문이 됨.
// 실행문이 아니기 때문에 js가 실행되면서 맨 위로 올라가서 선언이 되어짐.
function test1() {
	console.log('test1');
}

test1();	// 실행문

// ============================================================

// 실행문
// 선언문이 아니기 때문에 위치 및 순서가 중요함.
test2();	// 이건 test2이란 함수가 만들어져 있지 않기 때문에 오류가 발생함.
const test2 = () => console.log('test2');
test2();	// 이건 가능함.


// ============================================================
// 화살표 함수와 function의 this는 다름.
// 뭔 차이지...?

*/