/**
 * 
 */

// 유효성 검사를 위한 변수들
let isVaildId = false;
let isVaildPwd = false;
let isVaildEmail = false;
let isVaildPhone = false;

$(document).ready(function() {
	// 비밀번호 일치 검사
	var item = document.getElementById('labelPwdCheck');
	document.getElementById('inputPwdCheck').addEventListener('keyup', function() {
		var pwd = document.getElementById('inputPwd').value;
		var pwdCheck = this.value;
		if (pwd == pwdCheck) {
			item.textContent = '비밀번호가 일치합니다.';
			item.style.color = "blue";
			isVaildPwd = true;
		} else {
			item.textContent = '비밀번호가 일치하지않습니다.';
			item.style.color = "red";
			isVaildPwd = false;
		}
	});
	document.getElementById('inputPwd').addEventListener('keyup', function() {
		var pwdCheck = document.getElementById('inputPwdCheck').value;
		var pwd = this.value;
		if (pwd == pwdCheck) {
			item.textContent = '비밀번호가 일치합니다.';
			item.style.color = "blue";
			isVaildPwd = true;
		} else {
			item.textContent = '비밀번호가 일치하지않습니다.';
			item.style.color = "red";
			isVaildPwd = false;
		}

	});
	// 아이디 keyup
	document.getElementById('inputId').addEventListener('keyup', function() {
		isVaildId = false;
	});

	// 이메일 keyup
	document.getElementById('inputEmail').addEventListener('keyup', function() {
		isVaildEmail = false;
	});
	document.getElementById('inputEmailUrl').addEventListener('click', function() {
		isVaildEmail = false;
	});
	// 전화번호 keyup
	document.getElementById('inputPhone1').addEventListener('keyup', function() {
		if (document.getElementById('inputPhone1').value.length >= 3) {
			let tmp = document.getElementById('inputPhone1').value.substring(0, 3);
			document.getElementById('inputPhone1').value = tmp;
			document.getElementById('inputPhone2').focus();
		}
	});
	document.getElementById('inputPhone2').addEventListener('keyup', function() {
		if (document.getElementById('inputPhone2').value.length >= 4) {
			let tmp = document.getElementById('inputPhone2').value.substring(0, 4);
			document.getElementById('inputPhone2').value = tmp;
			document.getElementById('inputPhone3').focus();
		}
	});
	document.getElementById('inputPhone3').addEventListener('keyup', function() {
		if (document.getElementById('inputPhone3').value.length >= 4) {
			let tmp = document.getElementById('inputPhone3').value.substring(0, 4);
			document.getElementById('inputPhone3').value = tmp;
		}
	});


});

// 전화번호 중복 검사
function checkPhone(phone) {
	return $.ajax({
		url: "/member/checkPhone",
		data: { phone: phone },
		method: "get",
		success: function(result) {
			if (result == 'T') {
				isvalisVaildPhone = true;
			}
		}
	});
}

//아이디 중복확인
function checkId() {
	var id = document.getElementById('inputId').value;
	if (id == null || id == '') {
		swal('아이디 중복 검사 오류', "아이디를 입력하세요.", 'warning');
	} else if (id.length < 3 || id.length > 12) {
		swal('아이디 중복 검사 오류', "3~12글자 이내로 입력하세요.", 'warning');
	} else {
		$.ajax({
			url: "/member/checkId",
			data: { id: id },
			method: "get",
			success: function(result) {
				if (result == 'T') {
					swal('아이디 사용가능', "사용가능한 아이디입니다.", 'success');
					isVaildId = true;
				} else {
					swal('아이디 사용불가', "존재하는 아이디입니다.\n다른 아이디를 입력해주세요.", 'warning');
				}
			}
		});
	}
};

var authCode;
//이메일 중복확인
function checkEmail() {
	var email = document.getElementById('inputEmail').value + '@' + document.getElementById('inputEmailUrl').value;
	if (document.getElementById('inputEmail').value == null || document.getElementById('inputEmail').value == ""
		|| document.getElementById('inputEmailUrl').value == "none") {
		swal('이메일 입력 오류', "올바른 이메일을 입력하세요.", 'warning');
	} else {
		$.ajax({
			url: "/member/checkEmail",
			data: { email: email },
			success: function(msg) {
				if (msg == null || msg == "") {
					document.getElementById('divEmailCheck').style.display = 'block';
					document.getElementById("inputEmailCheck").disabled = false;
					$.ajax({
						url: "/member/isVaildEmail",
						data: { email: email },
						method: "get",
						success: function(code) {
							authCode = code;
							swal('이메일 전송 완료', '인증번호를 입력해주세요.', 'info');
						}
					})
				} else {
					swal('이메일 중복 오류', msg, 'warning');
				}
			}
		})
	}
};

//이메일 인증
function checkEmailCheck() {
	var inputCode = document.getElementById('inputEmailCheck').value;
	if (authCode == inputCode) {
		swal('이메일 인증 완료', '인증이 완료되었습니다.', 'success');
		isVaildEmail = true;
	} else {
		swal('이메일 인증 실패', '코드를 다시 확인해주세요.', 'warning');
		isVaildEmail = false;
	}
};
// 회원가입 버튼 눌렀을때
function submitForm() {
	var myForm = document.getElementById('form');
	document.getElementById("finalPhone").value =
		document.getElementById('inputPhone1').value + document.getElementById('inputPhone2').value + document.getElementById('inputPhone3').value;
	document.getElementById("finalEmail").value =
		document.getElementById('inputEmail').value + '@' + document.getElementById('inputEmailUrl').value;
	// 변수들의 값을 확인하여 모두 true인 경우에만 폼을 전송
	if (!isVaildId) {
		swal('회원가입 실패', '아이디 중복검사를 해주세요.', 'warning');
	} else if (!isVaildPwd) {
		swal('회원가입 실패', '동일한 비밀번호를 입력해주세요.', 'warning');
	} else if(document.getElementById('inputName').value == ""){
		swal('회원가입 실패', '이름을 입력 해주세요.', 'warning');
	} else if (!isVaildEmail) {
		swal('회원가입 실패', '이메일 인증을 해주세요.', 'warning');
	} else {
		$("#form").submit();
	}
};
