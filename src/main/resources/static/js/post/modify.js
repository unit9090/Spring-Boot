/**
 * 포스트 업데이트 & 삭제
 */

document.addEventListener('DOMContentLoaded', () => {
	
	// <form> 요소를 찾음
	const postModifyForm = document.querySelector('#postModifyForm');
	
    const btnUpdate = document.querySelector('#btnUpdate');
    btnUpdate.addEventListener('click', (e) => {
        const title = document.querySelector('#title').value;
        const content = document.querySelector('#content').value;
        
        if(title === 0 || content === 0) {
			alert('제목과 내용은 반드시 입력....');
			return;	
		}
		
		let result = confirm('변경된 내용을 업데이트 할까요?');
		if(!result) {
			return;
		}
        
        postModifyForm.action = '/post/update'
        postModifyForm.method = 'post';
        postModifyForm.submit();
    });
    
    const btnDelete = document.querySelector('#btnDelete');
    btnDelete.addEventListener('click', (e) => {
        let result = confirm('정말 삭제할까요?');
        if(!result) {
			return;
		}
        
        // submit 요청 주소
        postModifyForm.action = `/post/delete`;
        // submit 요청 방식
        postModifyForm.method = 'post';
        // 폼 제출(submit), 요청 보내기.
        postModifyForm.submit();
    });
});
